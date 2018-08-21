<%@ page import="app.entity.Task" %>
<%@ page import="java.util.List" %>
<%@ page import="app.entity.ExtendedTask" %><%--
  Created by IntelliJ IDEA.
  User: user
  Date: 14.08.2018
  Time: 12:49
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title >Task Manager</title>
    <!--<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">-->
    <style>
      <%@include file="/WEB-INF/style.css"%>
    </style>

    <script>
        function trClick() {

        }
    </script>

  </head>



  <body>
  <header class="head">
    <h1 class="head-title">Task Manager</h1>
  </header>


  <div class = "main">


    <div class="main-bar">
      <button class="main-bar-insert" onclick="location.href='/add'">Add task</button>
      <button class="main-bar-insert" onclick="location.href='/remove'">Remove task</button>
        <button class="main-bar-insert" onclick="location.href='/changeStatus'">Change status</button>



    </div>

    <div class="main-table">

      <%
        List<ExtendedTask> tasks = (List<ExtendedTask>) request.getAttribute("tasks");
        if(tasks != null){
          if(!tasks.isEmpty()){
            out.println("<table id=\"tasks\">");
            out.println("<tr>");
              out.println("<th>ID</th>");
            out.println("<th>Name</th>");
            out.println("<th>Description</th>");
            out.println("<th>Priority</th>");
            out.println("<th>Customer</th>");
            out.println("<th>Depart</th>");
            out.println("<th>Assignee</th>");
            out.println("<th>Status</th>");
            out.println("<th>Create Date</th>");
            out.println("<th>Due Date</th>");
            out.println("<tr>");

            for (ExtendedTask task : tasks) {
              out.println("<tr onclick='trClick()'>");
                out.println("<td>" + task.getId() + "</td>");
              out.println("<td>" + task.getTaskName() + "</td>");
              out.println("<td>" + task.getDescription() + "</td>");
              out.println("<td>" + task.getPriority() + "</td>");
              out.println("<td>" + task.getCustomerName() + "</td>");
              out.println("<td>" + task.getDepartName() + "</td>");
              out.println("<td>" + task.getAssigneeName().getLastName() + "</td>");
              out.println("<td>" + task.getStatus() + "</td>");
              out.println("<td>" + task.getCreatedate() + "</td>");
              out.println("<td>" + task.getDuedate() + "</td>");
              out.println("</tr>");
            }
            out.println("</table>");

          }
          else {
            out.println("No tasks yet");
          }
        }
        else {
          out.println("Null");
        }

      %>
    </div>

  </div>
  </body>

</html>
