package kr.co.firestock.service;


import kr.co.firestock.repository.UserMongoRepository;
import kr.co.firestock.security.JwtTokenProvider;
import kr.co.firestock.util.StringUtil;
import kr.co.firestock.vo.LoginRequest;
import kr.co.firestock.vo.ResponseInfo;
import kr.co.firestock.vo.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService {

    @Autowired
    UserMongoRepository userMongoRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    public String createUser(User user) {

        return userMongoRepository.save(User.builder().
                        _id(user.get_id())
                        .password(passwordEncoder.encode(user.getPassword()))
                        .roles("ADMIN")
                        .name(user.getName())
                        .nickname(user.getNickname())
                        .email(user.getEmail())
                        .birthday(user.getBirthday())
                        .token("")
                        .lastloginDt(StringUtil.makeTodayDate())
                        .updDt(StringUtil.makeTodayDate())
                        .regDt(StringUtil.makeTodayDate()).build()).get_id();

    }


    public ResponseInfo createToken(LoginRequest loginRequest) {
        try {
            //유효성검사 진행
//            Optional<User> user = Optional.ofNullable(userMongoRepository.findBy_id(loginRequest.get_id())
//                    .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 ID 입니다.")));
            Optional<User> user = userMongoRepository.findBy_id(loginRequest.get_id());
            User loginUser = findUser(loginRequest.get_id(),loginRequest.getPassword());
            String token = jwtTokenProvider.createToken(user.get().get_id(),user.get().getPassword(),user.get().getRoles());
            loginUser.setToken(token);
            loginUser.setLastloginDt(StringUtil.makeTodayDate());
            userMongoRepository.save(loginUser);
            return new ResponseInfo(0,"Success",loginUser);
        }catch(Exception e){
            log.error("해당 아이디는 존재하지 않습니다.");
            return new ResponseInfo(-1,"Fail","WRONG ID");
        }
    }

    public User findUser(String _id, String password) {
        Optional<User> user = userMongoRepository.findBy_id(_id);
        if(!passwordEncoder.matches(password,user.get().getPassword())){
            log.info("Login Fail");
            throw new IllegalArgumentException("잘못된 비밀번호 입니다.. ");
        }
        log.info("Login Success");
        return user.get();
    }

    public ResponseInfo findUserInfo(String token){
        User user = jwtTokenProvider.getUserInfoInAuthentication(token);
        ResponseInfo responseInfo = new ResponseInfo();
        responseInfo.setReturnCode(0);
        responseInfo.setReturnMsg("Success");
        responseInfo.setData(user);
        return responseInfo;
    }
}
