package ua.in.smartjava.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import org.bson.types.ObjectId;
import org.slf4j.helpers.MessageFormatter;

import java.util.ArrayList;
import java.util.List;

public class CrudRepository<T extends BaseEntity> {

    private final EntityConverter<T> entityConverter;
    private final DBCollection collection;

    public CrudRepository(EntityConverter<T> entityConverter, DBCollection collection) {
        this.collection = collection;
        this.entityConverter = entityConverter;
    }

    public void save(T entity) {
        BasicDBObject document = entityConverter.toDocument(entity);
        collection.insert(document);
        ObjectId id = (ObjectId)document.get( "_id" );
        entity.setId(id.toString());
    }

    public void update(T entity, String id) {

        if (!id.equalsIgnoreCase(entity.getId())) {
            throw new RuntimeException(MessageFormatter.format("Id {} in url does not match entity id: {}", id, entity.getId()).getMessage());
        }
        BasicDBObject document = entityConverter.toDocument(entity);
        BasicDBObject updateDocument = new BasicDBObject();
        updateDocument.append("$set", document);

        collection.update(new BasicDBObject().append("_id", new ObjectId(entity.getId())), updateDocument);
    }

    public List<T> findAll() {
        List<T> collection = new ArrayList<>();
        DBCursor cursor = this.collection.find();
        while (cursor.hasNext()) {
            collection.add(entityConverter.toEntity(cursor.next()));
        }
        return collection;
    }

    public T findById(String id) {
        //TODO change to Optional add check when there are more than one value in DB
        return entityConverter.toEntity(getById(id));
    }
    private DBObject getById(String id) {
        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put("_id", new ObjectId(id));
        return this.collection.find(searchQuery).next();
    }

    public long count() {
        return collection.count();
    }

    //TODO check this method is correct
    public void delete(T entity) {
        collection.remove(entityConverter.toBasicDBObject(entity));
    }

    public void delete(String id) {
        collection.remove(getById(id));
    }

    public void deleteAll() {
        collection.remove(new BasicDBObject());
    }

}
