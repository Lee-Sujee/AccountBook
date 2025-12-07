package com.ssafy.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ssafy.user.entity.User;
import com.ssafy.user.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 🔥 Security가 기본적으로 사용하는 메서드 (이메일 로그인 용)
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.selectUserByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        return new CustomUserDetails(user);
    }

    // 🔥 우리가 JWT 인증에서 "id 기반"으로 사용할 메서드
    public UserDetails loadUserById(String userId) throws UsernameNotFoundException {
        User user = userRepository.selectUserById(userId);

        if (user == null) {
            throw new UsernameNotFoundException("User not found with id: " + userId);
        }

        return new CustomUserDetails(user);
    }
}
