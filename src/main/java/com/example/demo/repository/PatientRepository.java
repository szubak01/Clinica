package com.example.demo.repository;

import com.example.demo.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** Repository that is used for encapsulating storage, retrieval, and search behavior for {@link Patient} entity.
 *
 */
@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

}
