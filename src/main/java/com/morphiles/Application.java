package com.morphiles;

import com.morphiles.gui.GuiFrame;

/**
 * Main entry point for the application
 * @author robevans
 */
public class Application {

    /**
     * Application main entry point
     * @param args
     */
    public static void main(String[] args){
        GuiFrame analyser = GuiFrame.SINGLETON;
        analyser.init();
    }
}
