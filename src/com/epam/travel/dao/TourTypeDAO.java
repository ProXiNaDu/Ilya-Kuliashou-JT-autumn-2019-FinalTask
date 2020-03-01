/*
 * Tour type DAO
 *
 * Version 1.0
 *
 * Date 18 Jan 2020
 */


package com.epam.travel.dao;

/**
 * Interface describing tour type DAO
 *
 * @version 1.0 18 Jan 2020
 * @author Ilya Kuliashou
 */
public interface TourTypeDAO {
    /**
     * Insert a new tour type.
     *
     * @param type tour type to insert.
     * @return tour type id.
     */
    int insertTourType(String type);

    /**
     * Search tour type by id.
     *
     * @param id tour type id.
     * @return found tour type name.
     */
    String findTourTypeByID(int id);

    /**
     * Search tour type by name.
     *
     * @param name tour type name.
     * @return found tour type id.
     */
    int findTourTypeByName(String name);

    /**
     * @return all tour types.
     */
    String[] selectAllTourTypes();

    /**
     * Update tour type.
     *
     * @param id tour type id.
     * @param name tour type name.
     * @return true if success and false otherwise.
     */
    boolean updateTourType(int id, String name);

    /**
     * Delete tour type.
     *
     * @param id tour type id.
     * @return true if success and false otherwise.
     */
    boolean deleteTourType(int id);
}
