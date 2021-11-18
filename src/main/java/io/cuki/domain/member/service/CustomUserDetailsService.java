package io.cuki.domain.member.service;

import io.cuki.domain.member.entity.Email;
import io.cuki.domain.member.entity.Member;
import io.cuki.domain.member.exception.DeactivatedMemberException;
import io.cuki.domain.member.exception.MemberNotFoundException;
import io.cuki.domain.member.repository.MemberRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    public CustomUserDetailsService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username){
        Email email = new Email(username);
        return memberRepository.findByEmail(email)
                .map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("가입되어 있지 않은 메일 주소입니다."));
    }

    private UserDetails createUserDetails(Member member) {
        if (!member.getActivated()) {
            throw new DeactivatedMemberException();
        }

        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(member.getAuthority().toString());

        return new User(
                String.valueOf(member.getId()),
                member.getPassword(),
                Collections.singleton(grantedAuthority));
    }
}
