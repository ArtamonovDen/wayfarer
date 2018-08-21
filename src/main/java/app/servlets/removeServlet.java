package app.servlets;

import app.model.Model;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class removeServlet extends HttpServlet {

        public void doGet(HttpServletRequest req, HttpServletResponse resp)
                throws ServletException, IOException {
            RequestDispatcher requestDispatcher = req.getRequestDispatcher("views/remove.jsp");
            requestDispatcher.forward(req, resp);


        }
        public void doPost(HttpServletRequest req, HttpServletResponse resp)
                throws ServletException, IOException {
            Model prototype = Model.getInstance();

            try{
                prototype.removeTask(
                        Integer.parseInt(req.getParameter("id"))
                );
            }catch (Exception e){
                System.out.println("Error in removing: "+e.getMessage());
                req.setAttribute("error",1);
            }

            req.setAttribute("error",null);
            req.setAttribute("active",1);
            prototype.updatedTaskTable = false;
            doGet(req, resp);

        }
}
