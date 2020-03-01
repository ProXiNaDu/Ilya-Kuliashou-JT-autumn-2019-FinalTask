/*
 * Command Factory
 *
 * Version 1.0
 *
 * Date 20 Jan 2020
 */


package com.epam.travel.commands;

import com.epam.travel.dao.DAOFactory;

/**
 * Abstract Command Factory.
 *
 * @version 1.0 20 Jan 2020
 * @author Ilya Kuliashou
 */
public abstract class CommandFactory {
    /**
     * Enumeration of factories.
     */
    public enum Factories {
        /**
         * Customer command factory id.
         */
        CUSTOMER,
        /**
         * Tour agent command factory id.
         */
        TOUR_AGENT
    }

    /**
     * Creates a command factory.
     *
     * @param whichFactory factory index.
     * @return command factory.
     */
    public static CommandFactory getCommandFactory(Factories whichFactory, DAOFactory daoFactory) {
        switch (whichFactory) {
            case CUSTOMER:
                return new CustomerCommandFactory(daoFactory);
            case TOUR_AGENT:
                return new AgentCommandFactory(daoFactory);
        }
        return null;
    }

    /**
     * Create a new command.
     *
     * @param commandName command name.
     * @return command.
     */
    public abstract Command getCommand(String commandName);
}
