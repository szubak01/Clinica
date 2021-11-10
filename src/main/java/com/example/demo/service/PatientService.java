package com.example.demo.service;

import com.example.demo.infrastructure.LoginException;
import com.example.demo.model.Patient;
import com.example.demo.repository.PatientRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Service class that provides methods which are responsible for saving, deleting, retrieving and filtering data obtained from database (Patient Entity).
 *
 */
@Service
@RequiredArgsConstructor
public class PatientService {

  private final PatientRepository patientRepo;

  /** This method is used to retrieve all the patients from database.
   * @return All the patients that are stored in database.
   */
  public List<Patient> getAllPatients(){
    return patientRepo.findAll();
  }

  /** Deletes object from database by indicated id.
   * @param id Indicates which object will be removed
   */
  public void deletePatientById(Long id) {
    patientRepo.deleteById(id);
  }

  /** This method saves passed object to the database.
   * @param patient Passed object to be saved into the database.
   * @throws LoginException Exception is thrown when passed object contains null.
   */
  public void save(Patient patient) {

    if (patient.getFirstName() == null){
      throw new LoginException("Please enter first name.");
    }
    if (patient.getLastName() == null){
      throw new LoginException("Please enter last name.");
    }
    if (patient.getDateOfBirth() == null){
      throw new LoginException("Please pick a date.");
    }
    if (patient.getCity() == null){
      throw new LoginException("Please enter city.");
    }
    if (patient.getGender() == null){
      throw new LoginException("Please select gender.");
    }
    if (patient.getPhoneNumber() == null){
      throw new LoginException("Please enter phone number.");
    }
    patientRepo.save(patient);
  }

}
