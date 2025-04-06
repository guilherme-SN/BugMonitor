package br.com.ccm.api.bugmonitor.repository;

import br.com.ccm.api.bugmonitor.model.Bug;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface BugRepository extends JpaRepository<Bug, Long> {
    @Query(value = """
            SELECT ccm_id
            FROM bugs
            """, nativeQuery = true)
    Set<Long> findAllIds();

    @Query(value = """
            SELECT *
            FROM bugs
            WHERE notification_status = 'READY'
            """, nativeQuery = true)
    Set<Bug> findAllBugsReadyToBeNotified();
}
