package ua.in.smartjava.domain.logrecord;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ua.in.smartjava.mongo.BaseEntity;

@Data
@EqualsAndHashCode(callSuper=true)
public class LogRecord extends BaseEntity {
    private String record;
    //TODO create convertor to-from LocalDateTime
    private String localDateTime;

    @Builder
    private LogRecord(String record, String localDateTime, String id){
        super(id);
        this.record = record;
        this.localDateTime = localDateTime;
    }
}
