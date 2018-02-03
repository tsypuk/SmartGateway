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
        Optional.ofNullable(device.getPort()).map(id -> document.put("_id", id));
        return new BasicDBObject(document);
    }

    @Override
    public BasicDBObject toBasicDBObject(Device device) {
        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put("ip", device.getIp());
        searchQuery.put("port", device.getPort());
        searchQuery.put("name", device.getName());
        searchQuery.put("_id", device.getId());
        return searchQuery;
    }

    @Override
    public Device toEntity(DBObject document) {
        return Device.builder()
                .ip(String.valueOf(document.get("ip")))
                .port(String.valueOf(document.get("port")))
                .name(String.valueOf(document.get("name")))
                .id(String.valueOf(document.get("_id")))
                .build();
    }
}
