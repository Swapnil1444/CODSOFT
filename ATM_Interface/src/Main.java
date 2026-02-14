import java.sql.*;
import java.util.Scanner;

class DBConnection {
    public static Connection getConnection() throws Exception {
        String url = "jdbc:mysql://localhost:3306/atmdb?useSSL=false&serverTimezone=UTC";
        String user = "root";
        String password = "9309";  

        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(url, user, password);
    }
}

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        try {
            Connection con = DBConnection.getConnection();

            System.out.print("User ID: ");
            String userid = sc.nextLine();

            System.out.print("PIN: ");
            String pin = sc.nextLine();

            PreparedStatement loginPs = con.prepareStatement(
                    "SELECT * FROM accounts WHERE userid=? AND pin=?");
            loginPs.setString(1, userid);
            loginPs.setString(2, pin);

            ResultSet rs = loginPs.executeQuery();

            if (!rs.next()) {
                System.out.println("Invalid Login!");
                return;
            }

            double balance = rs.getDouble("balance");
            System.out.println("Login Successful!");

            while (true) {
                System.out.println("\n1.Deposit  2.Withdraw  3.Balance  4.History  5.Exit");
                int choice = sc.nextInt();

                switch (choice) {

                    case 1:
                        System.out.print("Amount: ");
                        double deposit = sc.nextDouble();
                        balance += deposit;

                        PreparedStatement dp = con.prepareStatement(
                                "UPDATE accounts SET balance=? WHERE userid=?");
                        dp.setDouble(1, balance);
                        dp.setString(2, userid);
                        dp.executeUpdate();

                        PreparedStatement trans1 = con.prepareStatement(
                                "INSERT INTO transactions(userid,type,amount) VALUES (?,?,?)");
                        trans1.setString(1, userid);
                        trans1.setString(2, "Deposit");
                        trans1.setDouble(3, deposit);
                        trans1.executeUpdate();

                        System.out.println("Deposit Successful!");
                        break;

                    case 2:
                        System.out.print("Amount: ");
                        double withdraw = sc.nextDouble();

                        if (withdraw <= balance) {
                            balance -= withdraw;

                            PreparedStatement wp = con.prepareStatement(
                                    "UPDATE accounts SET balance=? WHERE userid=?");
                            wp.setDouble(1, balance);
                            wp.setString(2, userid);
                            wp.executeUpdate();

                            PreparedStatement trans2 = con.prepareStatement(
                                    "INSERT INTO transactions(userid,type,amount) VALUES (?,?,?)");
                            trans2.setString(1, userid);
                            trans2.setString(2, "Withdraw");
                            trans2.setDouble(3, withdraw);
                            trans2.executeUpdate();

                            System.out.println("Withdrawal Successful!");
                        } else {
                            System.out.println("Insufficient Balance!");
                        }
                        break;

                    case 3:
                        System.out.println("Current Balance: ₹" + balance);
                        break;

                    case 4:
                        Statement st = con.createStatement();
                        ResultSet history = st.executeQuery(
                                "SELECT * FROM transactions WHERE userid='" + userid + "'");

                        System.out.println("\n=== Transaction History ===");
                        while (history.next()) {
                            System.out.println(history.getString("type")
                                    + " ₹" + history.getDouble("amount")
                                    + " on " + history.getTimestamp("date"));
                        }
                        break;

                    case 5:
                        System.exit(0);
                }
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
