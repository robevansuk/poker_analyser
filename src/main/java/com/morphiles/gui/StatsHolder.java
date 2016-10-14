package com.morphiles.gui;

import com.morphiles.game.Card;
import com.morphiles.models.PokerDataModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.SubCategoryAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.GroupedStackedBarRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.KeyToGroupMap;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Hashtable;


public class StatsHolder extends JPanel {

    private static int index = 1;
    private static ArrayList<String> labels = new ArrayList<String>();
    private static ArrayList<String> newLabels = new ArrayList<String>();
    private static Hashtable<String, Integer> indexes = new Hashtable<String, Integer>();

	private JFreeChart line_chart;

    private ArrayList<String> players = new ArrayList<String>();
    private ArrayList<Hashtable<String, Integer>> handCounts = new ArrayList<Hashtable<String, Integer>>();
    private ArrayList<Hashtable<String, Integer>> winsOverall = new ArrayList<Hashtable<String, Integer>>();
    private ArrayList<Hashtable<String, Integer>> hitsOverall = new ArrayList<Hashtable<String, Integer>>();
    private ArrayList<Hashtable<String, Integer>> hitsAndWins = new ArrayList<Hashtable<String, Integer>>();
    private ArrayList<Hashtable<String, Integer>> missAndWins = new ArrayList<Hashtable<String, Integer>>();

    static{
        labels.add("PAIR");
        labels.add("AKs");
        labels.add("AK");
        labels.add("AQs");
        labels.add("AQ");
        labels.add("AJs");
        labels.add("AJ");
        labels.add("ATs");
        labels.add("AT");
        labels.add("KQs");
        labels.add("KQ");
        labels.add("KJs");
        labels.add("KJ");
        labels.add("AA");
        labels.add("KK");
        labels.add("QQ");
        labels.add("JJ");
        labels.add("TT");
        labels.add("99");
        labels.add("88");
        labels.add("77");
        labels.add("66");
        labels.add("55");
        labels.add("44");
        labels.add("33");
        labels.add("22");
        labels.add("QJs");
        labels.add("JTs");
        labels.add("T9s");
        labels.add("98s");
        labels.add("87s");
        labels.add("76s");
        labels.add("65s");
        labels.add("54s");
        labels.add("43s");
        labels.add("32s");
        labels.add("JUNK");

        for (String item : labels){
            indexes.put(item, new Integer(index++));
        }

        indexes.put("JUNK", new Integer(index++));

        newLabels.add("JUNK");
        newLabels.add("Conns-0");
        newLabels.add("Conns-1");
        newLabels.add("Conns-2");
        newLabels.add("Conns-3");
        newLabels.add("Sconns-0");
        newLabels.add("Sconns-1");
        newLabels.add("Sconns-2");
        newLabels.add("SConns-3");
        newLabels.add("TXs");
        newLabels.add("JXs");
        newLabels.add("QXs");
        newLabels.add("KXs");
        newLabels.add("AXs");
        newLabels.add("PAIR-22-88");
        newLabels.add("PAIR-99-JJ");
        newLabels.add("PAIR-QQ");
        newLabels.add("PAIR-KK-AA");

    }


	public StatsHolder(){
		super();

		this.setLayout(new BorderLayout());
		this.setSize(400,475);
		
		//line_chart = new JFreeChart(populate());
		
		//this.add(new ChartPanel(line_chart));
		this.setVisible(true);
	}

	public XYPlot populate()
	{
		XYPlot plot = (XYPlot) line_chart.getPlot();
        plot.setDomainCrosshairVisible(true);
        plot.setRangeCrosshairVisible(true);

        XYItemRenderer r = plot.getRenderer();
        if (r instanceof XYLineAndShapeRenderer) {
            XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;
            renderer.setBaseShapesVisible(true);
            renderer.setBaseShapesFilled(true);
            renderer.setSeriesPaint(0, Color.BLUE);
        }
        return plot;
	}


