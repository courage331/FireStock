package kr.co.firestock.controller;

import kr.co.firestock.service.LoginService;
import kr.co.firestock.service.PortFolioService;
import kr.co.firestock.vo.LoginRequest;
import kr.co.firestock.vo.ResponseInfo;
import kr.co.firestock.vo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
public class LoginController {

    @Autowired
    LoginService loginService;

    @Autowired
    PortFolioService portFolioService;

    /**
     * 회원가입
     *
     * 회원가입시에 필요한 정보들은 User 객체 참고
     *
     * 로그인 진행 후 -> 포트폴리오 생성
     *
     * 포스트 형식으로 보내야함
     * ex) http://localhost:8080/api/v1/auth/join
     * Body에 최소로 들어가야 하는값(형식)
      {
          "_id":"test6",
          "password":"1234"
      }
     * */
    @PostMapping("/join")
    public ResponseInfo createUser(HttpServletRequest request, @RequestBody User user){
        log.info("[Start createUser][{}]",request.getRequestURL());
        ResponseInfo responseInfo = loginService.createUser(user);
        if(responseInfo.getReturnCode()!=0){
            return responseInfo;
        }
        responseInfo = portFolioService.createFolio(user.get_id());
        if(responseInfo.getReturnCode()!=0){
            return responseInfo;
        }
        responseInfo.setReturnMsg("[회원가입 성공!]");
        log.info("[End createUser][{}]",request.getRequestURL());
        return responseInfo;
    }


    /**
     * 로그인시 User의 모든 정보를 return
     *
     * User가 token은 방금 발급받은 토큰이 된다.
     *
     * ex) http://localhost:8080/api/v1/auth/login
     *
     * Body에 들어가는 값
     {
         "_id":"test6",
         "password":"1234"
     }
     */
    @PostMapping("/login")
    public ResponseInfo login(HttpServletRequest request, @RequestBody LoginRequest loginRequest){
        log.info("[Start login][{}]",request.getRequestURL());
        ResponseInfo responseInfo = loginService.createToken(loginRequest);
        log.info("[End login][{}]",request.getRequestURL());
        return responseInfo;
    }

    /**
     * 토큰을 body에 담아 보내면 User의 모든 정보를 return
     *
     * 위의 Login Method와는 비슷하지만 어떤 데이터를 보내느냐에 따라서 쓰는 Method가 달라짐
     *
     * 아이디랑 비밀번호로 데이터를 얻을경우에는 login method,
     * token을 사용하여 데이터를 얻을경우에는 아래 method
     *
     * ex) localhost:8080/api/v1/auth/info
     {
         "token" : "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0NiIsInBhc3N3b3JkIjoie2JjcnlwdH0kMmEkMTAkbDB3ZXI3dS9JOFdhMmF1Y0FjcjdlLmRvamNxdjJybE9oTENvNExUZ1lSRkVGeWlwdUUwdjIiLCJyb2xlcyI6IkFETUlOIiwiaWF0IjoxNjM3ODIxMDg0LCJleHAiOjE2Mzc4NTcwODR9.mUKc--wmQVKXFm_qs25ANuINxLElLr6Pin5UUN_Lo04"
     }

     */
    @PostMapping("/info")
    public ResponseInfo getUserFromToken(HttpServletRequest request, @RequestBody Map<String,String> param){
        log.info("[Start getUserFromToken][{}]",request.getRequestURL());
        ResponseInfo responseInfo = loginService.findUserInfo(param.get("token"));
        log.info("[End getUserFromToken][{}]",request.getRequestURL());
        return responseInfo;
    }

    /** 아이디 중복확인 */
    @GetMapping("/check/id/{_id}")
    public ResponseInfo checkId(HttpServletRequest request, @PathVariable(value="_id") String _id){
        log.info("[Start checkId][{}]",request.getRequestURL());
        ResponseInfo responseInfo = loginService.findUserId(_id);
        log.info("[End checkId][{}]",request.getRequestURL());
        return responseInfo;
    }

    /** 회원 탈퇴 */
    @DeleteMapping("/delete/{_id}")
    public ResponseInfo deleteUser(HttpServletRequest request, @PathVariable(value="_id") String _id){
        log.info("[Start deleteUser][{}]",request.getRequestURL());
        ResponseInfo responseInfo = loginService.deleteUser(_id);
        log.info("[End deleteUser][{}]",request.getRequestURL());
        return responseInfo;
    }

    /**비밀번호 변경 */
    @GetMapping("/find/password/{_id}")
    public ResponseInfo findPassword(
            HttpServletRequest request,
            @PathVariable(value="_id") String _id,
            @RequestParam(name = "password", required = true, defaultValue = "") String password
    ){
        log.info("[Start findPassword][{}]",request.getRequestURL());
        ResponseInfo responseInfo = loginService.findPassword(_id,password);
        log.info("[End findPassword][{}]",request.getRequestURL());
        return responseInfo;
    }


    /**닉네임 변경 */
    @GetMapping("/change/nickname/{_id}/{nickname}")
    public ResponseInfo changeNickname(
            HttpServletRequest request, @PathVariable(value="_id") String _id, @PathVariable(value="nickname") String nickname
    ){
        log.info("[Start changeNickname][{}]",request.getRequestURL());
        ResponseInfo responseInfo = loginService.changeNickname(_id,nickname);
        log.info("[End changeNickname][{}]",request.getRequestURL());
        return responseInfo;
    }


}
