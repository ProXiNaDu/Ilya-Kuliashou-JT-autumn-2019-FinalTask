/*
 * MySQL customer DAO
 *
 * Version 2.0
 *
 * Date 18 Jan 2020
 */


package com.epam.travel.dao;

import com.epam.travel.managers.ConfigurationManager;
import com.epam.travel.managers.ConnectionManager;
import com.epam.travel.travelagency.Customer;
import com.epam.travel.travelagency.Tour;
import com.epam.travel.travelagency.TourAgent;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * MySQL customer DAO
 *
 * @version 2.0 18 Jan 2020
 * @author Ilya Kuliashou
 */
public class MysqlCustomerDAO implements CustomerDAO {
    /**
     * The name of the property containing
     * SQL command to insert customer.
     */
    private static final String INSERT_CUSTOMER = "SQL_INSERT_CUSTOMER";
    /**
     * The name of the property containing
     * SQL command to delete customer.
     */
    private static final String DELETE_CUSTOMER = "SQL_DELETE_CUSTOMER";
    /**
     * The name of the property containing
     * SQL command to update customer.
     */
    private static final String UPDATE_CUSTOMER = "SQL_UPDATE_CUSTOMER";
    /**
     * The name of the property containing
     * SQL command to find customer by id.
     */
    private static final String FIND_CUSTOMER_BY_ID =
            "SQL_FIND_CUSTOMER_BY_ID";
    /**
     * The name of the property containing
     * SQL command to find customer by email.
     */
    private static final String FIND_CUSTOMER_BY_EMAIL =
            "SQL_FIND_CUSTOMER_BY_EMAIL";
    /**
     * The name of the property containing
     * SQL command to find customers by name.
     */
    private static final String FIND_CUSTOMERS_BY_NAME =
            "SQL_FIND_CUSTOMERS_BY_NAME";
    /**
     * The name of the property containing
     * SQL command to insert customer tours
     */
    private static final String INSERT_CUSTOMER_TOURS =
            "SQL_INSERT_CUSTOMER_TOURS";

    /**
     * Logger.
     */
    private static Logger log = LogManager.getLogger(MysqlCustomerDAO.class);

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

    private void insertCustomerTours(Customer customer, Connection connection) {
        if (customer.getId() == -1) {
            return;
        }

        PreparedStatement preparedStatement = null;
        String sql = ConfigurationManager.getInstance()
                .getProperty(INSERT_CUSTOMER_TOURS);

        Tour[] tours = customer.getTours();

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, customer.getId());

