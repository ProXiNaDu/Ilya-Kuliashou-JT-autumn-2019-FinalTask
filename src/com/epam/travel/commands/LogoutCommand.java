/*
 * Logout Command.
 *
 * Version 1.0
 *
 * Date 20 Jan 2020
 */


package com.epam.travel.commands;

import com.epam.travel.travelagency.Person;
import com.epam.travel.travelagency.TourAgent;
import org.json.JSONObject;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Logout Command.
 *
 * @version 1.0 20 Jan 2020
 * @author Ilya Kuliashou
 */
public class LogoutCommand implements Command {
    /**
     * Removes cookies for automatic login.
     *
     * @param req HttpServletRequest.
     * @param resp HttpServletResponse.
     */
    private void removeUserCookie(HttpServletRequest req, HttpServletResponse resp) {
        Cookie[] cookies = req.getCookies();
        if (cookies == null) {
            return;
        }
        for (Cookie cookie : cookies) {
            if ("email".equals(cookie.getName()) ||
                    "pass".equals(cookie.getName())) {
                cookie.setValue("");
                cookie.setPath("/");
                cookie.setMaxAge(0);
                resp.addCookie(cookie);
            }
        }
    }

    @Override
    public JSONObject execute(HttpServletRequest req, HttpServletResponse resp, JSONObject body) {
        HttpSession session = req.getSession();
        Person user = (Person)session.getAttribute("user");
        session.setAttribute("user", null);
        session.setMaxInactiveInterval(0);
        removeUserCookie(req, resp);
        JSONObject res = new JSONObject();
        res.put("success", true);
        if (user != null && user.getClass() == TourAgent.class) {
            res.put("url", "../index.html");
        }
        return res;
    }
}
