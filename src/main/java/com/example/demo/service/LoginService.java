package com.example.demo.service;

import com.example.demo.infrastructure.LoginException;
import com.example.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Service class that provides methods which are responsible for saving, deleting, retrieving and filtering data obtained from database (User Entity).
 *
 */
@Service
public class LoginService {

  private final UserService userService;

  @Autowired
  public LoginService(
      UserService userService) {
    this.userService = userService;
  }

  /** This method handles logging into the system.
   * If provided login and password match, the user logs into the system.
   * Otherwise there is an LoginException thrown with a specific information what went wrong.
   * @param login String provided by the user to identify account assigned to him
   * @param password String provided by the user to check the compatibility with the login
   * @return User that successfully logged into the system.
   * @throws LoginException is thrown in 4 cases:
   * <ul>
   * <li>User has not provided login</li>
   * <li>User has not provided password</li>
   * <li>Login provided by user is not in the database</li>
   * <li>Password provided by the user does not match any login</li>
   * </ul>
   */
  public User loggingIntoSystem(String login, String password){

    if(login.isEmpty()){
      throw new LoginException("Please enter your login.");
    }

    if(password.isEmpty()){
      throw new LoginException("Please enter your password.");
    }

    User user = userService.getUserByLogin(login)
        .stream()
        .filter(user1 -> user1.getLogin().equals(login))
        .findFirst()
        .orElseThrow(() -> new LoginException("Provided login is invalid."));

    if(!password.equals(user.getPassword())){
      throw new LoginException("Provided password is invalid.");
    }
    return user;
  }
}
