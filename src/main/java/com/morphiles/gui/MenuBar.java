package com.morphiles.gui;

import com.morphiles.models.reports.Report;
import com.morphiles.models.reports.SummaryReport;
import com.morphiles.views.DataTable;
import com.morphiles.views.TableAndChartsViewer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

public class MenuBar extends JMenuBar implements ActionListener {

    private JMenu file;
    private JMenuItem open;
    private JMenuItem save1;
    private JMenuItem save2;
    private JMenuItem save3;
    private JMenuItem clearAllData;
    private JMenuItem exit;

    private JMenu view;
    private JMenuItem dashBoardView;

    private JMenu sort;
    private JMenuItem profitSort;
    private boolean isProfitSorted = false;

    private HandHistoryTabs histories;

    private JMenu tools;
    private JMenuItem options;

    private Properties props;
    private static final String LAST_DIR = "last_dir_accessed";
    private static final String PROPS_FILE = "config.props";

    public MenuBar(HandHistoryTabs histories){
        this.histories = histories;
        loadProperties();

        file = new JMenu("File");
        this.add(file);

        open = new JMenuItem("Open");
        file.add(open);

        file.addSeparator();

        save1 = new JMenuItem("Save active tab");
        save2 = new JMenuItem("Save all tabs");
        save3 = new JMenuItem("Save my hands (all tabs)");

        file.add(save1);
        file.add(save2);
        file.add(save3);
        file.addSeparator();


        exit = new JMenuItem("Close");
        file.add(exit);

        clearAllData = new JMenuItem("Clear all data");
        file.add(clearAllData);

        view = new JMenu("View");
        dashBoardView = new JMenuItem("Dashboard");
        view.add(dashBoardView);

        this.add(view);

        sort = new JMenu("Sort by..");
        profitSort = new JMenuItem("profit");
        profitSort.addActionListener(this);
        sort.add(profitSort);

        this.add(sort);

        tools = new JMenu("Tools");
        this.add(tools);

        options = new JMenuItem("Options");
        tools.add(options);

        open.addActionListener(this);
        save1.addActionListener(this);
        save2.addActionListener(this);
        save3.addActionListener(this);
        exit.addActionListener(this);

        dashBoardView.addActionListener(this);

        tools.addActionListener(this);

        options.addActionListener(this);

    }


    public void loadProperties(){
        if (props==null) try {

            props = new Properties();
            File f = new File(PROPS_FILE);

            if (f.isFile()){
                FileInputStream in = new FileInputStream(PROPS_FILE);
                props.load(in);
            } else {
                props.store(new FileOutputStream(PROPS_FILE), "");
            }

        } catch (IOException e) {
            e.printStackTrace();
            props = null;
        }
    }

    /**
     * Imports/Saves
     */
    public void actionPerformed(ActionEvent e) {

        //Handle open button action.
        if (e.getSource() == open) {
            openFile(this);

        } if (e.getSource() == save1) {
            // SAVE ACTIVE TAB
            TableAndChartsViewer dataTabs = GuiFrame.SINGLETON.getDataTabs();
            String tableName = dataTabs.getSelectedTableName();
            DataTable table = dataTabs.getSelectedTable();

            new ExcelExporter(table.getModel(), tableName);

        } if (e.getSource() == save2) {
            // SAVE ALL TABS
            TableAndChartsViewer dataTabs = GuiFrame.SINGLETON.getDataTabs();
            new ExcelExporter(dataTabs.getDataTables(), "AllPokerData");

        } if (e.getSource() == save3) {
            // SAVE My HANDS ONLY
            TableAndChartsViewer dataTabs = GuiFrame.SINGLETON.getDataTabs();
            new ExcelExporter(dataTabs.getDataTables(), "MyPokerData");

        } else if (e.getSource() == profitSort) {
            isProfitSorted = ! isProfitSorted;
            histories.getActiveTable().sortByProfit();
        } else if (e.getSource() == dashBoardView){
            List<Report> reports = new ArrayList<Report>();

            for (DataTable table : GuiFrame.SINGLETON.getDataTabs().getTables().values()){
                Iterator it = table.getChartReports().iterator();
                while (it.hasNext()){
                    for (Report report : ((ChartReports)it.next()).getReports()){
                        reports.add(report);
                    }
                }
            }

            new SummaryReport(reports);
        }
    }


