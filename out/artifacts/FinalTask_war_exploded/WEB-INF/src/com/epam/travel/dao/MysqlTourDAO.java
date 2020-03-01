/*
 * MySQL tour DAO
 *
 * Version 2.0
 *
 * Date 18 Jan 2020
 */


package com.epam.travel.dao;

import com.epam.travel.managers.ConfigurationManager;
import com.epam.travel.managers.ConnectionManager;
import com.epam.travel.travelagency.Tour;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * MySQL tour DAO
 *
 * @version 2.0 18 Jan 2020
 * @author Ilya Kuliashou
 */
public class MysqlTourDAO implements TourDAO {
    /**
     * The name of the property containing
     * SQL command to insert tour.
     */
    private static final String INSERT_TOUR = "SQL_INSERT_TOUR";
    /**
     * The name of the property containing
     * SQL command to delete tour.
     */
    private static final String DELETE_TOUR = "SQL_DELETE_TOUR";
    /**
     * The name of the property containing
     * SQL command to update tour.
     */
    private static final String UPDATE_TOUR = "SQL_UPDATE_TOUR";
    /**
     * The name of the property containing
     * SQL command to remove tour links tour.
     */
    private static final String REMOVE_CUSTOMER_TOUR = "SQL_REMOVE_CUSTOMER_TOUR";
    /**
     * The name of the property containing
     * SQL command to find tour by id.
     */
    private static final String FIND_TOUR_BY_ID = "SQL_FIND_TOUR_BY_ID";
    /**
     * The name of the property containing
     * SQL command to find tour by name.
     */
    private static final String FIND_TOUR_BY_KEYWORD = "SQL_FIND_TOUR_BY_KEYWORD";
    /**
     * The name of the property containing
     * SQL command to select last-minute tours.
     */
    private static final String SELECT_LAST_MINUTE_TOURS =
            "SQL_SELECT_LAST_MINUTE_TOURS";
    /**
     * The name of the property containing
     * SQL command to select customer tours.
     */
    private static final String SELECT_CUSTOMER_TOURS =
            "SQL_SELECT_CUSTOMER_TOURS";
    /**
     * The name of the property containing
     * SQL command to select all tours.
     */
    private static final String SELECT_ALL_TOURS =
            "SQL_SELECT_ALL_TOURS";

    /**
     * Logger.
     */
    private static Logger log = LogManager.getLogger(MysqlTourDAO.class);

