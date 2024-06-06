package it.unisa.control;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import it.unisa.model.UserBean;
import it.unisa.model.UserDao;

@WebServlet("/Login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserDao usDao;

    @Override
    public void init() {
        // Inizializza il DAO con il DataSource (gestito dal server applicativo)
        this.usDao = new UserDao((DataSource) getServletContext().getAttribute("DataSource"));
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String username = request.getParameter("un");
            String password = request.getParameter("pw");
            UserBean user = usDao.doRetrieve(username, password);

            String checkout = request.getParameter("checkout");

            if (user != null && user.isValid()) {
                HttpSession session = request.getSession(true);
                session.setAttribute("currentSessionUser", user);
                if (checkout != null) {
                    response.sendRedirect(request.getContextPath() + "/account?page=Checkout.jsp");
                } else {
                    response.sendRedirect(request.getContextPath() + "/Home.jsp");
                }
            } else {
                response.sendRedirect(request.getContextPath() + "/Login.jsp?action=error");
            }
        } catch (SQLException e) {
            throw new ServletException("Database error while authenticating user", e);
        }
    }
}
