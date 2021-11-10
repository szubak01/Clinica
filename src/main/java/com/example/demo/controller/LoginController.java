package com.example.demo.controller;

import com.example.demo.HealthcareApplication;
import com.example.demo.infrastructure.LoginException;
import com.example.demo.model.User;
import com.example.demo.service.LoginService;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/** Controller that is responsible for business logic of Login view.
 *
 */
@Controller
public class LoginController {

  public static User loginUser;

  private final LoginService loginService;

  /**
   * @param loginService Injected service into LoginController.
   */
  @Autowired
  public LoginController(LoginService loginService) {
    this.loginService = loginService;
  }

  @FXML
  private TextField loginField;

  @FXML
  private PasswordField passwordField;

  @FXML
  private Label validationMessage;


  @FXML
  private Button btnClose;

  @FXML
  private Button btnLogin;

  @FXML
  private void close() {
    btnClose.getScene().getWindow().hide();
  }

  private void attachEvent() {
    loginField.getScene().setOnKeyPressed(event -> {

      if (event.getCode() == KeyCode.ENTER) {

        if (btnClose.isFocused()) {
          close();
        }

        if (btnLogin.isFocused()) {
          login();
        }
      }
    });
  }

  @FXML
  private void login() {
    try {
      loginUser = loginService.loggingIntoSystem(loginField.getText(), passwordField.getText());

      // open application
      MainFrameController.show();

      // close login view
      close();

    } catch (LoginException e) {
      validationMessage.setText(e.getMessage());

    } catch (Exception e) {
      e.printStackTrace();
      close();
    }
  }

  /** Loads an fxml file.
   * @param stage The view that will be shown.
   */
  public static void loadView(Stage stage) {
    try {
      FXMLLoader loader = new FXMLLoader(
          LoginController.class.getClassLoader().getResource("fxml/login.fxml"));
      loader.setControllerFactory(HealthcareApplication.getSpringContext()::getBean);

      Parent view = loader.load();

      stage.setScene(new Scene(view));
      stage.getIcons().add(new Image("images/logo.jpg"));
      stage.setTitle("Healthcare system");
      stage.show();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
