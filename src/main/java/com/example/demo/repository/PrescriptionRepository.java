package com.example.demo.repository;

import com.example.demo.model.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** Repository that is used for encapsulating storage, retrieval, and search behavior for {@link Prescription} entity.
 *
 */
@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {

}
