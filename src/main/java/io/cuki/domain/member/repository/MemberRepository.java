package io.cuki.domain.member.repository;

import io.cuki.domain.member.entity.Email;
import io.cuki.domain.member.entity.Member;
import io.cuki.domain.member.entity.Nickname;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(Email email);
    Boolean existsByEmail(Email email);
    Boolean existsByNickname(Nickname nickname);
}
