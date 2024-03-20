package org.example;

import java.sql.*;

public class PostgreSQLJDBCConnection {

    public static String url = "jdbc:postgresql://localhost:5432/school";
    public static String user = "postgres";
    public static String password = "Yusuf1234";

    public Connection conn;




    public void getAllStudents() {
        try {
            Statement stmt = conn.createStatement(); // Execute SQL query
            String SQL = "SELECT * FROM students";
            ResultSet rs = stmt.executeQuery(SQL); // Process the result
            while (rs.next()) {
                String first_name = rs.getString("first_name");
                String last_name = rs.getString("last_name");
                String email = rs.getString("email");
                Date enrollment_date = rs.getDate("enrollment_date");
                System.out.println("Name: " + first_name + " " + last_name + "\nEmail: " + email + "\nDate Enrolled: " + enrollment_date);
            }
            // Close resources
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("SQL Exception in return");
        }
    }

    public void addStudent(String first_name, String last_name, String email, Date enrollment_date) {

            String insert = "INSERT INTO students (first_name, last_name, email, enrollement_date) VALUES(?,?,?,?)";
        try ( PreparedStatement pstmt = conn.prepareStatement(insert)){
                pstmt.setString(1, first_name);
                pstmt.setString(2, last_name);
                pstmt.setString(3, email);
                pstmt.setDate(4, enrollment_date);
                pstmt.executeUpdate();
                System.out.println("Data inserted using PreparedStatement.");
                getAllStudents();
        } catch (SQLException e) {
            System.out.println("SQL Exception in create");
        }
    }

    public void updateStudentEmail(int student_id, String new_email) {
        String SQL = "UPDATE students SET email = ? WHERE student_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setString(1, new_email);
            pstmt.setInt(2, student_id);
            int emailRowCount = pstmt.executeUpdate();
            if (emailRowCount > 0){
                System.out.println("Student: " + student_id + " has changed their email to " + new_email);
                getAllStudents();
            } else {
                System.out.println("Student with ID: " + student_id + " was not found");
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception in update");
        }
    }

    public void deleteStudent(int student_id) {
        String SQL = "DELETE FROM students WHERE student_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, student_id);
            int rowDeleted = pstmt.executeUpdate();
            if (rowDeleted > 0){
                System.out.println("Student: " + student_id + " has been deleted");
                getAllStudents();
            } else {
                System.out.println("Student with ID: " + student_id + " was not found");
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception in delete");
        }
    }

    public static void main(String[] args) {
// JDBC & Database credentials

        try { // Load PostgreSQL JDBC Driver
            Class.forName("org.postgresql.Driver");
// Connect to the database
            Connection conn = DriverManager.getConnection(url, user, password);
            if (conn != null) {
                System.out.println("Connected to PostgreSQL successfully!");

            } else {
                System.out.println("Failed to establish connection.");
            } // Close the connection (in a real scenario, do this in a finally
            conn.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}