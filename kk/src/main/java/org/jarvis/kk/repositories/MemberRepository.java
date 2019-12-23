package org.jarvis.kk.repositories;

import org.jarvis.kk.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * MemberRepository
 */
public interface MemberRepository extends JpaRepository<Member, String> {

}