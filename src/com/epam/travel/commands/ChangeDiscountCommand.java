/*
 * Change Discount Command.
 *
 * Version 1.0
 *
 * Date 20 Jan 2020
 */


package com.epam.travel.commands;

import com.epam.travel.dao.CustomerDAO;
import com.epam.travel.dao.DAOFactory;
import com.epam.travel.travelagency.Customer;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Change User Discount Command.
 *
 * @version 1.0 20 Jan 2020
 * @author Ilya Kuliashou
 */
public class ChangeDiscountCommand implements Command {
    /**
     * Customer data access object.
     */
    private CustomerDAO customerDAO;

    /**
     * Creates a new change user discount command.
     *
     * @param daoFactory DAO factory used by command.
     */
    public ChangeDiscountCommand(DAOFactory daoFactory) {
        customerDAO = daoFactory.getCustomerDAO();
    }

    @Override
    public JSONObject execute(HttpServletRequest req, HttpServletResponse resp, JSONObject body) {
        JSONObject res = new JSONObject();

        int id = body.getInt("id");
        int discount = body.getInt("discount");

        Customer customer = customerDAO.findCustomerByID(id);
        customer.setDiscount(discount);
        if (customerDAO.updateCustomer(customer)) {
            res.put("success", true);
        }
        else {
            res.put("success", false);
            res.put("msg", "Customer has not been updated.");
        }

        return res;
    }
}
