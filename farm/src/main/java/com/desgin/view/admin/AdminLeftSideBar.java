package com.desgin.view.admin;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class AdminLeftSideBar {

    private static Button activeButton;

    public static List<Button> navigationButtons = new ArrayList<>();
    public static Button dashboardBtn;
    public static Button approvalsBtn;
    public static Button usersBtn;
    public static Button paymentsBtn;
    public static Button profileSettingsBtn;
    public static Button escrowBtn;
    public static Button disputesBtn;
    public static Button queriesBtn;
    public static Button settingsBtn;

    public static StackPane root;

    public AdminLeftSideBar(AdminDashboard obj) {
        root = AdminDashboard.root;
    }

    public static void navigateToProfileSettings() {
        if (profileSettingsBtn != null) {
            setActiveButton(profileSettingsBtn, navigationButtons);
            AdminProfileManagement.setHeaderTitle("Master Setting ⚙️", "Manage your administrator credentials, profile picture, and account security");
            AdminDashboard.borderPane.setCenter(AdminProfileSettings.getPage(root));
        }
    }

    public static void navigateToPaymentsDetail() {
        if (paymentsBtn != null) {
            setActiveButton(paymentsBtn, navigationButtons);
            AdminProfileManagement.setHeaderTitle("Payments Detail 💳", "Financial ledger, live escrow transactions, platform commission, and user payouts");
            AdminDashboard.borderPane.setCenter(PaymentsDetail.getPage(root));
        }
    }

    public VBox getSideBar(Runnable ref) {
        navigationButtons.clear();

        Image logoImage;
        try {
            logoImage = new Image("file:farm/src/main/resources/assets/Images/logo.png");
            if (logoImage.isError()) {
                logoImage = new Image(getClass().getResourceAsStream("/assets/Images/logo.png"));
            }
        } catch (Exception e) {
            logoImage = new Image("file:farm/src/main/resources/assets/Images/logo.jpeg");
        }

        ImageView logoImageView = new ImageView(logoImage);
        logoImageView.setFitWidth(30);
        logoImageView.setFitHeight(30);
        logoImageView.setPreserveRatio(true);
        logoImageView.setSmooth(true);

        StackPane logoContainer = new StackPane(logoImageView);
        logoContainer.setPrefSize(42, 42);
        logoContainer.setMinSize(42, 42);
        logoContainer.setMaxSize(42, 42);
        logoContainer.setStyle(
                "-fx-background-color: linear-gradient(to bottom right, rgba(45, 106, 79, 0.7), rgba(82, 183, 136, 0.4));" +
                "-fx-background-radius: 12px;" +
                "-fx-border-color: rgba(255, 255, 255, 0.2);" +
                "-fx-border-radius: 12px;" +
                "-fx-border-width: 1px;" +
                "-fx-effect: dropshadow(gaussian, rgba(45, 106, 79, 0.4), 8, 0, 0, 2);");

        Text textName = new Text("FarmEquip");
        textName.setStyle(
                "-fx-font-size: 19px;" +
                "-fx-font-weight: bold;" +
                "-fx-font-family: 'Poppins';" +
                "-fx-fill: #FFFFFF;");

        Text roleBadge = new Text("🛡️ Admin Console");
        roleBadge.setStyle(
                "-fx-font-size: 11px;" +
                "-fx-font-weight: 600;" +
                "-fx-font-family: 'Poppins';" +
                "-fx-fill: #74C69D;");

        VBox brandBox = new VBox(1, textName, roleBadge);
        brandBox.setAlignment(Pos.CENTER_LEFT);

        HBox logoTextHBox = new HBox(10, logoContainer, brandBox);
        logoTextHBox.setAlignment(Pos.CENTER_LEFT);
        logoTextHBox.setPadding(new Insets(4, 6, 16, 6));
        logoTextHBox.setStyle("-fx-border-color: rgba(255, 255, 255, 0.1); -fx-border-width: 0 0 1px 0;");

        dashboardBtn = new Button("⌂   Master Dashboard");
        styleMenuButton(dashboardBtn);

        approvalsBtn = new Button("🚜  Equipment Management");
        styleMenuButton(approvalsBtn);

        usersBtn = new Button("👥  User Management");
        styleMenuButton(usersBtn);

        paymentsBtn = new Button("💳   Payments Detail");
        styleMenuButton(paymentsBtn);

        profileSettingsBtn = new Button("⚙️   Master Setting");
        styleMenuButton(profileSettingsBtn);


        queriesBtn = new Button("💬   User Queries");
        styleMenuButton(queriesBtn);


        VBox vBoxBtn1 = new VBox(6, dashboardBtn, approvalsBtn, usersBtn, paymentsBtn, queriesBtn);


        Button logoutBtn = new Button("↪   Logout");
        logoutBtn.setOnAction(e -> {
            AdminProfileStore.reset();
            if (ref != null) {
                ref.run();
            }
        });
        styleLogoutButton(logoutBtn);

        VBox vBoxBtn2 = new VBox(6, profileSettingsBtn, logoutBtn);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        VBox btnBox = new VBox(10, vBoxBtn1, spacer, vBoxBtn2);
        VBox.setVgrow(btnBox, Priority.ALWAYS);

        VBox leftVB = new VBox(logoTextHBox, btnBox);
        VBox.setVgrow(leftVB, Priority.ALWAYS);
        leftVB.setPrefWidth(250);
        leftVB.setMinWidth(250);
        leftVB.setMaxWidth(250);
        leftVB.setMaxHeight(Double.MAX_VALUE);
        leftVB.setSpacing(16);
        leftVB.setPadding(new Insets(20, 14, 20, 14));
        leftVB.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #11281E 0%, #163628 50%, #0F231B 100%);" +
                "-fx-border-color: rgba(255, 255, 255, 0.08);" +
                "-fx-border-width: 0 1px 0 0;" +
                "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.18), 12, 3, 0, 0);");

        navigationButtons.add(dashboardBtn);
        navigationButtons.add(approvalsBtn);
        navigationButtons.add(usersBtn);

        navigationButtons.add(paymentsBtn);
        navigationButtons.add(profileSettingsBtn);
        navigationButtons.add(queriesBtn);

        setActiveButton(dashboardBtn, navigationButtons);

        dashboardBtn.setOnAction(event -> {
            setActiveButton(dashboardBtn, navigationButtons);
            AdminProfileManagement.updateHeaderGreeting();
            AdminDashboard.borderPane.setCenter(AdminHome.getPage(root));
        });

        approvalsBtn.setOnAction(event -> {
            setActiveButton(approvalsBtn, navigationButtons);
            AdminProfileManagement.setHeaderTitle("Equipment Management 🚜", "Review and approve provider equipment listings, RTO certificates, and insurance documents");
            AdminDashboard.borderPane.setCenter(MachineryApprovals.getPage(root));
        });

        usersBtn.setOnAction(event -> {
            setActiveButton(usersBtn, navigationButtons);
            AdminProfileManagement.setHeaderTitle("User Management 👥", "Manage platform users, review KYC records, and control account access");
            AdminDashboard.borderPane.setCenter(UserManagement.getPage(root));
        });

        paymentsBtn.setOnAction(event -> {
            navigateToPaymentsDetail();
        });

        profileSettingsBtn.setOnAction(event -> {
            navigateToProfileSettings();

        

        queriesBtn.setOnAction(event -> {
            setActiveButton(queriesBtn, navigationButtons);
            AdminProfileManagement.setHeaderTitle("User Queries & Live Support 💬", "Real-time WhatsApp-style helpdesk for operator and farmer queries");
            AdminDashboard.borderPane.setCenter(UserQueries.getPage(root));
        });


        return leftVB;
    }

    public static void setActiveButton(Button selected, List<Button> buttons) {
        activeButton = selected;
        for (Button button : buttons) {
            if (button == null) continue;
            if (button == selected) {
                button.setStyle(
                        "-fx-background-color: linear-gradient(to right, #2D6A4F, #40916C, #52B788);" +
                        "-fx-background-radius: 10px;" +
                        "-fx-text-fill: #FFFFFF;" +
                        "-fx-font-family: 'Poppins';" +
                        "-fx-font-size: 13.5px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-cursor: hand;" +
                        "-fx-effect: dropshadow(gaussian, rgba(82, 183, 136, 0.4), 10, 0, 0, 3);");
            } else {
                button.setStyle(
                        "-fx-background-color: transparent;" +
                        "-fx-background-radius: 10px;" +
                        "-fx-text-fill: #D1E7DD;" +
                        "-fx-font-family: 'Poppins';" +
                        "-fx-font-size: 13.5px;" +
                        "-fx-font-weight: 500;" +
                        "-fx-cursor: hand;");
            }
        }
    }

    private void styleMenuButton(Button button) {
        button.setPrefWidth(222);
        button.setMinWidth(222);
        button.setMaxWidth(222);
        button.setPrefHeight(44);
        button.setMinHeight(44);
        button.setMaxHeight(44);
        button.setAlignment(Pos.CENTER_LEFT);
        button.setPadding(new Insets(0, 16, 0, 16));

        button.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-background-radius: 10px;" +
                "-fx-text-fill: #D1E7DD;" +
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 13.5px;" +
                "-fx-font-weight: 500;" +
                "-fx-cursor: hand;");

        button.setOnMouseEntered(e -> {
            if (button != activeButton) {
                button.setStyle(
                        "-fx-background-color: rgba(255, 255, 255, 0.08);" +
                        "-fx-background-radius: 10px;" +
                        "-fx-text-fill: #FFFFFF;" +
                        "-fx-font-family: 'Poppins';" +
                        "-fx-font-size: 13.5px;" +
                        "-fx-font-weight: 600;" +
                        "-fx-cursor: hand;");
            }
        });

        button.setOnMouseExited(e -> {
            if (button != activeButton) {
                button.setStyle(
                        "-fx-background-color: transparent;" +
                        "-fx-background-radius: 10px;" +
                        "-fx-text-fill: #D1E7DD;" +
                        "-fx-font-family: 'Poppins';" +
                        "-fx-font-size: 13.5px;" +
                        "-fx-font-weight: 500;" +
                        "-fx-cursor: hand;");
            }
        });
    }

    private void styleLogoutButton(Button button) {
        button.setPrefWidth(222);
        button.setMinWidth(222);
        button.setMaxWidth(222);
        button.setPrefHeight(42);
        button.setMinHeight(42);
        button.setMaxHeight(42);
        button.setAlignment(Pos.CENTER_LEFT);
        button.setPadding(new Insets(0, 16, 0, 16));

        button.setStyle(
                "-fx-background-color: rgba(239, 68, 68, 0.12);" +
                "-fx-background-radius: 10px;" +
                "-fx-text-fill: #FCA5A5;" +
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 13px;" +
                "-fx-font-weight: 600;" +
                "-fx-cursor: hand;" +
                "-fx-border-color: rgba(239, 68, 68, 0.3);" +
                "-fx-border-radius: 10px;" +
                "-fx-border-width: 1px;");

        button.setOnMouseEntered(e -> {
            button.setStyle(
                    "-fx-background-color: rgba(239, 68, 68, 0.25);" +
                    "-fx-background-radius: 10px;" +
                    "-fx-text-fill: #FFFFFF;" +
                    "-fx-font-family: 'Poppins';" +
                    "-fx-font-size: 13px;" +
                    "-fx-font-weight: 600;" +
                    "-fx-cursor: hand;" +
                    "-fx-border-color: rgba(239, 68, 68, 0.6);" +
                    "-fx-border-radius: 10px;" +
                    "-fx-border-width: 1px;" +
                    "-fx-effect: dropshadow(gaussian, rgba(239, 68, 68, 0.25), 8, 0, 0, 2);");
        });

        button.setOnMouseExited(e -> {
            button.setStyle(
                    "-fx-background-color: rgba(239, 68, 68, 0.12);" +
                    "-fx-background-radius: 10px;" +
                    "-fx-text-fill: #FCA5A5;" +
                    "-fx-font-family: 'Poppins';" +
                    "-fx-font-size: 13px;" +
                    "-fx-font-weight: 600;" +
                    "-fx-cursor: hand;" +
                    "-fx-border-color: rgba(239, 68, 68, 0.3);" +
                    "-fx-border-radius: 10px;" +
                    "-fx-border-width: 1px;");
        });
    }
}
