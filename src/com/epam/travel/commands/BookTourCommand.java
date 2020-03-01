/*
 * Book a Tour Command.
 *
 * Version 1.0
 *
 * Date 20 Jan 2020
 */


package com.epam.travel.commands;

import com.epam.travel.dao.CustomerDAO;
import com.epam.travel.dao.DAOFactory;
import com.epam.travel.dao.TourDAO;
import com.epam.travel.travelagency.Customer;
import com.epam.travel.travelagency.Person;
import com.epam.travel.travelagency.Tour;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;

/**
 * Book a Tour Command.
 *
 * @version 1.0 20 Jan 2020
 * @author Ilya Kuliashou
 */
public class BookTourCommand implements Command {
    /**
     * Tour data access object.
     */
    private TourDAO tourDAO;
    /**
     * Tour data access object.
     */
    private CustomerDAO customerDAO;

    /**
     * Creates a new book a tour command.
     *
     * @param daoFactory DAO factory used by command.
     */
    public BookTourCommand(DAOFactory daoFactory) {
        tourDAO = daoFactory.getTourDAO();
        customerDAO = daoFactory.getCustomerDAO();
    }

    /**
     * Booking a new tour.
     *
     * @param req JSON request.
     * @param session session.
     * @return JSON response.
     */
    private JSONObject bookTour(JSONObject req, HttpSession session) {
        JSONObject res = new JSONObject();
        Customer customer = (Customer) session.getAttribute("user");

        Tour tour = tourDAO.findTourByID(req.getInt("id"));
        int realPrice = tour.getPrice() -
                tour.getPrice() * customer.getDiscount() / 100;
        if (tour == null ||
                customer.getMoney() < realPrice ||
                Arrays.asList(customer.getTours()).contains(tour)) {
            res.put("success", false);
            res.put("msg", "You can not book this tour.");
            return res;
        }

        customer.addTour(tour);
        customer.setMoney(customer.getMoney() - realPrice);
        customerDAO.updateCustomer(customer);

        res.put("success", true);
        res.put("user", customer.toJSON());
        return res;
    }

    @Override
    public JSONObject execute(HttpServletRequest req, HttpServletResponse resp, JSONObject body) {
        HttpSession session = req.getSession();
        Person user = (Person) session.getAttribute("user");
        JSONObject res;
        if (user != null && user.getClass() == Customer.class) {
            res = bookTour(body, session);
        }
        else {
            res = new JSONObject();
            res.put("success", false);
            res.put("msg", "You are not authorized.");
        }
        return res;
    }
}
