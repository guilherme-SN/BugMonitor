package br.com.ccm.api.bugmonitor.service;

import br.com.ccm.api.bugmonitor.command.notion.outputs.attribute.NotionPage;
import br.com.ccm.api.bugmonitor.repository.BugRepository;
import br.com.ccm.api.bugmonitor.util.NotionPageExtractor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BugService {
    private final NotionPageExtractor notionPageExtractor;
    private final BugRepository bugRepository;

    public void saveFromNotionPage(NotionPage notionPage) {
        bugRepository.save(notionPageExtractor.extractBugFromNotionPage(notionPage));
    }
}