    /**
     * Closing a result set.
     * @param rs result set.
     */
    private void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                log.error(e);
            }
        }
    }

    /**
     * Closing a prepared statement.
     * @param ps prepared statement.
     */
    private void closePreparedStatement(PreparedStatement ps) {
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                log.error(e);
            }
        }
    }

    /**
     * Creates a new tour.
     *
     * @param resultSet query result.
     * @return tour.
     * @throws SQLException An exception that provides information on
     * a database access error or other errors.
     */
    private Tour createTour(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt(1);
        String name = resultSet.getString(2);
        String city = resultSet.getString(3);
        int typeID = resultSet.getInt(4);
        int price = resultSet.getInt(5);
        Date checkInDate = resultSet.getDate(6);
        boolean isLastMinuteTour = resultSet.getBoolean(7);
        String description = resultSet.getString(8);
        String type = "";

        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.MYSQL);
        if (daoFactory != null) {
            type = daoFactory.getTourTypeDAO().findTourTypeByID(typeID);
        }

        Tour tour = new Tour(name, city, type, price, checkInDate);
        tour.setId(id);
        tour.setLastMinutePackage(isLastMinuteTour);
        tour.setDescription(description);

        return tour;
    }

    @Override
    public int insertTour(Tour tour) {
        ConnectionManager connectionManager = ConnectionManager.getInstance();
        Connection connection = connectionManager.getConnection();
        String sql = ConfigurationManager.getInstance()
                .getProperty(INSERT_TOUR);

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int id = -1;
        int typeID = -1;

        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.MYSQL);
        if (daoFactory != null) {
            typeID = daoFactory.getTourTypeDAO()
                    .findTourTypeByName(tour.getType());
        }

        log.info("Inserting a tour '" + tour.getName() + "'...");

        try {
            preparedStatement = connection.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, tour.getName());
            preparedStatement.setString(2, tour.getCity());
            preparedStatement.setInt(3, typeID);
            preparedStatement.setInt(4, tour.getPrice());
            preparedStatement.setDate(5,
                    new java.sql.Date(tour.getCheckInDate().getTime()));

            preparedStatement.executeUpdate();

            resultSet = preparedStatement.getGeneratedKeys();
            resultSet.next();
            id = resultSet.getInt(1);
            tour.setId(id);

            log.info(tour.getName() + " tour added with id " + id);
        }
        catch (SQLException e) {
            log.error(e);
        }
        finally {
            closeResultSet(resultSet);
            closePreparedStatement(preparedStatement);
            connectionManager.releaseConnection(connection);
        }

        return id;
    }

    @Override
    public boolean deleteTour(int id) {
        ConnectionManager connectionManager = ConnectionManager.getInstance();
        Connection connection = connectionManager.getConnection();
        String sqlDelete = ConfigurationManager.getInstance()
                .getProperty(DELETE_TOUR);
        String sqlRemoveLinks = ConfigurationManager.getInstance()
                .getProperty(REMOVE_CUSTOMER_TOUR);

        PreparedStatement preparedStatement = null;
        int success = 0;

        log.info("Deleting a tour with id " + id + "...");

        try {
            preparedStatement = connection.prepareStatement(sqlRemoveLinks);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

            preparedStatement = connection.prepareStatement(sqlDelete);
            preparedStatement.setInt(1, id);

            success = preparedStatement.executeUpdate();
        }
        catch (SQLException e) {
            log.error(e);
        }
        finally {
            closePreparedStatement(preparedStatement);
            connectionManager.releaseConnection(connection);
        }

        if (success == 0) {
            log.info("Tour with id " + id + " has not been deleted.");
            return false;
        }
        else {
            log.info("Tour with id " + id + " successfully deleted.");
            return true;
        }
    }

    @Override
    public Tour findTourByID(int id) {
        ConnectionManager connectionManager = ConnectionManager.getInstance();
        Connection connection = connectionManager.getConnection();
        String sql = ConfigurationManager.getInstance()
                .getProperty(FIND_TOUR_BY_ID);

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        Tour tour = null;

        log.info("Search tour with id " + id + "...");

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                tour = createTour(resultSet);
                log.info("Tour with id " + id + " successfully found.");
            }
            else {
                log.info("Tour with id " + id + " not found.");
            }
        }
        catch (SQLException e) {
            log.error(e);
        }
        finally {
            closeResultSet(resultSet);
            closePreparedStatement(preparedStatement);
            connectionManager.releaseConnection(connection);
        }

        return tour;
    }

    @Override
    public Tour[] findToursByKeyword(String keyword) {
        ConnectionManager connectionManager = ConnectionManager.getInstance();
        Connection connection = connectionManager.getConnection();
        String sql = ConfigurationManager.getInstance()
                .getProperty(FIND_TOUR_BY_KEYWORD);

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        List<Tour> tours = new LinkedList<>();

        log.info("Search tour '" + keyword + "'...");

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, keyword);
            preparedStatement.setString(2, keyword);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                tours.add(createTour(resultSet));
            }

            log.info(tours.size() + " tours selected.");
        }
        catch (SQLException e) {
            log.error(e);
        }
        finally {
            closeResultSet(resultSet);
            closePreparedStatement(preparedStatement);
            connectionManager.releaseConnection(connection);
        }

        connectionManager.releaseConnection(connection);
        return tours.toArray(new Tour[0]);
    }

    @Override
    public boolean updateTour(Tour tour) {
        ConnectionManager connectionManager = ConnectionManager.getInstance();
        Connection connection = connectionManager.getConnection();
        String sql = ConfigurationManager.getInstance()
                .getProperty(UPDATE_TOUR);

        PreparedStatement preparedStatement = null;
        int success = 0;
        int typeID = -1;

        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.MYSQL);
        if (daoFactory != null) {
            typeID = daoFactory.getTourTypeDAO()
                    .findTourTypeByName(tour.getType());
        }

        log.info("Updating tour...");

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, tour.getName());
            preparedStatement.setString(2, tour.getCity());
            preparedStatement.setInt(3, typeID);
            preparedStatement.setInt(4, tour.getPrice());
            preparedStatement.setDate(5,
                    new java.sql.Date(tour.getCheckInDate().getTime()));
            preparedStatement.setBoolean(6, tour.isLastMinutePackage());
            preparedStatement.setString(7, tour.getDescription());
            preparedStatement.setInt(8, tour.getId());

            success = preparedStatement.executeUpdate();
        }
        catch (SQLException e) {
            log.error(e);
        }
        finally {
            closePreparedStatement(preparedStatement);
            connectionManager.releaseConnection(connection);
        }

        if (success == 0) {
            log.info("Tour has not been updated.");
            return false;
        }
        else {
            log.info("Tour successfully updated.");
            return true;
        }
    }

    @Override
    public Tour[] selectLastMinuteTours() {
        ConnectionManager connectionManager = ConnectionManager.getInstance();
        Connection connection = connectionManager.getConnection();
        String sql = ConfigurationManager.getInstance()
                .getProperty(SELECT_LAST_MINUTE_TOURS);

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        List<Tour> tours = new LinkedList<>();

        log.info("Selecting last-minute tours...");

        try {
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                tours.add(createTour(resultSet));
            }

            log.info(tours.size() + " last-minute tours selected.");
        }
        catch (SQLException e) {
            log.error(e);
        }
        finally {
            closeResultSet(resultSet);
            closePreparedStatement(preparedStatement);
            connectionManager.releaseConnection(connection);
        }

        return tours.toArray(new Tour[0]);
    }

    @Override
    public Tour[] selectAllTours() {
        ConnectionManager connectionManager = ConnectionManager.getInstance();
        Connection connection = connectionManager.getConnection();
        String sql = ConfigurationManager.getInstance()
                .getProperty(SELECT_ALL_TOURS);

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        List<Tour> tours = new LinkedList<>();

        log.info("Selecting all tours...");

        try {
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                tours.add(createTour(resultSet));
            }

            log.info(tours.size() + " tours selected.");
        }
        catch (SQLException e) {
            log.error(e);
        }
        finally {
            closeResultSet(resultSet);
            closePreparedStatement(preparedStatement);
            connectionManager.releaseConnection(connection);
        }

        return tours.toArray(new Tour[0]);
    }

    @Override
    public Tour[] selectCustomerTours(int customerID) {
        ConnectionManager connectionManager = ConnectionManager.getInstance();
        Connection connection = connectionManager.getConnection();
        String sql = ConfigurationManager.getInstance()
                .getProperty(SELECT_CUSTOMER_TOURS);

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        List<Tour> tours = new LinkedList<>();

        log.info("Selecting customer tours...");

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, customerID);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                tours.add(createTour(resultSet));
            }

            log.info(tours.size() + " customer tours selected.");
        }
        catch (SQLException e) {
            log.error(e);
        }
        finally {
            closeResultSet(resultSet);
            closePreparedStatement(preparedStatement);
            connectionManager.releaseConnection(connection);
        }

        return tours.toArray(new Tour[0]);
    }
}
