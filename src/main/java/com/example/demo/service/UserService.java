package com.example.demo.service;

import com.example.demo.infrastructure.LoginException;
import com.example.demo.model.User;
import com.example.demo.model.enums.Role;
import com.example.demo.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Service class that provides methods which are responsible for saving, deleting, retrieving and filtering data obtained from database (User Entity).
 *
 */
@Service
public class UserService {

  private final UserRepository userRepository;

  @Autowired
  public UserService(UserRepository userRepository){
    this.userRepository = userRepository;
  }


  /** This method is used to get specified user by login.
   * @param login Is provided by user to log into the system.
   * @return User that is matched by provided login.
   */
  public Optional<User> getUserByLogin(String login){

    return userRepository.findAll()
        .stream()
        .filter(user -> user.getLogin().equals(login))
        .findFirst();
  }

  /** This method is used to retrieve all the users from database.
   * @return All users stored in database.
   */
  public List<User> getAllUsers() {
    return userRepository.findAll();
  }

  /** This method is used to retrieve all the doctors from database.
   * @return All users with role that is equal to DOCTOR
   */
  public List<User> getAllDoctors(){
    return userRepository.findAll()
        .stream()
        .filter(user -> user.getRole() == Role.DOCTOR)
        .collect(Collectors.toList());
    }

  /** This method is used to retrieve all the nurses from database.
   * @return All users with role that is equal to NURSE
   */
  public List<User> getAllNurses() {
    return userRepository.findAll()
        .stream()
        .filter(user -> user.getRole() == Role.NURSE)
        .collect(Collectors.toList());
  }

  /** Deletes object from database by indicated id.
   * @param id Indicates which object will be removed
   */
  public void deleteUserById(Long id){
    userRepository.deleteById(id);
  }

  /** This method saves passed object to the database.
   * @param user Passed object to be saved into the database.
   * @throws LoginException Exception is thrown when passed object contains null.
   */
  public void save(User user) {

    if (user.getFirstName() == null){
      throw new LoginException("Please enter first name.");
    }
    if (user.getLastName() == null){
      throw new LoginException("Please enter last name.");
    }
    if (user.getLogin() == null){
      throw new LoginException("Please enter login.");
    }
    if (user.getPassword() == null){
      throw new LoginException("Please enter password.");
    }
    if (user.getGender() == null){
      throw new LoginException("Please select gender.");
    }
    if (user.getRole() == null){
      throw new LoginException("Please select role.");
    }
    userRepository.save(user);
  }

  public UserRepository getUserRepository() {
    return userRepository;
  }


}
