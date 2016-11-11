package com.morphiles.gui;

import com.morphiles.models.PokerDataModel;
import com.morphiles.processors.BlankHandHistory;
import com.morphiles.processors.HandHistoryProcessor;
import com.morphiles.views.DataTable;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import org.springframework.beans.factory.annotation.Autowired;

public class AllDataTable extends JPanel {
	
	private JTable table;
    private PokerDataModel model;
	
	private JScrollPane scrollpane;

    private HandHistoryProcessor historyProcessor;

    private Hashtable<String, TableOptionsMenuBar> optionsMenuBar = new Hashtable<>();

    private HandHistoryTab h;

    @Autowired
    GuiFrame gui;
	
	/**
	 * Contstructor 
	 * Builds independent table model 
	 * and the JTable
	 * @param h
     * @param name
	 */
	public AllDataTable(HandHistoryTab h, String name){
		super();
		this.setLayout(new BorderLayout());
		this.setSize(400,475);

        this.h = h;

        model = new PokerDataModel();
        table = new JTable(model);

        historyProcessor = new BlankHandHistory(new DataTable(name,
                gui.getHandHistoryTabs(name).get(name)), model);

        table.setDefaultRenderer(String.class, CustomRenderer.getInstance(null, null, historyProcessor.getColumns().get("Player")));
        table.setAutoCreateRowSorter(true);

	    table.setVisible(true);
	    table.setEnabled(true);
	    table.setPreferredSize(new Dimension(1800,1024));

	    scrollpane = new JScrollPane(table);
	    scrollpane.setHorizontalScrollBarPolicy(
	    		JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	    scrollpane.setVerticalScrollBarPolicy(
	    		JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	    
	    table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	    this.add(scrollpane, BorderLayout.CENTER);
	    this.add(table.getTableHeader(), BorderLayout.NORTH);

        addTableOptionsMenu(name);


	    resizeColumns();
	    
	    this.setVisible(true);
	}

//    public void setHistoryProcessor(int gameHost){
//        switch(gameHost){
//            case (0):
//                new com.morphiles.views.AllDataTable()
//                historyProcessor = new BlankHandHistory();
//                break;
//            case (1):
//                historyProcessor = new PartyPokerHhProcessorImpl(this, model);
//                break;
//            case(2):
//                historyProcessor = new PokerStarsHhProcessorImpl(this, model);
//                break;
//        }
//    }

//    public String getHHProcessorFirstLine(){
//        return historyProcessor.getHH_FIRST_LINE_STARTS();
//    }
//
//    public void addData(String text, int handId, String name){
//        historyProcessor.addData(text, handId, name);
//        table.setModel(historyProcessor.getModel());
//    }

    private void addTableOptionsMenu(String name){
        optionsMenuBar.put(name, new TableOptionsMenuBar(this));
        this.add(optionsMenuBar.get(name), BorderLayout.SOUTH);
    }

//    public TableOptionsMenuBar getTableOptionsMenuBar(String name){
//        return optionsMenuBar.get(name);
//    }
//
//
//	public String getTableName(String data)
//	{
//		String table = data.substring(7, data.indexOf("Real Money")-2);
//		return table;
//	}
//
//    public JTable getJTable(){
//        return table;
//    }

    public PokerDataModel getMyTableModel(){
        return model;
    }

//    public Hashtable<String, TableOptionsMenuBar> getOptionsMenuBar(){
//        return optionsMenuBar;
//    }

    public int getRowCount(){
        return table.getRowCount();
    }
	

	
	private void resizeColumns(){
		int[] widths = {
				25, 30, 75, 50, 40, 30, 50, // header
				70, 60, 100, 50,     // preflop 
				65, 65, 60, 100, 50, // flop
				30, 65, 60, 100, 50, // turn
				30, 65, 60, 100, 60, // river
				40, 50, 60, 120, 55, 35, // footer data
				30, 30, 30, 30, 50   // Player Counts
		};
		
		for (int i=0; i<table.getColumnModel().getColumnCount(); i++)
		{	
			if (i >= widths.length){
				table.getColumnModel().getColumn(i).setMinWidth(100);
				table.getColumnModel().getColumn(i).setMaxWidth(100*3);
				table.getColumnModel().getColumn(i).setPreferredWidth(100);
			} else {
				table.getColumnModel().getColumn(i).setMinWidth(widths[i]);
				table.getColumnModel().getColumn(i).setMaxWidth(widths[i]*3);
				table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
			}
		}
	}

    public void highlightCells(List<String> valuesToHighlight, String forPlayer){
        for (int i=0; i<historyProcessor.getModel().getRowCount(); i++){
            for (int j=0; j<historyProcessor.getModel().getRowCount(); j++){
                //String cellValue = (String) model.getValueAt(i,j);

                int playerCol = historyProcessor.getColumns().get("Player").intValue();
                //String playerId = ((String)model.getValueAt(i, playerCol));

                table.setDefaultRenderer(String.class, CustomRenderer.getInstance((ArrayList)valuesToHighlight, forPlayer, playerCol));
                table.repaint();
            }
        }


    }

    /**
     * A singleton class that will format the table cells.
     * Think of this as a stamp that paints the table cells.
     */
    static class CustomRenderer extends DefaultTableCellRenderer
    {
        private static ArrayList<String> cellValuesToHighlight = null;
        private static String playerID = null;
        private static CustomRenderer instance = null;
        private static Integer playerColumnID = 0;

        public CustomRenderer(){
            super();
        }

        public static CustomRenderer getInstance(){
            if (instance==null){
                synchronized (CustomRenderer.class){
                    if (instance == null) {
                        instance = new CustomRenderer();
                    }
                }
            }
            return instance;
        }

        public static CustomRenderer getInstance(ArrayList<String> valuesToHighlight, String forPlayer, Integer playerCol){
            if (instance==null){
                synchronized (CustomRenderer.class){
                    if (instance == null) {
                        instance = new CustomRenderer();
                    }
                }
            }
            if (valuesToHighlight!=null && valuesToHighlight.size()!=0){
                cellValuesToHighlight = valuesToHighlight;
            } else {
                cellValuesToHighlight = null;
            }

            if (forPlayer != null){
                playerID = forPlayer;
            } else {
                playerID = null;
            }
            if (playerCol!=null){
                playerColumnID = playerCol;
            }

            return instance;
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
        {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            c.setBackground(new java.awt.Color(255,255,255));

            if (cellValuesToHighlight != null && cellValuesToHighlight.size()!=0){
                for(int i=0; i<cellValuesToHighlight.size(); i++){

                    if (playerID.equals("--All Players--") && ((String)table.getModel().getValueAt(row, column)).contains(cellValuesToHighlight.get(i))){
                            c.setBackground(new java.awt.Color(71, 255, 192));
                    } else if (((String)table.getModel().getValueAt(row, playerColumnID)).equals(playerID) &&
                            ((String)value).contains(cellValuesToHighlight.get(i))){

                             c.setBackground(new java.awt.Color(255, 192, 71));
                    }
                }
            } else if(playerID != null){
                if (((String)table.getModel().getValueAt(row, playerColumnID)).contains(playerID)){
                    c.setBackground(new java.awt.Color(225, 130, 245));
                } else {
                    c.setBackground(new java.awt.Color(255,255,255));
                }
            }

            return c;
        }
    }

	
}