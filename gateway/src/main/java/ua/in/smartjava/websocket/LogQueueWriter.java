package ua.in.smartjava.websocket;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import org.bson.types.ObjectId;
import org.eclipse.jetty.websocket.api.Session;

import java.util.Date;
import java.util.List;

public class LogQueueWriter implements Runnable {
    private List<Session> sessions;
    private DBCursor cursor;
    private boolean process = true;
    private ObjectId latestObjectId = new ObjectId(new Date());
    DBCollection collection;

    public LogQueueWriter(DBCollection collection, List<Session> sessions) {
        this.sessions = sessions;
        this.collection = collection;
        reloadCollectionData();
        System.out.println("== open cursor ==");
    }

    @Override
    public void run() {
        System.out.println("Waiting for events");
        while (process) {
            if (cursor.hasNext()) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                DBObject obj = cursor.next();
                latestObjectId = (ObjectId) obj.get("_id");
                send(String.valueOf(obj));
                System.out.println(obj);

            } else {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                reloadCollectionData();
            }
        }
    }

    //TODO move to CrudRepository
    private void reloadCollectionData() {
        BasicDBObject query = new BasicDBObject();
        query.put("_id", new BasicDBObject("$gt", latestObjectId));
        cursor = collection.find(query);
    }

    private void send(String message) {
        sessions.stream().filter(Session::isOpen)
                .forEach(session -> {
                    try {
                        session.getRemote().sendString(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }
}
