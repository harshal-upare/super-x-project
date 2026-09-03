package com.desgin.dao;

import com.desgin.config.FirestoreConfig;
import com.desgin.model.AuthenticateModel;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;

public class AuthDAO {

    private Firestore db = FirestoreConfig.getFirestore();;

    public void addUser(AuthenticateModel objModel) {

        try {
           
            db.collection(objModel.getRole()).document(objModel.getMail()).create(objModel).get();

        } catch(Exception e) {
            
            e.printStackTrace();
        }
    }

    public boolean isUser(String mail, String role) {

        try {

            ApiFuture<DocumentSnapshot> future = db.collection(role).document(mail).get();  
            DocumentSnapshot doc = future.get();

            boolean valid = doc.exists();
            
            if(valid)
                return true;
        } catch(Exception e) {
            
            e.printStackTrace();
        }

        return false;
    }

   
}
