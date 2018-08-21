package app.servlets;

import app.entity.Status;
import app.model.Model;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class changeStatusServlet extends HttpServlet {

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("views/changeStatus.jsp");
        requestDispatcher.forward(req, resp);


    }
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Model prototype = Model.getInstance();

        int newId = Integer.parseInt(req.getParameter("id"));
        Status status = null;
        req.setAttribute("active",1);

        try {
            status = Status.getByString(req.getParameter("status"));
        }catch (IllegalArgumentException e) {
            System.out.println("Error in changeStatus: "+e.getMessage());
            req.setAttribute("error",e.getMessage());
            doGet(req, resp);
            return;
        }

        try{
            prototype.changeStatus(newId, status);

        }catch (IllegalArgumentException e){
            System.out.println("Error in changeStatus: "+e.getMessage());
            req.setAttribute("error",e.getMessage());
            doGet(req, resp);
            return;
        }

        prototype.updatedTaskTable = false;
        req.setAttribute("error",null);
        doGet(req, resp);

    }
}
