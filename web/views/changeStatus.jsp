<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 19.08.2018
  Time: 17:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Change Status</title>
    <style>
        <%@include file="/WEB-INF/style.css"%>
    </style>
</head>
<body>

<header class="head">
    <h1 class="head-title">Task Manager</h1>
</header>
<%

    if(request.getAttribute("active")!=null){
        if(request.getAttribute("error")==null){
            out.println("<h2>Changed successfully</h2>");
        }
        else
            out.println("<h2>Error."+(String)request.getAttribute("error")+"</h2>");

    }



%>

<div class="change-main">
    <form method="post" class="change-main-form">
        <legend>Change status:</legend>
        <div>
            <label>ID:
                <input type="text" name="id" >
            </label>
            <label>New status:
                <select type="text" name="status">
                    <option>Open</option>
                    <option>Accepted</option>
                    <option>Assessment</option>
                    <option>In_progress</option>
                    <option>Technical_review</option>
                    <option>AIR</option>
                    <option>Resolved</option>
                    <option>Rejected</option>
                    <option>Close</option>
                    <option>Reopen</option>
                    <option>Move</option>
                </select>
            </label>

            <button type="submit">Change</button>
        </div>
    </form>

</div>

<div>
    <button onclick="location.href='/'">Back to main</button>
</div>

</body>
</html>
