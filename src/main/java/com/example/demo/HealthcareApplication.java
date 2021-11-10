package com.example.demo;

import com.example.demo.controller.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/** Program that allows you to manage a health clinic.
 * @version 1.0
 */
@SpringBootApplication
public class HealthcareApplication extends Application{

  public static ConfigurableApplicationContext springContext;
  private FXMLLoader fxmlLoader;

  public static void main(String[] args) {
    Application.launch(args);
  }

  @Override
  public void start(Stage stage) throws Exception {
    LoginController.loadView(stage);
  }

  @Override
  public void init() throws Exception {
    springContext = SpringApplication.run(HealthcareApplication.class);
    fxmlLoader = new FXMLLoader();
    fxmlLoader.setControllerFactory(springContext::getBean);
  }

  @Override
  public void stop() throws Exception {
    springContext.stop();
  }

  public static ConfigurableApplicationContext getSpringContext(){
    return springContext;
  }
}
