<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <title>Вы проиграли!</title>
  <link href="static/main.css?$v=1.0" rel="stylesheet">
  <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
  <script src="<c:url value="/static/jquery-3.6.0.min.js"/>"></script>
</head>
<body>
<h1>О нет!</h1>
<div>${loosingCause}</div>
<div><form action="/restart" method="post">
  <input type="submit" value="Сыграть ещё раз">
</form></div>
</body>
</html>
