/*
 * Search User Command.
 *
 * Version 1.0
 *
 * Date 20 Jan 2020
 */


package com.epam.travel.commands;

import com.epam.travel.dao.CustomerDAO;
import com.epam.travel.dao.DAOFactory;
import com.epam.travel.travelagency.Customer;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Search User Command.
 *
 * @version 1.0 20 Jan 2020
 * @author Ilya Kuliashou
 */
public class SearchUserCommand implements Command {
    /**
     * Tour data access object.
     */
    private CustomerDAO customerDAO;

    /**
     * Creates a new search user command.
     *
     * @param daoFactory DAO factory used by command.
     */
    public SearchUserCommand(DAOFactory daoFactory) {
        customerDAO = daoFactory.getCustomerDAO();
    }

    /**
     * Converts a customers to JSON array.
     *
     * @param customers customers.
     * @return JSON array.
     */
    private JSONArray customersToJSONArray(Customer[] customers) {
        JSONArray customersJSON = new JSONArray();
        JSONObject customerJSON;

        for (Customer customer : customers) {
            customerJSON = new JSONObject();
            customerJSON.put("id", customer.getId());
            customerJSON.put("name", customer.getFullName());
            customerJSON.put("email", customer.getEmail());
            customerJSON.put("money", customer.getMoney());
            customerJSON.put("discount", customer.getDiscount());

            customersJSON.put(customerJSON);
        }

        return customersJSON;
    }

    @Override
    public JSONObject execute(HttpServletRequest req, HttpServletResponse resp, JSONObject body) {
        JSONObject res = new JSONObject();

        String keyword = body.getString("keyword");
        Customer[] users = customerDAO.findCustomersByName(keyword);

        res.put("success", true);
        res.put("users", customersToJSONArray(users));

        return res;
    }
}
