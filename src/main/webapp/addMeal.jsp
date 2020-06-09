<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Добавить новую запись</title>
</head>
<body>
<form action = "meals" method="post">
    <input required type="text" name="desc" placeholder="Description">
    <input required type="datetime-local" name="datetime" placeholder="Data/Time">
    <input required type="text" name="calories" placeholder="Calories">

    <input type="submit" value="Сохранить">
</form>
</body>
</html>
