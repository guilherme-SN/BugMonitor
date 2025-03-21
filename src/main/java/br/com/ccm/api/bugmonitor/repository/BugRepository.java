package br.com.ccm.api.bugmonitor.repository;

import br.com.ccm.api.bugmonitor.model.Bug;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BugRepository extends JpaRepository<Bug, Long> {
}
