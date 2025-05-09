package com.weaver.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * WeaverGame类是游戏的核心模型类，实现了单词变换游戏的主要逻辑
 */
public class WeaverGame {
    private String startWord;
    private String targetWord;
    private Set<String> dictionary;
    private List<String> gameHistory;
    private boolean showErrorMessage;
    private boolean showPath;
    private boolean useRandomWords;

    /**
     * 构造函数
     * @param dictionaryPath 字典文件的路径
     */
    public WeaverGame(String dictionaryPath) {
        assert dictionaryPath != null && !dictionaryPath.isEmpty() : "字典文件路径不能为空";
        
        this.dictionary = loadDictionary(dictionaryPath);
        assert !dictionary.isEmpty() : "字典不能为空";
        
        this.gameHistory = new ArrayList<>();
        this.showErrorMessage = true;
        this.showPath = false;
        this.useRandomWords = false;
    }

    /**
     * 加载字典文件
     * @param path 字典文件路径
     * @return 包含所有有效单词的Set
     */
    private Set<String> loadDictionary(String path) {
        Set<String> words = new HashSet<>();
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(path);
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = reader.readLine()) != null) {
                words.add(line.trim().toLowerCase());
            }
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            System.err.println("Error loading dictionary file: " + path);
        }
        return words;
    }

    /**
     * 初始化游戏
     * @param start 起始单词
     * @param target 目标单词
     */
    public void initializeGame(String start, String target) {
        assert start != null && target != null : "起始词和目标词不能为空";
        assert start.length() == target.length() : "起始词和目标词长度必须相同";
        assert dictionary.contains(start.toLowerCase()) : "起始词必须在字典中";
        assert dictionary.contains(target.toLowerCase()) : "目标词必须在字典中";

        this.startWord = start.toLowerCase();
        this.targetWord = target.toLowerCase();
        this.gameHistory.clear();
        this.gameHistory.add(this.startWord);
    }

    /**
     * 尝试一个新单词
     * @param word 玩家输入的单词
     * @return 是否是有效的移动
     */
    public boolean tryWord(String word) {
        assert word != null : "输入单词不能为空";
        assert !gameHistory.isEmpty() : "游戏历史不能为空";
        
        word = word.toLowerCase();
        
        // 检查单词长度是否正确
        if (word.length() != startWord.length()) {
            return false;
        }

        // 检查是否只改变了一个字母
        String lastWord = gameHistory.get(gameHistory.size() - 1);
        int differences = 0;
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) != lastWord.charAt(i)) {
                differences++;
            }
        }
        if (differences != 1) {
            return false;
        }

        // 检查是否是有效单词
        if (!dictionary.contains(word)) {
            return false;
        }

        // 添加到历史记录
        gameHistory.add(word);
        return true;
    }

    /**
     * 检查游戏是否获胜
     * @return 是否获胜
     */
    public boolean hasWon() {
        assert !gameHistory.isEmpty() : "游戏历史不能为空";
        return gameHistory.get(gameHistory.size() - 1).equals(targetWord);
    }

    /**
     * 从字典中随机选择一个单词
     * @param length 单词长度
     * @return 随机单词
     */
    public String getRandomWord(int length) {
        assert length > 0 : "单词长度必须大于0";
        assert !dictionary.isEmpty() : "字典不能为空";

        List<String> wordsOfLength = dictionary.stream()
                .filter(word -> word.length() == length)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        
        assert !wordsOfLength.isEmpty() : "没有找到指定长度的单词";
        
        Random random = new Random();
        return wordsOfLength.get(random.nextInt(wordsOfLength.size()));
    }

    // Getters and setters with assertions
    public String getStartWord() {
        assert startWord != null : "起始词未初始化";
        return startWord;
    }

    public String getTargetWord() {
        assert targetWord != null : "目标词未初始化";
        return targetWord;
    }

    public List<String> getGameHistory() {
        assert gameHistory != null : "游戏历史未初始化";
        return new ArrayList<>(gameHistory);
    }

    public boolean isShowErrorMessage() {
        return showErrorMessage;
    }

    public void setShowErrorMessage(boolean showErrorMessage) {
        this.showErrorMessage = showErrorMessage;
    }

    public boolean isShowPath() {
        return showPath;
    }

    public void setShowPath(boolean showPath) {
        this.showPath = showPath;
    }

    public boolean isUseRandomWords() {
        return useRandomWords;
    }

    public void setUseRandomWords(boolean useRandomWords) {
        this.useRandomWords = useRandomWords;
    }
} 