<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 15.08.2018
  Time: 1:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <style>
        <%@include file="/WEB-INF/style.css"%>
    </style>
</head>
<%

    if(request.getAttribute("active")!=null){
        if (request.getAttribute("task")!=null){
            out.println("<h2> Task "+request.getAttribute("task")+" created successfully</h2>");
        }
        else{
            out.println("<h2> Error in adding</h2>");
        }
    }



%>

<body>
<header class="head">
    <h1 class="head-title">Task Manager</h1>
</header>

<div class="add-main">
    <form method="post" class="add-main-form">
        <legend>Create task:</legend>

        <div class="add-row">
            <div class="add-col-25">
                <label for="inputName" >Name:</label>
            </div>
            <div class="add-col-75">
                <input id="inputName" type="text" name="name" >
            </div>
        </div>



        <div class="add-row">
            <div class="add-col-25">
                <label for="inputPriority">Priority:</label>
            </div>
            <div class="add-col-75">
                <select id="inputPriority" type="text" name="priority">
                    <option>minor</option>
                    <option>normal</option>
                    <option>major</option>
                    <option>critical</option>
                    <option>blocker</option>
                </select>
            </div>
        </div>


        <div class="add-row">
            <div class="add-col-25">
                <label for="inputCustomer">Customer ID:</label>
            </div>
            <div class=add-col-75">
                <input id="inputCustomer" type="text" name="customer_id">
            </div>
        </div>


        <div class="add-row">
            <div class="add-col-25">
                <label for="inputDepart" class="add-label">Depart ID:</label>
            </div>
            <div class=add-col-75">
                <input id="inputDepart" type="text" name="depart_id"  >
            </div>
        </div>

        <div class="add-row">
            <div class="add-col-25">
                <label for="descriptionid">Description</label>
            </div>
            <div class=add-col-75">
                <textarea  id="descriptionid" name = "description" cols="40" rows="5" class="add-input-box" style="height:200px" > </textarea>
            </div>
        </div>

        <div class="add-row">
            <button type="submit">Add</button>
        </div>
    </form>

</div>

<div>
    <button onclick="location.href='/'">Back to main</button>
</div>

<script>
    function getDate(){
        return new Date();
    }
</script>


</body>
</html>
