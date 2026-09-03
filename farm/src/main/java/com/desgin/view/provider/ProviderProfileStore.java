package com.desgin.view.provider;

import java.util.ArrayList;
import java.util.List;

public class ProviderProfileStore {

    public static String name = "Rajesh Patil";
    public static String email = "provider@farmequip.com";
    public static String phone = "+91 98765 00000";
    public static String town = "Pune";
    public static String district = "Pune";
    public static String state = "Maharashtra";
    public static String pincode = "411001";
    public static String profilePic = null;
    public static String accountHolder = "";
    public static String bankName = "";
    public static String accountNumber = "";
    public static String ifsc = "";
    public static String upiId = "";

    // Listeners for credentials/profile change
    private static final List<Runnable> profileListeners = new ArrayList<>();

    // Listeners for location change
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

    public static synchronized void setFullProfile(String newName, String newEmail, String newPhone,
                                                  String newTown, String newDistrict, String newState, String newPincode) {
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

    public static synchronized void setFullProfile(String newName, String newEmail, String newPhone,
                                                  String newTown, String newDistrict, String newState, String newPincode,
                                                  String newProfilePic) {
        setFullProfile(newName, newEmail, newPhone, newTown, newDistrict, newState, newPincode);
        if (newProfilePic != null && !newProfilePic.trim().isEmpty()) {
            profilePic = newProfilePic.trim();
            notifyProfileListeners();
        }
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

    public static synchronized void setProfilePic(String newProfilePic) {
        profilePic = newProfilePic;
        notifyProfileListeners();
    }

    public static synchronized void setBankDetails(String newAccountHolder, String newBankName, String newAccountNumber, String newIfsc, String newUpiId) {
        if (newAccountHolder != null) accountHolder = newAccountHolder.trim();
        if (newBankName != null) bankName = newBankName.trim();
        if (newAccountNumber != null) accountNumber = newAccountNumber.trim();
        if (newIfsc != null) ifsc = newIfsc.trim();
        if (newUpiId != null) upiId = newUpiId.trim();
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

    public static synchronized void notifyListeners() {
        notifyProfileListeners();
        notifyLocationListeners();
    }
}
