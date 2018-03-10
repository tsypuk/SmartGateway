package ua.in.smartjava.snakeyaml;

import lombok.Data;

@Data
public final class Config {
    private Mongo mongo;
    private String alexaIp;
    private Server server;
    private UPnP upnp;
}
