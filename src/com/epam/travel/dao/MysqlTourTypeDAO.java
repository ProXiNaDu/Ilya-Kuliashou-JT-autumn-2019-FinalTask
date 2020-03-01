/*
 * MySQL tour type DAO
 *
 * Version 1.0
 *
 * Date 18 Jan 2020
 */


package com.epam.travel.dao;

import com.epam.travel.managers.ConfigurationManager;
import com.epam.travel.managers.ConnectionManager;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

/**
 * MySQL tour type DAO
 *
 * @version 1.0 18 Jan 2020
 * @author Ilya Kuliashou
 */
public class MysqlTourTypeDAO implements TourTypeDAO {
    /**
     * The name of the property containing
     * SQL command to insert tour type.
     */
    private static final String INSERT_TOUR_TYPE = "SQL_INSERT_TOUR_TYPE";
    /**
     * The name of the property containing
     * SQL command to delete tour type.
     */
    private static final String DELETE_TOUR_TYPE = "SQL_DELETE_TOUR_TYPE";
    /**
     * The name of the property containing
     * SQL command to update tour type.
     */
    private static final String UPDATE_TOUR_TYPE = "SQL_UPDATE_TOUR_TYPE";
    /**
     * The name of the property containing
     * SQL command to find tour type by id.
     */
    private static final String FIND_TOUR_TYPE_BY_ID =
            "SQL_FIND_TOUR_TYPE_BY_ID";
    /**
     * The name of the property containing
     * SQL command to find tour type by name.
     */
    private static final String FIND_TOUR_TYPE_BY_NAME =
            "SQL_FIND_TOUR_TYPE_BY_NAME";
    /**
     * The name of the property containing
     * SQL command to select all tour types.
     */
    private static final String SELECT_ALL_TOUR_TYPES =
            "SQL_SELECT_ALL_TOUR_TYPES";

    /**
     * Logger.
     */
    private static Logger log = LogManager.getLogger(MysqlTourTypeDAO.class);

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

    @Override
    public int insertTourType(String type) {
        ConnectionManager connectionManager = ConnectionManager.getInstance();
        Connection connection = connectionManager.getConnection();
        String sql = ConfigurationManager.getInstance()
                .getProperty(INSERT_TOUR_TYPE);

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int id = -1;

        log.info("Inserting a tour type '" + type + "'...");

        try {
            preparedStatement = connection.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, type);

            preparedStatement.executeUpdate();

            resultSet = preparedStatement.getGeneratedKeys();
            resultSet.next();
            id = resultSet.getInt(1);

            log.info(type + " tour type added with id " + id);
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
    public String findTourTypeByID(int id) {
        ConnectionManager connectionManager = ConnectionManager.getInstance();
        Connection connection = connectionManager.getConnection();
        String sql = ConfigurationManager.getInstance()
                .getProperty(FIND_TOUR_TYPE_BY_ID);

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        String type = "";

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                type = resultSet.getString(1);
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

        return type;
    }

    @Override
    public int findTourTypeByName(String name) {
        ConnectionManager connectionManager = ConnectionManager.getInstance();
        Connection connection = connectionManager.getConnection();
        String sql = ConfigurationManager.getInstance()
                .getProperty(FIND_TOUR_TYPE_BY_NAME);

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        int id = -1;

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                id = resultSet.getInt(1);
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

        return id;
    }

    @Override
    public String[] selectAllTourTypes() {
        ConnectionManager connectionManager = ConnectionManager.getInstance();
        Connection connection = connectionManager.getConnection();
        String sql = ConfigurationManager.getInstance()
                .getProperty(SELECT_ALL_TOUR_TYPES);

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        List<String> types = new LinkedList<>();

        log.info("Selecting all tour types...");

        try {
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                types.add(resultSet.getString(1));
            }

            log.info(types.size() + " tour types selected.");
        }
        catch (SQLException e) {
            log.error(e);
        }
        finally {
            closeResultSet(resultSet);
            closePreparedStatement(preparedStatement);
            connectionManager.releaseConnection(connection);
        }

        return types.toArray(new String[0]);
    }

    @Override
    public boolean updateTourType(int id, String name) {
        ConnectionManager connectionManager = ConnectionManager.getInstance();
        Connection connection = connectionManager.getConnection();
        String sql = ConfigurationManager.getInstance()
                .getProperty(UPDATE_TOUR_TYPE);

        PreparedStatement preparedStatement = null;
        int success = 0;

        log.info("Updating tour type...");

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, id);

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
            log.info("Tour type has not been updated.");
            return false;
        }
        else {
            log.info("Tour type successfully updated.");
            return true;
        }
    }

    @Override
    public boolean deleteTourType(int id) {
        ConnectionManager connectionManager = ConnectionManager.getInstance();
        Connection connection = connectionManager.getConnection();
        String sql = ConfigurationManager.getInstance()
                .getProperty(DELETE_TOUR_TYPE);

        PreparedStatement preparedStatement = null;
        int success = 0;

        log.info("Deleting a tour type with id " + id + "...");

        try {
            preparedStatement = connection.prepareStatement(sql);
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
            log.info("Tour type with id " + id + " has not been deleted.");
            return false;
        }
        else {
            log.info("Tour type with id " + id + " successfully deleted.");
            return true;
        }
    }
}
