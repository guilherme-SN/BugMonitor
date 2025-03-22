package br.com.ccm.api.bugmonitor.repository;

import br.com.ccm.api.bugmonitor.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = """
            SELECT *
            FROM users
            WHERE uuid = :uuid
            """, nativeQuery = true)
    Optional<User> findByUuid(@Param(value = "uuid") String uuid);
}
