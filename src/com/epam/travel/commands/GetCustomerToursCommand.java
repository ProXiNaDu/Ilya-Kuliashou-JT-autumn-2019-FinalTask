/*
 * Get Customer Tours Command.
 *
 * Version 1.0
 *
 * Date 20 Jan 2020
 */


package com.epam.travel.commands;

import com.epam.travel.dao.DAOFactory;
import com.epam.travel.dao.TourDAO;
import com.epam.travel.travelagency.Customer;
import com.epam.travel.travelagency.Person;
import com.epam.travel.travelagency.Tour;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Get Customer Tours Command.
 *
 * @version 1.0 20 Jan 2020
 * @author Ilya Kuliashou
 */
public class GetCustomerToursCommand implements Command {
    /**
     * Tour data access object.
     */
    private TourDAO tourDAO;

    /**
     * Creates a new GetCustomerToursCommand.
     *
     * @param daoFactory DAO factory used by command.
     */
    public GetCustomerToursCommand(DAOFactory daoFactory) {
        tourDAO = daoFactory.getTourDAO();
    }

    /**
     * Return all customer tours.
     *
     * @param session session
     * @return all customer tours.
     */
    private JSONObject getCustomerTours(HttpSession session) {
        JSONObject res = new JSONObject();
        Customer customer = (Customer) session.getAttribute("user");

        Tour[] tours = tourDAO.selectCustomerTours(customer.getId());

        res.put("success", true);
        res.put("tours", Tour.toursToJSONArray(tours));
        return res;
    }

    @Override
    public JSONObject execute(HttpServletRequest req, HttpServletResponse resp, JSONObject body) {
        HttpSession session = req.getSession();
        Person user = (Person) session.getAttribute("user");
        JSONObject res;
        if (user != null && user.getClass() == Customer.class) {
            res = getCustomerTours(session);
        }
        else {
            res = new JSONObject();
            res.put("success", false);
            res.put("msg", "You are not authorized.");
        }
        return res;
    }
}
