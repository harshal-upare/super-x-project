package com.desgin.view.operator;

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

public class OperatorLeftSideBar {

    private static Button activeButton;

    public static List<Button> navigationButtons = new ArrayList<>();
    public static Button dashboardBtn;
    public static Button jobsBtn;
    public static Button jobRequestsBtn;
    public static Button reviewsBtn;
    public static Button earningsBtn;
    public static Button settingsBtn;

    // Retained for backward compatibility
    public static Button machineryBtn;
    public static Button fieldLogsBtn;
    public static Button analyticsBtn;
    public static Button supportBtn;

    public static StackPane root;

    public OperatorLeftSideBar(OperatorDashboard obj) {
        root = OperatorDashboard.root;
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
                "-fx-background-color: #E8F5E9;" +
                "-fx-background-radius: 10px;" +
                "-fx-border-color: rgba(45, 106, 79, 0.2);" +
                "-fx-border-radius: 10px;" +
                "-fx-border-width: 1px;");

        Text textName = new Text("FarmEquip");
        textName.setStyle(
                "-fx-font-size: 19px;" +
                "-fx-font-weight: bold;" +
                "-fx-font-family: 'Poppins';" +
                "-fx-fill: #1B4332;");

        Text roleBadge = new Text("👷 Operator Portal");
        roleBadge.setStyle(
                "-fx-font-size: 11px;" +
                "-fx-font-weight: 600;" +
                "-fx-font-family: 'Poppins';" +
                "-fx-fill: #2D6A4F;");

        VBox brandBox = new VBox(1, textName, roleBadge);
        brandBox.setAlignment(Pos.CENTER_LEFT);

        HBox logoTextHBox = new HBox(10, logoContainer, brandBox);
        logoTextHBox.setAlignment(Pos.CENTER_LEFT);
        logoTextHBox.setPadding(new Insets(4, 6, 16, 6));
        logoTextHBox.setStyle("-fx-border-color: #E2EBE5; -fx-border-width: 0 0 1px 0;");

        dashboardBtn = new Button("⌂  Dashboard");
        styleMenuButton(dashboardBtn);

        jobsBtn = new Button("🚜  Jobs");
        styleMenuButton(jobsBtn);

        jobRequestsBtn = new Button("📥  Job Requests");
        styleMenuButton(jobRequestsBtn);

        reviewsBtn = new Button("⭐  Reviews & Rating");
        styleMenuButton(reviewsBtn);

        earningsBtn = new Button("💳  Payment Details");
        styleMenuButton(earningsBtn);

        settingsBtn = new Button("⚙   Settings");
        styleMenuButton(settingsBtn);

        supportBtn = new Button("❓  Help & Support");
        styleMenuButton(supportBtn);

        // Retain references for backward compatibility
        machineryBtn = new Button("🚜  Assigned Machinery");
        fieldLogsBtn = new Button("⏱  Field Logs");
        analyticsBtn = new Button("📊  Performance");

        VBox vBoxBtn1 = new VBox(6, dashboardBtn, jobsBtn, jobRequestsBtn, reviewsBtn, earningsBtn);

        Button logoutBtn = new Button("↪   Logout");
        logoutBtn.setOnAction(e -> {
            if (ref != null) {
                ref.run();
            }
        });
        styleLogoutButton(logoutBtn);

