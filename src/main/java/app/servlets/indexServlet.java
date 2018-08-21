package app.servlets;

import app.entity.ExtendedTask;
import app.model.Model;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class indexServlet extends HttpServlet {

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException{


        //List<EntityObject> tasks2 =  Model.getInstance().dbManager.obtainAllFromTable(Task.class.getSimpleName());
        List<ExtendedTask> tasks = Model.getInstance().getExtendedTasksList();

        req.setAttribute("tasks", tasks);


        RequestDispatcher requestDispatcher = req.getRequestDispatcher("views/index.jsp");
        requestDispatcher.forward(req, resp);

    }
}
