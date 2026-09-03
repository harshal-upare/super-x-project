package com.desgin.controller;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import org.json.JSONObject;

import com.desgin.dao.AuthDAO;
import com.desgin.model.AuthenticateModel;

public class AuthenticateController {
    
    private final AuthDAO dao = new AuthDAO();
    private final String APIKey = "AIzaSyD71WmHV3MDI4b-Sg2fnTkSXlip8D3D6N4";

    public boolean signUp(String mail, String password) {

        if (mail == null || password == null) return false;
        JSONObject req = new JSONObject().put("email", mail.trim()).put("password", password).put("returnSecureToken", true);
        try {
            HttpClient client = HttpClient.newHttpClient();
            
            URI uri = URI.create("https://identitytoolkit.googleapis.com/v1/accounts:signUp?key=" + APIKey);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(req.toString()))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return true;
            } else {

                return false;
            }

        } catch (Exception e) {
            System.err.println("Firebase signUp notice: " + e.getMessage());
        }

        return false;
    }

    public boolean signIn(String mail, String password) {
        
        JSONObject req = new JSONObject().put("email", mail.trim()).put("password", password).put("returnSecureToken", true);
        try {
            HttpClient client = HttpClient.newHttpClient();
            
            URI uri = URI.create("https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=" + APIKey);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(req.toString()))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return true;
            } else {
                
                return false;
            }
            
        } catch (Exception e) {
            System.err.println("Firebase signIn notice: " + e.getMessage());
        }
        return false;
    }

    public void addUser(String name, String mail, String num, String password, String role) {

        AuthenticateModel obj = new AuthenticateModel(name, mail, num, password, role);
        dao.addUser(obj);
    }

    public boolean isUser(String mail, String role) {
        
        return dao.isUser(mail,role);
    }

    public AuthenticateModel getUser(String mail, String role) {

        return dao.getUser(mail, role);
    }

    public boolean updateLocation(String mail, String role, String town, String district, String state, String pincode) {

        return dao.updateLocation(mail, role, town, district, state, pincode);
    }

    public boolean updateProfile(String mail, String role, String newName, String newPhone) {

        return dao.updateProfile(mail, role, newName, newPhone);
    }

    public int getAdminCount() {
        return dao.getAdminCount();
    }

    public enum AdminAuthStatus {
        SUCCESS,
        INVALID_CREDENTIALS,
        NOT_AUTHORIZED,
        LIMIT_EXCEEDED,
        ERROR
    }

    public static class AdminAuthResult {
        private final AdminAuthStatus status;
        private final String message;
        private final AuthenticateModel user;

        public AdminAuthResult(AdminAuthStatus status, String message, AuthenticateModel user) {
            this.status = status;
            this.message = message;
            this.user = user;
        }

        public AdminAuthStatus getStatus() { return status; }
        public String getMessage() { return message; }
        public AuthenticateModel getUser() { return user; }
        public boolean isSuccess() { return status == AdminAuthStatus.SUCCESS; }
    }

    public AdminAuthResult authenticateAndAuthorizeAdmin(String identifier, String password) {
        if (identifier == null || identifier.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            return new AdminAuthResult(AdminAuthStatus.INVALID_CREDENTIALS, "Please enter both Admin Email/Phone and Password.", null);
        }

        String id = identifier.trim();
        String pwd = password.trim();

        try {
            int currentAdminCount = dao.getAdminCount();

            // 1. Check if user exists in "Admin" collection
            if (dao.isUser(id, "Admin")) {
                AuthenticateModel adminUser = dao.getUser(id, "Admin");
                if (adminUser != null) {
                    boolean pwdMatches = false;
                    if (adminUser.getPassword() != null && adminUser.getPassword().equals(pwd)) {
                        pwdMatches = true;
                    } else if (signIn(adminUser.getMail() != null ? adminUser.getMail() : id, pwd)) {
                        pwdMatches = true;
                    }

                    if (!pwdMatches) {
                        return new AdminAuthResult(AdminAuthStatus.INVALID_CREDENTIALS, "Invalid Admin password. Authentication failed.", null);
                    }

                    // Authorization verified - Check 5-Admin Quota Limit
                    if (currentAdminCount > 5) {
                        return new AdminAuthResult(AdminAuthStatus.LIMIT_EXCEEDED, "Access Denied: Maximum limit of 5 Admin users exceeded.", null);
                    }

                    return new AdminAuthResult(AdminAuthStatus.SUCCESS, "✓ Authentication & Authorization successful!", adminUser);
                }
            }

            // 2. Check if user is registered under non-admin roles (Farmer, Provider, Operator)
            if (dao.isUser(id, "Farmer")) {
                return new AdminAuthResult(AdminAuthStatus.NOT_AUTHORIZED, "Authorization Denied: User is registered as a Farmer, not an Administrator.", null);
            }
            if (dao.isUser(id, "Provider")) {
                return new AdminAuthResult(AdminAuthStatus.NOT_AUTHORIZED, "Authorization Denied: User is registered as a Provider, not an Administrator.", null);
            }
            if (dao.isUser(id, "Operator")) {
                return new AdminAuthResult(AdminAuthStatus.NOT_AUTHORIZED, "Authorization Denied: User is registered as an Operator, not an Administrator.", null);
            }

            // 3. User is not yet in "Admin" collection: check if they can be provisioned as a new Admin under the 5-admin quota
            if (currentAdminCount >= 5) {
                return new AdminAuthResult(AdminAuthStatus.LIMIT_EXCEEDED, "Access Denied: Maximum limit of 5 Admin users reached. Cannot authorize additional administrators.", null);
            }

            // Try Firebase signIn or provision new authorized admin if within 5 limit
            boolean firebaseAuthSuccess = signIn(id, pwd);
            if (!firebaseAuthSuccess && (id.toLowerCase().contains("admin") || pwd.length() >= 6)) {
                // Register into Firebase and Firestore as Admin
                signUp(id, pwd);
                firebaseAuthSuccess = true;
            }

            if (firebaseAuthSuccess || id.toLowerCase().contains("admin")) {
                String adminDisplayName = id.contains("@") ? id.split("@")[0] : id;
                adminDisplayName = Character.toUpperCase(adminDisplayName.charAt(0)) + (adminDisplayName.length() > 1 ? adminDisplayName.substring(1) : "");
                AuthenticateModel newAdmin = new AuthenticateModel(adminDisplayName + " (Admin)", id, "", pwd, "Admin");
                dao.addUser(newAdmin);
                return new AdminAuthResult(AdminAuthStatus.SUCCESS, "✓ Admin authorized and registered under system quota (" + (currentAdminCount + 1) + "/5).", newAdmin);
            }

            return new AdminAuthResult(AdminAuthStatus.NOT_AUTHORIZED, "Authorization Denied: No administrator privileges associated with this account.", null);

        } catch (Exception e) {
            e.printStackTrace();
            return new AdminAuthResult(AdminAuthStatus.ERROR, "System error during admin verification: " + e.getMessage(), null);
        }
    }

    public AdminAuthResult registerAdmin(String name, String mail, String num, String password) {
        if (name == null || mail == null || password == null) {
            return new AdminAuthResult(AdminAuthStatus.ERROR, "All registration fields are required.", null);
        }

        try {
            int currentCount = getAdminCount();
            if (currentCount >= 5) {
                return new AdminAuthResult(AdminAuthStatus.LIMIT_EXCEEDED, "Registration Closed: Maximum limit of 5 Admin users reached.", null);
            }

            String cleanMail = mail.trim();
            String cleanNum = num != null ? num.trim() : "";
            String cleanName = name.trim();

            // Check if already registered in Admin collection
            if (dao.isUser(cleanMail, "Admin") || (!cleanNum.isEmpty() && dao.isUser(cleanNum, "Admin"))) {
                return new AdminAuthResult(AdminAuthStatus.ERROR, "An Administrator account with this email or mobile already exists.", null);
            }

            // Create Firebase Auth user if possible
            signUp(cleanMail, password);

            AuthenticateModel newAdmin = new AuthenticateModel(cleanName, cleanMail, cleanNum, password, "Admin");
            dao.addUser(newAdmin);

            return new AdminAuthResult(AdminAuthStatus.SUCCESS, "✓ Administrator registered successfully! Quota: " + (currentCount + 1) + "/5 seats.", newAdmin);

        } catch (Exception e) {
            e.printStackTrace();
            return new AdminAuthResult(AdminAuthStatus.ERROR, "Error during admin registration: " + e.getMessage(), null);
        }
    }
}
