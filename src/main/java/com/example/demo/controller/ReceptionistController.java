package com.example.demo.controller;

import com.example.demo.controller.dialogs.AppAddController;
import com.example.demo.controller.dialogs.PatientAddController;
import com.example.demo.model.Appointment;
import com.example.demo.model.Patient;
import com.example.demo.model.User;
import com.example.demo.model.enums.Gender;
import com.example.demo.service.AppointmentService;
import com.example.demo.service.PatientService;
import com.example.demo.service.UserService;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/** Controller that is responsible for business logic of Receptionist view.
 *
 */
@Controller
public class ReceptionistController extends AbstractController {

  private final PatientService patientService;
  private final UserService userService;
  private final AppointmentService appointmentService;

  /**
   * @param patientService Injected service into ReceptionistController.
   * @param userService Injected service into ReceptionistController.
   * @param appointmentService Injected service into ReceptionistController.
   */
  @Autowired
  public ReceptionistController(PatientService patientService,
      UserService userService,
      AppointmentService appointmentService) {
    this.patientService = patientService;
    this.userService = userService;
    this.appointmentService = appointmentService;
  }

  // Observable list to store data from patient table
  private final ObservableList<Patient> observablePatientList = FXCollections
      .observableArrayList();

  // Observable list to store data from appointment table
  private final ObservableList<Appointment> obsAppointments = FXCollections
      .observableArrayList();

  @FXML
  private TextField searchField;

  @FXML
  private TextField searchAppointments;

  @FXML
  private TableView<Patient> patientsTableView;

  @FXML
  private TableView<User> doctorTable;

  @FXML
  private TableView<Appointment> appTable;

  @FXML
  public void initialize() {

    // PATIENTS DYNAMIC TABLE VIEW
    refreshPatientTable();
    showPatientTable();

    // DOCTOR TABLE
    refreshAndShowDoctorTable();

    // APPOINTMENT TABLE
    refreshAppointmentsTable();
    showAppointmentsTable();
  }

  private void showPatientTable() {
    FilteredList<Patient> filteredList = new FilteredList<>(observablePatientList, b -> true);

    searchField.textProperty().addListener((observable, oldValue, newValue) -> {
      filteredList.setPredicate(patient -> {

        if (newValue == null || newValue.isEmpty()) {
          return true;
        }
        String lowerCaseFilter = newValue.toLowerCase();
        if (patient.getFirstName().toLowerCase().contains(lowerCaseFilter)) {
          return true;
        } else if (patient.getLastName().toLowerCase().contains(lowerCaseFilter)) {
          return true;
        } else if (String.valueOf(patient.getDateOfBirth()).toLowerCase()
            .contains(lowerCaseFilter)) {
          return true;
        } else if (String.valueOf(patient.getGender()).toLowerCase().contains(lowerCaseFilter)) {
          return true;
        } else if (patient.getCity().toLowerCase().contains(lowerCaseFilter)) {
          return true;
        } else if (patient.getPhoneNumber().toLowerCase().contains(lowerCaseFilter)) {
          return true;
        } else {
          return false;
        }
      });
    });

    SortedList<Patient> patientSortedList = new SortedList<>(filteredList);
    patientSortedList.comparatorProperty().bind(patientsTableView.comparatorProperty());
    patientsTableView.setItems(patientSortedList);

    patientsTableView.setOnMouseClicked(mouseEvent -> {
      if (mouseEvent.getClickCount() == 2) {
        Patient patient = patientsTableView.getSelectionModel().getSelectedItem();
        if (patient != null) {
          PatientAddController.editPatient(patient, this::savePatient, Gender.values());
        }
      }
    });
  }

  @FXML
  private void createNewPatient() {
    PatientAddController.createNew(this::savePatient, Gender.values());
  }

  @FXML
  private void createNewAppointment(){
    List<User> doctors = userService.getAllDoctors();
    List<Patient> patients = patientService.getAllPatients();
    AppAddController.createAppointment(this::saveAppointment, doctors, patients, appointmentService);
  }

  private void savePatient(Patient patient) {
    patientService.save(patient);
    refreshPatientTable();
  }

  private void saveAppointment(Appointment appointment){
    appointmentService.save(appointment);
    refreshAppointmentsTable();
  }

  private void refreshPatientTable() {
    observablePatientList.clear();
    observablePatientList.addAll(patientService.getAllPatients());
  }

  private void refreshAndShowDoctorTable() {
    List<User> doctorList = userService.getAllDoctors();
    doctorTable.getItems().clear();
    doctorTable.getItems().addAll(doctorList);
  }

  private void refreshAppointmentsTable(){
    obsAppointments.clear();
    obsAppointments.addAll(appointmentService.getAllAppointments());
  }
  private void showAppointmentsTable(){

    FilteredList<Appointment> filteredList = new FilteredList<>(obsAppointments, b -> true);

    searchAppointments.textProperty().addListener((observable, oldValue, newValue) -> {
      filteredList.setPredicate(appointment -> {

        if (newValue == null || newValue.isEmpty()) {
          return true;
        }
        String lowerCaseFilter = newValue.toLowerCase();
        if (appointment.getPatientName().toLowerCase().contains(lowerCaseFilter)) {
          return true;
        } else if (appointment.getDoctorName().toLowerCase().contains(lowerCaseFilter)) {
          return true;
        } else // Doesn't match
          if (String.valueOf(appointment.getDate()).toLowerCase()
            .contains(lowerCaseFilter)) {
          return true;
        } else
            return appointment.getTime().toLowerCase().contains(lowerCaseFilter);
      });
    });

    SortedList<Appointment> appointmentsSortedList = new SortedList<>(filteredList);
    appointmentsSortedList.comparatorProperty().bind(appTable.comparatorProperty());
    appTable.setItems(appointmentsSortedList);

    appTable.setOnMouseClicked(mouseEvent -> {
      if (mouseEvent.getClickCount() == 2) {
        Appointment appointment = appTable.getSelectionModel().getSelectedItem();
        if (appointment != null) {
          List<User> doctors = userService.getAllDoctors();
          List<Patient> patients = patientService.getAllPatients();
          List<Appointment> appointments = appointmentService.getAllAppointments();
          AppAddController.editAppointment(appointment, this::saveAppointment, doctors, patients, appointmentService);
        }
      }
    });
  }

  @FXML
  private void deletePatient() {
    Patient selectedPatient = patientsTableView.getSelectionModel().getSelectedItem();

    if (selectedPatient == null) {
      Alert alert = new Alert(AlertType.INFORMATION);
      alert.setTitle("Unselected record");
      alert.setContentText("Please select record first!");
      alert.setHeaderText(null);
      alert.showAndWait();
    } else {
      Long selectedPatientId = selectedPatient.getId();
      patientService.deletePatientById(selectedPatientId);
      refreshPatientTable();
    }
  }

  @FXML
  private void deleteAppointment(){
    Appointment selectedApp = appTable.getSelectionModel().getSelectedItem();

    if (selectedApp == null) {
      Alert alert = new Alert(AlertType.INFORMATION);
      alert.setTitle("Unselected record");
      alert.setContentText("Please select record first!");
      alert.setHeaderText(null);
      alert.showAndWait();
    } else {
      Long selectedAppId = selectedApp.getId();
      appointmentService.deleteAppointmentById(selectedAppId);
      refreshAppointmentsTable();
    }
  }

}
