package kr.co.firestock.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ResponseInfo {
    private int returnCode = 0;
    private String returnMsg = "성공";
    private Object data = null;

    public ResponseInfo(int returnCode,String returnMsg) {
        this.returnCode = returnCode;
        this.returnMsg = returnMsg;
    }

    public ResponseInfo(int returnCode, String returnMsg, Object data) {
        this.returnCode = returnCode;
        this.returnMsg = returnMsg;
        this.data = data;
    }
}
