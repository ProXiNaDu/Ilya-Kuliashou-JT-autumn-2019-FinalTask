/*
 * Add Money Command.
 *
 * Version 1.0
 *
 * Date 20 Jan 2020
 */


package com.epam.travel.commands;

import com.epam.travel.dao.CustomerDAO;
import com.epam.travel.dao.DAOFactory;
import com.epam.travel.travelagency.Customer;
import com.epam.travel.travelagency.Person;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Add Money Command.
 *
 * @version 1.0 20 Jan 2020
 * @author Ilya Kuliashou
 */
public class AddMoneyCommand implements Command {
    /**
     * Customer data access object.
     */
    private CustomerDAO customerDAO;

    /**
     * Add Money Command.
     *
     * @param daoFactory DAO factory used by command.
     */
    public AddMoneyCommand(DAOFactory daoFactory) {
        customerDAO = daoFactory.getCustomerDAO();
    }

    /**
     * Add money.
     *
     * @param req JSON request.
     * @return JSON response.
     */
    private JSONObject addMoney(JSONObject req, HttpSession session) {
        JSONObject res = new JSONObject();
        Customer customer = (Customer) session.getAttribute("user");

        customer.addMoney(req.getInt("amount"));
        if (customerDAO.updateCustomer(customer)) {
            res.put("success", true);
            res.put("user", customer.toJSON());
        }
        else {
            customer.addMoney(-req.getInt("amount"));
            res.put("success", false);
            res.put("msg", "Customer has not been updated.");
        }

        return res;
    }

    @Override
    public JSONObject execute(HttpServletRequest req, HttpServletResponse resp, JSONObject body) {
        HttpSession session = req.getSession();
        JSONObject res;
        Person user = (Person) session.getAttribute("user");

        if (user != null && user.getClass() == Customer.class) {
            res = addMoney(body, session);
        }
        else {
            res = new JSONObject();
            res.put("success", false);
            res.put("msg", "You are not authorized.");
        }

        return res;
    }
}
