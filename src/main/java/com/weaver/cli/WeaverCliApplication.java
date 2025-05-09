package com.weaver.cli;

import com.weaver.model.WeaverGame;
import java.util.Scanner;

/**
 * 命令行版本的主应用程序类
 */
public class WeaverCliApplication {
    private final WeaverGame game;
    private final Scanner scanner;

    public WeaverCliApplication() {
        this.game = new WeaverGame("dictionary.txt");
        this.scanner = new Scanner(System.in);
    }

    /**
     * 运行游戏
     */
    public void run() {
        System.out.println("欢迎来到 Weaver 游戏！");
        
        // 初始化游戏
        if (game.isUseRandomWords()) {
            String startWord = game.getRandomWord(4);
            String targetWord = game.getRandomWord(4);
            game.initializeGame(startWord, targetWord);
        } else {
            game.initializeGame("SOUL", "MATE");
        }

        // 显示初始状态
        displayGameState();

        // 主游戏循环
        while (true) {
            System.out.print("请输入一个4字母的单词（或输入'quit'退出）：");
            String input = scanner.nextLine().trim().toUpperCase();

            if (input.equals("QUIT")) {
                break;
            }

            if (input.length() != 4) {
                System.out.println("请输入4个字母的单词！");
                continue;
            }

            if (game.tryWord(input)) {
                displayGameState();
                if (game.hasWon()) {
                    System.out.println("恭喜你赢了！");
                    break;
                }
            } else if (game.isShowErrorMessage()) {
                System.out.println("无效的单词！");
            }
        }

        scanner.close();
    }

    /**
     * 显示游戏状态
     */
    private void displayGameState() {
        System.out.println("\n当前游戏状态：");
        System.out.println("起始单词：" + game.getStartWord());
        System.out.println("目标单词：" + game.getTargetWord());
        System.out.println("历史记录：");

        var history = game.getGameHistory();
        String targetWord = game.getTargetWord();

        for (String word : history) {
            StringBuilder display = new StringBuilder();
            for (int i = 0; i < word.length(); i++) {
                if (word.charAt(i) == targetWord.charAt(i)) {
                    display.append("\u001B[32m").append(word.charAt(i)).append("\u001B[0m"); // 绿色
                } else {
                    display.append("\u001B[37m").append(word.charAt(i)).append("\u001B[0m"); // 灰色
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