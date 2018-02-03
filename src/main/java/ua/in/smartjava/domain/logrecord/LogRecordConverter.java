package ua.in.smartjava.domain.logrecord;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import java.util.HashMap;
import java.util.Map;

import ua.in.smartjava.mongo.EntityConverter;

public class LogRecordConverter implements EntityConverter<LogRecord> {

    @Override
    public BasicDBObject toDocument(LogRecord logRecord) {
        Map<String, Object> document = new HashMap<>();
        document.put("record", logRecord.getRecord());
        document.put("localdatetime", logRecord.getLocalDateTime().toString());
        if (logRecord.getId() != null) {
            document.put("_id", logRecord.getId());
        }
        return new BasicDBObject(document);
    }

    @Override
    public BasicDBObject toBasicDBObject(LogRecord logRecord) {
        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put("record", logRecord.getRecord());
        searchQuery.put("localdatetime", logRecord.getLocalDateTime());
        searchQuery.put("_id", logRecord.getId());
        return searchQuery;
    }

    @Override
    public LogRecord toEntity(DBObject document) {
        return LogRecord.builder()
                .record(String.valueOf(document.get("record")))
//                .localDateTime(LocalDateTime.fromString.valueOf(document.get("localdatetime")))
                .id(String.valueOf(document.get("_id")))
                .build();
    }
}
