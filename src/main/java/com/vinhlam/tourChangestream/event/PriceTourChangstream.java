package com.vinhlam.tourChangestream.event;

import java.text.ParseException;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mongodb.client.MongoChangeStreamCursor;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import com.mongodb.client.model.changestream.FullDocument;

@Service
public class PriceTourChangstream {

	@Autowired
	private MongoDatabase mongoDatabase;
	
//	private MongoCollection<Document> dateOpenCollection;
	private MongoCollection<Document> priceTourCollection;
	
////Khai báo các hằng số collection
//	private static final String DATAOPEN_COLLECTION = "dateOpen";
	private static final String PRICETOUR_COLLECTION = "priceTour";
	
//	@Autowired
//	private DateOpenEvent dateOpenEvent;
	@Autowired
	private PriceTourEvent priceTourEvent;
	
	
	@Autowired
	public void PriceTourChangstream() {
		priceTourCollection = mongoDatabase.getCollection("priceTour");
		
//		Cho mỗi changestream chạy trên 1 luồng khác nhau thì mới lắng nghe từng collection riêng được
		new Thread(() -> {
			MongoChangeStreamCursor<ChangeStreamDocument<Document>> cursor = priceTourCollection.watch().fullDocument(FullDocument.UPDATE_LOOKUP).cursor();
			
			
			while(cursor.hasNext()) { //Lưu ý đây phải để while nó mới chạy bắt nhiều lần được, còn không chạy được 1 lần thôi
				ChangeStreamDocument<Document> next = cursor.next();
				if(next.getOperationTypeString().equalsIgnoreCase("delete")) {
					System.out.println("Lắng nghe sự kiện delete của collection: " +  PRICETOUR_COLLECTION);
			    }
			    
			    if(next.getOperationTypeString().equalsIgnoreCase("update")) {
			    	
			    	System.out.println("Lắng nghe sự kiện update của collection: " +  PRICETOUR_COLLECTION);
			    }
			    
			    if(next.getOperationTypeString().equalsIgnoreCase("insert")) {
			    	System.out.println("Lắng nghe sự kiện insert của collection: " +  PRICETOUR_COLLECTION);
			    	try {
						priceTourEvent.handleInsertPriceTour(next);
					} catch (JsonProcessingException | ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			    }
			}
		}).start();
		
	}
}
