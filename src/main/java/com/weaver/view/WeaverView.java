package com.weaver.view;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * 游戏的视图类，负责UI的展示
 */
public class WeaverView implements GameView {
    private VBox root;
    private VBox wordHistory;
    private TextField inputField;
    private Button submitButton;
    private Button resetButton;
    private Button newGameButton;
    private Label messageLabel;
    private GridPane keyboard;
    private CheckBox showErrorMessageCheckBox;
    private CheckBox showPathCheckBox;
    private CheckBox useRandomWordsCheckBox;
    private ListView<String> historyList;
    private Label timerLabel;
    private Label scoreLabel;

    public WeaverView() {
        initialize();
    }

    /**
     * 创建视图组件
     */
    private void initialize() {
        root = new VBox(20);
        root.getStyleClass().add("game-container");
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(20));

        // 游戏标题
        Label titleLabel = new Label("Weaver Game");
        titleLabel.getStyleClass().add("game-title");

        // 计时器和得分
        HBox statsBox = new HBox(20);
        statsBox.setAlignment(Pos.CENTER);
        timerLabel = new Label("Time: 00:00");
        timerLabel.getStyleClass().add("timer");
        scoreLabel = new Label("Score: 0");
        scoreLabel.getStyleClass().add("score");
        statsBox.getChildren().addAll(timerLabel, scoreLabel);

        // 单词显示区域
        VBox wordBox = new VBox(10);
        wordBox.setAlignment(Pos.CENTER);
        wordBox.getStyleClass().add("word-display");
        Text startWord = new Text("START");
        Text targetWord = new Text("TARGET");
        wordBox.getChildren().addAll(startWord, new Text("↓"), targetWord);

        // 输入区域
        inputField = new TextField();
        inputField.getStyleClass().add("input-field");
        inputField.setPromptText("输入你的单词...");
        inputField.setMaxWidth(300);

        // 历史记录列表
        historyList = new ListView<>();
        historyList.getStyleClass().add("history-list");
        historyList.setMaxHeight(200);
        historyList.setMaxWidth(300);

        // 消息标签
        messageLabel = new Label();
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(300);

        // 虚拟键盘
        createKeyboard();

        // 控制按钮
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        submitButton = new Button("提交");
        submitButton.getStyleClass().addAll("button", "submit-button");
        resetButton = new Button("重置游戏");
        resetButton.getStyleClass().addAll("button", "reset-button");
        newGameButton = new Button("新游戏");
        newGameButton.getStyleClass().addAll("button", "new-game-button");
        buttonBox.getChildren().addAll(submitButton, resetButton, newGameButton);

        // 创建标志控制区域
        VBox flagsArea = new VBox(5);
        flagsArea.setAlignment(Pos.CENTER);
        flagsArea.setPadding(new Insets(10));
        flagsArea.setStyle("-fx-border-color: #cccccc; -fx-border-radius: 5;");

        showErrorMessageCheckBox = new CheckBox("显示错误信息");
        showPathCheckBox = new CheckBox("显示路径");
        useRandomWordsCheckBox = new CheckBox("使用随机单词");

        Label flagsLabel = new Label("游戏设置");
        flagsLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        
        flagsArea.getChildren().addAll(
            flagsLabel,
            showErrorMessageCheckBox,
            showPathCheckBox,
            useRandomWordsCheckBox
        );

