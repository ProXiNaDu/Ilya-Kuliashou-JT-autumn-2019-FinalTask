/*
 * Tour
 *
 * Version 2.0
 *
 * Date 18 Jan 2020
 */


package com.epam.travel.travelagency;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Date;
import java.util.Objects;

/**
 * Tour class.
 *
 * @version 2.0 18 Jan 2020
 * @author Ilya Kuliashou
 */
public class Tour {
    /**
     * Tour name.
     */
    private String name;
    /**
     * City.
     */
    private String city;
    /**
     * Tour type.
     */
    private String type;
    /**
     * Description of the tour.
     */
    private String description;
    /**
     * Tour id.
     */
    private int id;
    /**
     * Tour price.
     */
    private int price;
    /**
     * It is true if the tour is last-minute.
     */
    private boolean isLastMinuteTour;
    /**
     * Check in date.
     */
    private Date checkInDate;

    /**
     * Creates a new tour.
     *
     * @param name tour name.
     * @param city city.
     * @param type tour type.
     * @param price tour price.
     * @param checkInDate check in date.
     */
    public Tour(String name, String city, String type, int price, Date checkInDate) {
        id = -1;
        isLastMinuteTour = false;

        this.name = name;
        this.city = city;
        this.type = type;
        this.price = price;
        this.checkInDate = (Date)checkInDate.clone();
    }

    /**
     * @return tour name.
     */
    public String getName() {
        return name;
    }

    /**
     * @param name tour name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return City.
     */
    public String getCity() {
        return city;
    }

    /**
     *
     * @param city City.
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @return Tour type.
     */
    public String getType() {
        return type;
    }

    /**
     * @param type Tour type.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return Tour description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description Tour description.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return tour id.
     */
    public int getId() {
        return id;
    }

    /**
     * @param id tour id.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return tour price.
     */
    public int getPrice() {
        return price;
    }

    /**
     * @param price tour price.
     */
    public void setPrice(int price) {
        this.price = price;
    }

    /**
     * @return true if the tour is last-minute.
     */
    public boolean isLastMinutePackage() {
        return isLastMinuteTour;
    }

    /**
     * @param lastMinuteTour true if the tour is last-minute.
     */
    public void setLastMinutePackage(boolean lastMinuteTour) {
        isLastMinuteTour = lastMinuteTour;
    }

    /**
     * @return Check in date.
     */
    public Date getCheckInDate() {
        return checkInDate;
    }

    /**
     * @param checkInDate Check in date.
     */
    public void setCheckInDate(Date checkInDate) {
        this.checkInDate = checkInDate;
    }

    /**
     * Converts tours to JSON array.
     *
     * @param tours tours.
     * @return JSON array.
     */
    public static JSONArray toursToJSONArray(Tour[] tours) {
        JSONArray toursJSON = new JSONArray();
        JSONObject tourJSON;

        for (Tour tour : tours) {
            tourJSON = new JSONObject();
            tourJSON.put("id", tour.getId());
            tourJSON.put("name", tour.getName());
            tourJSON.put("city", tour.getCity());
            tourJSON.put("type", tour.getType());
            tourJSON.put("price", tour.getPrice());
            tourJSON.put("date", tour.getCheckInDate());
            tourJSON.put("description", tour.getDescription());
            tourJSON.put("isLastMinute", tour.isLastMinutePackage());
            toursJSON.put(tourJSON);
        }

        return toursJSON;
    }

    @Override
    public String toString() {
        return "Tour{" +
                "id=" + id +
                ", name='" + name +
                "', city='" + city +
                "', type='" + type +
                "', description='" + description +
                "', price=" + price +
                ", isLastMinuteTour=" + isLastMinuteTour +
                ", checkInDate=" + checkInDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tour tour = (Tour) o;
        return id == tour.id &&
                price == tour.price &&
                isLastMinuteTour == tour.isLastMinuteTour &&
                Objects.equals(name, tour.name) &&
                Objects.equals(city, tour.city) &&
                Objects.equals(type, tour.type) &&
                Objects.equals(description, tour.description) &&
                Objects.equals(checkInDate, tour.checkInDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, city, type, description, id, price, isLastMinuteTour, checkInDate);
    }
}