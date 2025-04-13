package br.com.ccm.api.bugmonitor.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "backend_services")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BackendService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
}
