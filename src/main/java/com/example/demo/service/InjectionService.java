package com.example.demo.service;

import com.example.demo.infrastructure.LoginException;
import com.example.demo.model.Injection;
import com.example.demo.model.enums.InjectionStatus;
import com.example.demo.repository.InjectionRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Service class that provides methods which are responsible for saving, deleting, retrieving and filtering data obtained from database (Injection Entity).
 *
 */
@Service
public class InjectionService {

  private final InjectionRepository injectionRepo;

  @Autowired
  public InjectionService(InjectionRepository injectionRepo) {
    this.injectionRepo = injectionRepo;
  }

  /** This method is used to retrieve all the injections that are to be made.
   * @return All the injections that their status is equal to PENDING.
   */
  public List<Injection> getAllPendingInjections(){
    return injectionRepo.findAll().stream()
        .filter(injection -> injection.getStatus() == InjectionStatus.PENDING)
        .collect(Collectors.toList());
  }

  /** This method is used to retrieve all the injections that are already made.
   * @return All the injections that their status is equal to DONE.
   */
  public List<Injection> getAllDoneInjections(){
    return injectionRepo.findAll().stream()
        .filter(injection -> injection.getStatus() == InjectionStatus.DONE)
        .collect(Collectors.toList());
  }

  /** This method saves passed object to the database.
   * @param injection Passed object to be saved into the database.
   * @throws LoginException Exception is thrown when passed object contains null.
   */
  public void save(Injection injection) {
    if (injection.getDescription() == null){
      throw new LoginException("Please enter description.");
    }
    if (injection.getDate() == null){
      throw new LoginException("Please pick a date.");
    }
    if (injection.getDoctor() == null){
      throw new LoginException("Please pick a doctor.");
    }
    if (injection.getPatient() == null){
      throw new LoginException("Please pick a patient.");
    }
    if (injection.getNurse() == null){
      throw new LoginException("Please pick a nurse.");
    }
    if (injection.getStatus() == null){
      throw new LoginException("Please select status.");
    }
    injectionRepo.save(injection);
  }
}
