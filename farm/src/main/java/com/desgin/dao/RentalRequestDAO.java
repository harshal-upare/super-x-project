package com.desgin.dao;

import java.util.ArrayList;
import java.util.List;

import com.desgin.config.FirestoreConfig;
import com.desgin.exception.DatabaseOperationException;
import com.desgin.exception.RentalRequestException;
import com.desgin.model.RentalRequestModel;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;

public class RentalRequestDAO {

    private static final String COLLECTION_NAME = "RentalRequests";

    private Firestore getDb() {
        return FirestoreConfig.getFirestore();
    }

    public void createRequest(RentalRequestModel request) throws RentalRequestException, DatabaseOperationException {
        if (request == null) {
            throw new RentalRequestException("Rental request cannot be null.");
        }
        if (request.getMachineryName() == null || request.getMachineryName().trim().isEmpty()) {
            throw new RentalRequestException("Machinery name must be specified for rental.");
        }
        if (request.getDays() <= 0) {
            throw new RentalRequestException("Rental duration must be at least 1 day.");
        }

        try {
            if (request.getRequestId() == null || request.getRequestId().trim().isEmpty()) {
                request.setRequestId("REQ_" + System.currentTimeMillis());
            }
            Firestore db = getDb();
            DocumentReference docRef = db.collection(COLLECTION_NAME).document(request.getRequestId());
            ApiFuture<WriteResult> future = docRef.set(request);
            future.get();
        } catch (Exception e) {
            throw new DatabaseOperationException("Failed to persist rental request to Firestore: " + e.getMessage(), e);
        }
    }

    public List<RentalRequestModel> getAllRequests() {
        List<RentalRequestModel> list = new ArrayList<>();
        try {
            Firestore db = getDb();
            ApiFuture<QuerySnapshot> future = db.collection(COLLECTION_NAME).get();
            List<QueryDocumentSnapshot> docs = future.get().getDocuments();
            for (QueryDocumentSnapshot doc : docs) {
                RentalRequestModel r = doc.toObject(RentalRequestModel.class);
                if (r != null) {
                    if (r.getRequestId() == null) r.setRequestId(doc.getId());
                    list.add(r);
                }
            }
        } catch (Exception e) {
            System.err.println("Notice: Could not load rental requests from Firestore: " + e.getMessage());
        }
        return list;
    }

    public List<RentalRequestModel> getRequestsByProvider(String providerEmail) {
        List<RentalRequestModel> list = new ArrayList<>();
        if (providerEmail == null || providerEmail.trim().isEmpty()) {
            return list;
        }
        try {
            Firestore db = getDb();
            ApiFuture<QuerySnapshot> future = db.collection(COLLECTION_NAME)
                    .whereEqualTo("providerEmail", providerEmail.trim())
                    .get();
            List<QueryDocumentSnapshot> docs = future.get().getDocuments();
            for (QueryDocumentSnapshot doc : docs) {
                RentalRequestModel r = doc.toObject(RentalRequestModel.class);
                if (r != null) {
                    if (r.getRequestId() == null) r.setRequestId(doc.getId());
                    list.add(r);
                }
            }
        } catch (Exception e) {
            System.err.println("Notice: Could not load requests for provider: " + e.getMessage());
        }
        return list;
    }

    public List<RentalRequestModel> getRequestsByFarmer(String farmerEmail) {
        List<RentalRequestModel> list = new ArrayList<>();
        if (farmerEmail == null || farmerEmail.trim().isEmpty()) {
            return list;
        }
        try {
            Firestore db = getDb();
            ApiFuture<QuerySnapshot> future = db.collection(COLLECTION_NAME)
                    .whereEqualTo("farmerEmail", farmerEmail.trim())
                    .get();
            List<QueryDocumentSnapshot> docs = future.get().getDocuments();
            for (QueryDocumentSnapshot doc : docs) {
                RentalRequestModel r = doc.toObject(RentalRequestModel.class);
                if (r != null) {
                    if (r.getRequestId() == null) r.setRequestId(doc.getId());
                    list.add(r);
                }
            }
        } catch (Exception e) {
            System.err.println("Notice: Could not load requests for farmer: " + e.getMessage());
        }
        return list;
    }

    public void updateRequestStatus(String requestId, String newStatus) throws DatabaseOperationException {
        if (requestId == null || newStatus == null) return;
        try {
            Firestore db = getDb();
            db.collection(COLLECTION_NAME).document(requestId).update("status", newStatus).get();
        } catch (Exception e) {
            throw new DatabaseOperationException("Failed to update rental status in Firestore: " + e.getMessage(), e);
        }
    }
}
