/*
 * Agent Command Factory.
 *
 * Version 1.0
 *
 * Date 20 Jan 2020
 */


package com.epam.travel.commands;

import com.epam.travel.dao.DAOFactory;

/**
 * Command Factory for agents.
 *
 * @version 1.0 20 Jan 2020
 * @author Ilya Kuliashou
 */
public class AgentCommandFactory extends CommandFactory {
    /**
     * Enumeration of commands.
     */
    private enum Commands {
        /**
         * Search tour.
         */
        SEARCH_TOUR,
        /**
         * Search user.
         */
        SEARCH_USER,
        /**
         * Add a new tour.
         */
        ADD_TOUR,
        /**
         * Edit tour.
         */
        EDIT_TOUR,
        /**
         * Delete tour.
         */
        DELETE_TOUR,
        /**
         * Change discount for customer.
         */
        CHANGE_DISCOUNT,
        /**
         * Get all types of tour.
         */
        GET_TOUR_TYPES,
        /**
         * Logout.
         */
        LOGOUT
    }

    /**
     * DAO factory used by commands.
     */
    private DAOFactory daoFactory;

    /**
     * Creates a new tour agent command factory.
     *
     * @param daoFactory DAO factory used by commands.
     */
    public AgentCommandFactory(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    public Command getCommand(String commandName) {
        Commands command = Commands.valueOf(commandName);
        switch (command) {
            case SEARCH_TOUR:
                return new SearchTourCommand(daoFactory);
            case SEARCH_USER:
                return new SearchUserCommand(daoFactory);
            case ADD_TOUR:
                return new AddTourCommand(daoFactory);
            case EDIT_TOUR:
                return new EditTourCommand(daoFactory);
            case DELETE_TOUR:
                return new DeleteTourCommand(daoFactory);
            case CHANGE_DISCOUNT:
                return new ChangeDiscountCommand(daoFactory);
            case GET_TOUR_TYPES:
                return new GetTourTypesCommand(daoFactory);
            case LOGOUT:
                return new LogoutCommand();
        }
        return new UnknownActionCommand();
    }
}
