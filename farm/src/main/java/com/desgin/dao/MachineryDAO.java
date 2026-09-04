package com.desgin.dao;

import java.util.ArrayList;
import java.util.List;

import com.desgin.config.FirestoreConfig;
import com.desgin.exception.DatabaseOperationException;
import com.desgin.exception.MachineryValidationException;
import com.desgin.model.MachineryModel;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;

public class MachineryDAO {

    private static final String COLLECTION_NAME = "Machinery";

    private Firestore getDb() {
        return FirestoreConfig.getFirestore();
    }

    public void addMachinery(MachineryModel model) throws MachineryValidationException, DatabaseOperationException {
        if (model == null) {
            throw new MachineryValidationException("Machinery details cannot be null.");
        }
        if (model.getName() == null || model.getName().trim().isEmpty()) {
            throw new MachineryValidationException("Machinery title/name is required.");
        }
        if (model.getPricePerDay() <= 0) {
            throw new MachineryValidationException("Rental rate per day must be greater than zero.");
        }
        if (model.getLocation() == null || model.getLocation().trim().isEmpty()) {
            throw new MachineryValidationException("Operating location/town is required.");
        }

        try {
            if (model.getId() == null || model.getId().trim().isEmpty()) {
                model.setId("MAC_" + System.currentTimeMillis());
            }
            Firestore db = getDb();
            DocumentReference docRef = db.collection(COLLECTION_NAME).document(model.getId());
            ApiFuture<WriteResult> future = docRef.set(model);
            future.get();
        } catch (Exception e) {
            throw new DatabaseOperationException("Failed to save machinery to Firestore: " + e.getMessage(), e);
        }
    }

    public List<MachineryModel> getAllMachinery() {
        List<MachineryModel> list = new ArrayList<>();
        try {
            Firestore db = getDb();
            ApiFuture<QuerySnapshot> future = db.collection(COLLECTION_NAME).get();
            List<QueryDocumentSnapshot> docs = future.get().getDocuments();
            for (QueryDocumentSnapshot doc : docs) {
                MachineryModel m = doc.toObject(MachineryModel.class);
                if (m != null) {
                    if (m.getId() == null) m.setId(doc.getId());
                    list.add(m);
                }
            }
        } catch (Exception e) {
            System.err.println("Notice: Could not load machinery from Firestore: " + e.getMessage());
        }
        return list;
    }

    public List<MachineryModel> getMachineryByProvider(String providerEmail) {
        List<MachineryModel> list = new ArrayList<>();
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
                MachineryModel m = doc.toObject(MachineryModel.class);
                if (m != null) {
                    if (m.getId() == null) m.setId(doc.getId());
                    list.add(m);
                }
            }
        } catch (Exception e) {
            System.err.println("Notice: Could not load provider machinery: " + e.getMessage());
        }
        return list;
    }

    public void updateMachineryStatus(String id, String newStatus) throws DatabaseOperationException {
        if (id == null || newStatus == null) return;
        try {
            Firestore db = getDb();
            db.collection(COLLECTION_NAME).document(id).update("status", newStatus).get();
        } catch (Exception e) {
            throw new DatabaseOperationException("Failed to update status in Firestore: " + e.getMessage(), e);
        }
    }

    public void updateMachineryPrice(String id, int newPrice) throws DatabaseOperationException {
        if (id == null) return;
        try {
            Firestore db = getDb();
            db.collection(COLLECTION_NAME).document(id).update("pricePerDay", newPrice).get();
        } catch (Exception e) {
            throw new DatabaseOperationException("Failed to update price in Firestore: " + e.getMessage(), e);
        }
    }

    public void deleteMachinery(String id) throws DatabaseOperationException {
        if (id == null) return;
        try {
            Firestore db = getDb();
            db.collection(COLLECTION_NAME).document(id).delete().get();
        } catch (Exception e) {
            throw new DatabaseOperationException("Failed to delete machinery from Firestore: " + e.getMessage(), e);
        }
    }

    public void updateMachinery(MachineryModel model) throws DatabaseOperationException {
        if (model == null || model.getId() == null) return;
        try {
            Firestore db = getDb();
            db.collection(COLLECTION_NAME).document(model.getId()).set(model).get();
        } catch (Exception e) {
            throw new DatabaseOperationException("Failed to update machinery in Firestore: " + e.getMessage(), e);
        }
    }

    public MachineryModel getMachineryById(String id) {
        if (id == null || id.trim().isEmpty()) return null;
        try {
            Firestore db = getDb();
            DocumentSnapshot doc = db.collection(COLLECTION_NAME).document(id.trim()).get().get();
            if (doc.exists()) {
                MachineryModel m = doc.toObject(MachineryModel.class);
                if (m != null && m.getId() == null) m.setId(doc.getId());
                return m;
            }
        } catch (Exception e) {
            System.err.println("Notice: Could not load machinery by id: " + e.getMessage());
        }
        return null;
    }
}
