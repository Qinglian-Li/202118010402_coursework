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
    @DisplayName("游戏初始化测试")
    class InitializationTests {
        @Test
        @DisplayName("测试正常游戏初始化")
        void testNormalInitialization() {
            game.initializeGame("able", "ably");
            assertEquals("able", game.getStartWord());
            assertEquals("ably", game.getTargetWord());
            assertEquals(1, game.getGameHistory().size());
            assertEquals("able", game.getGameHistory().get(0));
        }

        @Test
        @DisplayName("测试初始化时的断言 - 不同长度的单词")
        void testInitializationAssertionDifferentLength() {
            assertThrows(AssertionError.class, () -> 
                game.initializeGame("able", "ables")
            );
        }

        @Test
        @DisplayName("测试初始化时的断言 - 空单词")
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
    @DisplayName("单词尝试测试")
    class WordTryTests {
        @BeforeEach
        void initializeGameForTests() {
            game.initializeGame("able", "ably");
        }

        @Test
        @DisplayName("测试有效的单词尝试")
        void testValidWordTry() {
            assertTrue(game.tryWord("ably"));
            assertEquals(2, game.getGameHistory().size());
            assertEquals("ably", game.getGameHistory().get(1));
        }

        @Test
        @DisplayName("测试无效的单词尝试 - 多个字母改变")
        void testInvalidWordTryMultipleChanges() {
            assertFalse(game.tryWord("aces"));
        }

        @Test
        @DisplayName("测试无效的单词尝试 - 长度不匹配")
        void testInvalidWordTryWrongLength() {
            assertFalse(game.tryWord("ables"));
        }

        @Test
        @DisplayName("测试无效的单词尝试 - 不在字典中的单词")
        void testInvalidWordTryNotInDictionary() {
            assertFalse(game.tryWord("abxx"));
        }

        @Test
        @DisplayName("测试无效的单词尝试 - 空单词")
        void testInvalidWordTryNull() {
            assertThrows(AssertionError.class, () -> 
                game.tryWord(null)
            );
        }

        @Test
        @DisplayName("测试重复单词尝试")
        void testDuplicateWordTry() {
            assertTrue(game.tryWord("ably"));
            assertFalse(game.tryWord("ably"));
        }
    }

    @Nested
    @DisplayName("游戏状态测试")
    class GameStateTests {
        @Test
        @DisplayName("测试游戏胜利条件")
        void testWinCondition() {
            game.initializeGame("able", "ably");
            assertFalse(game.hasWon());
            game.tryWord("ably");
            assertTrue(game.hasWon());
        }

        @Test
        @DisplayName("测试游戏历史记录")
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
    @DisplayName("随机单词生成测试")
    class RandomWordTests {
        @Test
        @DisplayName("测试4字母单词生成")
        void testRandomWordGeneration() {
            String randomWord = game.getRandomWord(4);
            assertNotNull(randomWord);
            assertEquals(4, randomWord.length());
            assertTrue(randomWord.matches("[a-z]+"));
            
            // 初始化一个新游戏，使用生成的单词作为起始词
            // 如果初始化成功，说明这个单词在字典中
            assertDoesNotThrow(() -> game.initializeGame(randomWord, "able"));
        }

        @Test
        @DisplayName("测试随机单词生成的断言 - 无效长度")
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
    @DisplayName("游戏设置测试")
    class GameSettingsTests {
        @Test
        @DisplayName("测试游戏设置标志")
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