package com.example.demo.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
class LoginServiceTest {

  @Mock private UserRepository userRepository;
  @Mock private UserService userService;
  @Captor private ArgumentCaptor<String> captor;
  private LoginService underTest;
  private final User user = new User("Grace", "Scott", "userLogin", "userPass", Gender.FEMALE, Role.NURSE);

  @BeforeEach
  void setUp(){
    underTest = new LoginService(userService);

    User user = new User("Grace", "Scott", "userLogin", "userPass", Gender.FEMALE, Role.NURSE);
  }

  @Test
  public void shouldThrowExceptionIfLoginIsEmpty() {
    String login = "";
    String password = "passwordTest";

    assertThatThrownBy(() -> underTest.loggingIntoSystem(login, password))
        .isInstanceOf(LoginException.class)
        .hasMessageContaining("Please enter your login.");
  }

  @Test
  public void shouldThrowExceptionIfPasswordIsEmpty() {
    String login = "loginTest";
    String password = "";

    assertThatThrownBy(() -> underTest.loggingIntoSystem(login, password))
        .isInstanceOf(LoginException.class)
        .hasMessageContaining("Please enter your password.");
  }

  @Test
  public void shouldThrowExceptionIfLoginDoesNotMatch() {
    String login = "";
    String password = "passwordTest";

    assertThatThrownBy(() -> underTest.loggingIntoSystem(login, password))
        .isInstanceOf(LoginException.class)
        .hasMessageContaining("Please enter your login.");
  }
}