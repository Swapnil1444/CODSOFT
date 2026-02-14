package NumberGuessingGame;




import java.util.Random;
import java.util.Scanner;

class NumberGuessingGame {

    private static final int MAX_ATTEMPTS = 5;
    private static final int RANGE = 100;
    private int score = 0;

    public void startGame() {
        Scanner sc = new Scanner(System.in);
        Random random = new Random();
        String playAgain;

        do {
            int numberToGuess = random.nextInt(RANGE) + 1;
            int attempts = 0;
            boolean guessedCorrectly = false;

            System.out.println("\n🎯 New Round Started!");
            System.out.println("Guess a number between 1 and " + RANGE);
            System.out.println("You have " + MAX_ATTEMPTS + " attempts.");

            while (attempts < MAX_ATTEMPTS) {
                System.out.print("Enter your guess: ");
                int userGuess = sc.nextInt();
                attempts++;

                if (userGuess == numberToGuess) {
                    System.out.println("✅ Correct! You guessed the number.");
                    score += (MAX_ATTEMPTS - attempts + 1) * 10;
                    guessedCorrectly = true;
                    break;
                } else if (userGuess < numberToGuess) {
                    System.out.println("📉 Too low!");
                } else {
                    System.out.println("📈 Too high!");
                }

                System.out.println("Attempts left: " + (MAX_ATTEMPTS - attempts));
            }

            if (!guessedCorrectly) {
                System.out.println("❌ You lost! The correct number was: " + numberToGuess);
            }

            System.out.println("🏆 Current Score: " + score);

            System.out.print("\nDo you want to play again? (yes/no): ");
            playAgain = sc.next();

        } while (playAgain.equalsIgnoreCase("yes"));

        System.out.println("\n🎮 Game Over!");
        System.out.println("Final Score: " + score);
        sc.close();
    }
}

public class Number_Guessin_Game {
    public static void main(String[] args) {
        NumberGuessingGame game = new NumberGuessingGame();
        game.startGame();
    }
}
