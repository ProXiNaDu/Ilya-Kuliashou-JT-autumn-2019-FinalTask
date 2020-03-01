/*
 * MySQL DAO Factory
 *
 * Version 1.1
 *
 * Date 18 Jan 2020
 */


package com.epam.travel.dao;

/**
 * MySQL DAO Factory.
 *
 * @version 1.1 18 Jan 2020
 * @author Ilya Kuliashou
 */
public class MysqlDAOFactory extends DAOFactory {
    @Override
    public CustomerDAO getCustomerDAO() {
        return new MysqlCustomerDAO();
    }

    @Override
    public TourAgentDAO getTourAgentDAO() {
        return new MysqlTourAgentDAO();
    }

    @Override
    public TourDAO getTourDAO() {
        return new MysqlTourDAO();
    }

    @Override
    public TourTypeDAO getTourTypeDAO() {
        return new MysqlTourTypeDAO();
    }
}
