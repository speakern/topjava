<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Добавить\изменить запись</title>
</head>
<body>
<form action = "meals" method="post">
    <input type="hidden" readonly="readonly" name="id"
           value="${meal.id}"
    />
    <input required type="text" name="desc" placeholder="Description"
           value="${meal.description}"
    />
    <input required type="datetime-local" name="datetime" placeholder="Data/Time"
           value="${meal.dateTime}"
    />
    <input required type="number" name="calories" placeholder="Calories"
           value="${meal.calories}"
    />
    <input type="submit" value="Сохранить">
</form>

</body>
</html>
