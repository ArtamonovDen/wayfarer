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

public class changeStatusServlet extends HttpServlet {

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("views/changeStatus.jsp");
        requestDispatcher.forward(req, resp);


    }
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Model prototype = Model.getInstance();
        req.setAttribute("active",1);

        if(req.getParameter("id").isEmpty()){
            req.setAttribute("error","No ID");
            doGet(req, resp);
            return;
        }

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
            if (e.getMessage().equals("Wrong status")){

                req.setAttribute("available", prototype.getAvailableStatus(newId));
            }
            doGet(req, resp);
            return;
        }

        //update Statistics if task is closed/moved or reopen
        System.out.println("LLL: "+status.name());
        switch (status){
            case Closed:
            case Move:
            case Reopen://created om created date, but reopen date is another one
                prototype.updateStatistics(Calendar.getInstance(),status );
                break;
        }



        prototype.updatedTaskTable = false;
        req.setAttribute("error",null);
        doGet(req, resp);

    }
}
