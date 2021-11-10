package com.example.demo.model;


import com.example.demo.model.enums.Gender;
import com.example.demo.model.enums.Role;
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


/** Entity that represents user.
 * User has 4 roles: Admin, Doctor, Nurse, Receptionist.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String firstName;

  private String lastName;

  private String login;

  private String password;

  private Gender gender;

  private Role role;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "doctor")
  private List<Injection> doctorInjectionList;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "nurse")
  private List<Injection> nurseInjectionList;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "doctor")
  private List<Appointment> doctorAppList;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "doctor")
  private List<Prescription> doctorPreList;

}
