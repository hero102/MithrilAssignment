package com.aurionpro.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class jdbc_demo {
public static void main(String[] args) throws ClassNotFoundException, SQLException {
	
	Class.forName("com.mysql.cj.jdbc.Driver");
	Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/school", "root", "Hero@123");
	connection.setAutoCommit(true);  // Ensure this

	System.out.println("Connected to DB successfully");

	// Insert
	String insertQuery = "INSERT INTO student(studentid, rollnumber, name, age, percentage) VALUES (?, ?, ?, ?, ?)";
	PreparedStatement ps = connection.prepareStatement(insertQuery);
//	ps.setString(1, "17");
//	ps.setString(2, "150");
//	ps.setString(3, "Hero");
//	ps.setString(4, "23");
//	ps.setString(5, "73");
//	int rows = ps.executeUpdate();
//	System.out.println(rows + " row inserted successfully");

	// Fetch
	String selectQuery = "SELECT * FROM student";
	Statement st = connection.createStatement();
	ResultSet rs = st.executeQuery(selectQuery);
	while (rs.next()) {
	    System.out.println("Student_ID: " + rs.getInt("studentid") +
	                       " ; Roll_Number: " + rs.getInt("rollnumber") +
	                       " ; Name: " + rs.getString("name"));
	}

	// Update
	String updateQuery = "UPDATE student SET name = ? WHERE rollnumber = ?";
	PreparedStatement ps2 = connection.prepareStatement(updateQuery);
	ps2.setString(1, "PrashantKumar");
	ps2.setString(2, "150");  // Changed to existing roll number
	int updateRows = ps2.executeUpdate();
	System.out.println(updateRows + " row(s) updated");
	
	// Delete
	String deleteQuery = "delete from student WHERE rollnumber = ?";
	PreparedStatement ps3 = connection.prepareStatement(deleteQuery);
	ps3.setString(1, "149");
	int deleteRows = ps3.executeUpdate();
	System.out.println(deleteRows + " row(s) updated");

}
}
