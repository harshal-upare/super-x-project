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
            if (mail == null || mail.trim().isEmpty()) return false;
            String key = mail.trim();

            ApiFuture<DocumentSnapshot> future = db.collection(role).document(key).get();  
            DocumentSnapshot doc = future.get();

            if (doc.exists()) return true;

            // Query by email field
            var queryMail = db.collection(role).whereEqualTo("mail", key).get().get();
            if (!queryMail.isEmpty()) return true;

            // Query by phone number field
            var queryNum = db.collection(role).whereEqualTo("num", key).get().get();
            if (!queryNum.isEmpty()) return true;

        } catch(Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public AuthenticateModel getUser(String mail, String role) {

        try {
            if (mail == null || mail.trim().isEmpty()) return null;
            String key = mail.trim();

            ApiFuture<DocumentSnapshot> future = db.collection(role).document(key).get();  
            DocumentSnapshot doc = future.get();

            if (doc.exists()) {
                return doc.toObject(AuthenticateModel.class);
            }

            // Query by email field
            var queryMail = db.collection(role).whereEqualTo("mail", key).get().get();
            if (!queryMail.isEmpty()) {
                return queryMail.getDocuments().get(0).toObject(AuthenticateModel.class);
            }

            // Query by phone number field
            var queryNum = db.collection(role).whereEqualTo("num", key).get().get();
            if (!queryNum.isEmpty()) {
                return queryNum.getDocuments().get(0).toObject(AuthenticateModel.class);
            }

        } catch(Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean updateLocation(String mail, String role, String town, String district, String state, String pincode) {

        try {
            if (mail == null || mail.trim().isEmpty()) {
                return false;
            }
            String key = mail.trim();

            java.util.Map<String, Object> updates = new java.util.HashMap<>();
            updates.put("town", town != null ? town.trim() : "");
            updates.put("district", district != null ? district.trim() : "");
            updates.put("state", state != null ? state.trim() : "");
            updates.put("pincode", pincode != null ? pincode.trim() : "");

            DocumentSnapshot doc = db.collection(role).document(key).get().get();
            if (doc.exists()) {
                db.collection(role).document(key).set(updates, com.google.cloud.firestore.SetOptions.merge()).get();
                return true;
            }

            var queryMail = db.collection(role).whereEqualTo("mail", key).get().get();
            if (!queryMail.isEmpty()) {
                String docId = queryMail.getDocuments().get(0).getId();
                db.collection(role).document(docId).set(updates, com.google.cloud.firestore.SetOptions.merge()).get();
                return true;
            }

            var queryNum = db.collection(role).whereEqualTo("num", key).get().get();
            if (!queryNum.isEmpty()) {
                String docId = queryNum.getDocuments().get(0).getId();
                db.collection(role).document(docId).set(updates, com.google.cloud.firestore.SetOptions.merge()).get();
                return true;
            }

            db.collection(role).document(key).set(updates, com.google.cloud.firestore.SetOptions.merge()).get();
            return true;
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateProfile(String mail, String role, String newName, String newPhone) {

        try {
            if (mail == null || mail.trim().isEmpty()) {
                return false;
            }
            String key = mail.trim();

            java.util.Map<String, Object> updates = new java.util.HashMap<>();
            if (newName != null && !newName.trim().isEmpty()) {
                updates.put("name", newName.trim());
            }
            if (newPhone != null && !newPhone.trim().isEmpty()) {
                updates.put("num", newPhone.trim());
            }

            DocumentSnapshot doc = db.collection(role).document(key).get().get();
            if (doc.exists()) {
                db.collection(role).document(key).set(updates, com.google.cloud.firestore.SetOptions.merge()).get();
                return true;
            }

            var queryMail = db.collection(role).whereEqualTo("mail", key).get().get();
            if (!queryMail.isEmpty()) {
                String docId = queryMail.getDocuments().get(0).getId();
                db.collection(role).document(docId).set(updates, com.google.cloud.firestore.SetOptions.merge()).get();
                return true;
            }

            var queryNum = db.collection(role).whereEqualTo("num", key).get().get();
            if (!queryNum.isEmpty()) {
                String docId = queryNum.getDocuments().get(0).getId();
                db.collection(role).document(docId).set(updates, com.google.cloud.firestore.SetOptions.merge()).get();
                return true;
            }

            db.collection(role).document(key).set(updates, com.google.cloud.firestore.SetOptions.merge()).get();
            return true;
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getAdminCount() {
        try {
            if (db == null) return 0;
            var snapshots = db.collection("Admin").get().get();
            return snapshots.size();
        } catch (Exception e) {
            System.err.println("Notice: Could not count admins: " + e.getMessage());
            return 0;
        }
    }

    public java.util.List<AuthenticateModel> getAllAdminUsers() {
        java.util.List<AuthenticateModel> list = new java.util.ArrayList<>();
        try {
            if (db == null) return list;
            var docs = db.collection("Admin").get().get().getDocuments();
            for (var d : docs) {
                AuthenticateModel m = d.toObject(AuthenticateModel.class);
                if (m != null) {
                    list.add(m);
                }
            }
        } catch (Exception e) {
            System.err.println("Notice: Could not fetch all admins: " + e.getMessage());
        }
        return list;
    }
}
