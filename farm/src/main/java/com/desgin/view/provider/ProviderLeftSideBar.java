package com.desgin.view.provider;

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

public class ProviderLeftSideBar {

    private static Button activeButton;

    public static List<Button> navigationButtons = new ArrayList<>();
    public static Button dashboardBtn;
    public static Button fleetBtn;
    public static Button rentalRequestsBtn;
    public static Button earningsBtn;
    public static Button maintenanceBtn;
    public static Button analyticsBtn;
    public static Button settingsBtn;
    public static Button supportBtn;

    public static StackPane root;

    public ProviderLeftSideBar(ProviderDashboard obj) {
        root = ProviderDashboard.root;
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

        Text roleBadge = new Text("🚜 Provider Hub");
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

        dashboardBtn = new Button("⌂   Dashboard");
        styleMenuButton(dashboardBtn);

        fleetBtn = new Button("🚜  My Fleet & Machinery");
        styleMenuButton(fleetBtn);

        rentalRequestsBtn = new Button("📥  Rental Requests");
        styleMenuButton(rentalRequestsBtn);

        earningsBtn = new Button("💰  Earnings & Payouts");
        styleMenuButton(earningsBtn);

        settingsBtn = new Button("⚙   Settings & Profile");
        styleMenuButton(settingsBtn);

        supportBtn = new Button("💬  Help & Support Desk");
        styleMenuButton(supportBtn);

        // Keep references for compatibility
        maintenanceBtn = new Button("🛠  Service & Health");
        analyticsBtn = new Button("📊  Fleet Analytics");

        VBox vBoxBtn1 = new VBox(6, dashboardBtn, fleetBtn, rentalRequestsBtn, earningsBtn, settingsBtn, supportBtn);

        Button logoutBtn = new Button("↪   Logout");
        logoutBtn.setOnAction(e -> {
            if (ref != null) {
                ref.run();
            }
        });
        styleLogoutButton(logoutBtn);

        VBox vBoxBtn2 = new VBox(6, logoutBtn);

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
                "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.18), 12, 0, 3, 0);");

        navigationButtons.add(dashboardBtn);
        navigationButtons.add(fleetBtn);
        navigationButtons.add(rentalRequestsBtn);
        navigationButtons.add(earningsBtn);
        navigationButtons.add(settingsBtn);
        navigationButtons.add(supportBtn);

        setActiveButton(dashboardBtn, navigationButtons);

        // Sidebar Navigation Actions
        dashboardBtn.setOnAction(event -> {
            setActiveButton(dashboardBtn, navigationButtons);
            ProviderDashboard.borderPane.setCenter(Dashboard.getPage());
        });

        fleetBtn.setOnAction(event -> {
            setActiveButton(fleetBtn, navigationButtons);
            ProviderDashboard.borderPane.setCenter(MyEquipment.getFleetSection(root));
        });

        rentalRequestsBtn.setOnAction(event -> {
            setActiveButton(rentalRequestsBtn, navigationButtons);
            ProviderDashboard.borderPane.setCenter(RentalRequests.getRequestsSection(root));
        });

        earningsBtn.setOnAction(event -> {
            setActiveButton(earningsBtn, navigationButtons);
            ProviderDashboard.borderPane.setCenter(Earnings.getEarningsSection(root));
        });

        settingsBtn.setOnAction(event -> {
            setActiveButton(settingsBtn, navigationButtons);
            ProviderDashboard.borderPane.setCenter(ProviderSettings.getSettingsSection());
        });

        supportBtn.setOnAction(event -> {
            setActiveButton(supportBtn, navigationButtons);
            ProviderDashboard.borderPane.setCenter(ProviderHelp.getHelpSection());
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
