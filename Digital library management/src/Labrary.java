import java.sql.*;
import java.util.Scanner;


class DBConnection {
    public static Connection getConnection() throws Exception {
        String url = "jdbc:mysql://localhost:3306/librarydb?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
        String user = "root";
        String password = "9309";  // change if needed

        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(url, user, password);
    }
}


public class Labrary {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        try {
            Connection con = DBConnection.getConnection();

            System.out.println("===== DIGITAL LIBRARY MANAGEMENT =====");

          
            System.out.print("Admin Username: ");
            String username = sc.nextLine();

            System.out.print("Admin Password: ");
            String password = sc.nextLine();

            PreparedStatement loginPs = con.prepareStatement(
                    "SELECT * FROM admins WHERE username=? AND password=?");
            loginPs.setString(1, username);
            loginPs.setString(2, password);

            ResultSet loginRs = loginPs.executeQuery();

            if (!loginRs.next()) {
                System.out.println("Invalid Login!");
                return;
            }

            System.out.println("Login Successful!");

            while (true) {

                System.out.println("\n1. Add Book");
                System.out.println("2. View Books");
                System.out.println("3. Delete Book");
                System.out.println("4. Issue Book");
                System.out.println("5. Return Book");
                System.out.println("6. Exit");
                System.out.print("Choose: ");

                int choice = sc.nextInt();
                sc.nextLine();

                switch (choice) {

                    
                    case 1:
                        System.out.print("Enter Book Title: ");
                        String title = sc.nextLine();

                        System.out.print("Enter Author: ");
                        String author = sc.nextLine();

                        PreparedStatement addPs = con.prepareStatement(
                                "INSERT INTO books(title,author) VALUES (?,?)");
                        addPs.setString(1, title);
                        addPs.setString(2, author);
                        addPs.executeUpdate();

                        System.out.println("Book Added Successfully!");
                        break;

                    
                    case 2:
                        Statement st = con.createStatement();
                        ResultSet rs = st.executeQuery("SELECT * FROM books");

                        System.out.println("\n--- Book List ---");
                        while (rs.next()) {
                            System.out.println("ID: " + rs.getInt("id")
                                    + " | " + rs.getString("title")
                                    + " | " + rs.getString("author")
                                    + " | Available: " + rs.getBoolean("available"));
                        }
                        break;

                   
                    case 3:
                        System.out.print("Enter Book ID to delete: ");
                        int deleteId = sc.nextInt();

                        PreparedStatement deletePs = con.prepareStatement(
                                "DELETE FROM books WHERE id=?");
                        deletePs.setInt(1, deleteId);
                        deletePs.executeUpdate();

                        System.out.println("Book Deleted Successfully!");
                        break;

                  
                    case 4:
                        System.out.print("Enter Book ID to issue: ");
                        int issueId = sc.nextInt();
                        sc.nextLine();

                        System.out.print("Issued To: ");
                        String issuedTo = sc.nextLine();

                        PreparedStatement issuePs = con.prepareStatement(
                                "INSERT INTO issued_books(book_id,issued_to) VALUES (?,?)");
                        issuePs.setInt(1, issueId);
                        issuePs.setString(2, issuedTo);
                        issuePs.executeUpdate();

                        PreparedStatement updateAvailability = con.prepareStatement(
                                "UPDATE books SET available=FALSE WHERE id=?");
                        updateAvailability.setInt(1, issueId);
                        updateAvailability.executeUpdate();

                        System.out.println("Book Issued Successfully!");
                        break;

                   
                    case 5:
                        System.out.print("Enter Book ID to return: ");
                        int returnId = sc.nextInt();

                        PreparedStatement returnPs = con.prepareStatement(
                                "UPDATE issued_books SET return_date=NOW() WHERE book_id=? AND return_date IS NULL");
                        returnPs.setInt(1, returnId);
                        returnPs.executeUpdate();

                        PreparedStatement updateBook = con.prepareStatement(
                                "UPDATE books SET available=TRUE WHERE id=?");
                        updateBook.setInt(1, returnId);
                        updateBook.executeUpdate();

                        System.out.println("Book Returned Successfully!");
                        break;

                    case 6:
                        System.out.println("Thank You!");
                        System.exit(0);

                    default:
                        System.out.println("Invalid Choice!");
                }
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
