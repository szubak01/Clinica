package com.example.demo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;

import com.example.demo.infrastructure.LoginException;
import com.example.demo.model.Patient;
import com.example.demo.model.Prescription;
import com.example.demo.model.User;
import com.example.demo.model.enums.Gender;
import com.example.demo.model.enums.Role;
import com.example.demo.repository.PrescriptionRepository;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PrescriptionServiceTest {

  @Mock private PrescriptionRepository prescriptionRepo;
  @Captor private ArgumentCaptor<Prescription> captor;
  private PrescriptionService underTest;
  private Prescription prescription;

  @BeforeEach
  void setUp(){
    underTest = new PrescriptionService(prescriptionRepo);

    prescription = new Prescription();
    prescription.setDate(LocalDate.now());
    prescription.setDescription("Description");
    prescription.setDoctor(new User("Thomas", "Hunt", "ThomasHunt", "password", Gender.MALE, Role.DOCTOR));
    prescription.setPatient(new Patient("John", "Doe", LocalDate.now(), Gender.MALE, "New York", "123123123"));
  }

  @Test
  void canSavePrescription() {
    //when
    underTest.save(prescription);

    //then
    verify(prescriptionRepo).save(captor.capture());
    assertThat(captor.getValue()).isEqualTo(prescription);
  }

  @Test
  public void shouldThrowExceptionIfDescriptionIsNull() {
    prescription.setDescription(null);

    assertThatThrownBy(() -> underTest.save(prescription))
        .isInstanceOf(LoginException.class)
        .hasMessageContaining("Please write a description.");
  }

  @Test
  public void shouldThrowExceptionIfStatIsNull() {
    prescription.setPatient(null);

    assertThatThrownBy(() -> underTest.save(prescription))
        .isInstanceOf(LoginException.class)
        .hasMessageContaining("Please pick a patient.");
  }

  @Test
  public void shouldThrowExceptionIfDateIsNull() {
    prescription.setDate(null);

    assertThatThrownBy(() -> underTest.save(prescription))
        .isInstanceOf(LoginException.class)
        .hasMessageContaining("Please select date.");
  }

  @Test
  public void shouldThrowExceptionIfDoctorIsNull() {
    prescription.setDoctor(null);

    assertThatThrownBy(() -> underTest.save(prescription))
        .isInstanceOf(LoginException.class)
        .hasMessageContaining("Please pick a doctor.");
  }


}