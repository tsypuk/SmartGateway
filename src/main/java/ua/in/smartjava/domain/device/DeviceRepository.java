package ua.in.smartjava.domain.device;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;

import org.bson.types.ObjectId;

import java.util.HashMap;
import java.util.Map;

import ua.in.smartjava.mongo.CrudRepository;
import ua.in.smartjava.mongo.EntityConverter;

public class DeviceRepository extends CrudRepository<Device> {

    public DeviceRepository(EntityConverter<Device> entityConverter, DBCollection collection) {
        super(entityConverter, collection);
    }

    public void updateState(String id, State state) {
        Map<String, Object> document = new HashMap<>();
        document.put("state", state.toString());

        BasicDBObject updateDocument = new BasicDBObject();
        updateDocument.append("$set", document);
        collection.update(new BasicDBObject().append("_id", new ObjectId(id)), updateDocument);
    }
}
