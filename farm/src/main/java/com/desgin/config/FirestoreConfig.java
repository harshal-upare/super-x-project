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
    
    private static boolean initialized = false;

    static {
        getFirebaseConfig();
    }

    public static synchronized void getFirebaseConfig() {
        if (initialized || !FirebaseApp.getApps().isEmpty()) {
            initialized = true;
            return;
        }

        try {
            InputStream serviceAccount = FirestoreConfig.class.getResourceAsStream("/auth.json");
            if (serviceAccount == null) {
                serviceAccount = FirestoreConfig.class.getClassLoader().getResourceAsStream("auth.json");
            }
            if (serviceAccount == null) {
                File f = new File("src/main/resources/auth.json");
                if (f.exists()) {
                    serviceAccount = new FileInputStream(f);
                } else {
                    f = new File("farm/src/main/resources/auth.json");
                    if (f.exists()) {
                        serviceAccount = new FileInputStream(f);
                    }
                }
            }

            if (serviceAccount != null) {
                FirebaseOptions options = new FirebaseOptions.Builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .build();

                if (FirebaseApp.getApps().isEmpty()) {
                    FirebaseApp.initializeApp(options);
                }
                initialized = true;
            }
        } catch(Exception e) {
            System.err.println("Firebase init warning: " + e.getMessage());
        }
    }

    public static Firestore getFirestore() {
        try {
            getFirebaseConfig();
            if (!FirebaseApp.getApps().isEmpty()) {
                return FirestoreClient.getFirestore();
            }
        } catch (Exception e) {
            System.err.println("Firestore client warning: " + e.getMessage());
        }
        return null;
    }
}
