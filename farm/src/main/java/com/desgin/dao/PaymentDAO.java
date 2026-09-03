package com.desgin.dao;

import java.util.ArrayList;
import java.util.List;

import com.desgin.config.FirestoreConfig;
import com.desgin.model.PaymentModel;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;

public class PaymentDAO {

    private static final String COLLECTION_NAME = "payments";

    private Firestore getDb() {
        return FirestoreConfig.getFirestore();
    }

    public void recordPayment(PaymentModel payment) {
        if (payment == null) return;
        try {
            if (payment.getPaymentId() == null || payment.getPaymentId().trim().isEmpty()) {
                payment.setPaymentId("PAY_" + System.currentTimeMillis());
            }
            Firestore db = getDb();
            DocumentReference docRef = db.collection(COLLECTION_NAME).document(payment.getPaymentId());
            ApiFuture<WriteResult> future = docRef.set(payment);
            future.get();
        } catch (Exception e) {
            System.err.println("Notice: Could not record payment in Firestore: " + e.getMessage());
        }
    }

    public List<PaymentModel> getAllPayments() {
        List<PaymentModel> list = new ArrayList<>();
        try {
            Firestore db = getDb();
            ApiFuture<QuerySnapshot> future = db.collection(COLLECTION_NAME).get();
            List<QueryDocumentSnapshot> docs = future.get().getDocuments();
            for (QueryDocumentSnapshot doc : docs) {
                PaymentModel p = doc.toObject(PaymentModel.class);
                if (p != null) {
                    if (p.getPaymentId() == null) p.setPaymentId(doc.getId());
                    list.add(p);
                }
            }
        } catch (Exception e) {
            System.err.println("Notice: Could not load payments: " + e.getMessage());
        }
        return list;
    }

    public List<PaymentModel> getPaymentsByFarmer(String farmerId) {
        List<PaymentModel> list = new ArrayList<>();
        if (farmerId == null || farmerId.trim().isEmpty()) return list;
        try {
            Firestore db = getDb();
            ApiFuture<QuerySnapshot> future = db.collection(COLLECTION_NAME)
                    .whereEqualTo("farmerId", farmerId.trim())
                    .get();
            List<QueryDocumentSnapshot> docs = future.get().getDocuments();
            for (QueryDocumentSnapshot doc : docs) {
                PaymentModel p = doc.toObject(PaymentModel.class);
                if (p != null) {
                    if (p.getPaymentId() == null) p.setPaymentId(doc.getId());
                    list.add(p);
                }
            }
        } catch (Exception e) {
            System.err.println("Notice: Could not load payments for farmer: " + e.getMessage());
        }
        return list;
    }

    public List<PaymentModel> getPaymentsByProvider(String providerId) {
        List<PaymentModel> list = new ArrayList<>();
        if (providerId == null || providerId.trim().isEmpty()) return list;
        try {
            Firestore db = getDb();
            ApiFuture<QuerySnapshot> future = db.collection(COLLECTION_NAME)
                    .whereEqualTo("providerId", providerId.trim())
                    .get();
            List<QueryDocumentSnapshot> docs = future.get().getDocuments();
            for (QueryDocumentSnapshot doc : docs) {
                PaymentModel p = doc.toObject(PaymentModel.class);
                if (p != null) {
                    if (p.getPaymentId() == null) p.setPaymentId(doc.getId());
                    list.add(p);
                }
            }
        } catch (Exception e) {
            System.err.println("Notice: Could not load payments for provider: " + e.getMessage());
        }
        return list;
    }

    public List<PaymentModel> getPaymentsByOperator(String operatorId) {
        List<PaymentModel> list = new ArrayList<>();
        if (operatorId == null || operatorId.trim().isEmpty()) return list;
        try {
            Firestore db = getDb();
            ApiFuture<QuerySnapshot> future = db.collection(COLLECTION_NAME)
                    .whereEqualTo("operatorId", operatorId.trim())
                    .get();
            List<QueryDocumentSnapshot> docs = future.get().getDocuments();
            for (QueryDocumentSnapshot doc : docs) {
                PaymentModel p = doc.toObject(PaymentModel.class);
                if (p != null) {
                    if (p.getPaymentId() == null) p.setPaymentId(doc.getId());
                    list.add(p);
                }
            }
        } catch (Exception e) {
            System.err.println("Notice: Could not load payments for operator: " + e.getMessage());
        }
        return list;
    }

    public PaymentModel getPaymentByBooking(String bookingId) {
        if (bookingId == null || bookingId.trim().isEmpty()) return null;
        try {
            Firestore db = getDb();
            ApiFuture<QuerySnapshot> future = db.collection(COLLECTION_NAME)
                    .whereEqualTo("bookingId", bookingId.trim())
                    .limit(1)
                    .get();
            List<QueryDocumentSnapshot> docs = future.get().getDocuments();
            if (!docs.isEmpty()) {
                PaymentModel p = docs.get(0).toObject(PaymentModel.class);
                if (p != null && p.getPaymentId() == null) p.setPaymentId(docs.get(0).getId());
                return p;
            }
        } catch (Exception e) {
            System.err.println("Notice: Could not load payment by booking: " + e.getMessage());
        }
        return null;
    }
}
