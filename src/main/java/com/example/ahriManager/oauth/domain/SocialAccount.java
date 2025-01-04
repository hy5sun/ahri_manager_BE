package com.example.ahriManager.oauth.domain;

import com.example.ahriManager.common.domain.BaseTimeEntity;
import com.example.ahriManager.common.type.Provider;
import com.example.ahriManager.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
public class SocialAccount extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Provider provider;

    @Column(nullable = false, unique = true)
    private String socialId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @Builder
    public SocialAccount(String provider, String socialId, Member member) {
        this.provider = Provider.fromType(provider);
        this.socialId = socialId;
        this.member = member;
    }
}
