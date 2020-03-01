/*
 * Get All Tours Command.
 *
 * Version 1.0
 *
 * Date 20 Jan 2020
 */


package com.epam.travel.commands;

import com.epam.travel.dao.DAOFactory;
import com.epam.travel.dao.TourDAO;
import com.epam.travel.travelagency.Tour;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Get All Tours Command.
 *
 * @version 1.0 20 Jan 2020
 * @author Ilya Kuliashou
 */
public class GetAllToursCommand implements Command {
    /**
     * Tour data access object.
     */
    private TourDAO tourDAO;

    /**
     * Creates a new GetCustomerToursCommand.
     *
     * @param daoFactory DAO factory used by command.
     */
    public GetAllToursCommand(DAOFactory daoFactory) {
        tourDAO = daoFactory.getTourDAO();
    }

    @Override
    public JSONObject execute(HttpServletRequest req, HttpServletResponse resp, JSONObject body) {
        JSONObject res = new JSONObject();

        Tour[] tours = tourDAO.selectAllTours();

        res.put("success", true);
        res.put("tours", Tour.toursToJSONArray(tours));

        return res;
    }
}
