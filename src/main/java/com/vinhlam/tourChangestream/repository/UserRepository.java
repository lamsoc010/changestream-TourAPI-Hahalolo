package com.vinhlam.tourChangestream.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.cloud.storage.Acl.User;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.vinhlam.tourChangestream.entity.PriceTour;

@Repository
public class UserRepository {

	@Autowired
	private MongoDatabase mongoDatabase;
	
	private MongoCollection<User> userTourCollection;
	
	@Autowired
	public void PriceTourService() {
		CodecRegistry cRegistry = CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), 
				CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()));

		userTourCollection = mongoDatabase.getCollection("user", User.class).withCodecRegistry(cRegistry);
	}
	
//	Get All User by topic
	public List<Document> getAllUserByTopic(String topic) {
		List<Document> listDocument = new ArrayList<>();
		
		List<Bson> pipeline = new ArrayList<>();
		Bson match = new Document("$match", new Document("listTopic", new Document("$in", Arrays.asList(topic))));
		pipeline.add(match);
		
		userTourCollection.aggregate(pipeline, Document.class).into(listDocument);
		
		return listDocument;
	}
	
}
