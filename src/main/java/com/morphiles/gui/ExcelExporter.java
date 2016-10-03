package com.morphiles.gui;

import com.morphiles.models.PokerDataModel;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

/**
 *
 * @author robevans
 */
public class ExcelExporter {

    private Properties props;
    private static final String PROPS_FILE = "config.props";
    private static final String LAST_DIR_SAVED_TO = "last_dir_saved_to";

    private static int totalRows; // rows go up to 65537... so int easily suffices (up to 32m rows)

    /**
     * SAVE 1
     * @param model
     * @param fileName
     */
    public ExcelExporter(PokerDataModel model, String fileName){
        HSSFWorkbook workbook = new HSSFWorkbook();
        exportResults(model, fileName, workbook);
    }

    /**
     * SAVE 2 and 3 - ALL TABS - all hands or my hands only - depends on the file name.
     * @param models
     * @param fileName
     */
    public ExcelExporter(ArrayList<PokerDataModel> models, String fileName){
        HSSFWorkbook workbook = new HSSFWorkbook();
        totalRows  = 0;
        exportAllResults(models, fileName, workbook);
    }

    public void exportResults(PokerDataModel model, String fileName, HSSFWorkbook workbook){
        HSSFSheet sheet = workbook.createSheet(fileName);
        totalRows  = 0;

        copyHeaderToExcel(model, sheet);
        copyValuesToExcel(model, sheet);
        writeTheWorkbookToFile(workbook, fileName);

    }

    public void exportAllResults(ArrayList<PokerDataModel> models, String fileId, HSSFWorkbook workbook){

        createTableWithPokerData(models, fileId, workbook);
        createHandLookupSheet(workbook);
        writeTheWorkbookToFile(workbook, fileId);
    }

    public int getStopIndex(int i){
        switch(i){
            case 0:
                return 12;
            case 1:
                return 26;
            case 2:
                return 26;
            case 3:
                return 27;
            case 4:
                return 27;
            case 5:
                return 27;
            case 6: // PAIR
                return 24;
            case 7: // PAIR +gutshot
                return 24;
            case 8: // PAIR +straightDraw
                return 27;
            case 9: // PAIR +flushDraw
                return 27;
            case 10: // PAIR +flushDraw +gs
                return 27;
            case 11: // PAIR +flushDraw +sd
                return 27;
            case 12: // TWO PAIR
                return 24;
            case 13: // TWO PAIR +gutshot
                return 26;
            case 14: // TWO PAIR +straightDraw
                return 26;
            case 15: // TWO PAIR +flushDraw
                return 27;
            case 16: // TWO PAIR +flushDraw +gs
                return 27;
            case 17: // TWO PAIR +flushDraw +sd
                return 27;
            case 18: // SET
                return 24;
            case 19: // SET PAIR +gutshot
                return 26;
            case 20: // SET PAIR +straightDraw
                return 26;
            case 21: // SET PAIR +flushDraw
                return 27;
            case 22: // SET PAIR +flushDraw +gs
                return 27;
            case 23: // SET PAIR +flushDraw +sd
                return 27;
            case 24: // SET PAIR +flushDraw +sd
                return 27;
            default:
                return 29;
        }
    }

    public void copyHeaderToExcel(PokerDataModel model, HSSFSheet sheet){
        // Set the Column Header Values
        Row header = sheet.createRow(0);
        totalRows += 1;
        for(int c=0; c<model.getColumnCount(); c++){
            Cell cell = header.createCell(c);
            cell.setCellValue((String) model.getColumnName(c));
            //cell.setCellStyle(getHeaderCellStyle());
        }
    }

    public void copyDataLookupHeaderToExcel(HSSFSheet sheet){
        String[] colIndexes = {"Flop", "FCount", "FWins", "Turn", "TCount", "TWins", "River", "RCount", "RWins"};
        // Set the Column Header Values
        Row header = sheet.createRow(0);
        totalRows += 1;
        for(int c=0; c<colIndexes.length; c++){
            Cell cell = header.createCell(c);
            cell.setCellValue((String) colIndexes[c]);
        }
    }

    public void copyValuesToExcel(PokerDataModel model, HSSFSheet sheet){

        // Set the Cell Values
        for (int r=0; r<model.getRowCount(); r++){
            if(((totalRows+r) <= 65536)){
                Row row = sheet.createRow(totalRows+r);
                copyRowToExcel(model, r, row);
            }
        }
        totalRows = totalRows + model.getRowCount();
    }

    public void copyMyHandValuesToExcel(PokerDataModel model, HSSFSheet sheet){
        for (int r=0; r<model.getRowCount(); r++) {
            if (model.getValueAt(r, model.getColumnNamed("Player")).equals(model.getPlayerID())) {
                if (((totalRows + r) <= 65536)) {
                    Row row = sheet.createRow(totalRows);
                    totalRows = totalRows + 1;
                    copyRowToExcel(model, r, row);
                }
            }
        }
    }

    public void copyRowToExcel(PokerDataModel model, int r, Row row){
        for (int c=0; c<model.getColumnCount(); c++){
            Cell cell = row.createCell(c);
            cell.setCellValue((String) model.getValueAt(r, c));
        }
    }

    public void addSummaryData(String value, Row row, int col){
        Cell cell = row.createCell(col);
        if (value != null){
            if(value.substring(0, 1).equals("=")){
                cell.setCellFormula(value.substring(1));
            } else {
                cell.setCellValue(value);
            }
        }
    }

