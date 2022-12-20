//package com.vinhlam.tourChangestream.event;
//
//import java.text.ParseException;
//import java.util.Arrays;
//import java.util.List;
//
//import org.bson.BsonObjectId;
//import org.bson.codecs.configuration.CodecRegistry;
//import org.bson.codecs.pojo.PojoCodecProvider;
//import org.bson.conversions.Bson;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.JsonMappingException;
//import com.mongodb.MongoClientSettings;
//import com.mongodb.client.MongoChangeStreamCursor;
//import com.mongodb.client.MongoCollection;
//import com.mongodb.client.MongoDatabase;
//import com.mongodb.client.model.Aggregates;
//import com.mongodb.client.model.Filters;
//import com.mongodb.client.model.changestream.ChangeStreamDocument;
//import com.mongodb.client.model.changestream.FullDocument;
//import com.vinhlam.tourChangestream.entity.DateOpen;
//import com.vinhlam.tourChangestream.entity.PriceOpen;
//import com.vinhlam.tourChangestream.entity.PriceTour;
//import com.vinhlam.tourChangestream.service.PriceOpenService;
//import com.vinhlam.tourChangestream.service.PriceTourService;
//
//@Service
//public class PriceTourChangestream {
//	@Autowired
//	private MongoDatabase mongoDatabase;
//	
//	@Autowired
//	private PriceTourService priceTourService;
//	
//	@Autowired
//	private PriceOpenService priceOpenService;
//	
//	private MongoCollection<PriceTour> mongoCollection;
//	
//	
//	@Autowired
//	public void PriceTourChangestream() throws JsonMappingException, JsonProcessingException, ParseException {
//		changeStream();
//	}
//	
//	public void changeStream() throws JsonMappingException, JsonProcessingException, ParseException {
//		CodecRegistry pojoCodecRegistry = org.bson.codecs.configuration.CodecRegistries.fromRegistries(
//				MongoClientSettings.getDefaultCodecRegistry(), org.bson.codecs.configuration.CodecRegistries
//						.fromProviders(PojoCodecProvider.builder().automatic(true).build()));
//		
//		mongoCollection = mongoDatabase.getCollection("priceTour", PriceTour.class).withCodecRegistry(pojoCodecRegistry);
//		
////		List<Bson> pipeline = Arrays.asList(
////                Aggregates.match(
////                        Filters.in("operationType",
////                                Arrays.asList("insert", "update", "delete"))));
////		MongoChangeStreamCursor<ChangeStreamDocument<PriceTour>> cursor = mongoCollection.watch().fullDocument(FullDocument.UPDATE_LOOKUP).cursor();
////		
////		if(cursor.hasNext()) {
////			ChangeStreamDocument<PriceTour> next = cursor.next();
////			System.err.println(next);
////			if(next.getOperationTypeString().equalsIgnoreCase("delete")) {
////		    	System.err.println("Đây là phương thức delete: " + next.getOperationTypeString());
////		    	
////		    }
////		    
////		    if(next.getOperationTypeString().equalsIgnoreCase("update")) {
////		    	System.out.println("Update");
////		    }
////		    
////		    if(next.getOperationTypeString().equalsIgnoreCase("insert")) {
////		    	System.err.println("Insert");
////		    	handleInsertPriceTour(next);
////		    }
//////		    cursor.close();
////		}
//	}
//	
//	public void handleInsertPriceTour(ChangeStreamDocument<PriceTour> next) throws JsonMappingException, JsonProcessingException, ParseException {
//		
//////		Get id
////		BsonObjectId idString = (BsonObjectId)next.getDocumentKey().get("_id");
////		System.err.println("Id String: " + idString.getValue());
////		
//////		1. Get data after insert dateOpen
////			//Input: new Data DateOpen
////			//Output: dateOpen
////		PriceTour priceTour = next.getFullDocument();
////		String idTour = dateOpen.getTourId().toString();
////		System.err.println("status:" + dateOpen.getStatus());
////		
//////		2. Get pricetTour By idTour
////			//Input: idTour, date
////			//Output: priceTour(cụ thể chỉ cần price và currency)
////		PriceTour priceTour = priceTourService.getPriceTourByTourId(idTour, dateOpen.getDateAvailable());
////		
////
//////		Nếu như priceTour == null tức là không có giá phù hợp nằm trong khoảng thời gian đó thì cho price mặc định là 1 triệu
////		Float price;
////		String currency;
////		if(priceTour == null) {
////			price = (float)1000000;
////			currency = "VND";
////		} else {
////			price = priceTour.getPrice();
////			currency = priceTour.getCurrency();
////		}
////		
//////		3. Insert priceOpen
////			//Input: idTour, price, currency, dateOpen
////		PriceOpen priceOpen = new PriceOpen();
////		priceOpen.setTourId(idTour.toString());
////		priceOpen.setDateOpen(dateOpen.getDateAvailable());
////		priceOpen.setCurrency(currency);
////		priceOpen.setPrice(price);
////		priceOpenService.insertPriceOpen(priceOpen);
//		
//
//		
//	}
//}
