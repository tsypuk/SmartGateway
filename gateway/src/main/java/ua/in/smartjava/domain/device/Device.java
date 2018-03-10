package ua.in.smartjava.domain.device;

import org.slf4j.helpers.MessageFormatter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ua.in.smartjava.mongo.BaseEntity;

@Data
@EqualsAndHashCode(callSuper=true)
public class Device extends BaseEntity {
    private String name;
    private String ip;
    private String port;
    private String state;

    @Builder
    private Device(String name, String ip, String port, String id, String state){
        super(id);
        this.name = name;
        this.ip = ip;
        this.port = port;
        this.state = state;
    }

    public String getAddress() {
        return MessageFormatter.format("{}:{}", this.ip, this.port).getMessage();
    }
}
