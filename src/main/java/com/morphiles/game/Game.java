package com.morphiles.game;

import javax.swing.JFrame;
import java.awt.*;

/**
 * entry point for the application. This should just call
 * the gui class.
 */
public class Game extends JFrame {

    public Game(){
        super("Game Window");
        init();
    }

    private void init(){
        setSize(400, 400);
        this.setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    /**
     * entry point
     * @param args
     */
    public static void main(String args[]){
        new Game();
        System.out.println("Goodbye!");
    }
}