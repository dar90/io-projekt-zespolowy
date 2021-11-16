package com.example.fuelprices;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class FirebaseInitializer implements ApplicationListener<ApplicationReadyEvent> {
    
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
     
        try {
			FirebaseOptions options = FirebaseOptions.builder()
										.setCredentials(
											GoogleCredentials.fromStream(
												getClass().getResourceAsStream("/firebase_config.json")
												))
										.build();
			FirebaseApp.initializeApp(options);
            log.info("Firebase initialized!");
		} catch (FileNotFoundException e1) {
			log.error("Firebase error - config not found!");
		} catch (IOException e2) {
			log.error("Firebase error - IOException!");
		} catch (IllegalStateException e3) {
			log.info("Firebase already initialized");
		}

    }
    
}
