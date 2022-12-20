package com.vinhlam.tourChangestream.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.vinhlam.tourChangestream.entity.PriceOpen;

@Repository
public interface PriceOpenRepository extends MongoRepository<PriceOpen, String>{

}
