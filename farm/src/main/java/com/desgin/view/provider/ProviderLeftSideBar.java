package com.desgin.view.provider;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
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

        Image logoImage = null;
        try {
            logoImage = new Image("file:farm/src/main/resources/assets/Images/logo.png");
        } catch (Exception e) {
            // fallback if needed
        }
        
        ImageView logoImageView;
        if (logoImage != null && !logoImage.isError()) {
            logoImageView = new ImageView(logoImage);
            logoImageView.setFitWidth(44);
            logoImageView.setFitHeight(44);
            logoImageView.setPreserveRatio(false);
            logoImageView.setSmooth(true);
        } else {
            logoImageView = new ImageView();
            logoImageView.setFitWidth(44);
            logoImageView.setFitHeight(44);
        }

        Text textName = new Text("FarmEquip");
        textName.setStyle("-fx-font-size: 26px;" + "-fx-font-weight: bold;" + "-fx-font-family: 'Poppins';"
                + "-fx-fill: #4A2C20;");

        Text roleBadge = new Text("PROVIDER HUB");
        roleBadge.setStyle("-fx-font-size: 9px;" + "-fx-font-weight: bold;" + "-fx-font-family: 'Poppins';"
                + "-fx-fill: #8B6F47;" + "-fx-letter-spacing: 1px;");

        VBox brandBox = new VBox(1, textName, roleBadge);
        brandBox.setAlignment(Pos.CENTER_LEFT);

        dashboardBtn = new Button("⌂   Dashboard");
        styleMenuButton(dashboardBtn);

        fleetBtn = new Button("🚜  My Fleet & Machinery");
        styleMenuButton(fleetBtn);

        rentalRequestsBtn = new Button("📥  Rental Requests");
        styleMenuButton(rentalRequestsBtn);

        earningsBtn = new Button("💰  Earnings & Payouts");
        styleMenuButton(earningsBtn);

        maintenanceBtn = new Button("🛠  Service & Health");
        styleMenuButton(maintenanceBtn);

        analyticsBtn = new Button("📊  Fleet Analytics");
        styleMenuButton(analyticsBtn);

        VBox vBoxBtn1 = new VBox(8, 
            new HBox(dashboardBtn), 
            new HBox(fleetBtn), 
            new HBox(rentalRequestsBtn), 
            new HBox(earningsBtn), 
            new HBox(maintenanceBtn),
            new HBox(analyticsBtn)
        );

        settingsBtn = new Button("⚙   Settings");
        styleMenuButton(settingsBtn);

        supportBtn = new Button("❓  Help & Support");
        styleMenuButton(supportBtn);

        Button logoutBtn = new Button("↪   Logout");
        logoutBtn.setOnAction(e -> {
            if (ref != null) {
                ref.run();
            }
        });
        styleLogoutButton(logoutBtn);

        VBox vBoxBtn2 = new VBox(8, new HBox(settingsBtn), new HBox(supportBtn), new HBox(logoutBtn));

        Region spacer = new Region();
        VBox.setVgrow(spacer, javafx.scene.layout.Priority.ALWAYS);
        VBox btnBox = new VBox(10, vBoxBtn1, spacer, vBoxBtn2);

        HBox logoTextHBox = new HBox(10, logoImageView, brandBox);
        logoTextHBox.setAlignment(Pos.CENTER_LEFT);
        logoTextHBox.setPadding(new Insets(0, 5, 10, 5));

        VBox leftVB = new VBox(logoTextHBox, btnBox);
        leftVB.setPrefWidth(260);
        leftVB.setMinWidth(260);
        leftVB.setMaxWidth(260);
        leftVB.setMaxHeight(Double.MAX_VALUE);
        leftVB.setSpacing(20);
        leftVB.setPadding(new Insets(25, 18, 25, 18));
        leftVB.setStyle("-fx-background-color: #F5EFE6;" + "-fx-background-radius: 15;"
                + "-fx-border-color: #D8C7B5;" + "-fx-border-width: 1;" + "-fx-border-radius: 15;");

        navigationButtons.add(dashboardBtn);
        navigationButtons.add(fleetBtn);
        navigationButtons.add(rentalRequestsBtn);
        navigationButtons.add(earningsBtn);
        navigationButtons.add(maintenanceBtn);
        navigationButtons.add(analyticsBtn);
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

        maintenanceBtn.setOnAction(event -> {
            setActiveButton(maintenanceBtn, navigationButtons);
            ProviderDashboard.borderPane.setCenter(Maintenance.getMaintenanceSection(root));
        });

        analyticsBtn.setOnAction(event -> {
            setActiveButton(analyticsBtn, navigationButtons);
            ProviderDashboard.borderPane.setCenter(ProviderAnalytics.getAnalyticsSection());
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
            button.setStyle(
                    "-fx-background-color: transparent;" +
                    "-fx-background-radius: 10;" +
                    "-fx-text-fill: #5C4033;" +
                    "-fx-font-family: 'Poppins';" +
                    "-fx-font-size: 13.5px;" +
                    "-fx-font-weight: normal;" +
                    "-fx-cursor: hand;");
        }

        if (selected != null) {
            selected.setStyle(
                    "-fx-background-color: #E4D3C2;" +
                    "-fx-background-radius: 10;" +
                    "-fx-text-fill: #4A2C20;" +
                    "-fx-font-family: 'Poppins';" +
                    "-fx-font-size: 13.5px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-cursor: hand;");
        }
    }

    private void styleMenuButton(Button button) {
        button.setPrefWidth(224);
        button.setMinWidth(224);
        button.setMaxWidth(224);

        button.setPrefHeight(44);
        button.setMinHeight(44);
        button.setMaxHeight(44);

        button.setAlignment(Pos.CENTER_LEFT);
        button.setPadding(new Insets(0, 15, 0, 15));

        // Normal style
        button.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-background-radius: 10;" +
                "-fx-text-fill: #5C4033;" +
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 13.5px;" +
                "-fx-font-weight: normal;" +
                "-fx-cursor: hand;");

        // Hover
        button.setOnMouseEntered(e -> {
            if (button != activeButton) {
                button.setStyle(
                        "-fx-background-color: #E4D3C2;" +
                        "-fx-background-radius: 10;" +
                        "-fx-text-fill: #3E2723;" +
                        "-fx-font-family: 'Poppins';" +
                        "-fx-font-size: 13.5px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-cursor: hand;");
            }
        });

        // Mouse leaves
        button.setOnMouseExited(e -> {
            if (button != activeButton) {
                button.setStyle(
                        "-fx-background-color: transparent;" +
                        "-fx-background-radius: 10;" +
                        "-fx-text-fill: #5C4033;" +
                        "-fx-font-family: 'Poppins';" +
                        "-fx-font-size: 13.5px;" +
                        "-fx-font-weight: normal;" +
                        "-fx-cursor: hand;");
            }
        });
    }

    private void styleLogoutButton(Button button) {
        button.setPrefWidth(224);
        button.setMinWidth(224);
        button.setMaxWidth(224);

        button.setPrefHeight(44);
        button.setMinHeight(44);
        button.setMaxHeight(44);

        button.setAlignment(Pos.CENTER_LEFT);
        button.setPadding(new Insets(0, 15, 0, 15));

        button.setFont(javafx.scene.text.Font.font("Poppins", 13.5));
        button.setTextFill(javafx.scene.paint.Color.web("#FFFFFF"));

        button.setStyle(
                "-fx-background-color: #8B3A3A;" +
                "-fx-background-radius: 10;" +
                "-fx-cursor: hand;");

        button.setOnMouseEntered(e -> {
            button.setStyle(
                    "-fx-background-color: #A94442;" +
                    "-fx-background-radius: 10;" +
                    "-fx-cursor: hand;");
        });

        button.setOnMouseExited(e -> {
            button.setStyle(
                    "-fx-background-color: #8B3A3A;" +
                    "-fx-background-radius: 10;" +
                    "-fx-cursor: hand;");
        });
    }
}
