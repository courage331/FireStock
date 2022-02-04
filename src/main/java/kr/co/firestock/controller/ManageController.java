package kr.co.firestock.controller;

import kr.co.firestock.service.PortFolioService;
import kr.co.firestock.vo.ResponseInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/api/v1/manage")
public class ManageController {

    @Autowired
    PortFolioService portFolioService;

    /**매니저의 포트폴리오 조회 */
    @GetMapping("/find/all/portfolio/{userId}")
    public ResponseInfo findAllPortFolio(
            HttpServletRequest request,
            @PathVariable(value="userId") String userId){
        ResponseInfo responseInfo = new ResponseInfo();
        responseInfo = portFolioService.findAllPortFolio(userId);


        return responseInfo;
    }


}
