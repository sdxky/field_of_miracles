import java.util.Random;
import java.util.Scanner;

public class Main {

    private static final Random rnd = new Random();
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        tourLaunch();
    }

    public static String selectWord(int category, String[][] words, boolean[][] used) {
        int index;
        do {
            index = rnd.nextInt(words[category].length);
        } while (used[category][index]);
        used[category][index] = true;
        return words[category][index];
    }

    public static int chooseLetter(String word, int[] totalAttempts) {
        String hidden = "*".repeat(word.length());
        char[] hiddenArray = hidden.toCharArray();
        word = word.toLowerCase();
        int points = 0;

        while (totalAttempts[0] > 0) {
            System.out.println("Word: " + String.valueOf(hiddenArray));
            System.out.print("Enter a letter, the whole word (or 0 to quit): ");
            String input = sc.next();

            if (input.equals("0")) {
                System.out.println("The word was: " + word);
                return points;
            }

            if (input.length() > 1) {
                if (input.equalsIgnoreCase(word)) {
                    int basePoints = word.length();
                    int bonusPoints = (int) Math.ceil(basePoints * 0.05);
                    points = basePoints + bonusPoints;

                    System.out.println("You guessed the whole word: " + word);
                    System.out.println("Points earned: " + basePoints + " (letters) + " + bonusPoints + " (5% bonus) = " + points);
                    return points;
                } else {
                    totalAttempts[0]--;
                    System.out.println("Wrong word! Attempts left: " + totalAttempts[0]);
                    if (totalAttempts[0] == 0) {
                        System.out.println("No attempts left for this round. The word was: " + word);
                        return 0;
                    }
                    continue;
                }
            }

            if (input.length() != 1) {
                System.out.println("Please enter only one letter or the whole word!");
                continue;
            }

            char guess = Character.toLowerCase(input.charAt(0));
            boolean found = false;

            for (int i = 0; i < word.length(); i++) {
                if (word.charAt(i) == guess && hiddenArray[i] == '*') {
                    hiddenArray[i] = word.charAt(i);
                    found = true;
                    points++;
                }
            }

            if (found) {
                System.out.println("You guessed a letter!");
            } else {
                totalAttempts[0]--;
                System.out.println("Wrong letter! Attempts left: " + totalAttempts[0]);
            }

            if (String.valueOf(hiddenArray).equals(word)) {
                System.out.println("🎉 You guessed the word: " + word);
                return points;
            }

            if (totalAttempts[0] == 0) {
                System.out.println("No attempts left for this round. The word was: " + word);
                return points;
            }
        }

        return points;
    }

    public static void playOneGame() {
        String[][] words = {
                {"cat", "dog", "fox"},
                {"bmw", "audi", "toyota"},
                {"plov", "cake", "manty", "friedchicken"},
                {"bishkek", "moscow", "newyork", "osh"}
        };

        String[] categories = {"Animals", "Cars", "Food", "City"};

        boolean[][] used = new boolean[words.length][];
        for (int i = 0; i < words.length; i++) {
            used[i] = new boolean[words[i].length];
        }

        int[] totalAttempts = {9};
        int rounds = 3;
        int[] scores = new int[rounds];
        int[] attemptsAfter = new int[rounds];
        int totalScore = 0;

        System.out.println("=== Start game ===");
        System.out.println("You have " +totalAttempts[0] + " attempts for the entire game!");

        for (int i = 0; i < rounds; i++) {
            if (totalAttempts[0] == 0) {
                System.out.println("No attempts left! Game over!");
                break;
            }

            System.out.println("\n---- Round " + (i + 1) + " ----");
            System.out.println("Remaining attempts: " + totalAttempts[0]);

            int category = rnd.nextInt(4);
            String word = selectWord(category, words, used);
            System.out.println("Category: " + categories[category]);

            int scoreBefore = totalScore;
            int score = chooseLetter(word, totalAttempts);
            totalScore += score;

            scores[i] = totalScore - scoreBefore;
            attemptsAfter[i] = totalAttempts[0];
        }

        System.out.println("\n-------------- Game Results --------------");
        System.out.println("Round |  Score  | Attempts");
        System.out.println("------+----------+----------");

        for (int i = 0; i < rounds; i++) {
            System.out.printf("  %d   |   %-6d|   %-6d%n", i + 1, scores[i], attemptsAfter[i]);
        }

        System.out.println("------+----------+----------");
        System.out.printf("Total |   %-6d|   %-6d%n", totalScore, totalAttempts[0]);
        System.out.println("===========================================");
    }

    public static void tourLaunch() {
        boolean playAgain;

        do {
            playOneGame();

            System.out.print("\nDo you want to play one more time? (y/n): ");
            String answer = sc.next().toLowerCase();
            playAgain = answer.equals("y") || answer.equals("yes");

        } while (playAgain);

        System.out.println("Thanks for playing!");
    }
}