    /**
     * Recursively processes the files that were already
     * extracted to a list of .txt files.
     * @param file
     */
    public void beginImport(File  file){
        String fileName = getFileName(file);
        try {
            if(!histories.contains(fileName)){
                FileImporter f = new FileImporter(file, histories, fileName);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        //StatsHolder.outputResults();
    }

    /**
     * extracts the file name only (minus the '.txt' file extension)
     * @param file
     * @return
     */
    public String getFileName(File file){
        //System.out.println(file.getName());
        String fileName = file.getName();
        if (fileName.contains(".txt")){
            fileName = file.getName().substring(0, file.getName().length()-4);
        }
        return fileName;
    }



    public List<File> getFilesToProcess(File file){
        List<File> fileList = getFileList(file, null);
        return fileList;
    }


    /**
     * recursively populate the fileList variable
     * with the names of all text files
     * @param file
     */
    public List<File> getFileList(File file, List<File> fileList){
        fileList = initFileList(fileList);

        if (file!=null){
            if (file.isDirectory()){
                File[] dir = file.listFiles();
                for(File fileInList : dir) {
                    getFileList(fileInList, fileList);
                }
            }else if (file.isFile() && (file.getName().endsWith(".txt"))) {
                fileList.add(file);
            }
        }
        return fileList;
    }

    public List<File> initFileList(List<File> fileList){
        if(fileList == null){
            return new ArrayList<File>();
        }
        return fileList;
    }


    public void openFile(final Component component){

        JFileChooser fc;
        String last_dir= "C:\\";

        if(props != null){
            last_dir =  props.getProperty(LAST_DIR);
        }

        if (last_dir != null){
            fc = new JFileChooser(last_dir);
        } else {
            fc = new JFileChooser();
        }
        // int returnVal = fc.showOpenDialog(this);
        fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        int returnVal = fc.showOpenDialog(component);


        File file = null;

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            file = fc.getSelectedFile();
            //This is where a real application would open the file.

            try {
                props.setProperty(LAST_DIR, file.getParent());
                props.store(new FileOutputStream(PROPS_FILE), "");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        final List<File> fileList = getFilesToProcess(file);


        if(histories!=null){

            new FileProcessor(fileList).execute();
            // ensure there's a repaint at the end of the EDT queue to
            // force the table to show.

        } else {
            String msg = "You have already imported this file.\n\nUse the 'monitor' menu item (not implemented yet) to initiate a " +
                    "live monitoring session\nfor the hand history."; // TODO - present them with yes/no option to enable the option here
            JOptionPane.showMessageDialog(GuiFrame.SINGLETON.getFrame(), msg, "Already exists", JOptionPane.WARNING_MESSAGE);
        }
    }


    class FileProcessor extends SwingWorker<Void, Void>
    {
        List<File> fileList;

        public FileProcessor(final List<File> fileList){
            this.fileList = fileList;
        }

        protected Void doInBackground() throws Exception
        {
            importFromFile();
            return null;
        }

        public void importFromFile(){
            final int fileListSize = fileList.size()-1;
            long start = System.currentTimeMillis();
            if (fileList.size()!= 0) {
                for (int i = 0; i < fileList.size(); i++) {
                    int percentComplete = 0;
                    if (i != 0) {
                        percentComplete = (i * 100) / fileListSize;
                    } else if (i == 0 && fileList.size() == 1) {
                        percentComplete = 100;
                    } else {
                        percentComplete = 0;
                    }

                    GuiFrame.SINGLETON.getStatusBar().getProgressBar().setValue(percentComplete);

                    beginImport(fileList.get(i));

                    //GuiFrame.SINGLETON.getDataTabs().getSelectedTable().getJTable().repaint();

                    System.out.println("Imported: " + i + "/" + fileListSize +
                            ", " + percentComplete + "% ");

                    long duration = System.currentTimeMillis() - start;
                    String processingTime = (duration / 1000) + "s taken";

                    long avgTimePerFile = duration / (i + 1);
                    String avgTimeLeft = (avgTimePerFile * (fileList.size() - 1 - i) / 1000) + "";

                    String approxTimeLeft = ", approx. " + avgTimeLeft + "s remaining";
                    GuiFrame.SINGLETON.getStatusBar().timeToProcess(processingTime + approxTimeLeft);
                }
                GuiFrame.SINGLETON.getStatusBar().timeToProcess("Processing Completed in ("
                                   + ((System.currentTimeMillis() - start) / 1000) +"s)");
            }
        }

        protected void done()
        {
            GuiFrame.SINGLETON.getFrame().revalidate();
        }
    }
}