package app.servlets;

import app.model.Model;
import app.statistics.StatisticsPerWeek;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class statisticsServlet extends HttpServlet {

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("statistics",Model.getInstance().getStatistics());
        req.setAttribute("assigneeStatistics",Model.getInstance().getExtendedAssigneeStatistics());

        RequestDispatcher requestDispatcher = req.getRequestDispatcher("views/statistics.jsp");
        requestDispatcher.forward(req, resp);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {


    }
}

