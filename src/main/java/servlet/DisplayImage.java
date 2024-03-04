package servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Set;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/DisplayImage")
public class DisplayImage extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false); // Get existing session without creating a new one
        if (session == null) {
            response.getWriter().println("Session expired or user not logged in.");
            return;
        }

        String userEmail = (String) session.getAttribute("email_key"); // Assuming you stored the email in the session as "email_key"

        String connectionURL = "jdbc:mysql://localhost/imagedb?user=root&password=0800";
        Connection conn = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(connectionURL);

            PreparedStatement statement = conn.prepareStatement("SELECT id FROM images WHERE email = ?");
            statement.setString(1, userEmail);
            ResultSet rs = statement.executeQuery();

            Set<Integer> uniqueImageIds = new HashSet<>();
            while (rs.next()) {
                int id = rs.getInt("id");
                uniqueImageIds.add(id);
            }
            rs.close();
            statement.close();

            // Write the images to the response
            for (int id : uniqueImageIds) {
                statement = conn.prepareStatement("SELECT data FROM images WHERE id = ?");
                statement.setInt(1, id);
                rs = statement.executeQuery();
                if (rs.next()) {
                    byte[] imageData = rs.getBytes("data");
                    response.setContentType("image/jpeg");
                    OutputStream out = response.getOutputStream();
                    out.write(imageData);
                    out.close();
                }
                rs.close();
                statement.close();
            }
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
