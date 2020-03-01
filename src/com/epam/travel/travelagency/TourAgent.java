/*
 * Tour Agent
 *
 * Version 2.0
 *
 * Date 18 Jan 2020
 */


package com.epam.travel.travelagency;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * TourAgent class.
 *
 * @version 2.0 18 Jan 2020
 * @author Ilya Kuliashou
 */
public class TourAgent extends Person {
    /**
     * Logger.
     */
    private static final Logger log = LogManager.getLogger(TourAgent.class);

    /**
     * Creates a new tour agent.
     * 
     * @param fullName tour agent full name.
     * @param email tour agent email.
     * @param password tour agent password.
     */
    public TourAgent(String fullName, String email, String password) {
        super(fullName, email, password);
    }
}
