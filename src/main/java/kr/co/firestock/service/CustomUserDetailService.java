package kr.co.firestock.service;

import kr.co.firestock.repository.UserMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {
    private final UserMongoRepository userMongoRepository;

    @Override
    public UserDetails loadUserByUsername(String _id) throws UsernameNotFoundException {
        return userMongoRepository.findBy_id(_id)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
    }
}
