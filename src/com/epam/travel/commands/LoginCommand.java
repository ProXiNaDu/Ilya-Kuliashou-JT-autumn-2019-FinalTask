/*
 * Login Command.
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
import com.epam.travel.travelagency.Person;
import com.epam.travel.travelagency.TourAgent;
import org.json.JSONObject;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Login Command.
 *
 * @version 1.0 20 Jan 2020
 * @author Ilya Kuliashou
 */
public class LoginCommand implements Command {
    /**
     * Tour agent page address.
     */
    private static final String TOUR_AGENT_PAGE = "./pages/agents.html";

    /**
     * Customer data access object.
     */
    protected CustomerDAO customerDAO;
    /**
     * Tour agent data access object.
     */
    protected TourAgentDAO tourAgentDAO;

    /**
     * Creates a new login command.
     *
     * @param daoFactory DAO factory used by command.
     */
    public LoginCommand(DAOFactory daoFactory) {
        customerDAO = daoFactory.getCustomerDAO();
        tourAgentDAO = daoFactory.getTourAgentDAO();
    }

    /**
     * Creates cookies for automatic login.
     *
     * @param user user.
     * @param res http servlet response.
     */
    private void setUserCookie(Person user, HttpServletResponse res) {
        if (user != null) {
            Cookie emailCookie = new Cookie("email", user.getEmail());
            Cookie passCookie = new Cookie("pass", user.getPassword());
            emailCookie.setPath("/");
            passCookie.setPath("/");
            res.addCookie(emailCookie);
            res.addCookie(passCookie);
        }
    }

    /**
     * Login tour agent.
     *
     * @param agent tour agent.
     * @return JSON response.
     */
    protected JSONObject loginTourAgent(TourAgent agent) {
        JSONObject res = new JSONObject();
        JSONObject user = new JSONObject();
        user.put("name", agent.getFullName());
        res.put("success", true);
        res.put("user", user);
        return res;
    }

    /**
     * Login customer.
     *
     * @param customer customer.
     * @return JSON response.
     */
    protected JSONObject loginCustomer(Customer customer) {
        JSONObject res = new JSONObject();
        res.put("success", true);
        res.put("user", customer.toJSON());
        return res;
    }

    /**
     * Login.
     *
     * @param req JSON request.
     * @param session session.
     * @return JSON response.
     */
    private JSONObject login(JSONObject req, HttpSession session) {
        JSONObject res = new JSONObject();
        String email = req.getString("email");
        String password = req.getString("password");

        TourAgent agent = tourAgentDAO.findTourAgentByEmail(email);
        if (agent != null && agent.getPassword().equals(password)) {
            res = loginTourAgent(agent);
            if (res.getBoolean("success")) {
                session.setAttribute("user", agent);
            }
            return res;
        }

        Customer customer = customerDAO.findCustomerByEmail(email);
        if (customer != null && customer.getPassword().equals(password)) {
            res = loginCustomer(customer);
            if (res.getBoolean("success")) {
                session.setAttribute("user", customer);
            }
            return res;
        }

        res.put("success", false);
        res.put("msg", "User not found.");
        session.setAttribute("user", null);
        session.setMaxInactiveInterval(0);

        return res;
    }

    /**
     * Authorize user.
     *
     * @param resJSON JSON response.
     * @param resp HTTP response
     * @param session HTTP session.
     * @return JSON response.
     */
    protected JSONObject authorization(JSONObject resJSON, HttpServletResponse resp, HttpSession session) {
        Person user = (Person) session.getAttribute("user");
        setUserCookie(user, resp);
        if (user != null && user.getClass() == TourAgent.class) {
            resJSON.put("success", true);
            resJSON.put("url", TOUR_AGENT_PAGE);
        }
        return resJSON;
    }

    @Override
    public JSONObject execute(HttpServletRequest req, HttpServletResponse resp, JSONObject body) {
        HttpSession session = req.getSession();
        JSONObject res = login(body, session);
        res = authorization(res, resp, session);
        return res;
    }
}
