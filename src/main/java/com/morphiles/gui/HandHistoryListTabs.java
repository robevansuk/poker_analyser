package com.morphiles.gui;

import javax.swing.*;
import java.awt.*;
import java.util.Hashtable;

public class HandHistoryListTabs extends JPanel {

	private Hashtable<String, JList> historyLists = new Hashtable<String, JList>();
	private Hashtable<String, DefaultListModel> listModels = new Hashtable<String, DefaultListModel>();
	
	/**
	 * Creates a new hand History list 
	 * using a deault model list
	 * This is then added to using addLine(final String line)
	 * in a multithreaded manner.
	 */
	public HandHistoryListTabs(String name){
		super();
		setLayout(new BorderLayout());

        listModels.put(name, new DefaultListModel()); // TODO - provide own better JList models with highlighting
        historyLists.put(name, new JList(listModels.get(name)));

		add(getHistoryListInScrollPane(name), BorderLayout.CENTER);

	}

    public JScrollPane getHistoryListInScrollPane(String name){
        JScrollPane scrollList = new JScrollPane(historyLists.get(name));
        scrollList.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollList.setPreferredSize(new Dimension(300, getHeight()));
        return scrollList;
    }
	
	/**
	 * Appends a line of text to the JList
	 * in a multi threaded way!
	 * @param line
	 */
	public void addLine(final String name, final String line){
			listModels.get(name).add(listModels.get(name).getSize(), line);
			//historyLists.get(name).repaint();
	}
	
	public ListModel getModel(String name){
		return (ListModel) historyLists.get(name).getModel();
	}
	
	/**
	 * Cleanses the data that was brought in.
	 */
	public void cleanse(String name){
		if (historyLists.get(name).getModel().getSize() >=1) {
			String s;
			for (int i=0; i< historyLists.get(name).getModel().getSize(); i++){
				s = (String) historyLists.get(name).getModel().getElementAt(i);
				//System.out.println("Cleansing: " + s);
			}
		}
		else {
			System.out.println("Nothing to cleanse!");
		}
	}
	
}
