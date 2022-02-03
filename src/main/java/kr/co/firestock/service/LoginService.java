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

    public ResponseInfo createUser(User user) {
        ResponseInfo responseInfo = new ResponseInfo();
        try {
            User newUser = User.builder().
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
                    .regDt(StringUtil.makeTodayDate()).build();
            userMongoRepository.save(newUser);

            responseInfo.setReturnCode(0);
            log.info("[Create User info][{}]", newUser.toString());
        } catch (Exception e) {
            log.error("[createUser Error][{}]", e.toString());
            responseInfo.setReturnCode(-1);
            responseInfo.setReturnMsg("[회원가입 실패!]");
        }
        return responseInfo;

    }

    public ResponseInfo createToken(LoginRequest loginRequest) {
        try {
            //유효성검사 진행
//            Optional<User> user = Optional.ofNullable(userMongoRepository.findBy_id(loginRequest.get_id())
//                    .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 ID 입니다.")));
            Optional<User> user = userMongoRepository.findBy_id(loginRequest.get_id());
            User loginUser = findUser(loginRequest.get_id(), loginRequest.getPassword());
            String token = jwtTokenProvider.createToken(user.get().get_id(), user.get().getPassword(), user.get().getRoles());
            loginUser.setToken(token);
            loginUser.setLastloginDt(StringUtil.makeTodayDate());
            userMongoRepository.save(loginUser);
            log.info("[{} 유저 로그인 성공!]", loginRequest.get_id());
            return new ResponseInfo(0, "[로그인 성공!]", loginUser);
        } catch (Exception e) {
            log.error(loginRequest.get_id() + " : 에해당하는 아이디는 존재하지 않습니다.");
            log.error("[createToken Error][{}]", e.toString());
            return new ResponseInfo(-1, "[로그인 실패!]", e.toString());
        }
    }

    public User findUser(String _id, String password) {
        Optional<User> user = userMongoRepository.findBy_id(_id);
        if (!passwordEncoder.matches(password, user.get().getPassword())) {
            log.info("Login Fail");
            throw new IllegalArgumentException("잘못된 비밀번호 입니다.. ");
        }
        log.info("Login Success");
        return user.get();
    }

    public ResponseInfo findUserInfo(String token) {
        ResponseInfo responseInfo = new ResponseInfo();
        try {
            User user = jwtTokenProvider.getUserInfoInAuthentication(token);
            responseInfo.setReturnCode(1);
            responseInfo.setReturnMsg("[유저 정보 조회 성공]");
            responseInfo.setData(user);
            log.info("[findUserInfo Success][{}]", user.toString());
        } catch (Exception e) {
            responseInfo.setReturnCode(-1);
            responseInfo.setReturnMsg("Fail");
            log.error("[findUserInfo Error][{}]", e.toString());
        }
        return responseInfo;
    }

    public ResponseInfo findUserId(String _id) {
        ResponseInfo responseInfo = new ResponseInfo();
        Optional<User> user = userMongoRepository.findBy_id(_id);
        /** 중복이면 true, 중복이 아니면 false return */
        if (user.isPresent()) {
            responseInfo.setReturnMsg("[중복되는 아이디 입니다!]");
            responseInfo.setData("true");
        } else {
            responseInfo.setReturnMsg("[중복되는 아이디가 없습니다!]");
            responseInfo.setData("false");
        }
        responseInfo.setReturnCode(0);
        return responseInfo;
    }

    public ResponseInfo findPassword(String _id, String password) {
        Optional<User> user = userMongoRepository.findBy_id(_id);
        if (!user.isPresent()) {
            return new ResponseInfo(-1, "[존재하지 않는 유저입니다.]");
        }
        User changeUser = user.get();
        changeUser.setPassword(passwordEncoder.encode(password));
        changeUser.setUpdDt(new StringUtil().makeTodayDate());
        userMongoRepository.save(changeUser);
        return new ResponseInfo(0, "[비밀번호가 변경되었습니다.]");
    }

    public ResponseInfo deleteUser(String _id) {
        ResponseInfo responseInfo = new ResponseInfo();
        Optional<User> user = userMongoRepository.findBy_id(_id);
        userMongoRepository.deleteById(_id);
        if (!user.isPresent()) {
            return new ResponseInfo(1, "[성공적으로 삭제되었습니다.]");
        } else {
            return new ResponseInfo(-1,"[삭제 실패!]");
        }
    }

    public ResponseInfo changeNickname(String _id, String nickname) {
        Optional<User> user = userMongoRepository.findBy_id(_id);
        if (!user.isPresent()) {
            return new ResponseInfo(-1, "[존재하지 않는 유저입니다.]");
        }
        User changeUser = user.get();
        changeUser.setNickname(nickname);
        changeUser.setUpdDt(new StringUtil().makeTodayDate());
        userMongoRepository.save(changeUser);
        return new ResponseInfo(0, "[닉네임이 변경되었습니다.]",changeUser);

    }
}
