package com.vinhlam.tourChangestream;

import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
//import com.vinhlam.tourChangestream.event.DateOpenChangestream;
@SpringBootApplication
public class ChangestreamApplication {

	@Value("${spring.data.mongodb.database}")
	private String mongoDB;
	
	
	public static void main(String[] args){

		SpringApplication.run(ChangestreamApplication.class, args);
	}
	
	@Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSkipNullEnabled(true);
        return modelMapper;
    }
	
	@Bean
	public MongoDatabase mongoDatabase() {
		CodecRegistry cRegistry = CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), 
				CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()));
		return MongoClients.create().getDatabase(mongoDB).withCodecRegistry(cRegistry);
	}

}
