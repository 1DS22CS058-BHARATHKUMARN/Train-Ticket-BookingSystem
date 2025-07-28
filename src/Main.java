import model.Train;
import service.AuthService;
import service.TrainService;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Main {
    static  int loggedInUserId;
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        AuthService auth = new AuthService();

        while (true) {
            System.out.println("\n========= Train Booking System =========");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Check Trains");
            System.out.println("4. Book a seat");
            System.out.println("5. Cancel ticket");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();
            sc.nextLine(); // clear buffer

            switch (choice) {
                case 1:
                    System.out.print("Enter username: ");
                    String regUser = sc.nextLine();
                    System.out.print("Enter password: ");
                    String regPass = sc.nextLine();
                    auth.registerUser(regUser, regPass);
                    break;

                case 2:
                    System.out.print("Enter username: ");
                    String user = sc.nextLine();
                    System.out.print("Enter password: ");
                    String pass = sc.nextLine();
                    int userId = AuthService.loginUser(user, pass);
                    if (userId != -1) {
                        loggedInUserId = userId; // ✅ Set here!
                        System.out.println("✅ Login successful!");
                    } else {
                        System.out.println("❌ Invalid username or password.");
                    }
                    break;
                case 3:

                    if (loggedInUserId == -1) {
                        System.out.println("⚠️ Please login first.");
                        break;
                    }

                    TrainService trainService = new TrainService();

                    try {
                        trainService.showTrainRoutes();

                        System.out.print("Enter source: ");
                        String source = sc.nextLine();
                        System.out.print("Enter destination: ");
                        String destination = sc.nextLine();

                        List<Train> trains = trainService.getTrainsByRoute(source, destination);

                        if (trains.isEmpty()) {
                            System.out.println("❌ No trains found for this route.");
                        } else {
                            System.out.println("Available Trains:");
                            for (Train t : trains) {
                                t.display(); // Make sure your Train class has a display() method
                            }
                        }
                    } catch (SQLException e) {
                        System.out.println("Error: " + e.getMessage());
                    }

                    break;

                case 4:
                    if (loggedInUserId == -1) {
                        System.out.println("⚠️ Please login first.");
                        break;
                    }

                    System.out.print("Enter Train ID to book: ");
                    int trainId = sc.nextInt();
                    System.out.print("Enter number of seats to book: ");
                    int seats = sc.nextInt();
                    sc.nextLine(); // consume newline

                    int bookingId = TrainService.bookSeat(loggedInUserId, trainId, seats);
                    if (bookingId != -1) {
                        System.out.println("✅ Booking Successful!");
                        TrainService.printTicket(bookingId); // display ticket details
                    } else {
                        System.out.println("❌ Booking Failed. Not enough seats.");
                    }
                    break;

                case 5:
                    if (loggedInUserId == -1) {
                        System.out.println("⚠️ Please login first.");
                        break;
                    }

                    TrainService.showBookings(loggedInUserId);
                    System.out.print("\nDo you want to cancel a ticket? (yes/no): ");
                    String confirm = sc.nextLine();

                    if (confirm.equalsIgnoreCase("yes")) {
                        System.out.print("Enter Ticket ID to cancel: ");
                        int ticketId = sc.nextInt();
                        sc.nextLine(); // consume newline
                        TrainService.cancelBooking(loggedInUserId, ticketId);
                    } else {
                        System.out.println("❌ Cancellation aborted.");
                    }
                    break;

                case 6:
                    System.out.println("✅ Exiting... Thank you!");
                    sc.close();
                    System.exit(0);
                    break;

                default:
                    System.out.println("❌ Invalid choice. Try again.");
            }
        }
    }
}
