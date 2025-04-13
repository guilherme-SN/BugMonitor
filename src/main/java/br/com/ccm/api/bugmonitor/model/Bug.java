package br.com.ccm.api.bugmonitor.model;

import br.com.ccm.api.bugmonitor.enums.EBugNotificationStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "bugs")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Bug {
    @Id
    private Long ccmId;

    private Integer priority;
    private String name;
    private String url;

    private String reportedBy;

    private String taskStatus;
    private String productStatus;
    private String qaStatus;
    private String backendStatus;
    private String frontendStatus;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "bug_epics",
            joinColumns = @JoinColumn(name = "bug_id"),
            inverseJoinColumns = @JoinColumn(name = "epic_id")
    )
    private Set<Epic> epics = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "bug_customers",
            joinColumns = @JoinColumn(name = "bug_id"),
            inverseJoinColumns = @JoinColumn(name = "customer_id")
    )
    private Set<Customer> impactedCustomers = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "bug_backend_responsibles",
            joinColumns = @JoinColumn(name = "bug_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> backendResponsibles = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "bug_frontend_responsibles",
            joinColumns = @JoinColumn(name = "bug_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> frontendResponsibles = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "bug_backend_services",
            joinColumns = @JoinColumn(name = "bug_id"),
            inverseJoinColumns = @JoinColumn(name = "backend_service_id")
    )
    private Set<BackendService> backendServices = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User createdBy;

    private LocalDateTime createdAt;
    private LocalDateTime lastEditedAt;
    private LocalDateTime completedAt;

    @Enumerated(EnumType.STRING)
    private EBugNotificationStatus notificationStatus;
}
