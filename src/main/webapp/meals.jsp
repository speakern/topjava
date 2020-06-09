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
        <td>${meal.getDateTimeToString()}</td>
        <td>${meal.getCalories()}</td>
        <td>
            <form action = "updateMeal.jsp" method="post">
                <input type="hidden" name="id" value="${user.getId()}">
                <input type="hidden" name="name" value="${user.getName()}">
                <input type="hidden" name="age" value="${user.getAge()}">
                <input type="submit" value="Изменить" style="float:left">
            </form>
        </td>
        <td></td>
    </tr>
</c:forEach>
</table>
<form action = "addMeal.jsp">
    <input type="submit" value="Добавить новую запись">
</form>
</body>
</html>