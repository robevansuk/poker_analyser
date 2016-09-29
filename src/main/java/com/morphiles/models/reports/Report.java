package com.morphiles.models.reports;

import com.morphiles.models.PokerDataModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItem;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;


public abstract class Report extends JPanel{

    private String playerId;

    private String reportName;

    private int totalHandCount;
    private int totalGameCount;

    private static int index = 1;

    private static List<String> hands = new ArrayList<String>();

    protected Hashtable<String, Integer> handCounts = new Hashtable<String, Integer>();
    protected Hashtable<String, Integer> winsOverall = new Hashtable<String, Integer>();//without show
    protected Hashtable<String, Integer> lossesOverall = new Hashtable<String, Integer>();// without show - i.e. player folded

    private String chartTitle;

    private static String[] labels = new String[]{
            "JUNK","Conns-0","Conns-1","Conns-2","Conns-3",
            "SConns-0","SConns-1","SConns-2","SConns-3",
            "TXs","JXs","QXs","KXs","AXs",
            "PAIR-22-88","PAIR-99-JJ","PAIR-QQ","PAIR-KK-AA",
            "AK","AQ","AJ","KQ","KJ",
            "AA","KK","QQ","JJ","TT","99","88","77","66","55","44","33","22",
            ""}; // "" = no hand information available.

    private PokerDataModel model;

    static {
        for (String label : labels){
            hands.add(label);
        }
    }

    public abstract int getWins(String preflopHandLabel);

    public abstract int getLosses(String preflopHandLabel);

    public void incrementTotalHandCount(){
        totalHandCount++;
    }

    public int getTotalHandCount(){
        return totalHandCount;
    }

    public void incrementTotalGameCount(){
        totalGameCount++;
    }

    public int getTotalGameCount(){
        return totalGameCount;
    }

    public void setTotalHandCount(int totalHandCount) {
        this.totalHandCount = totalHandCount;
    }

    public void setTotalGameCount(int totalGameCount) {
        this.totalGameCount = totalGameCount;
    }

    public String getHandGrouping(String hand, String ranks){

        if (hand!=null) {
            if (hand.contains("Pair")) {
                String rank = ranks.substring(0, 1);
                if (rank.equals("2") || rank.equals("3") || rank.equals("4") ||
                        rank.equals("5") || rank.equals("6") || rank.equals("7") || rank.equals("8")) {
                    return "PAIR-22-88";
                } else if (rank.equals("9") || rank.equals("T") || rank.equals("J")) {
                    return "PAIR-99-JJ";
                } else if (rank.equals("Q")) {
                    return "PAIR-QQ";
                } else {
                    return "PAIR-KK-AA";
                }
            } else if (hand.startsWith("AK")) {
                return "AK";
            } else if (hand.startsWith("AQ")) {
                return "AQ";
            } else if (hand.startsWith("AJ")) {
                return "AJ";
            } else if (hand.startsWith("KQ")) {
                return "KQ";
            } else {
                if (!hands.contains(hand)) {
                    return "JUNK";
                } else {
                    return hand;
                }
            }
        } else {
            return "";
        }
    }

    public String getHandLabel2(String handPreflop, String card){
        if (handPreflop!=null && handPreflop.contains("Pair")) {
            String rank = card.substring(0, 1);
            return rank + rank;
        } else {
            return null;
        }
    }

    public List<String> getLabels(){
        return hands;
    }

    public int getColumnNamed(String name){
        return model.getColumnNamed(name);
    }

    public String getPlayerId(){
        return playerId;
    }

    public void setPlayerId(String playerId){
        this.playerId = playerId;
    }

    public void setModel(PokerDataModel model){
        this.model = model;
    }

    public PokerDataModel getModel(){
        return model;
    }

