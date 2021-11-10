package com.example.demo.controller;

import com.example.demo.HealthcareApplication;
import com.example.demo.controller.dialogs.LogoutDialogController;
import com.example.demo.model.enums.Role;
import com.example.demo.routing.Menu;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.springframework.stereotype.Controller;

/** Controller that is responsible for business logic of MainFrame view.
 *
 */
@Controller
public class MainFrameController {

  @FXML
  private VBox leftBar;

  @FXML
  private StackPane contentView;

  @FXML
  private void initialize() {
    Role userRole = LoginController.loginUser.getRole();

      if (userRole.equals(Role.ADMIN)) {
        loadView(Menu.adminMenu);
      }

      if (userRole.equals(Role.DOCTOR)) {
        loadView(Menu.doctorMenu);

        leftBar.getChildren().removeIf(node -> node.getId().equals("adminMenu"));
        leftBar.getChildren().removeIf(node -> node.getId().equals("receptionistMenu"));
        leftBar.getChildren().removeIf(node -> node.getId().equals("nurseMenu"));
      }

      if (userRole.equals(Role.NURSE)) {
        loadView(Menu.nurseMenu);

        leftBar.getChildren().removeIf(node -> node.getId().equals("adminMenu"));
        leftBar.getChildren().removeIf(node -> node.getId().equals("receptionistMenu"));
        leftBar.getChildren().removeIf(node -> node.getId().equals("doctorMenu"));
      }

      if (userRole.equals(Role.RECEPTIONIST)) {
        loadView(Menu.receptionistMenu);

        leftBar.getChildren().removeIf(node -> node.getId().equals("adminMenu"));
        leftBar.getChildren().removeIf(node -> node.getId().equals("doctorMenu"));
        leftBar.getChildren().removeIf(node -> node.getId().equals("nurseMenu"));
      }
  }

  @FXML
  private void clickMenu(MouseEvent event) {
    try {
      Node node = (Node) event.getSource();

      if (node.getId().equals("Logout")) {
        LogoutDialogController.DialogBuilder.builder()
            .title("Confirm")
            .message("Do you want to logout?")
            .okActionListener(() -> leftBar.getScene().getWindow().hide())
            .build()
            .show();

      } else {
        Menu menu = Menu.valueOf(node.getId());
        loadView(menu);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void loadView(Menu menu) {
    try {

      for (Node node : leftBar.getChildren()) {

        node.getStyleClass().remove("active");

        if (node.getId().equals(menu.name())) {
          node.getStyleClass().add("active");
        }
      }

      contentView.getChildren().clear();
      FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(menu.getFxml()));
      loader.setControllerFactory(HealthcareApplication.getSpringContext()::getBean);
      Parent view = loader.load();

      AbstractController aController = loader.getController();
      aController.setTitle(menu);

      contentView.getChildren().add(view);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /** Loads fxml file.
   * This method is responsible for loading mainFrame view.
   */
  public static void show() {

    try {
      Stage stage = new Stage();
      Parent root = FXMLLoader
          .load(MainFrameController.class.getClassLoader().getResource("fxml/mainFrame.fxml"));
      stage.setScene(new Scene(root));
      stage.setResizable(false);
      stage.getIcons().add(new Image("images/logo.jpg"));
      stage.setTitle("Healthcare system");
      stage.show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}

