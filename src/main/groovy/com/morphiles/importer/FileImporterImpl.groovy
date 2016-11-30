package com.morphiles.importer

import com.morphiles.gui.HandHistoryTabs
import com.morphiles.models.PokerDataModel
import com.morphiles.views.DataTable
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import static com.morphiles.views.DataTable.*
/**
 * TODO - this should really be implemented as a static class rather than using a constructor
 * This class serves the purpose of
 * cleansing data as it is imported.
 * @author Rob Evans
 *
 */
@Component
public class FileImporterImpl implements FileImporter {

  @Autowired
  HandHistoryTabs histories

  FileInputStream fstream
  DataInputStream inputStream

  /**
   * imports a file's hand history contents
   * @param file
   */
  @Override
  public void importFile(File file) {

    histories.setHandId(0)

    // only import the file if its not already imported
    if (histories.contains(getFileName(file)))
      return

    try{
      // Open the file that is the first
      // command line parameter
      fstream = new FileInputStream(file);

      // Get the object of DataInputStream
      inputStream = new DataInputStream(fstream);
      BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

      String line;

      String tabName = getFileName(file);

      // get to the first real line of text - partypoker hand histories start weirdly.
      while ((line = reader.readLine()) != null && line.equals("")){
        // get first non-null/empty line
      }

      // work out if the file has any data and
      // determine the hand history processor to use
      if (line != null) {

        DataTable dataTable = histories.addTabbedHistoryListPane(tabName);

        // compare the line so you know which hand history processor to use.
        if (line.contains("Game #") && line.contains(" starts.")){
          dataTable.setHistoryProcessor(PARTYPOKER);
        } else if (line.contains("*********** # ")){
          dataTable.setHistoryProcessor(POKERSTARS);
        } else {
          // will do its best
          dataTable.setHistoryProcessor(UNKNOWN);
        }

        dataTable.setFileURL(file.getAbsolutePath());

        // **********************************************************
        //******** PROCESS THE REMAINDER OF THE FILE! ***************
        // **********************************************************
        while (line != null)   {
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

//          gui.addNodeTo(nodeToAddTo, tabName);
        }

        // invokes the method that calls the getChart method.
        if(model.getRowCount()) {
          gui.getDataTabs().getTables().get(tabName).getChartReport(tabName).initChartPanel(true, tabName);
        }

        // Cleanse the data.
        histories.get(tabName).cleanse(tabName);

        // Close the input stream
        inputStream.close();

      }

    } catch (Exception e){//Catch exception if any
      System.err.println("Error: " + e.getMessage());
      e.printStackTrace();
    }

    if (histories.contains("default")) {
//      gui.removeTabsFor("default");
    }
  }


  /**
   * extracts the file name only (minus the '.txt' file extension)
   * @param file
   * @return
   */
  public String getFileName(File file){
    String fileName = file.getName();
    if (fileName.contains(".txt")){
      fileName = file.getName().substring(0, file.getName().length()-4);
    }
    return fileName;
  }
}