        VBox vBoxBtn2 = new VBox(6, settingsBtn, supportBtn, logoutBtn);

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
                "-fx-background-color: #FFFFFF;" +
                "-fx-border-color: #E2EBE5;" +
                "-fx-border-width: 0 1px 0 0;");

        navigationButtons.add(dashboardBtn);
        navigationButtons.add(jobsBtn);
        navigationButtons.add(jobRequestsBtn);
        navigationButtons.add(reviewsBtn);
        navigationButtons.add(earningsBtn);
        navigationButtons.add(settingsBtn);
        navigationButtons.add(supportBtn);

        setActiveButton(dashboardBtn, navigationButtons);

        dashboardBtn.setOnAction(event -> navigateToDashboard());
        jobsBtn.setOnAction(event -> navigateToJobs());
        jobRequestsBtn.setOnAction(event -> navigateToJobRequests());
        reviewsBtn.setOnAction(event -> navigateToReviews());
        earningsBtn.setOnAction(event -> navigateToEarnings());
        settingsBtn.setOnAction(event -> navigateToSettings());
        supportBtn.setOnAction(event -> navigateToHelp());

        return leftVB;
    }

    public static void navigateToDashboard() {
        if (dashboardBtn != null && navigationButtons != null) {
            setActiveButton(dashboardBtn, navigationButtons);
            OperatorProfileManagement.updateHeaderGreeting();
            if (OperatorDashboard.borderPane != null) {
                OperatorDashboard.borderPane.setCenter(OperatorHome.getPage());
            }
        }
    }

    public static void navigateToJobs() {
        if (jobsBtn != null && navigationButtons != null) {
            setActiveButton(jobsBtn, navigationButtons);
            OperatorProfileManagement.setHeaderTitle("Jobs 🚜", "Search, track and manage all active, pending, completed and cancelled jobs");
            if (OperatorDashboard.borderPane != null) {
                OperatorDashboard.borderPane.setCenter(OperatorJobs.getJobsSection(root != null ? root : OperatorDashboard.root));
            }
        }
    }

    public static void navigateToJobRequests() {
        if (jobRequestsBtn != null && navigationButtons != null) {
            setActiveButton(jobRequestsBtn, navigationButtons);
            OperatorProfileManagement.setHeaderTitle("Job Requests 📥", "Review incoming farmer requests, approve assignments & manage shifts");
            if (OperatorDashboard.borderPane != null) {
                OperatorDashboard.borderPane.setCenter(OperatorJobRequests.getJobRequestsSection(root != null ? root : OperatorDashboard.root));
            }
        }
    }

    public static void navigateToReviews() {
        if (reviewsBtn != null && navigationButtons != null) {
            setActiveButton(reviewsBtn, navigationButtons);
            OperatorProfileManagement.setHeaderTitle("Reviews & Rating ⭐", "View farmer ratings, feedback history and performance reputation");
            if (OperatorDashboard.borderPane != null) {
                OperatorDashboard.borderPane.setCenter(OperatorReviews.getReviewsSection(root != null ? root : OperatorDashboard.root));
            }
        }
    }

    public static void navigateToEarnings() {
        if (earningsBtn != null && navigationButtons != null) {
            setActiveButton(earningsBtn, navigationButtons);
            OperatorProfileManagement.setHeaderTitle("Payment Details 💳", "Track operator earnings, admin commission breakdown & payout settlements");
            if (OperatorDashboard.borderPane != null) {
                OperatorDashboard.borderPane.setCenter(OperatorEarnings.getEarningsSection(root != null ? root : OperatorDashboard.root));
            }
        }
    }

    public static void navigateToPaymentDetails() {
        navigateToEarnings();
    }

    public static void navigateToSettings() {
        if (settingsBtn != null && navigationButtons != null) {
            setActiveButton(settingsBtn, navigationButtons);
            OperatorProfileManagement.setHeaderTitle("Operator Settings ⚙", "Manage operator license, contact profile and security");
            if (OperatorDashboard.borderPane != null) {
                OperatorDashboard.borderPane.setCenter(OperatorSettings.getSettingsSection());
            }
        }
    }

    public static void navigateToHelp() {
        if (supportBtn != null && navigationButtons != null) {
            setActiveButton(supportBtn, navigationButtons);
            OperatorProfileManagement.setHeaderTitle("Help & Support Desk 🛟", "24x7 machinery hotline and technical field support");
            if (OperatorDashboard.borderPane != null) {
                OperatorDashboard.borderPane.setCenter(OperatorHelp.getHelpSection());
            }
        }
    }

    public static void setActiveButton(Button selected, List<Button> buttons) {
        activeButton = selected;
        for (Button button : buttons) {
            if (button == null) continue;
            if (button == selected) {
                button.setStyle(
                        "-fx-background-color: linear-gradient(to right, #2D6A4F, #40916C);" +
                        "-fx-background-radius: 10px;" +
                        "-fx-text-fill: #FFFFFF;" +
                        "-fx-font-family: 'Poppins';" +
                        "-fx-font-size: 13.5px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-cursor: hand;" +
                        "-fx-effect: dropshadow(gaussian, rgba(45, 106, 79, 0.25), 8, 0, 0, 2);");
            } else {
                button.setStyle(
                        "-fx-background-color: transparent;" +
                        "-fx-background-radius: 10px;" +
                        "-fx-text-fill: #374151;" +
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
                "-fx-text-fill: #374151;" +
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 13.5px;" +
                "-fx-font-weight: 500;" +
                "-fx-cursor: hand;");

        button.setOnMouseEntered(e -> {
            if (button != activeButton) {
                button.setStyle(
                        "-fx-background-color: #F0FDF4;" +
                        "-fx-background-radius: 10px;" +
                        "-fx-text-fill: #1B4332;" +
                        "-fx-font-family: 'Poppins';" +
                        "-fx-font-size: 13.5px;" +
                        "-fx-font-weight: 600;" +
                        "-fx-cursor: hand;" +
                        "-fx-border-color: #D1E7DD;" +
                        "-fx-border-radius: 10px;" +
                        "-fx-border-width: 1px;");
            }
        });

        button.setOnMouseExited(e -> {
            if (button != activeButton) {
                button.setStyle(
                        "-fx-background-color: transparent;" +
                        "-fx-background-radius: 10px;" +
                        "-fx-text-fill: #374151;" +
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
                "-fx-background-color: #FEF2F2;" +
                "-fx-background-radius: 10px;" +
                "-fx-text-fill: #DC2626;" +
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 13.5px;" +
                "-fx-font-weight: bold;" +
                "-fx-cursor: hand;");

        button.setOnMouseEntered(e -> {
            button.setStyle(
                    "-fx-background-color: #FEE2E2;" +
                    "-fx-background-radius: 10px;" +
                    "-fx-text-fill: #B91C1C;" +
                    "-fx-font-family: 'Poppins';" +
                    "-fx-font-size: 13.5px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-cursor: hand;");
        });

        button.setOnMouseExited(e -> {
            button.setStyle(
                    "-fx-background-color: #FEF2F2;" +
                    "-fx-background-radius: 10px;" +
                    "-fx-text-fill: #DC2626;" +
                    "-fx-font-family: 'Poppins';" +
                    "-fx-font-size: 13.5px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-cursor: hand;");
        });
    }
}
