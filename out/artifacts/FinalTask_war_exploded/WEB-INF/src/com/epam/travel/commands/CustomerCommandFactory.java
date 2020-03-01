/*
 * Customer Command Factory.
 *
 * Version 1.0
 *
 * Date 20 Jan 2020
 */


package com.epam.travel.commands;

import com.epam.travel.dao.DAOFactory;

/**
 * Command Factory for customers.
 *
 * @version 1.0 20 Jan 2020
 * @author Ilya Kuliashou
 */
public class CustomerCommandFactory extends CommandFactory {
    /**
     * Enumeration of commands.
     */
    private enum Commands {
        /**
         * Register a new user.
         */
        REGISTER,
        /**
         * Login.
         */
        LOGIN,
        /**
         * Search tour.
         */
        SEARCH_TOUR,
        /**
         * Add money.
         */
        ADD_MONEY,
        /**
         * Logout.
         */
        LOGOUT,
        /**
         * Book a tour.
         */
        BOOK_TOUR,
        /**
         * Get customer tours.
         */
        GET_CUSTOMER_TOURS,
        /**
         * Get all tours.
         */
        GET_ALL_TOURS
    }

    /**
     * DAO factory used by commands.
     */
    private DAOFactory daoFactory;

    /**
     * Creates a new customer command factory.
     *
     * @param daoFactory DAO factory used by commands.
     */
    public CustomerCommandFactory(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    public Command getCommand(String commandName) {
        Commands command = Commands.valueOf(commandName);
        switch (command) {
            case REGISTER:
                return new RegisterCommand(daoFactory);
            case LOGIN:
                return new LoginCommand(daoFactory);
            case SEARCH_TOUR:
                return new SearchTourCommand(daoFactory);
            case ADD_MONEY:
                return new AddMoneyCommand(daoFactory);
            case LOGOUT:
                return new LogoutCommand();
            case BOOK_TOUR:
                return new BookTourCommand(daoFactory);
            case GET_CUSTOMER_TOURS:
                return new GetCustomerToursCommand(daoFactory);
            case GET_ALL_TOURS:
                return new GetAllToursCommand(daoFactory);
        }
        return new UnknownActionCommand();
    }
}