        // 将所有组件添加到根容器
        root.getChildren().addAll(
            titleLabel,
            statsBox,
            wordBox,
            inputField,
            historyList,
            messageLabel,
            keyboard,
            buttonBox,
            flagsArea
        );
    }

    /**
     * 创建虚拟键盘
     */
    private void createKeyboard() {
        keyboard = new GridPane();
        keyboard.setAlignment(Pos.CENTER);
        keyboard.setHgap(5);
        keyboard.setVgap(5);
        keyboard.setPadding(new Insets(10));

        String[][] keyboardLayout = {
            {"Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P"},
            {"A", "S", "D", "F", "G", "H", "J", "K", "L"},
            {"ENTER", "Z", "X", "C", "V", "B", "N", "M", "⌫"}
        };

        for (int row = 0; row < keyboardLayout.length; row++) {
            for (int col = 0; col < keyboardLayout[row].length; col++) {
                String key = keyboardLayout[row][col];
                Button keyButton = new Button(key);
                keyButton.setStyle(
                    "-fx-background-color: #404040;" + // 深灰色背景
                    "-fx-text-fill: white;" +          // 白色文字
                    "-fx-font-weight: bold;" +         // 粗体
                    "-fx-min-width: 40px;" +          // 最小宽度
                    "-fx-min-height: 40px;" +         // 最小高度
                    "-fx-background-radius: 5px;"      // 圆角
                );
                
                // 特殊按键样式
                if (key.equals("ENTER") || key.equals("⌫")) {
                    keyButton.setStyle(keyButton.getStyle() + 
                        "-fx-min-width: 60px;"         // 更宽的特殊按键
                    );
                }

                // 添加按键事件处理
                keyButton.setOnAction(e -> {
                    switch (key) {
                        case "ENTER":
                            if (submitButton != null) {
                                submitButton.fire();
                            }
                            break;
                        case "⌫":
                            String text = inputField.getText();
                            if (!text.isEmpty()) {
                                inputField.setText(text.substring(0, text.length() - 1));
                            }
                            break;
                        default:
                            if (inputField.getText().length() < 4) {
                                inputField.setText(inputField.getText() + key);
                            }
                            break;
                    }
                });

                // 鼠标悬停效果
                keyButton.setOnMouseEntered(e -> 
                    keyButton.setStyle(keyButton.getStyle() + "-fx-background-color: #505050;")
                );
                keyButton.setOnMouseExited(e -> 
                    keyButton.setStyle(keyButton.getStyle() + "-fx-background-color: #404040;")
                );

                // 点击效果
                keyButton.setOnMousePressed(e -> 
                    keyButton.setStyle(keyButton.getStyle() + "-fx-background-color: #606060;")
                );
                keyButton.setOnMouseReleased(e -> 
                    keyButton.setStyle(keyButton.getStyle() + "-fx-background-color: #404040;")
                );

                keyboard.add(keyButton, col, row);
            }
        }
    }

    /**
     * 添加单词到历史记录
     */
    public void addWordToHistory(String word, boolean[] correctPositions) {
        HBox wordBox = new HBox(5);
        wordBox.setAlignment(Pos.CENTER);

        for (int i = 0; i < word.length(); i++) {
            Label letter = new Label(String.valueOf(word.charAt(i)));
            letter.setFont(Font.font("System", FontWeight.BOLD, 20));
            letter.setPrefWidth(40);
            letter.setPrefHeight(40);
            letter.setAlignment(Pos.CENTER);
            letter.setStyle(
                "-fx-background-color: " + (correctPositions[i] ? "#90EE90" : "#D3D3D3") + ";" +
                "-fx-border-color: black;" +
                "-fx-border-width: 1px;"
            );
            wordBox.getChildren().add(letter);
        }

        historyList.getItems().add(word);
    }

    @Override
    public void updateDisplay() {
        // 实现显示更新逻辑
    }

    @Override
    public void showError(String message) {
        messageLabel.getStyleClass().setAll("error-message");
        messageLabel.setText(message);
        shakeNode(messageLabel);
    }

    @Override
    public void showSuccess() {
        messageLabel.getStyleClass().setAll("success-message");
        messageLabel.setText("恭喜你赢得了游戏！");
        playVictoryAnimation();
    }

    private void shakeNode(javafx.scene.Node node) {
        ParallelTransition shake = new ParallelTransition(
            node,
            createFadeTransition(node, 1.0, 0.5, 100),
            createScaleTransition(node, 1.0, 1.1, 100)
        );
        shake.setCycleCount(2);
        shake.setAutoReverse(true);
        shake.play();
    }

    private void playVictoryAnimation() {
        ParallelTransition victory = new ParallelTransition(
            root,
            createFadeTransition(root, 1.0, 0.8, 500),
            createScaleTransition(root, 1.0, 1.05, 500)
        );
        victory.setCycleCount(2);
        victory.setAutoReverse(true);
        victory.play();
    }

    private FadeTransition createFadeTransition(javafx.scene.Node node, double from, double to, double duration) {
        FadeTransition fade = new FadeTransition(Duration.millis(duration), node);
        fade.setFromValue(from);
        fade.setToValue(to);
        return fade;
    }

    private ScaleTransition createScaleTransition(javafx.scene.Node node, double from, double to, double duration) {
        ScaleTransition scale = new ScaleTransition(Duration.millis(duration), node);
        scale.setFromX(from);
        scale.setFromY(from);
        scale.setToX(to);
        scale.setToY(to);
        return scale;
    }

    // Getters
    public VBox getRoot() {
        return root;
    }

    public TextField getInputField() {
        return inputField;
    }

    public Button getSubmitButton() {
        return submitButton;
    }

    public Button getResetButton() {
        return resetButton;
    }

    public Button getNewGameButton() {
        return newGameButton;
    }

    public Label getMessageLabel() {
        return messageLabel;
    }

    public GridPane getKeyboard() {
        return keyboard;
    }

    public void clearHistory() {
        historyList.getItems().clear();
    }

    public void showMessage(String message) {
        messageLabel.setText(message);
    }

    public CheckBox getShowErrorMessageCheckBox() {
        return showErrorMessageCheckBox;
    }

    public CheckBox getShowPathCheckBox() {
        return showPathCheckBox;
    }

    public CheckBox getUseRandomWordsCheckBox() {
        return useRandomWordsCheckBox;
    }

    public void updateTimer(String time) {
        timerLabel.setText("Time: " + time);
    }

    public void updateScore(int score) {
        scoreLabel.setText("Score: " + score);
    }
} 