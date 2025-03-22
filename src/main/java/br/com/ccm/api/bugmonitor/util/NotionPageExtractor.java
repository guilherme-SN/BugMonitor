package br.com.ccm.api.bugmonitor.util;

import br.com.ccm.api.bugmonitor.command.notion.outputs.attribute.NotionPage;
import br.com.ccm.api.bugmonitor.command.notion.outputs.attribute.NotionProperties;
import br.com.ccm.api.bugmonitor.command.notion.outputs.attribute.Responsible;
import br.com.ccm.api.bugmonitor.command.notion.outputs.attribute.Select;
import br.com.ccm.api.bugmonitor.enums.EImplementationStatus;
import br.com.ccm.api.bugmonitor.enums.EReporter;
import br.com.ccm.api.bugmonitor.enums.EResponsibleRole;
import br.com.ccm.api.bugmonitor.enums.ETaskStatus;
import br.com.ccm.api.bugmonitor.model.Bug;
import br.com.ccm.api.bugmonitor.model.Customer;
import br.com.ccm.api.bugmonitor.model.User;
import br.com.ccm.api.bugmonitor.repository.CustomerRepository;
import br.com.ccm.api.bugmonitor.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class NotionPageExtractor {
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;

    public Bug extractBugFromNotionPage(NotionPage notionPage) {
        return Bug.builder()
                .url(notionPage.url())
                .ccmId(extractCcmId(notionPage))
                .name(extractName(notionPage))
                .reportedBy(extractReportedBy(notionPage))
                .taskStatus(extractTaskStatus(notionPage))
                .qaStatus(extractImplementationStatusByRole(notionPage, EResponsibleRole.QA))
                .backendStatus(extractImplementationStatusByRole(notionPage, EResponsibleRole.BACKEND))
                .frontendStatus(extractImplementationStatusByRole(notionPage, EResponsibleRole.FRONTEND))
                .impactedCustomers(extractOrCreateImpactedCustomers(notionPage))
                .backendResponsibles(extractOrCreateResponsibles(notionPage, EResponsibleRole.BACKEND))
                .frontendResponsibles(extractOrCreateResponsibles(notionPage, EResponsibleRole.FRONTEND))
                .createdBy(extractOrCreateCreatedBy(notionPage))
                .createdAt(extractCreatedAt(notionPage))
                .lastEditedAt(extractLastEditedAt(notionPage))
                .build();
    }

    private Long extractCcmId(NotionPage notionPage) {
        return notionPage.properties().pageId().uniqueId().number();
    }

    private String extractName(NotionPage notionPage) {
        return notionPage.properties().taskName().titles().getFirst().text().content();
    }

    private EReporter extractReportedBy(NotionPage notionPage) {
        String reportedBy = notionPage.properties().reportedBy().select().name();

        return EReporter.fromString(reportedBy);
    }

    private ETaskStatus extractTaskStatus(NotionPage notionPage) {
        String taskStatus = notionPage.properties().taskStatus().select().name();

        return ETaskStatus.fromString(taskStatus);
    }

    private EImplementationStatus extractImplementationStatusByRole(NotionPage notionPage, EResponsibleRole role) {
        NotionProperties notionProperties = notionPage.properties();
        String status = "";

        switch (role) {
            case QA -> status = notionProperties.qaStatus().select().name();
            case FRONTEND -> status = notionProperties.frontendStatus().select().name();
            case BACKEND -> status = notionProperties.backendStatus().select().name();
        }

        return EImplementationStatus.fromString(status);
    }

    private Set<Customer> extractOrCreateImpactedCustomers(NotionPage notionPage) {
        Set<Customer> impactedCustomers = new HashSet<>();
        List<Select> customersName = notionPage.properties().customers().impactedCustomers();

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

    private Set<User> extractOrCreateResponsibles(NotionPage notionPage, EResponsibleRole role) {
        List<Responsible> responsibles = role.equals(EResponsibleRole.BACKEND)
                ? notionPage.properties().backendResponsible().responsibles()
                : notionPage.properties().frontendResponsible().responsibles();

        return responsibles.stream()
                .map(responsible ->
                        findOrCreateUser(responsible.id(), responsible.person().email(), responsible.name()))
                .collect(Collectors.toSet());
    }

    private User extractOrCreateCreatedBy(NotionPage notionPage) {
        return findOrCreateUser(
                notionPage.properties().createdBy().creator().id(),
                notionPage.properties().createdBy().creator().person().email(),
                notionPage.properties().createdBy().creator().name()
        );
    }

    private User findOrCreateUser(String uuid, String email, String name) {
        return userRepository.findByUuid(uuid)
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .uuid(uuid)
                            .email(email)
                            .name(name)
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
