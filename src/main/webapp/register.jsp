<!DOCTYPE html>
<html lang="en">
<head>
  <!-- Design by foolishdeveloper.com -->
    <title>Login</title>
    <link rel="stylesheet" href="login.css">
    <script src="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/js/all.min.js"></script>
</head>
<body>
    <div class="background">
        <div class="shape"></div>
        <div class="shape"></div>
    </div>
    <form action="regForm" method="post">
        <h3>Register Here</h3>

        <label for="name">Name</label>
        <input type="text" placeholder="Your Name" id="name" name="name1">

        <label for="username">Username</label>
        <input type="text" placeholder="Email or Phone" id="username" name="email1">

        <label for="password">Password</label>
        <input type="password" placeholder="Password" id="password" name="password1">

        <button>Sign Up</button>
        
        <p class="existinguser">Already have an account? <a href="login.jsp">Log in</a></p>
    </form>
</body>
</html>
