/*
 * Tour Agent DAO
 *
 * Version 2.0
 *
 * Date 18 Jan 2020
 */


package com.epam.travel.dao;

import com.epam.travel.travelagency.TourAgent;

/**
 * Interface describing tour agent DAO
 *
 * @version 2.0 18 Jan 2020
 * @author Ilya Kuliashou
 */
public interface TourAgentDAO {
    /**
     * Insert a new tour agent.
     *
     * @param tourAgent tour agent to insert.
     * @return tour agent id.
     */
    public int insertTourAgent(TourAgent tourAgent);

    /**
     * Search tour agent by id.
     *
     * @param id tour agent id.
     * @return found tour agent.
     */
    public TourAgent findTourAgentByID(int id);

    /**
     * Search tour agent by email.
     *
     * @param email tour agent email.
     * @return found tour agent.
     */
    public TourAgent findTourAgentByEmail(String email);

    /**
     * Search tour agent by name.
     *
     * @param fullName tour agent full name.
     * @return found tour agent.
     */
    public TourAgent[] findTourAgentsByName(String fullName);

    /**
     * Update tour agent.
     *
     * @param tourAgent updated tour agent.
     * @return true if success and false otherwise.
     */
    public boolean updateTourAgent(TourAgent tourAgent);

    /**
     * Delete tour agent.
     *
     * @param id tour agent id.
     * @return true if success and false otherwise.
     */
    public boolean deleteTourAgent(int id);
}
