package com.weaver.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import static org.junit.jupiter.api.Assertions.*;

public class WeaverGameTest {
    private WeaverGame game;

    @BeforeEach
    void setUp() {
        game = new WeaverGame("dictionary.txt");
    }

    @Nested
    @DisplayName("Game Initialization Test")
    class InitializationTests {
        @Test
        @DisplayName("Normal Game Initialization Test")
        void testNormalInitialization() {
            game.initializeGame("able", "ably");
            assertEquals("able", game.getStartWord());
            assertEquals("ably", game.getTargetWord());
            assertEquals(1, game.getGameHistory().size());
            assertEquals("able", game.getGameHistory().get(0));
        }

        @Test
        @DisplayName("Test assertions at initialization -words of different lengths")
        void testInitializationAssertionDifferentLength() {
            assertThrows(AssertionError.class, () -> 
                game.initializeGame("able", "ables")
            );
        }

        @Test
        @DisplayName("Test assertions at initialization -empty word")
        void testInitializationAssertionNullWords() {
            assertThrows(AssertionError.class, () -> 
                game.initializeGame(null, "ably")
            );
            assertThrows(AssertionError.class, () -> 
                game.initializeGame("able", null)
            );
        }
    }

    @Nested
    @DisplayName("Word Trial Test")
    class WordTryTests {
        @BeforeEach
        void initializeGameForTests() {
            game.initializeGame("able", "ably");
        }

        @Test
        @DisplayName("Test for effective word attempts")
        void testValidWordTry() {
            assertTrue(game.tryWord("ably"));
            assertEquals(2, game.getGameHistory().size());
            assertEquals("ably", game.getGameHistory().get(1));
        }

        @Test
        @DisplayName("Test invalid word attempts - multiple letter changes")
        void testInvalidWordTryMultipleChanges() {
            assertFalse(game.tryWord("aces"));
        }

        @Test
        @DisplayName("Test for invalid word trial-length mismatches")
        void testInvalidWordTryWrongLength() {
            assertFalse(game.tryWord("ables"));
        }

        @Test
        @DisplayName("Test for invalid word attempts -words that are not in the dictionary")
        void testInvalidWordTryNotInDictionary() {
            assertFalse(game.tryWord("abxx"));
        }

        @Test
        @DisplayName("Test for invalid word tries-empty words")
        void testInvalidWordTryNull() {
            assertThrows(AssertionError.class, () -> 
                game.tryWord(null)
            );
        }

        @Test
        @DisplayName("Test Duplicate Word Attempts")
        void testDuplicateWordTry() {
            assertTrue(game.tryWord("ably"));
            assertFalse(game.tryWord("ably"));
        }
    }

    @Nested
    @DisplayName("Game State Test")
    class GameStateTests {
        @Test
        @DisplayName("Test the game win condition")
        void testWinCondition() {
            game.initializeGame("able", "ably");
            assertFalse(game.hasWon());
            game.tryWord("ably");
            assertTrue(game.hasWon());
        }

        @Test
        @DisplayName("Game History Test")
        void testGameHistory() {
            game.initializeGame("able", "ably");
            game.tryWord("ably");
            var history = game.getGameHistory();
            assertEquals(2, history.size());
            assertEquals("able", history.get(0));
            assertEquals("ably", history.get(1));
        }
    }

    @Nested
    @DisplayName("Random Word Generation Test")
    class RandomWordTests {
        @Test
        @DisplayName("Test 4-letter word generation")
        void testRandomWordGeneration() {
            String randomWord = game.getRandomWord(4);
            assertNotNull(randomWord);
            assertEquals(4, randomWord.length());
            assertTrue(randomWord.matches("[a-z]+"));
            
            // Initialize a new game, using the generated word as the starting word
            // If initialization was successful, then the word is in the dictionary
            assertDoesNotThrow(() -> game.initializeGame(randomWord, "able"));
        }

        @Test
        @DisplayName("Test random word generation for assertion-invalid length")
        void testRandomWordGenerationInvalidLength() {
            assertThrows(AssertionError.class, () -> 
                game.getRandomWord(0)
            );
            assertThrows(AssertionError.class, () -> 
                game.getRandomWord(-1)
            );
        }
    }

    @Nested
    @DisplayName("Game Setup testing")
    class GameSettingsTests {
        @Test
        @DisplayName("Game Setting Flag Test")
        void testGameFlags() {
            game.setShowErrorMessage(false);
            assertFalse(game.isShowErrorMessage());

            game.setShowPath(true);
            assertTrue(game.isShowPath());

            game.setUseRandomWords(true);
            assertTrue(game.isUseRandomWords());
        }
    }
} 