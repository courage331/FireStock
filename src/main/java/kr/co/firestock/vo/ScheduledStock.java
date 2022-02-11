package kr.co.firestock.vo;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Document("stocks")
public class ScheduledStock {
    @Id
    String _id;
}
