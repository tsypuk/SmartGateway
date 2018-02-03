package ua.in.smartjava.domain.device;

import org.slf4j.helpers.MessageFormatter;

import lombok.Builder;
import lombok.Data;
import ua.in.smartjava.mongo.BaseEntity;

@Data
public class Device extends BaseEntity {
    private String name;
    private String ip;
    private String port;

    @Builder
    private Device(String name, String ip, String port, String id){
        super(id);
        this.name = name;
        this.ip = ip;
        this.port = port;
    }

    public String getAddress() {
        return MessageFormatter.format("{}:{}", this.ip, this.port).getMessage();
    }
}
