package kr.co.firestock.controller;

import kr.co.firestock.service.PortFolioService;
import kr.co.firestock.vo.ReqBodyFormat;
import kr.co.firestock.vo.ResponseInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/api/v1/portfolio")
public class PortFolioController {

    @Autowired
    PortFolioService portFolioService;

    /**
     * 회원 가입시에 자동으로 이루어짐
     * 회원에 대한 전체의 큰 포트폴리오를 만드는 method
     * createUser -> 이 컨트롤러를 통하는게 아니라 바로 service를 통하기에 이 method를 사용하진 않지만 일단 남겨둠...
     * 해당 유저의 포트폴리오가 이미 있다면 생성하지 않는다.
     * <p>
     * ex) localhost:8080/api/v1/portfolio/create/folio?userId=test4
     */
    @RequestMapping("/input/folio")
    public ResponseInfo createFolio(
            HttpServletRequest request,
            @RequestParam(name = "userId", required = true, defaultValue = "") String userId
    ) {
        log.info("[Start createFolio]");
        ResponseInfo responseInfo = portFolioService.createFolio(userId);
        log.info("[End createFolio]");
        return responseInfo;
    }

    /**
     * 큰 포트폴리오 안에 들어가는 포트폴리오 각각의 이름을 지어주는 method
     * 선행조건:  test6 이라는 아이디가 존재해야함
     * <p>
     * param)
     * userId : 사용자 id
     * portFolioName : 포트폴리오의 제목(이름)
     * portFolioType : 포트폴리오의 종류(일반형인지, ISP 같은식으로 나뉘는 종류)
     * <p>
     * ex) localhost:8080/api/v1/portfolio/input/folioname?userId=test6&portFolioName=testv3&portFolioType=normal
     */
    @RequestMapping("/input/folioname")
    public ResponseInfo createFolioName(
            HttpServletRequest request,
            @RequestParam(name = "userId", required = true, defaultValue = "") String userId,
            @RequestParam(name = "portFolioName", required = true, defaultValue = "") String portFolioName,
            @RequestParam(name = "portFolioType", required = true, defaultValue = "") String portFolioType
    ) {
        log.info("[Start createFolioName][{}]",request.getRequestURL());
        ResponseInfo responseInfo = portFolioService.createFolioName(userId, portFolioType, portFolioName);
        log.info("[End createFolioName][{}]",request.getRequestURL());
        return responseInfo;
    }

    /**
     * 사용자의 포트폴리오 전체현황을 보여준다.
     * 용도 : 포트폴리오 탭 맨 처음 누를때 호출한다음에 파싱해서 사용...
     * <p>
     * ex) localhost:8080/api/v1/portfolio/find/folio?userId=test6
     * <p>
     * param)
     * <p>
     * userId : 사용자 아이디
     */
    @RequestMapping("/find/folio")
    public ResponseInfo findPortFolio(HttpServletRequest request, @RequestParam(name = "userId", required = true, defaultValue = "") String userId) {
        log.info("[Start findPortFolio][{}]",request.getRequestURL());
        ResponseInfo responseInfo = portFolioService.findPortFolio(userId);
        log.info("[End findPortFolio][{}]",request.getRequestURL());
        return responseInfo;
    }

