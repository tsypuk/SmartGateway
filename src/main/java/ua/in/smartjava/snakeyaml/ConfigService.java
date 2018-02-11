package ua.in.smartjava.snakeyaml;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Optional;

import static org.slf4j.helpers.MessageFormatter.format;

public class ConfigService {

    private Config configuration;

    public ConfigService() {
        this.configuration = loadFromYaml("config", Config.class);
    }

    private  <T> T loadFromYaml(String configName, Class<T> clazz) {
        Optional<String> profileName = readActiveProfile();
        String configFile = profileName.map(profile -> format("{}_{}.yaml", configName, profile).getMessage())
                .orElse(format("{}.yaml",configName).getMessage());
        Yaml yaml = new Yaml();
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        try (InputStream resourceAsStream = contextClassLoader.getResourceAsStream(configFile)) {
            T config = yaml.loadAs(resourceAsStream, clazz);
            return new EnvironmentEnricher<T>().enrichWithEnvVariables(config);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Optional<String> readActiveProfile() {
        return Optional.ofNullable(System.getProperty("activeProfile"));
    }

    public Config getGlobalConfig() {
        return configuration;
    }

    public String getAlexaIp() {
        return configuration.getAlexaIp();
    }

    public Mongo getMongoConfig() {
        return configuration.getMongo();
    }

    public UPnP getUpNPConfig() {
        return configuration.getUpnp();
    }

    public Server getServerConfig() {
        return configuration.getServer();
    }
}
