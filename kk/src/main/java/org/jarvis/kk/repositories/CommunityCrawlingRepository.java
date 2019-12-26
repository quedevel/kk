package org.jarvis.kk.repositories;

import org.jarvis.kk.domain.CommunityCrawling;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * CommunotyCrawlingRepository
 */
public interface CommunityCrawlingRepository extends JpaRepository<CommunityCrawling, Integer>{

    // @Query("select c from CommunityCrawling c where c.lastCrawling = true order by c desc limit 1")
    // public List<CommunityCrawling> findPrevCrawling();
}