    /**
     * 사용자의 특정 포트폴리오 자세한 현황을 보여준다.
     * <p>
     * ex) localhost:8080/api/v1/portfolio/find/foliodetail?userId=test6&type=coin&portFolioName=testv3
     * <p>
     * param)
     * <p>
     * userId : 사용자 아이디
     * type : 어떤 종류의 값들을 볼건지(all -> 모든 내역을 다봄, coin을 입력할 경우 암호화폐에 대한것만 보여줌 자세한것은  portFolioService.findPortFolioDetail 가서 확인)
     * portFolioName : 사용자의 어떤 포트폴리오에서 볼건지
     */
    @RequestMapping("/find/foliodetail")
    public ResponseInfo findPortFolioDetail(
            HttpServletRequest request,
            @RequestParam(name = "userId", required = true, defaultValue = "") String userId,
            @RequestParam(name = "type", required = true, defaultValue = "") String type,
            @RequestParam(name = "portFolioName", required = true, defaultValue = "") String portFolioName
    ) {
        log.info("[Start findPortFolioDetail][{}]",request.getRequestURL());
        ResponseInfo responseInfo = portFolioService.findPortFolioDetail(type, userId, portFolioName);
        log.info("[End findPortFolioDetail][{}]",request.getRequestURL());
        return responseInfo;
    }


    @RequestMapping("/delete/foliodetail")
    public ResponseInfo deletePortFolioDetail(
            HttpServletRequest request,
            @RequestParam(name = "userId", required = true, defaultValue = "") String userId,
            @RequestParam(name = "portFolioName", required = true, defaultValue = "") String portFolioName
    ) {
        log.info("[Start deletePortFolioDetail][{}]",request.getRequestURL());
        ResponseInfo responseInfo = portFolioService.deletePortFolioDetail(userId, portFolioName);
        log.info("[End deletePortFolioDetail][{}]",request.getRequestURL());
        return responseInfo;
    }

    /**
     * 포트폴리오 데이터를 삽입한다.
     * ex) localhost:8080/api/v1/portfolio/input/stock?&userId=test6&method=update&portFolioName=testv3
     * <p>
     * param)
     * userId = 사용자 아아디
     * portFolioName = 어떤 포트폴리오에서 작업할건지
     * method = sell(매매), buy(매수) update(추가), delete(삭제)
     */
    @RequestMapping("/input/stock")
    public ResponseInfo inputStock(
            HttpServletRequest request,
            @RequestBody ReqBodyFormat reqBodyFormat,
            @RequestParam(name = "userId", required = true, defaultValue = "") String userId,
            @RequestParam(name = "portFolioName", required = true, defaultValue = "") String portFolioName,
            @RequestParam(name = "method", required = true, defaultValue = "") String method /** sell(매매), buy(매수) update(추가), delete(삭제) */
    ) {
        log.info("[Start inputStock][{}]",request.getRequestURL());
        ResponseInfo responseInfo = portFolioService.inputPortFolioData(reqBodyFormat, method, userId, portFolioName);
        log.info("[End inputStock][{}]",request.getRequestURL());
        return responseInfo;
    }

    /**
     * 포트폴리오 데이터를 삽입한다.
     * ex) localhost:8080/api/v1/portfolio/input/won?&userId=test6&method=input&portFolioName=testv3&money=10000&moneytype=won
     * <p>
     * param)
     * userId = 사용자 아아디
     * portFolioName = 어떤 포트폴리오에서 작업할건지
     * method = input(돈 넣기), output(돈 뺴내기)
     * moneyType = won, dollar
     * money = 넣는(뺄) 금액 -- 뺀 금액이 현재 있는 금액보다 클 경우 returnCode = -1이 된다.
     */
    @RequestMapping("/input/won")
    public ResponseInfo inputWon(
            HttpServletRequest request,
            @RequestParam(name = "userId", required = true, defaultValue = "") String userId,
            @RequestParam(name = "portFolioName", required = true, defaultValue = "") String portFolioName,
            @RequestParam(name = "method", required = true, defaultValue = "") String method,  /**input output */
            @RequestParam(name = "moneyType", required = true, defaultValue = "") String moneyType,
            @RequestParam(name = "money", required = true, defaultValue = "") int money
    ) {
        log.info("[Start inputWon][{}]",request.getRequestURL());
        ResponseInfo responseInfo = portFolioService.inputWon(method, userId, portFolioName, money, moneyType);
        log.info("[End inputWon][{}]",request.getRequestURL());
        return responseInfo;
    }
}
