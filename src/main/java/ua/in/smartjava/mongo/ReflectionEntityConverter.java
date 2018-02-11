package ua.in.smartjava.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class ReflectionEntityConverter<T> implements EntityConverter<T> {

    private Class<T> clazz;

    public ReflectionEntityConverter(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public BasicDBObject toDocument(T logRecord) {
        Map<String, Object> document = mapFields(logRecord);
        return new BasicDBObject(document);
    }

    private Map<String, Object> mapFields(T entity) {
        Map<String, Object> document = new HashMap<>();
        Field[] declaredFields = entity.getClass().getDeclaredFields();
        Arrays.stream(declaredFields)
                .forEach(field -> {
                    field.setAccessible(true);
                    try {
                        if (field.get(entity) != null) {
                            document.put(field.getName(), field.get(entity));
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                });
        return document;
    }

    @Override
    public BasicDBObject toBasicDBObject(T entity) {
        Map<String, Object> document = mapFields(entity);
        mapId(entity, document);
        return new BasicDBObject(document);
    }

    private void mapId(T entity, Map<String, Object> document) {
        try {
            Field id = BaseEntity.class.getDeclaredField("id");
            id.setAccessible(true);
            document.put("_id", id.get(entity));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This implementation was created to operate with classes instrumented by Lombok.
     * Lombok's builder inner class is used to enrich properties.
     * @param document
     * @return instantiated entity
     */
    @Override
    public T toEntity(DBObject document) {
        try {
            Object builder = clazz.getMethod("builder").invoke(clazz);
            Field[] declaredFields = builder.getClass().getDeclaredFields();
            Stream.of(declaredFields).forEach(
                    field -> {
                        field.setAccessible(true);
                        String fieldName = field.getName();
                        if (document.get(fieldName) != null) {
                            try {
                                field.set(builder, document.get(fieldName));
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        }
                    }
            );

            Field id = builder.getClass().getDeclaredField("id");
            id.setAccessible(true);
            id.set(builder, String.valueOf(document.get("_id")));

            Object entity = builder.getClass().getMethod("build").invoke(builder);
            return (T)entity;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
