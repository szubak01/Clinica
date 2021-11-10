package com.example.demo.model;

import com.example.demo.model.enums.Gender;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Entity that represent patient.
 *
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Patient {

  /** Creates patient with specified first name, last name, date of birth, gender, city, phone number.
   * @param firstName Patient's first name.
   * @param lastName Patient's last name.
   * @param dayOfBirth Patient's date of birth.
   * @param gender Patient's gender.
   * @param city City that patient lives in.
   * @param phoneNumber Patient's phone number.
   */

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String firstName;

  private String lastName;

  private LocalDate dateOfBirth;

  private Gender gender;

  private String city;

  private String phoneNumber;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "patient")
  private List<Injection> patientInjectionList;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "patient")
  private List<Appointment> patientAppList;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "patient")
  private List<Prescription> patientPreList;

}
