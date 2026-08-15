package com.desgin.view.provider;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class ProviderProfileManagement {

    public HBox getProfile(StackPane root) {
        Button notificationBtn = new Button("🔔  Notifications (3)");
        notificationBtn.setPadding(new Insets(0, 15, 0, 15));
        notificationBtn.setStyle("-fx-background-color: transparent;" + "-fx-background-radius: 10;"
                + "-fx-border-width: 0;"
                + "-fx-border-color: transparent;" + "-fx-text-fill: #5C4033;"
                + "-fx-font-family: 'Poppins';"
                + "-fx-font-size: 14px;" + "-fx-font-weight: 500;"
                + "-fx-cursor: hand;");
        hoverElement(notificationBtn);

        Text profileIcon = new Text("🚜");
        profileIcon.setStyle("-fx-font-size: 24px;");

        Text profileName = new Text("Rajesh Agro Services");
        profileName.setStyle("-fx-font-family: 'Poppins';"
                + "-fx-font-size: 14px;"
                + "-fx-font-weight: bold;"
                + "-fx-fill: #4A2C20;");

        Text profileRole = new Text("Verified Equipment Provider ✓");
        profileRole.setStyle("-fx-font-family: 'Poppins';"
                + "-fx-font-size: 11px;"
                + "-fx-font-weight: bold;"
                + "-fx-fill: #2E7D32;");

        VBox profileText = new VBox(2, profileName, profileRole);
        profileText.setAlignment(Pos.CENTER_LEFT);

        HBox profileBox = new HBox(10, profileIcon, profileText);
        hoverElement(profileBox);
        profileBox.setAlignment(Pos.CENTER_LEFT);
        profileBox.setPadding(new Insets(6, 12, 6, 12));
        profileBox.setStyle("-fx-background-color: #EDE3D5;"
                + "-fx-background-radius: 10;"
                + "-fx-cursor: hand;");

        Button closeButton = new Button("✕");
        closeButton.setStyle(
                "-fx-background-color: transparent;"
                + "-fx-text-fill: #8B3A3A;"
                + "-fx-font-size: 16px;"
                + "-fx-font-weight: bold;"
                + "-fx-cursor: hand;");

        VBox profilePopup = new VBox(10, closeButton, createProfilePopupContent());
        profilePopup.setAlignment(Pos.TOP_RIGHT);
        profilePopup.setPadding(new Insets(15));
        profilePopup.setPrefSize(480, 680);
        profilePopup.setMaxSize(480, 680);

        profilePopup.setStyle(
                "-fx-background-color: #FFFDF9;"
                + "-fx-background-radius: 15;"
                + "-fx-border-color: #D8C7B5;"
                + "-fx-border-radius: 15;"
                + "-fx-border-width: 1;"
                + "-fx-effect: dropshadow(gaussian, rgba(74,44,32,0.25), 18, 0.2, 0, 8);");

        StackPane.setAlignment(profilePopup, Pos.TOP_RIGHT);
        StackPane.setMargin(profilePopup, new Insets(75, 25, 0, 0));
        profilePopup.setVisible(false);

        root.getChildren().add(profilePopup);

        closeButton.setOnAction(e -> profilePopup.setVisible(false));

        profileBox.setOnMouseClicked(event -> {
            profilePopup.setVisible(!profilePopup.isVisible());
        });

        // Top Notifications Popup
        VBox notifPopup = createNotificationsPopup(root);
        notificationBtn.setOnAction(e -> {
            notifPopup.setVisible(!notifPopup.isVisible());
            if (profilePopup.isVisible()) profilePopup.setVisible(false);
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox topBar = new HBox(15, spacer, notificationBtn, profileBox);
        topBar.setAlignment(Pos.CENTER_RIGHT);
        topBar.setPadding(new Insets(12, 30, 12, 30));
        topBar.setStyle("-fx-background-color: #F5EFE6;" + "-fx-border-color: #D8C7B5;"
                + "-fx-border-width: 0 0 1 0;");

        return topBar;
    }

    private VBox createNotificationsPopup(StackPane root) {
        Button closeNotif = new Button("✕");
        closeNotif.setStyle("-fx-background-color: transparent; -fx-text-fill: #8B3A3A; -fx-font-weight: bold; -fx-cursor: hand;");

        Text title = new Text("Provider Notifications");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 16px; -fx-font-weight: bold; -fx-fill: #4A2C20;");

        HBox notifHeader = new HBox(title, new Region(), closeNotif);
        HBox.setHgrow(notifHeader.getChildren().get(1), Priority.ALWAYS);
        notifHeader.setAlignment(Pos.CENTER_LEFT);

        VBox itemsList = new VBox(10);
        itemsList.getChildren().addAll(
            createNotifItem("📥 New Booking Request", "Farmer Suresh Patil requested 55HP Tractor for 3 days.", "10 mins ago", "#E8F5E9"),
            createNotifItem("💰 Payment Credited", "₹12,000 transferred to your bank account for Harvester rental.", "2 hours ago", "#E3F2FD"),
            createNotifItem("🛠 Maintenance Reminder", "Rotavator (MH-12-AB-4040) is due for servicing in 2 days.", "1 day ago", "#FFF3E0")
        );

        VBox notifBox = new VBox(12, notifHeader, itemsList);
        notifBox.setPrefWidth(380);
        notifBox.setMaxWidth(380);
        notifBox.setPadding(new Insets(16));
        notifBox.setStyle("-fx-background-color: #FFFDF9; -fx-background-radius: 14; -fx-border-color: #D8C7B5; -fx-border-radius: 14; -fx-border-width: 1; -fx-effect: dropshadow(gaussian, rgba(74,44,32,0.25), 18, 0.2, 0, 8);");
        
        StackPane.setAlignment(notifBox, Pos.TOP_RIGHT);
        StackPane.setMargin(notifBox, new Insets(75, 180, 0, 0));
        notifBox.setVisible(false);

        closeNotif.setOnAction(e -> notifBox.setVisible(false));
        root.getChildren().add(notifBox);

        return notifBox;
    }

    private VBox createNotifItem(String title, String desc, String time, String bg) {
        Text t = new Text(title);
        t.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-font-weight: bold; -fx-fill: #4A2C20;");

        Text d = new Text(desc);
        d.setWrappingWidth(330);
        d.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #5C4033;");

        Text tm = new Text(time);
        tm.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 10px; -fx-fill: #806A5B;");

        VBox box = new VBox(4, t, d, tm);
        box.setPadding(new Insets(10));
        box.setStyle("-fx-background-color: " + bg + "; -fx-background-radius: 8; -fx-border-color: #D8C7B5; -fx-border-radius: 8; -fx-border-width: 0.5;");
        return box;
    }

    private ScrollPane createProfilePopupContent() {
        VBox content = new VBox(15);
        content.setAlignment(Pos.TOP_CENTER);
        content.setPadding(new Insets(10, 15, 20, 15));

        Text icon = new Text("🚜");
        icon.setStyle("-fx-font-size: 42px;");

        Text name = new Text("Rajesh Agro Services & Equipment");
        name.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 18px; -fx-font-weight: bold; -fx-fill: #4A2C20;");

        Text badge = new Text("★ 4.9 Rating  •  18 Fleet Machines  •  Verified Provider");
        badge.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #2E7D32; -fx-font-weight: bold;");

        // KPI mini cards
        HBox stats = new HBox(12,
            createMiniStat("18", "Fleet Units"),
            createMiniStat("142", "Rentals Done"),
            createMiniStat("₹4.85L", "Total Earned")
        );
        stats.setAlignment(Pos.CENTER);

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setPadding(new Insets(10, 0, 10, 0));

        form.add(createFieldLabel("Provider Owner:"), 0, 0);
        form.add(createProfileTextField("Rajesh Patil"), 1, 0);

        form.add(createFieldLabel("Mobile Number:"), 0, 1);
        form.add(createProfileTextField("+91 98220 12345"), 1, 1);

        form.add(createFieldLabel("Email Address:"), 0, 2);
        form.add(createProfileTextField("rajesh.agro@farmmail.com"), 1, 2);

        form.add(createFieldLabel("Service Hub:"), 0, 3);
        form.add(createProfileTextField("Pune Rural & Baramati Cluster"), 1, 3);

        form.add(createFieldLabel("GST / Trade Reg:"), 0, 4);
        form.add(createProfileTextField("27AAACR1234F1Z5"), 1, 4);

        form.add(createFieldLabel("Bank Payout Acc:"), 0, 5);
        form.add(createProfileTextField("HDFC Bank •••• 8842"), 1, 5);

        Button saveBtn = new Button("Update Business Profile");
        saveBtn.setPrefWidth(260);
        saveBtn.setPrefHeight(38);
        saveBtn.setStyle("-fx-background-color: #6B8E23; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand;");

        content.getChildren().addAll(icon, name, badge, stats, form, saveBtn);

        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        return scroll;
    }

    private VBox createMiniStat(String val, String lbl) {
        Text v = new Text(val);
        v.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 16px; -fx-font-weight: bold; -fx-fill: #4A2C20;");
        Text l = new Text(lbl);
        l.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-fill: #806A5B;");

        VBox b = new VBox(2, v, l);
        b.setAlignment(Pos.CENTER);
        b.setPrefWidth(110);
        b.setPadding(new Insets(8));
        b.setStyle("-fx-background-color: #F5EFE6; -fx-background-radius: 8; -fx-border-color: #D8C7B5; -fx-border-radius: 8; -fx-border-width: 1;");
        return b;
    }

    private Label createFieldLabel(String text) {
        Label l = new Label(text);
        l.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #5C4033;");
        return l;
    }

    private TextField createProfileTextField(String text) {
        TextField tf = new TextField(text);
        tf.setPrefHeight(34);
        tf.setPrefWidth(220);
        tf.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #D8C7B5; -fx-border-radius: 6; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-text-fill: #4A2C20;");
        return tf;
    }

    private void hoverElement(Node node) {
        node.setOnMouseEntered(e -> node.setStyle(
                "-fx-background-color: #E4D3C2;"
                + "-fx-background-radius: 10;"
                + "-fx-cursor: hand;"));

        node.setOnMouseExited(e -> node.setStyle(
                "-fx-background-color: transparent;"
                + "-fx-background-radius: 10;"
                + "-fx-cursor: hand;"));
    }
}
