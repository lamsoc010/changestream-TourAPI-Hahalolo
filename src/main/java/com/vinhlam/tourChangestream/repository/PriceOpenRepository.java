package com.vinhlam.tourChangestream.repository;

import java.text.ParseException;
import java.util.Date;

import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import com.vinhlam.tourChangestream.entity.PriceOpen;

@Repository
public class PriceOpenRepository {
	@Autowired
	private MongoDatabase mongoDatabase;
	
	private MongoCollection<PriceOpen> priceOpenCollection;
	
	
	@Autowired
	public void DateOpenService() {
		CodecRegistry cRegistry = CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), 
				CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()));

		priceOpenCollection = mongoDatabase.getCollection("priceOpen", PriceOpen.class).withCodecRegistry(cRegistry);
	}
	
	
//	Delete PricetOpen By TourId And DateOpen
	public DeleteResult deletePriceOpenByTourIdAndDateOpen(String tourId, Date date) {

		Bson match = new Document("tourId", tourId).append("dateOpen", date);
		DeleteResult dr = priceOpenCollection.deleteOne(match);
		
		return dr;
	}
	
//	Delete PricetOpen By TourId And DateOpen
	public InsertOneResult insertPriceOpen(PriceOpen priceOpen) {

		InsertOneResult ir = priceOpenCollection.insertOne(priceOpen);
		
		return ir;
	}
}
