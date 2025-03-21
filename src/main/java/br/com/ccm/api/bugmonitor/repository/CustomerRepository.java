package br.com.ccm.api.bugmonitor.repository;

import br.com.ccm.api.bugmonitor.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    @Query(value = """
            SELECT *
            FROM customers
            WHERE name = :name
            """, nativeQuery = true)
    Optional<Customer> findByName(@Param(value = "name") String name);
}
