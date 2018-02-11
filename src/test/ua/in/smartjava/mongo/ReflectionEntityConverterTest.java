package ua.in.smartjava.mongo;

import com.mongodb.BasicDBObject;

import org.junit.Test;

import ua.in.smartjava.domain.device.Device;

import static org.junit.Assert.*;

public class ReflectionEntityConverterTest {

    private EntityConverter<Device> entityConverter = new ReflectionEntityConverter(Device.class);

    @Test
    public void toDocument() throws Exception {
        Device device = Device.builder().port("8080").ip("127.0.0.1").id("13").name("Test").build();
        BasicDBObject basicDBObject = entityConverter.toDocument(device);
        assertEquals(basicDBObject.get("port"), "8080");
        assertEquals(basicDBObject.get("ip"), "127.0.0.1");
        assertEquals(basicDBObject.get("name"), "Test");
        assertNull(basicDBObject.get("_id"));
    }

    @Test
    public void toBasicDBObject() throws Exception {
        Device device = Device.builder().port("8080").ip("127.0.0.1").id("13").name("Test").build();
        BasicDBObject basicDBObject = entityConverter.toBasicDBObject(device);
        assertEquals(basicDBObject.get("port"), "8080");
        assertEquals(basicDBObject.get("ip"), "127.0.0.1");
        assertEquals(basicDBObject.get("name"), "Test");
        assertEquals("13", basicDBObject.get("_id"));
    }

    @Test
    public void toEntity() throws Exception {
        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put("port", "8080");
        searchQuery.put("ip", "127.0.0.1");
        searchQuery.put("name", "Test");
        searchQuery.put("_id", "13");
        Device device = entityConverter.toEntity(searchQuery);
        assertEquals("8080", device.getPort());
        assertEquals("127.0.0.1", device.getIp());
        assertEquals("Test", device.getName());
        assertEquals("13", device.getId());

    }

}