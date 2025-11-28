package com.jidang.Title; // [중요] 패키지명 확인

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// user 패키지의 클래스 import
import com.jidang.user.SiteUser;
import com.jidang.user.UserRepository;
import com.jidang.user.UserTitle;
import com.jidang.user.UserTitleRepository;

// Post 패키지의 리포지토리 import (게시글 수 확인용)
import com.jidang.Post.PostRepository;

@RequiredArgsConstructor
@Service
public class TitleService {

    private final UserTitleRepository userTitleRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    // 1. 칭호 부여 메서드 (없는 칭호면 만들어서 부여, 있으면 그냥 부여)
    @Transactional
    public void grantTitle(SiteUser user, String titleName, String desc) {
        // 칭호 정보 가져오기 (없으면 생성)
        UserTitle title = userTitleRepository.findByName(titleName)
                .orElseGet(() -> {
                    UserTitle newTitle = new UserTitle();
                    newTitle.setName(titleName);
                    newTitle.setDescription(desc);
                    return userTitleRepository.save(newTitle);
                });

        // 유저가 아직 이 칭호가 없다면 추가
        if (!user.getOwnedTitles().contains(title)) {
            user.getOwnedTitles().add(title);
            userRepository.save(user); 
        }
    }

    // 2. 게시글 수 체크 및 칭호 지급 로직
    @Transactional
    public void checkPostTitle(SiteUser user) {
        long count = postRepository.countByAuthor(user); // 작성 글 수 조회

        if (count >= 1) {
            grantTitle(user, "첫 걸음", "첫 게시글 작성 달성");
        }
        if (count >= 5) {
            grantTitle(user, "열정맨", "게시글 5개 작성 달성");
        }
        // 필요한 조건 계속 추가 가능
    }
}