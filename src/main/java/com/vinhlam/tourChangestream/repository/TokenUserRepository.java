package com.vinhlam.tourChangestream.repository;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.cloud.storage.Acl.User;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.vinhlam.tourChangestream.entity.TokenUser;

@Repository
public class TokenUserRepository {

	@Autowired
	private MongoDatabase mongoDatabase;
	
	private MongoCollection<TokenUser> tokenUserTourCollection;
	
	@Autowired
	public void PriceTourService() {
		CodecRegistry cRegistry = CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), 
				CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()));

		tokenUserTourCollection = mongoDatabase.getCollection("tokenUser", TokenUser.class).withCodecRegistry(cRegistry);
	}
	
//	Get list tokenUser by listIdUser
	public List<Document> getListTokenByListUser(List<ObjectId> listIdUser) {
		List<Document> listTokenUser = new ArrayList<>();
		List<Bson> pipeline = new ArrayList<>();
		Bson match = new Document("$match", new Document("idUser", new Document("$in", listIdUser)));
		Bson project = new Document("$project", new Document("_id", 0).append("listToken", 1) );
		
		pipeline.add(match);
		pipeline.add(project);
		
		tokenUserTourCollection.aggregate(pipeline, Document.class).into(listTokenUser);
		
		return listTokenUser;
	}
}
