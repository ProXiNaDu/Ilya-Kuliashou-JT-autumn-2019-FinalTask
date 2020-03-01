/*
 * Register Command.
 *
 * Version 1.0
 *
 * Date 20 Jan 2020
 */


package com.epam.travel.commands;

import com.epam.travel.dao.CustomerDAO;
import com.epam.travel.dao.DAOFactory;
import com.epam.travel.dao.TourAgentDAO;
import com.epam.travel.travelagency.Customer;
import com.epam.travel.travelagency.TourAgent;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Register Command.
 *
 * @version 1.0 20 Jan 2020
 * @author Ilya Kuliashou
 */
public class RegisterCommand extends LoginCommand implements Command {
    /**
     * Creates a new register command.
     *
     * @param daoFactory DAO factory used by command.
     */
    public RegisterCommand(DAOFactory daoFactory) {
        super(daoFactory);
    }

    /**
     * Register a new tour agent.
     *
     * @param agent new tour agent.
     * @return JSON response.
     */
    private JSONObject registerTourAgent(TourAgent agent) {
        JSONObject res = new JSONObject();

        if (tourAgentDAO.insertTourAgent(agent) == -1) {
            res.put("success", false);
            res.put("msg", "Tour agent has not been inserted.");
        }
        else {
            res = loginTourAgent(agent);
        }

        return res;
    }

    /**
     * Register a new customer.
     *
     * @param customer new customer.
     * @return JSON response.
     */
    private JSONObject registerCustomer(Customer customer) {
        JSONObject res = new JSONObject();

        if (customerDAO.insertCustomer(customer) == -1) {
            res.put("success", false);
            res.put("msg", "Customer has not been inserted.");
        }
        else {
            res = loginCustomer(customer);
        }

        return res;
    }

    /**
     * Register a new user.
     *
     * @param req JSON request.
     * @param session session.
     * @return JSON response.
     */
    private JSONObject register(JSONObject req, HttpSession session) {
        JSONObject res = new JSONObject();

        String fullName = req.getString("username");
        String email = req.getString("email");
        String password = req.getString("password");
        boolean isTourAgent = req.getBoolean("isTourAgent");

        if (tourAgentDAO.findTourAgentByEmail(email) == null &&
                customerDAO.findCustomerByEmail(email) == null) {
            if (isTourAgent) {
                TourAgent tourAgent = new TourAgent(fullName, email, password);
                res = registerTourAgent(tourAgent);
                if (res.getBoolean("success")) {
                    session.setAttribute("user", tourAgent);
                }
            }
            else {
                Customer customer = new Customer(fullName, email, password);
                res = registerCustomer(customer);
                if (res.getBoolean("success")) {
                    session.setAttribute("user", customer);
                }
            }
        }
        else {
            res.put("success", false);
            res.put("msg", "User already exist.");
            session.setAttribute("user", null);
        }

        return res;
    }

    @Override
    public JSONObject execute(HttpServletRequest req, HttpServletResponse resp, JSONObject body) {
        HttpSession session = req.getSession();
        JSONObject res = register(body, session);
        res = authorization(res, resp, session);
        return res;
    }
}
