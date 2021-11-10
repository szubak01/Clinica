package com.example.demo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;

import com.example.demo.infrastructure.LoginException;
import com.example.demo.model.Patient;
import com.example.demo.model.enums.Gender;
import com.example.demo.repository.PatientRepository;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {


  @Mock
  private PatientRepository patientRepo;
  @Captor
  private ArgumentCaptor<Patient> captor;
  private PatientService underTest;
  private Patient patient;

  @BeforeEach
  void setUp() {
    underTest = new PatientService(patientRepo);

    patient = new Patient();
    patient.setFirstName("Firstname");
    patient.setLastName("Lastname");
    patient.setDateOfBirth(LocalDate.now());
    patient.setGender(Gender.MALE);
    patient.setCity("City");
    patient.setPhoneNumber("123123123");
  }

  @Test
  void canGetAllPatients() {
    //when
    underTest.getAllPatients();
    //then
    verify(patientRepo).findAll();
  }

  @Test
  void canDeletePatientById() {
    //when
    underTest.deletePatientById(1L);
    //then
    verify(patientRepo).deleteById(1L);
  }

  @Test
  void canSavePatient() {
    //when
    underTest.save(patient);

    //then
    verify(patientRepo).save(captor.capture());
    assertThat(captor.getValue()).isEqualTo(patient);
  }

  @Test
  public void shouldThrowExceptionIfFirsNameIsNull() {
    patient.setFirstName(null);

    assertThatThrownBy(() -> underTest.save(patient))
        .isInstanceOf(LoginException.class)
        .hasMessageContaining("Please enter first name.");
  }

  @Test
  public void shouldThrowExceptionIfLastNameIsNull() {
    patient.setLastName(null);

    assertThatThrownBy(() -> underTest.save(patient))
        .isInstanceOf(LoginException.class)
        .hasMessageContaining("Please enter last name.");
  }

  @Test
  public void shouldThrowExceptionIfDateIsNull() {
    patient.setDateOfBirth(null);

    assertThatThrownBy(() -> underTest.save(patient))
        .isInstanceOf(LoginException.class)
        .hasMessageContaining("Please pick a date.");
  }

  @Test
  public void shouldThrowExceptionIfCityIsNull() {
    patient.setCity(null);

    assertThatThrownBy(() -> underTest.save(patient))
        .isInstanceOf(LoginException.class)
        .hasMessageContaining("Please enter city.");
  }

  @Test
  public void shouldThrowExceptionIfGenderIsNull() {
    patient.setGender(null);

    assertThatThrownBy(() -> underTest.save(patient))
        .isInstanceOf(LoginException.class)
        .hasMessageContaining("Please select gender.");
  }

  @Test
  public void shouldThrowExceptionIfPhoneNumberIsNull() {
    patient.setPhoneNumber(null);

    assertThatThrownBy(() -> underTest.save(patient))
        .isInstanceOf(LoginException.class)
        .hasMessageContaining("Please enter phone number.");
  }
}