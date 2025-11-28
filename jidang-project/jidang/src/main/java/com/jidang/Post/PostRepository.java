package com.jidang.Post;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.jidang.user.SiteUser; // SiteUser import 필요

public interface PostRepository extends JpaRepository<Post, Integer>, JpaSpecificationExecutor<Post>{
    Post findBySubject(String subject);
    Post findBySubjectAndContent(String subject, String content);

    // [추가] 특정 유저가 작성한 글의 개수 반환
    long countByAuthor(SiteUser author);
}
