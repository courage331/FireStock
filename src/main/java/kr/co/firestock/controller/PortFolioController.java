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
@CrossOrigin("*")
public class PortFolioController {

    @Autowired
    PortFolioService portFolioService;

    /** 회원 가입시에 이루어짐*/
    @RequestMapping("/input/polio")
    public ResponseInfo createPolio(
            @RequestParam(name = "userId", required = true, defaultValue = "") String userId
    ) {
        ResponseInfo responseInfo = portFolioService.createPolio(userId);
        return responseInfo;
    }

    /**포트폴리오 이름 설정 */
    @RequestMapping("/input/polioname")
    public ResponseInfo createPolio(
            @RequestParam(name = "userId", required = true, defaultValue = "") String userId,
            @RequestParam(name = "portPolioType", required = true, defaultValue="") String portpolioType,
            @RequestParam(name = "portPolioName", required = true, defaultValue = "") String portPolioName
    ) {
        ResponseInfo responseInfo = portFolioService.createPolioName(userId,portpolioType, portPolioName);
        return responseInfo;
    }

    @RequestMapping("/find/stock")
    public ResponseInfo findStock(
            @RequestParam(name = "userId", required = true, defaultValue = "") String userId,
            @RequestParam(name = "type", required = true, defaultValue = "") String type,
            @RequestParam(name = "portPolioName", required = true, defaultValue = "") String portPolioName

    ) {
        ResponseInfo responseInfo = portFolioService.findStock(type,userId,portPolioName);
        return responseInfo;
    }

    @RequestMapping("/input/stock")
    public ResponseInfo inputStock(
            @RequestBody ReqBodyFormat reqBodyFormat,
            @RequestParam(name = "type", required = true, defaultValue = "") String type,
            @RequestParam(name = "userId", required = true, defaultValue = "") String userId,
            @RequestParam(name = "portPolioName", required = true, defaultValue = "") String portPolioName,
            @RequestParam(name = "method", required = true, defaultValue = "") String method /** update, delete */

    ) {
        ResponseInfo responseInfo = new ResponseInfo();
        if (type.equals("domestic")) {
            responseInfo = portFolioService.workDomesticStock(reqBodyFormat, method, userId, portPolioName);
        } else if (type.equals("overseas")) {
            responseInfo = portFolioService.workOverseasStock(reqBodyFormat, method, userId, portPolioName);
        } else if (type.equals("isa")) {
            responseInfo = portFolioService.workISA(reqBodyFormat, method, userId, portPolioName);
        } else if (type.equals("personal")) {
            responseInfo = portFolioService.workPersonal(reqBodyFormat, method, userId, portPolioName);
        } else if (type.equals("retirement")) {
            responseInfo = portFolioService.workRetirement(reqBodyFormat, method, userId, portPolioName);
        } else if (type.equals("coin")) {
            responseInfo = portFolioService.workCoin(reqBodyFormat, method, userId, portPolioName);
        } else if (type.equals("noncurrent")) {
            responseInfo = portFolioService.workNonCurrent(reqBodyFormat, method, userId, portPolioName);
        } else {
            responseInfo.setReturnCode(-1);
            responseInfo.setReturnMsg("Wrong type!!");
        }
        return responseInfo;
    }

}
