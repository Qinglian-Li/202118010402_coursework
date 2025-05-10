package com.weaver.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * WeaverGame class is the core model class of the game
 * and implements the main logic of the word change game
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
     * Constructors
     * @param dictionaryPath Path to the dictionary file
     */
    public WeaverGame(String dictionaryPath) {
        assert dictionaryPath != null && !dictionaryPath.isEmpty() : "Dictionary file path cannot be empty";
        
        this.dictionary = loadDictionary(dictionaryPath);
        assert !dictionary.isEmpty() : "Dictionary cannot be empty";
        
        this.gameHistory = new ArrayList<>();
        this.showErrorMessage = true;
        this.showPath = false;
        this.useRandomWords = false;
    }

    /**
     * Loading dictionary file
     * @param path Dictionary file path
     * @return Set of all valid words
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
     * Initializing Game
     * @param start Start Word
     * @param target Target Word
     */
    public void initializeGame(String start, String target) {
        assert start != null && target != null : "The start and target words cannot be null";
        assert start.length() == target.length() : "The start and target words must be the same length";
        assert dictionary.contains(start.toLowerCase()) : "The starting word must be in the dictionary";
        assert dictionary.contains(target.toLowerCase()) : "The target word must be in the dictionary";

        this.startWord = start.toLowerCase();
        this.targetWord = target.toLowerCase();
        this.gameHistory.clear();
        this.gameHistory.add(this.startWord);
    }

    /**
     * Try a new word
     * @param word Word entered by the player
     * @return If a valid move
     */
    public boolean tryWord(String word) {
        assert word != null : "The input word cannot be empty";
        assert !gameHistory.isEmpty() : "The game history cannot be empty";
        
        word = word.toLowerCase();
        
        // Check that the word length is correct
        if (word.length() != startWord.length()) {
            return false;
        }

        // Check if only one letter has changed
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

        // Checks if it's a valid word
        if (!dictionary.contains(word)) {
            return false;
        }

        // Add to history
        gameHistory.add(word);
        return true;
    }

    /**
     * Check if the game is won
     * @return If win
     */
    public boolean hasWon() {
        assert !gameHistory.isEmpty() : "The game history cannot be empty";
        return gameHistory.get(gameHistory.size() - 1).equals(targetWord);
    }

    /**
     * Choose a random word from the dictionary
     * @param length Word length
     * @return Random word
     */
    public String getRandomWord(int length) {
        assert length > 0 : "Word length must be greater than 0";
        assert !dictionary.isEmpty() : "Dictionary cannot be empty";

        List<String> wordsOfLength = dictionary.stream()
                .filter(word -> word.length() == length)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        
        assert !wordsOfLength.isEmpty() : "No word of the specified length was found";
        
        Random random = new Random();
        return wordsOfLength.get(random.nextInt(wordsOfLength.size()));
    }

    // Getters and setters with assertions
    public String getStartWord() {
        assert startWord != null : "Start word is uninitialized";
        return startWord;
    }

    public String getTargetWord() {
        assert targetWord != null : "Target word is uninitialized";
        return targetWord;
    }

    public List<String> getGameHistory() {
        assert gameHistory != null : "Game history is uninitialized";
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