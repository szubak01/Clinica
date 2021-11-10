package com.example.demo.controller.dialogs;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.stereotype.Controller;

/** Controller that is responsible for business logic of logout dialog.
 *
 */
@Controller
public class LogoutDialogController {

  @FXML
  private Label title;
  @FXML
  private Label message;
  @FXML
  private Button okButton;
  @FXML
  private Button cancelButton;

  @FXML
  private void cancel() {
    okButton.getScene().getWindow().hide();
  }

  private Stage stage;

  public void show() {
    stage.show();
  }

  public static class DialogBuilder {

    private String title;
    private String message;
    private ActionListener okActionListener;

    private DialogBuilder() {
    }

    public DialogBuilder title(String title) {
      this.title = title;
      return this;
    }

    public DialogBuilder message(String message) {
      this.message = message;
      return this;
    }

    public DialogBuilder okActionListener(ActionListener okActionListener) {
      this.okActionListener = okActionListener;
      return this;
    }

    public LogoutDialogController build() {

      try {

        Stage stage = new Stage(StageStyle.UNDECORATED);
        FXMLLoader loader = new FXMLLoader(
            LogoutDialogController.class.getClassLoader()
                .getResource("fxml/dialogs/logoutDialog.fxml"));
        Parent view = loader.load();
        stage.setScene(new Scene(view));
        stage.initModality(Modality.APPLICATION_MODAL);

        LogoutDialogController controller = loader.getController();
        controller.stage = stage;
        controller.title.setText(this.title);
        controller.message.setText(this.message);

        if (null != okActionListener) {
          controller.okButton.setOnAction(actionEvent -> {
            controller.cancel();
            okActionListener.doAction();
          });
        } else {
          controller.cancelButton.setOnAction(actionEvent -> controller.cancel());
        }

        return controller;

      } catch (Exception e) {
        e.printStackTrace();
        throw new RuntimeException(e);
      }
    }

    public static DialogBuilder builder() {
      return new DialogBuilder();
    }
  }

  public interface ActionListener {
    void doAction();
  }
}
