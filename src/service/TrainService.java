package service;

import db.DBConnection;
import model.Train;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TrainService {
    public static List<Train> getTrainsByRoute(String source, String destination) throws Exception {
        Connection conn = DBConnection.getConnection();
        String sql = "SELECT * FROM trains WHERE source=? AND destination=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, source);
        ps.setString(2, destination);
        ResultSet rs = ps.executeQuery();

        List<Train> trains = new ArrayList<>();
        while (rs.next()) {
            Train t = new Train(
                    rs.getInt("train_id"),
                    rs.getString("name"),
                    rs.getString("source"),
                    rs.getString("destination"),
                    rs.getString("departure_time"),
                    rs.getInt("total_seats"),
                    rs.getInt("available_seats")
            );
            trains.add(t);
        }
        conn.close();
        return trains;
    }

    public void showTrainRoutes() throws Exception {
        Connection conn = DBConnection.getConnection();
        String query = "SELECT DISTINCT source, destination FROM trains";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        System.out.println("Available Routes:");
        while (rs.next()) {
            System.out.println(rs.getString("source") + " ➝ " + rs.getString("destination"));
        }
        conn.close();
    }



    public static int bookSeat(int userId, int trainId, int seatsToBook) {
        try (Connection conn = DBConnection.getConnection()) {
            // 1. Check seat availability
            String checkQuery = "SELECT total_seats FROM trains WHERE train_id = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setInt(1, trainId);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                int availableSeats = rs.getInt("total_seats");
                if (availableSeats >= seatsToBook) {
                    // 2. Update seats
                    String updateQuery = "UPDATE trains SET total_seats = total_seats - ? WHERE train_id = ?";
                    PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
                    updateStmt.setInt(1, seatsToBook);
                    updateStmt.setInt(2, trainId);
                    updateStmt.executeUpdate();

                    // 3. Insert booking
                    String insertQuery = "INSERT INTO bookings(user_id, train_id, seats_booked) VALUES (?, ?, ?)";
                    PreparedStatement insertStmt = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
                    insertStmt.setInt(1, userId);
                    insertStmt.setInt(2, trainId);
                    insertStmt.setInt(3, seatsToBook);
                    insertStmt.executeUpdate();

                    ResultSet keys = insertStmt.getGeneratedKeys();
                    if (keys.next()) {
                        int ticketId = keys.getInt(1);
                        return ticketId; // ✅ Return ticket ID
                    }
                } else {
                    System.out.println("❌ Not enough seats available.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1; // ❌ Failure
    }


    public static void printTicket(int ticketId) {
        try (Connection conn = DBConnection.getConnection()) {
            String query = """
            SELECT b.booking_id, u.username, t.name AS train_name,
                   t.source, t.destination, t.departure_time, b.seats_booked
            FROM bookings b
            JOIN users u ON b.user_id = u.id
            JOIN trains t ON b.train_id = t.train_id
            WHERE b.booking_id = ?
        """;

            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, ticketId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                System.out.println("\n🎫 Ticket Details");
                System.out.println("Ticket ID   : " + rs.getInt("booking_id"));
                System.out.println("Username    : " + rs.getString("username"));
                System.out.println("Train       : " + rs.getString("train_name"));
                System.out.println("Route       : " + rs.getString("source") + " ➡ " + rs.getString("destination"));
                System.out.println("Time        : " + rs.getString("departure_time"));
                System.out.println("Seats Booked: " + rs.getInt("seats_booked"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // ✅ Show user's bookings
    public static void showBookings(int userId) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT b.booking_id, t.name, t.source, t.destination, t.departure_time, b.seats_booked " +
                    "FROM bookings b JOIN trains t ON b.train_id = t.train_id " +
                    "WHERE b.user_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println("\n🧾 Ticket ID: " + rs.getInt("booking_id"));
                System.out.println("🚆 Train: " + rs.getString("name"));
                System.out.println("📍 Route: " + rs.getString("source") + " ➝ " + rs.getString("destination"));
                System.out.println("🕐 Time: " + rs.getString("departure_time"));
                System.out.println("🎟️ Seats Booked: " + rs.getInt("seats_booked"));
            }

            if (!found) {
                System.out.println("❌ No bookings found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ✅ Cancel ticket
    public static void cancelBooking(int userId, int ticketId) {
        try (Connection conn = DBConnection.getConnection()) {
            // Get seats and train id
            String fetch = "SELECT train_id, seats_booked FROM bookings WHERE booking_id = ? AND user_id = ?";
            PreparedStatement fetchStmt = conn.prepareStatement(fetch);
            fetchStmt.setInt(1, ticketId);
            fetchStmt.setInt(2, userId);
            ResultSet rs = fetchStmt.executeQuery();

            if (rs.next()) {
                int trainId = rs.getInt("train_id");
                int seats = rs.getInt("seats_booked");

                // 1. Delete booking
                String delete = "DELETE FROM bookings WHERE booking_id = ?";
                PreparedStatement delStmt = conn.prepareStatement(delete);
                delStmt.setInt(1, ticketId);
                delStmt.executeUpdate();

                // 2. Add seats back to train
                String update = "UPDATE trains SET total_seats = total_seats + ? WHERE train_id = ?";
                PreparedStatement updStmt = conn.prepareStatement(update);
                updStmt.setInt(1, seats);
                updStmt.setInt(2, trainId);
                updStmt.executeUpdate();

                System.out.println("✅ Booking cancelled successfully.");
            } else {
                System.out.println("❌ Ticket not found or does not belong to you.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
