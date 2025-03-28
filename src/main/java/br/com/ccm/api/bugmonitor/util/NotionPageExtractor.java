package br.com.ccm.api.bugmonitor.util;

import br.com.ccm.api.bugmonitor.command.notion.outputs.attribute.NotionPage;
import br.com.ccm.api.bugmonitor.command.notion.outputs.attribute.NotionProperties;
import br.com.ccm.api.bugmonitor.command.notion.outputs.attribute.Responsible;
import br.com.ccm.api.bugmonitor.command.notion.outputs.attribute.Select;
import br.com.ccm.api.bugmonitor.enums.EResponsibleRole;
import br.com.ccm.api.bugmonitor.model.Bug;
import br.com.ccm.api.bugmonitor.model.Customer;
import br.com.ccm.api.bugmonitor.model.Epic;
import br.com.ccm.api.bugmonitor.model.User;
import br.com.ccm.api.bugmonitor.repository.CustomerRepository;
import br.com.ccm.api.bugmonitor.repository.EpicRepository;
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
    private final EpicRepository epicRepository;

    public Bug extractBugFromNotionPage(NotionPage notionPage) {
        return Bug.builder()
                .url(notionPage.url())
                .ccmId(extractCcmId(notionPage))
                .priority(extractPriority(notionPage))
                .name(extractName(notionPage))
                .reportedBy(extractReportedBy(notionPage))
                .epics(extractOrCreateEpics(notionPage))
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

    private Integer extractPriority(NotionPage notionPage) {
        String stringPriority = notionPage.properties().priority().select().name();

        try {
            return Integer.parseInt(stringPriority);
        } catch (NumberFormatException ex) {
            return switch (stringPriority) {
                case "Baixa" -> 2;
                case "Média" -> 5;
                case "Alta" -> 7;
                case "Urgente" -> 10;
                default -> 0;
            };
        }
    }

    private String extractName(NotionPage notionPage) {
        return notionPage.properties().taskName().titles().getFirst().text().content();
    }

    private String extractReportedBy(NotionPage notionPage) {
        return notionPage.properties().reportedBy().select().name();
    }

    private Set<Epic> extractOrCreateEpics(NotionPage notionPage) {
        Set<Epic> epics = new HashSet<>();
        List<Select> epicsName = notionPage.properties().epics().epics();

        for (Select epicName : epicsName) {
            Epic epic = epicRepository.findByName(epicName.name())
                    .orElseGet(() -> {
                        Epic newEpic = Epic.builder().name(epicName.name()).build();
                        return epicRepository.save(newEpic);
                    });

            epics.add(epic);
        }

        return epics;
    }

    private String extractTaskStatus(NotionPage notionPage) {
        return notionPage.properties().taskStatus().select().name();
    }

    private String extractImplementationStatusByRole(NotionPage notionPage, EResponsibleRole role) {
        NotionProperties notionProperties = notionPage.properties();
        String status = "";

        switch (role) {
            case QA -> status = notionProperties.qaStatus().select().name();
            case FRONTEND -> status = notionProperties.frontendStatus().select().name();
            case BACKEND -> status = notionProperties.backendStatus().select().name();
        }

        return status;
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
