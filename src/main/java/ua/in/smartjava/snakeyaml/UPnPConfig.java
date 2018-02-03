package ua.in.smartjava.snakeyaml;

import lombok.Data;

@Data
public class UPnPConfig {
    private String ttl;
    private String timeout;
    private String port;
    private String gateway;
    private String broadcastIp;
}
