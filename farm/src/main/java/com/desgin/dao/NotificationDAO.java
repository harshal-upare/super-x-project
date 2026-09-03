package com.desgin.dao;

import java.util.ArrayList;
import java.util.List;

import com.desgin.config.FirestoreConfig;
import com.desgin.model.NotificationModel;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;

public class NotificationDAO {

    private static final String COLLECTION_NAME = "notifications";

    private Firestore getDb() {
        return FirestoreConfig.getFirestore();
    }

    public void sendNotification(NotificationModel notif) {
        if (notif == null || notif.getUserId() == null) return;
        try {
            if (notif.getNotificationId() == null || notif.getNotificationId().trim().isEmpty()) {
                notif.setNotificationId("NOTIF_" + System.currentTimeMillis());
            }
            Firestore db = getDb();
            DocumentReference docRef = db.collection(COLLECTION_NAME).document(notif.getNotificationId());
            ApiFuture<WriteResult> future = docRef.set(notif);
            future.get();
        } catch (Exception e) {
            System.err.println("Notice: Could not send notification: " + e.getMessage());
        }
    }

    public List<NotificationModel> getNotificationsByUser(String userId) {
        List<NotificationModel> list = new ArrayList<>();
        if (userId == null || userId.trim().isEmpty()) return list;
        try {
            Firestore db = getDb();
            ApiFuture<QuerySnapshot> future = db.collection(COLLECTION_NAME)
                    .whereEqualTo("userId", userId.trim().toLowerCase())
                    .get();
            List<QueryDocumentSnapshot> docs = future.get().getDocuments();
            for (QueryDocumentSnapshot doc : docs) {
                NotificationModel n = doc.toObject(NotificationModel.class);
                if (n != null) {
                    if (n.getNotificationId() == null) n.setNotificationId(doc.getId());
                    list.add(n);
                }
            }
            // Sort by timestamp descending (newest first)
            list.sort((a, b) -> Long.compare(b.getTimestamp(), a.getTimestamp()));
        } catch (Exception e) {
            System.err.println("Notice: Could not load notifications: " + e.getMessage());
        }
        return list;
    }

    public int getUnreadCount(String userId) {
        if (userId == null || userId.trim().isEmpty()) return 0;
        int count = 0;
        for (NotificationModel n : getNotificationsByUser(userId)) {
            if (!n.isRead()) count++;
        }
        return count;
    }

    public void markAsRead(String notificationId) {
        if (notificationId == null || notificationId.trim().isEmpty()) return;
        try {
            Firestore db = getDb();
            db.collection(COLLECTION_NAME).document(notificationId).update("read", true).get();
        } catch (Exception e) {
            System.err.println("Notice: Could not mark notification as read: " + e.getMessage());
        }
    }
}
