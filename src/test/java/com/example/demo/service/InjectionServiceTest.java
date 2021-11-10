package com.example.demo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.example.demo.infrastructure.LoginException;
import com.example.demo.model.Injection;
import com.example.demo.model.Patient;
import com.example.demo.model.User;
import com.example.demo.model.enums.Gender;
import com.example.demo.model.enums.InjectionStatus;
import com.example.demo.model.enums.Role;
import com.example.demo.repository.InjectionRepository;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class InjectionServiceTest {

  @Mock private InjectionRepository injectionRepo;
  @Captor private ArgumentCaptor<Injection> captor;
  private InjectionService underTest;
  private Injection injection;

  @BeforeEach
  void setUp() {
    underTest = new InjectionService(injectionRepo);

    injection = new Injection();
    injection.setStatus(InjectionStatus.PENDING);
    injection.setDate(LocalDate.now());
    injection.setDescription("Description");
    injection.setDoctor(new User("Thomas", "Hunt", "ThomasHunt", "password", Gender.MALE, Role.DOCTOR));
    injection.setNurse(new User("Grace", "Scott", "GraceScott", "password", Gender.FEMALE, Role.NURSE));
    injection.setPatient(new Patient("John", "Doe", LocalDate.now(), Gender.MALE, "New York", "123123123"));
  }

  @Test
  void canGetAll() {
    //when
    underTest.getAllPendingInjections();
    underTest.getAllDoneInjections();
    //then
    verify(injectionRepo, times(2)).findAll();
  }

  @Test
  void canSaveInjection(){
    //when
    underTest.save(injection);

    //then
    verify(injectionRepo).save(captor.capture());
    assertThat(captor.getValue()).isEqualTo(injection);
  }

  @Test
  public void shouldThrowExceptionIfDescriptionIsNull() {
    injection.setDescription(null);

    assertThatThrownBy(() -> underTest.save(injection))
        .isInstanceOf(LoginException.class)
        .hasMessageContaining("Please enter description.");
  }

  @Test
  public void shouldThrowExceptionIfStatusIsNull() {
    injection.setStatus(null);

    assertThatThrownBy(() -> underTest.save(injection))
        .isInstanceOf(LoginException.class)
        .hasMessageContaining("Please select status.");
  }

  @Test
  public void shouldThrowExceptionIfDateIsNull() {
    injection.setDate(null);

    assertThatThrownBy(() -> underTest.save(injection))
        .isInstanceOf(LoginException.class)
        .hasMessageContaining("Please pick a date.");
  }

  @Test
  public void shouldThrowExceptionIfDoctorIsNull() {
    injection.setDoctor(null);

    assertThatThrownBy(() -> underTest.save(injection))
        .isInstanceOf(LoginException.class)
        .hasMessageContaining("Please pick a doctor.");
  }

  @Test
  public void shouldThrowExceptionIfNurseIsNull() {
    injection.setNurse(null);

    assertThatThrownBy(() -> underTest.save(injection))
        .isInstanceOf(LoginException.class)
        .hasMessageContaining("Please pick a nurse.");
  }

  @Test
  public void shouldThrowExceptionIfPatientIsNull() {
    injection.setPatient(null);

    assertThatThrownBy(() -> underTest.save(injection))
        .isInstanceOf(LoginException.class)
        .hasMessageContaining("Please pick a patient.");
  }

}