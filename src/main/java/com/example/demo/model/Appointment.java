package com.example.demo.model;

import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Entity that represents an appointment
 *
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Appointment {

  /** Creates appointment with specified date, time, doctor and patient.
   * @param date The date for which the appointment has been scheduled.
   * @param time The time for which the appointment has been scheduled.
   * @param doctor The doctor for whom an appointment was made.
   * @param patient The patient who has an appointment.
   */

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private LocalDate date;
  private String time;

  @ManyToOne
  @JoinColumn(name = "doctor", referencedColumnName = "id", nullable = false)
  private User doctor;

  @ManyToOne
  @JoinColumn(name = "patient", referencedColumnName = "id", nullable = false)
  private Patient patient;

  @ManyToOne
  @JoinColumn(name = "prescription", referencedColumnName = "id")
  private Prescription prescription;

  /** Returns doctor name
   * @return String representing doctor's first and last name
   */
  @Transient
  public String getDoctorName(){
    return doctor.getFirstName() + " " + doctor.getLastName();
  }

  /** Returns patient name
   * @return String representing patient's first and last name
   */
  @Transient
  public String getPatientName(){
    return patient.getFirstName() + " " + patient.getLastName();
  }

}
