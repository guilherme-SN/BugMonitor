package br.com.ccm.api.bugmonitor.repository;

import br.com.ccm.api.bugmonitor.model.Epic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EpicRepository extends JpaRepository<Epic, Long> {
    @Query(value = """
            SELECT *
            FROM epics
            WHERE name = :name
            """, nativeQuery = true)
    Optional<Epic> findByName(@Param(value = "name") String name);
}
