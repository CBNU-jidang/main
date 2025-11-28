package com.jidang.user;

import jakarta.validation.Valid;
import java.security.Principal;
import org.springframework.ui.Model; // 화면에 데이터 전달용
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import lombok.RequiredArgsConstructor;

import org.springframework.dao.DataIntegrityViolationException;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final UserDetailsService userSecurityService;

    @GetMapping("/signup")  //URL이 GET으로 요청되면 회원 가입을 위한 템플릿을 렌더링
    public String signup(UserCreateForm userCreateForm) {
        return "signup_form";
    }

    @PostMapping("/signup")  //POST로 요청되면 회원 가입을 진행
    public String signup(@Valid UserCreateForm userCreateForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "signup_form";
        }

        if (!userCreateForm.getPassword1().equals(userCreateForm.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordInCorrect",
                    "2개의 패스워드가 일치하지 않습니다.");
            return "signup_form";
        }

        //중복 알림 메세지 출력
        try {
            userService.create(userCreateForm.getUsername(),
                    userCreateForm.getEmail(), userCreateForm.getPassword1());
        }catch(DataIntegrityViolationException e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
            return "signup_form";
        }catch(Exception e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", e.getMessage());
            return "signup_form";
        }

        //회원가입 후 자동 로그인 + 메인페이지로 이동
        UserDetails userDetails = userSecurityService.loadUserByUsername(userCreateForm.getUsername());
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        return "redirect:/";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // 1. 마이페이지 화면 이동 (보유 칭호 목록 전달)
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/mypage")
    public String myPage(Model model, Principal principal) {
        SiteUser siteUser = this.userService.getUser(principal.getName());
        
        model.addAttribute("siteUser", siteUser); // 유저 정보(칭호 목록 포함) 전달
        return "mypage"; // mypage.html 템플릿 렌더링
    }

    // 2. 대표 칭호 변경 처리
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/title/update")
    public String updateTitle(@RequestParam("titleName") String titleName, Principal principal) {
        this.userService.updateRepresentativeTitle(principal.getName(), titleName);
        return "redirect:/user/mypage"; // 변경 후 다시 마이페이지로
    }
}
