package com.desgin.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

public class FirestoreConfig {
    

    static {

        getFirebaseConfig();
    }

    public static void getFirebaseConfig() {

        try {
            FileInputStream serviceAccount =
            new FileInputStream("farm\\src\\main\\resources\\api\\titan-edfd2-firebase-adminsdk-fbsvc-61bea2a63d.json");

            FirebaseOptions options = new FirebaseOptions.Builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .build();

            FirebaseApp.initializeApp(options);

        } catch(Exception e) {

            e.printStackTrace();
        }
    }

    public static Firestore getFirestore() {
        
        return FirestoreClient.getFirestore();
    }
}
