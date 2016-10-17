package com.morphiles.views;

import com.morphiles.gui.ChartReports;
import com.morphiles.gui.HandHistoryListTabs;
import com.morphiles.models.PokerDataModel;
import com.morphiles.processors.BlankHandHistory;
import com.morphiles.processors.HandHistoryProcessor;
import com.morphiles.processors.PartyPokerHhProcessorImpl;
import com.morphiles.processors.PokerStarsHhProcessorImpl;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class DataTable extends JPanel {

    public static final int UNKNOWN = 0;
    public static final int PARTYPOKER = 1;
    public static final int POKERSTARS = 2;
	
	private JTable table;
    private PokerDataModel model;
	
	private JScrollPane scrollpane;

    private JPanel tablePanel;

    private JSplitPane splitPane;

    private HandHistoryProcessor historyProcessor;

    private Hashtable<String, ChartReports> chartReports = new Hashtable<String, ChartReports>();

    private HandHistoryListTabs h;

    // use these params (fileURL and processor) when refreshing the data.
    // This is essentially metaData about the hand history.
    private String fileURL;


	/**
	 * Contstructor 
	 * Builds independent table model 
	 * and the JTable
	 * //@param h
	 */
	public DataTable(String name, HandHistoryListTabs h){
		super();
		this.setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(400,300));

        this.h = h;

        splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.add(initTable());
        splitPane.add(addChartReportsPanel(name));
        splitPane.setResizeWeight(0.1);

	    this.add(splitPane, BorderLayout.CENTER);
	    this.setVisible(true);
	}

    public JPanel initTable(){
        model = new PokerDataModel();
        table = new JTable(model);
        historyProcessor = new BlankHandHistory(this, model);

        table.setDefaultRenderer(String.class, CustomRenderer.getInstance(null, null, historyProcessor.getColumns().get("Player")));
        table.setAutoCreateRowSorter(true);//TODO - what the shit is this doing? - sorts the rows I think by profit - badly
        table.setVisible(true);
        table.setEnabled(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        scrollpane = new JScrollPane(table);
        scrollpane.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollpane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout());
        tablePanel.add(scrollpane, BorderLayout.CENTER);
        tablePanel.add(table.getTableHeader(), BorderLayout.NORTH);

        resizeColumns();

        return tablePanel;
    }

    public void setHistoryProcessor(int gameHost){
        switch(gameHost){
            case (UNKNOWN):
                historyProcessor = new BlankHandHistory(this, model);
                break;
            case (PARTYPOKER):
                historyProcessor = new PartyPokerHhProcessorImpl(this, model);
                break;
            case(POKERSTARS):
                historyProcessor = new PokerStarsHhProcessorImpl(this, model);
                break;
        }
    }

    public String getHHProcessorFirstLine(){
        return historyProcessor.getHH_FIRST_LINE_STARTS();
    }

    public void addData(String text, int handId, String name){
        historyProcessor.addData(text, handId, name);
        table.setModel(historyProcessor.getModel());
    }

    private JPanel addChartReportsPanel(String name){
        chartReports.put(name, new ChartReports(this));
        return chartReports.get(name);
    }

    public ChartReports getChartReport(String name){
        return chartReports.get(name);
    }


	public String setTableName(String data)
	{
		String table = data.substring(7, data.indexOf("Real Money")-2);
		return table;
	}

    public String getGameType(){
        return model.getGameType();
    }

    public JTable getJTable(){
        return table;
    }

    public PokerDataModel getModel(){
        return model;
    }

    public Collection<ChartReports> getChartReports(){
        return chartReports.values();
    }

    public int getRowCount(){
        return table.getRowCount();
    }
	
	private void resizeColumns(){
		int[] widths = {
				30, 30, 75, 50, 40, 30, 50, // header
				70, 60, 100, 50,     // preflop 
				65, 65, 50, 60, 100, 50, // flop
				30, 65, 50, 60, 100, 50, // turn
				30, 65, 50, 60, 100, 60, // river
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

    public void highlightCells(ArrayList<String> valuesToHighlight, String forPlayer){
        for (int i=0; i<historyProcessor.getModel().getRowCount(); i++){
            for (int j=0; j<historyProcessor.getModel().getRowCount(); j++){
                //String cellValue = (String) model.getValueAt(i,j);

                int playerCol = historyProcessor.getColumns().get("Player").intValue();
                //String playerId = ((String)model.getValueAt(i, playerCol));

                table.setDefaultRenderer(String.class, CustomRenderer.getInstance(valuesToHighlight, forPlayer, playerCol));

            }
        }
    }

    /**
     * set the file URL so we can call a refresh on the data without having
     * to respecify the
     * @param fileURL
     */
    public void setFileURL(String fileURL){
        this.fileURL = fileURL;
    }

    public String getFileURL(){
        return fileURL;
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