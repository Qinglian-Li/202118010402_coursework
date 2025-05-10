package com.weaver.gui;

import com.weaver.controller.WeaverController;
import com.weaver.model.WeaverGame;
import com.weaver.view.WeaverView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The main application class of the GUI version
 */
public class WeaverGuiApplication extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        // Create Model
        WeaverGame model = new WeaverGame("dictionary.txt");
        
        // Create View
        WeaverView view = new WeaverView();

        // Initializing the game
        if (model.isUseRandomWords()) {
            String startWord = model.getRandomWord(4);
            String targetWord = model.getRandomWord(4);
            model.initializeGame(startWord, targetWord);
        } else {
            model.initializeGame("SOUL", "MATE");
        }

        // Create Controller
        WeaverController controller = new WeaverController(model, view);
        
        // Set Scene
        Scene scene = new Scene(view.getRoot(), 800, 600);
        primaryStage.setTitle("Weaver Game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
} 