    public void runWinLossReport(PokerDataModel model){


        for (int i=0; i<model.getRowCount(); i++){
            //for( int j=0; j<model.getColumnCount(); j++){

                if (!model.getValueAt(i,9).equals("fold") && model.getValueAt(i,4)!=null && !model.getValueAt(i,4).equals("")){
                    String player = (String) model.getValueAt(i,2);
                    String ranks = (String) model.getValueAt(i,4);
                    ranks = ranks.substring(0,1) + ranks.substring(3,4);
                    String handPreflop = (String) model.getValueAt(i,7);
                    String handAfterFlop = (String) model.getValueAt(i,12);
                    boolean win = model.getValueAt(i,5).equals("Win");

                    storeHandForPlayer(player, ranks, handPreflop, handAfterFlop, win);
                }
            //}
        }
    }

    public void storeHandForPlayer(String player, String ranks, String handPreflop, String handAfterFlop, boolean win){
        boolean hit = false;

        String handPreflopLabel = "";

        int index = addPlayer(player);

        if (handPreflop.equals("Pair")){
            String rank = ranks.substring(0,1);
            if(rank.equals("2") || rank.equals("3") || rank.equals("4") ||
                    rank.equals("5") || rank.equals("6") || rank.equals("7") || rank.equals("8")){
                handPreflopLabel = "PAIR-22-88";
            } else if (rank.equals("9") || rank.equals("T") || rank.equals("J")) {
                handPreflopLabel = "PAIR-99-JJ";
            } else if ( rank.equals("Q")){
                handPreflopLabel = "PAIR-QQ";
            } else {
                handPreflopLabel = "PAIR-KK-AA";
            }
        }else {
            handPreflopLabel = handPreflop;
            if (!newLabels.contains(handPreflop)){
                handPreflopLabel = "JUNK";
            }
        }

        //Times Played
        handCounts.get(index).put(handPreflopLabel, new Integer(handCounts.get(index).get(handPreflopLabel))+1);

        // Hits Overall
        if (hasHit(handPreflop, handAfterFlop)){
             hitsOverall.get(index).put(handPreflopLabel, new Integer(hitsOverall.get(index).get(handPreflopLabel))+1);
        }
        // Wins Overall
        if (win){
            winsOverall.get(index).put(handPreflopLabel, new Integer(winsOverall.get(index).get(handPreflopLabel))+1);
        }

        // Hit and Win
        if (win && hasHit(handPreflop, handAfterFlop)){
            hitsAndWins.get(index).put(handPreflopLabel, new Integer(hitsAndWins.get(index).get(handPreflopLabel))+1);
        }
        // Miss and win
        if (win && !hasHit(handPreflop, handAfterFlop)){
            missAndWins.get(index).put(handPreflopLabel, new Integer(missAndWins.get(index).get(handPreflopLabel))+1);
        }
    }

    public void storeData(String playerID, String hand, int pre, int flop, int turn, int river)
    {
        int arrayID = addPlayer(playerID);
        addHandType(arrayID, hand);
    }

    /**
     * Adds the player to the players list array if it doesnt already exist.
     * @param playerID
     */
    public int addPlayer(String playerID){
        int found = -1;
        for (int idx=0; idx<players.size(); idx++){
            if (players.get(idx).equals(playerID)){
                found = idx;
                break;
            }
        }

        if (found==-1){
            players.add(playerID);

            handCounts.add(new Hashtable<String, Integer>());
            winsOverall.add(new Hashtable<String, Integer>());
            hitsOverall.add(new Hashtable<String, Integer>());
            hitsAndWins.add(new Hashtable<String, Integer>());
            missAndWins.add(new Hashtable<String, Integer>());

            found = players.size()-1;


            //populate the hashtables
            for (int x=0; x<newLabels.size(); x++){
                handCounts.get(found).put(newLabels.get(x), new Integer(0));
                winsOverall.get(found).put(newLabels.get(x), new Integer(0));
                hitsOverall.get(found).put(newLabels.get(x), new Integer(0));
                hitsAndWins.get(found).put(newLabels.get(x), new Integer(0));
                missAndWins.get(found).put(newLabels.get(x), new Integer(0));
            }

        }
        return found;
    }

