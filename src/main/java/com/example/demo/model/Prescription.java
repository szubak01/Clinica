package com.example.demo.model;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Entity that represents prescription.
 *
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Prescription {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String description;
  private LocalDate date;

  @ManyToOne
  @JoinColumn(name = "doctor", referencedColumnName = "id", nullable = false)
  private User doctor;

  @ManyToOne
  @JoinColumn(name = "patient", referencedColumnName = "id", nullable = false)
  private Patient patient;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "prescription")
  private List<Appointment> appointmentList;

}
