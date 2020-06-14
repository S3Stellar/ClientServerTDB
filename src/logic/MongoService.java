package logic;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import dao.LastIdValueDao;
import dao.services.CrudRepository;
import data.LastIdValue;
import data.Message;

public class MongoService implements CrudRepository<Message, String> {

	private final String CON = "mongodb://nfjk:nfjk1234@cluster0-shard-00-00-6amt4.mongodb.net:27017,cluster0-shard-00-01-6amt4.mongodb.net:27017,cluster0-shard-00-02-6amt4.mongodb.net:27017/test?ssl=true&replicaSet=Cluster0-shard-0&authSource=admin&retryWrites=true&w=majority&journal=true&wtimeoutms=1000";
	private MongoClientURI mongoClientURI;
	private MongoClient mongoClient;
	private MongoDatabase database;
	private MongoCollection<Document> messageCollection;
	private LastIdValueDao lastIdValueDao;

	public MongoService(LastIdValueDao lastIdValueDao) {
		mongoClientURI = new MongoClientURI(CON);
		mongoClient = new MongoClient(mongoClientURI);
		database = mongoClient.getDatabase("test");
		System.out.println("Connection to MongoDB has been established.");
		messageCollection = database.getCollection("myTable");
		this.lastIdValueDao = lastIdValueDao;
	}

	@Override
	public Message[] read(String id) {
		FindIterable<Document> iterDoc = messageCollection.find();
		MongoCursor<Document> dbc = iterDoc.iterator();
		List<Message> msgList = new ArrayList<>();
		while (dbc.hasNext()) {
			try {
				Gson gsonPasrer = new Gson();
				Message msg = gsonPasrer.fromJson(dbc.next().toJson(), Message.class);
				if (msg != null && msg.getId().equals(id)) {
					 msgList.add(msg);
					 return msgList.toArray(new Message[0]);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public Message create(Message t) {
		if (messageCollection.countDocuments() == 0) {
			lastIdValueDao.update("0", lastIdValueDao.save().getId());
		}

		Document newMsg = new Document();
		LastIdValue newId = lastIdValueDao.save();
		t.setId(newId.getId());
		newMsg.put("msg", t.getMsg());
		newMsg.put("id", newId.getId());

		messageCollection.insertOne(newMsg);
		return t;
	}

	@Override
	public void update(String id, Message data) {

		Document query = new Document();
		query.append("id", id);

		Document setData = new Document();
		setData.append("msg", data.getMsg());

		Document update = new Document();
		update.append("$set", setData);
		messageCollection.updateOne(query, update);
	}

	@Override
	public void delete(String id) {
		Document query = new Document();
		query.append("id", id);
		messageCollection.deleteOne(query);
	}
}
