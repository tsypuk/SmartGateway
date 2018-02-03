package ua.in.smartjava.snakeyaml;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Optional;

import static org.slf4j.helpers.MessageFormatter.format;

public class ConfigService {

    private Configuration configuration;

    public ConfigService() {
        this.configuration = loadFromYaml("config", Configuration.class);
    }

    private  <T> T loadFromYaml(String configName, Class<T> clazz) {
        Optional<String> profileName = readActiveProfile();
        String configFile = profileName.map(profile -> format("/{}_{}.yaml", configName, profile).getMessage())
                .orElse(configName);
        Yaml yaml = new Yaml();
        try (InputStream in = ConfigService.class.getResourceAsStream(configFile)) {
            T config = yaml.loadAs(in, clazz);
            return config;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Optional<String> readActiveProfile() {
        return Optional.ofNullable(System.getProperty("activeProfile"));
    }

    public Configuration getGlobalConfig() {
        return configuration;
    }

    public String getAlexaIp() {
        return configuration.getAlexaIp();
    }

    public MongoConfig getMongoConfig() {
        return configuration.getMongo();
    }

    public UPnPConfig getUpNPConfig() {
        return configuration.getUpnp();
    }

    public ServerConfig getServerConfig() {
        return configuration.getServer();
    }
}