    private CategoryDataset createDataset() {

        // create the dataset...
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        dataset.addValue(getWins("AK"), "Win", "AK");
        dataset.addValue(getWins("AQ"), "Win", "AQ");
        dataset.addValue(getWins("AJ"), "Win", "AJ");
        dataset.addValue(getWins("KQ"), "Win", "KQ");
        dataset.addValue(getWins("KJ"), "Win", "KJ");
        dataset.addValue(getWins("AA"), "Win", "AA");
        dataset.addValue(getWins("KK"), "Win", "KK");
        dataset.addValue(getWins("QQ"), "Win", "QQ");
        dataset.addValue(getWins("JJ"), "Win", "JJ");
        dataset.addValue(getWins("TT"), "Win", "TT");
        dataset.addValue(getWins("99"), "Win", "99");
        dataset.addValue(getWins("88"), "Win", "88");
        dataset.addValue(getWins("77"), "Win", "77");
        dataset.addValue(getWins("66"), "Win", "66");
        dataset.addValue(getWins("55"), "Win", "55");
        dataset.addValue(getWins("44"), "Win", "44");
        dataset.addValue(getWins("33"), "Win", "33");
        dataset.addValue(getWins("22"), "Win", "22");
        dataset.addValue(getWins("SConns-1"), "Win", "SConns-1");
        dataset.addValue(getWins("SConns-2"), "Win", "SConns-2");
        dataset.addValue(getWins("SConns-3"), "Win", "SConns-3");
        dataset.addValue(getWins("Conns-1"), "Win", "Conns-1");
        dataset.addValue(getWins("Conns-2"), "Win", "Conns-2");
        dataset.addValue(getWins("Conns-3"), "Win", "Conns-3");
        dataset.addValue(getWins("JUNK"), "Win", "JUNK");
        dataset.addValue(getWins(""), "Win", "Unknown");

        dataset.addValue(0, "Draw", "AK");
        dataset.addValue(0, "Draw", "AQ");
        dataset.addValue(0, "Draw", "AJ");
        dataset.addValue(0, "Draw", "KQ");
        dataset.addValue(0, "Draw", "KJ");
        dataset.addValue(0, "Draw", "AA");
        dataset.addValue(0, "Draw", "KK");
        dataset.addValue(0, "Draw", "QQ");
        dataset.addValue(0, "Draw", "JJ");
        dataset.addValue(0, "Draw", "TT");
        dataset.addValue(0, "Draw", "99");
        dataset.addValue(0, "Draw", "88");
        dataset.addValue(0, "Draw", "77");
        dataset.addValue(0, "Draw", "66");
        dataset.addValue(0, "Draw", "55");
        dataset.addValue(0, "Draw", "44");
        dataset.addValue(0, "Draw", "33");
        dataset.addValue(0, "Draw", "22");
        dataset.addValue(0, "Draw", "SConns-1");
        dataset.addValue(0, "Draw", "SConns-2");
        dataset.addValue(0, "Draw", "SConns-3");
        dataset.addValue(0, "Draw", "Conns-1");
        dataset.addValue(0, "Draw", "Conns-2");
        dataset.addValue(0, "Draw", "Conns-3");
        dataset.addValue(0, "Draw", "JUNK");
        dataset.addValue(0, "Draw", "Unknown");

        dataset.addValue(getLosses("AK"), "Lose", "AK");
        dataset.addValue(getLosses("AQ"), "Lose", "AQ");
        dataset.addValue(getLosses("AJ"), "Lose", "AJ");
        dataset.addValue(getLosses("KQ"), "Lose", "KQ");
        dataset.addValue(getLosses("KJ"), "Lose", "KJ");
        dataset.addValue(getLosses("AA"), "Lose", "AA");
        dataset.addValue(getLosses("KK"), "Lose", "KK");
        dataset.addValue(getLosses("QQ"), "Lose", "QQ");
        dataset.addValue(getLosses("JJ"), "Lose", "JJ");
        dataset.addValue(getLosses("TT"), "Lose", "TT");
        dataset.addValue(getLosses("99"), "Lose", "99");
        dataset.addValue(getLosses("88"), "Lose", "88");
        dataset.addValue(getLosses("77"), "Lose", "77");
        dataset.addValue(getLosses("66"), "Lose", "66");
        dataset.addValue(getLosses("55"), "Lose", "55");
        dataset.addValue(getLosses("44"), "Lose", "44");
        dataset.addValue(getLosses("33"), "Lose", "33");
        dataset.addValue(getLosses("22"), "Lose", "22");
        dataset.addValue(getLosses("SConns-1"), "Lose", "SConns-1");
        dataset.addValue(getLosses("SConns-2"), "Lose", "SConns-2");
        dataset.addValue(getLosses("SConns-3"), "Lose", "SConns-3");
        dataset.addValue(getLosses("Conns-1"), "Lose", "Conns-1");
        dataset.addValue(getLosses("Conns-2"), "Lose", "Conns-2");
        dataset.addValue(getLosses("Conns-3"), "Lose", "Conns-3");
        dataset.addValue(getLosses("JUNK"), "Lose", "JUNK");
        dataset.addValue(getLosses(""), "Lose", "Unknown");

        return dataset;

    }

