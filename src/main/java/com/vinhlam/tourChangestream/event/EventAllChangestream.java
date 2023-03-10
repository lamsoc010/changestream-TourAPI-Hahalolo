//package com.vinhlam.tourChangestream.event;
//
//import java.io.IOException;
//import java.net.URI;
//import java.net.http.HttpClient;
//import java.net.http.HttpRequest;
//import java.net.http.HttpResponse;
//import java.text.ParseException;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.concurrent.CompletableFuture;
//import java.util.concurrent.ExecutionException;
//import java.net.http.HttpRequest.BodyPublishers;
//
//import org.bson.BsonObjectId;
//import org.bson.Document;
//import org.bson.codecs.configuration.CodecRegistry;
//import org.bson.codecs.pojo.PojoCodecProvider;
//import org.bson.conversions.Bson;
//import org.bson.types.ObjectId;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.stereotype.Controller;
//import org.springframework.stereotype.Service;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.JsonMappingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.mongodb.MongoClientSettings;
//import com.mongodb.client.MongoChangeStreamCursor;
//import com.mongodb.client.MongoClients;
//import com.mongodb.client.MongoCollection;
//import com.mongodb.client.MongoDatabase;
//import com.mongodb.client.model.Aggregates;
//import com.mongodb.client.model.Filters;
//import com.mongodb.client.model.changestream.ChangeStreamDocument;
//import com.mongodb.client.model.changestream.FullDocument;
//import com.mongodb.client.model.changestream.FullDocumentBeforeChange;
//import com.vinhlam.tourChangestream.entity.DateOpen;
//import com.vinhlam.tourChangestream.entity.PriceOpen;
//import com.vinhlam.tourChangestream.entity.PriceTour;
//import com.vinhlam.tourChangestream.service.PriceOpenService;
//import com.vinhlam.tourChangestream.service.PriceTourService;
//import com.vinhlam.tourChangestream.service.TokenUserService;
//
//@Service
//public class EventAllChangestream {
//	@Autowired
//	private MongoDatabase mongoDatabase;
//
////	Kh???i t???o c??c class ch???a event function x??? l?? sau khi l???ng nghe s??? ki???n
//	@Autowired
//	private DateOpenEvent dateOpenEvent;
//	@Autowired
//	private PriceTourEvent priceTourEvent;
//	
//	
////	Khai b??o c??c h???ng s??? collection
//	private static final String DATAOPEN_COLLECTION = "dateOpen";
//	private static final String PRICETOUR_COLLECTION = "priceTour";
//	
////	private MongoCollection<DateOpen> mongoCollection;
////	private MongoCollection<PriceTour> priceTourCollection;
//	
//	
//	@Autowired
//	public void DateOpenChangestream() throws JsonMappingException, JsonProcessingException, ParseException, InterruptedException, ExecutionException {
//		changeStream();
//	}
//	
//	public void changeStream() throws JsonMappingException, JsonProcessingException, ParseException, InterruptedException, ExecutionException {
////		CodecRegistry pojoCodecRegistry = org.bson.codecs.configuration.CodecRegistries.fromRegistries(
////				MongoClientSettings.getDefaultCodecRegistry(), org.bson.codecs.configuration.CodecRegistries
////						.fromProviders(PojoCodecProvider.builder().automatic(true).build()));
//		
////		mongoCollection = mongoDatabase.getCollection("dateOpen", DateOpen.class).withCodecRegistry(pojoCodecRegistry);
////		priceTourCollection = mongoDatabase.getCollection("pricetTour", PriceTour.class).withCodecRegistry(pojoCodecRegistry);
//		
////		MongoChangeStreamCursor<ChangeStreamDocument<DateOpen>> cursor = mongoCollection.watch().fullDocument(FullDocument.UPDATE_LOOKUP).cursor();
////		MongoChangeStreamCursor<ChangeStreamDocument<PriceTour>> cursorTour = priceTourCollection.watch().fullDocument(FullDocument.UPDATE_LOOKUP).cursor();
//		
////		Ch??? l???ng nghe s??? ki???n delete, insert, update v?? ch??? l???ng nghe tr??n 2 b???ng dateOpen, priceTourOpen
//		List<Bson> pipeline = new ArrayList<>();
//		Bson match = new Document("$match", 
//				new Document("operationType", new Document("$in", Arrays.asList("delete","insert","update")))
//							.append("ns.coll", new Document("$in", Arrays.asList(DATAOPEN_COLLECTION, PRICETOUR_COLLECTION))) );
//		pipeline.add(match);
//		
////		L???ng nghe lu??n c??? database ????, 
////		Nh??ng k???t h???p v???i pipeline tr??? t???i ??i???u ki???n ch??? l???ng nghe tr??n nh???ng b???ng v?? nh???ng ph????ng th???c ???????c quy ?????nh th??i
//		MongoChangeStreamCursor<ChangeStreamDocument<Document>> cursor = mongoDatabase.watch(pipeline).fullDocumentBeforeChange(FullDocumentBeforeChange.WHEN_AVAILABLE).fullDocument(FullDocument.UPDATE_LOOKUP).cursor();
//		
//		while(cursor.hasNext()) { //L??u ?? ????y ph???i ????? while n?? m???i ch???y b???t nhi???u l???n ???????c, c??n kh??ng ch???y ???????c 1 l??n th??i
//			ChangeStreamDocument<Document> next = cursor.next();
////			System.err.println(next);
////			//Check xem c?? ph???i l?? collection dateOpen hay kh??ng?
////			if(next.getNamespace().getCollectionName().equalsIgnoreCase(DATAOPEN_COLLECTION)) {
////				
////				if(next.getOperationTypeString().equalsIgnoreCase("delete")) {
////			    	System.out.println("L???ng nghe s??? ki???n delete c???a collection: " +  DATAOPEN_COLLECTION);
////			    }
////			    
////			    if(next.getOperationTypeString().equalsIgnoreCase("update")) {
////			    	System.out.println("L???ng nghe s??? ki???n update c???a collection: " +  DATAOPEN_COLLECTION);
////			    	dateOpenEvent.handleUpdateDateOpen(next);
////			    }
////			    
////			    if(next.getOperationTypeString().equalsIgnoreCase("insert")) {
////			    	System.out.println("L???ng nghe s??? ki???n insert c???a collection: " +  DATAOPEN_COLLECTION);
////			    	dateOpenEvent.handleInsertDateOpen(next);
////			    }
////			//Check xem c?? ph???i l?? collection priceTour hay kh??ng?
////			} else if(next.getNamespace().getCollectionName().equalsIgnoreCase(PRICETOUR_COLLECTION)) {
////				if(next.getOperationTypeString().equalsIgnoreCase("delete")) {
////					System.out.println("L???ng nghe s??? ki???n delete c???a collection: " +  PRICETOUR_COLLECTION);
////			    }
////			    
////			    if(next.getOperationTypeString().equalsIgnoreCase("update")) {
////			    	
////			    	System.out.println("L???ng nghe s??? ki???n update c???a collection: " +  PRICETOUR_COLLECTION);
////			    }
////			    
////			    if(next.getOperationTypeString().equalsIgnoreCase("insert")) {
////			    	System.out.println("L???ng nghe s??? ki???n insert c???a collection: " +  PRICETOUR_COLLECTION);
////			    	priceTourEvent.handleInsertPriceTour(next);
////			    }
////			}
//			
//			CompletableFuture<Void> futureDateOpen = CompletableFuture.runAsync(() -> {
////				//Check xem c?? ph???i l?? collection dateOpen hay kh??ng?
//				if(next.getNamespace().getCollectionName().equalsIgnoreCase(DATAOPEN_COLLECTION)) {
//					
//					if(next.getOperationTypeString().equalsIgnoreCase("delete")) {
//				    	System.out.println("L???ng nghe s??? ki???n delete c???a collection: " +  DATAOPEN_COLLECTION);
//				    }
//				    
//				    if(next.getOperationTypeString().equalsIgnoreCase("update")) {
//				    	System.out.println("L???ng nghe s??? ki???n update c???a collection: " +  DATAOPEN_COLLECTION);
//				    	try {
//							dateOpenEvent.handleUpdateDateOpen(next);
//						} catch (ParseException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//				    }
//				    
//				    if(next.getOperationTypeString().equalsIgnoreCase("insert")) {
//				    	System.out.println("L???ng nghe s??? ki???n insert c???a collection: " +  DATAOPEN_COLLECTION);
//				    	try {
//							dateOpenEvent.handleInsertDateOpen(next);
//						} catch (JsonProcessingException | ParseException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//				    }
//				}
//			});
//			
//			CompletableFuture<Void> futurePriceOpen = CompletableFuture.runAsync(() -> {
//				//Check xem c?? ph???i l?? collection priceTour hay kh??ng?
//				if(next.getNamespace().getCollectionName().equalsIgnoreCase(PRICETOUR_COLLECTION)) {
//					if(next.getOperationTypeString().equalsIgnoreCase("delete")) {
//						System.out.println("L???ng nghe s??? ki???n delete c???a collection: " +  PRICETOUR_COLLECTION);
//				    }
//				    
//				    if(next.getOperationTypeString().equalsIgnoreCase("update")) {
//				    	
//				    	System.out.println("L???ng nghe s??? ki???n update c???a collection: " +  PRICETOUR_COLLECTION);
//				    }
//				    
//				    if(next.getOperationTypeString().equalsIgnoreCase("insert")) {
//				    	System.out.println("L???ng nghe s??? ki???n insert c???a collection: " +  PRICETOUR_COLLECTION);
//				    	try {
//							priceTourEvent.handleInsertPriceTour(next);
//						} catch (JsonProcessingException | ParseException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//				    }
//				}
//			});
//			
//			CompletableFuture<Void> futureAllOf = CompletableFuture.allOf(futureDateOpen, futurePriceOpen);
//			
//			CompletableFuture<String> resutFutureAllOf = futureAllOf.handle((res, ex) -> {
//				if (ex != null) {
//	                System.out.println("C?? l???i khi l???ng nghe: " + ex.getMessage());
//	                return "L???i!";
//	            }
//	            return "Ho??n th??nh l???ng nghe" ;
//			});
//			
//			System.out.println(resutFutureAllOf.get());
//			
////		    cursor.close();
//		}
//		
//		
//	}
//	
//
//}
