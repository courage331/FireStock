package kr.co.firestock.controller;

import kr.co.firestock.service.PortFolioService;
import kr.co.firestock.vo.ReqBodyFormat;
import kr.co.firestock.vo.ResponseInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/portfolio")
//@CrossOrigin("*")
public class PortFolioController {

    @Autowired
    PortFolioService portFolioService;

    /**
     * 회원 가입시에 자동으로 이루어짐
     * 회원에 대한 전체의 큰 포트폴리오를 만드는 method
     * createUser -> 이 컨트롤러를 통하는게 아니라 바로 service를 통하기에 이 method를 사용하진 않지만 일단 남겨둠...
     * 해당 유저의 포트폴리오가 이미 있다면 생성하지 않는다.
     *
     * ex) localhost:8080/api/v1/portfolio/create/folio?userId=test4
     *
     * */
    @RequestMapping("/input/folio")
    public ResponseInfo createFolio(
            @RequestParam(name = "userId", required = true, defaultValue = "") String userId
    ) {
        ResponseInfo responseInfo = portFolioService.createFolio(userId);
        return responseInfo;
    }

    /**
     *
     * 큰 포트폴리오 안에 들어가는 포트폴리오 각각의 이름을 지어주는 method
     * 선행조건:  test6 이라는 아이디가 존재해야함
     *
     * param)
     * userId : 사용자 id
     * portFolioName : 포트폴리오의 제목(이름)
     * portFolioType : 포트폴리오의 종류(일반형인지, ISP 같은식으로 나뉘는 종류)
     * portFolioMoney : 예수금 초기에 입력이 가능하다면 입력, 0으로 시작할거면 0 입력
     *
     * ex) localhost:8080/api/v1/portfolio/input/folioname?userId=test6&portFolioName=testv3&portFolioType=normal&portFolioMoney=30000
     *
     * */
    @RequestMapping("/input/folioname")
    public ResponseInfo createFolioName(
            @RequestParam(name = "userId", required = true, defaultValue = "") String userId,
            @RequestParam(name = "portFolioName", required = true, defaultValue = "") String portFolioName,
            @RequestParam(name = "portFolioType", required = true, defaultValue="") String portFolioType,
            @RequestParam(name = "portFolioMoney", required = true, defaultValue = "0") String portFolioMoney

    ) {
        ResponseInfo responseInfo = portFolioService.createFolioName(userId,portFolioType, portFolioName,portFolioMoney);
        return responseInfo;
    }

    /**
     *
     * 사용자의 포트폴리오 전체현황을 보여준다.
     * 용도 : 포트폴리오 탭 맨 처음 누를때 호출한다음에 파싱해서 사용...
     *
     * ex) localhost:8080/api/v1/portfolio/find/folio?userId=test6
     *
     * param)
     *
     * userId : 사용자 아이디
     * */
    @RequestMapping("/find/folio")
    public ResponseInfo findPortFolio(@RequestParam(name = "userId", required = true, defaultValue = "") String userId) {
        ResponseInfo responseInfo = portFolioService.findPortFolio(userId);
        return responseInfo;
    }

    /**
     *
     * 사용자의 특정 포트폴리오 자세한 현황을 보여준다.
     *
     * ex) localhost:8080/api/v1/portfolio/find/foliodetail?userId=test6&type=coin&portFolioName=testv3
     *
     * param)
     *
     * userId : 사용자 아이디
     * type : 어떤 종류의 값들을 볼건지(all -> 모든 내역을 다봄, coin을 입력할 경우 암호화폐에 대한것만 보여줌 자세한것은  portFolioService.findStock 가서 확인)
     * portFolioName : 사용자의 어떤 포트폴리오에서 볼건지
     * */
    @RequestMapping("/find/foliodetail")
    public ResponseInfo findPortFolioDetail(
            @RequestParam(name = "userId", required = true, defaultValue = "") String userId,
            @RequestParam(name = "type", required = true, defaultValue = "") String type,
            @RequestParam(name = "portFolioName", required = true, defaultValue = "") String portFolioName

    ) {
        ResponseInfo responseInfo = portFolioService.findPortFolioDetail(type,userId,portFolioName);
        return responseInfo;
    }

    /**
     * 포트폴리오 데이터를 삽입한다.
     * ex) localhost:8080/api/v1/portfolio/input/stock?type=coin&userId=test6&method=update&portFolioName=testv3
     *
     * param)
     * type = 어떤 타입의 데이터인지 ex) 국내 주식이면 domestic, ISA 이면 isa 처럼
     * userId = 사용자 아아디
     * method = update, delete 중에서 선택 삭제할거면 delete, 데이터를 삽입하거나 갱신할거면 update
     * portFolioName = 어떤 포트폴리오에서 작업할건지
     *
     * */
    @RequestMapping("/input/stock")
    public ResponseInfo inputStock(
            @RequestBody ReqBodyFormat reqBodyFormat,
            @RequestParam(name = "type", required = true, defaultValue = "") String type,
            @RequestParam(name = "userId", required = true, defaultValue = "") String userId,
            @RequestParam(name = "portFolioName", required = true, defaultValue = "") String portFolioName,
            @RequestParam(name = "method", required = true, defaultValue = "") String method /** update, delete */

    ) {
        ResponseInfo responseInfo = new ResponseInfo();
        if (type.equals("domestic")) {
            responseInfo = portFolioService.workDomesticStock(reqBodyFormat, method, userId, portFolioName);
        } else if (type.equals("overseas")) {
            responseInfo = portFolioService.workOverseasStock(reqBodyFormat, method, userId, portFolioName);
        } else if (type.equals("isa")) {
            responseInfo = portFolioService.workISA(reqBodyFormat, method, userId, portFolioName);
        } else if (type.equals("personal")) {
            responseInfo = portFolioService.workPersonal(reqBodyFormat, method, userId, portFolioName);
        } else if (type.equals("retirement")) {
            responseInfo = portFolioService.workRetirement(reqBodyFormat, method, userId, portFolioName);
        } else if (type.equals("coin")) {
            responseInfo = portFolioService.workCoin(reqBodyFormat, method, userId, portFolioName);
        } else if (type.equals("noncurrent")) {
            responseInfo = portFolioService.workNonCurrent(reqBodyFormat, method, userId, portFolioName);
        } else {
            responseInfo.setReturnCode(-1);
            responseInfo.setReturnMsg("Wrong type!!");
        }
        return responseInfo;
    }

}
