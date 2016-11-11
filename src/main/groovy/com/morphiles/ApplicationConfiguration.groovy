package com.morphiles

import com.morphiles.gui.GuiFrame
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
}
