/*
 * Customer
 *
 * Version 2.0
 *
 * Date 18 Jan 2020
 */


package com.epam.travel.travelagency;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Customer class.
 *
 * @version 2.0 18 Jan 2020
 * @author Ilya Kuliashou
 */
public class Customer extends Person {
    /**
     * Logger.
     */
    private static final Logger log = LogManager.getLogger(Customer.class);

    /**
     * Customer money.
     */
    private int money;
    /**
     * Customer discount.
     */
    private int discount;
    /**
     * Tours.
     */
    private List<Tour> tours;

    /**
     * Creates a new customer
     *
     * @param fullName customer full name.
     * @param email customer email.
     * @param password customer password.
     */
    public Customer(String fullName, String email, String password) {
        super(fullName, email, password);
        money = 0;
        discount = 0;
        tours = new LinkedList<>();
    }

    /**
     * Add a new tour to customer.
     *
     * @param tour tour to add.
     */
    public void addTour(Tour tour) {
        tours.add(tour);
    }

    /**
     * Add money for customer.
     *
     * @return new amount of money.
     */
    public int addMoney(int amount) {
        money += amount;
        return money;
    }

    /**
     * @return tours.
     */
    public Tour[] getTours() {
        return tours.toArray(new Tour[0]);
    }

    /**
     * @param tours new tours.
     */
    public void setTours(Tour[] tours) {
        this.tours = new LinkedList<>(Arrays.asList(tours));
    }

    /**
     * @return customer money.
     */
    public int getMoney() {
        return money;
    }

    /**
     * @param money customer money.
     */
    public void setMoney(int money) {
        this.money = money;
    }

    /**
     * @return customer discount.
     */
    public int getDiscount() {
        return discount;
    }

    /**
     * @param discount customer discount.
     */
    public void setDiscount(int discount) {
        this.discount = discount;
    }

    /**
     * Converts a customer to JSON object.
     *
     * @return JSON object.
     */
    public JSONObject toJSON() {
        JSONObject user = new JSONObject();
        user.put("name", getFullName());
        user.put("discount", getDiscount());
        user.put("balance", getMoney());
        return user;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + getId() +
                ", fullName='" + getFullName() +
                "', email='" + getEmail() +
                "', money=" + money +
                ", discount=" + discount +
                ", tours=" + tours.toString() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Customer customer = (Customer) o;
        return money == customer.money &&
                discount == customer.discount &&
                Objects.equals(tours, customer.tours);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), money, discount, tours);
    }
}
