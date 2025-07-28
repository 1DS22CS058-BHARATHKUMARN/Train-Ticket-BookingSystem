package service;

import db.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AuthService {

    public boolean registerUser(String username, String password) {
        try (Connection conn = DBConnection.getConnection()) {
            // Check if username exists
            String checkQuery = "SELECT * FROM users WHERE username = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setString(1, username);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                System.out.println("❌ Username already exists. Try a different one.");
                return false;
            }

            // Insert new user
            String insertQuery = "INSERT INTO users (username, password) VALUES (?, ?)";
            PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
            insertStmt.setString(1, username);
            insertStmt.setString(2, password); // Note: You can hash the password for security
            insertStmt.executeUpdate();

            System.out.println("✅ User registered successfully!");
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public static int loginUser(String username, String password) {
        try (Connection conn = DBConnection.getConnection()) {
            String loginQuery = "SELECT id FROM users WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(loginQuery);
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                System.out.println("✅ Login successful!");
                return rs.getInt("id"); // Return the user's ID
            } else {
                System.out.println("❌ Invalid username or password.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1; // Login failed
    }


}

