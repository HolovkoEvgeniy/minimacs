package com.lumiring.minimacs.services.user;


import com.lumiring.minimacs.dao.user.UserRepository;
import com.lumiring.minimacs.domain.constant.Code;
import com.lumiring.minimacs.domain.response.exception.CommonException;
import com.lumiring.minimacs.entity.user.UserEntity;
import com.lumiring.minimacs.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailService implements UserDetailsService {

    private final UserRepository userDao;
//    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String cacheKey = "user:" + username;

//        UserEntity cachedUser = (UserEntity) redisTemplate.opsForValue().get(cacheKey);

//        if (cachedUser != null) {
//            return new UserDetailsImpl(cachedUser);
//        }


        UserEntity user = userDao.getByUsernameIgnoreCase(username)
                .orElseThrow(() -> CommonException.builder().code(Code.USER_NOT_FOUND)
                        .userMessage(String.format("Username %s not found", username)).httpStatus(HttpStatus.NOT_FOUND).build());

//        redisTemplate.opsForValue().set(cacheKey, user, 10, TimeUnit.MINUTES);
        return new UserDetailsImpl(user);
    }


}
