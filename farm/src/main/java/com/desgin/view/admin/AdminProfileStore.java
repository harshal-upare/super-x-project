package com.desgin.view.admin;

import java.util.ArrayList;
import java.util.List;

public class AdminProfileStore {

    public static String adminName = "Super Administrator";
    public static String adminEmail = "admin@farmequip.com";
    public static String adminPhone = "+91 98000 00001";
    public static String adminRole = "Master Admin";
    public static String adminProfilePic = "";
    public static final int MAX_ADMIN_LIMIT = 5;

    private static final List<Runnable> listeners = new ArrayList<>();

    public static synchronized void setAdminProfile(String name, String email, String phone, String role) {
        setAdminProfile(name, email, phone, role, adminProfilePic);
    }

    public static synchronized void setAdminProfile(String name, String email, String phone, String role, String pic) {
        if (name != null && !name.trim().isEmpty()) {
            adminName = name.trim();
        }
        if (email != null && !email.trim().isEmpty()) {
            adminEmail = email.trim();
        }
        if (phone != null && !phone.trim().isEmpty()) {
            adminPhone = phone.trim();
        }
        if (role != null && !role.trim().isEmpty()) {
            adminRole = role.trim();
        }
        adminProfilePic = (pic != null) ? pic.trim() : "";

        notifyListeners();
    }

    public static synchronized void reset() {
        adminName = "Super Administrator";
        adminEmail = "admin@farmequip.com";
        adminPhone = "+91 98000 00001";
        adminRole = "Master Admin";
        adminProfilePic = "";
        notifyListeners();
    }

    public static void notifyListeners() {
        for (Runnable r : new ArrayList<>(listeners)) {
            try {
                r.run();
            } catch (Exception ignored) {}
        }
    }

    public static synchronized void addListener(Runnable r) {
        if (r != null && !listeners.contains(r)) {
            listeners.add(r);
        }
    }

    public static synchronized void removeListener(Runnable r) {
        if (r != null) {
            listeners.remove(r);
        }
    }
}
