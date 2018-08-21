<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 19.08.2018
  Time: 16:44
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Remove</title>
    <style>
        <%@include file="/WEB-INF/style.css"%>
    </style>
</head>
<body>
<header class="head">
    <h1 class="head-title">Task Manager</h1>
</header>


<div class="remove-main">
    <form method="post" class="remove-main-form">
        <legend>Remove task:</legend>
        <div>
            <label>ID:
                <input type="text" name="id" >
            </label>

            <button type="submit">Remove</button>
        </div>
    </form>

</div>

<div>
    <button onclick="location.href='/'">Back to main</button>
</div>

</body>
</html>
