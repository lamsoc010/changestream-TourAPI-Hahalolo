package com.vinhlam.tourChangestream.event;

import java.text.ParseException;

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
import com.vinhlam.tourChangestream.service.DateOpenService;
import com.vinhlam.tourChangestream.service.PriceOpenService;
import com.vinhlam.tourChangestream.service.PriceTourService;

@Configuration
public class PriceTourEvent {
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private PriceOpenService priceOpenService;
	@Autowired
	private DateOpenService dateOpenService;
	
	public PriceTourEvent() {
		
	}

	public void handleInsertPriceTour(ChangeStreamDocument<Document> next)
			throws JsonMappingException, JsonProcessingException, ParseException {

//		1. Get data after insert priceTour
		// Input: new Data priceTour
		// Output: priceTour
		Document priceTourDocument = next.getFullDocument();
		
		PriceTour priceTour = modelMapper.map(priceTourDocument, PriceTour.class);
		
		String idTour = priceTour.getTourId().toString();

//		2. Get dateOpen By idTour
		// Input: idTour, dateStart, dateEnd
		// Output: dateOpen(cụ thể chỉ cần price và currency)
		DateOpen dateOpen = dateOpenService.getDateOpenByTourId(idTour, priceTour.getDateApplyStart(), priceTour.getDateApplyEnd());

//		3. Insert priceOpen
		// Input: idTour, price, currency, dateOpen(dateAvailable)
//		Nếu như dateOpen == null tức là 1 là không có tourId phù hợp, 2 là ngày mở bán không nằm trong khoảng đó thì không insert vào
		if (dateOpen != null) {
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
