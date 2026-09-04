package com.desgin.dao;

import com.desgin.config.FirestoreConfig;
import com.desgin.model.AuthenticateModel;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;

public class AuthDAO {

    private Firestore db = FirestoreConfig.getFirestore();;

    public void addUser(AuthenticateModel objModel) {

        try {
           
            db.collection(objModel.getRole()).document(objModel.getMail()).create(objModel).get();

        } catch(Exception e) {
            
            e.printStackTrace();
        }
    }

    public boolean isUser(String mail, String role) {

        try {
            if (mail == null || mail.trim().isEmpty()) return false;
            String key = mail.trim();

            ApiFuture<DocumentSnapshot> future = db.collection(role).document(key).get();  
            DocumentSnapshot doc = future.get();

            if (doc.exists()) return true;

            // Query by email field
            var queryMail = db.collection(role).whereEqualTo("mail", key).get().get();
            if (!queryMail.isEmpty()) return true;

            // Query by phone number field
            var queryNum = db.collection(role).whereEqualTo("num", key).get().get();
            if (!queryNum.isEmpty()) return true;

        } catch(Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public AuthenticateModel getUser(String mail, String role) {

        try {
            if (mail == null || mail.trim().isEmpty()) return null;
            String key = mail.trim();

            ApiFuture<DocumentSnapshot> future = db.collection(role).document(key).get();  
            DocumentSnapshot doc = future.get();

            if (doc.exists()) {
                return doc.toObject(AuthenticateModel.class);
            }

            // Query by email field
            var queryMail = db.collection(role).whereEqualTo("mail", key).get().get();
            if (!queryMail.isEmpty()) {
                return queryMail.getDocuments().get(0).toObject(AuthenticateModel.class);
            }

            // Query by phone number field
            var queryNum = db.collection(role).whereEqualTo("num", key).get().get();
            if (!queryNum.isEmpty()) {
                return queryNum.getDocuments().get(0).toObject(AuthenticateModel.class);
            }

        } catch(Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean updateLocation(String mail, String role, String town, String district, String state, String pincode) {

        try {
            if (mail == null || mail.trim().isEmpty()) {
                return false;
            }
            String key = mail.trim();

            java.util.Map<String, Object> updates = new java.util.HashMap<>();
            updates.put("town", town != null ? town.trim() : "");
            updates.put("district", district != null ? district.trim() : "");
            updates.put("state", state != null ? state.trim() : "");
            updates.put("pincode", pincode != null ? pincode.trim() : "");

            DocumentSnapshot doc = db.collection(role).document(key).get().get();
            if (doc.exists()) {
                db.collection(role).document(key).set(updates, com.google.cloud.firestore.SetOptions.merge()).get();
                return true;
            }

            var queryMail = db.collection(role).whereEqualTo("mail", key).get().get();
            if (!queryMail.isEmpty()) {
                String docId = queryMail.getDocuments().get(0).getId();
                db.collection(role).document(docId).set(updates, com.google.cloud.firestore.SetOptions.merge()).get();
                return true;
            }

            var queryNum = db.collection(role).whereEqualTo("num", key).get().get();
            if (!queryNum.isEmpty()) {
                String docId = queryNum.getDocuments().get(0).getId();
                db.collection(role).document(docId).set(updates, com.google.cloud.firestore.SetOptions.merge()).get();
                return true;
            }

            db.collection(role).document(key).set(updates, com.google.cloud.firestore.SetOptions.merge()).get();
            return true;
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateBankDetails(String mail, String role, String accountHolder, String bankName, String accountNumber, String ifsc, String upiId) {
        try {
            if (mail == null || mail.trim().isEmpty()) {
                return false;
            }
            String key = mail.trim();

            java.util.Map<String, Object> updates = new java.util.HashMap<>();
            updates.put("accountHolder", accountHolder != null ? accountHolder.trim() : "");
            updates.put("bankName", bankName != null ? bankName.trim() : "");
            updates.put("accountNumber", accountNumber != null ? accountNumber.trim() : "");
            updates.put("ifsc", ifsc != null ? ifsc.trim() : "");
            updates.put("upiId", upiId != null ? upiId.trim() : "");

            DocumentSnapshot doc = db.collection(role).document(key).get().get();
            if (doc.exists()) {
                db.collection(role).document(key).set(updates, com.google.cloud.firestore.SetOptions.merge()).get();
                return true;
            }

            var queryMail = db.collection(role).whereEqualTo("mail", key).get().get();
            if (!queryMail.isEmpty()) {
                String docId = queryMail.getDocuments().get(0).getId();
                db.collection(role).document(docId).set(updates, com.google.cloud.firestore.SetOptions.merge()).get();
                return true;
            }

            db.collection(role).document(key).set(updates, com.google.cloud.firestore.SetOptions.merge()).get();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateProfile(String mail, String role, String newName, String newPhone) {

        try {
            if (mail == null || mail.trim().isEmpty()) {
                return false;
            }
            String key = mail.trim();

            java.util.Map<String, Object> updates = new java.util.HashMap<>();
            if (newName != null && !newName.trim().isEmpty()) {
                updates.put("name", newName.trim());
            }
            if (newPhone != null && !newPhone.trim().isEmpty()) {
                updates.put("num", newPhone.trim());
            }

            DocumentSnapshot doc = db.collection(role).document(key).get().get();
            if (doc.exists()) {
                db.collection(role).document(key).set(updates, com.google.cloud.firestore.SetOptions.merge()).get();
                return true;
            }

            var queryMail = db.collection(role).whereEqualTo("mail", key).get().get();
            if (!queryMail.isEmpty()) {
                String docId = queryMail.getDocuments().get(0).getId();
                db.collection(role).document(docId).set(updates, com.google.cloud.firestore.SetOptions.merge()).get();
                return true;
            }

            var queryNum = db.collection(role).whereEqualTo("num", key).get().get();
            if (!queryNum.isEmpty()) {
                String docId = queryNum.getDocuments().get(0).getId();
                db.collection(role).document(docId).set(updates, com.google.cloud.firestore.SetOptions.merge()).get();
                return true;
            }

            db.collection(role).document(key).set(updates, com.google.cloud.firestore.SetOptions.merge()).get();
            return true;
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateProfilePic(String mail, String role, String imageUrl) {
        try {
            if (mail == null || mail.trim().isEmpty() || imageUrl == null) {
                return false;
            }
            String key = mail.trim();
            java.util.Map<String, Object> updates = new java.util.HashMap<>();
            updates.put("profilePic", imageUrl.trim());

            DocumentSnapshot doc = db.collection(role).document(key).get().get();
            if (doc.exists()) {
                db.collection(role).document(key).set(updates, com.google.cloud.firestore.SetOptions.merge()).get();
                return true;
            }

            var queryMail = db.collection(role).whereEqualTo("mail", key).get().get();
            if (!queryMail.isEmpty()) {
                String docId = queryMail.getDocuments().get(0).getId();
                db.collection(role).document(docId).set(updates, com.google.cloud.firestore.SetOptions.merge()).get();
                return true;
            }

            var queryNum = db.collection(role).whereEqualTo("num", key).get().get();
            if (!queryNum.isEmpty()) {
                String docId = queryNum.getDocuments().get(0).getId();
                db.collection(role).document(docId).set(updates, com.google.cloud.firestore.SetOptions.merge()).get();
                return true;
            }

            db.collection(role).document(key).set(updates, com.google.cloud.firestore.SetOptions.merge()).get();
            return true;
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getAdminCount() {
        try {
            if (db == null) return 0;
            var snapshots = db.collection("Admin").get().get();
            return snapshots.size();
        } catch (Exception e) {
            System.err.println("Notice: Could not count admins: " + e.getMessage());
            return 0;
        }
    }

    public java.util.List<AuthenticateModel> getAllAdminUsers() {
        java.util.List<AuthenticateModel> list = new java.util.ArrayList<>();
        try {
            if (db == null) return list;
            var docs = db.collection("Admin").get().get().getDocuments();
            for (var d : docs) {
                AuthenticateModel m = d.toObject(AuthenticateModel.class);
                if (m != null) {
                    list.add(m);
                }
            }
        } catch (Exception e) {
            System.err.println("Notice: Could not fetch all admins: " + e.getMessage());
        }
        return list;
    }

    public java.util.List<AuthenticateModel> getAllUsers() {
        java.util.List<AuthenticateModel> all = new java.util.ArrayList<>();
        String[] roles = new String[]{"Farmer", "Provider", "Operator", "Admin"};
        for (String role : roles) {
            try {
                if (db != null) {
                    var docs = db.collection(role).get().get().getDocuments();
                    for (var d : docs) {
                        AuthenticateModel m = d.toObject(AuthenticateModel.class);
                        if (m != null) {
                            if (m.getRole() == null || m.getRole().isEmpty()) m.setRole(role);
                            all.add(m);
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println("Notice: Could not fetch users for role " + role + ": " + e.getMessage());
            }
        }
        return all;
    }

    public boolean updatePassword(String mail, String role, String newPassword) {
        if (mail == null || role == null || newPassword == null || db == null) return false;
        try {
            String key = mail.trim();
            java.util.Map<String, Object> update = new java.util.HashMap<>();
            update.put("password", newPassword.trim());

            DocumentSnapshot doc = db.collection(role).document(key).get().get();
            if (doc.exists()) {
                db.collection(role).document(key).set(update, com.google.cloud.firestore.SetOptions.merge()).get();
                return true;
            }

            var queryMail = db.collection(role).whereEqualTo("mail", key).get().get();
            if (!queryMail.isEmpty()) {
                String docId = queryMail.getDocuments().get(0).getId();
                db.collection(role).document(docId).set(update, com.google.cloud.firestore.SetOptions.merge()).get();
                return true;
            }

            var queryNum = db.collection(role).whereEqualTo("num", key).get().get();
            if (!queryNum.isEmpty()) {
                String docId = queryNum.getDocuments().get(0).getId();
                db.collection(role).document(docId).set(update, com.google.cloud.firestore.SetOptions.merge()).get();
                return true;
            }

            db.collection(role).document(key).set(update, com.google.cloud.firestore.SetOptions.merge()).get();
            return true;
        } catch (Exception e) {
            System.err.println("Notice: Failed to update password in Firestore: " + e.getMessage());
            return false;
        }
    }

    public boolean updateUserStatus(String email, String role, String newStatus) {
        if (email == null || role == null || newStatus == null || db == null) return false;
        try {
            java.util.Map<String, Object> update = new java.util.HashMap<>();
            update.put("status", newStatus);
            db.collection(role).document(email.trim()).set(update, com.google.cloud.firestore.SetOptions.merge()).get();
            return true;
        } catch (Exception e) {
            System.err.println("Notice: Failed to update user status: " + e.getMessage());
            return false;
        }
    }

    public boolean updateOperatorBusinessInfo(String email, String name, String phone, String photoUrl, String drivingExp, String equipProf, String licenseImgUrl) {
        return updateOperatorBusinessInfo(email, name, phone, photoUrl, drivingExp, equipProf, licenseImgUrl, null, null);
    }

    public boolean updateOperatorBusinessInfo(String email, String name, String phone, String photoUrl, String drivingExp, String equipProf, String licenseImgUrl, String zone, String licenseNo) {
        if (email == null || db == null) return false;
        try {
            String key = email.trim();
            java.util.Map<String, Object> update = new java.util.HashMap<>();
            if (name != null && !name.trim().isEmpty()) update.put("name", name.trim());
            if (phone != null && !phone.trim().isEmpty()) update.put("num", phone.trim());
            if (photoUrl != null && !photoUrl.trim().isEmpty()) update.put("profilePic", photoUrl.trim());
            if (drivingExp != null && !drivingExp.trim().isEmpty()) update.put("drivingExperience", drivingExp.trim());
            if (equipProf != null && !equipProf.trim().isEmpty()) update.put("equipmentProfession", equipProf.trim());
            if (licenseImgUrl != null && !licenseImgUrl.trim().isEmpty()) update.put("licenseImage", licenseImgUrl.trim());
            if (zone != null && !zone.trim().isEmpty()) update.put("town", zone.trim());
            if (licenseNo != null && !licenseNo.trim().isEmpty()) update.put("licenseNo", licenseNo.trim());

            DocumentSnapshot doc = db.collection("Operator").document(key).get().get();
            if (doc.exists()) {
                db.collection("Operator").document(key).set(update, com.google.cloud.firestore.SetOptions.merge()).get();
                return true;
            }

            var queryMail = db.collection("Operator").whereEqualTo("mail", key).get().get();
            if (!queryMail.isEmpty()) {
                String docId = queryMail.getDocuments().get(0).getId();
                db.collection("Operator").document(docId).set(update, com.google.cloud.firestore.SetOptions.merge()).get();
                return true;
            }

            var queryNum = db.collection("Operator").whereEqualTo("num", key).get().get();
            if (!queryNum.isEmpty()) {
                String docId = queryNum.getDocuments().get(0).getId();
                db.collection("Operator").document(docId).set(update, com.google.cloud.firestore.SetOptions.merge()).get();
                return true;
            }

            db.collection("Operator").document(key).set(update, com.google.cloud.firestore.SetOptions.merge()).get();
            return true;
        } catch (Exception e) {
            System.err.println("Notice: Failed to update operator business info: " + e.getMessage());
            return false;
        }
    }

    public boolean setOperatorAvailability(String identifier, boolean available) {
        if (identifier == null || identifier.trim().isEmpty() || db == null) return false;
        try {
            String key = identifier.trim();
            java.util.Map<String, Object> update = new java.util.HashMap<>();
            update.put("available", available);
            update.put("status", available ? "AVAILABLE" : "BUSY");

            DocumentSnapshot doc = db.collection("Operator").document(key).get().get();
            if (doc.exists()) {
                db.collection("Operator").document(key).set(update, com.google.cloud.firestore.SetOptions.merge()).get();
                return true;
            }

            var qMail = db.collection("Operator").whereEqualTo("mail", key).get().get();
            if (!qMail.isEmpty()) {
                for (var d : qMail.getDocuments()) {
                    db.collection("Operator").document(d.getId()).set(update, com.google.cloud.firestore.SetOptions.merge()).get();
                }
                return true;
            }

            var qNum = db.collection("Operator").whereEqualTo("num", key).get().get();
            if (!qNum.isEmpty()) {
                for (var d : qNum.getDocuments()) {
                    db.collection("Operator").document(d.getId()).set(update, com.google.cloud.firestore.SetOptions.merge()).get();
                }
                return true;
            }

            var qName = db.collection("Operator").whereEqualTo("name", key).get().get();
            if (!qName.isEmpty()) {
                for (var d : qName.getDocuments()) {
                    db.collection("Operator").document(d.getId()).set(update, com.google.cloud.firestore.SetOptions.merge()).get();
                }
                return true;
            }

            db.collection("Operator").document(key).set(update, com.google.cloud.firestore.SetOptions.merge()).get();
            return true;
        } catch (Exception e) {
            System.err.println("Notice: Failed to update operator availability in Firestore: " + e.getMessage());
            return false;
        }
    }

    public java.util.List<AuthenticateModel> getAvailableOperators() {
        java.util.List<AuthenticateModel> operators = new java.util.ArrayList<>();
        if (db == null) return operators;
        try {
            var docs = db.collection("Operator").get().get().getDocuments();
            for (var d : docs) {
                Boolean isAvail = d.getBoolean("available");
                if (Boolean.FALSE.equals(isAvail)) continue;

                String status = d.getString("status");
                if (status != null) {
                    String st = status.trim().toUpperCase();
                    if (st.contains("BUSY") || st.contains("UNAVAILABLE") || st.contains("NOT AVAILABLE") || st.contains("OFFLINE") || st.contains("OFF_DUTY") || st.contains("OFF-DUTY")) {
                        continue;
                    }
                }

                AuthenticateModel m = d.toObject(AuthenticateModel.class);
                if (m != null) {
                    m.setRole("Operator");
                    operators.add(m);
                }
            }
        } catch (Exception e) {
            System.err.println("Notice: Could not load available operators: " + e.getMessage());
        }
        return operators;
    }

    public java.util.Map<String, Integer> getUserRoleCounts() {
        java.util.Map<String, Integer> counts = new java.util.HashMap<>();
        counts.put("Farmer", 0);
        counts.put("Provider", 0);
        counts.put("Operator", 0);
        counts.put("Admin", 0);
        if (db == null) return counts;
        for (String role : new String[]{"Farmer", "Provider", "Operator", "Admin"}) {
            try {
                int size = db.collection(role).get().get().size();
                counts.put(role, size);
            } catch (Exception ignored) {}
        }
        return counts;
    }
}
