package com.morphiles.models.reports;

import com.morphiles.models.PokerDataModel;
import org.jfree.chart.JFreeChart;

import java.awt.*;
import java.util.Hashtable;

/**
 * Created by user on 24/05/2014.
 */
public class CheckCheckBetSuccessReport extends Report {

    public JFreeChart chart;

    public CheckCheckBetSuccessReport(String reportName, PokerDataModel model){
        super();
        setReportName(reportName);
        this.setLayout(new BorderLayout());
        setModel(model);
        setChartTitle("CHECK on flop, CHECK turn, BET River (all players & hands)");
        for (int x=0; x<getLabels().size(); x++){
            handCounts.put(getLabels().get(x), new Integer(0));
            winsOverall.put(getLabels().get(x), new Integer(0));
            lossesOverall.put(getLabels().get(x), new Integer(0));
        }
        this.runReport();
        this.add(createChartPanel(), BorderLayout.CENTER);
    }

    public void runReport(){

        for (int i=0; i<getModel().getRowCount(); i++){

            incrementTotalHandCount();

            boolean isFMvCall = ((String)getModel().getValueAt(i, getColumnNamed("F Mv"))).contains("call");
            boolean isTMvCheck = ((String)getModel().getValueAt(i, getColumnNamed("T Mv"))).contains("check");
            boolean isRMvBet = ((String)getModel().getValueAt(i, getColumnNamed("R Mv"))).contains("all-In");
            boolean isRMvFolded = ((String)getModel().getValueAt(i, getColumnNamed("R Mv"))).contains("fold");

            String rContrib = (String)getModel().getValueAt(i, getColumnNamed("R Contribution"));
            //String tPot = (String)getModel().getValueAt(i, getColumnNamed("T Pot"));
            String stack = (String)getModel().getValueAt(i, getColumnNamed("Stack"));

            boolean isBetSizeInRange = false;
            if (rContrib!=null && !rContrib.equals("") && stack!=null && !stack.equals("")){
                isBetSizeInRange = (Double.parseDouble(rContrib) >= (Double.parseDouble(stack)));
            }

            if (isFMvCall && isTMvCheck && isRMvBet && !isRMvFolded && isBetSizeInRange)
            // TODO Also include All-IN switch for river bluffs all in.
            // TODO also include flop player count breakdown. eg. when 2, 3, 4 players are involved.
            { // doesn't include check raises on flop.

                String holeCards = (String) getModel().getValueAt(i, getModel().getColumnNamed("Cards")); // should be irrelevant for wins.
                String handPreflop = null;
                String ranks = null;
                if (holeCards != null && !holeCards.equals("")) {
                    ranks = holeCards.substring(0,1) + holeCards.substring(3,4);
                    handPreflop = (String) getModel().getValueAt(i, getModel().getColumnNamed("Pf Hand"));
                }

                boolean win = getModel().getValueAt(i, getModel().getColumnNamed("Win")).equals("Win");

                storeHand(ranks, handPreflop, win);
            }
        }

        incrementTotalGameCount();
    }

    public void storeHand(String ranks, String handPreflop, boolean win){
        String handPreflopLabel = getHandGrouping(handPreflop, ranks);
        String handLabel2 = getHandLabel2(handPreflop, ranks);

        //Times Played
        handCounts.put(handPreflopLabel, new Integer(handCounts.get(handPreflopLabel)) + 1);
        if(handLabel2!=null){
            handCounts.put(handLabel2, new Integer(handCounts.get(handLabel2)) + 1);
        }

        // Wins Overall
        if (win){
            winsOverall.put(handPreflopLabel, new Integer(winsOverall.get(handPreflopLabel)) + 1);
            if(handLabel2!=null) {
                winsOverall.put(handLabel2, new Integer(winsOverall.get(handLabel2)) + 1);
            }
        } else {
            lossesOverall.put(handPreflopLabel, new Integer(lossesOverall.get(handPreflopLabel)) + 1);
            if(handLabel2!=null) {
                lossesOverall.put(handLabel2, new Integer(lossesOverall.get(handLabel2)) + 1);
            }
        }
    }

    public int getWins(String preflopHandLabel){
        return winsOverall.get(preflopHandLabel);
    }

    public int getLosses(String preflopHandLabel){
        return lossesOverall.get(preflopHandLabel);
    }

}
