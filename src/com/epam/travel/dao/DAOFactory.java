/*
 * DAO Factory
 *
 * Version 1.1
 *
 * Date 18 Jan 2020
 */


package com.epam.travel.dao;

/**
 * Abstract DAO factory.
 *
 * @version 1.1 18 Jan 2020
 * @author Ilya Kuliashou
 */
public abstract class DAOFactory {
    /**
     * MySQL database.
     */
    public static final int MYSQL = 1;

    /**
     * @return customers DAO.
     */
    public abstract CustomerDAO getCustomerDAO();

    /**
     * @return tour agent DAO.
     */
    public abstract TourAgentDAO getTourAgentDAO();

    /**
     * @return tour DAO.
     */
    public abstract TourDAO getTourDAO();

    /**
     * @return tour type DAO.
     */
    public abstract TourTypeDAO getTourTypeDAO();

    /**
     * Creates a DAO factory for the selected database.
     *
     * @param whichFactory database index.
     * @return DAO factory.
     */
    public static DAOFactory getDAOFactory(int whichFactory) {
        switch (whichFactory) {
            case MYSQL:
                return new MysqlDAOFactory();
            default:
                return null;
        }
    }
}
