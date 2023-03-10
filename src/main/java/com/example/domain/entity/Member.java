package com.example.domain.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "email" })})
// 이따가 테이블에 유니크 제약조건 추가
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(length = 30, nullable = false)
    private String email;

    @Column(length = 15, nullable = false)
    private String nickname;

    @Column(length = 300, nullable = false)
    private String profile;

    @Builder
    public Member(String email, String nickname, String profile) {
        this.email = email;
        this.nickname = nickname;
        this.profile = profile;
    }

}
