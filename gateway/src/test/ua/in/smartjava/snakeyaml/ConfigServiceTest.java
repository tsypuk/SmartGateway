package ua.in.smartjava.snakeyaml;

import org.junit.Test;

import static org.junit.Assert.*;

public class ConfigServiceTest {

    private final ConfigService configService = new ConfigService();

    private static final String ALEXA_IP = "127.0.0.1";
    private static final String MONGO_HOST = "www.fake.test";
    private static final String MONGO_DB = "database";
    private static final String MONGO_PORT = "7777";

    public static final String UPNP_PORT = "555";

    static {
        System.setProperty("Config.alexaIp", ALEXA_IP);
        System.setProperty("Mongo.host", MONGO_HOST);
        System.setProperty("MONGO.DB", MONGO_DB);
        System.setProperty("mongo.port", MONGO_PORT);
        System.setProperty("upnp.port", UPNP_PORT);
    }

    @Test
    public void test() {
        // Given-When-Then
        assertEquals(ALEXA_IP, configService.getAlexaIp());
        assertEquals(MONGO_HOST, configService.getMongoConfig().getHost());
        assertEquals(MONGO_DB, configService.getMongoConfig().getDb());
        assertEquals("10", configService.getServerConfig().getMaxThreads());
        assertEquals(MONGO_PORT, configService.getMongoConfig().getPort());
        assertEquals("8080", configService.getServerConfig().getPort());
        assertEquals(UPNP_PORT, configService.getUpNPConfig().getPort());

    }

}