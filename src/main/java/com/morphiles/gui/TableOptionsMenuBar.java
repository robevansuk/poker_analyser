package com.morphiles.gui;

import org.jfree.chart.ChartPanel;

import javax.swing.*;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Rob Evans
 * Date: 15/06/13
 */
public class TableOptionsMenuBar extends JSplitPane implements ActionListener {

    private List<JCheckBox> buttons = new ArrayList<>();

    private JComboBox playerNameSelection;

    private JPanel menuPanel;
    JPanel chart;

    private JLabel show = new JLabel("SHOW:");
    private JLabel forLabel = new JLabel("FOR:");

    private String[] buttonNames = {"fold",
                                    "check",
                                    "calls",
                                    "bets",
                                    "raises",
                                    "all-In"};
    private DataTable table;

    private static final String START = "GO";
    private JButton startButton = new JButton(START);

    ArrayList<String> valuesToHighlight;

    public TableOptionsMenuBar(DataTable table){
        super(JSplitPane.VERTICAL_SPLIT);

        this.table = table;

        menuPanel = new JPanel();

        initMenuPanel();
        initChartPanel(false, "");
        this.setResizeWeight(0.03);
        this.setVisible(true);
    }

    private void initMenuPanel(){
        initButtons();
        initPlayerNameSelection();
        initOptionsMenuLayout();
        startButton.addActionListener(this);
        menuPanel.add(startButton);
    }

    public void initChartPanel(boolean isReady, String tabName){
        if(!isReady){
            chart = new JPanel();
            chart.setSize(400,300);

        } else {
            this.remove(chart);
            StatsHolder stats = new StatsHolder();
            stats.runWinLossReport(GuiFrame.SINGLETON.getDataTabs().getTables().get(tabName).getModel());
            chart = new ChartPanel(stats.createChart());
            chart.setLayout(new GridBagLayout());
            chart.setSize(10, 10);
        }
        this.add(chart);
    }

    private void initOptionsMenuLayout(){
        menuPanel.setLayout(new GridBagLayout());
        menuPanel.add(show);
//        for (JCheckBox button:buttons){
//            menuPanel.add(button);
//        }
        menuPanel.add(forLabel);
        menuPanel.add(playerNameSelection);
//        this.add(menuPanel);
    }

    public void initButtons(){
        for (String label : buttonNames){
            buttons.add(new JCheckBox(label));
        }
    }

    public void initPlayerNameSelection(){
        playerNameSelection = new JComboBox();
        addPlayerNameToSelectionBox("--All Players--");
    }

    public void addPlayerNameToSelectionBox(String name){
        for (int i=0; i<playerNameSelection.getItemCount(); i++){
            if (playerNameSelection.getItemAt(i).equals(name)){
                playerNameSelection.addItem(name);
                break;
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if(e.getSource() instanceof JButton){
            if(((JButton)e.getSource()).getActionCommand().equals(START)){
                valuesToHighlight = new ArrayList<>();

                for(JCheckBox item : buttons){
                      if(item.isSelected()){
                          valuesToHighlight.add(item.getLabel());
                      }
                }
                if (valuesToHighlight.size()!=0){
                    table.highlightCells(valuesToHighlight, (String) playerNameSelection.getSelectedItem());
                } else {
                    table.highlightCells(null, (String) playerNameSelection.getSelectedItem());
                }
            }
        }
    }
}
