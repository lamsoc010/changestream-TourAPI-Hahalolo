package com.vinhlam.tourChangestream.entity;

import java.util.Date;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@Document(collation = "dateOpen")
public class DateOpen {

	@BsonProperty("_id")
	@BsonId
	private ObjectId id;
	
	@BsonProperty("tourId")
	private ObjectId tourId;
	
	
	private Date dateAvailable;
	private int status;
	
}
