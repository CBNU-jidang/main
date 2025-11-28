package com.jidang.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import java.util.Optional;
import com.jidang.DataNotFoundException;


@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SiteUser create(String username, String email, String password) {
        SiteUser user = new SiteUser();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        this.userRepository.save(user);
        return user;
    }

    //SiteUser를 조회
    public SiteUser getUser(String username) {
        Optional<SiteUser> siteUser = this.userRepository.findByusername(username);
        if (siteUser.isPresent()) {
            return siteUser.get();
        } else {
            throw new DataNotFoundException("siteuser not found");
        }
    }

    // [추가] 대표 칭호 변경 메서드
    @Transactional
    public void updateRepresentativeTitle(String username, String titleName) {
        SiteUser user = this.getUser(username);
        
        // 실제로 유저가 가진 칭호인지 검증 (보안상 필요)
        boolean hasTitle = user.getOwnedTitles().stream()
                .anyMatch(t -> t.getName().equals(titleName));

        if (hasTitle) {
            user.setRepresentativeTitle(titleName);
            this.userRepository.save(user);
        } else {
            throw new IllegalArgumentException("보유하지 않은 칭호입니다.");
        }
    }
}
