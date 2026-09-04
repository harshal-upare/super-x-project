package com.desgin.view.operator;

import java.util.ArrayList;
import java.util.List;

public class OperatorProfileStore {

    public static String email = "operator@farmequip.com";
    public static String name = "Ramesh Chavan";
    public static String phone = "+91 94231 98765";
    public static String zone = "Pune / Baramati Sector";
    public static String licenseNo = "MH-12-HM-88492 (Valid Heavy Agri)";
    public static String badge = "Certified Pro";
    public static String status = "Available for Field Shifts";
    public static String profilePic = "";
    public static String drivingExperience = "3-5 Years (Certified Operator)";
    public static String equipmentProfession = "Tractors & Heavy Tillage";
    public static String licenseImage = "";
    public static boolean availableForShifts = true;
    public static String currentPassword = "";

    // Dynamic Operator KPI Metrics (Strictly 0 defaults until populated from Firestore)
    public static String assignedMachinery = "0 Units";
    public static String assignedMachinerySub = "0 In Shift • 0 Standby";
    public static String activeJobs = "0 Total Jobs";
    public static String activeJobsSub = "0 Completed • 0 Active";
    public static String engineHours = "0 hrs";
    public static String engineHoursSub = "0 hrs this week";
    public static String wagesEarned = "₹0";
    public static String wagesEarnedSub = "₹0 Settled • ₹0 Escrow";

    public static final java.util.Set<String> readNotificationIds = new java.util.HashSet<>();

    public static boolean isNotificationRead(String id) {
        return id != null && readNotificationIds.contains(id);
    }

    public static void markNotificationRead(String id) {
        if (id != null) readNotificationIds.add(id);
    }

    public static void markAllNotificationsRead(java.util.Collection<String> ids) {
        if (ids != null) readNotificationIds.addAll(ids);
    }

    private static final List<Runnable> profileListeners = new ArrayList<>();

    public static synchronized void setProfile(String newName, String newPhone, String newZone, String newLicense) {
        if (newName != null && !newName.trim().isEmpty()) {
            name = newName.trim();
        }
        if (newPhone != null && !newPhone.trim().isEmpty()) {
            phone = newPhone.trim();
        }
        if (newZone != null && !newZone.trim().isEmpty()) {
            zone = newZone.trim();
        }
        if (newLicense != null && !newLicense.trim().isEmpty()) {
            licenseNo = newLicense.trim();
        }

        notifyListeners();
    }

    public static synchronized void setProfilePic(String pic) {
        profilePic = (pic != null && !pic.trim().isEmpty()) ? pic.trim() : "";
        notifyListeners();
    }

    public static synchronized void setBusinessProfile(String newName, String newPhone, String newEmail,
                                                      String newExperience, String newProfession,
                                                      String newPhoto, String newLicenseImg) {
        if (newName != null && !newName.trim().isEmpty()) name = newName.trim();
        if (newPhone != null && !newPhone.trim().isEmpty()) phone = newPhone.trim();
        if (newEmail != null && !newEmail.trim().isEmpty()) email = newEmail.trim();
        if (newExperience != null && !newExperience.trim().isEmpty()) drivingExperience = newExperience.trim();
        if (newProfession != null && !newProfession.trim().isEmpty()) equipmentProfession = newProfession.trim();
        if (newPhoto != null && !newPhoto.trim().isEmpty()) profilePic = newPhoto.trim();
        if (newLicenseImg != null && !newLicenseImg.trim().isEmpty()) licenseImage = newLicenseImg.trim();

        notifyListeners();
    }

    public static synchronized void setMetrics(String machinery, String machSub, String jobs, String jobsSub, String hours, String hoursSub, String wages, String wageSub) {
        if (machinery != null) assignedMachinery = machinery;
        if (machSub != null) assignedMachinerySub = machSub;
        if (jobs != null) activeJobs = jobs;
        if (jobsSub != null) activeJobsSub = jobsSub;
        if (hours != null) engineHours = hours;
        if (hoursSub != null) engineHoursSub = hoursSub;
        if (wages != null) wagesEarned = wages;
        if (wageSub != null) wagesEarnedSub = wageSub;

        notifyListeners();
    }

    public static void notifyListeners() {
        for (Runnable r : new ArrayList<>(profileListeners)) {
            try {
                r.run();
            } catch (Exception ignored) {}
        }
    }

    public static synchronized void addProfileListener(Runnable r) {
        if (r != null && !profileListeners.contains(r)) {
            profileListeners.add(r);
        }
    }

    public static synchronized void removeProfileListener(Runnable r) {
        if (r != null) {
            profileListeners.remove(r);
        }
    }

    public static synchronized void setAvailability(boolean available) {
        availableForShifts = available;
        status = available ? "Available for Field Shifts" : "Not Available / Off-duty";
        notifyListeners();

        new Thread(() -> {
            try {
                com.google.cloud.firestore.Firestore db = com.desgin.config.FirestoreConfig.getFirestore();
                if (db != null && email != null && !email.trim().isEmpty()) {
                    java.util.Map<String, Object> updateMap = new java.util.HashMap<>();
                    updateMap.put("available", available);
                    updateMap.put("status", available ? "AVAILABLE" : "NOT AVAILABLE");
                    db.collection("Operator").document(email).set(updateMap, com.google.cloud.firestore.SetOptions.merge());
                }
            } catch (Exception ignored) {}
        }).start();
    }

    public static synchronized void toggleAvailability() {
        setAvailability(!availableForShifts);
    }
}
