package com.example.demo.controller;

import com.example.demo.controller.dialogs.InjectionAddController;
import com.example.demo.controller.dialogs.WritePrescriptionController;
import com.example.demo.model.Appointment;
import com.example.demo.model.Injection;
import com.example.demo.model.Patient;
import com.example.demo.model.Prescription;
import com.example.demo.model.User;
import com.example.demo.model.enums.InjectionStatus;
import com.example.demo.service.AppointmentService;
import com.example.demo.service.InjectionService;
import com.example.demo.service.PatientService;
import com.example.demo.service.PrescriptionService;
import com.example.demo.service.UserService;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/** Controller that is responsible for business logic of Doctor view.
 *
 */
@Controller
public class DoctorController extends AbstractController {

  @Autowired
  private AppointmentService appointmentService;
  @Autowired
  private PatientService patientService;
  @Autowired
  private InjectionService injectionService;
  @Autowired
  private UserService userService;
  @Autowired
  private PrescriptionService prescriptionService;

  @FXML
  private TableView<Appointment> appointmentsForTodayTable;

  @FXML
  private TableView<Appointment> appointmentsHistoryTable;

  @FXML
  private TextField searchField;

  private final ObservableList<Appointment> obsHistoryAppointments = FXCollections
      .observableArrayList();

  @FXML
  private void initialize() {
    todayAppointmentsTable();
    historyAppointments();
  }

  @FXML
  private void scheduleInjection() {
    List<User> nurses = userService.getAllNurses();
    List<Patient> patients = patientService.getAllPatients();
    InjectionAddController
        .addNewInjection(this::saveInjection, nurses, patients, InjectionStatus.values());
  }

  private void savePrescription(Prescription prescription) {
    prescriptionService.save(prescription);
  }

  @FXML
  private void writePrescription() {
    List<User> doctors = userService.getAllDoctors();
    List<Patient> patients = patientService.getAllPatients();
    WritePrescriptionController.addNewPrescription(this::savePrescription, patients, doctors);
  }

  private void todayAppointmentsTable() {
    Long doctorId = LoginController.loginUser.getId();
    appointmentsForTodayTable.getItems().clear();
    appointmentsForTodayTable.getItems().addAll(appointmentService.getAppointmentsForToday(doctorId));
  }

  private void historyAppointments() {
    Long doctorId = LoginController.loginUser.getId();
    obsHistoryAppointments.clear();
    obsHistoryAppointments.addAll(appointmentService.getAppointmentsForSpecificDoctor(doctorId));

    FilteredList<Appointment> filteredList = new FilteredList<>(obsHistoryAppointments, b -> true);

    searchField.textProperty().addListener((observable, oldValue, newValue) -> {
      filteredList.setPredicate(appointment -> {

        if (newValue == null || newValue.isEmpty()) {
          return true;
        }
        String lowerCaseFilter = newValue.toLowerCase();
        if (appointment.getPatientName().toLowerCase().contains(lowerCaseFilter)) {
          return true;
        } else if (appointment.getDate().toString().toLowerCase().contains(lowerCaseFilter)) {
          return true;
        } else if (appointment.getTime().toLowerCase().contains(lowerCaseFilter)) {
          return true;
        } else {
          return false;
        }
      });
    });

    SortedList<Appointment> appHistorySortedList = new SortedList<>(filteredList);
    appHistorySortedList.comparatorProperty().bind(appointmentsHistoryTable.comparatorProperty());
    appointmentsHistoryTable.setItems(appHistorySortedList);
  }

  private void saveInjection(Injection injection) {
    injectionService.save(injection);
  }

}
