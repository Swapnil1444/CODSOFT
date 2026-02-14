import java.sql.*;
import java.util.*;
import java.io.*;


class DBConnection {
    public static Connection getConnection() throws Exception {
        String url = "jdbc:mysql://localhost:3306/reservationdb";
        String user = "root";
        String password = "9309";   
        

        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(url, user, password);
    }
}


class User {

    public static boolean login(String username, String password) {
        try {
            Connection con = DBConnection.getConnection();
            String query = "SELECT * FROM users WHERE username=? AND password=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                con.close();
                return true;
            }
            con.close();

        } catch (Exception e) {
            System.out.println("Login Error: " + e.getMessage());
        }
        return false;
    }
}


class Reservation {

    private static int counter = 100;
    private String pnr;
    private String passengerName;
    private String source;
    private String destination;
    private String date;

    public Reservation(String passengerName, String source, String destination, String date) {
        this.pnr = "PNR" + counter++;
        this.passengerName = passengerName;
        this.source = source;
        this.destination = destination;
        this.date = date;
    }

    public String getPnr() {
        return pnr;
    }

    public void saveToFile() {
        try {
            FileWriter fw = new FileWriter("reservations.txt", true);
            fw.write(pnr + "," + passengerName + "," + source + "," + destination + "," + date + "\n");
            fw.close();
        } catch (IOException e) {
            System.out.println("File Error: " + e.getMessage());
        }
    }

    public void saveToDatabase() {
        try {
            Connection con = DBConnection.getConnection();
            String query = "INSERT INTO reservations(pnr, passenger_name, source, destination, journey_date) VALUES (?,?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, pnr);
            ps.setString(2, passengerName);
            ps.setString(3, source);
            ps.setString(4, destination);
            ps.setString(5, date);

            ps.executeUpdate();
            con.close();

        } catch (Exception e) {
            System.out.println("Database Error: " + e.getMessage());
        }
    }

    public static void viewFromDatabase() {
        try {
            Connection con = DBConnection.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM reservations");

            System.out.println("\n===== ALL RESERVATIONS =====");
            while (rs.next()) {
                System.out.println("PNR: " + rs.getString("pnr"));
                System.out.println("Passenger: " + rs.getString("passenger_name"));
                System.out.println("From: " + rs.getString("source"));
                System.out.println("To: " + rs.getString("destination"));
                System.out.println("Date: " + rs.getString("journey_date"));
                System.out.println("------------------------");
            }

            con.close();
        } catch (Exception e) {
            System.out.println("View Error: " + e.getMessage());
        }
    }

    public static void cancelReservation(String pnr) {
        try {
            Connection con = DBConnection.getConnection();
            String query = "DELETE FROM reservations WHERE pnr=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, pnr);

            int rows = ps.executeUpdate();
            if (rows > 0)
                System.out.println("Ticket Cancelled Successfully!");
            else
                System.out.println("PNR Not Found!");

            con.close();
        } catch (Exception e) {
            System.out.println("Cancel Error: " + e.getMessage());
        }
    }
}


public class Online_reservation_system {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("===== ONLINE RESERVATION SYSTEM =====");

        System.out.print("Username: ");
        String username = sc.nextLine();

        System.out.print("Password: ");
        String password = sc.nextLine();

        if (!User.login(username, password)) {
            System.out.println("Invalid Login!");
            return;
        }

        System.out.println("Login Successful!");

        while (true) {
            System.out.println("\n1. Book Ticket");
            System.out.println("2. View Reservations");
            System.out.println("3. Cancel Ticket");
            System.out.println("4. Exit");
            System.out.print("Choose: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:
                    System.out.print("Passenger Name: ");
                    String name = sc.nextLine();

                    System.out.print("From: ");
                    String from = sc.nextLine();

                    System.out.print("To: ");
                    String to = sc.nextLine();

                    System.out.print("Journey Date: ");
                    String date = sc.nextLine();

                    Reservation r = new Reservation(name, from, to, date);

                    r.saveToFile();        
                    r.saveToDatabase();    

                    System.out.println("Ticket Booked Successfully!");
                    System.out.println("Generated PNR: " + r.getPnr());
                    break;

                case 2:
                    Reservation.viewFromDatabase();
                    break;

                case 3:
                    System.out.print("Enter PNR to Cancel: ");
                    String pnr = sc.nextLine();
                    Reservation.cancelReservation(pnr);
                    break;

                case 4:
                    System.out.println("Thank You!");
                    System.exit(0);

                default:
                    System.out.println("Invalid Choice!");
            }
        }
    }
}
