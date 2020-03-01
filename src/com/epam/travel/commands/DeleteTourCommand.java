/*
 * Delete Tour Command.
 *
 * Version 1.0
 *
 * Date 20 Jan 2020
 */


package com.epam.travel.commands;

import com.epam.travel.dao.DAOFactory;
import com.epam.travel.dao.TourDAO;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Delete Tour Command.
 *
 * @version 1.0 20 Jan 2020
 * @author Ilya Kuliashou
 */
public class DeleteTourCommand implements Command {
    /**
     * Tour data access object.
     */
    private TourDAO tourDAO;

    /**
     * Creates a new delete tour command.
     *
     * @param daoFactory DAO factory used by command.
     */
    public DeleteTourCommand(DAOFactory daoFactory) {
        tourDAO = daoFactory.getTourDAO();
    }

    @Override
    public JSONObject execute(HttpServletRequest req, HttpServletResponse resp, JSONObject body) {
        JSONObject res = new JSONObject();

        int id = body.getInt("id");
        if (tourDAO.deleteTour(id)) {
            res.put("success", true);
        }
        else {
            res.put("success", false);
            res.put("msg", "Tour has not been deleted.");
        }

        return res;
    }
}
