package com.vinhlam.tourChangestream.repository;

import java.text.DateFormat;
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
import org.springframework.stereotype.Repository;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.vinhlam.tourChangestream.entity.DateOpen;

@Repository
public class DateOpenRepository {

	@Autowired
	private MongoDatabase mongoDatabase;
	
	private MongoCollection<DateOpen> dateOpenCollection;
	
	@Autowired
	public void DateOpenService() {
		CodecRegistry cRegistry = CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), 
				CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()));

		dateOpenCollection = mongoDatabase.getCollection("dateOpen", DateOpen.class).withCodecRegistry(cRegistry);
	}
	
	
//	Get DateOpen by tourId and dateStart, dateEnd
	public DateOpen getDateOpenByTourId(String tourId, Date dateStart,Date dateEnd) {
		
		DateOpen dateOpen = new DateOpen();
		List<Bson> pipeline = new ArrayList<>();
//		Bson match = Aggregates.match(Filters.eq("tourId", new ObjectId(id)));
		Bson match = new Document("$match", 
				new Document("$and", Arrays.asList(
						new Document("dateAvailable", new Document("$gte", dateStart )),
						new Document("dateAvailable", new Document("$lte", dateEnd )) ) )
				.append("tourId", new ObjectId(tourId))
				.append("status", 1) );
		Bson project = new Document("$project", 
				new Document("tourId", new Document("$toString", "$tourId") )
				.append("_id", 0)
				.append("dateAvailable", 1)
				.append("status", 1) );
		pipeline.add(match);
		pipeline.add(project);
		
		dateOpen = dateOpenCollection.aggregate(pipeline).first();
		return dateOpen;
	}
	
}
