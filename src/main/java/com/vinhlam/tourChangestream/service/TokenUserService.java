package com.vinhlam.tourChangestream.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vinhlam.tourChangestream.repository.TokenUserRepository;

@Service
public class TokenUserService {

	@Autowired
	private TokenUserRepository tokenUserRepository; 
	
	@Autowired
	private UserService userService;
	
//	get list token by topic
	public List<String> getListTokenByTopic(String topic) {
//		1. Get list User by topic
		List<Document> listUser = new ArrayList<>();
		listUser = userService.getListUserByTopic(topic);
		
//		2. Add listUserId By ListUser
		List<ObjectId> listUserId = new ArrayList<>();
		for(Document d : listUser) {
			listUserId.add(d.getObjectId("_id"));
		}
		
//		3. Get listTokenUser by listUserId
		List<Document> listTokenUser = new ArrayList<>();
		listTokenUser = tokenUserRepository.getListTokenByListUser(listUserId);
		
//		Tạm thời chưa nghỉ ra được câu lệnh mongo tối ưu cho việc này nên cứ xử lý Java ở đây đã
//		Nếu ở câu query mà trả về được luôn ListToken thì đỡ phải xử lý ở Java
//		4. Add listToken by listTokenUser
		List<String> listToken = new ArrayList<>();
		for(Document d : listTokenUser ) {
			for(String s : (List<String>)d.get("listToken")) {
				listToken.add(s);
			}
		}
		
		return listToken;
	}
}
