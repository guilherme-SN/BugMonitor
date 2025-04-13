package br.com.ccm.api.bugmonitor.repository;

import br.com.ccm.api.bugmonitor.model.BackendService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BackendServiceRepository extends JpaRepository<BackendService, Long> {
    @Query(value = """
            SELECT *
            FROM backend_services
            WHERE name = :name
            """, nativeQuery = true)
    Optional<BackendService> findByNameIgnoreCase(@Param(value = "name") String name);
}
