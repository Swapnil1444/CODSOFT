import java.sql.*;
import java.util.*;


class DBConnection {
    public static Connection getConnection() throws Exception {
        String url = "jdbc:mysql://localhost:3306/examdb?useSSL=false&serverTimezone=UTC";
        String user = "root";
        String password = "9309";  // Change if needed

        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(url, user, password);
    }
}


class ExamTimer implements Runnable {

    private int timeLeft;
    private volatile boolean running = true;

    public ExamTimer(int seconds) {
        this.timeLeft = seconds;
    }

    public void stopTimer() {
        running = false;
    }

    @Override
    public void run() {
        while (timeLeft > 0 && running) {
            System.out.print("\r⏳ Time Left: " + timeLeft + " seconds ");
            timeLeft--;

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("Timer Error");
            }
        }

        if (timeLeft == 0) {
            System.out.println("\n⏰ Time Up! Auto Submitting...");
        }
    }
}


public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        try {
            Connection con = DBConnection.getConnection();

            System.out.println("===== ONLINE EXAM SYSTEM =====");

            System.out.print("Username: ");
            String username = sc.nextLine();

            System.out.print("Password: ");
            String password = sc.nextLine();

            PreparedStatement loginPs = con.prepareStatement(
                    "SELECT * FROM users WHERE username=? AND password=?");
            loginPs.setString(1, username);
            loginPs.setString(2, password);

            ResultSet loginRs = loginPs.executeQuery();

            if (!loginRs.next()) {
                System.out.println("Invalid Login!");
                return;
            }

            System.out.println("Login Successful!\n");

            // FETCH QUESTIONS 
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM questions");

            int score = 0;
            int total = 0;

            // ===== START TIMER (60 Seconds) =====
            ExamTimer timer = new ExamTimer(60);
            Thread timerThread = new Thread(timer);
            timerThread.start();

            while (rs.next()) {

                total++;

                System.out.println("\n\n" + rs.getString("question"));
                System.out.println("1. " + rs.getString("option1"));
                System.out.println("2. " + rs.getString("option2"));
                System.out.println("3. " + rs.getString("option3"));
                System.out.println("4. " + rs.getString("option4"));
                System.out.print("Enter answer (1-4): ");

                int answer = sc.nextInt();

                if (answer == rs.getInt("correct_option")) {
                    score++;
                }
            }

            // Stop Timer when exam completes
            timer.stopTimer();

            System.out.println("\n\n===== EXAM FINISHED =====");
            System.out.println("Your Score: " + score + "/" + total);

            // ===== SAVE RESULT =====
            PreparedStatement resultPs = con.prepareStatement(
                    "INSERT INTO results(username, score) VALUES (?, ?)");
            resultPs.setString(1, username);
            resultPs.setInt(2, score);
            resultPs.executeUpdate();

            System.out.println("Result Saved Successfully!");

            con.close();

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
