package com.vinhlam.tourChangestream.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vinhlam.tourChangestream.entity.PriceOpen;
import com.vinhlam.tourChangestream.repository.PriceOpenRepository;


@Service
public class PriceOpenService {

	@Autowired
	private PriceOpenRepository priceOpenRepository;
	
	public boolean insertPriceOpen(PriceOpen priceOpen) {
		PriceOpen priceOpenResult = priceOpenRepository.save(priceOpen);
		if(priceOpenResult == null) {
			return false;
		} else {
			return true;
		}
	}
}
