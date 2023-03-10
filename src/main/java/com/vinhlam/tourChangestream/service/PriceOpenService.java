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

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;
import com.google.firebase.messaging.Notification.Builder;
import com.google.firebase.messaging.SendResponse;
import com.google.firebase.messaging.WebpushConfig;
import com.google.firebase.messaging.WebpushNotification;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.internal.bulk.DeleteRequest;
import com.vinhlam.tourChangestream.entity.DateOpen;
import com.vinhlam.tourChangestream.entity.PriceOpen;
import com.vinhlam.tourChangestream.repository.PriceOpenRepository;



@Service
public class PriceOpenService {

	@Autowired
	private PriceOpenRepository priceOpenRepository;
	@Autowired
	private TokenUserService tokenUserService;
	
	private static final String TOPIC= "tour";
	
//	@Autowired
//	private FirebaseApp firebaseApp;
	
	public PriceOpenService() {
		
	}
	
	public boolean deletePriceOpenByTourIdAndDateOpen(String tourId, Date date) throws ParseException {

		try {
			DeleteResult dr = priceOpenRepository.deletePriceOpenByTourIdAndDateOpen(tourId, date);
			
			if(dr.getDeletedCount() != 0) {
				return true; //Xo?? th??nh c??ng
			} else {
				return false; //Xo?? th???t b???i
			}
		} catch (Exception e) {
			return false; //L???i
		}
		
	}
	
	
	public boolean insertPriceOpen(PriceOpen priceOpen) {
		try {
			InsertOneResult ir = priceOpenRepository.insertPriceOpen(priceOpen);
			
//			S??? ki???n n??o d???n ?????n c??i insertPriceOpen n??y th?? kh??ng c???n bi???t
//			Ch??? c???n bi???t l?? khi n??o priceOpen ???????c insert v??o db th?? g???i th??ng b??o cho client
			
//			Get List Token device by Topic 
			List<String> listToken = tokenUserService.getListTokenByTopic(TOPIC);
			sendNotificationToAlLUserByTopic(listToken);
			
			if(ir.wasAcknowledged()) {
				return true; //Insert th??nh c??ng
			} else {
				return false; //Insert th???t b???i
			}
		} catch (Exception e) {
			return false; //L???i h??? th???ng
		}
		
	}
	
	
//	function sendNotificationToAlLUserByTopic
	public void sendNotificationToAlLUserByTopic(List<String> listToken) throws FirebaseMessagingException {
		List<String> registrationTokens = listToken;

			Notification.Builder builder = Notification.builder();
			try {
				builder.setTitle("C?? s??? thay ?????i v??? gi?? Tour");
				builder.setBody("Check ngay ??? Hahalolo.com b???n nh??!");
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			WebpushConfig.Builder builderWeb = WebpushConfig.builder();
			builderWeb.putHeader("Header", "C?? s??? thay ?????i v??? gi?? Tour!!");
			builderWeb.putData("Data", "Check ngay ??? Hahalolo.com b???n nh??!!");
			builderWeb.setNotification(new WebpushNotification("Demo header", "Demo body"));
//			builderWeb.
//			Notification noti = new Notification(builder.build());
			MulticastMessage message = MulticastMessage.builder()
			    .addAllTokens(registrationTokens)
			    .putData("123", "132")
			    .setWebpushConfig(builderWeb.build())
			    .setNotification(builder.build())
			    .build();
			
			BatchResponse response = FirebaseMessaging.getInstance().sendMulticast(message);
			if (response.getFailureCount() > 0) {
			  List<SendResponse> responses = response.getResponses();
			  List<String> failedTokens = new ArrayList<>();
			  for (int i = 0; i < responses.size(); i++) {
			    if (!responses.get(i).isSuccessful()) {
			      // The order of responses corresponds to the order of the registration tokens.
			      failedTokens.add(registrationTokens.get(i));
			    }
			  }

			  System.out.println("List of tokens that caused failures: " + failedTokens);
			}
			System.out.println("Successfully sent message: " + response);
	}
}
