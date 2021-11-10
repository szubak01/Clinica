package com.example.demo.controller.dialogs;

import com.example.demo.controller.LoginController;
import com.example.demo.infrastructure.LoginException;
import com.example.demo.model.Injection;
import com.example.demo.model.Patient;
import com.example.demo.model.User;
import com.example.demo.model.enums.InjectionStatus;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Consumer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.StringConverter;
import org.springframework.stereotype.Controller;

/** Controller that is responsible for business logic of dialog that adds new injection to the system.
 *
 */
@Controller
public class InjectionAddController {

  User loggedDoctor = LoginController.loginUser;

  @FXML
  private TextField schedulingDoctor;

  @FXML
  private ComboBox<User> nurseComboBox;

  @FXML
  private ComboBox<Patient> patientComboBox;

  @FXML
  private ComboBox<InjectionStatus> statusComboBox;

  @FXML
  private DatePicker datePicker;

  @FXML
  private TextArea description;

  @FXML
  private Label title;

  @FXML
  private Label message;

  private Injection injection;
  private Consumer<Injection> saveHandler;


  /** Displays a dialog where user creates new injection.
   * @param saveHandler Consumes accepted object.
   * @param nurses Handing over the list of nurses.
   * @param patients Handing over the list of patients.
   * @param status Handing over the list of statuses.
   */
  @FXML
  public static void addNewInjection(
      Consumer<Injection> saveHandler,
      List<User> nurses,
      List<Patient> patients,
      InjectionStatus[] status) {

    editInjectionStatus(null, saveHandler, nurses, patients, status);
  }

  /** Displays a dialog where user edits already created injection.
   * @param injection Handing over already created object.
   * @param saveHandler Consumes accepted object.
   * @param nurses Handing over the list of nurses.
   * @param patients Handing over the list of patients.
   * @param status Handing over the list of statuses.
   */
  @FXML
  public static void editInjectionStatus(
      Injection injection,
      Consumer<Injection> saveHandler,
      List<User> nurses,
      List<Patient> patients,
      InjectionStatus[] status) {

    try {

      Stage stage = new Stage(StageStyle.UNDECORATED);
      FXMLLoader loader = new FXMLLoader(
          InjectionAddController.class.getClassLoader()
              .getResource("fxml/dialogs/injectionAdd.fxml"));
      stage.setScene(new Scene(loader.load()));
      stage.initModality(Modality.APPLICATION_MODAL);

      InjectionAddController injectionAddController = loader.getController();
      injectionAddController.init(injection, saveHandler, nurses, patients, status);

      stage.show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @FXML
  private void save() {
    try {

      injection.setDoctor(loggedDoctor);
      injection.setNurse(nurseComboBox.getValue());
      injection.setPatient(patientComboBox.getValue());
      injection.setStatus(statusComboBox.getValue());
      injection.setDate(datePicker.getValue());
      injection.setDescription(description.getText());

      saveHandler.accept(injection);
      cancel();
    } catch (LoginException e) {
      message.setText(e.getMessage());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @FXML
  private void cancel() {
    statusComboBox.getScene().getWindow().hide();
  }

  private void init(Injection injection,
      Consumer<Injection> saveHandler,
      List<User> nurses,
      List<Patient> patients,
      InjectionStatus[] status) {

    this.injection = injection;
    this.saveHandler = saveHandler;

    datePicker.setDayCellFactory(picker -> new DateCell() {
      public void updateItem(LocalDate date, boolean empty) {
        super.updateItem(date, empty);
        LocalDate today = LocalDate.now();
        setDisable(empty || date.compareTo(today) < 0);
      }
    });

    nurseComboBox.getItems().addAll(nurses);
    nurseComboBox.setConverter(new StringConverter<User>() {
      @Override
      public String toString(User user) {
        return user.getFirstName() + " " + user.getLastName();
      }

      @Override
      public User fromString(String s) {
        return null;
      }
    });

    patientComboBox.getItems().addAll(patients);
    patientComboBox.setConverter(new StringConverter<Patient>() {
      @Override
      public String toString(Patient patient) {
        return patient.getFirstName() + " " + patient.getLastName();
      }

      @Override
      public Patient fromString(String s) {
        return null;
      }
    });
    statusComboBox.getItems().addAll(status);

    if (injection == null) {
      title.setText("Schedule injection");
      this.injection = new Injection();
    } else {
      title.setText("Change status of injections");
    }

    // binding data to edit dialog
    schedulingDoctor.setText(loggedDoctor.getFirstName() + " " + loggedDoctor.getLastName());
    nurseComboBox.setValue(this.injection.getNurse());
    patientComboBox.setValue(this.injection.getPatient());
    statusComboBox.setValue(this.injection.getStatus());
    description.setText(this.injection.getDescription());
    datePicker.setValue(this.injection.getDate());
  }


}
