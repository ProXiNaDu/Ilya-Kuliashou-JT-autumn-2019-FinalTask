/*
 * Get Tour Types Command.
 *
 * Version 1.0
 *
 * Date 20 Jan 2020
 */


package com.epam.travel.commands;

import com.epam.travel.dao.DAOFactory;
import com.epam.travel.dao.TourTypeDAO;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Get Tour Types Command.
 *
 * @version 1.0 20 Jan 2020
 * @author Ilya Kuliashou
 */
public class GetTourTypesCommand implements Command {
    /**
     * Tour type data access object.
     */
    private TourTypeDAO tourTypeDAO;

    /**
     * Creates a new get tour types command.
     *
     * @param daoFactory DAO factory used by command.
     */
    public GetTourTypesCommand(DAOFactory daoFactory) {
        tourTypeDAO = daoFactory.getTourTypeDAO();
    }

    @Override
    public JSONObject execute(HttpServletRequest req, HttpServletResponse resp, JSONObject body) {
        JSONObject res = new JSONObject();
        JSONArray typesJSON = new JSONArray();

        String[] types = tourTypeDAO.selectAllTourTypes();

        for (String type : types) {
            typesJSON.put(type);
        }

        res.put("success", true);
        res.put("types", typesJSON);

        return res;
    }
}
