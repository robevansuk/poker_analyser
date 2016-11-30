package com.morphiles.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Hashtable;

import javax.swing.DefaultListModel;
import javax.swing.JPanel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import org.springframework.stereotype.Component;

@Component
public class HandHistoryTab extends JPanel {
	
	private BorderLayout main = new BorderLayout();
	private Hashtable<String, JList> infos;
	private Hashtable<String, DefaultListModel> models;
	
	/**
	 * Creates a new hand History list 
	 * using a deault model list
	 * This is then added to using addLine(final String line)
	 * in a multithreaded manner.
	 */
	public HandHistoryTab(){
		super();
		setLayout(main);
		String name = "default";
		
		models = new Hashtable<String, DefaultListModel>();
        models.put(name, new DefaultListModel());
		infos = new Hashtable<String, JList>();

        infos.put(name, new JList(models.get(name)));
		
		JScrollPane scrollList = new JScrollPane(infos.get(name));
		scrollList.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollList.setPreferredSize(new Dimension(300,1));
		
		add(scrollList, BorderLayout.CENTER);
		
	}
	
	/**
	 * Appends a line of text to the JList
	 * in a multi threaded way!
	 * @param line
	 */
	public void addLine(final String name, final String line){
		
		//if (line.length()>75){
		//	model.add(model.getSize(), line.substring(0, 75));
		//} else {
			models.get(name).add(models.get(name).getSize(), line);
		//}
//		SwingUtilities.invokeLater(new Runnable(){
//			public void run(){
				infos.get(name).repaint();
//			}
//		});
	}
	
	public ListModel getModel(String name){
		return (ListModel) infos.get(name).getModel();
	}
	
	/**
	 * Cleanses the data that was brought in.
	 */
	public void cleanse(String name){
		if (infos.get(name).getModel().getSize() >=1) {
			String s;
			for (int i=0; i<infos.get(name).getModel().getSize(); i++){
				s = (String) infos.get(name).getModel().getElementAt(i);
				//System.out.println("Cleansing: " + s);
			}
		}
		else {
			System.out.println("Nothing to cleanse!");
		}
	}
	
}
