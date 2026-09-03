package com.desgin.dao;

import java.util.ArrayList;
import java.util.List;

import com.desgin.config.FirestoreConfig;
import com.desgin.model.ReviewModel;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QuerySnapshot;

public class ReviewDAO {

    private final Firestore db;

    public ReviewDAO() {
        this.db = FirestoreConfig.getFirestore();
    }

    public void addReview(ReviewModel review) {
        if (db == null || review == null) return;
        try {
            String docId = review.getReviewId();
            if (docId == null || docId.isEmpty()) {
                docId = "REV_" + System.currentTimeMillis();
                review.setReviewId(docId);
            }
            db.collection("Reviews").document(docId).set(review).get();
        } catch (Exception e) {
            System.err.println("ReviewDAO add notice: " + e.getMessage());
        }
    }

    public List<ReviewModel> getAllReviews() {
        List<ReviewModel> list = new ArrayList<>();
        if (db == null) return list;
        try {
            QuerySnapshot snapshot = db.collection("Reviews")
                  .orderBy("timestamp", Query.Direction.DESCENDING)
                  .get()
                  .get();

            for (DocumentSnapshot doc : snapshot.getDocuments()) {
                if (doc.exists()) {
                    ReviewModel m = doc.toObject(ReviewModel.class);
                    if (m != null) list.add(m);
                }
            }
        } catch (Exception e) {
            System.err.println("ReviewDAO getAll notice: " + e.getMessage());
        }
        return list;
    }

    public List<ReviewModel> getReviewsByFarmer(String farmerEmail) {
        List<ReviewModel> list = new ArrayList<>();
        if (db == null || farmerEmail == null || farmerEmail.isEmpty()) return list;
        try {
            QuerySnapshot snapshot = db.collection("Reviews")
                  .whereEqualTo("farmerEmail", farmerEmail.trim().toLowerCase())
                  .get()
                  .get();

            for (DocumentSnapshot doc : snapshot.getDocuments()) {
                if (doc.exists()) {
                    ReviewModel m = doc.toObject(ReviewModel.class);
                    if (m != null) list.add(m);
                }
            }
        } catch (Exception e) {
            System.err.println("ReviewDAO getByFarmer notice: " + e.getMessage());
        }
        return list;
    }
}