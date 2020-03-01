/*
 * Customer DAO
 *
 * Version 2.0
 *
 * Date 18 Jan 2020
 */


package com.epam.travel.dao;

import com.epam.travel.travelagency.Customer;

/**
 * Interface describing customer DAO
 *
 * @version 2.0 18 Jan 2020
 * @author Ilya Kuliashou
 */
public interface CustomerDAO {
    /**
     * Insert a new customer.
     *
     * @param customer customer to insert.
     * @return customer id.
     */
    public int insertCustomer(Customer customer);

    /**
     * Search customer by id.
     *
     * @param id customer id.
     * @return found customer.
     */
    public Customer findCustomerByID(int id);

    /**
     * Search customer by email.
     *
     * @param email customer email.
     * @return found customer.
     */
    public Customer findCustomerByEmail(String email);

    /**
     * Search customer by name.
     *
     * @param fullName customer full name.
     * @return found customers.
     */
    public Customer[] findCustomersByName(String fullName);

    /**
     * Update customer.
     *
     * @param customer updated customer.
     * @return true if success and false otherwise.
     */
    public boolean updateCustomer(Customer customer);

    /**
     * Delete customer.
     *
     * @param id customer id.
     * @return true if success and false otherwise.
     */
    public boolean deleteCustomer(int id);
}
