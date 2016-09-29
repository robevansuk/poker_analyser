package com.morphiles.gui;

import com.morphiles.models.PokerDataModel;
import com.morphiles.models.reports.BetBetBetSuccessReport;
import com.morphiles.models.reports.BetBetSuccessReport;
import com.morphiles.models.reports.BetSuccessReport;
import com.morphiles.models.reports.PlayerAllInFlopHandTypeSuccessReport;
import com.morphiles.models.reports.PlayerBetFlopDetailedHandTypeSuccessReport;
import com.morphiles.models.reports.PlayerBetFlopHandTypeSuccessReport;
import com.morphiles.models.reports.PlayerCheckFlopDetailedHandTypeSuccessReport;
import com.morphiles.models.reports.Report;
import com.morphiles.models.reports.RiverBluffSuccessReport;
import com.morphiles.models.reports.WinLoseOtherPlayersOnlyReport;
import com.morphiles.models.reports.WinLoseReport;
import com.morphiles.models.reports.WinLoseThisPlayerOnlyReport;
import com.morphiles.views.DataTable;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;


/**
 * User: Rob Evans
 * Date: 15/06/13
 */
public class ChartReports extends JPanel {

    private DataTable table;
    private JTabbedPane tabbedPane;
    private List<Report> reports;

    public ChartReports(DataTable table){
        super();
        this.table = table;
        reports = new ArrayList<Report>();
        init();
        initChartPanel(false, "");
    }

    public void init(){
        this.setLayout(new BorderLayout());
        this.setEnabled(true);
        this.setVisible(true);
        tabbedPane= new JTabbedPane();
        tabbedPane.addTab("blank", new JPanel());
        this.add(tabbedPane, BorderLayout.CENTER);
        this.setPreferredSize(new Dimension(getWidth(), 300));
    }

    /**
     * need better name for isReady... this is actually used to signal
     * whether or not the chart is based on live data or whether its just
     * a part of the "startup" screen version.
     * @param isReady
     * @param tabName
     */
    public void initChartPanel(boolean isReady, String tabName){
        // This method will invoke a new set of tabs for the poker data model
        // when a new one is created
        if(isReady){
            createLiveCharts(tabName);
        }
    }

    public void createLiveCharts(String tabName){
        tabbedPane.removeAll();
        PokerDataModel model = table.getModel();

        reports.add(new BetSuccessReport("BetSuccess", model));
        reports.add(new BetBetSuccessReport("BetBetSuccess", model));
        reports.add(new BetBetBetSuccessReport("BetBetBetSuccess", model));
        reports.add(new RiverBluffSuccessReport("RiverBluffSuccess",model));
        reports.add(new WinLoseReport("Win/Lose (Other Players)", model));
        reports.add(new WinLoseThisPlayerOnlyReport("Win/Lose (This Player Only)", model));
        reports.add(new WinLoseOtherPlayersOnlyReport("Win/Lose (Other Players Only)", model));
        reports.add(new PlayerBetFlopHandTypeSuccessReport("Stats for hands bet on the flop", model));
        reports.add(new PlayerAllInFlopHandTypeSuccessReport("Stats for hands all-In'd on the flop", model));
        reports.add(new PlayerBetFlopDetailedHandTypeSuccessReport("Win/Lose stats, BET from flop (all players & hands)", model));
        reports.add(new PlayerCheckFlopDetailedHandTypeSuccessReport("Win/Lose stats, Check/Call from flop (all players & hands)", model));
        //reports.add(new PlayerFlopMovePieChartsReport("Player move stats (all hands)", model));


        for (int i=0; i<reports.size(); i++) {
            tabbedPane.addTab(reports.get(i).getReportName(), reports.get(i));
        }

        this.setPreferredSize(new Dimension(getWidth(), 300));
    }

    public List<Report> getReports(){
        return reports;
    }
}
