package com.example.demo.repository;

import com.example.demo.model.Injection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** Repository that is used for encapsulating storage, retrieval, and search behavior for {@link Injection} entity.
 *
 */
@Repository
public interface InjectionRepository extends JpaRepository<Injection, Long> {

}
