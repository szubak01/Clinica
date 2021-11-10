package com.example.demo.model;

import com.example.demo.model.enums.InjectionStatus;
import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Entity that represents injection.
 *
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Injection {

  /** Creates an injection with specified description, date, status, doctor, nurse, patient.
   * @param description The description of the injection.
   * @param date The date for which the injection has been made.
   * @param status The status that is set for the injection.
   * @param doctor The doctor who ordered the injection.
   * @param nurse The nurse who will give the injection.
   * @param patient The patient who will receive the injection.
   */

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String description;

  private LocalDate date;

  private InjectionStatus status;

  @ManyToOne
  @JoinColumn(name = "doctor", referencedColumnName = "id", nullable = false)
  private User doctor;

  @ManyToOne
  @JoinColumn(name = "nurse", referencedColumnName = "id", nullable = false)
  private User nurse;

  @ManyToOne
  @JoinColumn(name = "patient", referencedColumnName = "id", nullable = false)
  private Patient patient;

  /**
   * @return String representing doctor's first and last name
   */
  @Transient
  public String getDoctorName(){
    return doctor.getFirstName() + " " + doctor.getLastName();
  }

  /**
   * @return String representing patient's first and last name
   */
  @Transient
  public String getPatientName(){
    return patient.getFirstName() + " " + patient.getLastName();
  }

  /**
   * @return String representing nurse's first and last name
   */
  @Transient
  public String getNurseName(){
    return nurse.getFirstName() + " " + nurse.getLastName();
  }
}
