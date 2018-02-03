package ua.in.smartjava.snakeyaml;

import lombok.Data;

@Data
public class ServerConfig {
    private String maxThreads;
    private String port;
}
