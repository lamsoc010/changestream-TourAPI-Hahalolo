package com.vinhlam.tourChangestream.entity;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Document(collation = "user")
public class User {
	
	@BsonProperty("_id")
	@BsonId
	private ObjectId id;
	private String firstName;
	private String lastName;
	private String phoneNumber;
	private String address;
}
