package com.example.demo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.example.demo.infrastructure.LoginException;
import com.example.demo.model.User;
import com.example.demo.model.enums.Gender;
import com.example.demo.model.enums.Role;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock private UserRepository userRepo;
  @Captor private ArgumentCaptor<User> captor;
  private UserService underTest;
  private User user;

  @BeforeEach
  void setUp() {
    underTest = new UserService(userRepo);

    user = new User();
    user.setFirstName("Firstname");
    user.setLastName("Lastname");
    user.setLogin("Login");
    user.setPassword("Password");
    user.setGender(Gender.MALE);
    user.setRole(Role.DOCTOR);
  }

  @Test
  void canGetAllUsers() {
    //when
    underTest.getAllUsers();
    underTest.getAllDoctors();
    underTest.getAllNurses();
    underTest.getUserByLogin("login");

    //then
    verify(userRepo, times(4)).findAll();
  }

  @Test
  void canDeleteUserById() {
    //when
    underTest.deleteUserById(1L);
    //then
    verify(userRepo).deleteById(1L);
  }

  @Test
  void canSaveUser() {
    //when
    underTest.save(user);

    //then
    verify(userRepo).save(captor.capture());
    assertThat(captor.getValue()).isEqualTo(user);
  }

  @Test
  public void shouldThrowExceptionIfFirsNameIsNull() {
    user.setFirstName(null);

    assertThatThrownBy(() -> underTest.save(user))
        .isInstanceOf(LoginException.class)
        .hasMessageContaining("Please enter first name.");
  }

  @Test
  public void shouldThrowExceptionIfLastNameIsNull() {
    user.setLastName(null);

    assertThatThrownBy(() -> underTest.save(user))
        .isInstanceOf(LoginException.class)
        .hasMessageContaining("Please enter last name.");
  }

  @Test
  public void shouldThrowExceptionIfLoginIsNull() {
    user.setLogin(null);

    assertThatThrownBy(() -> underTest.save(user))
        .isInstanceOf(LoginException.class)
        .hasMessageContaining("Please enter login.");
  }

  @Test
  public void shouldThrowExceptionIfPasswordIsNull() {
    user.setPassword(null);

    assertThatThrownBy(() -> underTest.save(user))
        .isInstanceOf(LoginException.class)
        .hasMessageContaining("Please enter password.");
  }

  @Test
  public void shouldThrowExceptionIfGenderIsNull() {
    user.setGender(null);

    assertThatThrownBy(() -> underTest.save(user))
        .isInstanceOf(LoginException.class)
        .hasMessageContaining("Please select gender.");
  }

  @Test
  public void shouldThrowExceptionIfRoleIsNull() {
    user.setRole(null);

    assertThatThrownBy(() -> underTest.save(user))
        .isInstanceOf(LoginException.class)
        .hasMessageContaining("Please select role.");
  }
}