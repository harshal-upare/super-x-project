package com.desgin.view.farmer.Swapnil;

import java.util.ArrayList;
import java.util.List;

public class FarmerProfileStore {

    public static String name = "Harshal Upare";
    public static String email = "harshal.farmer@farmmail.com";
    public static String phone = "+91 98765 43210";
    public static String town = "Pune";
    public static String district = "Pune";
    public static String state = "Maharashtra";
    public static String pincode = "411058";

    public static synchronized void setCredentials(String newName, String newEmail, String newPhone) {
        if (newName != null && !newName.trim().isEmpty()) {
            name = newName.trim();
        }
        if (newEmail != null && !newEmail.trim().isEmpty()) {
            email = newEmail.trim();
        }
        if (newPhone != null && !newPhone.trim().isEmpty()) {
            phone = newPhone.trim();
        }
    }

    // Listeners for location change to refresh views
    private static final List<Runnable> locationListeners = new ArrayList<>();

    public static synchronized void setLocation(String newTown, String newDistrict, String newState, String newPincode) {
        if (newTown != null && !newTown.trim().isEmpty()) {
            town = newTown.trim();
        }
        if (newDistrict != null && !newDistrict.trim().isEmpty()) {
            district = newDistrict.trim();
        }
        if (newState != null && !newState.trim().isEmpty()) {
            state = newState.trim();
        }
        if (newPincode != null && !newPincode.trim().isEmpty()) {
            pincode = newPincode.trim();
        }

        // Notify listeners
        for (Runnable r : locationListeners) {
            try {
                r.run();
            } catch (Exception ignored) {}
        }
    }

    public static synchronized void addLocationListener(Runnable r) {
        if (r != null && !locationListeners.contains(r)) {
            locationListeners.add(r);
        }
    }
}
