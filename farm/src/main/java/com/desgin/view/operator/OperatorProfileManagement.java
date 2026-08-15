package com.desgin.view.operator;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class OperatorProfileManagement {

    public HBox getProfile(StackPane root) {

        Button notificationBtn1 = new Button("🔔  Notifications");
        notificationBtn1.setPadding(new Insets(0, 15, 0, 15));
        notificationBtn1.setStyle("-fx-background-color: transparent;" + "-fx-background-radius: 10;"
                + "-fx-border-width: 0;"
                + "-fx-border-color: transparent;" + "-fx-text-fill: #5C4033;"
                + "-fx-font-family: 'Poppins';"
                + "-fx-font-size: 14px;" + "-fx-font-weight: normal;"
                + "-fx-cursor: hand;");
        hoverElement(notificationBtn1);

        Text profileIcon = new Text("👨‍🌾");
        profileIcon.setStyle("-fx-font-size: 26px;");

        Text profileName = new Text("Ramesh Chavan");
        profileName.setStyle("-fx-font-family: 'Poppins';" 
                + "-fx-font-size: 14px;" 
                + "-fx-font-weight: bold;"
                + "-fx-fill: #4A2C20;");

        Text profileRole = new Text("Heavy Machinery Operator");
        profileRole.setStyle("-fx-font-family: 'Poppins';" 
                + "-fx-font-size: 11px;"
                + "-fx-fill: #806A5B;");

        VBox profileText = new VBox(2, profileName, profileRole);
        profileText.setAlignment(Pos.CENTER_LEFT);

        HBox profileBox = new HBox(10, profileIcon, profileText);

        Button closeButton = new Button("✕");

        VBox profilePopup = new VBox(closeButton, profilePopup());
        profilePopup.setAlignment(Pos.TOP_RIGHT);
        profilePopup.setPadding(new Insets(10));
        profilePopup.setPrefSize(520, 720);
        profilePopup.setMaxSize(520, 720);

        profilePopup.setStyle(
                "-fx-background-color: #FFFDF9;" 
                + "-fx-background-radius: 15;" 
                + "-fx-border-color: #D8C7B5;" 
                + "-fx-border-radius: 15;" 
                + "-fx-border-width: 1; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 18, 0.2, 0, 6);");

        StackPane.setAlignment(profilePopup, Pos.TOP_RIGHT);
        StackPane.setMargin(profilePopup, new Insets(80, 25, 0, 0));
        profilePopup.setVisible(false);

        root.getChildren().addAll(profilePopup);

        hoverElement(profileBox);
        profileBox.setAlignment(Pos.CENTER_LEFT);
        profileBox.setPadding(new Insets(8, 14, 8, 14));
        profileBox.setStyle("-fx-background-color: #EDE3D5;"
                + "-fx-background-radius: 10;" 
                + "-fx-cursor: hand;");

        profileBox.setOnMouseClicked(event -> {
            closeButton.setStyle(
                    "-fx-background-color: transparent;" 
                    + "-fx-text-fill: #8B3A3A;" 
                    + "-fx-font-size: 18px;" 
                    + "-fx-font-weight: bold;" 
                    + "-fx-cursor: hand;");

            closeButton.setOnMouseEntered(e -> closeButton.setStyle(
                    "-fx-background-color: #A94442;" 
                    + "-fx-text-fill: white;" 
                    + "-fx-font-size: 18px;" 
                    + "-fx-font-weight: bold;"
                    + "-fx-cursor: hand;"));

            closeButton.setOnMouseExited(e -> closeButton.setStyle(
                    "-fx-background-color: transparent;" 
                    + "-fx-text-fill: #8B3A3A;" 
                    + "-fx-font-size: 18px;" 
                    + "-fx-font-weight: bold;" 
                    + "-fx-cursor: hand;"));

            profilePopup.setVisible(true);
        });

        closeButton.setOnMouseClicked(event -> {
            profilePopup.setVisible(false);
        });

        Button closeButton2 = new Button("✕");

        VBox notificationPopUp = new VBox(closeButton2, getNotification());
        notificationPopUp.setAlignment(Pos.TOP_RIGHT);
        notificationPopUp.setPadding(new Insets(10));
        notificationPopUp.setPrefSize(500, 480);
        notificationPopUp.setMaxSize(500, 480);

        notificationPopUp.setStyle(
                "-fx-background-color: #FFFDF9;" 
                + "-fx-background-radius: 15;" 
                + "-fx-border-color: #D8C7B5;" 
                + "-fx-border-radius: 15;" 
                + "-fx-border-width: 1; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 18, 0.2, 0, 6);");

        StackPane.setAlignment(notificationPopUp, Pos.TOP_RIGHT);
        StackPane.setMargin(notificationPopUp, new Insets(80, 25, 0, 0));
        notificationPopUp.setVisible(false);

        root.getChildren().addAll(notificationPopUp);

        hoverElement(notificationBtn1);
        notificationBtn1.setAlignment(Pos.CENTER_LEFT);
        notificationBtn1.setPadding(new Insets(8, 14, 8, 14));
        notificationBtn1.setStyle("-fx-background-color: #EDE3D5;"
                + "-fx-background-radius: 10;"
                + "-fx-cursor: hand;");

        notificationBtn1.setOnMouseClicked(event -> {
            closeButton2.setStyle(
                    "-fx-background-color: transparent;" 
                    + "-fx-text-fill: #8B3A3A;" 
                    + "-fx-font-size: 18px;" 
                    + "-fx-font-weight: bold;" 
                    + "-fx-cursor: hand;");

            closeButton2.setOnMouseEntered(e -> closeButton2.setStyle(
                    "-fx-background-color: #A94442;" 
                    + "-fx-text-fill: white;" 
                    + "-fx-font-size: 18px;" 
                    + "-fx-font-weight: bold;"
                    + "-fx-cursor: hand;"));

            closeButton2.setOnMouseExited(e -> closeButton2.setStyle(
                    "-fx-background-color: transparent;" 
                    + "-fx-text-fill: #8B3A3A;" 
                    + "-fx-font-size: 18px;" 
                    + "-fx-font-weight: bold;" 
                    + "-fx-cursor: hand;"));
                    
            notificationPopUp.setVisible(true);
        });

        closeButton2.setOnMouseClicked(event -> {
            notificationPopUp.setVisible(false);
        });

        HBox rightHBox = new HBox(12, notificationBtn1, profileBox);
        rightHBox.setAlignment(Pos.CENTER_RIGHT);
        rightHBox.setPadding(new Insets(15, 30, 10, 30));

        return rightHBox;
    }

    static void hoverElement(Node node) {
        node.setOnMouseEntered(e -> {
            node.setTranslateY(-2);
            node.setOpacity(0.85);
        });

        node.setOnMouseExited(e -> {
            node.setTranslateY(0);
            node.setOpacity(1.0);
        });
    }

    public static ScrollPane profilePopup() {
        VBox profileContent = new VBox(18);
        profileContent.setPadding(new Insets(20));
        profileContent.setStyle("-fx-background-color: #FFFDF9;");

        HBox profileHeader = new HBox(15);
        profileHeader.setAlignment(Pos.CENTER_LEFT);

        Text profileIcon = new Text("👨‍🌾");
        profileIcon.setStyle("-fx-font-size: 35px;");

        Text profileTitle = new Text("Operator Profile & License");
        profileTitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 22px; -fx-font-weight: bold; -fx-fill: #4A2C20;");

        Text subtitle = new Text("Manage your operator credentials, machinery endorsements & bank details");
        subtitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #806A5B;");

        VBox titleBox = new VBox(2, profileTitle, subtitle);
        profileHeader.getChildren().addAll(profileIcon, titleBox);

        // Section 1: Personal Info
        Text personalTitle = new Text("Personal Information");
        personalTitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 15px; -fx-font-weight: bold; -fx-fill: #4A2C20;");

        TextField nameField = new TextField("Ramesh Chavan");
        TextField emailField = new TextField("ramesh.operator@gmail.com");
        TextField phoneField = new TextField("+91 98224 81920");

        styleField(nameField);
        styleField(emailField);
        styleField(phoneField);

        GridPane personalGrid = new GridPane();
        personalGrid.setHgap(15);
        personalGrid.setVgap(10);
        personalGrid.add(createFieldBox("Full Name", nameField), 0, 0);
        personalGrid.add(createFieldBox("Email", emailField), 1, 0);
        personalGrid.add(createFieldBox("Mobile Number", phoneField), 0, 1);

        // Section 2: Operator Certification & License
        Text licenseTitle = new Text("Operator Licenses & Certifications");
        licenseTitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 15px; -fx-font-weight: bold; -fx-fill: #4A2C20;");

        TextField dlField = new TextField("MH-14-2021-0089241");
        TextField expField = new TextField("7+ Years Commercial Farming");
        TextField certField = new TextField("Heavy Tractor (4WD), Combine Harvester, Laser Leveler");
        TextField validityField = new TextField("Valid Thru: 14 Aug 2031");

        styleField(dlField);
        styleField(expField);
        styleField(certField);
        styleField(validityField);

        GridPane licenseGrid = new GridPane();
        licenseGrid.setHgap(15);
        licenseGrid.setVgap(10);
        licenseGrid.add(createFieldBox("Commercial DL / Badge No.", dlField), 0, 0);
        licenseGrid.add(createFieldBox("Operating Experience", expField), 1, 0);
        licenseGrid.add(createFieldBox("Machinery Endorsements", certField), 0, 1);
        licenseGrid.add(createFieldBox("License Validity", validityField), 1, 1);

        // Section 3: Operating Location & Emergency Contact
        Text locTitle = new Text("Base Location & Emergency Contact");
        locTitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 15px; -fx-font-weight: bold; -fx-fill: #4A2C20;");

        TextField baseLocationField = new TextField("Pune / Baramati Sector, Maharashtra");
        TextField emergencyField = new TextField("Suresh Chavan (Brother) - +91 98501 23456");

        styleField(baseLocationField);
        styleField(emergencyField);

        GridPane locGrid = new GridPane();
        locGrid.setHgap(15);
        locGrid.setVgap(10);
        locGrid.add(createFieldBox("Primary Dispatch Region", baseLocationField), 0, 0);
        locGrid.add(createFieldBox("Emergency Contact", emergencyField), 1, 0);

        // Section 4: Wage Payout Bank Account
        Text bankTitle = new Text("Wage Settlement Account");
        bankTitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 15px; -fx-font-weight: bold; -fx-fill: #4A2C20;");

        TextField bankName = new TextField("State Bank of India (SBI) - Baramati Branch");
        TextField accNo = new TextField("A/C: ••••••••• 4120  |  IFSC: SBIN0004512");

        styleField(bankName);
        styleField(accNo);

        GridPane bankGrid = new GridPane();
        bankGrid.setHgap(15);
        bankGrid.setVgap(10);
        bankGrid.add(createFieldBox("Primary Bank", bankName), 0, 0);
        bankGrid.add(createFieldBox("Account Details", accNo), 1, 0);

        // Section 5: Password
        Text passTitle = new Text("Security Settings");
        passTitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 15px; -fx-font-weight: bold; -fx-fill: #4A2C20;");

        PasswordField newPass = new PasswordField();
        newPass.setPromptText("Enter new password");
        styleField(newPass);

        Button saveButton = new Button("Save Profile Changes");
        saveButton.setPrefHeight(38);
        saveButton.setStyle("-fx-background-color: #2E7D32; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 0 18 0 18;");

        profileContent.getChildren().addAll(
                profileHeader,
                personalTitle, personalGrid,
                licenseTitle, licenseGrid,
                locTitle, locGrid,
                bankTitle, bankGrid,
                passTitle, createFieldBox("Change Password", newPass),
                saveButton
        );

        ScrollPane scrollPane = new ScrollPane(profileContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: #FFFDF9;");

        return scrollPane;
    }

    private static void styleField(TextField field) {
        field.setPrefHeight(38);
        field.setPrefWidth(220);
        field.setStyle(
                "-fx-background-color: white;" 
                + "-fx-border-color: #D8C7B5;" 
                + "-fx-border-radius: 8px;" 
                + "-fx-background-radius: 8px;" 
                + "-fx-padding: 0 10px;" 
                + "-fx-font-family: 'Poppins';"
                + "-fx-font-size: 13px;");
    }

    private static VBox createFieldBox(String labelText, TextField field) {
        Label label = new Label(labelText);
        label.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-font-weight: bold; -fx-text-fill: #5C4033;");
        VBox box = new VBox(4, label, field);
        return box;
    }

    public static VBox getNotification() {
        HBox header = new HBox();

        Button notificationBtn1 = new Button("🔔  Operator Alerts");
        notificationBtn1.setStyle("-fx-background-color: transparent; -fx-text-fill: #4A2C20; -fx-font-family: 'Poppins'; -fx-font-size: 15px; -fx-font-weight: bold; -fx-cursor: hand;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button markAllRead = new Button("Mark all as read");
        markAllRead.setStyle("-fx-background-color: transparent; -fx-text-fill: #2E7D32; -fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-cursor: hand;");

        header.setAlignment(Pos.CENTER_LEFT);
        header.getChildren().addAll(notificationBtn1, spacer, markAllRead);

        VBox notificationList = new VBox(10);
        notificationList.setPadding(new Insets(5));

        notificationList.getChildren().addAll(
                createNotification("🚜 New Plowing Job Assigned", "Farmer Balasaheb Shirole requested 14-acre deep plowing with John Deere 5310 in Baramati.", "12 min ago", "#2E7D32"),
                createNotification("💰 Daily Wage Credited", "₹2,800 wage for yesterday's rotavator shift was deposited to your SBI account.", "1 hour ago", "#2E7D32"),
                createNotification("⚙ Pre-Trip Inspection Alert", "Pre-op check due for Mahindra 575 DI (Engine oil and hydraulic fluid).", "3 hours ago", "#E65100"),
                createNotification("🌦 Field Weather Advisory", "Rain showers expected tomorrow afternoon. Plan harvesting accordingly.", "Yesterday", "#1976D2")
        );

        ScrollPane scrollPane = new ScrollPane(notificationList);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(380);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");

        VBox notificationContentVBox = new VBox(12, header, scrollPane);
        notificationContentVBox.setPrefWidth(470);

        return notificationContentVBox;
    }

    private static HBox createNotification(String title, String message, String time, String dotColor) {
        Text dot = new Text("● ");
        dot.setStyle("-fx-fill: " + dotColor + "; -fx-font-size: 14px;");

        Text notificationTitle = new Text(title);
        notificationTitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13.5px; -fx-font-weight: bold; -fx-fill: #4A2C20;");

        HBox titleBox = new HBox(4, dot, notificationTitle);
        titleBox.setAlignment(Pos.CENTER_LEFT);

        Text notificationMessage = new Text(message);
        notificationMessage.setWrappingWidth(390);
        notificationMessage.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #5C4033;");

        Text notificationTime = new Text(time);
        notificationTime.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 10.5px; -fx-fill: #806A5B;");

        VBox textBox = new VBox(3, titleBox, notificationMessage, notificationTime);

        HBox notification = new HBox(textBox);
        notification.setPadding(new Insets(10, 12, 10, 12));
        notification.setPrefWidth(450);
        notification.setStyle("-fx-background-color: #F5EFE6; -fx-background-radius: 10px; -fx-border-color: #D8C7B5; -fx-border-width: 1; -fx-border-radius: 10px;");

        return notification;
    }
}