    /**
     * Creates a sample chart.
     *
     * @param dataset  the dataset.
     *
     * @return The chart.
     */
    protected JFreeChart createChart(CategoryDataset dataset) {

        // create the chart...
        JFreeChart chart = ChartFactory.createStackedBarChart(
                getChartTitle(),
                "Hands",                    // domain axis label
                "# Hands",                 // range axis label
                dataset,                      // data
                PlotOrientation.HORIZONTAL,   // orientation
                false,                        // include legend
                true,                         // tooltips?
                false                         // URLs?
        );

        chart.getTitle().setMargin(2.0, 0.0, 0.0, 0.0);

        TextTitle tt = new TextTitle("Results provided by sixCentsPoker.co.uk",new Font("Dialog", Font.PLAIN, 11));
        tt.setPosition(RectangleEdge.BOTTOM);
        tt.setHorizontalAlignment(HorizontalAlignment.RIGHT);
        tt.setMargin(0.0, 0.0, 4.0, 4.0);
        chart.addSubtitle(tt);

        TextTitle t = new TextTitle("Across " + getTotalHandCount() + " hands over "+ getTotalGameCount()+" games", new Font("Dialog", Font.PLAIN, 11));
        t.setPosition(RectangleEdge.BOTTOM);
        t.setHorizontalAlignment(HorizontalAlignment.RIGHT);
        t.setMargin(4.0, 0.0, 2.0, 4.0);
        chart.addSubtitle(t);

        // get a reference to the plot for further customisation...
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        LegendItemCollection items = new LegendItemCollection();
        items.add(new LegendItem("Win", null, null, null,
                new Rectangle2D.Double(-6.0, -3.0, 12.0, 6.0), Color.green));
        items.add(new LegendItem("Lose", null, null, null,
                new Rectangle2D.Double(-6.0, -3.0, 12.0, 6.0), Color.red));

        plot.setFixedLegendItems(items);
        plot.setInsets(new RectangleInsets(5, 5, 5, 20));
        LegendTitle legend = new LegendTitle(plot);
        legend.setPosition(RectangleEdge.BOTTOM);
        chart.addSubtitle(legend);

        plot.setDomainGridlinesVisible(true);

        // set the range axis to display integers only...
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        rangeAxis.setUpperMargin(0.0);

        // disable bar outlines...
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(true);

        ChartUtilities.applyCurrentTheme(chart);

        // set up gradient paints for series...
        GradientPaint gp0 = new GradientPaint(0.0f, 0.0f, Color.green,
                0.0f, 0.0f, new Color(0, 64, 0));
        Paint gp1 = new Color(0, 0, 0, 0);
        GradientPaint gp2 = new GradientPaint(0.0f, 0.0f, Color.red,
                0.0f, 0.0f, new Color(64, 0, 0));
        renderer.setSeriesPaint(0, gp0);
        renderer.setSeriesPaint(1, gp1);
        renderer.setSeriesPaint(2, gp2);

        return chart;

    }

    /**
     * Creates a panel for holding the barchart
     *
     * @return A panel.
     */
    public JPanel createChartPanel() {
        JFreeChart chart = createChart(createDataset());
        return new ChartPanel(chart);
    }

    public void setChartTitle(String title){
        this.chartTitle = title;
    }

    public String getChartTitle(){
        return chartTitle;
    }

    public void setReportName(String name){
        reportName = name;
    }

    public String getReportName(){
        return reportName;

    }
}
