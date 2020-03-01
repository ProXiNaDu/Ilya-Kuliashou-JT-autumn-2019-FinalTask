/*
 * Edit Tour Command.
 *
 * Version 1.0
 *
 * Date 20 Jan 2020
 */


package com.epam.travel.commands;

import com.epam.travel.dao.DAOFactory;
import com.epam.travel.dao.TourDAO;
import com.epam.travel.dao.TourTypeDAO;
import com.epam.travel.travelagency.Tour;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Edit Tour Command.
 *
 * @version 1.0 20 Jan 2020
 * @author Ilya Kuliashou
 */
public class EditTourCommand implements Command {
    /**
     * Tour data access object.
     */
    private TourDAO tourDAO;
    /**
     * Tour type data access object.
     */
    private TourTypeDAO tourTypeDAO;

    /**
     * Creates a new edit tour command.
     *
     * @param daoFactory DAO factory used by command.
     */
    public EditTourCommand(DAOFactory daoFactory) {
        tourDAO = daoFactory.getTourDAO();
        tourTypeDAO = daoFactory.getTourTypeDAO();
    }

    @Override
    public JSONObject execute(HttpServletRequest req, HttpServletResponse resp, JSONObject body) {
        JSONObject res = new JSONObject();

        int id = body.getInt("id");
        String name = body.getString("name");
        String city = body.getString("city");
        String type = body.getString("type");
        if (tourTypeDAO.findTourTypeByName(type) == -1) {
            tourTypeDAO.insertTourType(type);
        }
        String description = body.getString("description");
        boolean isLastMinute = body.getBoolean("isLastMinute");
        int price = body.getInt("price");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        try {
            date = dateFormat.parse(body.getString("date"));

            Tour tour = new Tour(name, city, type, price, date);
            tour.setId(id);
            tour.setLastMinutePackage(isLastMinute);
            tour.setDescription(description);
            if (tourDAO.updateTour(tour)) {
                res.put("success", true);
            }
            else {
                res.put("success", false);
                res.put("msg", "Tour has not been updated.");
            }
        } catch (ParseException e) {
            res.put("success", false);
            res.put("msg", "Tour has not been updated.");
        }

        return res;
    }
}
