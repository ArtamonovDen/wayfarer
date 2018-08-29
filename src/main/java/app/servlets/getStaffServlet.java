package app.servlets;

import app.entity.ExtendedAssignee;
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

    private StringBuffer wrapIntoJson(ArrayList<ExtendedAssignee> seList){
        StringBuffer jsonWrap = new StringBuffer();
        jsonWrap.append("{ \"staff\":[");
        for(int i=0; i<seList.size();i++ ){
            //"{ \"firstname\":\"John\", \"lastname\":\"Doe\"},"
            if(i==seList.size()-1){
                jsonWrap.append("{\"supname\":\""+ seList.get(i).getFIO()
                        +"\","+"\"id\":\""+ seList.get(i).getId()+"\"}");
            }
            else
                jsonWrap.append("{\"supname\":\""+ seList.get(i).getFIO()+
                        "\","+"\"id\":\""+ seList.get(i).getId()+"\"},");
        }
        jsonWrap.append("]}");
        System.out.println(jsonWrap);

        return jsonWrap;
    }

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

        ArrayList<ExtendedAssignee>  avStaff = new ArrayList<>();
        try {
           avStaff =  prototype.getAvailableAssignees(id);
        }catch(IllegalArgumentException e){
            resp.getWriter().print(e.getMessage());
            resp.setStatus(204);
            return;
        }

        resp.setStatus(200);
        resp.getWriter().print(wrapIntoJson(avStaff));


    }
}
