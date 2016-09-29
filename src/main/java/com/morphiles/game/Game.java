package com.morphiles.game;

import javax.swing.JFrame;
import java.awt.*;

public class Game extends JFrame {
	
	private long id;
	private int totalPlayers;
	private int totalTables;
	private int prizePool;

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

    public static void main(String args[]){
        new Game();
        System.out.println("Goodbye!");
    }
}