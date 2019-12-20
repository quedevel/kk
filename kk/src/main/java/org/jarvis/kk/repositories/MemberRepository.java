package org.jarvis.kk.repositories;

import java.util.Optional;

import org.jarvis.kk.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * MemberRepository
 */
public interface MemberRepository extends JpaRepository<Member, String> {

    Optional<Member> findByMid(String mid);
}