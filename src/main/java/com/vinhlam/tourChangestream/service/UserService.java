package com.vinhlam.tourChangestream.service;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vinhlam.tourChangestream.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	
//	get list User by topic
	public List<Document> getListUserByTopic(String topic) {
		List<Document> listUser = userRepository.getAllUserByTopic(topic);
		
		return listUser;
	}
	
}
