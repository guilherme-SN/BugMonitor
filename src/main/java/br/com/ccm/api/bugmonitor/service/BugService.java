package br.com.ccm.api.bugmonitor.service;

import br.com.ccm.api.bugmonitor.command.notion.outputs.attribute.NotionPage;
import br.com.ccm.api.bugmonitor.command.notion.outputs.attribute.People;
import br.com.ccm.api.bugmonitor.command.notion.outputs.attribute.Select;
import br.com.ccm.api.bugmonitor.enums.EImplementationStatus;
import br.com.ccm.api.bugmonitor.enums.EReporter;
import br.com.ccm.api.bugmonitor.enums.ETaskStatus;
import br.com.ccm.api.bugmonitor.model.Bug;
import br.com.ccm.api.bugmonitor.model.Customer;
import br.com.ccm.api.bugmonitor.model.User;
import br.com.ccm.api.bugmonitor.repository.BugRepository;
import br.com.ccm.api.bugmonitor.repository.CustomerRepository;
import br.com.ccm.api.bugmonitor.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BugService {
    private final BugRepository bugRepository;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;

    public void saveFromNotionPage(NotionPage notionPage) {
        Bug bug = Bug.builder()
                .url(notionPage.url())
                .ccmId(extractCcmId(notionPage))
                .name(extractName(notionPage))
                .reportedBy(extractReportedBy(notionPage))
                .taskStatus(extractTaskStatus(notionPage))
                .qaStatus(extractQaStatus(notionPage))
                .backendStatus(extractBackendStatus(notionPage))
                .frontendStatus(extractFrontendStatus(notionPage))
                .impactedCustomers(extractOrCreateImpactedCustomers(notionPage))
                .backendResponsibles(extractOrCreateBackendResponsibles(notionPage))
                .frontendResponsibles(extractOrCreateFrontendResponsibles(notionPage))
                .createdBy(extractOrCreateCreatedBy(notionPage))
                .createdAt(extractCreatedAt(notionPage))
                .lastEditedAt(extractLastEditedAt(notionPage))
                .build();

        bugRepository.save(bug);
    }

    private Long extractCcmId(NotionPage notionPage) {
        return notionPage.properties().id().uniqueId().number();
    }

    private String extractName(NotionPage notionPage) {
        return notionPage.properties().taskName().title().getFirst().text().content();
    }

    private EReporter extractReportedBy(NotionPage notionPage) {
        String reportedBy = notionPage.properties().reportedBy().select().name();

        return EReporter.fromString(reportedBy);
    }

    private ETaskStatus extractTaskStatus(NotionPage notionPage) {
        String taskStatus = notionPage.properties().taskStatus().select().name();

        return ETaskStatus.fromString(taskStatus);
    }

    private EImplementationStatus extractQaStatus(NotionPage notionPage) {
        String qaStatus = notionPage.properties().qaStatus().select().name();

        return EImplementationStatus.fromString(qaStatus);
    }

    private EImplementationStatus extractBackendStatus(NotionPage notionPage) {
        String backendStatus = notionPage.properties().backendStatus().select().name();

        return EImplementationStatus.fromString(backendStatus);
    }

    private EImplementationStatus extractFrontendStatus(NotionPage notionPage) {
        String frontendStatus = notionPage.properties().frontendStatus().select().name();

        return EImplementationStatus.fromString(frontendStatus);
    }

    private Set<Customer> extractOrCreateImpactedCustomers(NotionPage notionPage) {
        Set<Customer> impactedCustomers = new HashSet<>();
        List<Select> customersName = notionPage.properties().client().multiSelect();

        for (Select customerName : customersName) {
            Customer customer = customerRepository.findByName(customerName.name())
                    .orElseGet(() -> {
                        Customer newCustomer = Customer.builder().name(customerName.name()).build();
                        return customerRepository.save(newCustomer);
                    });

            impactedCustomers.add(customer);
        }

        return impactedCustomers;
    }

    private Set<User> extractOrCreateBackendResponsibles(NotionPage notionPage) {
        Set<User> backendResponsibleUsers = new HashSet<>();

        List<People> backendResponsiblePeople = notionPage.properties().backendResponsible().people();

        for (People responsible : backendResponsiblePeople) {
            User user = userRepository.findByUuid(responsible.id())
                    .orElseGet(() -> {
                        User newUser = User.builder()
                                .uuid(responsible.id())
                                .email(responsible.person().email())
                                .name(responsible.name())
                                .build();

                        return userRepository.save(newUser);
                    });

            backendResponsibleUsers.add(user);
        }

        return backendResponsibleUsers;
    }

    private Set<User> extractOrCreateFrontendResponsibles(NotionPage notionPage) {
        Set<User> frontendResponsibleUsers = new HashSet<>();

        List<People> frontendResponsiblePeople = notionPage.properties().frontendResponsible().people();

        for (People responsible : frontendResponsiblePeople) {
            User user = userRepository.findByUuid(responsible.id())
                    .orElseGet(() -> {
                        User newUser = User.builder()
                                .uuid(responsible.id())
                                .email(responsible.person().email())
                                .name(responsible.name())
                                .build();

                        return userRepository.save(newUser);
                    });

            frontendResponsibleUsers.add(user);
        }

        return frontendResponsibleUsers;
    }

    private User extractOrCreateCreatedBy(NotionPage notionPage) {
        return userRepository.findByEmail(notionPage.properties().createdBy().user().person().email())
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .uuid(notionPage.properties().createdBy().user().id())
                            .email(notionPage.properties().createdBy().user().person().email())
                            .name(notionPage.properties().createdBy().user().name())
                            .build();

                    return userRepository.save(newUser);
                });
    }

    private LocalDateTime extractCreatedAt(NotionPage notionPage) {
        return notionPage.properties().createdTime().createdTime();
    }

    private LocalDateTime extractLastEditedAt(NotionPage notionPage) {
        return notionPage.properties().lastEditedAt().lastEditedAt();
    }
}
