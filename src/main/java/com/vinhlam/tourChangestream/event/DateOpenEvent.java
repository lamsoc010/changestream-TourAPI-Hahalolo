package com.vinhlam.tourChangestream.event;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.bson.BsonDocument;
import org.bson.BsonInt32;
import org.bson.BsonObjectId;
import org.bson.BsonValue;
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
			insertPriceOpen(priceTour, dateOpen.getDateAvailable());
		}
	}
	
	public void handleUpdateDateOpen(ChangeStreamDocument<Document> next) throws ParseException {
//		1. Get data after insert dateOpen
		// Input: new Data DateOpen
		// Output: dateOpen
		Document dateOpenDocument = next.getFullDocument();
		
		DateOpen dateOpen = modelMapper.map(dateOpenDocument, DateOpen.class);
		
		String idTour = dateOpen.getTourId().toString();
		Date dateOpenAfterChange = dateOpen.getDateAvailable();
//		2. Lấy ra những field được update
		BsonDocument listFieldUpdate = next.getUpdateDescription().getUpdatedFields();

		//3. Thay đổi status:
		// TH1: Từ status 1 -> 0 : Lấy dateAvailable + tourId để xoá priceOpen tương ứng
				//TH1.1: Thay đổi thằng dateAvailable luôn => Lấy ra được dateAvailable trước khi thay đổi
				//TH1.2: Không thay đổi dateAvailable      => Lấy dateAvailable hiện tại 
		// TH2: Từ status 0 -> 1: Lấy dateAvailable + tourId
				//Query đến bảng priceTour, check xem với tourId đó thì dateAvailable có nằm trong khoảng được set hay không
				//TH2.1: Nếu có thì lấy ra price, currency + kết hợp dateAvailable + tourId để insert priceOpen
				//TH2.2: Nếu không thì không thực hiện gì cả
		
		//3. Thay đổi status:		
		if(listFieldUpdate.containsKey("status")) {  
			int status = listFieldUpdate.getInt32("status").getValue();
			
			// TH1: Từ status 1 -> 0 : Lấy dateAvailable + tourId để xoá priceOpen tương ứng
			if(status == 0) {
				
				//TH1.1: Thay đổi thằng dateAvailable luôn => Lấy ra được dateAvailable trước khi thay đổi
				if(listFieldUpdate.containsKey("dateAvailable")) { 
					
					//Note: Lưu ý là cần lấy ra ngày trước khi thay đổi của dateOpen chứ k phải ngày thay đổi như bây giờ
				} 
				//TH1.2: Không thay đổi dateAvailable => Lấy dateAvailable hiện tại 
				else { 
					boolean checkDeletePriceOpen = priceOpenService.deletePriceOpenByTourIdAndDateOpen(idTour, dateOpenAfterChange);
					if(checkDeletePriceOpen) {
						System.out.println("Delete priceOpen is tourId: " + idTour +"and dateOpen: "+dateOpenAfterChange+ " success");
					}
				}
			} 
			
			// TH2: Từ status 0 -> 1: Lấy dateAvailable + tourId
			else if(status == 1) {
				
				//Query đến bảng priceTour, check xem với tourId đó thì dateAvailable có nằm trong khoảng được set hay không
				PriceTour priceTour = priceTourService.getPriceTourByTourId(idTour, dateOpenAfterChange);
				
				//TH2.1: Nếu có thì lấy ra price, currency + kết hợp dateAvailable + tourId để insert priceOpen
				if (priceTour != null) {
					insertPriceOpen(priceTour, dateOpenAfterChange);
				}
			}
		} 
		
		//4. Thay đổi dateAvailble
		// Check xem status hiện tại là bao nhiêu:
		//TH1: status = 0 => Không thực hiện gì cả
		//TH2: status = 1 => Tương tự TH2 của bước 3
		if(listFieldUpdate.containsKey("dateAvailable")) {
			int status = listFieldUpdate.getInt32("status").getValue();
			
			if(status == 1) {
				PriceTour priceTour = priceTourService.getPriceTourByTourId(idTour, dateOpenAfterChange);
				//Nếu có thì lấy ra price, currency + kết hợp dateAvailable + tourId để insert priceOpen
				if (priceTour != null) {
					insertPriceOpen(priceTour, dateOpenAfterChange);
				}
			}
		}

		
	}
	
	
//	Phương thức để insertPriceOpen
	public void insertPriceOpen(PriceTour priceTour, Date dateOpen) {
		PriceOpen priceOpen = new PriceOpen();
		
		priceOpen.setTourId(priceTour.getTourId());
		priceOpen.setDateOpen(dateOpen);
		priceOpen.setCurrency(priceTour.getCurrency());
		priceOpen.setPrice(priceTour.getPrice());
		
		priceOpenService.insertPriceOpen(priceOpen);
		
		System.err.println("Insert PriceOpen is tourId: " + priceTour.getTourId() + " success!!");
	}
}
