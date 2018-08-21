package app.servlets;

import app.entity.Priority;
import app.entity.Status;
import app.entity.Task;
import app.model.Model;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;

public class addServlet extends HttpServlet {

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("views/add.jsp");
        requestDispatcher.forward(req, resp);


    }
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        //creating new Task
        Task comingTask = new Task();
        boolean error = false;
        Model prototype = Model.getInstance();
        req.setAttribute("active",1 );

        try {
            //TODO check depart_id
            //TODO check customer id
            comingTask.setName(
                    req.getParameter("name")
            );
            comingTask.setCustomer_id(
                    Integer.parseInt(req.getParameter("customer_id"))
            );
            comingTask.setDepart_id(
                    Integer.parseInt(req.getParameter("depart_id"))
            );
            comingTask.setPriority(
                    Priority.getByString(req.getParameter("priority"))
            );
            comingTask.setStatus(Status.Open);
            comingTask.setDescription(
                    req.getParameter("description")
            );
            comingTask.setAssignee(
                    prototype.getAssignee(comingTask.getDepart_id())
            );

            Calendar curD = Calendar.getInstance();
            comingTask.setCreatedate(curD.getTime());
            comingTask.setDuedate(
                    prototype.getDueDate(curD,comingTask.getPriority())
                            .getTime()
            );

            prototype.addTask(comingTask);

        }catch (Exception e){
            System.out.println("Error in adding");
            req.setAttribute("task",null );
            doGet(req, resp);
            return;

        }

        prototype.updatedTaskTable = false;
        req.setAttribute("task",comingTask.getName()  );

        req.setAttribute("active",1 );
        doGet(req, resp);

    }


    //TODO css styles
    //TODO check status

    }
