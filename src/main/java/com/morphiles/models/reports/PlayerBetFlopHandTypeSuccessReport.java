package com.morphiles.models.reports;

import com.morphiles.models.PokerDataModel;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

/**
 * Created by user on 24/05/2014.
 */
public class PlayerBetFlopHandTypeSuccessReport extends Report {

    private JFreeChart chart;

    private static Hashtable<String,Integer> hands = new Hashtable<String, Integer>();

    private static String[] labels = {
      "", "HIGH CARD", "gutshot", "straightDraw", "flushDraw", "Overpair", "Underpair",
      "2PAIR", "PAIR", "SET", "STRAIGHT", "FLUSH", "FULL", "QUADS", "STRAIGHT FLUSH", "ROYAL FLUSH"
    };

    static {
        for (String label : labels){
            hands.put(label, 0);
        }
    }

    public PlayerBetFlopHandTypeSuccessReport(String reportName, PokerDataModel model){
        super();
        setReportName(reportName);
        this.setLayout(new BorderLayout());
        setModel(model);
        setChartTitle("Bet/Raise on flop (all players & hands)");
        for (int x=0; x<labels.length; x++){
            handCounts.put(labels[x], new Integer(0));
            winsOverall.put(labels[x], new Integer(0));
            lossesOverall.put(labels[x], new Integer(0));
        }
        this.runReport();
        this.add(createChartPanel(), BorderLayout.CENTER);
    }

    public void runReport(){

        for (int i=0; i<getModel().getRowCount(); i++){

            incrementTotalHandCount();

            boolean isFMvBet = ((String)getModel().getValueAt(i, getColumnNamed("F Mv"))).contains("bet");
            boolean isFMvRaise = ((String)getModel().getValueAt(i, getColumnNamed("F Mv"))).contains("raise");
            boolean isFMvFold = ((String)getModel().getValueAt(i, getColumnNamed("F Mv"))).contains("fold");
            String fHand = "";
            if (((String)getModel().getValueAt(i, getColumnNamed("F Hand")))!=null){
                fHand = ((String)getModel().getValueAt(i, getColumnNamed("F Hand")));
                for (String hand : hands.keySet()){
                    if (fHand.contains(hand)){
                        fHand = hand;
                        break;
                    }
                }
            }

            if ((isFMvBet || isFMvRaise) && !isFMvFold)
            {
                boolean win = getModel().getValueAt(i, getModel().getColumnNamed("Win")).equals("Win");

                storeHand(fHand, win);
            }
        }

        incrementTotalGameCount();
    }

    public void storeHand(String fHand, boolean win){
        handCounts.put(fHand, new Integer(handCounts.get(fHand)) + 1);
        if (win){
            winsOverall.put(fHand, new Integer(winsOverall.get(fHand)) + 1);
        } else {
            lossesOverall.put(fHand, new Integer(lossesOverall.get(fHand)) + 1);
        }
    }

    public int getWins(String fHand){
        return winsOverall.get(fHand);
    }

    public int getLosses(String fHand){
        return lossesOverall.get(fHand);
    }

    public List<String> getLabels(){
        return Arrays.asList(labels);
    }

    private CategoryDataset createDataset() {

        // create the dataset...
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (String hand : labels) {
            dataset.addValue(getWins(hand), "Win", hand);
        }

        for (String hand : labels) {
            dataset.addValue(0, "Draw", hand);
        }

        for (String hand : labels) {
            dataset.addValue(getLosses(hand), "Lose", hand);
        }

        return dataset;
    }

    /**
     * Creates a panel for holding the barchart
     *
     * @return A panel.
     */
    public JPanel createChartPanel() {
        JFreeChart chart = super.createChart(createDataset());
        return new ChartPanel(chart);
    }
}
