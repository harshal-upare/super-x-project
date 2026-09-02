package com.desgin.view.provider;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class ProviderSettings {

    public static ScrollPane getSettingsSection() {
        Text headerTitle = new Text("Provider Account & Operational Settings");
        headerTitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 26px; -fx-font-weight: bold; -fx-fill: #4A2C20;");

        Text headerSubtitle = new Text("Manage your agricultural agency details, verified payout bank accounts, rental policies, and notification alerts.");
        headerSubtitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-fill: #806A5B;");

        VBox titleBox = new VBox(4, headerTitle, headerSubtitle);

        // Section 1: Business & Agency Profile
        VBox businessCard = createBusinessCard();

        // Section 2: Bank Payout Account
        VBox bankCard = createBankCard();

        // Section 3: Rental Terms & Security Policy
        VBox policyCard = createPolicyCard();

        // Section 4: Notification Alerts
        VBox notifCard = createNotifCard();

        // Save Button & Status Label
        Label statusLabel = new Label("");
        statusLabel.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #2E7D32;");

        Button saveAllBtn = new Button("💾  Save All Settings & Preferences");
        saveAllBtn.setPrefHeight(44);
        saveAllBtn.setPrefWidth(300);
        saveAllBtn.setStyle("-fx-background-color: #2E7D32; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 13.5px; -fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand;");

        saveAllBtn.setOnAction(e -> {
            statusLabel.setText("✔ All provider business profile & payout settings updated successfully!");
        });

        VBox botBox = new VBox(10, saveAllBtn, statusLabel);
        botBox.setAlignment(Pos.CENTER_LEFT);

        VBox content = new VBox(22, titleBox, businessCard, bankCard, policyCard, notifCard, botBox);
        content.setPadding(new Insets(25, 30, 35, 30));

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        return scrollPane;
    }

    private static VBox createBusinessCard() {
        Text title = new Text("1. Agricultural Agency & Provider Information");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 16px; -fx-font-weight: bold; -fx-fill: #4A2C20;");

        GridPane form = new GridPane();
        form.setHgap(15);
        form.setVgap(12);

        form.add(createLabel("Agency / Trade Name:"), 0, 0);
        form.add(createTextField("Rajesh Agro Services & Farm Rentals"), 1, 0);

        form.add(createLabel("Owner / Contact Person:"), 0, 1);
        form.add(createTextField("Rajesh Patil"), 1, 1);

        form.add(createLabel("Phone / WhatsApp:"), 0, 2);
        form.add(createTextField("+91 98220 12345"), 1, 2);

        form.add(createLabel("Registered Email:"), 0, 3);
        form.add(createTextField("rajesh.agro@farmmail.com"), 1, 3);

        form.add(createLabel("GSTIN / Trade License:"), 0, 4);
        form.add(createTextField("27AAACR1234F1Z5 (Verified ✓)"), 1, 4);

        form.add(createLabel("Primary Service Hub:"), 0, 5);
        form.add(createTextField("Baramati & Pune Rural Agro Complex"), 1, 5);

        VBox card = new VBox(12, title, form);
        card.setPadding(new Insets(18));
        card.setStyle("-fx-background-color: #F5EFE6; -fx-background-radius: 14; -fx-border-color: #D8C7B5; -fx-border-width: 1; -fx-border-radius: 14;");
        return card;
    }

    private static VBox createBankCard() {
        Text title = new Text("2. Verified Payout Bank Account Details");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 16px; -fx-font-weight: bold; -fx-fill: #4A2C20;");

        GridPane form = new GridPane();
        form.setHgap(15);
        form.setVgap(12);

        form.add(createLabel("Account Holder Name:"), 0, 0);
        form.add(createTextField("Rajesh V. Patil"), 1, 0);

        form.add(createLabel("Bank Name:"), 0, 1);
        form.add(createTextField("HDFC Bank Ltd."), 1, 1);

        form.add(createLabel("Account Number:"), 0, 2);
        form.add(createTextField("50100293848842"), 1, 2);

        form.add(createLabel("IFSC Code:"), 0, 3);
        form.add(createTextField("HDFC0001024"), 1, 3);

        form.add(createLabel("UPI ID for Instant Settlement:"), 0, 4);
        form.add(createTextField("rajesh.agro@okhdfcbank"), 1, 4);

        VBox card = new VBox(12, title, form);
        card.setPadding(new Insets(18));
        card.setStyle("-fx-background-color: #F5EFE6; -fx-background-radius: 14; -fx-border-color: #D8C7B5; -fx-border-width: 1; -fx-border-radius: 14;");
        return card;
    }

    private static VBox createPolicyCard() {
        Text title = new Text("3. Rental Terms, Radius & Security Deposit Policy");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 16px; -fx-font-weight: bold; -fx-fill: #4A2C20;");

        GridPane form = new GridPane();
        form.setHgap(15);
        form.setVgap(12);

        form.add(createLabel("Maximum Operating Radius (km):"), 0, 0);
        form.add(createTextField("50 km from Base Hub"), 1, 0);

        form.add(createLabel("Default Security Deposit (₹):"), 0, 1);
        form.add(createTextField("₹2,000 (Refunded upon inspection)"), 1, 1);

        form.add(createLabel("Standard Driver Daily Wage (₹):"), 0, 2);
        form.add(createTextField("₹500 / day"), 1, 2);

        form.add(createLabel("Cancellation Policy:"), 0, 3);
        form.add(createTextField("Full refund if cancelled 24h prior to start"), 1, 3);

        VBox card = new VBox(12, title, form);
        card.setPadding(new Insets(18));
        card.setStyle("-fx-background-color: #F5EFE6; -fx-background-radius: 14; -fx-border-color: #D8C7B5; -fx-border-width: 1; -fx-border-radius: 14;");
        return card;
    }

    private static VBox createNotifCard() {
        Text title = new Text("4. Booking Alerts & Notification Channels");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 16px; -fx-font-weight: bold; -fx-fill: #4A2C20;");

        CheckBox cb1 = new CheckBox("Send Instant WhatsApp notification when farmer books equipment");
        cb1.setSelected(true);
        cb1.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-text-fill: #4A2C20;");

        CheckBox cb2 = new CheckBox("Send SMS alerts for urgent booking requests due in < 24 hours");
        cb2.setSelected(true);
        cb2.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-text-fill: #4A2C20;");

        CheckBox cb3 = new CheckBox("Email monthly earnings statement & tax invoice summary");
        cb3.setSelected(true);
        cb3.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-text-fill: #4A2C20;");

        VBox card = new VBox(10, title, cb1, cb2, cb3);
        card.setPadding(new Insets(18));
        card.setStyle("-fx-background-color: #F5EFE6; -fx-background-radius: 14; -fx-border-color: #D8C7B5; -fx-border-width: 1; -fx-border-radius: 14;");
        return card;
    }

    private static Label createLabel(String text) {
        Label l = new Label(text);
        l.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-font-weight: bold; -fx-text-fill: #5C4033;");
        return l;
    }

    private static TextField createTextField(String text) {
        TextField tf = new TextField(text);
        tf.setPrefHeight(36);
        tf.setPrefWidth(320);
        tf.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #D8C7B5; -fx-border-radius: 6; -fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-text-fill: #4A2C20;");
        return tf;
    }
}
