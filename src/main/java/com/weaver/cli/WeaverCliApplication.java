package com.weaver.cli;

import com.weaver.model.WeaverGame;
import java.util.Scanner;

/**
 * The command-line version of the main application class
 */
public class WeaverCliApplication {
    private final WeaverGame game;
    private final Scanner scanner;

    public WeaverCliApplication() {
        this.game = new WeaverGame("dictionary.txt");
        this.scanner = new Scanner(System.in);
    }

    /**
     * Running the game
     */
    public void run() {
        System.out.println("Welcome to Weaver Game！");
        
        // 初始化游戏
        if (game.isUseRandomWords()) {
            String startWord = game.getRandomWord(4);
            String targetWord = game.getRandomWord(4);
            game.initializeGame(startWord, targetWord);
        } else {
            game.initializeGame("SOUL", "MATE");
        }

        // Display the initial state
        displayGameState();

        // Main game loop
        while (true) {
            System.out.print("Please enter a 4 - letter word (or type 'QUIT' to quit): ");
            String input = scanner.nextLine().trim().toUpperCase();

            if (input.equals("QUIT")) {
                break;
            }

            if (input.length() != 4) {
                System.out.println("Please enter a 4 - letter word");
                continue;
            }

            if (game.tryWord(input)) {
                displayGameState();
                if (game.hasWon()) {
                    System.out.println("Congratulations on winning!");
                    break;
                }
            } else if (game.isShowErrorMessage()) {
                System.out.println("Invalid word!");
            }
        }

        scanner.close();
    }

    /**
     * Displaying game status
     */
    private void displayGameState() {
        System.out.println("\nCurrent Game State：");
        System.out.println("Start Word: " + game.getStartWord());
        System.out.println("Target Word: " + game.getTargetWord());
        System.out.println("History Record: ");

        var history = game.getGameHistory();
        String targetWord = game.getTargetWord();

        for (String word : history) {
            StringBuilder display = new StringBuilder();
            for (int i = 0; i < word.length(); i++) {
                if (word.charAt(i) == targetWord.charAt(i)) {
                    display.append("\u001B[32m").append(word.charAt(i)).append("\u001B[0m"); // Green
                } else {
                    display.append("\u001B[37m").append(word.charAt(i)).append("\u001B[0m"); // Grey
                }
            }
            System.out.println(display);
        }
        System.out.println();
    }

    public static void main(String[] args) {
        new WeaverCliApplication().run();
    }
} 