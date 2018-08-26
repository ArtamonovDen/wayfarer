package app.servlets;

import app.entity.Status;
import app.model.Model;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;

public class changeAssigneeServlet extends HttpServlet {

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("views/changeAssignee.jsp");
        requestDispatcher.forward(req, resp);


    }
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Model prototype = Model.getInstance();
        req.setAttribute("active",1);
        req.setAttribute("error",null);
        doGet(req, resp);

    }


}



