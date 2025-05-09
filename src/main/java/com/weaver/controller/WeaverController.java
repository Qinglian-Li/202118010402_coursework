package com.weaver.controller;

import com.weaver.model.WeaverGame;
import com.weaver.view.WeaverView;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;

/**
 * 游戏的控制器类，负责处理用户输入和更新视图
 */
public class WeaverController {
    private final WeaverGame model;
    private final WeaverView view;
    private StringBuilder currentInput;

    public WeaverController(WeaverGame model, WeaverView view) {
        this.model = model;
        this.view = view;
        this.currentInput = new StringBuilder();
        initializeController();
    }

    /**
     * 初始化控制器，设置事件处理
     */
    private void initializeController() {
        // 设置提交按钮事件
        view.getSubmitButton().setOnAction(e -> handleSubmit());

        // 设置重置按钮事件
        view.getResetButton().setOnAction(e -> handleReset());

        // 设置新游戏按钮事件
        view.getNewGameButton().setOnAction(e -> handleNewGame());

        // 设置标志控制
        view.getShowErrorMessageCheckBox().setSelected(model.isShowErrorMessage());
        view.getShowPathCheckBox().setSelected(model.isShowPath());
        view.getUseRandomWordsCheckBox().setSelected(model.isUseRandomWords());

        view.getShowErrorMessageCheckBox().setOnAction(e -> 
            model.setShowErrorMessage(view.getShowErrorMessageCheckBox().isSelected()));
        
        view.getShowPathCheckBox().setOnAction(e -> {
            model.setShowPath(view.getShowPathCheckBox().isSelected());
            updateView(); // 更新显示以反映路径显示状态
        });
        
        view.getUseRandomWordsCheckBox().setOnAction(e -> 
            model.setUseRandomWords(view.getUseRandomWordsCheckBox().isSelected()));

        // 设置输入框事件
        view.getInputField().setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                handleSubmit();
            }
        });

        // 设置虚拟键盘事件
        for (var node : view.getKeyboard().getChildren()) {
            if (node instanceof Button) {
                Button key = (Button) node;
                key.setOnAction(e -> handleKeyPress(key.getText()));
            }
        }

        // 更新视图显示初始状态
        updateView();
    }

    /**
     * 处理虚拟键盘按键
     */
    private void handleKeyPress(String key) {
        switch (key) {
            case "ENTER":
                handleSubmit();
                break;
            case "⌫":
                if (currentInput.length() > 0) {
                    currentInput.setLength(currentInput.length() - 1);
                    view.getInputField().setText(currentInput.toString());
                }
                break;
            default:
                if (currentInput.length() < 4) {
                    currentInput.append(key);
                    view.getInputField().setText(currentInput.toString());
                }
                break;
        }
    }

    /**
     * 处理提交操作
     */
    private void handleSubmit() {
        String input = view.getInputField().getText().toUpperCase();
        if (input.length() != 4) {
            view.showMessage("请输入4个字母的单词");
            return;
        }

        if (model.tryWord(input)) {
            updateView();
            if (model.hasWon()) {
                view.showSuccess();
                view.getSubmitButton().setDisable(true);
                
                // 显示更详细的胜利对话框
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("游戏胜利");
                alert.setHeaderText("恭喜你完成了挑战！");
                alert.setContentText(
                    String.format("你成功将 %s 转换成了 %s！\n\n" +
                                "游戏统计：\n" +
                                "- 使用步数：%d\n" +
                                "- 转换路径：%s",
                                model.getStartWord().toUpperCase(),
                                model.getTargetWord().toUpperCase(),
                                model.getGameHistory().size() - 1,
                                String.join(" → ", model.getGameHistory()).toUpperCase()
                    )
                );
                alert.showAndWait();
            }
        } else if (model.isShowErrorMessage()) {
            view.showMessage("无效的单词");
        }

        view.getInputField().clear();
        currentInput.setLength(0);
    }

    /**
     * 处理重置操作
     */
    private void handleReset() {
        model.initializeGame(model.getStartWord(), model.getTargetWord());
        view.clearHistory();
        view.getSubmitButton().setDisable(false);
        view.showMessage("");
        updateView();
    }

    /**
     * 处理新游戏操作
     */
    private void handleNewGame() {
        if (model.isUseRandomWords()) {
            String startWord = model.getRandomWord(4);
            String targetWord = model.getRandomWord(4);
            model.initializeGame(startWord, targetWord);
        } else {
            model.initializeGame("SOUL", "MATE");
        }
        view.clearHistory();
        view.getSubmitButton().setDisable(false);
        view.showMessage("");
        updateView();
    }

    /**
     * 更新视图显示
     */
    private void updateView() {
        view.clearHistory();
        var history = model.getGameHistory();
        String targetWord = model.getTargetWord();

        for (String word : history) {
            boolean[] correctPositions = new boolean[word.length()];
            for (int i = 0; i < word.length(); i++) {
                correctPositions[i] = word.charAt(i) == targetWord.charAt(i);
            }
            view.addWordToHistory(word, correctPositions);
        }

        // 如果启用了路径显示，显示当前路径
        if (model.isShowPath() && !history.isEmpty()) {
            StringBuilder pathMessage = new StringBuilder("当前路径: ");
            for (String word : history) {
                pathMessage.append(word).append(" -> ");
            }
            pathMessage.setLength(pathMessage.length() - 4); // 移除最后的 " -> "
            view.showMessage(pathMessage.toString());
        }
    }
} 