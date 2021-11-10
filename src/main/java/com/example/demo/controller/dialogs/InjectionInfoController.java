package com.example.demo.controller.dialogs;

import com.example.demo.model.Injection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.stereotype.Controller;

/** Controller that is responsible for business logic of dialog that shows info about injection.
 *
 */
@Controller
public class InjectionInfoController {

  @FXML
  private TextField doctorField;

  @FXML
  private TextField nurseField;

  @FXML
  private TextField patientField;

  @FXML
  private TextField dateField;

  @FXML
  private TextField statusField;

  @FXML
  private TextArea descriptionArea;

  private Injection injection;

  @FXML
  void cancel() {
    descriptionArea.getScene().getWindow().hide();
  }

  /** Displays a dialog where user can see info about selected injection.
   * @param injection Object to be displayed.
   */
  public static void showInfo(Injection injection){
    try {

      Stage stage = new Stage(StageStyle.UNDECORATED);
      FXMLLoader loader = new FXMLLoader(
          InjectionInfoController.class.getClassLoader().getResource("fxml/dialogs/injectionInfo.fxml"));
      stage.setScene(new Scene(loader.load()));
      stage.initModality(Modality.APPLICATION_MODAL);

      InjectionInfoController injInfoController = loader.getController();
      injInfoController.init(injection);

      stage.show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void init(Injection injection) {

    this.injection = injection;

    //bind data to fields
    doctorField.setText(this.injection.getDoctorName());
    nurseField.setText(this.injection.getNurseName());
    patientField.setText(this.injection.getPatientName());
    dateField.setText(this.injection.getDate().toString());
    statusField.setText(this.injection.getStatus().toString());
    descriptionArea.setText(this.injection.getDescription());
  }
}
