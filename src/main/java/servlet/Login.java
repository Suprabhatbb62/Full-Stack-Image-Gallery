package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/login")
public class Login extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String myemail = req.getParameter("email1");
        String mypass = req.getParameter("password");
        PrintWriter out = res.getWriter();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/imagedb", "root", "0800");
            PreparedStatement ps = con.prepareStatement(" select * from register where email=? and password=? ");
            ps.setString(1, myemail);
            ps.setString(2, mypass);
            ResultSet set = ps.executeQuery();

            if (set.next()) {
                // Create or retrieve session
                HttpSession session = req.getSession(true);

                // Set user attributes in the session
                session.setAttribute("name_key", set.getString("name"));
                session.setAttribute("email_key", set.getString("email")); // You might want to store the email for further use

                RequestDispatcher rd = req.getRequestDispatcher("/index.jsp");
                rd.forward(req, res);
            } else {
                res.setContentType("text/html");
                out.print("<h3 style=\"color: white; text-align: center; margin-top: 20px;\">Login Failed: User info incorrect...</h3>");
                RequestDispatcher rd = req.getRequestDispatcher("/login.jsp");
                rd.include(req, res);
            }

        } catch (Exception e) {
            e.printStackTrace();
            res.setContentType("text/html");
            out.print("<h3>Login Failed, Exception: " + e.getMessage() + "</h3>");
            RequestDispatcher rd = req.getRequestDispatcher("/login.jsp");
            rd.include(req, res);
        }
    }
}
