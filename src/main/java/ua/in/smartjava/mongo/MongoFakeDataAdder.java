package ua.in.smartjava.mongo;

import java.time.LocalDateTime;

import lombok.extern.slf4j.Slf4j;
import ua.in.smartjava.domain.device.Device;
import ua.in.smartjava.domain.logrecord.LogRecord;
import ua.in.smartjava.snakeyaml.ConfigService;

@Slf4j
public class MongoFakeDataAdder {

    public static void main(String[] args) {
        final ConfigService configuration = new ConfigService();
        MongoData mongoData = new MongoData(configuration.getMongoConfig());

        CrudRepository<Device> deviceRepository = mongoData.getRepository(Device.class);
        CrudRepository<LogRecord> logRecordCrudRepository = mongoData.getRepository(LogRecord.class);
        /*
        Device kitchen = Device.builder()
                .ip("192.168.1.6")
                .port("5211")
                .name("kitchen")
                .build();

        Device garage = Device.builder()
                .ip("192.168.1.6")
                .port("5212")
                .name("garage")
                .build();

        Device room = Device.builder()
                .ip("192.168.1.6")
                .port("5213")
                .name("room")
                .build();

        deviceRepository.save(kitchen);
        deviceRepository.save(garage);
        deviceRepository.save(room);
        */
        deviceRepository.save(Device.builder()
                .name("test")
                .build());

        logRecordCrudRepository.save(LogRecord.builder().record("REcord").localDateTime(LocalDateTime.now()).build());

        deviceRepository.findAll().forEach(dev -> log.info("device: {}", dev));
//        deviceRepository.deleteAll();
        log.info("devices in DB: " + deviceRepository.count());
    }
}
