package ua.in.smartjava.mongo;

import com.mongodb.DB;
import com.mongodb.DBCollection;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import ua.in.smartjava.domain.device.Device;
import ua.in.smartjava.domain.device.DeviceRepository;
import ua.in.smartjava.domain.logrecord.LogRecord;
import ua.in.smartjava.snakeyaml.Mongo;

public class MongoData {

    private com.mongodb.Mongo mongo;
    private Map<Class, CrudRepository> repositories = new HashMap<>();
    DBCollection logRecordCollection;

    public MongoData(Mongo mongoConfig) {
        try {
            mongo = new com.mongodb.Mongo(mongoConfig.getHost(), Integer.parseInt(mongoConfig.getPort()));
            final DB database = mongo.getDB(mongoConfig.getDb());

            logRecordCollection = database.getCollection("logrecords");

            repositories.put(Device.class,
                    new DeviceRepository(new ReflectionEntityConverter(Device.class), database.getCollection("devices")));

            repositories.put(LogRecord.class,
                    new CrudRepository(new ReflectionEntityConverter(LogRecord.class), logRecordCollection));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public DBCollection getLogrecords() {
        return logRecordCollection;
    }

    public CrudRepository getRepository(Class entityClass) {
        return repositories.get(entityClass);
    }
}
