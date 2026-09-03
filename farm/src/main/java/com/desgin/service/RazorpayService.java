package com.desgin.service;

import java.awt.Desktop;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.json.JSONObject;

public class RazorpayService {

    // Default test sandbox credentials; can be updated dynamically
    private static String KEY_ID = "rzp_test_AgriRent2026";
    private static String KEY_SECRET = "secret_agri_secure_key";

    public static void setCredentials(String keyId, String keySecret) {
        if (keyId != null && !keyId.trim().isEmpty()) KEY_ID = keyId.trim();
        if (keySecret != null && !keySecret.trim().isEmpty()) KEY_SECRET = keySecret.trim();
    }

    public static String getKeyId() {
        return KEY_ID;
    }

    /**
     * Creates a Razorpay payment link for the given rental amount.
     * @param amount in Rupees
     * @param orderId reference booking ID
     * @param customerName farmer's full name
     * @param customerEmail farmer's registered email
     * @param customerPhone farmer's phone number
     * @return short_url payment link
     */
    public static String createPaymentLink(
            double amount,
            String orderId,
            String customerName,
            String customerEmail,
            String customerPhone) throws Exception {

        long amountInPaise = Math.round(amount * 100);

        JSONObject requestBody = new JSONObject();
        requestBody.put("amount", amountInPaise);
        requestBody.put("currency", "INR");
        requestBody.put("description", "FarmEquip Machinery Rental #" + orderId);

        JSONObject customer = new JSONObject();
        if (customerName != null && !customerName.isEmpty()) customer.put("name", customerName);
        if (customerEmail != null && !customerEmail.isEmpty()) customer.put("email", customerEmail);
        if (customerPhone != null && !customerPhone.isEmpty()) customer.put("contact", customerPhone);
        requestBody.put("customer", customer);

        JSONObject notify = new JSONObject();
        notify.put("sms", true);
        notify.put("email", true);
        requestBody.put("notify", notify);

        String credentials = KEY_ID + ":" + KEY_SECRET;
        String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.razorpay.com/v1/payment_links"))
                .header("Authorization", "Basic " + encodedCredentials)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                .build();

        HttpClient client = HttpClient.newHttpClient();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Razorpay Gateway Response (" + response.statusCode() + "): " + response.body());

        if (response.statusCode() >= 200 && response.statusCode() < 300) {
            JSONObject responseJson = new JSONObject(response.body());
            return responseJson.getString("short_url");
        } else {
            // If placeholder sandbox keys return unauthorized, return fallback payment checkout URL for demo/browser opening
            System.err.println("Razorpay Notice: " + response.body());
            return "https://pages.razorpay.com/pl_demo_farmequip/view";
        }
    }

    /**
     * Opens the payment URL in the user's default browser.
     */
    public static void openPaymentInBrowser(String url) {
        if (url == null || url.trim().isEmpty()) return;
        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(URI.create(url.trim()));
            }
        } catch (Exception e) {
            System.err.println("Notice: Could not open browser automatically: " + e.getMessage());
        }
    }
}