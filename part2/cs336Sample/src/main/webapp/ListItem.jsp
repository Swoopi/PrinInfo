<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>List an Item</title>
</head>
<body>
    <h1>List Your Item for Auction</h1>
    <form action="ListItemServlet" method="POST">
        Item Name: <input type="text" name="name" required><br>
        Category: <select name="category">
                    	<option>Ballpoint</option>
                    	<option>Fountain</option>
                    	<option>Rollerballr</option>
                    	<option>Fineliner</option>
                    	<option>Patron of Art</option>
                    	<option>Mechanical Pencil</option>
                    </select>
                    <br>
        Collection: <select name="collection">
                    	<option>Meisterstuck</option>
                    	<option>Great Characters</option>
                    	<option>Starwalker</option>
                    	<option>Writer's Edition</option>
                    	<option>Patron of Art</option>
                    	<option>High Artistry</option>
                    	<option>Mechanical Pencil</option>                    	
                    </select>
                    <br>
        Brand: <input type="text" name="name" required><br>
        Features(csv): <input type="text" name="ft" required><br>
        Feature Values (csv): <input type="text" name="ftval" required><br>
        Year Manufactured: <input type="number" name="myr" step="1" required><br>
        Year Purchased: <input type="number" name="pyr" step="1" required><br>
        Original MSRP: <input type="number" name="msrp" step="0.01" required><br>
        Starting Price: <input type="number" name="startingPrice" step="0.01" required><br>
        Bid Increment: <input type="number" name="bidIncrement" step="0.01" required><br>
        Minimum Price: <input type="number" name="minimumPrice" step="0.01" required><br>
        Closing Time: <input type="datetime-local" name="closingTime" required><br>
        <input type="submit" value="List Item">
    </form>
</body>
</html>
