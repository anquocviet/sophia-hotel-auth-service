package vn.edu.iuh.authservice.repositories;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.repository.CrudRepository;
import vn.edu.iuh.authservice.models.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends CrudRepository<User, UUID> {
   Optional<User> findByUsername(String username);

   Optional<User> findByPhone(String phone);

   Optional<User> findByEmail(String email);

   boolean existsByUsername(String username);

   boolean existsByEmail(@NotBlank(message = "Email is required") String email);
}