package deepstream.ttrack.repository;

import deepstream.ttrack.entity.User;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{


    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    @Query(nativeQuery = true, value = "SELECT * FROM users WHERE role_id = :roleId")
    Optional<User> findUserHasRoleId(@Param("roleId") int roleId);

    Optional<User> getUserByUsername(String username);

    Optional<User> findByUsername(String username);
}
