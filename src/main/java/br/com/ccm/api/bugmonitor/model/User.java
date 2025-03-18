package br.com.ccm.api.bugmonitor.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private UUID uuid;
    private String email;
    private String name;

    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL, orphanRemoval = false)
    private Set<Bug> createdBugs = new HashSet<>();

    @ManyToMany(mappedBy = "backendResponsibles", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private Set<Bug> backendResponsibleBugs = new HashSet<>();

    @ManyToMany(mappedBy = "frontendResponsibles", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private Set<Bug> frontendResponsibleBugs = new HashSet<>();
}
