package vn.edu.iuh.authservice.repositories;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import vn.edu.iuh.authservice.models.User;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends CrudRepository<User, UUID> {
//   Optional<User> findByUsername(String username);

   @Query("select u from User u where upper(u.username) like upper(concat('%', ?1, '%'))")
   Optional<User> findByUsernameLikeIgnoreCase(String username);

    @Query("select u from User u where upper(u.phone) like upper(concat('%', ?1, '%'))")
   Optional<User> findByPhone(String phone);

    @Query("select u from User u where upper(u.email) like upper(concat('%', ?1, '%'))")
   Optional<User> findByEmail(String email);

   @Query("select (count(u) > 0) from User u where u.username = ?1")
   boolean existsByUsername(String username);

   @Query("select (count(u) > 0) from User u where u.email = ?1")
   boolean existsByEmail(@NotBlank(message = "Email is required") String email);

  Optional<User> findByUsername(String username);

    // Add this to UserRepository.java
    List<User> findByCreatedAtBetween(Timestamp from, Timestamp to);
}