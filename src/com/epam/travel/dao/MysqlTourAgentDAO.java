/*
 * MySQL tour agent DAO
 *
 * Version 2.0
 *
 * Date 18 Jan 2020
 */


package com.epam.travel.dao;

import com.epam.travel.managers.ConfigurationManager;
import com.epam.travel.managers.ConnectionManager;
import com.epam.travel.travelagency.TourAgent;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

/**
 * MySQL tour agent DAO
 *
 * @version 2.0 18 Jan 2020
 * @author Ilya Kuliashou
 */
public class MysqlTourAgentDAO implements TourAgentDAO {
    /**
     * The name of the property containing
     * SQL command to insert tour agent.
     */
    private static final String INSERT_AGENT = "SQL_INSERT_AGENT";
    /**
     * The name of the property containing
     * SQL command to delete tour agent.
     */
    private static final String DELETE_AGENT = "SQL_DELETE_AGENT";
    /**
     * The name of the property containing
     * SQL command to update tour agent.
     */
    private static final String UPDATE_AGENT = "SQL_UPDATE_AGENT";
    /**
     * The name of the property containing
     * SQL command to find tour agent by id.
     */
    private static final String FIND_AGENT_BY_ID = "SQL_FIND_AGENT_BY_ID";
    /**
     * The name of the property containing
     * SQL command to find tour agent by email.
     */
    private static final String FIND_AGENT_BY_EMAIL = "SQL_FIND_AGENT_BY_EMAIL";
    /**
     * The name of the property containing
     * SQL command to find tour agent by name.
     */
    private static final String FIND_AGENTS_BY_NAME = "SQL_FIND_AGENTS_BY_NAME";

    /**
     * Logger.
     */
    private static Logger log = LogManager.getLogger(MysqlTourAgentDAO.class);

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
     * Creates a new tour agent.
     *
     * @param resultSet query result.
     * @return tour agent.
     * @throws SQLException An exception that provides information on
     * a database access error or other errors.
     */
    private TourAgent createAgent(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt(1);
        String fullName = resultSet.getString(2);
        String email = resultSet.getString(3);
        String password = resultSet.getString(4);

        TourAgent tourAgent = new TourAgent(fullName, email, password);
        tourAgent.setId(id);

        return tourAgent;
    }

    @Override
    public int insertTourAgent(TourAgent tourAgent) {
        ConnectionManager connectionManager = ConnectionManager.getInstance();
        Connection connection = connectionManager.getConnection();
        String sql = ConfigurationManager.getInstance()
                .getProperty(INSERT_AGENT);

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int id = -1;

        log.info("Inserting a tour agent " + tourAgent.getFullName() + "...");

        try {
            preparedStatement = connection.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, tourAgent.getFullName());
            preparedStatement.setString(2, tourAgent.getEmail());
            preparedStatement.setString(3, tourAgent.getPassword());

            preparedStatement.executeUpdate();

            resultSet = preparedStatement.getGeneratedKeys();
            resultSet.next();
            id = resultSet.getInt(1);
            tourAgent.setId(id);

            log.info(tourAgent.getFullName() + " added with id " + id);
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
    public TourAgent findTourAgentByID(int id) {
        ConnectionManager connectionManager = ConnectionManager.getInstance();
        Connection connection = connectionManager.getConnection();
        String sql = ConfigurationManager.getInstance()
                .getProperty(FIND_AGENT_BY_ID);

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        TourAgent tourAgent = null;

        log.info("Search tour agent with id " + id + "...");

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                tourAgent = createAgent(resultSet);
                log.info("Tour agent with id " + id + " successfully found.");
            }
            else {
                log.info("Tour agent with id " + id + " not found.");
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

        return tourAgent;
    }

    @Override
    public TourAgent findTourAgentByEmail(String email) {
        ConnectionManager connectionManager = ConnectionManager.getInstance();
        Connection connection = connectionManager.getConnection();
        String sql = ConfigurationManager.getInstance()
                .getProperty(FIND_AGENT_BY_EMAIL);

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        TourAgent tourAgent = null;

        log.info("Search tour agent with email " + email + "...");

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, email);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                tourAgent = createAgent(resultSet);
                log.info("Tour agent with email " + email + " successfully found.");
            }
            else {
                log.info("Tour agent with email " + email + " not found.");
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

        return tourAgent;
    }

    @Override
    public TourAgent[] findTourAgentsByName(String fullName) {
        ConnectionManager connectionManager = ConnectionManager.getInstance();
        Connection connection = connectionManager.getConnection();
        String sql = ConfigurationManager.getInstance()
                .getProperty(FIND_AGENTS_BY_NAME);

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        List<TourAgent> tourAgents = new LinkedList<>();

        log.info("Search for travel agents " + fullName + "...");

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, fullName);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                tourAgents.add(createAgent(resultSet));
            }

            log.info(tourAgents.size() + " travel agents found.");
        }
        catch (SQLException e) {
            log.error(e);
        }
        finally {
            closeResultSet(resultSet);
            closePreparedStatement(preparedStatement);
            connectionManager.releaseConnection(connection);
        }

        return tourAgents.toArray(new TourAgent[0]);
    }

    @Override
    public boolean updateTourAgent(TourAgent tourAgent) {
        ConnectionManager connectionManager = ConnectionManager.getInstance();
        Connection connection = connectionManager.getConnection();
        String sql = ConfigurationManager.getInstance()
                .getProperty(UPDATE_AGENT);

        PreparedStatement preparedStatement = null;
        int success = 0;

        log.info("Updating tour agent...");

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, tourAgent.getFullName());
            preparedStatement.setString(2, tourAgent.getEmail());
            preparedStatement.setString(3, tourAgent.getPassword());
            preparedStatement.setInt(4, tourAgent.getId());

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
            log.info("Tour agent has not been updated.");
            return false;
        }
        else {
            log.info("Tour agent successfully updated.");
            return true;
        }
    }

    @Override
    public boolean deleteTourAgent(int id) {
        ConnectionManager connectionManager = ConnectionManager.getInstance();
        Connection connection = connectionManager.getConnection();
        String sql = ConfigurationManager.getInstance()
                .getProperty(DELETE_AGENT);

        PreparedStatement preparedStatement = null;
        int success = 0;

        log.info("Deleting a tour agent with id " + id + "...");

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
            log.info("Tour agent with id " + id + " has not been deleted.");
            return false;
        }
        else {
            log.info("Tour agent with id " + id + " successfully deleted.");
            return true;
        }
    }
}
