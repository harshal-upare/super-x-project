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
    
}
