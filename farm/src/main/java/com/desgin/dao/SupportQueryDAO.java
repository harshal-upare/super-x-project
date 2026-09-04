package com.desgin.dao;

import java.util.ArrayList;
import java.util.List;

import com.desgin.config.FirestoreConfig;
import com.desgin.model.SupportMessageModel;
import com.desgin.model.SupportQueryModel;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;

public class SupportQueryDAO {

    public static final String COLLECTION_NAME = "support_queries";

    private Firestore getDb() {
        return FirestoreConfig.getFirestore();
    }

    public void saveQuery(SupportQueryModel query) {
        if (query == null) return;
        try {
            if (query.getQueryId() == null || query.getQueryId().trim().isEmpty()) {
                query.setQueryId("QRY_" + System.currentTimeMillis());
            }
            query.setLastUpdated(System.currentTimeMillis());
            Firestore db = getDb();
            DocumentReference docRef = db.collection(COLLECTION_NAME).document(query.getQueryId());
            ApiFuture<WriteResult> future = docRef.set(query);
            future.get();
        } catch (Exception e) {
            System.err.println("Notice: Could not save support query: " + e.getMessage());
        }
    }

    public SupportQueryModel getQueryById(String queryId) {
        if (queryId == null || queryId.trim().isEmpty()) return null;
        try {
            Firestore db = getDb();
            DocumentSnapshot doc = db.collection(COLLECTION_NAME).document(queryId.trim()).get().get();
            if (doc.exists()) {
                SupportQueryModel q = doc.toObject(SupportQueryModel.class);
                if (q != null && q.getQueryId() == null) {
                    q.setQueryId(doc.getId());
                }
                return q;
            }
        } catch (Exception e) {
            System.err.println("Notice: Could not fetch query " + queryId + ": " + e.getMessage());
        }
        return null;
    }

    public SupportQueryModel getActiveQueryForUser(String userEmail) {
        if (userEmail == null || userEmail.trim().isEmpty()) return null;
        try {
            Firestore db = getDb();
            ApiFuture<QuerySnapshot> future = db.collection(COLLECTION_NAME)
                    .whereEqualTo("userEmail", userEmail.trim().toLowerCase())
                    .get();
            List<QueryDocumentSnapshot> docs = future.get().getDocuments();
            SupportQueryModel latestResolved = null;

            for (QueryDocumentSnapshot doc : docs) {
                SupportQueryModel q = doc.toObject(SupportQueryModel.class);
                if (q != null) {
                    if (q.getQueryId() == null) q.setQueryId(doc.getId());
                    // If open, return immediately
                    if ("OPEN".equalsIgnoreCase(q.getStatus())) {
                        return q;
                    }
                    if (latestResolved == null || q.getLastUpdated() > latestResolved.getLastUpdated()) {
                        latestResolved = q;
                    }
                }
            }
            return latestResolved;
        } catch (Exception e) {
            System.err.println("Notice: Could not load user query: " + e.getMessage());
        }
        return null;
    }

    public List<SupportQueryModel> getAllQueries() {
        List<SupportQueryModel> list = new ArrayList<>();
        try {
            Firestore db = getDb();
            ApiFuture<QuerySnapshot> future = db.collection(COLLECTION_NAME).get();
            List<QueryDocumentSnapshot> docs = future.get().getDocuments();
            for (QueryDocumentSnapshot doc : docs) {
                SupportQueryModel q = doc.toObject(SupportQueryModel.class);
                if (q != null) {
                    if (q.getQueryId() == null) q.setQueryId(doc.getId());
                    list.add(q);
                }
            }
            list.sort((a, b) -> Long.compare(b.getLastUpdated(), a.getLastUpdated()));
        } catch (Exception e) {
            System.err.println("Notice: Could not fetch all queries: " + e.getMessage());
        }
        return list;
    }

    public void addMessage(String queryId, SupportMessageModel message) {
        if (queryId == null || message == null) return;
        try {
            SupportQueryModel q = getQueryById(queryId);
            if (q != null) {
                q.getMessages().add(message);
                q.setLastMessage(message.getText());
                q.setLastUpdated(System.currentTimeMillis());
                saveQuery(q);
            }
        } catch (Exception e) {
            System.err.println("Notice: Could not append message to query: " + e.getMessage());
        }
    }

    public void resolveQuery(String queryId) {
        if (queryId == null || queryId.trim().isEmpty()) return;
        try {
            Firestore db = getDb();
            db.collection(COLLECTION_NAME).document(queryId.trim()).update(
                    "status", "RESOLVED",
                    "lastUpdated", System.currentTimeMillis()
            ).get();
        } catch (Exception e) {
            System.err.println("Notice: Could not resolve query: " + e.getMessage());
        }
    }

    public void submitFeedback(String queryId, int rating, String comment) {
        if (queryId == null || queryId.trim().isEmpty()) return;
        try {
            Firestore db = getDb();
            db.collection(COLLECTION_NAME).document(queryId.trim()).update(
                    "feedbackRating", rating,
                    "feedbackComment", comment != null ? comment : "",
                    "feedbackGiven", true,
                    "lastUpdated", System.currentTimeMillis()
            ).get();
        } catch (Exception e) {
            System.err.println("Notice: Could not submit feedback: " + e.getMessage());
        }
    }
}
