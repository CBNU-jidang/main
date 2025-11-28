package com.jidang.user;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserTitleRepository extends JpaRepository<UserTitle, Long> {
    // 칭호 이름으로 조회 (Optional로 Null 방지)
    Optional<UserTitle> findByName(String name);
}