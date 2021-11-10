package com.example.demo.service;

import com.example.demo.infrastructure.LoginException;
import com.example.demo.model.Appointment;
import com.example.demo.model.User;
import com.example.demo.repository.AppointmentRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Service class that provides methods which are responsible for saving, deleting, retrieving and filtering data obtained from database (Appointment Entity).
 *
 */
@Service
public class AppointmentService {

  private final AppointmentRepository appRepo;

  @Autowired
  public AppointmentService(AppointmentRepository appRepo){
    this.appRepo = appRepo;
  }

  /** This method saves passed object to the database.
   * @param appointment Passed object to be saved into the database.
   * @throws LoginException Exception is thrown when passed object contains null.
   */
  public void save(Appointment appointment) {
    if (appointment.getTime() == null){
      throw new LoginException("Please select time.");
    }
    if (appointment.getDate() == null){
      throw new LoginException("Please pick a date.");
    }
    if (appointment.getDoctor() == null){
      throw new LoginException("Please pick a doctor.");
    }
    if (appointment.getPatient() == null){
      throw new LoginException("Please pick a patient.");
    }
    appRepo.save(appointment);
  }

  /** Deletes object from database by indicated id.
   * @param id Indicates which object will be removed
   */
  public void deleteAppointmentById(Long id) {
    appRepo.deleteById(id);
  }

  /** This method is used to retrieve all the appointments from database.
   * @return All the appointments that are stored in database.
   */
  public List<Appointment> getAllAppointments() {
    return appRepo.findAll();
  }

  /** This method is used to get appointments that are scheduled for a specific doctor.
   * @param doctorId Indicates doctor for whom appointments will be selected.
   * @return List of appointments assigned to a specific doctor.
   */
  public List<Appointment> getAppointmentsForSpecificDoctor(Long doctorId){

    return appRepo.findAll()
        .stream()
        .filter(appointment -> appointment.getDoctor().getId().equals(doctorId))
        .collect(Collectors.toList());
  }

  /** This method is used to get appointments that are scheduled for a specific doctor on a specific day.
   * @param doctorId Indicates doctor for whom appointments will be selected.
   * @return List of appointments assigned to a specific doctor on a specific day.
   */
  public List<Appointment> getAppointmentsForToday(Long doctorId) {
    return getAppointmentsForSpecificDoctor(doctorId).stream()
        .filter(appointment -> appointment.getDate().equals(LocalDate.now()))
        .collect(Collectors.toList());
  }

  /** This method is used to get already scheduled blocks of time for a specific doctor on a specific day.
   * @param pickedDoctor Indicates doctor that is assigned to a specific appointment.
   * @param pickedDate Indicates date of the appointment.
   * @return List that contains blocks of time that are already scheduled for a specific doctor on a specific day.
   */
  public ObservableList<String> getScheduledTime(User pickedDoctor, LocalDate pickedDate){
    List<Predicate<Appointment>> predicates = new ArrayList<>();
    predicates.add(appointment -> appointment.getDoctor().getFirstName().equals(pickedDoctor.getFirstName()));
    predicates.add(appointment -> appointment.getDoctor().getLastName().equals(pickedDoctor.getLastName()));
    predicates.add(appointment -> appointment.getDate().equals(pickedDate));

    List<Appointment> appointmentsInSpecificDay = getAllAppointments()
        .stream()
        .filter(predicates.stream().reduce(appointment -> true, Predicate::and))
        .collect(Collectors.toList());

    Iterator<Appointment> it = appointmentsInSpecificDay.iterator();
    ObservableList<String> scheduledHours = FXCollections.observableArrayList();

    while(it.hasNext()) {
      scheduledHours.add(it.next().getTime());
    }

    return scheduledHours;
  }

}
