/*
 * AgentsFilter
 *
 * Version 1.0
 *
 * Date 19 Jan 2020
 */


package com.epam.travel.servlets;

import com.epam.travel.travelagency.Person;
import com.epam.travel.travelagency.TourAgent;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * The filter prevents users from accessing the page.
 *
 * @version 1.0 18 Jan 2020
 * @author Ilya Kuliashou
 */
@WebFilter(urlPatterns = { "/pages/agents.html", "/AgentHandler"})
public class AgentsFilter implements Filter {
    /**
     * Logger.
     */
    private static final Logger log = LogManager.getLogger(AgentsFilter.class);

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        log.info("doFilter...");
        HttpSession session =((HttpServletRequest) req).getSession();

        Person user = (Person) session.getAttribute("user");
        if (user == null || user.getClass() != TourAgent.class) {
            log.info("Sending redirect...");
            ((HttpServletResponse) res).sendRedirect("../index.html");
            return;
        }

        chain.doFilter(req, res);
    }
}
