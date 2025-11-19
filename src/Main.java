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

    public static int[] playRound(String word) {

        String hidden = "*".repeat(word.length());
        char[] hiddenArray = hidden.toCharArray();
        word = word.toLowerCase();

        int userPoints = 0;
        int compPoints = 0;

        int userAttempts = 6;
        int compAttempts = 6;

        boolean[] usedLetters = new boolean[26];

        while (userAttempts > 0 || compAttempts > 0) {

            System.out.println("\nWord: " + String.valueOf(hiddenArray));
            System.out.println("User attempts: " + userAttempts + " | Computer attempts: " + compAttempts);

            if (userAttempts > 0) {

                System.out.print("Enter a letter or whole word (0 to quit): ");
                String input = sc.next();

                if (input.equals("0")) {
                    System.out.println("The word was: " + word);
                    return new int[]{userPoints, compPoints};
                }

                if (input.length() > 1) {
                    if (input.equalsIgnoreCase(word)) {
                        int base = word.length();
                        int bonus = (int) Math.ceil(base * 0.05);
                        userPoints += base + bonus;

                        System.out.println("User guessed the word! +" + (base + bonus));
                        return new int[]{userPoints, compPoints};
                    } else {
                        userAttempts--;
                        System.out.println("Wrong word! User attempts left: " + userAttempts);
                    }
                } else {
                    char guess = Character.toLowerCase(input.charAt(0));
                    usedLetters[guess - 'a'] = true;

                    boolean found = false;
                    for (int i = 0; i < word.length(); i++) {
                        if (word.charAt(i) == guess && hiddenArray[i] == '*') {
                            hiddenArray[i] = guess;
                            found = true;
                            userPoints++;
                        }
                    }

                    if (!found) {
                        userAttempts--;
                        System.out.println("Wrong letter! User attempts left: " + userAttempts);
                    } else {
                        System.out.println("Correct letter!");
                    }
                }

                if (String.valueOf(hiddenArray).equals(word)) {
                    System.out.println("User completed the word!");
                    return new int[]{userPoints, compPoints};
                }
            }

            if (compAttempts > 0) {
                System.out.println("\n--- Computer turn ---");

                char compLetter;
                do {
                    compLetter = (char) ('a' + rnd.nextInt(26));
                } while (usedLetters[compLetter - 'a']);

                usedLetters[compLetter - 'a'] = true;

                System.out.println("Computer guesses: " + compLetter);

                boolean found = false;
                for (int i = 0; i < word.length(); i++) {
                    if (word.charAt(i) == compLetter && hiddenArray[i] == '*') {
                        hiddenArray[i] = compLetter;
                        found = true;
                        compPoints++;
                    }
                }

                if (!found) {
                    compAttempts--;
                    System.out.println("Computer wrong! Attempts left: " + compAttempts);
                } else {
                    System.out.println("Computer found a letter!");
                }

                if (String.valueOf(hiddenArray).equals(word)) {
                    System.out.println("Computer completed the word!");
                    return new int[]{userPoints, compPoints};
                }
            }

            if (userAttempts == 0 && compAttempts == 0) {
                System.out.println("Both players are out of attempts!");
                System.out.println("The word was: " + word);
                return new int[]{userPoints, compPoints};
            }
        }

        return new int[]{userPoints, compPoints};
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

        int rounds = 3;

        int[] userScores = new int[rounds];
        int[] compScores = new int[rounds];

        int totalUser = 0;
        int totalComp = 0;

        System.out.println("=== Start game: User vs Computer ===");

        for (int i = 0; i < rounds; i++) {

            System.out.println("\n---- Round " + (i + 1) + " ----");

            int category = rnd.nextInt(4);
            String word = selectWord(category, words, used);

            System.out.println("Category: " + categories[category]);

            int[] scores = playRound(word);

            userScores[i] = scores[0];
            compScores[i] = scores[1];

            totalUser += scores[0];
            totalComp += scores[1];
        }

        System.out.println("\n-------------- Game Results --------------");
        System.out.println("Round | User | Comp");
        System.out.println("------+-------+-------");

        for (int i = 0; i < rounds; i++) {
            System.out.printf("  %d   |  %-5d|  %-5d%n", i + 1, userScores[i], compScores[i]);
        }

        System.out.println("------+-------+-------");
        System.out.printf("Total |  %-5d|  %-5d%n", totalUser, totalComp);
        System.out.println("===========================================");
    }

    public static void tourLaunch() {
        boolean playAgain;

        do {
            playOneGame();

            System.out.print("\nPlay again? (y/n): ");
            String answer = sc.next().toLowerCase();
            playAgain = answer.equals("y") || answer.equals("yes");

        } while (playAgain);

        System.out.println("Thanks for playing!");
    }
}