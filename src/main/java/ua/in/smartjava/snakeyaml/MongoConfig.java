package ua.in.smartjava.snakeyaml;

import lombok.Data;

@Data
public class MongoConfig {
    private String host;
    private String port;
    private String db;
}
