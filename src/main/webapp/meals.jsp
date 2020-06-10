<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<style><%@include file="/WEB-INF/css/table.css"%></style>

<html lang="ru">
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>

<table>
    <tr>
        <th>Description</th>
        <th>Data/Time</th>
        <th>Calories</th>
        <th></th>
        <th></th>
    </tr>
<c:forEach var="meal" items="${mealList}">
    <tr class = "${meal.excess ? 'red' : 'green'}">
        <td>${meal.description} </td>
        <td>${meal.dateTime.format(dateTimeFormatter)}</td>
        <td>${meal.calories}</td>
        <td><a href="meals?action=edit&id=${meal.id}">Update</a></td>
        <td><a href="meals?action=delete&id=${meal.id}">Delete</a></td>
    </tr>
</c:forEach>
</table>
<a href="meals?action=add">Add</a>
</body>
</html>