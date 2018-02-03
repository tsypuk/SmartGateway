package ua.in.smartjava.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public interface EntityConverter<T> {
    BasicDBObject toDocument(T logRecord);
    BasicDBObject toBasicDBObject(T device);
    T toEntity(DBObject document);
}
