package ua.in.smartjava.snakeyaml;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;

import static java.text.MessageFormat.format;

/**
 * Uses reflection to load properties from config, reads ENV variables to override values.
 * Usage examples (any of):
 *      Mongo.host
 *      MONGO.HOST
 *      mongo.host
 */
public class EnvironmentEnricher<T> {


    public T enrichWithEnvVariables(T config) {
        return (T) process(config);
    }

    private Object process(Object config) {
        Field[] declaredFields = config.getClass().getDeclaredFields();
        Arrays.stream(declaredFields).forEach(
                field -> {
                    field.setAccessible(true);
                    Class<?> type = field.getType();
                    if (type.getCanonicalName().startsWith("java.lang")) {
                        Optional.ofNullable(getProperty(
                                format("{0}.{1}", config.getClass().getSimpleName(), field.getName())))
                                .ifPresent(val -> {
                                    try {
                                        field.set(config, val);
                                    } catch (IllegalAccessException e) {
                                        e.printStackTrace();
                                    }
                                });
                    } else {
                        try {
                            process(field.get(config));
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
        return config;
    }

    private String getProperty(String property) {
        String result = null;
        if (System.getProperty(property) != null) {
            result = System.getProperty(property);
        } else if (System.getProperty(property.toUpperCase()) != null) {
            result = System.getProperty(property.toUpperCase());
        } else if (System.getProperty(property.toLowerCase()) != null) {
            result = System.getProperty(property.toLowerCase());
        }
        return result;
    }
}
