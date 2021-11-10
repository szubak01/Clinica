package com.example.demo.repository;

import com.example.demo.model.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/** Repository that is used for encapsulating storage, retrieval, and search behavior for {@link User} entity.
 *
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
