/*
 * Tour DAO
 *
 * Version 2.0
 *
 * Date 18 Jan 2020
 */


package com.epam.travel.dao;

import com.epam.travel.travelagency.Tour;

/**
 * Interface describing tour DAO
 *
 * @version 2.0 18 Jan 2020
 * @author Ilya Kuliashou
 */
public interface TourDAO {
    /**
     * Insert a new tour.
     *
     * @param tour tour to insert.
     * @return tour id.
     */
    int insertTour(Tour tour);

    /**
     * Search tour by id.
     *
     * @param id tour id.
     * @return found tour.
     */
    Tour findTourByID(int id);

    /**
     * Search keyword.
     *
     * @param keyword word to look for.
     * @return found tours.
     */
    Tour[] findToursByKeyword(String keyword);

    /**
     * Selection of customer tours.
     *
     * @param customerID customer id.
     * @return customer tours.
     */
    Tour[] selectCustomerTours(int customerID);

    /**
     * Selection of last-minute tours.
     *
     * @return last-minute tours.
     */
    Tour[] selectLastMinuteTours();

    /**
     * Select all tours.
     *
     * @return All tours.
     */
    Tour[] selectAllTours();

    /**
     * Update tour.
     *
     * @param tour updated tour.
     * @return true if success and false otherwise.
     */
    boolean updateTour(Tour tour);

    /**
     * Delete tour.
     *
     * @param id tour id.
     * @return true if success and false otherwise.
     */
    boolean deleteTour(int id);
}
