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
import com.vinhlam.tourChangestream.repository.PriceTourRepository;

@Service
public class PriceTourService {
	@Autowired
	private PriceTourRepository priceTourRepository;
	
	
//	Get PriceTour by TourId and Filter date
	public PriceTour getPriceTourByTourId(String id, Date date) {
		try {
			PriceTour priceTour = priceTourRepository.getPriceTourByTourId(id, date);
			if(priceTour == null) { //Ở đây ví dụ sau này nếu null thì trả về mã lỗi hay status chi đó, giờ cứ trả về là null
				return null; 
			} else {
				return priceTour;
			}
		} catch (Exception e) {
			return null; //Lỗi hệ thống
		}
	}
}