            for (Tour tour : tours) {
                if (tour.getId() == -1) {
                    continue;
                }
                preparedStatement.setInt(2, tour.getId());
                try {
                    preparedStatement.executeUpdate();
                }
                catch (SQLException e) {
                    log.error(e);
                }
            }
        }
        catch (SQLException e) {
            log.error(e);
        }
        finally {
            closePreparedStatement(preparedStatement);
        }
    }

    /**
     * Creates a new customer.
     *
     * @param resultSet query result.
     * @return customer.
     * @throws SQLException An exception that provides information on
     * a database access error or other errors.
     */
    private Customer createCustomer(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt(1);
        String fullName = resultSet.getString(2);
        String email = resultSet.getString(3);
        String password = resultSet.getString(4);
        int money = resultSet.getInt(5);
        int discount = resultSet.getInt(6);

        Customer customer = new Customer(fullName, email, password);
        customer.setId(id);
        customer.setMoney(money);
        customer.setDiscount(discount);

        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.MYSQL);
        if (daoFactory != null) {
            customer.setTours(daoFactory.getTourDAO().selectCustomerTours(id));
        }

        return customer;
    }

    @Override
    public int insertCustomer(Customer customer) {
        ConnectionManager connectionManager = ConnectionManager.getInstance();
        Connection connection = connectionManager.getConnection();
        String sql = ConfigurationManager.getInstance()
                .getProperty(INSERT_CUSTOMER);

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int id = -1;

        log.info("Inserting a customer " + customer.getFullName() + "...");

        try {
            preparedStatement = connection.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, customer.getFullName());
            preparedStatement.setString(2, customer.getEmail());
            preparedStatement.setString(3, customer.getPassword());

            preparedStatement.executeUpdate();

            resultSet = preparedStatement.getGeneratedKeys();
            resultSet.next();
            id = resultSet.getInt(1);
            customer.setId(id);

            insertCustomerTours(customer, connection);

            log.info(customer.getFullName() + " added with id " + id);
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
    public Customer findCustomerByID(int id) {
        ConnectionManager connectionManager = ConnectionManager.getInstance();
        Connection connection = connectionManager.getConnection();
        String sql = ConfigurationManager.getInstance()
                .getProperty(FIND_CUSTOMER_BY_ID);

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        Customer customer = null;

        log.info("Search customer with id " + id + "...");

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                customer = createCustomer(resultSet);
                log.info("Customer with id " + id + " successfully found.");
            }
            else {
                log.info("Customer with id " + id + " not found.");
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

        return customer;
    }

    @Override
    public Customer findCustomerByEmail(String email) {
        ConnectionManager connectionManager = ConnectionManager.getInstance();
        Connection connection = connectionManager.getConnection();
        String sql = ConfigurationManager.getInstance()
                .getProperty(FIND_CUSTOMER_BY_EMAIL);

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        Customer customer = null;

        log.info("Search customer with email " + email + "...");

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, email);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                customer = createCustomer(resultSet);
                log.info("Customer with email " + email + " successfully found.");
            }
            else {
                log.info("Customer with email " + email + " not found.");
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

        return customer;
    }

    @Override
    public Customer[] findCustomersByName(String fullName) {
        ConnectionManager connectionManager = ConnectionManager.getInstance();
        Connection connection = connectionManager.getConnection();
        String sql = ConfigurationManager.getInstance()
                .getProperty(FIND_CUSTOMERS_BY_NAME);

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        List<Customer> customers = new LinkedList<>();

        log.info("Search for a customer " + fullName + "...");

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, fullName);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                customers.add(createCustomer(resultSet));
            }

            log.info(customers.size() + " customers found.");
        }
        catch (SQLException e) {
            log.error(e);
        }
        finally {
            closeResultSet(resultSet);
            closePreparedStatement(preparedStatement);
            connectionManager.releaseConnection(connection);
        }

        return customers.toArray(new Customer[0]);
    }

    @Override
    public boolean updateCustomer(Customer customer) {
        ConnectionManager connectionManager = ConnectionManager.getInstance();
        Connection connection = connectionManager.getConnection();
        String sql = ConfigurationManager.getInstance()
                .getProperty(UPDATE_CUSTOMER);

        PreparedStatement preparedStatement = null;
        int success = 0;

        log.info("Updating customer...");

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, customer.getFullName());
            preparedStatement.setString(2, customer.getEmail());
            preparedStatement.setString(3, customer.getPassword());
            preparedStatement.setInt(4, customer.getMoney());
            preparedStatement.setInt(5, customer.getDiscount());
            preparedStatement.setInt(6, customer.getId());

            success = preparedStatement.executeUpdate();

            insertCustomerTours(customer, connection);
        }
        catch (SQLException e) {
            log.error(e);
        }
        finally {
            closePreparedStatement(preparedStatement);
            connectionManager.releaseConnection(connection);
        }

        if (success == 0) {
            log.info("Customer has not been updated.");
            return false;
        }
        else {
            log.info("Customer successfully updated.");
            return true;
        }
    }

    @Override
    public boolean deleteCustomer(int id) {
        ConnectionManager connectionManager = ConnectionManager.getInstance();
        Connection connection = connectionManager.getConnection();
        String sql = ConfigurationManager.getInstance()
                .getProperty(DELETE_CUSTOMER);

        PreparedStatement preparedStatement = null;
        int success = 0;

        log.info("Deleting a customer with id " + id + "...");

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
            log.info("Customer with id " + id + " has not been deleted.");
            return false;
        }
        else {
            log.info("Customer with id " + id + " successfully deleted.");
            return true;
        }
    }
}
