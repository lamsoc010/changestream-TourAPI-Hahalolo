package com.vinhlam.tourChangestream.event;

import java.text.ParseException;

import org.bson.BsonObjectId;
import org.bson.Document;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import com.vinhlam.tourChangestream.entity.DateOpen;
import com.vinhlam.tourChangestream.entity.PriceOpen;
import com.vinhlam.tourChangestream.entity.PriceTour;
import com.vinhlam.tourChangestream.service.PriceOpenService;
import com.vinhlam.tourChangestream.service.PriceTourService;

@Configuration
public class DateOpenEvent {
	
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private PriceTourService priceTourService;
	@Autowired
	private PriceOpenService priceOpenService;
	
	public DateOpenEvent() {
		
	}

	public void handleInsertDateOpen(ChangeStreamDocument<Document> next)
			throws JsonMappingException, JsonProcessingException, ParseException {

//		1. Get data after insert dateOpen
		// Input: new Data DateOpen
		// Output: dateOpen
		Document dateOpenDocument = next.getFullDocument();
		
		DateOpen dateOpen = modelMapper.map(dateOpenDocument, DateOpen.class);
		
		String idTour = dateOpen.getTourId().toString();

//		2. Get pricetTour By idTour
		// Input: idTour, date
		// Output: priceTour(cụ thể chỉ cần price và currency)
		PriceTour priceTour = priceTourService.getPriceTourByTourId(idTour, dateOpen.getDateAvailable());

//		3. Insert priceOpen
		// Input: idTour, price, currency, dateOpen
//		Nếu như priceTour == null tức là 1 là không có tourId phù hợp, 2 là ngày mở bán không nằm trong khoảng đó thì không insert vào
		if (priceTour != null) {
			PriceOpen priceOpen = new PriceOpen();
			
			priceOpen.setTourId(idTour.toString());
			priceOpen.setDateOpen(dateOpen.getDateAvailable());
			priceOpen.setCurrency(priceTour.getCurrency());
			priceOpen.setPrice(priceTour.getPrice());
			
			priceOpenService.insertPriceOpen(priceOpen);
			
			System.err.println("Insert PriceOpen is tourId: " + idTour + " success!!");
		}

		

	}
}
