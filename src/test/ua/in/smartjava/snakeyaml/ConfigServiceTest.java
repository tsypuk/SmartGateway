package ua.in.smartjava.snakeyaml;

import org.junit.Test;

import static org.junit.Assert.*;

public class ConfigServiceTest {

    private final ConfigService configService = new ConfigService();

    private static final String OVERRIDEN_IP = "127.0.0.1";
    private static final String OVERRIDEN_HOST = "www.fake.test";
    private static final String OVERRIDEN_DB = "database";

    static {
        System.setProperty("alexaIp", OVERRIDEN_IP);
        System.setProperty("HOST", OVERRIDEN_HOST);
        System.setProperty("DB", OVERRIDEN_DB);
    }

    @Test
    public void test() {
        // Given-When-Then
        assertEquals(OVERRIDEN_IP, configService.getAlexaIp());
        assertEquals(OVERRIDEN_HOST, configService.getMongoConfig().getHost());
        assertEquals(OVERRIDEN_DB, configService.getMongoConfig().getDb());
        assertEquals("10", configService.getServerConfig().getMaxThreads());
    }

}