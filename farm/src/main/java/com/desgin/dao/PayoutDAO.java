package com.desgin.dao;

import java.util.ArrayList;
import java.util.List;

import com.desgin.config.FirestoreConfig;
import com.desgin.model.PayoutModel;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;

public class PayoutDAO {

    private static final String COLLECTION_NAME = "payouts";

    private Firestore getDb() {
        return FirestoreConfig.getFirestore();
    }

    public void recordPayout(PayoutModel payout) {
        if (payout == null) return;
        try {
            if (payout.getPayoutId() == null || payout.getPayoutId().trim().isEmpty()) {
                payout.setPayoutId("PO_" + System.currentTimeMillis());
            }
            Firestore db = getDb();
            DocumentReference docRef = db.collection(COLLECTION_NAME).document(payout.getPayoutId());
            ApiFuture<WriteResult> future = docRef.set(payout);
            future.get();
        } catch (Exception e) {
            System.err.println("Notice: Could not record payout in Firestore: " + e.getMessage());
        }
    }

    public List<PayoutModel> getAllPayouts() {
        List<PayoutModel> list = new ArrayList<>();
        try {
            Firestore db = getDb();
            ApiFuture<QuerySnapshot> future = db.collection(COLLECTION_NAME).get();
            List<QueryDocumentSnapshot> docs = future.get().getDocuments();
            for (QueryDocumentSnapshot doc : docs) {
                PayoutModel p = doc.toObject(PayoutModel.class);
                if (p != null) {
                    if (p.getPayoutId() == null) p.setPayoutId(doc.getId());
                    list.add(p);
                }
            }
        } catch (Exception e) {
            System.err.println("Notice: Could not load payouts: " + e.getMessage());
        }
        return list;
    }

    public List<PayoutModel> getPayoutsByUser(String userId) {
        List<PayoutModel> list = new ArrayList<>();
        if (userId == null || userId.trim().isEmpty()) return list;
        try {
            Firestore db = getDb();
            ApiFuture<QuerySnapshot> future = db.collection(COLLECTION_NAME)
                    .whereEqualTo("userId", userId.trim().toLowerCase())
                    .get();
            List<QueryDocumentSnapshot> docs = future.get().getDocuments();
            for (QueryDocumentSnapshot doc : docs) {
                PayoutModel p = doc.toObject(PayoutModel.class);
                if (p != null) {
                    if (p.getPayoutId() == null) p.setPayoutId(doc.getId());
                    list.add(p);
                }
            }
        } catch (Exception e) {
            System.err.println("Notice: Could not load user payouts: " + e.getMessage());
        }
        return list;
    }

    public List<PayoutModel> getPayoutsByRole(String role) {
        List<PayoutModel> list = new ArrayList<>();
        if (role == null || role.trim().isEmpty()) return list;
        try {
            Firestore db = getDb();
            ApiFuture<QuerySnapshot> future = db.collection(COLLECTION_NAME)
                    .whereEqualTo("userRole", role.trim().toUpperCase())
                    .get();
            List<QueryDocumentSnapshot> docs = future.get().getDocuments();
            for (QueryDocumentSnapshot doc : docs) {
                PayoutModel p = doc.toObject(PayoutModel.class);
                if (p != null) {
                    if (p.getPayoutId() == null) p.setPayoutId(doc.getId());
                    list.add(p);
                }
            }
        } catch (Exception e) {
            System.err.println("Notice: Could not load payouts by role: " + e.getMessage());
        }
        return list;
    }

    public void updatePayoutStatus(String payoutId, String newStatus, String transactionRef) {
        if (payoutId == null || payoutId.trim().isEmpty()) return;
        try {
            Firestore db = getDb();
            DocumentReference docRef = db.collection(COLLECTION_NAME).document(payoutId.trim());
            docRef.update(
                    "status", newStatus,
                    "transactionReference", transactionRef,
                    "updatedAt", java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a"))
            ).get();
        } catch (Exception e) {
            System.err.println("Notice: Could not update payout status: " + e.getMessage());
        }
    }
}
