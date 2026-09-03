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

    // Listeners for credentials change to refresh views dynamically
    private static final List<Runnable> profileListeners = new ArrayList<>();

    // Listeners for location change to refresh views
    private static final List<Runnable> locationListeners = new ArrayList<>();

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
        notifyProfileListeners();
    }

    public static synchronized void setFullProfile(String newName, String newEmail, String newPhone, String newTown, String newDistrict, String newState, String newPincode) {
        if (newName != null && !newName.trim().isEmpty()) {
            name = newName.trim();
        }
        if (newEmail != null && !newEmail.trim().isEmpty()) {
            email = newEmail.trim();
        }
        if (newPhone != null && !newPhone.trim().isEmpty()) {
            phone = newPhone.trim();
        }
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
        notifyProfileListeners();
        notifyLocationListeners();
    }

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

        notifyLocationListeners();
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

    private static void notifyProfileListeners() {
        for (Runnable r : new ArrayList<>(profileListeners)) {
            try {
                r.run();
            } catch (Exception ignored) {}
        }
    }

    private static void notifyLocationListeners() {
        for (Runnable r : new ArrayList<>(locationListeners)) {
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

    public static synchronized void removeLocationListener(Runnable r) {
        if (r != null) {
            locationListeners.remove(r);
        }
    }
}
