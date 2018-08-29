package app.servlets;

import app.entity.Depart;
import app.model.Model;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class changeDepartServlet extends HttpServlet {

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        ArrayList<Depart> avDepart = new ArrayList<>();
        try{
            avDepart =  Model.getInstance().getDepartList();
        }catch (Exception e){
            avDepart = null;
            System.out.println("Error in getting Depart list");
        }
        req.setAttribute("avDepart", avDepart);
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("views/changeDepart.jsp");
        requestDispatcher.forward(req, resp);


    }
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Model prototype = Model.getInstance();
        req.setAttribute("active",1);
        int taskId;
        String newDepName;

        try{
            taskId = Integer.parseInt(req.getParameter("id"));
            newDepName = req.getParameter("depart-name");
        }catch(Exception e){
            req.setAttribute("error","Wrong values");
            doGet(req, resp);
            return;
        }

        if(newDepName==null){
            req.setAttribute("error","Wrong values");
            doGet(req, resp);
            return;
        }

        if(newDepName.isEmpty()){
            req.setAttribute("error","Wrong values");
            doGet(req, resp);
            return;
        }

        try{
            prototype.changeDepart(taskId, newDepName);
        }catch (IllegalArgumentException e){
            req.setAttribute("error",e.getMessage());
            doGet(req, resp);
            return;
        }


        prototype.updatedTaskTable=false;
        req.setAttribute("error",null);
        doGet(req, resp);

    }


}
