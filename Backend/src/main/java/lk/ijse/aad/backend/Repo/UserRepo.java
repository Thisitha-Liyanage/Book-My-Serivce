package lk.ijse.aad.backend.Repo;

import lk.ijse.aad.backend.Entity.Role;
import lk.ijse.aad.backend.Entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
@Repository
public interface UserRepo extends JpaRepository<User, Integer> {
    Optional<User>findByEmail(String email);
    long countByRole(Role role);
    void deleteUserByEmail(String email);
    List<User> findAllByRole(Role role);
}
