package com.desgin.service;

import java.util.ArrayList;
import java.util.List;

import com.desgin.config.FirestoreConfig;
import com.desgin.view.handling_start.Authentication;
import com.desgin.view.handling_start.WelcomePage;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.ListenerRegistration;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class UserStatusWatcher {

    private static final List<ListenerRegistration> activeListeners = new ArrayList<>();
    private static boolean isSuspensionTriggered = false;

    public static synchronized void startWatching(String emailOrPhone, String role, Runnable onSuspendedLogout) {
        if (emailOrPhone == null || emailOrPhone.trim().isEmpty() || role == null) return;

        try {
            Firestore db = FirestoreConfig.getFirestore();
            if (db == null) return;

            String collection = normalizeRole(role);
            String key = emailOrPhone.trim();

            isSuspensionTriggered = false;

            // 1. Direct document listener
            ListenerRegistration regDoc = db.collection(collection).document(key).addSnapshotListener((snapshot, error) -> {
                if (error != null || snapshot == null || !snapshot.exists()) return;
                checkAndTriggerSuspension(snapshot.getString("status"), onSuspendedLogout);
            });
            activeListeners.add(regDoc);

            // 2. Query listener on 'mail' field
            ListenerRegistration regMail = db.collection(collection).whereEqualTo("mail", key).addSnapshotListener((snapshots, error) -> {
                if (error != null || snapshots == null || snapshots.isEmpty()) return;
                for (var doc : snapshots.getDocuments()) {
                    checkAndTriggerSuspension(doc.getString("status"), onSuspendedLogout);
                }
            });
            activeListeners.add(regMail);

            // 3. Query listener on 'num' field
            ListenerRegistration regNum = db.collection(collection).whereEqualTo("num", key).addSnapshotListener((snapshots, error) -> {
                if (error != null || snapshots == null || snapshots.isEmpty()) return;
                for (var doc : snapshots.getDocuments()) {
                    checkAndTriggerSuspension(doc.getString("status"), onSuspendedLogout);
                }
            });
            activeListeners.add(regNum);

        } catch (Exception e) {
            System.err.println("Notice: Could not attach user status watcher: " + e.getMessage());
        }
    }

    private static synchronized void checkAndTriggerSuspension(String status, Runnable onSuspendedLogout) {
        if ("SUSPENDED".equalsIgnoreCase(status) && !isSuspensionTriggered) {
            isSuspensionTriggered = true;
            stopWatching();
            Platform.runLater(() -> {
                try {
                    Alert alert = new Alert(AlertType.WARNING);
                    alert.setTitle("Account Suspended");
                    alert.setHeaderText("Access Revoked by Administrator");
                    alert.setContentText("Your account has been suspended by the platform administrator. You have been logged out.");
                    alert.show();
                } catch (Exception ignored) {}

                if (onSuspendedLogout != null) {
                    onSuspendedLogout.run();
                } else {
                    Authentication auth = new Authentication();
                    WelcomePage.welcomePageStage.setScene(auth.getAuthenticationScene());
                }
            });
        }
    }

    public static synchronized void stopWatching() {
        for (ListenerRegistration reg : activeListeners) {
            try {
                reg.remove();
            } catch (Exception ignored) {}
        }
        activeListeners.clear();
    }

    private static String normalizeRole(String role) {
        if (role == null) return "Farmer";
        String r = role.trim().toUpperCase();
        if (r.contains("FARMER")) return "Farmer";
        if (r.contains("PROVIDER")) return "Provider";
        if (r.contains("OPERATOR")) return "Operator";
        if (r.contains("ADMIN")) return "Admin";
        return role;
    }
}
