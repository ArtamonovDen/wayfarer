package app.servlets;

import app.entity.Status;
import app.entity.supEngineer;
import app.model.Model;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class getStaffServlet extends HttpServlet {

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

//TODO trycatch i parseInt
        Model prototype = Model.getInstance();


        if(req.getParameter("id").isEmpty()){
            //Send error and alert in the page
            resp.getWriter().print("Enter id");
            resp.setStatus(204);
            return;
        }

        int id;
        try{
             id= Integer.parseInt(req.getParameter("id"));
        }catch (Exception e){
            resp.getWriter().print("Enter id");
            resp.setStatus(204);
            return;
        }

        ArrayList<supEngineer>  avStaff;
        try {
           avStaff =  prototype.getAvailableAssignees(id);
        }catch(IllegalArgumentException e){
            resp.getWriter().print(e.getMessage());
            resp.setStatus(204);
        }



        int newId = Integer.parseInt(req.getParameter("id"));
        Status status = null;

        try {
            status = Status.getByString(req.getParameter("status"));
        }catch (IllegalArgumentException e) {
            System.out.println("Error in changeStatus: "+e.getMessage());
            req.setAttribute("error",e.getMessage());
            doGet(req, resp);
            return;
        }

        System.out.println("PARAM"+req.getParameter("id"));
        System.out.println("PARAMs"+req.getParameter("ids"));

        resp.setStatus(200);
        resp.getWriter().print(
                "{ \"staff\":[ " +
                        "{ \"firstname\":\"John\", \"lastname\":\"Doe\"}," +
                        "{ \"firstname\":\"Andrew\", \"lastname\":\"HellCat\"}" +
                        "]}"
        );



    }
}
