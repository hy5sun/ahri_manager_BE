package com.example.ahriManager.member.repository;

import com.example.ahriManager.common.type.ProviderType;
import com.example.ahriManager.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MemberRepository extends JpaRepository<Member, UUID> {
    Optional<Member> findByEmailAndProvider(String email, ProviderType provider);
}
