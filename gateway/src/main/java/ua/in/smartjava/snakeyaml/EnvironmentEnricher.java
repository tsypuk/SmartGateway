package ua.in.smartjava.snakeyaml;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;

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
                        getProperty(
                                format("{0}.{1}", config.getClass().getSimpleName(), field.getName()))
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

    private Optional<String> getProperty(String property) {
        Function<String, String> likeAs = (any) -> System.getProperty(property);
        Function<String, String> upperCase = value -> (value != null) ? value : System.getProperty(property
                .toUpperCase());
        Function<String, String> lowerCase = value -> (value != null) ? value : System.getProperty(property
                .toLowerCase());
        return Optional.ofNullable(likeAs.andThen(upperCase).andThen(lowerCase).apply(property));
    }
}
