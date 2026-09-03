package com.desgin.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.desgin.config.FirestoreConfig;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;

public class WishlistDAO {

    private final Firestore db;

    public WishlistDAO() {
        this.db = FirestoreConfig.getFirestore();
    }

    private String buildDocId(String email, String name) {
        String cleanEmail = email != null ? email.trim().toLowerCase() : "farmer";
        String cleanName = name != null ? name.trim().toLowerCase() : "item";
        return (cleanEmail + "_" + cleanName).replaceAll("[^a-zA-Z0-9_-]", "_");
    }

    public void addEquipmentToWishlist(String farmerEmail, String name, String category, String price, String rating, String location, String imagePath) {
        if (db == null || farmerEmail == null || name == null) return;
        try {
            String docId = buildDocId(farmerEmail, name);
            Map<String, Object> data = new HashMap<>();
            data.put("farmerEmail", farmerEmail.trim().toLowerCase());
            data.put("equipmentName", name);
            data.put("category", category != null ? category : "Equipment");
            data.put("price", price != null ? price : "1000");
            data.put("rating", rating != null ? rating : "4.8");
            data.put("location", location != null ? location : "Maharashtra");
            data.put("imagePath", imagePath != null ? imagePath : "");
            data.put("timestamp", System.currentTimeMillis());

            db.collection("Wishlists").document(docId).set(data).get();
        } catch (Exception e) {
            System.err.println("WishlistDA add notice: " + e.getMessage());
        }
    }

    public void removeEquipmentFromWishlist(String farmerEmail, String name) {
        if (db == null || farmerEmail == null || name == null) return;
        try {
            String docId = buildDocId(farmerEmail, name);
            db.collection("Wishlists").document(docId).delete().get();
        } catch (Exception e) {
            System.err.println("WishlistDAO remove notice: " + e.getMessage());
        }
    }

    public List<Map<String, Object>> getWishlistByFarmer(String farmerEmail) {
        List<Map<String, Object>> results = new ArrayList<>();
        if (db == null || farmerEmail == null || farmerEmail.trim().isEmpty()) return results;
        try {
            QuerySnapshot snapshot = db.collection("Wishlists")
                  .whereEqualTo("farmerEmail", farmerEmail.trim().toLowerCase())
                  .get()
                  .get();

            for (DocumentSnapshot doc : snapshot.getDocuments()) {
                if (doc.exists()) {
                    results.add(doc.getData());
                }
            }
        } catch (Exception e) {
            System.err.println("WishlistDAOG get notice: " + e.getMessage());
        }
        return results;
    }
}
