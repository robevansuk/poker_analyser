package com.morphiles.gui;

import com.morphiles.models.PokerDataModel;
import com.morphiles.views.DataTable;
import com.morphiles.views.TreeNavigator;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * This class serves the purpose of
 * cleansing data as it is imported.
 * @author Rob Evans
 *
 */
public class FileImporter {

	private File file;
	private HandHistoryTabs histories;
    private Properties props;

	/**
	 * creates a new file importer each time a file
	 * is specified... this passes the information to the
	 * hand histories.
	 *
	 * @param f
	 * @param histories
	 */
	public FileImporter(File f, HandHistoryTabs histories, String name){
		this.file = f;
		this.histories = histories;
        histories.setHandId(0);
        FileInputStream fstream = null;
        DataInputStream in = null;

		try{
            // Open the file that is the first
            // command line parameter
            fstream = new FileInputStream(f);
            // Get the object of DataInputStream
            in = new DataInputStream(fstream);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            String line;

            String tabName = file.getName().substring(0,file.getName().length()-4);

            // get to the first real line of text - partypoker hand histories start weirdly.
            while ((line = reader.readLine()) != null && line.equals("")){
                // get first non-null/empty line
            }

            // work out if the file has any data and
            // determine the hand history processor to use
            if (line !=null){
                histories.addTabbedHistoryListPane(tabName);

                // compare the line so you know which hand history processor to use.
                if (line.contains("Game #") && line.contains(" starts.")){
                    histories.getDataTable(tabName).setHistoryProcessor(DataTable.PARTYPOKER);
                } else if (line.contains("*********** # ")){
                    histories.getDataTable(tabName).setHistoryProcessor(DataTable.POKERSTARS);
                } else {
                    // will do its best
                    histories.getDataTable(tabName).setHistoryProcessor(DataTable.UNKNOWN);
                }

                histories.getDataTable(tabName).setFileURL(file.getAbsolutePath());



                // **********************************************************
                //******** PROCESS THE REMAINDER OF THE FILE! ***************
                // **********************************************************
                while (line != null )   {
                    // Print the content on the console
                    if (!line.equals("") && !line.startsWith(">")){
                        histories.append(tabName, line);
                    }
                    line = reader.readLine(); // one line gap between hands for most hand histories
                }

                // at the end of processing the null line won't be read - so send this
                // null line to print the last hand stored
                histories.append(tabName, "\u0000");


                String nodeToAddTo = null;
                PokerDataModel model = histories.getDataTable(tabName).getModel();
                if (model.getRowCount()>0) {
                    String buy_in = (String) model.getValueAt(0, model.getColumnNamed("BuyIn"));

                    if (buy_in != null && (buy_in.equals("0.00"))) {
                        nodeToAddTo = "Freeroll";
                    } else if (buy_in != null && !buy_in.equals("")) {
                        nodeToAddTo = "Trny_" + buy_in;
                    } else {
                        String bigBlind = (String) model.getValueAt(0, model.getColumnNamed("BB"));
                        String smallBlind = (String) model.getValueAt(0, model.getColumnNamed("SB"));
                        nodeToAddTo = histories.getDataTable(tabName).getGameType() + "_" + bigBlind + "/" + smallBlind;
                    }

                    TreeNavigator.INSTANCE.addNodeTo(nodeToAddTo, tabName);
                }

                // invokes the method that calls the getChart method.
                if( GuiFrame.SINGLETON.getDataTabs().getTables().get(tabName).getModel().getRowCount()>0){
                    GuiFrame.SINGLETON.getDataTabs().getTables().get(tabName).getChartReport(tabName).initChartPanel(true, tabName);
                }

                // Cleanse the data.
                histories.get(tabName).cleanse(tabName);

                // Close the input stream
                in.close();

            }

        }catch (Exception e){//Catch exception if any
           System.err.println("Error: " + e.getMessage());
           e.printStackTrace();
        }

        if(histories.contains("default")){
            GuiFrame.SINGLETON.removeTabsFor("default");
        }
	}
}
