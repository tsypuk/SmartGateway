package ua.in.smartjava.mongo;

import com.mongodb.DB;
import com.mongodb.Mongo;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import ua.in.smartjava.domain.device.Device;
import ua.in.smartjava.domain.device.DeviceConverter;
import ua.in.smartjava.domain.logrecord.LogRecord;
import ua.in.smartjava.domain.logrecord.LogRecordConverter;
import ua.in.smartjava.snakeyaml.MongoConfig;

public class MongoData {

    private Mongo mongo;
    private Map<Class, CrudRepository> repositories = new HashMap<>();

    public MongoData(MongoConfig mongoConfig) {
        try {
            mongo = new Mongo(mongoConfig.getHost(), Integer.parseInt(mongoConfig.getPort()));
            final DB database = mongo.getDB(mongoConfig.getDb());
            repositories.put(Device.class,
                    new CrudRepository(new DeviceConverter(), database.getCollection("devices")));
            repositories.put(LogRecord.class,
                    new CrudRepository(new LogRecordConverter(), database.getCollection("logrecords")));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public CrudRepository getRepository(Class entityClass) {
        return repositories.get(entityClass);
    }
}
