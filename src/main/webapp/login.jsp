<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Zaloguj się</title>
</head>
<body>
        <form action="/login" method="post">
            <h1>Logowanie</h1>
            <div class="form-group">
                <input type="text" class="form-control" id="name" name="name" placeholder="Nazwa użytkownika">
            </div>
            <div class="form-group">
                <input type="password" id="password" name="password" placeholder="podaj hasło">
            </div>
            <button type="submit">Zaloguj</button>
        </form>
</body>
</html>
