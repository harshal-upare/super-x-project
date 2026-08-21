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
            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofMillis(2500))
                    .build();
            
            URI uri = URI.create("https://identitytoolkit.googleapis.com/v1/accounts:signUp?key=" + APIKey);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .timeout(Duration.ofMillis(2500))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(req.toString()))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return true;
            } else {
                if (response.body() != null && response.body().contains("EMAIL_EXISTS")) {
                    return true;
                }
                return false;
            }

        } catch (Exception e) {
            System.err.println("Firebase signUp notice: " + e.getMessage());
        }

        return false;
    }

    public boolean signIn(String mail, String password) {
        String result = authenticateUser(mail, password);
        return "SUCCESS".equals(result);
    }

    /**
     * Authenticates user against Firebase and returns status:
     * - SUCCESS
     * - EMAIL_NOT_FOUND
     * - INVALID_PASSWORD
     * - USER_DISABLED
     * - TOO_MANY_ATTEMPTS
     * - NETWORK_ERROR
     * - INVALID_CREDENTIALS
     */
    public String authenticateUser(String mail, String password) {
        if (mail == null || mail.trim().isEmpty()) return "EMAIL_REQUIRED";
        if (password == null || password.isEmpty()) return "PASSWORD_REQUIRED";

        JSONObject req = new JSONObject().put("email", mail.trim()).put("password", password).put("returnSecureToken", true);
        try {
            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofMillis(2500))
                    .build();
            
            URI uri = URI.create("https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=" + APIKey);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .timeout(Duration.ofMillis(2500))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(req.toString()))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return "SUCCESS";
            } else {
                String body = response.body();
                if (body != null) {
                    try {
                        JSONObject json = new JSONObject(body);
                        if (json.has("error")) {
                            String msg = json.getJSONObject("error").optString("message", "");
                            if (msg.contains("EMAIL_NOT_FOUND")) {
                                return "EMAIL_NOT_FOUND";
                            } else if (msg.contains("INVALID_PASSWORD") || msg.contains("INVALID_LOGIN_CREDENTIALS")) {
                                return "INVALID_PASSWORD";
                            } else if (msg.contains("USER_DISABLED")) {
                                return "USER_DISABLED";
                            } else if (msg.contains("TOO_MANY_ATTEMPTS")) {
                                return "TOO_MANY_ATTEMPTS";
                            }
                        }
                    } catch (Exception ignored) {}
                }
                return "INVALID_CREDENTIALS";
            }
            
        } catch (Exception e) {
            System.err.println("Firebase signIn notice: " + e.getMessage());
            return "NETWORK_ERROR";
        }
    }

    public void addUser(String name, String mail, String num, String password, String role) {
        AuthenticateModel obj = new AuthenticateModel(name, mail, num, password, role);
        dao.addUser(obj);
    }

    public AuthenticateModel getUser(String email) {
        return dao.getUserByEmail(email);
    }
}
