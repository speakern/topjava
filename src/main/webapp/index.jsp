<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Java Enterprise (Topjava)</title>
</head>
<body>
<h3>Проект <a href="https://github.com/JavaWebinar/topjava" target="_blank">Java Enterprise (Topjava)</a></h3>
<hr>
<p>Текущий пользователь: ${userId==null? 1: userId}</p>
<ul>
    <li><a href="users">Users</a></li>
    <li><a href="meals">Meals</a></li>
</ul>
<br><br><br>
<form method="get" action="meals">
    <input type="hidden" name="action" value="security">
    Изменить пользователя:
    <p><select name="user">
        <option value="1" selected>user1</option>
        <option value="2">user2</option>
    </select></p>
    <button type="submit">изменить</button>
</form>

</body>
</html>
