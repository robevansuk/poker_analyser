package com.morphiles.models.reports;

import com.morphiles.models.PokerDataModel;

import java.awt.*;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by user on 24/05/2014.
 */
public class WinLoseOtherPlayersOnlyReport extends Report {

    public WinLoseOtherPlayersOnlyReport(String reportName, PokerDataModel model){
        super();
        setReportName(reportName);
        this.setLayout(new BorderLayout());
        setModel(model);
        setPlayerId(model.getPlayerID());
        setChartTitle("Win/Lose rates (all players & hands)");

        for (int x=0; x<getLabels().size(); x++){
            handCounts.put(getLabels().get(x), new Integer(0));
            winsOverall.put(getLabels().get(x), new Integer(0));
            lossesOverall.put(getLabels().get(x), new Integer(0));
        }

        runReport();

        this.add(createChartPanel(), BorderLayout.CENTER);
    }

    public void runReport(){
        for (int i=0; i<getModel().getRowCount(); i++){

            incrementTotalHandCount();

            boolean flopSeen = !getModel().getValueAt(i, getModel().getColumnNamed("Pf Mv")).equals("fold");
            boolean holeCardsShown = getModel().getValueAt(i, getColumnNamed("Cards"))!=null && !getModel().getValueAt(i, getColumnNamed("Cards")).equals("");
            boolean isThisPlayer = getModel().getValueAt(i, getColumnNamed("Player"))!=null &&
                    !getModel().getValueAt(i, getColumnNamed("Player")).equals(getPlayerId());


            if (flopSeen && holeCardsShown && isThisPlayer){

                String player = (String) getModel().getValueAt(i, getColumnNamed("Player"));
                String ranks = (String) getModel().getValueAt(i, getColumnNamed("Cards"));
                ranks = ranks.substring(0,1) + ranks.substring(3,4);
                String handPreflop = (String) getModel().getValueAt(i, getColumnNamed("Pf Hand"));
                String handAfterFlop = (String) getModel().getValueAt(i, getColumnNamed("F Hand"));
                boolean win = getModel().getValueAt(i, getColumnNamed("Win")).equals("Win");

                storeHandForPlayer(player, ranks, handPreflop, handAfterFlop, win);
            }
        }

        incrementTotalGameCount();
    }

    public void storeHandForPlayer(String player, String ranks, String handPreflop, String handAfterFlop, boolean win){
        //        int index = addPlayer(player);
        //        boolean hasHit = hasHit(handPreflop, handAfterFlop);
        String handGrouping = getHandGrouping(handPreflop, ranks);
        String handLabel2 =  getHandLabel2(handPreflop, ranks);

//            //      incrementArrayForPlayer(
//            handCounts.put(handGrouping, handCounts.get(handGrouping) + 1);
//            if (handLabel2!=null){
//                handCounts.put(handLabel2, handCounts.get(handLabel2) + 1);
//            }
//
//                //          if (hasHit)
//                //              incrementArrayForPlayer(index, hitsOverall, handGrouping, handLabel2);
//
//        if (win){
//            //      incrementArrayForPlayer(
//            winsOverall.put(handGrouping, winsOverall.get(handGrouping) + 1);
//            if (handLabel2!=null){
//                winsOverall.put(handLabel2, winsOverall.get(handLabel2) + 1);
//            }
////            incrementArrayForPlayer(winsOverall, handGrouping, handLabel2);
//              //            if (hasHit) {
//              // Wins and has hit
//              //                incrementArrayForPlayer(index, hitsAndWins, handGrouping, handLabel2);
//              //            } else {
//              //                // Wins and misses
//              //                incrementArrayForPlayer(index, missAndWins, handGrouping, handLabel2);
//        } else {
//            //incrementArrayForPlayer(lossesOverall, handGrouping, handLabel2);
//            lossesOverall.put(handGrouping, lossesOverall.get(handGrouping) + 1);
//            if (handLabel2!=null){
//                lossesOverall.put(handLabel2, lossesOverall.get(handLabel2) + 1);
//            }
//        }

        //Times Played
        handCounts.put(handGrouping, new Integer(handCounts.get(handGrouping)) + 1);
        if(handLabel2!=null){
            handCounts.put(handLabel2, new Integer(handCounts.get(handLabel2)) + 1);
        }

        // Wins Overall
        if (win){
            winsOverall.put(handGrouping, new Integer(winsOverall.get(handGrouping)) + 1);
            if (handLabel2 != null) {
                winsOverall.put(handLabel2, new Integer(winsOverall.get(handLabel2)) + 1);
            }
        } else {
            lossesOverall.put(handGrouping, new Integer(lossesOverall.get(handGrouping)) + 1);
            if (handLabel2 != null) {
                lossesOverall.put(handLabel2, new Integer(lossesOverall.get(handLabel2)) + 1);
            }
        }
    }

    public void incrementArrayForPlayer(int index, List<Hashtable<String, Integer>> array, String handGrouping, String handLabel2){
        //Times Played
        array.get(index).put(handGrouping, new Integer(array.get(index).get(handGrouping))+1);
        if(handLabel2!=null){
            array.get(index).put(handLabel2, new Integer(array.get(index).get(handLabel2))+1);
        }
    }

    public int getWins(String preflopHandLabel){
//        int index = addPlayer(getPlayerId());
        return winsOverall.get(preflopHandLabel);
    }

    public int getLosses(String preflopHandLabel){
//        int index = addPlayer(getPlayerId());
        return (lossesOverall.get(preflopHandLabel));
    }


    /**
     * Adds the player to the players list array if it doesnt already exist.
     * @param
     */
//    public int addPlayer(String playerID){
//        int found = -1;
//        for (int idx=0; idx<players.size(); idx++){
//            if (players.get(idx).equals(playerID)){
//                found = idx;
//                break;
//            }
//        }
//
//        if (found==-1){
//            players.add(playerID);
//
//            handCounts.add(new Hashtable<String, Integer>());
//            winsOverall.add(new Hashtable<String, Integer>());
//            hitsOverall.add(new Hashtable<String, Integer>());
//            hitsAndWins.add(new Hashtable<String, Integer>());
//            missAndWins.add(new Hashtable<String, Integer>());
//            lossesOverall.add(new Hashtable<String, Integer>());
//
//            found = players.size()-1;
//
//            //populate the hashtables with 0's
//            for (int x=0; x< getLabels().size(); x++){
//                handCounts.get(found).put(getLabels().get(x), 0);
//                winsOverall.get(found).put(getLabels().get(x), 0);
//                hitsOverall.get(found).put(getLabels().get(x), 0);
//                hitsAndWins.get(found).put(getLabels().get(x), 0);
//                missAndWins.get(found).put(getLabels().get(x), 0);
//                lossesOverall.get(found).put(getLabels().get(x), 0);
//            }
//        }
//
//        return found;
//    }

    public boolean hasHit( String handPreflop, String handAfterFlop){
        if (!handPreflop.contains("Pair")){
            return (handAfterFlop.contains("PAIR")
                    || handAfterFlop.contains("2PAIR")
                    || handAfterFlop.contains("TWOPAIR")
                    || handAfterFlop.contains("SET")
                    || handAfterFlop.contains("FULL")
                    || handAfterFlop.contains("STRAIGHT")
                    || handAfterFlop.contains("FLUSH"));
        } else if ( handPreflop.contains("Pair")){
            return (handAfterFlop.contains("SET") || handAfterFlop.contains("FULL") || handAfterFlop.contains("QUADS"));
        } else {
            return false;
        }
    }
}
