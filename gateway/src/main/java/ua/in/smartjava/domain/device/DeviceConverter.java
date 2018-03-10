package ua.in.smartjava.domain.device;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import ua.in.smartjava.mongo.EntityConverter;

//TODO this can be replaced with the custom mapper (mapstruct as a variant)
public class DeviceConverter implements EntityConverter<Device> {

    @Override
    public BasicDBObject toDocument(Device device) {
        Map<String, Object> document = new HashMap<>();
        Optional.ofNullable(device.getIp()).map(ip -> document.put("ip", ip));
        Optional.ofNullable(device.getName()).map(name -> document.put("name", name));
        Optional.ofNullable(device.getPort()).map(port -> document.put("port", port));
//        Optional.ofNullable(device.getId()).map(id -> document.put("_id", id));
        return new BasicDBObject(document);
    }

    @Override
    public BasicDBObject toBasicDBObject(Device device) {
        BasicDBObject searchQuery = new BasicDBObject();
        Optional.ofNullable(device.getIp()).map(ip -> searchQuery.put("ip", ip));
        Optional.ofNullable(device.getPort()).map(port -> searchQuery.put("port", port));
        Optional.ofNullable(device.getName()).map(name -> searchQuery.put("name", name));
        Optional.ofNullable(device.getId()).map(id -> searchQuery.put("_id", id));
        return searchQuery;
    }

    @Override
    public Device toEntity(DBObject document) {
        return Device.builder()
                .ip(document.get("ip") == null ? null : String.valueOf(document.get("ip")))
                .port(document.get("port") == null ? null : String.valueOf(document.get("port")))
                .name(document.get("name") == null ? null :String.valueOf(document.get("name")))
                .id(document.get("_id") == null ? null :String.valueOf(document.get("_id")))
                .build();
    }
}
