package com.vinhlam.tourChangestream.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

@Configuration
public class FirebaseAppConfig {
	@Value("${app.firebase-configuration-file}")
	private String firebaseConfigPath;

	

//	Khởi tạo luôn khi chạy dự án, và chỉ khởi tạo 1 lần duy nhất này mà thôi
	@Autowired
	public FirebaseApp initialize()  {
//		if(FirebaseApp.getInstance(FirebaseApp.DEFAULT_APP_NAME) != null) {
//		    FirebaseApp.getInstance().delete();
//		}
		try {
			FirebaseOptions options = new FirebaseOptions.Builder()
					.setCredentials(
							GoogleCredentials.fromStream(new ClassPathResource(firebaseConfigPath).getInputStream()))
					.build();
//			if(FirebaseApp.getInstance().)
//			if(FirebaseApp.getInstance(FirebaseApp.DEFAULT_APP_NAME).getName().equals("[DEFAULT]")) {
//				FirebaseApp.getInstance().delete();
//			}
			
//			System.out.println(FirebaseApp.getInstance(FirebaseApp.DEFAULT_APP_NAME).getName().equals("[DEFAULT]"));
			return FirebaseApp.initializeApp(options);


			
		} catch (IOException e) {
//			logger.error(e.getMessage());
		}
		return null;
	}
}
