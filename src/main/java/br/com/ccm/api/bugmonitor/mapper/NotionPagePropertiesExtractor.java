package br.com.ccm.api.bugmonitor.mapper;

import br.com.ccm.api.bugmonitor.command.notion.outputs.attribute.*;
import br.com.ccm.api.bugmonitor.enums.EResponsibleRole;
import br.com.ccm.api.bugmonitor.model.*;
import br.com.ccm.api.bugmonitor.model.BackendService;
import br.com.ccm.api.bugmonitor.model.Customer;
import br.com.ccm.api.bugmonitor.model.Epic;
import br.com.ccm.api.bugmonitor.repository.BackendServiceRepository;
import br.com.ccm.api.bugmonitor.repository.CustomerRepository;
import br.com.ccm.api.bugmonitor.repository.EpicRepository;
import br.com.ccm.api.bugmonitor.repository.UserRepository;
import br.com.ccm.api.bugmonitor.util.TimezoneConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class NotionPagePropertiesExtractor {
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final EpicRepository epicRepository;
    private final BackendServiceRepository backendServiceRepository;

    public Bug extractBugFromNotionPage(NotionPage notionPage) {
        return Bug.builder()
                .url(notionPage.url())
                .ccmId(extractCcmId(notionPage))
                .priority(extractPriority(notionPage))
                .name(extractName(notionPage))
                .reportedBy(extractReportedBy(notionPage))
                .epics(extractOrCreateEpics(notionPage))
                .taskStatus(extractTaskStatus(notionPage))
                .productStatus(extractImplementationStatusByRole(notionPage, EResponsibleRole.PRODUCT))
                .qaStatus(extractImplementationStatusByRole(notionPage, EResponsibleRole.QA))
                .backendStatus(extractImplementationStatusByRole(notionPage, EResponsibleRole.BACKEND))
                .frontendStatus(extractImplementationStatusByRole(notionPage, EResponsibleRole.FRONTEND))
                .impactedCustomers(extractOrCreateImpactedCustomers(notionPage))
                .backendResponsibles(extractOrCreateResponsibles(notionPage, EResponsibleRole.BACKEND))
                .frontendResponsibles(extractOrCreateResponsibles(notionPage, EResponsibleRole.FRONTEND))
                .backendServices(extractOrCreateBackendServices(notionPage))
                .createdBy(extractOrCreateCreatedBy(notionPage))
                .createdAt(extractCreatedAt(notionPage))
                .lastEditedAt(extractLastEditedAt(notionPage))
                .build();
    }

    public Long extractCcmId(NotionPage notionPage) {
        return Optional.ofNullable(notionPage.properties())
                .map(NotionProperties::pageId)
                .map(Id::uniqueId)
                .map(UniqueId::number)
                .orElse(null);
    }

    public String extractPriority(NotionPage notionPage) {
        String stringPriority = Optional.ofNullable(notionPage.properties())
                .map(NotionProperties::priority)
                .map(Priority::select)
                .map(Select::name)
                .orElse(null);

        if (stringPriority == null) return null;

        try {
            int intPriority = Integer.parseInt(stringPriority);

            if (intPriority <= 2) return "Muito Baixa";
            if (intPriority <= 4) return "Baixa";
            if (intPriority <= 6) return "Média";
            if (intPriority <= 8) return "Alta";
            return "Muito Alta";
        } catch (NumberFormatException ex) {
            return stringPriority.equalsIgnoreCase("Urgente") ? "Muito Alta" : stringPriority;
        }
    }

    public String extractName(NotionPage notionPage) {
        return Optional.ofNullable(notionPage.properties())
                .map(NotionProperties::taskName)
                .map(TaskName::titles)
                .filter(titles -> !titles.isEmpty())
                .map(titles -> titles.get(0))
                .map(Title::text)
                .map(Text::content)
                .orElse(null);
    }

    public String extractReportedBy(NotionPage notionPage) {
        return Optional.ofNullable(notionPage.properties())
                .map(NotionProperties::reportedBy)
                .map(ReportedBy::select)
                .map(Select::name)
                .orElse(null);
    }

    public Set<Epic> extractOrCreateEpics(NotionPage notionPage) {
        Set<Epic> epics = new HashSet<>();
        List<Select> epicsName = Optional.ofNullable(notionPage.properties())
                .map(NotionProperties::epics)
                .map(br.com.ccm.api.bugmonitor.command.notion.outputs.attribute.Epic::epics)
                .orElse(new ArrayList<>());

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

    public String extractTaskStatus(NotionPage notionPage) {
        return Optional.ofNullable(notionPage.properties())
                .map(NotionProperties::taskStatus)
                .map(TaskStatus::select)
                .map(Select::name)
                .orElse(null);
    }

    public String extractImplementationStatusByRole(NotionPage notionPage, EResponsibleRole role) {
        NotionProperties notionProperties = notionPage.properties();

        return switch (role) {
            case PRODUCT -> getStatusName(notionProperties.productStatus());
            case QA -> getStatusName(notionProperties.qaStatus());
            case FRONTEND -> getStatusName(notionProperties.frontendStatus());
            case BACKEND -> getStatusName(notionProperties.backendStatus());
        };
    }

    private String getStatusName(TaskStatus taskStatus) {
        return Optional.ofNullable(taskStatus)
                .map(TaskStatus::select)
                .map(Select::name)
                .orElse(null);
    }

    public Set<Customer> extractOrCreateImpactedCustomers(NotionPage notionPage) {
        Set<Customer> impactedCustomers = new HashSet<>();
        List<Select> customersName = Optional.ofNullable(notionPage.properties())
                .map(NotionProperties::customers)
                .map(br.com.ccm.api.bugmonitor.command.notion.outputs.attribute.Customer::impactedCustomers)
                .orElse(new ArrayList<>());

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

    public Set<User> extractOrCreateResponsibles(NotionPage notionPage, EResponsibleRole role) {
        List<Responsible> responsibles = role == EResponsibleRole.BACKEND
                ? notionPage.properties().backendResponsible().responsibles()
                : notionPage.properties().frontendResponsible().responsibles();

        return responsibles.stream()
                .map(this::findOrCreateUser)
                .collect(Collectors.toSet());
    }

    public User extractOrCreateCreatedBy(NotionPage notionPage) {
        Responsible responsible = Optional.ofNullable(notionPage.properties())
                .map(NotionProperties::createdBy)
                .map(CreatedBy::creator)
                .orElse(null);

        return responsible == null ? null : findOrCreateUser(responsible);
    }

    public User findOrCreateUser(Responsible responsible) {
        String uuid = responsible.id();
        String name = responsible.name();
        String email = responsible.person().email();

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

    public Set<BackendService> extractOrCreateBackendServices(NotionPage notionPage) {
        List<RichText> richTextList = Optional.ofNullable(notionPage.properties())
                .map(NotionProperties::backendService)
                .map(br.com.ccm.api.bugmonitor.command.notion.outputs.attribute.BackendService::richText)
                .orElse(new ArrayList<>());

        if (richTextList.isEmpty()) return new HashSet<>();

        String combinedText = richTextList.stream()
                .map(RichText::plainText)
                .flatMap(service -> Arrays.stream(service.split("\\n")))
                .filter(service -> !service.isBlank())
                .collect(Collectors.joining(","));

        return findOrCreateBackendServices(combinedText);
    }

    private Set<BackendService> findOrCreateBackendServices(String rawServiceList) {
        Set<BackendService> backendServices = new HashSet<>();

        String[] services = Arrays.stream(rawServiceList.split("\\s*(,|\\be\\b)\\s*"))
                .filter(service -> !service.isBlank())
                .toArray(String[]::new);

        for (String service : services) {
            BackendService backendService = backendServiceRepository.findByNameIgnoreCase(service)
                    .orElseGet(() -> {
                        BackendService newBackendService = BackendService.builder().name(service).build();
                        return backendServiceRepository.save(newBackendService);
                    });

            backendServices.add(backendService);
        }

        return backendServices;
    }

    public LocalDateTime extractCreatedAt(NotionPage notionPage) {
        LocalDateTime utcCreatedTime = Optional.ofNullable(notionPage.properties())
                .map(NotionProperties::createdTime)
                .map(CreatedTime::createdTime)
                .orElse(null);

        return utcCreatedTime == null ? null : TimezoneConverter.convertToSaoPauloTime(utcCreatedTime);
    }

    public LocalDateTime extractLastEditedAt(NotionPage notionPage) {
        LocalDateTime utcLastEditedTime = Optional.ofNullable(notionPage.properties())
                .map(NotionProperties::lastEditedAt)
                .map(LastEditedAt::lastEditedAt)
                .orElse(null);

        return utcLastEditedTime == null ? null : TimezoneConverter.convertToSaoPauloTime(utcLastEditedTime);
    }
}
