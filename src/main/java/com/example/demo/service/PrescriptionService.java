package com.example.demo.service;

import com.example.demo.infrastructure.LoginException;
import com.example.demo.model.Prescription;
import com.example.demo.repository.PrescriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Service class that provides methods which are responsible for saving, deleting, retrieving and filtering data obtained from database (Prescription Entity).
 *
 */
@Service
public class PrescriptionService {

  private final PrescriptionRepository prescriptionRepo;

  @Autowired
  public PrescriptionService(PrescriptionRepository prescriptionRepo){
    this.prescriptionRepo = prescriptionRepo;
  }

  /** This method saves passed object to the database.
   * @param prescription Passed object to be saved into the database.
   * @throws LoginException Exception is thrown when passed object contains null.
   */
  public void save(Prescription prescription) {

    if (prescription.getDate() == null){
      throw new LoginException("Please select date.");
    }
    if (prescription.getDescription() == null){
      throw new LoginException("Please write a description.");
    }
    if (prescription.getDoctor() == null){
      throw new LoginException("Please pick a doctor.");
    }
    if (prescription.getPatient() == null){
      throw new LoginException("Please pick a patient.");
    }
    prescriptionRepo.save(prescription);
  }

}
