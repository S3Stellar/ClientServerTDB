package dao;
import org.bson.Document;

import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import dao.services.LastIdSave;
import data.LastIdValue;

public class LastIdValueDao implements LastIdSave<LastIdValue, Long> {

	private final String CON = "mongodb://nfjk:nfjk1234@cluster0-shard-00-00-6amt4.mongodb.net:27017,cluster0-shard-00-01-6amt4.mongodb.net:27017,cluster0-shard-00-02-6amt4.mongodb.net:27017/test?ssl=true&replicaSet=Cluster0-shard-0&authSource=admin&retryWrites=true&w=majority&j=true&wtimeout=1000";
	private MongoClientURI mongoClientURI;
	private MongoClient mongoClient;
	private MongoDatabase database;
	private MongoCollection<Document> idCollection;

	public LastIdValueDao() {
		mongoClientURI = new MongoClientURI(CON);
		mongoClient = new MongoClient(mongoClientURI);
		database = mongoClient.getDatabase("test");
		idCollection = database.getCollection("lastIdValue");
	}

	@Override
	public LastIdValue save() {
		FindIterable<Document> iterDoc = idCollection.find();
		MongoCursor<Document> dbc = iterDoc.iterator();

		while (dbc.hasNext()) {
			try {
				Gson gsonPasrer = new Gson();
				LastIdValue lastId = gsonPasrer.fromJson(dbc.next().toJson(), LastIdValue.class);
				if (lastId != null/* && lastId.getId().equals(id) */) {
					Long newId = Long.parseLong(lastId.getId()) + 1;
					update(newId.toString(), lastId.getId());
					lastId.setId(newId.toString());
					return lastId;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return create(new LastIdValue());
	}

	public LastIdValue create(LastIdValue t) {
		Document nextId = new Document();
		nextId.put("id", t.getId());
		idCollection.insertOne(nextId);
		return t;
	}

	public void update(String newId, String lastId) {

		Document query = new Document();
		query.append("id", lastId);

		Document setData = new Document();
		setData.append("id", newId);

		Document update = new Document();
		update.append("$set", setData);
		idCollection.updateOne(query, update);
	}
}
