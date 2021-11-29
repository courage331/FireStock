package kr.co.firestock.controller;

import kr.co.firestock.service.LoginService;
import kr.co.firestock.service.PortFolioService;
import kr.co.firestock.vo.LoginRequest;
import kr.co.firestock.vo.ResponseInfo;
import kr.co.firestock.vo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin("*")
public class LoginController {

    @Autowired
    LoginService loginService;

    @Autowired
    PortFolioService portFolioService;

    /**
     * 회원가입
     * */
    @PostMapping("/join")
    public ResponseInfo createUser(@RequestBody User user){
        ResponseInfo responseInfo = new ResponseInfo();
        String id = loginService.createUser(user);
        portFolioService.createPolio(id);
        responseInfo.setReturnCode(0);
        responseInfo.setReturnMsg("Success");
        responseInfo.setData(id);
        return responseInfo;
    }


    /**
     * 로그인시 토큰을 return 받음
     */
    @PostMapping("/login")
    public ResponseInfo login(@RequestBody LoginRequest loginRequest){
        ResponseInfo responseInfo = loginService.createToken(loginRequest);
        return responseInfo;
    }

    /**
     * token을 body에 싸서 보내면 해당하는 아이디에 모든 정보를 return
     */
    @PostMapping("/info")
    public ResponseInfo getUserFromToken(@RequestBody Map<String,String> param){
        ResponseInfo responseInfo = loginService.findUserInfo(param.get("token"));
        return responseInfo;
    }
}
