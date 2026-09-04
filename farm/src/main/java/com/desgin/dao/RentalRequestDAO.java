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
        String targetEmail = (farmerEmail != null) ? farmerEmail.trim() : "";
        String farmerPhone = com.desgin.view.farmer.Swapnil.FarmerProfileStore.phone;
        String farmerName = com.desgin.view.farmer.Swapnil.FarmerProfileStore.name;

        try {
            Firestore db = getDb();
            if (db == null) return list;
            ApiFuture<QuerySnapshot> allFuture = db.collection(COLLECTION_NAME).get();
            List<QueryDocumentSnapshot> allDocs = allFuture.get().getDocuments();
            java.util.Set<String> seenIds = new java.util.HashSet<>();

            for (QueryDocumentSnapshot doc : allDocs) {
                RentalRequestModel r = doc.toObject(RentalRequestModel.class);
                if (r != null) {
                    if (r.getRequestId() == null) r.setRequestId(doc.getId());
                    String rEmail = r.getFarmerEmail() != null ? r.getFarmerEmail().trim() : "";
                    String rPhone = r.getFarmerPhone() != null ? r.getFarmerPhone().trim() : "";
                    String rName = r.getFarmerName() != null ? r.getFarmerName().trim() : "";

                    boolean match = false;
                    if (!targetEmail.isEmpty() && targetEmail.equalsIgnoreCase(rEmail)) {
                        match = true;
                    } else if (farmerPhone != null && !farmerPhone.trim().isEmpty() && farmerPhone.trim().equals(rPhone)) {
                        match = true;
                    } else if (farmerName != null && !farmerName.trim().isEmpty() && farmerName.equalsIgnoreCase(rName)) {
                        match = true;
                    } else if (targetEmail.isEmpty()) {
                        match = true;
                    }

                    if (match && seenIds.add(r.getRequestId())) {
                        list.add(r);
                    }
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
            db.collection(COLLECTION_NAME).document(requestId).update("status", newStatus, "updatedAt", java.time.LocalDateTime.now().toString()).get();
        } catch (Exception e) {
            throw new DatabaseOperationException("Failed to update rental status in Firestore: " + e.getMessage(), e);
        }
    }

    public List<RentalRequestModel> getRequestsByOperator(String operatorEmail) {
        List<RentalRequestModel> list = new ArrayList<>();
        if (operatorEmail == null || operatorEmail.trim().isEmpty()) {
            return list;
        }
        try {
            Firestore db = getDb();
            ApiFuture<QuerySnapshot> future = db.collection(COLLECTION_NAME)
                    .whereEqualTo("operatorId", operatorEmail.trim().toLowerCase())
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
            System.err.println("Notice: Could not load requests for operator: " + e.getMessage());
        }
        return list;
    }

    public void updateOperatorStatus(String requestId, String operatorStatus) throws DatabaseOperationException {
        if (requestId == null || operatorStatus == null) return;
        try {
            Firestore db = getDb();
            db.collection(COLLECTION_NAME).document(requestId)
                    .update("operatorStatus", operatorStatus, "updatedAt", java.time.LocalDateTime.now().toString())
                    .get();
        } catch (Exception e) {
            throw new DatabaseOperationException("Failed to update operator status: " + e.getMessage(), e);
        }
    }

    public void updatePaymentStatus(String requestId, String paymentStatus, String transactionId, String paymentMode) throws DatabaseOperationException {
        if (requestId == null || paymentStatus == null) return;
        try {
            Firestore db = getDb();
            db.collection(COLLECTION_NAME).document(requestId).update(
                    "paymentStatus", paymentStatus,
                    "paymentTransactionId", transactionId != null ? transactionId : "",
                    "paymentMode", paymentMode != null ? paymentMode : "Razorpay",
                    "status", "CONFIRMED",
                    "updatedAt", java.time.LocalDateTime.now().toString()
            ).get();
        } catch (Exception e) {
            throw new DatabaseOperationException("Failed to update payment status: " + e.getMessage(), e);
        }
    }

    public void cancelRequest(String requestId) throws DatabaseOperationException {
        if (requestId == null) return;
        try {
            Firestore db = getDb();
            db.collection(COLLECTION_NAME).document(requestId).update(
                    "status", "CANCELLED",
                    "updatedAt", java.time.LocalDateTime.now().toString()
            ).get();
        } catch (Exception e) {
            throw new DatabaseOperationException("Failed to cancel request: " + e.getMessage(), e);
        }
    }

    public void startShift(String requestId, long shiftStartTime, long shiftDurationMillis) throws DatabaseOperationException {
        if (requestId == null) return;
        try {
            Firestore db = getDb();
            db.collection(COLLECTION_NAME).document(requestId).update(
                    "status", "IN_PROGRESS",
                    "operatorStatus", "IN_PROGRESS",
                    "shiftStartTime", shiftStartTime,
                    "shiftDurationMillis", shiftDurationMillis,
                    "updatedAt", java.time.LocalDateTime.now().toString()
            ).get();
        } catch (Exception e) {
            throw new DatabaseOperationException("Failed to start shift: " + e.getMessage(), e);
        }
    }

    public void completeShift(String requestId) throws DatabaseOperationException {
        if (requestId == null) return;
        try {
            Firestore db = getDb();
            db.collection(COLLECTION_NAME).document(requestId).update(
                    "status", "COMPLETED",
                    "operatorStatus", "COMPLETED",
                    "updatedAt", java.time.LocalDateTime.now().toString()
            ).get();
        } catch (Exception e) {
            throw new DatabaseOperationException("Failed to complete shift: " + e.getMessage(), e);
        }
    }

    public RentalRequestModel getRequestById(String requestId) {
        if (requestId == null || requestId.trim().isEmpty()) return null;
        try {
            Firestore db = getDb();
            var doc = db.collection(COLLECTION_NAME).document(requestId.trim()).get().get();
            if (doc.exists()) {
                RentalRequestModel r = doc.toObject(RentalRequestModel.class);
                if (r != null && r.getRequestId() == null) r.setRequestId(doc.getId());
                return r;
            }
        } catch (Exception e) {
            System.err.println("Notice: Could not load request by id: " + e.getMessage());
        }
        return null;
    }
}
