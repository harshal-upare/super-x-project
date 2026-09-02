package com.desgin.view.operator;

import java.util.ArrayList;
import java.util.List;

public class OperatorProfileStore {

    public static String name = "Ramesh Chavan";
    public static String phone = "+91 94231 98765";
    public static String zone = "Pune / Baramati Sector";
    public static String licenseNo = "MH-12-HM-88492 (Valid Heavy Agri)";
    public static String badge = "Certified Pro";
    public static String status = "Available for Field Shifts";

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

        // Notify all registered UI listeners to dynamically re-render info
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
}
