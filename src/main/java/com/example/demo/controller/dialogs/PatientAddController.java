package com.example.demo.controller.dialogs;

import com.example.demo.infrastructure.LoginException;
import com.example.demo.model.Patient;
import com.example.demo.model.enums.Gender;
import com.example.demo.repository.PatientRepository;
import java.util.function.Consumer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/** Controller that is responsible for business logic of dialog that adds new patient to the system.
 *
 */
@Controller
@Getter
public class PatientAddController {

  @Autowired
  private  PatientRepository patientRepo;

  @FXML
  private Label title;

  @FXML
  private Label message;

  @FXML
  private TextField firstName;

  @FXML
  private TextField lastName;

  @FXML
  private TextField phoneNumber;

  @FXML
  private TextField city;

  @FXML
  private DatePicker datePicker;

  @FXML
  private ComboBox<Gender> genderBox;

  @FXML
  private Button deleteButton;

  @FXML
  private Button closeButton;

  @FXML
  private Button saveButton;

  private Patient patient;
  private Consumer<Patient> patientSaveHandler;

  /** Displays a dialog where user creates new patient.
   * @param patientSaveHandler Consumes accepted object.
   * @param genderList Handing over list of genders.
   */
  public static void createNew(Consumer<Patient> patientSaveHandler, Gender[] genderList) {
    editPatient(null, patientSaveHandler, genderList);
  }

  /** Displays a dialog where user edits already created patient.
   * @param patient Handing over already created object.
   * @param patientSaveHandler Consumes accepted object.
   * @param genderList Handing over list of genders.
   */
  public static void editPatient(Patient patient,
      Consumer<Patient> patientSaveHandler,
      Gender[] genderList) {

    try {
      Stage stage = new Stage(StageStyle.UNDECORATED);
      FXMLLoader loader = new FXMLLoader(
          PatientAddController.class.getClassLoader()
              .getResource("fxml/dialogs/patientAddDialog.fxml"));
      stage.setScene(new Scene(loader.load()));
      stage.initModality(Modality.APPLICATION_MODAL);

      PatientAddController patientAddController = loader.getController();
      patientAddController.init(patient, patientSaveHandler, genderList);

      stage.show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @FXML
  private void save() {
    try {
      patient.setFirstName(firstName.getText());
      patient.setLastName(lastName.getText());
      patient.setPhoneNumber(phoneNumber.getText());
      patient.setCity(city.getText());
      patient.setDateOfBirth(datePicker.getValue());
      patient.setGender(genderBox.getValue());

      patientSaveHandler.accept(patient);
      close();

    } catch (LoginException e) {
      message.setText(e.getMessage());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @FXML
  private void close() {
    genderBox.getScene().getWindow().hide();
  }

  private void init(Patient patient, Consumer<Patient> patientSaveHandler, Gender[] genderList) {
    this.patient = patient;
    this.patientSaveHandler = patientSaveHandler;
    genderBox.getItems().addAll(genderList);

    if (patient == null) {
      title.setText("Create new patient");
      this.patient = new Patient();
    } else {
      title.setText("Manage patient");
    }

    // binding data to edit dialog
    firstName.setText(this.patient.getFirstName());
    lastName.setText(this.patient.getLastName());
    phoneNumber.setText(this.patient.getPhoneNumber());
    city.setText(this.patient.getCity());
    datePicker.setValue(this.patient.getDateOfBirth());
    genderBox.setValue(this.patient.getGender());
  }

}
