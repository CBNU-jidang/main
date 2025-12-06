package com.jidang.Post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jidang.user.SiteUser;

public interface PostRepository extends JpaRepository<Post, Integer>, JpaSpecificationExecutor<Post>{
    Post findBySubject(String subject);
    Post findBySubjectAndContent(String subject, String content);
    // 1. 특정 유저가 작성한 게시글 수 (이미 있으면 생략 가능)
    long countByAuthor(SiteUser author);

    // 2. 특정 유저가 작성하고 + 특정 태그가 포함된 게시글 수 조회
    // 네이밍 규칙: Author(작성자) AND PostTags_Tag_Name(게시글태그->태그->이름)
    
    long countByAuthorAndPostTags_Tag_Name(SiteUser author, String tagName);

    // [추가 1] 인기스타용: 특정 유저의 글 중 '좋아요(liker)' 크기가 n개 이상인 글의 개수 조회
    @Query("SELECT COUNT(p) FROM Post p WHERE p.author = :author AND SIZE(p.liker) >= :likeCount")
    long countPostsByAuthorAndMinLikes(@Param("author") SiteUser author, @Param("likeCount") int likeCount);

    // [추가 2] 사진작가용: 특정 유저의 글 중 'filename'이 비어있지 않은(이미지 있는) 글 개수 조회
    // 메서드 이름만으로 JPA가 자동으로 쿼리를 만들어줍니다. (IsNotNull)
    long countByAuthorAndFilenameIsNotNull(SiteUser author);
}
