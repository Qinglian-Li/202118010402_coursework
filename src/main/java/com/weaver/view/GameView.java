package com.weaver.view;

/**
 * Game view interface, which defines the basic methods the view needs to implement
 */
public interface GameView {
    /**
     * Update the display
     */
    void updateDisplay();

    /**
     * Displaying error messages
     * @param message Error messages
     */
    void showError(String message);

    /**
     * Display success messages
     */
    void showSuccess();
} 