package com.vinhlam.tourChangestream.event;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.net.http.HttpRequest.BodyPublishers;

import org.bson.BsonObjectId;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoChangeStreamCursor;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import com.mongodb.client.model.changestream.FullDocument;
import com.mongodb.client.model.changestream.FullDocumentBeforeChange;
import com.vinhlam.tourChangestream.entity.DateOpen;
import com.vinhlam.tourChangestream.entity.PriceOpen;
import com.vinhlam.tourChangestream.entity.PriceTour;
import com.vinhlam.tourChangestream.service.PriceOpenService;
import com.vinhlam.tourChangestream.service.PriceTourService;
import com.vinhlam.tourChangestream.service.TokenUserService;

@Service
public class EventAllChangestream {
	@Autowired
	private MongoDatabase mongoDatabase;

//	Khởi tạo các class chứa event function xử lý sau khi lắng nghe sự kiện
	@Autowired
	private DateOpenEvent dateOpenEvent;
	@Autowired
	private PriceTourEvent priceTourEvent;
	
	
//	Khai báo các hằng số collection
	private static final String DATAOPEN_COLLECTION = "dateOpen";
	private static final String PRICETOUR_COLLECTION = "priceTour";
	
//	private MongoCollection<DateOpen> mongoCollection;
//	private MongoCollection<PriceTour> priceTourCollection;
	
	
	@Autowired
	public void DateOpenChangestream() throws JsonMappingException, JsonProcessingException, ParseException {
		changeStream();
	}
	
	public void changeStream() throws JsonMappingException, JsonProcessingException, ParseException {
//		CodecRegistry pojoCodecRegistry = org.bson.codecs.configuration.CodecRegistries.fromRegistries(
//				MongoClientSettings.getDefaultCodecRegistry(), org.bson.codecs.configuration.CodecRegistries
//						.fromProviders(PojoCodecProvider.builder().automatic(true).build()));
		
//		mongoCollection = mongoDatabase.getCollection("dateOpen", DateOpen.class).withCodecRegistry(pojoCodecRegistry);
//		priceTourCollection = mongoDatabase.getCollection("pricetTour", PriceTour.class).withCodecRegistry(pojoCodecRegistry);
		
//		MongoChangeStreamCursor<ChangeStreamDocument<DateOpen>> cursor = mongoCollection.watch().fullDocument(FullDocument.UPDATE_LOOKUP).cursor();
//		MongoChangeStreamCursor<ChangeStreamDocument<PriceTour>> cursorTour = priceTourCollection.watch().fullDocument(FullDocument.UPDATE_LOOKUP).cursor();
		
//		Chỉ lắng nghe sự kiện delete, insert, update và chỉ lắng nghe trên 2 bảng dateOpen, priceTourOpen
		List<Bson> pipeline = new ArrayList<>();
		Bson match = new Document("$match", 
				new Document("operationType", new Document("$in", Arrays.asList("delete","insert","update")))
							.append("ns.coll", new Document("$in", Arrays.asList(DATAOPEN_COLLECTION, PRICETOUR_COLLECTION))) );
		pipeline.add(match);
		
//		Lắng nghe luôn cả database đó, 
//		Nhưng kết hợp với pipeline trỏ tới điều kiện chỉ lắng nghe trên những bảng và những phương thức được quy định thôi
		MongoChangeStreamCursor<ChangeStreamDocument<Document>> cursor = mongoDatabase.watch(pipeline).fullDocumentBeforeChange(FullDocumentBeforeChange.WHEN_AVAILABLE).fullDocument(FullDocument.UPDATE_LOOKUP).cursor();
		
		while(cursor.hasNext()) { //Lưu ý đây phải để while nó mới chạy bắt nhiều lần được, còn không chạy được 1 làn thôi
			ChangeStreamDocument<Document> next = cursor.next();
//			System.err.println(next);
			//Check xem có phải là collection dateOpen hay không?
			if(next.getNamespace().getCollectionName().equalsIgnoreCase(DATAOPEN_COLLECTION)) {
				
				if(next.getOperationTypeString().equalsIgnoreCase("delete")) {
			    	System.out.println("Lắng nghe sự kiện delete của collection: " +  DATAOPEN_COLLECTION);
			    }
			    
			    if(next.getOperationTypeString().equalsIgnoreCase("update")) {
			    	System.out.println("Lắng nghe sự kiện update của collection: " +  DATAOPEN_COLLECTION);
			    	dateOpenEvent.handleUpdateDateOpen(next);
			    }
			    
			    if(next.getOperationTypeString().equalsIgnoreCase("insert")) {
			    	System.out.println("Lắng nghe sự kiện insert của collection: " +  DATAOPEN_COLLECTION);
			    	dateOpenEvent.handleInsertDateOpen(next);
			    }
			//Check xem có phải là collection priceTour hay không?
			} else if(next.getNamespace().getCollectionName().equalsIgnoreCase(PRICETOUR_COLLECTION)) {
				if(next.getOperationTypeString().equalsIgnoreCase("delete")) {
					System.out.println("Lắng nghe sự kiện delete của collection: " +  PRICETOUR_COLLECTION);
			    }
			    
			    if(next.getOperationTypeString().equalsIgnoreCase("update")) {
			    	
			    	System.out.println("Lắng nghe sự kiện update của collection: " +  PRICETOUR_COLLECTION);
			    }
			    
			    if(next.getOperationTypeString().equalsIgnoreCase("insert")) {
			    	System.out.println("Lắng nghe sự kiện insert của collection: " +  PRICETOUR_COLLECTION);
			    	priceTourEvent.handleInsertPriceTour(next);
			    }
			}
			
//		    cursor.close();
		}
		
		
	}
	

}
