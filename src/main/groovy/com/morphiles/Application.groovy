package com.morphiles

import com.morphiles.gui.GuiFrame
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.context.annotation.ComponentScan
/**
 * Main entry point for the application
 */
@ComponentScan("com.morphiles")
public class Application {

  /**
   * This will set up the context for the application within the Spring framework
   * Spring will then register any components,
   * instantiate any required beans,
   * then autowire the beans that have been instantiated and registered,
   * this will then result in the GuiFrame 'component' being set up and displaying itself.
   * @param args
   */
  public static void main(String[] args) {
    ApplicationContext context =  new AnnotationConfigApplicationContext(Application.class);
    GuiFrame gui = context.getBean(GuiFrame.class);
  }
}

