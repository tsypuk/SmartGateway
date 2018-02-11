package ua.in.smartjava.domain.logrecord;

import java.time.LocalDateTime;

import ua.in.smartjava.mongo.CrudRepository;

//TODO create methods to log discovery, log, other events.
public class LogService {

    private CrudRepository<LogRecord> logRecordCrudRepository;

    public LogService(CrudRepository<LogRecord> repository) {
        this.logRecordCrudRepository = repository;
    }

    public void logAction(String action) {
        logRecordCrudRepository.save(LogRecord.builder().record(action).localDateTime(LocalDateTime.now().toString()).build());
    }

}
