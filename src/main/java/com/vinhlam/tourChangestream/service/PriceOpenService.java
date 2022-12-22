package com.vinhlam.tourChangestream.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.internal.bulk.DeleteRequest;
import com.vinhlam.tourChangestream.entity.DateOpen;
import com.vinhlam.tourChangestream.entity.PriceOpen;
import com.vinhlam.tourChangestream.repository.PriceOpenRepository;


@Service
public class PriceOpenService {

	@Autowired
	private MongoDatabase mongoDatabase;
	
	private MongoCollection<PriceOpen> priceOpenCollection;
	
	@Autowired
	private PriceOpenRepository priceOpenRepository;
	
	@Autowired
	public void DateOpenService() {
		CodecRegistry cRegistry = CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), 
				CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()));

		priceOpenCollection = mongoDatabase.getCollection("priceOpen", PriceOpen.class).withCodecRegistry(cRegistry);
	}

	public boolean deletePriceOpenByTourIdAndDateOpen(String tourId, Date date) throws ParseException {

		Bson match = new Document("tourId", tourId).append("dateOpen", date);
		DeleteResult dr = priceOpenCollection.deleteOne(match);
		
		if(dr.getDeletedCount() != 0) {
			return true; //Xoá thành công
		} else {
			return false; //Xoá thất bại
		}
	}
	
	
	public boolean insertPriceOpen(PriceOpen priceOpen) {
		PriceOpen priceOpenResult = priceOpenRepository.save(priceOpen);
		if(priceOpenResult == null) {
			return false;
		} else {
			return true;
		}
	}
}
