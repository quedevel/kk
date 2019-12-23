package org.jarvis.kk.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * CommunityCrawlingRepositoryTests
 */
@SpringBootTest
public class CommunityCrawlingRepositoryTests {

    @Autowired
    private CommunityCrawlingRepository communityCrawlingRepository;

    @Test
    public void findPrevCrawling(){
        // communityCrawlingRepository.findPrevCrawling();
    }
}