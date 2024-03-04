<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Image Upload and View</title>
<link rel="stylesheet" href="style.css">
</head>
<body>
<div class="container">
        <h2>Welcome ${name_key}</h2>
        <a href="logout">Logout</a>
    </div>
<div class="formcontainer">
        <h1>Upload Image Here</h1>
        <form action="UploadServlet" method="post" enctype="multipart/form-data">
            <div class="formcenter">
                <input type="file" name="file" id="file">
                <input type="submit" value="Upload Image" name="submit">
            </div>
        </form>
    </div>
<h2 style="color: white; text-align: center; margin-top: 40px;">Uploaded Images</h2>

 <div class="lastcontainer">
<%
    java.sql.Connection conn = null;
    java.sql.PreparedStatement stmt = null;
    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = java.sql.DriverManager.getConnection("jdbc:mysql://localhost/imagedb?user=root&password=0800");

        String userEmail = (String) session.getAttribute("email_key"); // Assuming you stored the email in the session as "email_key"
        stmt = conn.prepareStatement("SELECT id, name FROM images WHERE email = ?");
        stmt.setString(1, userEmail);
        java.sql.ResultSet rs = stmt.executeQuery();

        // Create a set to store unique image IDs
        java.util.Set<Integer> uniqueImageIds = new java.util.HashSet<>();
%>
    <div>
    <%
        while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            // Check if the image ID is unique
            if (!uniqueImageIds.contains(id)) {
                uniqueImageIds.add(id);
    %>
                <img src="DisplayImage?id=<%= id %>" alt="<%= name %>" width="200" height="200">
    <%
            }
        }
        rs.close();
    %>
    </div>
<%
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        try {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }
%>
</div>
</body>
</html>