    /**
     * increments the stats counter for the particular hand counter
     *
     */
    public void addHandType(int arrayID, String hand){

        Card card1 = new Card(hand.substring(0,2));
        Card card2 = new Card(hand.substring(3,5));

        if (card1.getRank() < card2.getRank()){
            //re-arrange cards other way arround for index IDs
            Card tmp = card1;
            card1 = card2;
            card2 = tmp;
        }

        localStore(arrayID, card1, card2);
    }


    public void localStore(int arrayID, Card card1, Card card2){
        StringBuilder lookupString = new StringBuilder(card1.getNamedRank() + card2.getNamedRank());

        if (card1.getSuit().equals(card2.getSuit())){
            lookupString.append("s");
        }

        if (!labels.contains(lookupString.toString())){
            lookupString = new StringBuilder("JUNK");
        }

        //increment the value for the playerID
        if(!handCounts.get(arrayID).containsKey(lookupString.toString())){
            handCounts.get(arrayID).put(lookupString.toString(), 0);
        }
        handCounts.get(arrayID).put(lookupString.toString(), handCounts.get(arrayID).get(lookupString.toString())+1);
    }

    public void outputResults(){
        for (int j = 0; j<players.size(); j++){
            System.out.println(players.get(j));
            System.out.println("================================");
            System.out.println("#\t\twins\thits\thits+wins\tmiss+wins\t\tHandType");

            for (int idx=0; idx<newLabels.size(); idx++){

                System.out.print(handCounts.get(j).get(newLabels.get(idx))+"\t\t");

                System.out.print(winsOverall.get(j).get(newLabels.get(idx))+"\t\t");
                System.out.print(hitsOverall.get(j).get(newLabels.get(idx))+"\t\t");
                System.out.print(hitsAndWins.get(j).get(newLabels.get(idx)) + "\t\t\t");
                System.out.print(missAndWins.get(j).get(newLabels.get(idx)) + "\t\t\t\t");
                System.out.println(":" + newLabels.get(idx));
            }
        }
    }

    public JFreeChart createChart(){
        return createChart(createDataset());
    }


    private JFreeChart createChart(final CategoryDataset dataset) {

        final JFreeChart stackedChart = ChartFactory.createStackedBarChart("Stacked Bar Chart", "Category", "Value",
                dataset, PlotOrientation.HORIZONTAL, true, true, false);

        //create group
        GroupedStackedBarRenderer renderer = new GroupedStackedBarRenderer();
        KeyToGroupMap map = createKeyToGroupMap();

        renderer.setSeriesToGroupMap(map);
        //margin between bar.
        renderer.setItemMargin(0.01);
        //end
//        SubCategoryAxis dom_axis = createSubCategoryAxis();

        CategoryPlot plot = (CategoryPlot) stackedChart.getPlot();
//        plot.setDomainAxis(dom_axis);
        plot.setRenderer(renderer);

        return stackedChart;
    }

    private SubCategoryAxis createSubCategoryAxis(){
        SubCategoryAxis dom_axis = new SubCategoryAxis("Hand Groups");
        //Margin between group
        dom_axis.setCategoryMargin(0.04);
        //end

        for (int j = 0; j<players.size(); j++){

            if(players.get(j).equals("pkrOD")){
                for (int idx=0; idx<newLabels.size(); idx++){
                    if (!(handCounts.get(j).get(newLabels.get(idx))==0
                            && hitsOverall.get(j).get(newLabels.get(idx))==0
                            && winsOverall.get(j).get(newLabels.get(idx))==0
                            && hitsAndWins.get(j).get(newLabels.get(idx))==0
                            && missAndWins.get(j).get(newLabels.get(idx))==0)){
                        dom_axis.addSubCategory(newLabels.get(idx) + "played");
                        dom_axis.addSubCategory(newLabels.get(idx) + "counts");
                        dom_axis.addSubCategory(newLabels.get(idx) + "win-lose");

                    }
                }
            }
        }
        return dom_axis;
    }

