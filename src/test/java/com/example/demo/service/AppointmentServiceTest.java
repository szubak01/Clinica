package com.example.demo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.example.demo.infrastructure.LoginException;
import com.example.demo.model.Appointment;
import com.example.demo.model.Patient;
import com.example.demo.model.User;
import com.example.demo.model.enums.Gender;
import com.example.demo.model.enums.Role;
import com.example.demo.repository.AppointmentRepository;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

  @Mock private AppointmentRepository appointmentRepo;
  @Captor private ArgumentCaptor<Appointment> captor;
  private AppointmentService underTest;
  private Appointment appointment;

  @BeforeEach
  void setUp() {
    underTest = new AppointmentService(appointmentRepo);

    appointment = new Appointment();
    appointment.setDate(LocalDate.now());
    appointment.setTime("8:30 - 9:00");
    appointment.setDoctor(new User("Thomas", "Hunt", "ThomasHunt", "password", Gender.MALE, Role.DOCTOR));
    appointment.setPatient(new Patient("John", "Doe", LocalDate.now(), Gender.MALE, "New York", "123456789"));

  }

  @Test
  void canGetAllAppointments() {
    //when
    underTest.getAllAppointments();
    underTest.getAppointmentsForToday(appointment.getDoctor().getId());
    underTest.getAppointmentsForSpecificDoctor(appointment.getDoctor().getId());
    underTest.getScheduledTime(appointment.getDoctor(), appointment.getDate());
    //then
    verify(appointmentRepo, times(4)).findAll();
  }

  @Test
  public void canSaveAppointment() {
    //when
    underTest.save(appointment);

    //then
    verify(appointmentRepo).save(captor.capture());
    assertThat(captor.getValue()).isEqualTo(appointment);
  }

  @Test
  void canDeleteAppointmentById() {
    //when
    underTest.deleteAppointmentById(1L);
    //then
    verify(appointmentRepo).deleteById(1L);
  }

  @Test
  public void shouldThrowExceptionIfTimeIsNull() {
    appointment.setTime(null);

    assertThatThrownBy(() -> underTest.save(appointment))
        .isInstanceOf(LoginException.class)
        .hasMessageContaining("Please select time.");
  }

  @Test
  public void shouldThrowExceptionIfDateIsNull() {
    appointment.setDate(null);

    assertThatThrownBy(() -> underTest.save(appointment))
        .isInstanceOf(LoginException.class)
        .hasMessageContaining("Please pick a date.");
  }

  @Test
  public void shouldThrowExceptionIfDoctorIsNull() {
    appointment.setDoctor(null);

    assertThatThrownBy(() -> underTest.save(appointment))
        .isInstanceOf(LoginException.class)
        .hasMessageContaining("Please pick a doctor.");
  }

  @Test
  public void shouldThrowExceptionIfPatientIsNull() {
    appointment.setPatient(null);

    assertThatThrownBy(() -> underTest.save(appointment))
        .isInstanceOf(LoginException.class)
        .hasMessageContaining("Please pick a patient.");
  }

}