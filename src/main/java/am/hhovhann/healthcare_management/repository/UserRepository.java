package am.hhovhann.healthcare_management.repository;

import am.hhovhann.healthcare_management.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Custom method to find a user by their username
    Optional<User> findByUsername(String username);
}
