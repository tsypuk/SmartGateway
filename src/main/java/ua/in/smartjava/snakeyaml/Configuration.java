package ua.in.smartjava.snakeyaml;

import lombok.Data;

@Data
public final class Configuration {
    private MongoConfig mongo;
    private String alexaIp;
    private ServerConfig server;
    private UPnPConfig upnp;
}
