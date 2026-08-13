package com.desgin.view.Provider;

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
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class ProfileManagement {
    
    public HBox getProfile(StackPane root) {

        // ==========================
        // Notification Button
        // ==========================

        Button notificationBtn = new Button("🔔 Notifications");
        notificationBtn.setPadding(new Insets(0, 18, 0, 18));

        notificationBtn.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-text-fill: #355E3B;" +
                "-fx-font-size: 14px;" +
                "-fx-font-family: 'Poppins';" +
                "-fx-cursor: hand;" +
                "-fx-background-radius:10;"
        );

        hoverElement(notificationBtn);

        // ==========================
        // Provider Profile Card
        // ==========================

        Text profileIcon = new Text("🏢");
        profileIcon.setStyle("-fx-font-size:30px;");

        Text providerName = new Text("Green Farm Equipments");
        providerName.setStyle(
                "-fx-font-size:15px;" +
                "-fx-font-weight:bold;" +
                "-fx-fill:#2E4A32;"
        );

        Text providerRole = new Text("Equipment Provider");
        providerRole.setStyle(
                "-fx-font-size:11px;" +
                "-fx-fill:#6D7B6D;"
        );

        VBox textBox = new VBox(3,
                providerName,
                providerRole);

        textBox.setAlignment(Pos.CENTER_LEFT);

        HBox profileBox = new HBox(12,
                profileIcon,
                textBox);

        profileBox.setAlignment(Pos.CENTER_LEFT);
        profileBox.setPadding(new Insets(12));

        profileBox.setStyle(
                "-fx-background-color:#F4F8F2;" +
                "-fx-background-radius:12;" +
                "-fx-border-color:#DCE5DC;" +
                "-fx-border-radius:12;" +
                "-fx-cursor:hand;"
        );

        hoverElement(profileBox);

        // ==========================
        // Popup Window
        // ==========================

        Button closeButton = new Button("✖");

        closeButton.setStyle(
                "-fx-background-color:transparent;" +
                "-fx-font-size:15px;" +
                "-fx-cursor:hand;"
        );

        HBox closeBox = new HBox(closeButton);
        closeBox.setAlignment(Pos.TOP_RIGHT);
        closeBox.setPadding(new Insets(10,10,0,0));

        VBox popup = new VBox(
                closeBox,
                providerProfile()
        );

        popup.setPrefSize(650, 760);
        popup.setMaxSize(650, 760);

        popup.setStyle(
                "-fx-background-color:white;" +
                "-fx-background-radius:18;" +
                "-fx-border-radius:18;" +
                "-fx-border-color:#DDE6DD;"
        );

        StackPane.setAlignment(
                popup,
                Pos.TOP_RIGHT
        );

        StackPane.setMargin(
                popup,
                new Insets(70,25,20,0)
        );

        popup.setVisible(false);

        root.getChildren().add(popup);

        // ==========================
        // Events
        // ==========================

        profileBox.setOnMouseClicked(e -> popup.setVisible(true));

        closeButton.setOnAction(e -> popup.setVisible(false));

        // ==========================
        // Top Right Layout
        // ==========================

        HBox rightBox = new HBox(
                15,
                notificationBtn,
                profileBox
        );

        rightBox.setAlignment(Pos.CENTER_RIGHT);

        return rightBox;
    }
        // ==========================================================
    // Provider Profile Window
    // ==========================================================

    public ScrollPane providerProfile() {

        VBox content = new VBox(25);
        content.setPadding(new Insets(25));
        content.setStyle("-fx-background-color:#F7F9F4;");

        // ======================================================
        // HEADER
        // ======================================================

        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER_LEFT);

        Text logo = new Text("🏢");
        logo.setStyle(
                "-fx-font-size:45px;"
        );

        VBox titleBox = new VBox(5);

        Text title = new Text("Green Farm Equipments");
        title.setStyle(
                "-fx-font-size:28px;" +
                "-fx-font-weight:bold;" +
                "-fx-fill:#2E7D32;"
        );

        Text verified = new Text("✔ Verified Equipment Provider");
        verified.setStyle(
                "-fx-font-size:13px;" +
                "-fx-fill:#43A047;" +
                "-fx-font-weight:bold;"
        );

        Text subtitle = new Text(
                "Manage your business information, equipment inventory and account settings."
        );

        subtitle.setWrappingWidth(420);

        subtitle.setStyle(
                "-fx-font-size:13px;" +
                "-fx-fill:#607D8B;"
        );

        titleBox.getChildren().addAll(
                title,
                verified,
                subtitle
        );

        header.getChildren().addAll(
                logo,
                titleBox
        );

        // ======================================================
        // DASHBOARD TITLE
        // ======================================================

        Text dashboardTitle = new Text("Business Overview");

        dashboardTitle.setStyle(
                "-fx-font-size:20px;" +
                "-fx-font-weight:bold;" +
                "-fx-fill:#37474F;"
        );

        // ======================================================
        // FIRST ROW OF CARDS
        // ======================================================

        HBox row1 = new HBox(20);
        row1.setAlignment(Pos.CENTER);

        VBox equipmentCard = createStatCard(
                "🚜",
                "Total Equipment",
                "42"
        );

        VBox rentalCard = createStatCard(
                "📦",
                "Active Rentals",
                "18"
        );

        row1.getChildren().addAll(
                equipmentCard,
                rentalCard
        );

        // ======================================================
        // SECOND ROW OF CARDS
        // ======================================================

        HBox row2 = new HBox(20);
        row2.setAlignment(Pos.CENTER);

        VBox ratingCard = createStatCard(
                "⭐",
                "Customer Rating",
                "4.8"
        );

        VBox customerCard = createStatCard(
                "👥",
                "Customers",
                "132"
        );

        row2.getChildren().addAll(
                ratingCard,
                customerCard
        );

        // ======================================================
        // ADD TO MAIN CONTENT
        // ======================================================

        content.getChildren().addAll(
                header,
                dashboardTitle,
                row1,
                row2
        );
                // ======================================================
        // BUSINESS INFORMATION
        // ======================================================

        Text businessTitle = new Text("Business Information");

        businessTitle.setStyle(
                "-fx-font-size:20px;" +
                "-fx-font-weight:bold;" +
                "-fx-fill:#37474F;"
        );

        TextField businessNameField = new TextField("Green Farm Equipments");
        businessNameField.setPromptText("Business Name");

        TextField ownerNameField = new TextField("Harshal Patil");
        ownerNameField.setPromptText("Owner Name");

        TextField emailField = new TextField("greenfarm@gmail.com");
        emailField.setPromptText("Business Email");

        TextField phoneField = new TextField();
        phoneField.setPromptText("Mobile Number");

        TextField gstField = new TextField();
        gstField.setPromptText("GST Number");

        TextField registrationField = new TextField();
        registrationField.setPromptText("Business Registration ID");

        styleField(businessNameField);
        styleField(ownerNameField);
        styleField(emailField);
        styleField(phoneField);
        styleField(gstField);
        styleField(registrationField);

        GridPane businessGrid = new GridPane();

        businessGrid.setHgap(20);
        businessGrid.setVgap(18);

        businessGrid.add(createFieldBox("Business Name", businessNameField),0,0);
        businessGrid.add(createFieldBox("Owner Name", ownerNameField),1,0);

        businessGrid.add(createFieldBox("Business Email", emailField),0,1);
        businessGrid.add(createFieldBox("Mobile Number", phoneField),1,1);

        businessGrid.add(createFieldBox("GST Number", gstField),0,2);
        businessGrid.add(createFieldBox("Registration ID", registrationField),1,2);

        // ======================================================
        // BUSINESS ADDRESS
        // ======================================================

        Text addressTitle = new Text("Business Address");

        addressTitle.setStyle(
                "-fx-font-size:20px;" +
                "-fx-font-weight:bold;" +
                "-fx-fill:#37474F;"
        );

        TextField addressField = new TextField();
        addressField.setPromptText("Shop Address");

        TextField cityField = new TextField();
        cityField.setPromptText("City");

        TextField districtField = new TextField();
        districtField.setPromptText("District");

        TextField stateField = new TextField("Maharashtra");
        stateField.setPromptText("State");

        TextField pincodeField = new TextField();
        pincodeField.setPromptText("Pincode");

        styleField(addressField);
        styleField(cityField);
        styleField(districtField);
        styleField(stateField);
        styleField(pincodeField);

        addressField.setPrefWidth(580);

        GridPane addressGrid = new GridPane();

        addressGrid.setHgap(20);
        addressGrid.setVgap(18);

        addressGrid.add(createFieldBox("Shop Address", addressField),0,0,2,1);

        addressGrid.add(createFieldBox("City", cityField),0,1);
        addressGrid.add(createFieldBox("District", districtField),1,1);

        addressGrid.add(createFieldBox("State", stateField),0,2);
        addressGrid.add(createFieldBox("Pincode", pincodeField),1,2);

        // ======================================================
        // ADD TO MAIN CONTENT
        // ======================================================

        content.getChildren().addAll(
                businessTitle,
                businessGrid,

                addressTitle,
                addressGrid
        );
                // ======================================================
        // EQUIPMENT INFORMATION
        // ======================================================

        Text equipmentTitle = new Text("Equipment Information");

        equipmentTitle.setStyle(
                "-fx-font-size:20px;" +
                "-fx-font-weight:bold;" +
                "-fx-fill:#37474F;"
        );

        TextField totalEquipmentField = new TextField();
        totalEquipmentField.setPromptText("Total Equipment");

        TextField availableEquipmentField = new TextField();
        availableEquipmentField.setPromptText("Available Equipment");

        TextField categoryField = new TextField();
        categoryField.setPromptText("Tractor, Harvester, Rotavator...");

        TextField yearsBusinessField = new TextField();
        yearsBusinessField.setPromptText("Years in Business");

        TextField rentalChargeField = new TextField();
        rentalChargeField.setPromptText("Starting Rental Price");

        TextField deliveryField = new TextField();
        deliveryField.setPromptText("Yes / No");

        styleField(totalEquipmentField);
        styleField(availableEquipmentField);
        styleField(categoryField);
        styleField(yearsBusinessField);
        styleField(rentalChargeField);
        styleField(deliveryField);

        GridPane equipmentGrid = new GridPane();

        equipmentGrid.setHgap(20);
        equipmentGrid.setVgap(18);

        equipmentGrid.add(
                createFieldBox("Total Equipment",
                        totalEquipmentField), 0, 0);

        equipmentGrid.add(
                createFieldBox("Available Equipment",
                        availableEquipmentField), 1, 0);

        equipmentGrid.add(
                createFieldBox("Equipment Categories",
                        categoryField), 0, 1);

        equipmentGrid.add(
                createFieldBox("Years in Business",
                        yearsBusinessField), 1, 1);

        equipmentGrid.add(
                createFieldBox("Starting Rental Charge",
                        rentalChargeField), 0, 2);

        equipmentGrid.add(
                createFieldBox("Delivery Service",
                        deliveryField), 1, 2);

        // ======================================================
        // BUSINESS DETAILS
        // ======================================================

        Text businessDetailTitle = new Text("Business Details");

        businessDetailTitle.setStyle(
                "-fx-font-size:20px;" +
                "-fx-font-weight:bold;" +
                "-fx-fill:#37474F;"
        );

        TextField openingTimeField = new TextField();
        openingTimeField.setPromptText("Opening Time");

        TextField closingTimeField = new TextField();
        closingTimeField.setPromptText("Closing Time");

        TextField serviceAreaField = new TextField();
        serviceAreaField.setPromptText(
                "Pune, Satara, Kolhapur..."
        );

        styleField(openingTimeField);
        styleField(closingTimeField);
        styleField(serviceAreaField);

        javafx.scene.control.TextArea descriptionArea =
                new javafx.scene.control.TextArea();

        descriptionArea.setPromptText(
                "Tell customers about your business..."
        );

        descriptionArea.setPrefHeight(140);

        descriptionArea.setWrapText(true);

        descriptionArea.setStyle(
                "-fx-background-color:white;" +
                "-fx-border-color:#D5DDD5;" +
                "-fx-border-radius:8;" +
                "-fx-background-radius:8;" +
                "-fx-font-size:14;" +
                "-fx-padding:10;"
        );

        GridPane detailGrid = new GridPane();

        detailGrid.setHgap(20);
        detailGrid.setVgap(18);

        detailGrid.add(
                createFieldBox("Opening Time",
                        openingTimeField), 0, 0);

        detailGrid.add(
                createFieldBox("Closing Time",
                        closingTimeField), 1, 0);

        detailGrid.add(
                createFieldBox("Service Areas",
                        serviceAreaField), 0, 1, 2, 1);

        VBox descriptionBox = new VBox(8);

        Label descriptionLabel = new Label("Business Description");

        descriptionLabel.setStyle(
                "-fx-font-size:13px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#546E7A;"
        );

        descriptionBox.getChildren().addAll(
                descriptionLabel,
                descriptionArea
        );

        detailGrid.add(
                descriptionBox, 0, 2, 2, 1);

        // ======================================================
        // ADD TO MAIN CONTENT
        // ======================================================

        content.getChildren().addAll(

                equipmentTitle,
                equipmentGrid,

                businessDetailTitle,
                detailGrid

        );
                // ======================================================
        // ACCOUNT SETTINGS
        // ======================================================

        Text accountTitle = new Text("Account Settings");

        accountTitle.setStyle(
                "-fx-font-size:20px;" +
                "-fx-font-weight:bold;" +
                "-fx-fill:#37474F;"
        );

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter New Password");

        styleField(passwordField);

        // ======================================================
        // BUTTONS
        // ======================================================

        Button saveButton = new Button("💾 Save Changes");
        saveButton.setPrefSize(170, 42);

        saveButton.setStyle(
                "-fx-background-color:#2E7D32;" +
                "-fx-text-fill:white;" +
                "-fx-font-size:14px;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:8;" +
                "-fx-cursor:hand;"
        );

        Button manageEquipmentButton =
                new Button("🚜 Manage Equipment");

        manageEquipmentButton.setPrefSize(190,42);

        manageEquipmentButton.setStyle(
                "-fx-background-color:#1565C0;" +
                "-fx-text-fill:white;" +
                "-fx-font-size:14px;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:8;" +
                "-fx-cursor:hand;"
        );

        Button publicProfileButton =
                new Button("👁 View Public Profile");

        publicProfileButton.setPrefSize(190,42);

        publicProfileButton.setStyle(
                "-fx-background-color:#FB8C00;" +
                "-fx-text-fill:white;" +
                "-fx-font-size:14px;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:8;" +
                "-fx-cursor:hand;"
        );

        Button logoutButton =
                new Button("🚪 Logout");

        logoutButton.setPrefSize(120,42);

        logoutButton.setStyle(
                "-fx-background-color:#FFFFFF;" +
                "-fx-text-fill:#D32F2F;" +
                "-fx-border-color:#D32F2F;" +
                "-fx-border-radius:8;" +
                "-fx-background-radius:8;" +
                "-fx-font-size:14px;" +
                "-fx-font-weight:bold;" +
                "-fx-cursor:hand;"
        );

        hoverElement(saveButton);
        hoverElement(manageEquipmentButton);
        hoverElement(publicProfileButton);
        hoverElement(logoutButton);

        // ======================================================
        // BUTTON LAYOUT
        // ======================================================

        HBox buttonRow1 = new HBox(
                15,
                saveButton,
                manageEquipmentButton
        );

        buttonRow1.setAlignment(Pos.CENTER_LEFT);

        HBox buttonRow2 = new HBox(
                15,
                publicProfileButton,
                logoutButton
        );

        buttonRow2.setAlignment(Pos.CENTER_LEFT);

        VBox buttonSection = new VBox(
                15,
                buttonRow1,
                buttonRow2
        );

        // ======================================================
        // ADD TO CONTENT
        // ======================================================

        content.getChildren().addAll(

                accountTitle,

                createFieldBox(
                        "Change Password",
                        passwordField
                ),

                buttonSection
        );

        // ======================================================
        // SCROLL PANE
        // ======================================================

        ScrollPane scrollPane = new ScrollPane();

        scrollPane.setContent(content);

        scrollPane.setFitToWidth(true);

        scrollPane.setPannable(true);

        scrollPane.setStyle(
                "-fx-background:#F7F9F4;" +
                "-fx-background-color:transparent;"
        );

        scrollPane.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER
        );

        scrollPane.setVbarPolicy(
                ScrollPane.ScrollBarPolicy.AS_NEEDED
        );

        return scrollPane;
    }
        // ==========================================================
    // CREATE DASHBOARD STATISTIC CARD
    // ==========================================================

    private VBox createStatCard(String icon, String title, String value) {

        Text iconText = new Text(icon);
        iconText.setStyle("-fx-font-size:28px;");

        Text valueText = new Text(value);
        valueText.setStyle(
                "-fx-font-size:24px;" +
                "-fx-font-weight:bold;" +
                "-fx-fill:#2E7D32;"
        );

        Text titleText = new Text(title);
        titleText.setStyle(
                "-fx-font-size:13px;" +
                "-fx-fill:#607D8B;"
        );

        VBox card = new VBox(10);

        card.setAlignment(Pos.CENTER);

        card.setPrefSize(250,120);

        card.setStyle(
                "-fx-background-color:white;" +
                "-fx-background-radius:15;" +
                "-fx-border-color:#DCE5DC;" +
                "-fx-border-radius:15;" +
                "-fx-padding:15;" +
                "-fx-cursor:hand;"
        );

        hoverElement(card);

        card.getChildren().addAll(
                iconText,
                valueText,
                titleText
        );

        return card;
    }

    // ==========================================================
    // STYLE TEXT FIELD
    // ==========================================================

    private void styleField(TextField field) {

        field.setPrefHeight(42);
        field.setPrefWidth(270);

        field.setStyle(
                "-fx-background-color:white;" +
                "-fx-background-radius:8;" +
                "-fx-border-radius:8;" +
                "-fx-border-color:#D5DDD5;" +
                "-fx-padding:0 12;" +
                "-fx-font-size:14px;"
        );
    }

    // ==========================================================
    // CREATE LABEL + FIELD
    // ==========================================================

    private VBox createFieldBox(String labelText, TextField field) {

        Label label = new Label(labelText);

        label.setStyle(
                "-fx-font-size:13px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#546E7A;"
        );

        VBox box = new VBox(6);

        box.getChildren().addAll(
                label,
                field
        );

        return box;
    }

    // ==========================================================
    // HOVER EFFECT
    // ==========================================================

    private void hoverElement(Node node) {

        node.setOnMouseEntered(e -> {

            node.setTranslateY(-2);

            node.setOpacity(0.92);

            node.setStyle(node.getStyle() +
                    "-fx-effect:dropshadow(gaussian,rgba(0,0,0,0.18),12,0.2,0,4);");

        });

        node.setOnMouseExited(e -> {

            node.setTranslateY(0);

            node.setOpacity(1.0);

            String style = node.getStyle();

            style = style.replaceAll("-fx-effect:[^;]*;", "");

            node.setStyle(style);
        });
    }

}