    public void createTableWithPokerData(ArrayList<PokerDataModel> models, String fileId, HSSFWorkbook workbook){

        int totalSheets = 1;
        HSSFSheet sheet = null;
        totalRows = 0;

        for (PokerDataModel m : models){

            if (m.getRowCount()>0) {

                if (fileId.equals("MyPokerData")) {

                    if (workbook.getNumberOfSheets()==0) {
                        sheet = workbook.createSheet("All_My_Hands");
                        copyHeaderToExcel(models.get(0), sheet);
                    }
                    // SAVE 2 - copy only my hands/rows to a single sheet.
                    copyMyHandValuesToExcel(m, sheet);
                } else {

                    // SAVE 3 - copy ALL hands/rows
                    totalRows = 0;
                    String sheetName = (String) m.getValueAt(1, m.getColumnNamed("Tbl Id"));

                    try {
                        if (sheetName != null && !sheetName.equals("")) {
                            sheet = workbook.createSheet(sheetName);
                        } else {
                            sheet = workbook.createSheet(totalSheets + "");
                        }
                        copyHeaderToExcel(models.get(0), sheet);
                        copyValuesToExcel(m, sheet);

                    } catch (Exception e){
                        System.out.println("ERROR: Sheet name duplication: " + sheetName);
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void createHandLookupSheet(HSSFWorkbook workbook){

        totalRows = 0;

        String[] hands = {
                "HIGH CARD",
                "gutshot",
                "straightDraw",
                "flushDraw",
                "flushDraw +gutshot",
                "flushDraw +straightDraw",
                "PAIR",
                "PAIR +gutshot",
                "PAIR +straightDraw",
                "PAIR +flushDraw",
                "PAIR +flushDraw +gutshot",
                "PAIR +flushDraw +straightDraw",
                "TWO PAIR",
                "TWO PAIR +gutshot",
                "TWO PAIR +straightDraw",
                "TWO PAIR +flushDraw",
                "TWO PAIR +flushDraw +gutshot",
                "TWO PAIR +flushDraw +straightDraw",
                "SET",
                "SET +gutshot",
                "SET +straightDraw",
                "SET +flushDraw",
                "SET +flushDraw +gutshot",
                "SET +flushDraw +straightDraw",
                "STRAIGHT",
                "STRAIGHT +flushDraw",
                "FLUSH",
                "FULL HOUSE",
                "QUADS",
                "STRAIGHT FLUSH",
                "ROYAL FLUSH"
        };


        HSSFSheet sheet = workbook.createSheet("HandLookup");

        copyDataLookupHeaderToExcel(sheet);

        for (int i=0; i<hands.length; i++){
            for (int k=i; k<getStopIndex(i); k++){
                for (int m=k; m<getStopIndex(k); m++){
                    if(totalRows < 65536){
                        Row row = sheet.createRow(totalRows);
                        totalRows++;

                        int col = 0;
                        addSummaryData(hands[i], row, col++);
                        addSummaryData("= COUNTIFS(AllMyHands!$E:$E,\">\"\"\",AllMyHands!$V:$V,\">\"\"\",AllMyHands!$M:$M,$A"+totalRows+")", row, col++);
                        addSummaryData("= COUNTIFS(AllMyHands!$E:$E,\">\"\"\",AllMyHands!$V:$V,\">\"\"\",AllMyHands!$M:$M,$A"+totalRows+",AllMyHands!$F:$F,\"Win\")", row, col++);
                        addSummaryData(hands[k], row, col++);
                        addSummaryData("= COUNTIFS(AllMyHands!$E:$E,\">\"\"\",AllMyHands!$V:$V,\">\"\"\",AllMyHands!$M:$M,$A"+totalRows+",AllMyHands!$R:$R,$D"+totalRows+")", row, col++);
                        addSummaryData("= COUNTIFS(AllMyHands!$E:$E,\">\"\"\",AllMyHands!$V:$V,\">\"\"\",AllMyHands!$M:$M,$A"+totalRows+",AllMyHands!$R:$R,$D"+totalRows+",AllMyHands!$F:$F,\"Win\")", row, col++);
                        addSummaryData(hands[m], row, col++);
                        addSummaryData("= COUNTIFS(AllMyHands!$E:$E,\">\"\"\",AllMyHands!$V:$V,\">\"\"\",AllMyHands!$M:$M,$A"+totalRows+",AllMyHands!$R:$R,$D"+totalRows+",AllMyHands!$W:$W,$G"+totalRows+")", row, col++);
                        addSummaryData("= COUNTIFS(AllMyHands!$E:$E,\">\"\"\",AllMyHands!$V:$V,\">\"\"\",AllMyHands!$M:$M,$A"+totalRows+",AllMyHands!$R:$R,$D"+totalRows+",AllMyHands!$W:$W,$G"+totalRows+",AllMyHands!$F:$F,\"Win\")", row, col++);

                    }
                }
            }
        }
    }

    public void writeTheWorkbookToFile(HSSFWorkbook workbook, String fileName){
        try {
            loadProperties();
            JFileChooser fc;
            String last_dir= "C:\\";

            if(props != null){
                last_dir =  props.getProperty(LAST_DIR_SAVED_TO);
            }

            if (last_dir != null){
                fc = new JFileChooser(last_dir);
            } else {
                fc = new JFileChooser();
            }

            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int returnVal = fc.showOpenDialog(null);

            File dir = null;

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                dir = fc.getSelectedFile();

                //This is where a real application would open the file.

                // Open the dir that is the first
                // command line parameter
                FileOutputStream out = new FileOutputStream(new File(dir.getPath()+File.separator+fileName+".xls"));
                workbook.write(out);
                out.close();

                props.setProperty(LAST_DIR_SAVED_TO, dir.getPath());
            }
            System.out.println("Excel written successfully..");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
}