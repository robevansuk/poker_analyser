package com.morphiles

import com.morphiles.gui.*
import com.morphiles.importer.FileImporter
import com.morphiles.views.JStatusBar
import com.morphiles.views.TableAndChartsViewer
import com.morphiles.views.TreeNavigator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * This class provides instantiated beans/objects that can be
 * insterted into the application using AutoWired annotations
 */
@Configuration
class ApplicationConfiguration {

  @Autowired
  @Bean
  public GuiFrame getGuiFrame(HandHistoryTabs hhTabs, TableAndChartsViewer datTabs, TreeNavigator treeNavigator, JStatusBar statusBar, MenuBar menuBar) {
    new GuiFrame(hhTabs, datTabs, treeNavigator, statusBar, menuBar)
  }

  @Autowired
  @Bean
  MenuBar getMenuBar(HandHistoryTabs histories, TableAndChartsViewer datTabs, JStatusBar statusBar, FileImporter fileImporter){
    new MenuBar(histories, datTabs, statusBar, fileImporter)
  }

  @Bean
  DataPresentationTabs getDataPresentationTabs() {
    new DataPresentationTabs(new HandHistoryListTabs())
  }

  @Bean
  JStatusBar getStatusBar(){
    new JStatusBar("Poker Analyser", "File > Open > Select a file/folder of hand histories to get started!")
  }

}
