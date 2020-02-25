package pl.raporty.controllers.menu.main;

import pl.raporty.daos.UserDao;
import pl.raporty.models.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(value = "/login")
public class Login extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String name = request.getParameter("name");
        String password = request.getParameter("password");

        UserDao userDao = new UserDao();
        User user = userDao.authorizeUser(name, password);

        if (user != null) {
            session.setAttribute("login", user);
            response.sendRedirect("/app/index");
        } else {
            getServletContext().getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("login");

        if (user != null) {
            response.sendRedirect("/app/index");
        } else {
            getServletContext().getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
}
