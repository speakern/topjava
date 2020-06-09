<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<style><%@include file="/WEB-INF/css/table.css"%></style>

<html lang="ru">
<head>
    <title>Users</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>

<table>
    <tr>
        <th>Id</th>
        <th>Description</th>
        <th>Data/Time</th>
        <th>Calories</th>
        <th></th>
        <th></th>
    </tr>
<c:forEach var="meal" items="${mealList}">
    <tr class = "<c:out value="${meal.isExcess() ? 'red' : 'green'}" />">
        <td>${meal.getId()} </td>
        <td>${meal.getDescription()} </td>
        <td>${meal.getDateTime().format(dateTimeFormatter)}</td>
        <td>${meal.getCalories()}</td>
        <td><a href="meals?action=edit&id=<c:out value="${meal.getId()}"/>">Update</a></td>
        <td><a href="meals?action=delete&id=<c:out value="${meal.getId()}"/>">Delete</a></td>
    </tr>
</c:forEach>
</table>
<form action = "addMeal.jsp">
    <input type="submit" value="Добавить новую запись">
</form>
</body>
</html>