package com.example.demo.repository;

import com.example.demo.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/** Repository that is used for encapsulating storage, retrieval, and search behavior for {@link Appointment} entity.
 *
 */
@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

}
