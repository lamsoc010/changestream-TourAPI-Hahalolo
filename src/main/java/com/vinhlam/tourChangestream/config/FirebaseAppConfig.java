package com.vinhlam.tourChangestream.config;

import java.io.IOException;

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

	

	@Bean
	public FirebaseApp initialize()  {
//		if(FirebaseApp.getInstance(FirebaseApp.DEFAULT_APP_NAME) != null) {
//		    FirebaseApp.getInstance().delete();
//		}
		try {
			FirebaseOptions options = new FirebaseOptions.Builder()
					.setCredentials(
							GoogleCredentials.fromStream(new ClassPathResource(firebaseConfigPath).getInputStream()))
					.build();
			
			return FirebaseApp.initializeApp(options);

			
		} catch (IOException e) {
//			logger.error(e.getMessage());
		}
		return null;
	}
}
