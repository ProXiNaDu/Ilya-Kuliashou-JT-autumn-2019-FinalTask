/*
 * Person
 *
 * Version 2.0
 *
 * Date 18 Jan 2020
 */


package com.epam.travel.travelagency;

import java.util.Objects;

/**
 * Person class.
 *
 * @version 2.0 18 Jan 2020
 * @author Ilya Kuliashou
 */
public class Person {
    /**
     * Person id.
     */
    private int id;
    /**
     * Person full name.
     */
    private String fullName;
    /**
     * Person email.
     */
    private String email;
    /**
     * Person password.
     */
    private String password;

    /**
     * Creates a new person.
     *
     * @param fullName person full name.
     * @param email person email.
     * @param password person password.
     */
    public Person(String fullName, String email, String password) {
        id = -1;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
    }

    /**
     * @return person id.
     */
    public int getId() {
        return id;
    }

    /**
     * @param id person id.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return person full name.
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * @param fullName person full name.
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * @return person email.
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email person email.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return person password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password person password.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", fullName='" + fullName +
                "', email='" + email +
                "'}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return id == person.id &&
                Objects.equals(fullName, person.fullName) &&
                Objects.equals(email, person.email) &&
                Objects.equals(password, person.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fullName, email, password);
    }
}
