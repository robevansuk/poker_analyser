package com.morphiles.gui;

/**
 * Copyright (c) 2002-2013 EAN.com, L.P. All rights reserved.
 *
 * @author robevans
 */
public class Application {

    /**
     * Application's main entry point
     * @param args
     */
    public static void main(String[] args){
        GuiFrame analyser = GuiFrame.SINGLETON;
        analyser.init();
    }
}
