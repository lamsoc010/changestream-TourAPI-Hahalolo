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
				return true; //Xoá thành công
			} else {
				return false; //Xoá thất bại
			}
		} catch (Exception e) {
			return false; //Lỗi
		}
		
	}
	
	
	public boolean insertPriceOpen(PriceOpen priceOpen) {
		try {
			InsertOneResult ir = priceOpenRepository.insertPriceOpen(priceOpen);
			
//			Sự kiện nào dẫn đến cái insertPriceOpen này thì không cần biết
//			Chỉ cần biết là khi nào priceOpen được insert vào db thì gửi thông báo cho client
			
//			Get List Token device by Topic 
			List<String> listToken = tokenUserService.getListTokenByTopic(TOPIC);
			sendNotificationToAlLUserByTopic(listToken);
			
			if(ir.wasAcknowledged()) {
				return true; //Insert thành công
			} else {
				return false; //Insert thất bại
			}
		} catch (Exception e) {
			return false; //Lỗi hệ thống
		}
		
	}
	
	
//	function sendNotificationToAlLUserByTopic
	public void sendNotificationToAlLUserByTopic(List<String> listToken) throws FirebaseMessagingException {
		List<String> registrationTokens = listToken;

			Notification.Builder builder = Notification.builder();
			try {
				builder.setTitle("Có sự thay đổi về giá Tour");
				builder.setBody("Check ngay ở Hahalolo.com bạn nhé!");
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			WebpushConfig.Builder builderWeb = WebpushConfig.builder();
			builderWeb.putHeader("Header", "Có sự thay đổi về giá Tour!!");
			builderWeb.putData("Data", "Check ngay ở Hahalolo.com bạn nhé!!");
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
