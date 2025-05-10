package com.weaver.controller;

import com.weaver.model.WeaverGame;
import com.weaver.view.WeaverView;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;

/**
 * The game's controller class, which handles user input and updates the view
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
     * Initialize the controller and set up event handling
     */
    private void initializeController() {
        // Set the submit button event
        view.getSubmitButton().setOnAction(e -> handleSubmit());

        // Set the reset button event
        view.getResetButton().setOnAction(e -> handleReset());

        // Set a new game button event
        view.getNewGameButton().setOnAction(e -> handleNewGame());

        // Setting flag controls
        view.getShowErrorMessageCheckBox().setSelected(model.isShowErrorMessage());
        view.getShowPathCheckBox().setSelected(model.isShowPath());
        view.getUseRandomWordsCheckBox().setSelected(model.isUseRandomWords());

        view.getShowErrorMessageCheckBox().setOnAction(e -> 
            model.setShowErrorMessage(view.getShowErrorMessageCheckBox().isSelected()));
        
        view.getShowPathCheckBox().setOnAction(e -> {
            model.setShowPath(view.getShowPathCheckBox().isSelected());
            updateView(); // Update the display to reflect the path display state
        });
        
        view.getUseRandomWordsCheckBox().setOnAction(e -> 
            model.setUseRandomWords(view.getUseRandomWordsCheckBox().isSelected()));

        // Sets the input field event
        view.getInputField().setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                handleSubmit();
            }
        });

        // Set virtual keyboard events
        for (var node : view.getKeyboard().getChildren()) {
            if (node instanceof Button) {
                Button key = (Button) node;
                key.setOnAction(e -> handleKeyPress(key.getText()));
            }
        }

        // The update view shows the initial state
        updateView();
    }

    /**
     * Handle virtual keyboard keystrokes
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
     * Handling commit operations
     */
    private void handleSubmit() {
        String input = view.getInputField().getText().toUpperCase();
        if (input.length() != 4) {
            view.showMessage("Please enter a 4 - letter word");
            return;
        }

        if (model.tryWord(input)) {
            updateView();
            if (model.hasWon()) {
                view.showSuccess();
                view.getSubmitButton().setDisable(true);
                
                // Displays a more detailed victory dialog
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Game Winning");
                alert.setHeaderText("Congratulations on completing the challenge!");
                alert.setContentText(
                    String.format("You successfully converted %s to %s！\n\n" +
                                "Game Statistics: \n" +
                                "- Steps Number: %d\n" +
                                "- Transition Path: %s",
                                model.getStartWord().toUpperCase(),
                                model.getTargetWord().toUpperCase(),
                                model.getGameHistory().size() - 1,
                                String.join(" → ", model.getGameHistory()).toUpperCase()
                    )
                );
                alert.showAndWait();
            }
        } else if (model.isShowErrorMessage()) {
            view.showMessage("Invalid word!");
        }

        view.getInputField().clear();
        currentInput.setLength(0);
    }

    /**
     * Handling reset operations
     */
    private void handleReset() {
        model.initializeGame(model.getStartWord(), model.getTargetWord());
        view.clearHistory();
        view.getSubmitButton().setDisable(false);
        view.showMessage("");
        updateView();
    }

    /**
     * Handle new game actions
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
     * Update the view display
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

        // If path display is enabled, display the current path
        if (model.isShowPath() && !history.isEmpty()) {
            StringBuilder pathMessage = new StringBuilder("Current path: ");
            for (String word : history) {
                pathMessage.append(word).append(" -> ");
            }
            pathMessage.setLength(pathMessage.length() - 4); // Remove the last " -> "
            view.showMessage(pathMessage.toString());
        }
    }
} 