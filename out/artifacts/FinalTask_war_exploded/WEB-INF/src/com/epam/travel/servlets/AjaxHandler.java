/*
 * AjaxHandler
 *
 * Version 1.0
 *
 * Date 20 Jan 2020
 */


package com.epam.travel.servlets;

import com.epam.travel.commands.Command;
import com.epam.travel.commands.CommandFactory;
import com.epam.travel.commands.UnknownActionCommand;
import com.epam.travel.dao.DAOFactory;
import com.epam.travel.managers.ConnectionManager;
import com.epam.travel.travelagency.Person;
import com.epam.travel.travelagency.TourAgent;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.stream.Collectors;

/**
 * Servlet processing AJAX requests.
 *
 * @version 1.0 20 Jan 2020
 * @author Ilya Kuliashou
 */
@WebServlet(urlPatterns = { "/CustomerHandler", "/AgentHandler"})
public class AjaxHandler extends HttpServlet {
    /**
     * Logger.
     */
    private static final Logger log = LogManager.getLogger(AjaxHandler.class);

    /**
     * Data access objects factory.
     */
    DAOFactory daoFactory;

    @Override
    public void init() throws ServletException {
        super.init();

        daoFactory = DAOFactory.getDAOFactory(DAOFactory.MYSQL);
        if (daoFactory == null) {
            log.fatal("DAO not found.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession();
        String reqBody = req.getReader().lines().collect(
                Collectors.joining(System.lineSeparator()));
        JSONObject reqJSON = new JSONObject(reqBody);
        JSONObject resJSON;

        Person user = (Person) session.getAttribute("user");
        CommandFactory.Factories whichFactory =
                (user != null && user.getClass() == TourAgent.class) ?
                CommandFactory.Factories.TOUR_AGENT :
                CommandFactory.Factories.CUSTOMER;

        Command command;
        CommandFactory commandFactory =
                CommandFactory.getCommandFactory(whichFactory, daoFactory);
        if (commandFactory != null) {
            command = commandFactory.getCommand(reqJSON.getString("action").toUpperCase());
        }
        else {
            command = new UnknownActionCommand();
        }

        resJSON = command.execute(req, resp, reqJSON);

        resp.setContentType("application/json;charset=utf-8");
        resp.setHeader("Cache-Control", "no-cache");
        resp.setHeader("Pragma", "no-cache");
        resp.getWriter().write(resJSON.toString());
    }

    @Override
    public void destroy() {
        super.destroy();

        ConnectionManager.getInstance().close();
    }
}
