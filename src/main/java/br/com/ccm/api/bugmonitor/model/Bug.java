package br.com.ccm.api.bugmonitor.model;

import br.com.ccm.api.bugmonitor.enums.EImplementationStatus;
import br.com.ccm.api.bugmonitor.enums.EReporter;
import br.com.ccm.api.bugmonitor.enums.ETaskStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "bugs")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Bug {
    @Id
    private Long ccmId;
    private Integer priority;
    private String name;
    private String url;

    @Enumerated(EnumType.STRING)
    private EReporter reportedBy;

    @Enumerated(EnumType.STRING)
    private ETaskStatus taskStatus;

    @Enumerated(EnumType.STRING)
    private EImplementationStatus qaStatus;

    @Enumerated(EnumType.STRING)
    private EImplementationStatus backendStatus;

    @Enumerated(EnumType.STRING)
    private EImplementationStatus frontendStatus;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "bug_customers",
            joinColumns = @JoinColumn(name = "bug_id"),
            inverseJoinColumns = @JoinColumn(name = "customer_id")
    )
    private Set<Customer> impactedCustomers = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "bug_backend_responsibles",
            joinColumns = @JoinColumn(name = "bug_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> backendResponsibles = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "bug_frontend_responsibles",
            joinColumns = @JoinColumn(name = "bug_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> frontendResponsibles = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User createdBy;

    private LocalDateTime createdAt;
    private LocalDateTime lastEditedAt;
}
