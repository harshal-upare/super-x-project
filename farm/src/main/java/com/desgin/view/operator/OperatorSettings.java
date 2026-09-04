package com.desgin.view.operator;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class OperatorSettings {

    public static ScrollPane getSettingsSection() {
        Text title = new Text("Operator Settings & Dispatch Preferences");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 26px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text subtitle = new Text("Configure your field availability status, operating radius, machinery dispatch alerts, and language preferences.");
        subtitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-fill: #4B5563;");

        VBox titleBox = new VBox(4, title, subtitle);

        // Section 1: Availability Status
        VBox availCard = createAvailabilityCard();

        // Section 2: Operating Radius & Machinery Preferences
        VBox radiusCard = createRadiusCard();

        // Section 3: Notification Alerts
        VBox notifCard = createNotificationSettingsCard();

        // Section 4: Language
        VBox langCard = createLanguageCard();

        // Save Button
        Button saveBtn = new Button("Save Operator Preferences");
        saveBtn.setPrefHeight(42);
        saveBtn.setStyle("-fx-background-color: #2E7D32; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 0 24 0 24;");

        VBox content = new VBox(20, titleBox, availCard, radiusCard, notifCard, langCard, saveBtn);
        content.setPadding(new Insets(25, 30, 35, 30));

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        return scrollPane;
    }

    private static VBox createAvailabilityCard() {
        Text t = new Text("Field Dispatch Availability");
        t.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 16px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        RadioButton r1 = new RadioButton("🟢 Available for Field Dispatch (Receive new farmer bookings)");
        RadioButton r2 = new RadioButton("🟡 Busy on Active Shift");
        RadioButton r3 = new RadioButton("🔴 On Leave / Off-Duty (Do not assign new jobs)");

        ToggleGroup tg = new ToggleGroup();
        r1.setToggleGroup(tg);
        r2.setToggleGroup(tg);
        r3.setToggleGroup(tg);
        r1.setSelected(OperatorProfileStore.availableForShifts);
        if (!OperatorProfileStore.availableForShifts) r3.setSelected(true);

        r1.setOnAction(e -> {
            OperatorProfileStore.availableForShifts = true;
            OperatorProfileStore.status = "Available for Field Shifts";
            new Thread(() -> {
                try {
                    new com.desgin.dao.AuthDAO().updateUserStatus(OperatorProfileStore.email, "Operator", "ACTIVE");
                } catch (Exception ignored) {}
            }).start();
        });
        r2.setOnAction(e -> {
            OperatorProfileStore.availableForShifts = false;
            OperatorProfileStore.status = "Busy on Active Shift";
            new Thread(() -> {
                try {
                    new com.desgin.dao.AuthDAO().updateUserStatus(OperatorProfileStore.email, "Operator", "BUSY");
                } catch (Exception ignored) {}
            }).start();
        });
        r3.setOnAction(e -> {
            OperatorProfileStore.availableForShifts = false;
            OperatorProfileStore.status = "On Leave / Off-Duty";
            new Thread(() -> {
                try {
                    new com.desgin.dao.AuthDAO().updateUserStatus(OperatorProfileStore.email, "Operator", "OFF_DUTY");
                } catch (Exception ignored) {}
            }).start();
        });

        String rbStyle = "-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-text-fill: #1B4332;";
        r1.setStyle(rbStyle);
        r2.setStyle(rbStyle);
        r3.setStyle(rbStyle);

        VBox b = new VBox(10, t, r1, r2, r3);
        b.setPadding(new Insets(18));
        b.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 12; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 12;");
        return b;
    }

    private static VBox createRadiusCard() {
        Text t = new Text("Operating Range & Machine Preferences");
        t.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 16px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        GridPane g = new GridPane();
        g.setHgap(15);
        g.setVgap(10);

        ComboBox<String> radiusSelect = new ComboBox<>();
        radiusSelect.getItems().addAll("Within 15 km", "Within 30 km (Recommended)", "Within 50 km", "All Sub-District (Baramati / Pune)");
        radiusSelect.setValue("Within 30 km (Recommended)");
        radiusSelect.setPrefWidth(260);

        TextField baseField = new TextField("Baramati Sector 4 Hub, Pune");
        baseField.setPrefHeight(36);
        baseField.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-background-color: white; -fx-border-color: #E2EBE5; -fx-border-radius: 6;");

        g.add(createLabel("Base Dispatch Location:"), 0, 0);
        g.add(baseField, 1, 0);

        g.add(createLabel("Maximum Travel Radius:"), 0, 1);
        g.add(radiusSelect, 1, 1);

        VBox b = new VBox(10, t, g);
        b.setPadding(new Insets(18));
        b.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 12; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 12;");
        return b;
    }

    private static VBox createNotificationSettingsCard() {
        Text t = new Text("Job Dispatch & Alert Preferences");
        t.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 16px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        CheckBox c1 = new CheckBox("Instant SMS alert for new work orders");
        CheckBox c2 = new CheckBox("Push notification when wage payout is credited to bank");
        CheckBox c3 = new CheckBox("Machine service interval reminders (40 hrs prior)");
        CheckBox c4 = new CheckBox("Severe field weather / rain warning notifications");

        c1.setSelected(true);
        c2.setSelected(true);
        c3.setSelected(true);
        c4.setSelected(true);

        String cbStyle = "-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-text-fill: #1B4332;";
        c1.setStyle(cbStyle);
        c2.setStyle(cbStyle);
        c3.setStyle(cbStyle);
        c4.setStyle(cbStyle);

        VBox b = new VBox(10, t, c1, c2, c3, c4);
        b.setPadding(new Insets(18));
        b.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 12; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 12;");
        return b;
    }

    private static VBox createLanguageCard() {
        Text t = new Text("Language & Regional Options");
        t.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 16px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        ComboBox<String> lang = new ComboBox<>();
        lang.getItems().addAll("English", "मराठी (Marathi)", "हिन्दी (Hindi)");
        lang.setValue("English");
        lang.setPrefWidth(220);

        VBox b = new VBox(10, t, lang);
        b.setPadding(new Insets(18));
        b.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 12; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 12;");
        return b;
    }

    private static Label createLabel(String text) {
        Label l = new Label(text);
        l.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #374151;");
        return l;
    }
}
