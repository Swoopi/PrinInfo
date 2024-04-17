<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>List an Item</title>
</head>
<body>
    <h1>List Your Item for Auction</h1>
    <form action="ListItemServlet" method="POST">
        Title: <input type="text" name="title" required><br>
        Description: <textarea name="description" required></textarea><br>
        Starting Price: <input type="number" name="startingPrice" step="0.01" required><br>
        Bid Increment: <input type="number" name="bidIncrement" step="0.01" required><br>
        Minimum Price: <input type="number" name="minimumPrice" step="0.01" required><br>
        Closing Time: <input type="datetime-local" name="closingTime" required><br>
        <input type="submit" value="List Item">
    </form>
</body>
</html>
