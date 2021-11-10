package com.example.demo.controller.dialogs;

import com.example.demo.infrastructure.LoginException;
import com.example.demo.model.Appointment;
import com.example.demo.model.Patient;
import com.example.demo.model.User;
import com.example.demo.service.AppointmentService;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Consumer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.StringConverter;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/** Controller that is responsible for business logic of dialog that adds new appointment to the system.
 *
 */
@Controller
@Getter
public class AppAddController {

  @Autowired
  private AppointmentService appService;

  @FXML
  private Label title;

  @FXML
  private Label message;

  @FXML
  private ComboBox<User> doctorCBox;

  @FXML
  private ComboBox<Patient> patientCBox;

  @FXML
  private DatePicker appDate;

  @FXML
  private ComboBox<String> appTime;

  @FXML
  private Button confirmButton;

  private Appointment appointment;
  private Consumer<Appointment> saveHandler;

  /** Displays a dialog where user creates new appointment.
   * @param saveHandler Consumes accepted object.
   * @param doctors Handing over the list of doctors.
   * @param patients Handing over the list of patients.
   * @param appointmentService Handing over the AppointmentService
   */
  public static void createAppointment(Consumer<Appointment> saveHandler, List<User> doctors,
      List<Patient> patients, AppointmentService appointmentService) {
    editAppointment(null, saveHandler, doctors, patients, appointmentService);
  }

  /** Displays a dialog where user edits already created appointment.
   * @param appointment Handing over already created object.
   * @param saveHandler Consumes accepted object.
   * @param doctors Handing over the list of doctors.
   * @param patients Handing over the list of patients.
   * @param appointmentService Handing over the AppointmentService
   */
  public static void editAppointment(Appointment appointment, Consumer<Appointment> saveHandler,
      List<User> doctors, List<Patient> patients, AppointmentService appointmentService) {
    try {
      Stage stage = new Stage(StageStyle.UNDECORATED);
      FXMLLoader loader = new FXMLLoader(
          AppAddController.class.getClassLoader().getResource("fxml/dialogs/appAddDialog.fxml"));
      stage.setScene(new Scene(loader.load()));
      stage.initModality(Modality.APPLICATION_MODAL);

      AppAddController appAddController = loader.getController();
      appAddController.init(appointment, saveHandler, doctors, patients, appointmentService);

      stage.show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @FXML
  void save() {
    try {

      appointment.setDoctor(doctorCBox.getValue());
      appointment.setPatient(patientCBox.getValue());
      appointment.setDate(appDate.getValue());
      appointment.setTime(appTime.getValue());

      saveHandler.accept(appointment);
      cancel();
    } catch (LoginException e) {
      message.setText(e.getMessage());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @FXML
  private void cancel() {
    appDate.getScene().getWindow().hide();
  }

  private void init(Appointment appointment, Consumer<Appointment> saveHandler, List<User> doctors,
      List<Patient> patients, AppointmentService appointmentService) {

    this.appointment = appointment;
    this.saveHandler = saveHandler;

    doctorCBox.getItems().addAll(doctors);
    patientCBox.getItems().addAll(patients);
    appTime.setDisable(true);

    if (appointment == null) {
      title.setText("Create new appointment");
      this.appointment = new Appointment();
    } else {
      title.setText("Edit appointment");
    }

    confirmButton.setOnAction(actionEvent -> {
      User pickedDoctor = doctorCBox.getValue();
      LocalDate pickedDate = appDate.getValue();
      if (pickedDoctor != null && pickedDate != null) {
        appTime.getItems().addAll(appTimeValues(appointmentService, pickedDoctor, pickedDate));
        appTime.setDisable(false);

        doctorCBox.setDisable(true);
        patientCBox.setDisable(true);
        appDate.setDisable(true);
        confirmButton.setDisable(true);

      }
    });

    //binding data for edit mode
    doctorCBox.setValue(this.appointment.getDoctor());
    patientCBox.setValue(this.appointment.getPatient());
    appDate.setValue(this.appointment.getDate());
    appTime.setValue(this.appointment.getTime());

    appDate.setDayCellFactory(picker -> new DateCell() {
      public void updateItem(LocalDate date, boolean empty) {
        super.updateItem(date, empty);
        LocalDate today = LocalDate.now();
        setDisable(empty || date.compareTo(today) < 0);
      }
    });

    doctorCBox.setConverter(new StringConverter<>() {
      @Override
      public String toString(User user) {
        return user.getFirstName() + " " + user.getLastName();
      }

      @Override
      public User fromString(String s) {
        return null;
      }
    });

    patientCBox.setConverter(new StringConverter<>() {
      @Override
      public String toString(Patient patient) {
        return patient.getFirstName() + " " + patient.getLastName();
      }

      @Override
      public Patient fromString(String s) {
        return null;
      }
    });
  }

  private ObservableList<String> appTimeValues(AppointmentService appointmentService,
      User pickedDoctor, LocalDate pickedDate) {

    ObservableList<String> values = FXCollections.observableArrayList();

    ObservableList<String> scheduledHours = appointmentService
        .getScheduledTime(pickedDoctor, pickedDate);

    values.add("8:00 - 8:30");
    values.add("8:30 - 9:00");
    values.add("9:00 - 9:30");
    values.add("9:30 - 10:00");
    values.add("10:00 - 10:30");
    values.add("10:30 - 11:00");
    values.add("11:00 - 11:30");
    values.add("11:30 - 12:00");
    values.add("12:00 - 12:30");
    values.add("12:30 - 13:00");
    values.add("13:00 - 13:30");
    values.add("13:30 - 14:00");
    values.add("14:00 - 14:30");
    values.add("14:30 - 15:00");
    values.add("15:00 - 15:30");
    values.add("15:30 - 16:00");

    values.removeAll(scheduledHours);
    return values;
  }
}
