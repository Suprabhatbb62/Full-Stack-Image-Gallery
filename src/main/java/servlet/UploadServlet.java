package servlet;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

@WebServlet("/UploadServlet")
@MultipartConfig(maxFileSize = 16177215) // 16 MB
public class UploadServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false); // Get existing session without creating a new one
        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String userEmail = (String) session.getAttribute("email_key"); // Assuming you stored the email in the session as "email_key"

        String connectionURL = "jdbc:mysql://localhost/imagedb?user=root&password=0800";
        Connection conn = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(connectionURL);

            Part filePart = request.getPart("file");
            InputStream inputStream = filePart.getInputStream();
            String fileName = filePart.getSubmittedFileName();

            String sql = "INSERT INTO images (name, data, email) values (?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, fileName);
            statement.setBlob(2, inputStream);
            statement.setString(3, userEmail);
            statement.executeUpdate();
            statement.close();

            response.sendRedirect(request.getContextPath());
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
