package com.jidang.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.ManyToMany;
import java.util.Set;
import java.util.HashSet;

@Getter
@Setter
@Entity
public class SiteUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)//유일값만 넣을 수 있음
    private String username;

    private String password;

    @Column(unique = true)
    private String email;

    // [추가 1] 획득한 칭호 목록 (중복 방지를 위해 Set 사용)
    @ManyToMany
    private Set<UserTitle> ownedTitles = new HashSet<>();

    // [추가 2] 현재 닉네임 앞에 달고 있는 대표 칭호 (칭호 이름 저장)
    private String representativeTitle;
}