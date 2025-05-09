package com.weaver.gui;

import com.weaver.controller.WeaverController;
import com.weaver.model.WeaverGame;
import com.weaver.view.WeaverView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * GUI版本的主应用程序类
 */
public class WeaverGuiApplication extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        // 创建模型
        WeaverGame model = new WeaverGame("dictionary.txt");
        
        // 创建视图
        WeaverView view = new WeaverView();
        
        // 创建控制器
        WeaverController controller = new WeaverController(model, view);
        
        // 设置场景
        Scene scene = new Scene(view.getRoot(), 800, 600);
        primaryStage.setTitle("Weaver Game");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        // 初始化游戏
        if (model.isUseRandomWords()) {
            String startWord = model.getRandomWord(4);
            String targetWord = model.getRandomWord(4);
            model.initializeGame(startWord, targetWord);
        } else {
            model.initializeGame("SOUL", "MATE");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
} 