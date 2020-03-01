/*
 * Command.
 *
 * Version 1.0
 *
 * Date 20 Jan 2020
 */


package com.epam.travel.commands;

import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Command interface.
 *
 * @version 1.0 20 Jan 2020
 * @author Ilya Kuliashou
 */
public interface Command {
    /**
     * Execute command.
     *
     * @param req servlet request.
     * @param resp servlet response.
     * @param body request body.
     * @return JSON response.
     */
    JSONObject execute(HttpServletRequest req, HttpServletResponse resp, JSONObject body);
}
