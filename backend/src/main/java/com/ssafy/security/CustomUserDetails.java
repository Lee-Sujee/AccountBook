package com.ssafy.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.ssafy.user.entity.User;

import lombok.Getter;

@Getter
public class CustomUserDetails implements UserDetails {

    private final String userId;     // PK
    private final String email;    // username 역할
    private final String password; // 비밀번호

    public CustomUserDetails(User user) {
        this.userId = user.getId();
        this.email = user.getEmail();
        this.password = user.getPassword();
    }

    // PK를 직접 꺼내기 위한 메서드
    public String getUserId() {
        return userId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 권한(ROLE) 기능을 나중에 추가할 수 있음
        return null;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    // Spring Security에서 username = 이메일로 사용된다.
    @Override
    public String getUsername() {
        return this.email;
    }

    // 계정 만료 여부 → true면 계정 사용 가능
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 계정 잠김 여부
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 비밀번호 만료 여부
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 계정 활성화 여부
    @Override
    public boolean isEnabled() {
        return true;
    }
}
