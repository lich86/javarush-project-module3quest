<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<link href="static/main.css?$v=${startup.time}" rel="stylesheet">
<!DOCTYPE html>
<html>
<head>
    <title>Победа!</title>
    <link href="static/main.css?$v=1.0" rel="stylesheet">
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <script src="<c:url value="/static/jquery-3.6.0.min.js"/>"></script>
</head>
<body>
<h1>Победа!</h1>
<div>Поздравляю, ${username}!</div>
<div>Черный властелин повержен. Вы и ваши спутники возвращаетесь домой героями. Вам почести и слава!</div>
<div><form action="/restart" method="post">
    <input type="submit" value="Сыграть ещё раз">
</form></div>
</body>
</html>
