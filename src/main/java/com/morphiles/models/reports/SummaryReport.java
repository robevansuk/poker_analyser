package com.morphiles.models.reports;

import com.morphiles.models.PokerDataModel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Copyright (c) 2002-2013 EAN.com, L.P. All rights reserved.
 *
 * @author robevans
 */
public class SummaryReport extends JFrame {

    private Map<String, Report> summaryReports = new Hashtable<String, Report>();
    private JTabbedPane tabbedPane;

    public SummaryReport(List<Report> reports){
        super("Summary of All Games");
        setupFrame();
        summariseReports(reports);
        addReportsToTabbedPane();
    }

    public void setupFrame(){
        this.setLayout(new BorderLayout());
        this.setSize(800,600);
        this.setVisible(true);
        tabbedPane = new JTabbedPane();
    }

    public void summariseReports(List<Report> reports){
        for (Report report : reports){
            if (!summaryReports.containsKey(report.getReportName())){
                addReport(report);
            }

            for (String handType : report.getLabels()){
                if (!summaryReports.get(report.getReportName()).handCounts.containsKey(handType)){
                    summaryReports.get(report.getReportName()).handCounts.put(handType, 0);
                    summaryReports.get(report.getReportName()).winsOverall.put(handType, 0);
                    summaryReports.get(report.getReportName()).lossesOverall.put(handType, 0);
                }

                summaryReports.get(report.getReportName()).handCounts.put(handType, summaryReports.get(report.getReportName()).handCounts.get(handType) + report.handCounts.get(handType));
                summaryReports.get(report.getReportName()).winsOverall.put(handType, summaryReports.get(report.getReportName()).winsOverall.get(handType) + report.winsOverall.get(handType));
                summaryReports.get(report.getReportName()).lossesOverall.put(handType, summaryReports.get(report.getReportName()).lossesOverall.get(handType) + report.lossesOverall.get(handType));
            }
            summaryReports.get(report.getReportName()).setTotalGameCount(summaryReports.get(report.getReportName()).getTotalGameCount() + report.getTotalGameCount());
            summaryReports.get(report.getReportName()).setTotalHandCount(summaryReports.get(report.getReportName()).getTotalHandCount() + report.getTotalHandCount());
        }
    }

    private void addReport(Report report){
        summaryReports.put(report.getReportName(), report);
    }


    public void addReportsToTabbedPane(){

        for (String reportName : summaryReports.keySet()){
            tabbedPane.addTab(reportName, summaryReports.get(reportName).createChartPanel());
        }
        this.add(tabbedPane, BorderLayout.CENTER);
    }
}
