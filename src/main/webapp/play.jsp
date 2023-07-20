<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Квест Lord of the wings</title>
    <link href="static/main.css?v=1.0" rel="stylesheet">
</head>
<body>

<c:if test="${question == null}">
    <h1>Пролог</h1>
    <br/>
    <div> Ты маленький хоббит который живёт в норе под горой. Однажды твой друг маг сообщает тебе, что кольцо, которое ты хранишь у себя уже много лет — могущественный артефакт, который необходимо уничтожить в пламени огненной горы. Неожиданно для самого себя ты оказываешься перед нелёгким выбором: отправиться в путешествие в страну мрака в окружении верных спутников: эльфа Гондураса, человека Барагоза, гнома Сталина и мага Пенделя или остаться дома. От того, какие решения ты будешь принимать, зависит судьба всего Мимоземелья. Кстати, а как тебя зовут?</div>
    <br>

    <div>
        <form id="login" action="/game" method="post">
            <label>Представься:</label><br>
            <input type="text" name="username" id="username"><br><br>
            <input type="submit" value="Отправить">
        </form>
    </div>
</c:if>

<c:if test="${question != null}">
    <h1>Глава ${chapterNumber}</h1>
    <br/>

    <div>${question.text}</div>

    <div>
        <form action="/game" method="post">
            <c:forEach items="${answers}" var="answer">
                <label><input type="radio" name="answerid" value="${answer.id}">${answer.text}</label><br><br>
            </c:forEach>
            <button type="submit">Ответить</button>
        </form>
    </div>

</c:if>

    <h3>Статистика</h3>
    <div>
        Количество сыгранных игр: ${cookie.counter.value}<br>
        Побед: ${cookie.counterWon.value}<br>
        Поражений: ${cookie.counterLost.value}<br>
        Ваш IP адрес: ${IP}
    </div>


</body>
</html>
