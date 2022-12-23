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
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.internal.bulk.DeleteRequest;
import com.vinhlam.tourChangestream.entity.DateOpen;
import com.vinhlam.tourChangestream.entity.PriceOpen;
import com.vinhlam.tourChangestream.repository.PriceOpenRepository;


@Service
public class PriceOpenService {

	@Autowired
	private PriceOpenRepository priceOpenRepository;

	
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
			
			if(ir.wasAcknowledged()) {
				return true; //Insert thành công
			} else {
				return false; //Insert thất bại
			}
		} catch (Exception e) {
			return false; //Lỗi hệ thống
		}
		
	}
}
