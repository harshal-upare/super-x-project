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

    public void sendNotification(String userId, String title, String message, String type) {
        sendNotification(userId, title, message, type, null);
    }

    public void sendNotification(String userId, String title, String message, String type, String relatedId) {
        if (userId == null || userId.trim().isEmpty()) return;
        NotificationModel notif = new NotificationModel(
                "NOTIF_" + System.currentTimeMillis(),
                userId.trim().toLowerCase(),
                title,
                message,
                type,
                relatedId
        );
        sendNotification(notif);
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

    public void markAdminAlertAsRead(String notifId) {
        if (notifId == null || notifId.trim().isEmpty()) return;
        try {
            Firestore db = getDb();
            if (db == null) return;
            java.util.Map<String, Object> data = new java.util.HashMap<>();
            data.put("id", notifId.trim());
            data.put("read", true);
            data.put("readAt", System.currentTimeMillis());
            db.collection("AdminReadAlerts").document(notifId.trim()).set(data).get();

            // Also mark in notifications collection if present
            try {
                db.collection(COLLECTION_NAME).document(notifId.trim()).update("read", true);
            } catch (Exception ignored) {}
        } catch (Exception e) {
            System.err.println("Notice: Could not mark admin alert as read: " + e.getMessage());
        }
    }

    public void markAllAdminAlertsAsRead(List<String> notifIds) {
        if (notifIds == null || notifIds.isEmpty()) return;
        try {
            Firestore db = getDb();
            if (db == null) return;
            for (String id : notifIds) {
                if (id == null || id.trim().isEmpty()) continue;
                java.util.Map<String, Object> data = new java.util.HashMap<>();
                data.put("id", id.trim());
                data.put("read", true);
                data.put("readAt", System.currentTimeMillis());
                db.collection("AdminReadAlerts").document(id.trim()).set(data);
            }
        } catch (Exception e) {
            System.err.println("Notice: Could not mark all admin alerts as read: " + e.getMessage());
        }
    }

    public List<NotificationModel> getAdminNotifications() {
        List<NotificationModel> list = new ArrayList<>();
        java.util.Set<String> seenIds = new java.util.HashSet<>();
        Firestore db = getDb();
        if (db == null) return list;

        try {
            // 0. Load all dismissed/read alert IDs from Firestore
            java.util.Set<String> readIds = new java.util.HashSet<>();
            try {
                var readDocs = db.collection("AdminReadAlerts").get().get().getDocuments();
                for (var d : readDocs) {
                    readIds.add(d.getId());
                }
            } catch (Exception ignored) {}

            // 1. Dynamic Notifications for Newly Added Equipment / Machinery
            try {
                var machs = db.collection("Machinery").get().get();
                for (QueryDocumentSnapshot doc : machs.getDocuments()) {
                    String id = doc.getId();
                    String notifId = "MACH_" + id;
                    if (readIds.contains(notifId) || !seenIds.add(notifId)) continue;

                    String name = doc.getString("name");
                    if (name == null || name.isEmpty()) name = doc.getString("equipmentName");
                    if (name == null) name = "Equipment";
                    String model = doc.getString("model");
                    String providerName = doc.getString("providerName");
                    if (providerName == null || providerName.isEmpty()) providerName = doc.getString("providerEmail");
                    if (providerName == null) providerName = "Fleet Provider";
                    Long rate = doc.getLong("pricePerDay");
                    String rateStr = rate != null ? " • Rate: ₹" + rate + "/day" : "";

                    NotificationModel n = new NotificationModel();
                    n.setNotificationId(notifId);
                    n.setUserId("admin");
                    n.setTitle("🚜 New Equipment Added: " + name);
                    n.setMessage("Model: " + (model != null ? model : "Standard") + " submitted by " + providerName + rateStr);
                    n.setType("EQUIPMENT");
                    n.setRelatedId(id);
                    n.setCreatedAt(doc.getString("createdAt") != null ? doc.getString("createdAt") : "Recent");
                    list.add(n);
                }
            } catch (Exception ignored) {}

            // 2. Dynamic Notifications for Newly Registered Users (Farmer, Provider, Operator)
            // 2a. Farmers
            try {
                var farmers = db.collection("Farmer").get().get();
                for (QueryDocumentSnapshot doc : farmers.getDocuments()) {
                    String id = doc.getId();
                    String notifId = "USER_FARM_" + id;
                    if (readIds.contains(notifId) || !seenIds.add(notifId)) continue;

                    String name = doc.getString("name");
                    if (name == null || name.isEmpty()) name = id;
                    String phone = doc.getString("num");
                    String town = doc.getString("town");
                    String district = doc.getString("district");

                    NotificationModel n = new NotificationModel();
                    n.setNotificationId(notifId);
                    n.setUserId("admin");
                    n.setTitle("👤 New Farmer Registered: " + name);
                    n.setMessage("Phone: " + (phone != null ? phone : "N/A") + " • Location: " + (town != null ? town : "Maharashtra") + (district != null ? ", " + district : ""));
                    n.setType("USER");
                    n.setRelatedId(id);
                    n.setCreatedAt("Recent");
                    list.add(n);
                }
            } catch (Exception ignored) {}

            // 2b. Providers
            try {
                var providers = db.collection("Provider").get().get();
                for (QueryDocumentSnapshot doc : providers.getDocuments()) {
                    String id = doc.getId();
                    String notifId = "USER_PROV_" + id;
                    if (readIds.contains(notifId) || !seenIds.add(notifId)) continue;

                    String name = doc.getString("name");
                    if (name == null || name.isEmpty()) name = id;
                    String phone = doc.getString("num");
                    String town = doc.getString("town");

                    NotificationModel n = new NotificationModel();
                    n.setNotificationId(notifId);
                    n.setUserId("admin");
                    n.setTitle("👤 New Equipment Provider Registered: " + name);
                    n.setMessage("Phone: " + (phone != null ? phone : "N/A") + " • Fleet Hub: " + (town != null ? town : "Maharashtra"));
                    n.setType("USER");
                    n.setRelatedId(id);
                    n.setCreatedAt("Recent");
                    list.add(n);
                }
            } catch (Exception ignored) {}

            // 2c. Operators
            try {
                var operators = db.collection("Operator").get().get();
                for (QueryDocumentSnapshot doc : operators.getDocuments()) {
                    String id = doc.getId();
                    String notifId = "USER_OP_" + id;
                    if (readIds.contains(notifId) || !seenIds.add(notifId)) continue;

                    String name = doc.getString("name");
                    if (name == null || name.isEmpty()) name = id;
                    String phone = doc.getString("num");
                    String prof = doc.getString("equipmentProfession");

                    NotificationModel n = new NotificationModel();
                    n.setNotificationId(notifId);
                    n.setUserId("admin");
                    n.setTitle("👤 New Operator Registered: " + name);
                    n.setMessage("Phone: " + (phone != null ? phone : "N/A") + " • Specialization: " + (prof != null ? prof : "Heavy Machinery"));
                    n.setType("USER");
                    n.setRelatedId(id);
                    n.setCreatedAt("Recent");
                    list.add(n);
                }
            } catch (Exception ignored) {}

            // 3. Dynamic Notifications for Newly Submitted Reviews
            try {
                var reviews = db.collection("Reviews").get().get();
                for (QueryDocumentSnapshot doc : reviews.getDocuments()) {
                    String id = doc.getId();
                    String notifId = "REV_" + id;
                    if (readIds.contains(notifId) || !seenIds.add(notifId)) continue;

                    String machName = doc.getString("machineryName");
                    String farmerName = doc.getString("farmerName");
                    Long rating = doc.getLong("rating");
                    String headline = doc.getString("headline");
                    String comment = doc.getString("comment");
                    String date = doc.getString("date");

                    String stars = (rating != null ? rating + "★" : "★");
                    NotificationModel n = new NotificationModel();
                    n.setNotificationId(notifId);
                    n.setUserId("admin");
                    n.setTitle("⭐ New Review (" + stars + "): " + (machName != null ? machName : "Equipment"));
                    n.setMessage("By " + (farmerName != null ? farmerName : "Farmer") + ": " + (headline != null ? "\"" + headline + "\" - " : "") + (comment != null ? comment : ""));
                    n.setType("REVIEW");
                    n.setRelatedId(id);
                    n.setCreatedAt(date != null ? date : "Recent");
                    list.add(n);
                }
            } catch (Exception ignored) {}

        } catch (Exception e) {
            System.err.println("Notice: Could not load admin dynamic notifications: " + e.getMessage());
        }

        return list;
    }
}

