package com.knubisoft.testapi.repository.postgresUsers;

import com.knubisoft.testapi.model.postgresUsers.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("users")
public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