    private KeyToGroupMap createKeyToGroupMap()
    {
        KeyToGroupMap map = new KeyToGroupMap("G1");
        int grpIndex = 0;

        for (int j = 0; j<players.size(); j++){
            if(players.get(j).equals("pkrOD")){
                for (int idx=0; idx<newLabels.size(); idx++){
                    if (!(handCounts.get(j).get(newLabels.get(idx))==0
                            && hitsOverall.get(j).get(newLabels.get(idx))==0
                            && winsOverall.get(j).get(newLabels.get(idx))==0
                            && hitsAndWins.get(j).get(newLabels.get(idx))==0
                            && missAndWins.get(j).get(newLabels.get(idx))==0)){

                        map.mapKeyToGroup("played", "G" + grpIndex++);
                        map.mapKeyToGroup("hits", "G" + grpIndex++);
                        map.mapKeyToGroup("wins", "G" + grpIndex++);
                        map.mapKeyToGroup("win+hit", "G" + grpIndex++);
                        map.mapKeyToGroup("win+miss", "G" + grpIndex++);

                    }
                }
            }
        }
        return map;
    }

    private CategoryDataset createDataset() {

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (int j = 0; j<players.size(); j++){

            if(players.get(j).equals("pkrOD")){
                for (int idx=0; idx<newLabels.size(); idx++){
                    if (!(handCounts.get(j).get(newLabels.get(idx))==0
                            && hitsOverall.get(j).get(newLabels.get(idx))==0
                            && winsOverall.get(j).get(newLabels.get(idx))==0
                            && hitsAndWins.get(j).get(newLabels.get(idx))==0
                            && missAndWins.get(j).get(newLabels.get(idx))==0)){
////                        dataset.addValue(number,rowkey, colkey)
//                        dataset.addValue(handCounts.get(j).get(newLabels.get(idx)), newLabels.get(idx) + "#Hands Overall", "Hands");
//                        dataset.addValue(hitsOverall.get(j).get(newLabels.get(idx)), newLabels.get(idx) + "Hits Overall", "Hands");
//                        dataset.addValue(winsOverall.get(j).get(newLabels.get(idx)), newLabels.get(idx) + "Wins Overall", "Hands");
//                        dataset.addValue(hitsAndWins.get(j).get(newLabels.get(idx)), newLabels.get(idx) + "Hits+Wins", "Hands");
//                        dataset.addValue(missAndWins.get(j).get(newLabels.get(idx)), newLabels.get(idx) + "Miss+Wins", "Hands");

                        //legenditemcollection.add(new LegendItem(newLabels.get(idx)));


                        dataset.addValue(handCounts.get(j).get(newLabels.get(idx)), "played", newLabels.get(idx) + "played");
                        dataset.addValue(hitsOverall.get(j).get(newLabels.get(idx)), "hits", newLabels.get(idx) + "counts");
                        dataset.addValue(winsOverall.get(j).get(newLabels.get(idx)), "wins", newLabels.get(idx) + "counts");
                        dataset.addValue(hitsAndWins.get(j).get(newLabels.get(idx)), "win+hit", newLabels.get(idx) + "win-lose");
                        dataset.addValue(missAndWins.get(j).get(newLabels.get(idx)), "win+miss", newLabels.get(idx) + "win-lose");

                    }
                }
            }
        }
        return dataset;
    }


    public boolean hasHit( String handPreflop, String handAfterFlop){
      if (!handPreflop.equals("Pair")){
            if (handAfterFlop.contains("PAIR")
                || handAfterFlop.contains("2PAIR")
                || handAfterFlop.contains("TWOPAIR")
                || handAfterFlop.contains("SET")
                || handAfterFlop.contains("FULL")
                || handAfterFlop.contains("STRAIGHT")
                || handAfterFlop.contains("FLUSH")){
                return true;
            } else {
                return false;
            }
      } else if ( handPreflop.equals("Pair")){
          if (handAfterFlop.contains("SET") || handAfterFlop.contains("FULL") || handAfterFlop.contains("QUADS")){
              return true;
          } else {
              return false;
          }
      } else {
          return false;
      }
    }
}
