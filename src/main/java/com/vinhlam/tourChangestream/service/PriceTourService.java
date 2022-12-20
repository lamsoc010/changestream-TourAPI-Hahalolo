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
import com.mongodb.client.model.Filters;
import com.vinhlam.tourChangestream.entity.PriceTour;

@Service
public class PriceTourService {
//	@Autowired
//	private PriceTourRepository priceTourRepository;
	
	@Autowired
	private MongoDatabase mongoDatabase;
	
	private MongoCollection<PriceTour> priceTourCollection;
	
	@Autowired
	public void PriceTourService() {
		CodecRegistry cRegistry = CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), 
				CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()));

		priceTourCollection = mongoDatabase.getCollection("priceTour", PriceTour.class).withCodecRegistry(cRegistry);
		
		
	}
	
	public PriceTour getPriceTourByTourId(String id, Date date) throws ParseException {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		PriceTour priceTour = new PriceTour();
		List<Bson> pipeline = new ArrayList<>();
//		Bson match = Aggregates.match(Filters.eq("tourId", new ObjectId(id)));
		Bson match = new Document("$match", 
				new Document("$and", Arrays.asList(
						new Document("dateApplyStart", new Document("$lte", date )),
						new Document("dateApplyEnd", new Document("$gte", date )) ) )
				.append("tourId", new ObjectId(id)));
		Bson project = new Document("$project", 
				new Document("tourId", new Document("$toString", "$tourId") )
				.append("_id", 0)
				.append("price", 1)
				.append("currency", 1)
				.append("dateApplyStart", 1)
				.append("dateApplyEnd", 1) );
		pipeline.add(match);
		pipeline.add(project);
		priceTour = priceTourCollection.aggregate(pipeline).first();
		return priceTour;
	}
}
