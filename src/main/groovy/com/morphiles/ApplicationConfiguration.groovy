package com.morphiles

import com.morphiles.gui.DataPresentationTabs
import com.morphiles.gui.GuiFrame
import com.morphiles.gui.HandHistoryListTabs
import com.morphiles.gui.HandHistoryTabs
import com.morphiles.views.TableAndChartsViewer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
/**
 * This class provides instantiated beans/objects that can be
 * insterted into the application using AutoWired annotations
 */
@Configuration
class ApplicationConfiguration {

  @Bean
  public GuiFrame getGuiFrame() {
    new GuiFrame()
  }

  @Autowired
  HandHistoryTabs getHandHistoryTabs(TableAndChartsViewer datTabs){
    new HandHistoryTabs(datTabs)
  }

  @Bean
  DataPresentationTabs getDataPresentationTabs() {
    new DataPresentationTabs(new HandHistoryListTabs())
  }
}
