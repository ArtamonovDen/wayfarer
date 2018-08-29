package app.servlets;

import app.entity.ExtendedAssignee;
import app.entity.Status;
import app.model.Model;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class getStatusServlet extends HttpServlet {

    private StringBuffer wrapIntoJson( ArrayList<Status> sList){
        StringBuffer jsonWrap = new StringBuffer();
        jsonWrap.append("{ \"nextStatus\":[");
        if (sList==null){
            jsonWrap.append("{\"status\":\"\"}");
        }
        else {
            for(int i=0; i<sList.size();i++ ){
                if(i==sList.size()-1){
                    jsonWrap.append("{\"status\":\""+ sList.get(i)+"\"}");
                }
                else
                    jsonWrap.append("{\"status\":\""+ sList.get(i)+"\"},");
            }
        }

        jsonWrap.append("]}");
        System.out.println(jsonWrap);
        return jsonWrap;
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Model prototype = Model.getInstance();


        if(req.getParameter("id").isEmpty()){
            //Send error and alert in the page
            resp.setStatus(204);
            return;
        }

        int taskId;
        try{
            taskId= Integer.parseInt(req.getParameter("id"));
        }catch (Exception e){
            resp.setStatus(204);
            return;
        }

        ArrayList<Status> avStatus = new ArrayList<>();
        try{
            avStatus = prototype.getAvailableStatus(taskId);
        }catch (IllegalArgumentException ie){
            resp.setStatus(204);
            return;
        }

        resp.setStatus(200);
        resp.getWriter().print(wrapIntoJson(avStatus));


    }


}
