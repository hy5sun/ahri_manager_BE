package com.example.ahriManager.member.domain;

import com.example.ahriManager.common.domain.BaseTimeEntity;
import com.example.ahriManager.common.type.ProviderType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private ProviderType provider;

    @Column(nullable = false)
    private Boolean emailAgreement;

    @Builder
    public Member(String nickname, String email, ProviderType provider) {
        this.nickname = nickname;
        this.email = email;
        this.provider = provider;
        this.emailAgreement = true;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }
}
