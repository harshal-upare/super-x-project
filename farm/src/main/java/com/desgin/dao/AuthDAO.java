package com.desgin.dao;

import com.desgin.config.FirestoreConfig;
import com.desgin.model.AuthenticateModel;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;

public class AuthDAO {

    public void addUser(AuthenticateModel objModel) {
        try {
            Firestore db = FirestoreConfig.getFirestore();
            if (db != null && objModel != null && objModel.getMail() != null) {
                db.collection("users").document(objModel.getMail().toLowerCase().trim()).set(objModel);
            }
        } catch(Exception e) {
            System.err.println("AuthDAO addUser error: " + e.getMessage());
        }
    }

    public AuthenticateModel getUserByEmail(String email) {
        try {
            Firestore db = FirestoreConfig.getFirestore();
            if (db != null && email != null) {
                DocumentSnapshot document = db.collection("users").document(email.toLowerCase().trim()).get().get();
                if (document.exists()) {
                    return document.toObject(AuthenticateModel.class);
                }
            }
        } catch (Exception e) {
            System.err.println("AuthDAO getUser error: " + e.getMessage());
        }
        return null;
    }
}
