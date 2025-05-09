package com.weaver.view;

/**
 * 游戏视图接口，定义了视图需要实现的基本方法
 */
public interface GameView {
    /**
     * 更新显示
     */
    void updateDisplay();

    /**
     * 显示错误信息
     * @param message 错误信息
     */
    void showError(String message);

    /**
     * 显示成功信息
     */
    void showSuccess();
} 