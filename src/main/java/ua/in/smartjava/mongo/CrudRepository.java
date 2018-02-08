package ua.in.smartjava.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.WriteResult;

import org.bson.types.ObjectId;
import org.slf4j.helpers.MessageFormatter;

import java.util.ArrayList;
import java.util.List;

public class CrudRepository<T extends BaseEntity> {

    private final EntityConverter<T> entityConverter;
    private final DBCollection documents;

    public CrudRepository(EntityConverter<T> entityConverter, DBCollection documents) {
        this.documents = documents;
        this.entityConverter = entityConverter;
    }

    public void save(T entity) {
        BasicDBObject document = entityConverter.toDocument(entity);
        documents.insert(document);
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

        documents.update(new BasicDBObject().append("_id", new ObjectId(entity.getId())), updateDocument);
    }

    public List<T> findAll() {
        List<T> collection = new ArrayList<>();
        DBCursor cursor = this.documents.find();
        while (cursor.hasNext()) {
            collection.add(entityConverter.toEntity(cursor.next()));
        }
        return collection;
    }

    public T findById(String id) {
        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put("_id", new ObjectId(id));
        //TODO change to Optional add check when there are more than one value in DB
        return entityConverter.toEntity(this.documents.find(searchQuery).next());
    }

    public long count() {
        return documents.count();
    }

    public void delete(T entity) {
        documents.remove(entityConverter.toBasicDBObject(entity));
    }

    public void deleteAll() {
        documents.remove(new BasicDBObject());
    }

}
