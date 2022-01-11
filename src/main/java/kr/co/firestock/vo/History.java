package kr.co.firestock.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("history")
@Getter
@Setter
@ToString
@Builder
public class History {

    /** _id 는 랜덤 생성 => 겹치지 않게 하기 위해서 */

    String userId;

    String portFolioName; /**어떤 포트폴리오에서 거래했는가 */

    String type; /**어떤 거래 종류였는가 buy or sell*/

    String money; /**손익이 얼마나 되는가 */

    String regdt; /**언제 거래했는가 */

    PortFolioData portFolioData; /**거래한 데이터*/

    public History(String userId, String portFolioName, String type, String money, String regdt, PortFolioData portFolioData) {
        this.userId = userId;
        this.portFolioName = portFolioName;
        this.type = type;
        this.money = money;
        this.regdt = regdt;
        this.portFolioData = portFolioData;
    }
}
