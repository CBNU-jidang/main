package com.jidang;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.jidang.Post.PostService;
import com.jidang.Title.TitleService;
import com.jidang.user.SiteUser;
import com.jidang.user.UserRepository;
import com.jidang.user.UserService;

@SpringBootTest
class TitleServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;
    
    @Autowired
    private UserRepository userRepository;

    @Test
    @Transactional // 테스트 후 데이터 롤백 (DB에 흔적 안 남김)
    void 칭호획득_테스트() throws Exception {
        // 1. 임시 사용자 생성
        SiteUser user = userService.create("testuser", "test@test.com", "1234");
        
        // 2. 게시글 1개 작성 (파일은 null로 처리)
        // create 메서드 파라미터: 제목, 내용, 유저, 태그목록, 파일
        postService.create("테스트 제목", "테스트 내용", user, null, null);

        // 3. 검증: 유저 정보를 다시 조회해서 칭호가 있는지 확인
        // (영속성 컨텍스트 갱신을 위해 다시 조회 권장)
        SiteUser targetUser = userRepository.findByusername("testuser").get();
        
        System.out.println("========================================");
        System.out.println("보유 칭호 개수: " + targetUser.getOwnedTitles().size());
        targetUser.getOwnedTitles().forEach(title -> 
            System.out.println("획득한 칭호: " + title.getName())
        );
        System.out.println("========================================");

        // '첫 걸음'이라는 칭호가 포함되어 있어야 성공
        boolean hasTitle = targetUser.getOwnedTitles().stream()
                .anyMatch(t -> t.getName().equals("첫 걸음"));
        
        assertTrue(hasTitle, "칭호가 정상적으로 부여되지 않았음.");
    }
}