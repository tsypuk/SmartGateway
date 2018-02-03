package ua.in.smartjava.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

import org.bson.types.ObjectId;

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
