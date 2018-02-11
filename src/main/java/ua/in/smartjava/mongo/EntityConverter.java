package ua.in.smartjava.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public interface EntityConverter<T> {
    BasicDBObject toDocument(T entity);
    BasicDBObject toBasicDBObject(T entity);
    T toEntity(DBObject document);
}
