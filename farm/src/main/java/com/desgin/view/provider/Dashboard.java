package com.desgin.view.provider;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class Dashboard {

    public static ScrollPane getPage() {
        String providerName = "Rajesh Patil";

        Text welcomeText = new Text("Welcome back, " + providerName + " 🌾");
        welcomeText.setStyle(
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 26px;" +
                "-fx-font-weight: bold;" +
                "-fx-fill: #4A2C20;");

        Text dashboardText = new Text("Provider Fleet Command Center");
        dashboardText.setStyle(
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 16px;" +
                "-fx-font-weight: bold;" +
                "-fx-fill: #5C4033;");

        Text descriptionText = new Text("Monitor agricultural machinery fleet, approve instant rental bookings & review earnings");
        descriptionText.setStyle(
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 13px;" +
                "-fx-fill: #806A5B;");

        VBox headerText = new VBox(4, welcomeText, dashboardText, descriptionText);
        headerText.setAlignment(Pos.TOP_LEFT);

        // Live Weather Card
        VBox weatherCard = createWeatherCard("Pune Agri Hub", 18.5204, 73.8567);
        weatherCard.setAlignment(Pos.TOP_RIGHT);

        HBox topRow = new HBox(20, headerText, weatherCard);
        HBox.setHgrow(headerText, Priority.ALWAYS);
        topRow.setAlignment(Pos.CENTER_LEFT);

        // Search Bar & Filter Bar
        TextField searchField = new TextField();
        searchField.setPromptText("Search fleet machinery, model, serial no, or booking ID...");
        searchField.setPrefHeight(42);
        searchField.setPrefWidth(480);
        searchField.setStyle(
                "-fx-background-color: #FFFFFF;" +
                "-fx-background-radius: 10;" +
                "-fx-border-color: #D8C7B5;" +
                "-fx-border-radius: 10;" +
                "-fx-border-width: 1;" +
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 13px;" +
                "-fx-text-fill: #4A2C20;" +
                "-fx-prompt-text-fill: #A18C7A;");

        Text searchIcon = new Text("🔍");
        searchIcon.setStyle("-fx-font-size: 18px;");

        Button searchButton = new Button("Search");
        searchButton.setPrefWidth(90);
        searchButton.setPrefHeight(42);
        searchButton.setStyle(
                "-fx-background-color: #6B8E23;" +
                "-fx-background-radius: 10;" +
                "-fx-text-fill: white;" +
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 13px;" +
                "-fx-font-weight: bold;" +
                "-fx-cursor: hand;");

        searchButton.setOnMouseEntered(e -> searchButton.setStyle("-fx-background-color: #55751C; -fx-background-radius: 10; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-font-weight: bold; -fx-cursor: hand;"));
        searchButton.setOnMouseExited(e -> searchButton.setStyle("-fx-background-color: #6B8E23; -fx-background-radius: 10; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-font-weight: bold; -fx-cursor: hand;"));

        HBox searchBox = new HBox(10, searchIcon, searchField, searchButton);
        searchBox.setAlignment(Pos.CENTER_LEFT);

        // 5 Elevated KPI Cards
        VBox fleetKpiCard = createDashboardCard("🚜", "Total Fleet Listed", "18 Units", "14 Avail • 4 Rented", "#2E7D32", () -> {
            ProviderLeftSideBar.setActiveButton(ProviderLeftSideBar.fleetBtn, ProviderLeftSideBar.navigationButtons);
            ProviderDashboard.borderPane.setCenter(MyEquipment.getFleetSection(ProviderDashboard.root));
        });

        VBox pendingKpiCard = createDashboardCard("📥", "Pending Requests", "5 New", "⚡ Action Required", "#E65100", () -> {
            ProviderLeftSideBar.setActiveButton(ProviderLeftSideBar.rentalRequestsBtn, ProviderLeftSideBar.navigationButtons);
            ProviderDashboard.borderPane.setCenter(RentalRequests.getRequestsSection(ProviderDashboard.root));
        });

        VBox revenueKpiCard = createDashboardCard("💰", "Monthly Revenue", "₹1,24,500", "+22.4% vs last mo ↑", "#2E7D32", () -> {
            ProviderLeftSideBar.setActiveButton(ProviderLeftSideBar.earningsBtn, ProviderLeftSideBar.navigationButtons);
            ProviderDashboard.borderPane.setCenter(Earnings.getEarningsSection(ProviderDashboard.root));
        });

        VBox inUseKpiCard = createDashboardCard("⏱", "Machinery In-Field", "4 Deployed", "All GPS Live Active", "#1976D2", () -> {
            ProviderLeftSideBar.setActiveButton(ProviderLeftSideBar.maintenanceBtn, ProviderLeftSideBar.navigationButtons);
            ProviderDashboard.borderPane.setCenter(Maintenance.getMaintenanceSection(ProviderDashboard.root));
        });

        VBox ratingKpiCard = createDashboardCard("⭐", "Provider Rating", "4.9 / 5.0", "128 Verified Reviews", "#8B6F47", () -> {
            ProviderLeftSideBar.setActiveButton(ProviderLeftSideBar.analyticsBtn, ProviderLeftSideBar.navigationButtons);
            ProviderDashboard.borderPane.setCenter(ProviderAnalytics.getAnalyticsSection());
        });

        HBox cards = new HBox(14, fleetKpiCard, pendingKpiCard, revenueKpiCard, inUseKpiCard, ratingKpiCard);
        cards.setAlignment(Pos.CENTER_LEFT);

        // Quick Action Command Toolbar
        HBox quickActions = createQuickActionToolbar();

        // Section 1: Actionable Pending Rental Requests
        VBox pendingSection = createPendingRequestsSection();

        // Section 2: My Fleet Inventory Highlights
        VBox fleetSection = createFleetCarouselSection();

        // Section 3: Active Machinery on Field Tracking
        VBox activeDeploymentSection = createActiveDeploymentsSection();

        // Section 4: Recent Earnings & Payout Transactions
        VBox recentEarningsSection = createRecentEarningsSection();

        VBox centerContent = new VBox(22,
                topRow,
                searchBox,
                cards,
                quickActions,
                pendingSection,
                fleetSection,
                activeDeploymentSection,
                recentEarningsSection
        );
        centerContent.setPadding(new Insets(20, 30, 35, 30));

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(centerContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        ProviderDashboard.borderPane.setCenter(scrollPane);
        return scrollPane;
    }

    private static HBox createQuickActionToolbar() {
        Button addEqBtn = createActionButton("➕  Add New Machinery", "#2E7D32", () -> {
            ProviderLeftSideBar.setActiveButton(ProviderLeftSideBar.fleetBtn, ProviderLeftSideBar.navigationButtons);
            ProviderDashboard.borderPane.setCenter(MyEquipment.getFleetSection(ProviderDashboard.root));
        });

        Button reviewBtn = createActionButton("📥  Review Bookings (5)", "#8B6F47", () -> {
            ProviderLeftSideBar.setActiveButton(ProviderLeftSideBar.rentalRequestsBtn, ProviderLeftSideBar.navigationButtons);
            ProviderDashboard.borderPane.setCenter(RentalRequests.getRequestsSection(ProviderDashboard.root));
        });

        Button payoutBtn = createActionButton("💸  Instant Bank Payout", "#5C4033", () -> {
            ProviderLeftSideBar.setActiveButton(ProviderLeftSideBar.earningsBtn, ProviderLeftSideBar.navigationButtons);
            ProviderDashboard.borderPane.setCenter(Earnings.getEarningsSection(ProviderDashboard.root));
        });

        Button logMaintBtn = createActionButton("🛠  Log Fleet Service", "#37474F", () -> {
            ProviderLeftSideBar.setActiveButton(ProviderLeftSideBar.maintenanceBtn, ProviderLeftSideBar.navigationButtons);
            ProviderDashboard.borderPane.setCenter(Maintenance.getMaintenanceSection(ProviderDashboard.root));
        });

        HBox bar = new HBox(12, addEqBtn, reviewBtn, payoutBtn, logMaintBtn);
        bar.setAlignment(Pos.CENTER_LEFT);
        return bar;
    }

    private static Button createActionButton(String text, String bg, Runnable action) {
        Button btn = new Button(text);
        btn.setPrefHeight(38);
        btn.setStyle("-fx-background-color: " + bg + "; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 0 16 0 16;");
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: derive(" + bg + ", -15%); -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 0 16 0 16;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: " + bg + "; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 0 16 0 16;"));
        if (action != null) {
            btn.setOnAction(e -> action.run());
        }
        return btn;
    }

    private static VBox createPendingRequestsSection() {
        Text sectionTitle = new Text("Incoming Rental Requests (Awaiting Provider Approval)");
        sectionTitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 18px; -fx-font-weight: bold; -fx-fill: #4A2C20;");

        Text viewAll = new Text("View All 5 Requests →");
        viewAll.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-font-weight: bold; -fx-fill: #2E7D32; -fx-cursor: hand;");
        viewAll.setOnMouseClicked(e -> {
            ProviderLeftSideBar.setActiveButton(ProviderLeftSideBar.rentalRequestsBtn, ProviderLeftSideBar.navigationButtons);
            ProviderDashboard.borderPane.setCenter(RentalRequests.getRequestsSection(ProviderDashboard.root));
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox header = new HBox(sectionTitle, spacer, viewAll);
        header.setAlignment(Pos.CENTER_LEFT);

        VBox req1 = createRequestCard("Suresh Patil", "Baramati (12 km away)", "John Deere 5310 4WD (55 HP)", "18 Aug - 22 Aug (5 Days)", "₹6,000", "With Driver/Operator", "🚜 Tractor");
        VBox req2 = createRequestCard("Anand Kadam", "Indapur (28 km away)", "Kartar 4000 Multicrop Harvester", "20 Aug - 23 Aug (4 Days)", "₹14,000", "Self-Pickup by Farmer", "🌾 Harvester");
        VBox req3 = createRequestCard("Dnyaneshwar Shinde", "Saswad (18 km away)", "Shaktiman Rotary Tiller (7ft)", "19 Aug - 20 Aug (2 Days)", "₹2,400", "Provider Delivery Needed", "⚙ Rotavator");

        HBox requestsRow = new HBox(15, req1, req2, req3);
        requestsRow.setAlignment(Pos.CENTER_LEFT);

        return new VBox(12, header, requestsRow);
    }

    private static VBox createRequestCard(String farmer, String location, String eq, String dates, String price, String delivery, String catTag) {
        Text farmerName = new Text("👨‍🌾 " + farmer);
        farmerName.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 15px; -fx-font-weight: bold; -fx-fill: #4A2C20;");

        Text catBadge = new Text(" " + catTag + " ");
        catBadge.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 10.5px; -fx-font-weight: bold; -fx-fill: #2E7D32; -fx-background-color: #E8F5E9;");

        HBox topH = new HBox(8, farmerName, new Region(), catBadge);
        HBox.setHgrow(topH.getChildren().get(1), Priority.ALWAYS);
        topH.setAlignment(Pos.CENTER_LEFT);

        Text locText = new Text("📍 " + location);
        locText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-fill: #806A5B;");

        Text eqText = new Text("🚜 " + eq);
        eqText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-font-weight: bold; -fx-fill: #2E7D32;");

        Text dateText = new Text("📅 " + dates);
        dateText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #5C4033;");

        Text delText = new Text("🚚 " + delivery);
        delText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-fill: #806A5B;");

        Text priceText = new Text(price);
        priceText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 18px; -fx-font-weight: bold; -fx-fill: #4A2C20;");

        Text netText = new Text("Gross Fare");
        netText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 10px; -fx-fill: #806A5B;");

        VBox priceBox = new VBox(1, priceText, netText);

        Button acceptBtn = new Button("✔ Approve");
        acceptBtn.setStyle("-fx-background-color: #2E7D32; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand;");
        acceptBtn.setPrefHeight(32);
        acceptBtn.setPrefWidth(90);

        Button rejectBtn = new Button("✕ Decline");
        rejectBtn.setStyle("-fx-background-color: #8B3A3A; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand;");
        rejectBtn.setPrefHeight(32);
        rejectBtn.setPrefWidth(85);

        HBox btnRow = new HBox(8, acceptBtn, rejectBtn);
        btnRow.setAlignment(Pos.CENTER_RIGHT);

        Region cardSpacer = new Region();
        HBox.setHgrow(cardSpacer, Priority.ALWAYS);
        HBox bottomRow = new HBox(priceBox, cardSpacer, btnRow);
        bottomRow.setAlignment(Pos.CENTER_LEFT);

        VBox card = new VBox(8, topH, locText, eqText, dateText, delText, bottomRow);
        card.setPrefWidth(350);
        card.setPadding(new Insets(16));
        card.setStyle(
                "-fx-background-color: #F5EFE6;" +
                "-fx-background-radius: 14;" +
                "-fx-border-color: #D8C7B5;" +
                "-fx-border-width: 1;" +
                "-fx-border-radius: 14;");

        acceptBtn.setOnAction(e -> {
            acceptBtn.setText("Approved ✓");
            acceptBtn.setStyle("-fx-background-color: #1B5E20; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 6;");
            rejectBtn.setVisible(false);
            card.setStyle("-fx-background-color: #E8F5E9; -fx-background-radius: 14; -fx-border-color: #81C784; -fx-border-width: 1.5; -fx-border-radius: 14;");
        });

        rejectBtn.setOnAction(e -> {
            rejectBtn.setText("Declined ✕");
            acceptBtn.setVisible(false);
            card.setStyle("-fx-background-color: #FFEBEE; -fx-background-radius: 14; -fx-border-color: #E57373; -fx-border-width: 1.5; -fx-border-radius: 14;");
        });

        return card;
    }

    private static VBox createFleetCarouselSection() {
        Text fleetTitle = new Text("My Machinery Fleet & Availability");
        fleetTitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 18px; -fx-font-weight: bold; -fx-fill: #4A2C20;");

        Text manageFleet = new Text("Manage All 18 Machines →");
        manageFleet.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-font-weight: bold; -fx-fill: #2E7D32; -fx-cursor: hand;");
        manageFleet.setOnMouseClicked(e -> {
            ProviderLeftSideBar.setActiveButton(ProviderLeftSideBar.fleetBtn, ProviderLeftSideBar.navigationButtons);
            ProviderDashboard.borderPane.setCenter(MyEquipment.getFleetSection(ProviderDashboard.root));
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox header = new HBox(fleetTitle, spacer, manageFleet);
        header.setAlignment(Pos.CENTER_LEFT);

        VBox c1 = createFleetCard("🚜", "Mahindra 575 DI (45HP)", "Tractor", "₹1,200 / day", "AVAILABLE", "#2E7D32", "24 Bookings");
        VBox c2 = createFleetCard("🚜", "John Deere 5310 (55HP)", "Tractor", "₹1,500 / day", "RENTED OUT", "#E65100", "41 Bookings");
        VBox c3 = createFleetCard("⚙", "Shaktiman Rotary Tiller 7ft", "Rotavator", "₹800 / day", "AVAILABLE", "#2E7D32", "19 Bookings");
        VBox c4 = createFleetCard("🌾", "Kartar 4000 Multi-Crop", "Harvester", "₹3,500 / day", "RENTED OUT", "#E65100", "32 Bookings");
        VBox c5 = createFleetCard("🚁", "Agri-Drone 16L Sprayer", "Drone", "₹1,800 / day", "IN SERVICE", "#C62828", "12 Bookings");

        HBox fleetRow = new HBox(16, c1, c2, c3, c4, c5);
        fleetRow.setAlignment(Pos.CENTER_LEFT);
        fleetRow.setPadding(new Insets(6, 0, 8, 0));

        ScrollPane scroll = new ScrollPane(fleetRow);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setFitToHeight(true);
        scroll.setStyle("-fx-background-color: transparent; -fx-background: transparent; -fx-border-color: transparent;");

        return new VBox(12, header, scroll);
    }

    private static VBox createFleetCard(String icon, String name, String category, String price, String status, String statusColor, String stats) {
        Image img = null;
        try {
            img = new Image("file:farm/src/main/resources/assets/Images/tractor.png");
        } catch (Exception ignored) {}

        ImageView iv = new ImageView();
        if (img != null && !img.isError()) {
            iv.setImage(img);
            iv.setFitWidth(140);
            iv.setFitHeight(75);
            iv.setPreserveRatio(true);
        } else {
            iv.setFitWidth(140);
            iv.setFitHeight(75);
        }

        Text iconBadge = new Text(icon);
        iconBadge.setStyle("-fx-font-size: 28px;");

        StackPane imgBox = new StackPane(iv, iconBadge);
        StackPane.setAlignment(iconBadge, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(iconBadge, new Insets(0, 8, 4, 0));
        imgBox.setPrefHeight(90);
        imgBox.setStyle("-fx-background-color: #E4D3C2; -fx-background-radius: 10;");

        Label statusBadge = new Label(status);
        statusBadge.setStyle("-fx-background-color: " + statusColor + "; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 9.5px; -fx-font-weight: bold; -fx-padding: 3 8 3 8; -fx-background-radius: 4;");

        Text title = new Text(name);
        title.setWrappingWidth(190);
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 14px; -fx-font-weight: bold; -fx-fill: #4A2C20;");

        Text cat = new Text(category + " • " + stats);
        cat.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-fill: #806A5B;");

        Text pr = new Text(price);
        pr.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13.5px; -fx-font-weight: bold; -fx-fill: #2E7D32;");

        Button editBtn = new Button("Manage Unit ⚙");
        editBtn.setPrefWidth(190);
        editBtn.setPrefHeight(32);
        editBtn.setStyle("-fx-background-color: #8B6F47; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand;");

        editBtn.setOnAction(e -> {
            ProviderLeftSideBar.setActiveButton(ProviderLeftSideBar.fleetBtn, ProviderLeftSideBar.navigationButtons);
            ProviderDashboard.borderPane.setCenter(MyEquipment.getFleetSection(ProviderDashboard.root));
        });

        VBox card = new VBox(8, imgBox, statusBadge, title, cat, pr, editBtn);
        card.setPrefWidth(215);
        card.setPadding(new Insets(12));
        card.setStyle("-fx-background-color: #F5EFE6; -fx-background-radius: 12; -fx-border-color: #D8C7B5; -fx-border-width: 1; -fx-border-radius: 12;");

        card.setOnMouseEntered(e -> {
            card.setStyle("-fx-background-color: #FFF9F0; -fx-background-radius: 12; -fx-border-color: #8B6F47; -fx-border-width: 1.5; -fx-border-radius: 12; -fx-effect: dropshadow(gaussian, rgba(74,44,32,0.2), 10, 0.2, 0, 3);");
            card.setTranslateY(-2);
        });

        card.setOnMouseExited(e -> {
            card.setStyle("-fx-background-color: #F5EFE6; -fx-background-radius: 12; -fx-border-color: #D8C7B5; -fx-border-width: 1; -fx-border-radius: 12;");
            card.setTranslateY(0);
        });

        return card;
    }

    private static VBox createActiveDeploymentsSection() {
        Text title = new Text("Active On-Field Machinery (Currently Deployed)");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 18px; -fx-font-weight: bold; -fx-fill: #4A2C20;");

        VBox row1 = createDeploymentCard("🚜 John Deere 5310 Tractor (55HP)", "Farmer: Ramesh Waghmare • Saswad Farm (Plot 4)", "15 Aug → 19 Aug 2026 (Due in 2 Days)", "Driver Assigned: Dilip Shinde (+91 98901 44552)", "RUNNING OK • GPS ACTIVE", 0.65);
        VBox row2 = createDeploymentCard("🌾 Kartar 4000 Combine Harvester", "Farmer: Balasaheb Shirole • Baramati East", "14 Aug → 18 Aug 2026 (Due Tomorrow)", "Self Operated by Farmer • Fuel Full", "RUNNING OK • GPS ACTIVE", 0.85);

        return new VBox(12, title, row1, row2);
    }

    private static VBox createDeploymentCard(String title, String farmerInfo, String dateInfo, String driverInfo, String gpsStatus, double progress) {
        Text t = new Text(title);
        t.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 15px; -fx-font-weight: bold; -fx-fill: #4A2C20;");

        Text f = new Text(farmerInfo);
        f.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #5C4033;");

        Text d = new Text(dateInfo);
        d.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-fill: #8B6F47;");

        Text dr = new Text(driverInfo);
        dr.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-fill: #806A5B;");

        ProgressBar pb = new ProgressBar(progress);
        pb.setPrefWidth(220);
        pb.setPrefHeight(6);
        pb.setStyle("-fx-accent: #2E7D32;");

        VBox pBox = new VBox(3, new Text("Lease Progress: " + (int)(progress * 100) + "%"), pb);

        Label gps = new Label("● " + gpsStatus);
        gps.setStyle("-fx-background-color: #E8F5E9; -fx-text-fill: #2E7D32; -fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-padding: 4 10 4 10; -fx-background-radius: 6;");

        Button callBtn = new Button("📞 Contact Farmer");
        callBtn.setStyle("-fx-background-color: #6B8E23; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox statusRow = new HBox(15, gps, pBox, spacer, callBtn);
        statusRow.setAlignment(Pos.CENTER_LEFT);

        VBox card = new VBox(6, t, f, d, dr, statusRow);
        card.setPadding(new Insets(14));
        card.setStyle("-fx-background-color: #F5EFE6; -fx-background-radius: 12; -fx-border-color: #D8C7B5; -fx-border-width: 1; -fx-border-radius: 12;");
        return card;
    }

    private static VBox createRecentEarningsSection() {
        Text title = new Text("Recent Payouts & Settlement Credits");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 18px; -fx-font-weight: bold; -fx-fill: #4A2C20;");

        HBox r1 = createPayoutRow("Harvester Rental #FE-8921", "Balasaheb Shirole", "14 Aug 2026", "₹12,600", "SETTLED TO BANK");
        HBox r2 = createPayoutRow("Rotavator 3-Day Rental #FE-8894", "Vikas More", "13 Aug 2026", "₹2,400", "SETTLED TO BANK");
        HBox r3 = createPayoutRow("Tractor 5-Day Rental #FE-8850", "Ganesh Jadhav", "10 Aug 2026", "₹7,500", "SETTLED TO BANK");

        return new VBox(12, title, r1, r2, r3);
    }

    private static HBox createPayoutRow(String booking, String farmer, String date, String amount, String status) {
        Text b = new Text(booking);
        b.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13.5px; -fx-font-weight: bold; -fx-fill: #4A2C20;");
        Text f = new Text("Farmer: " + farmer);
        f.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-fill: #806A5B;");
        VBox bBox = new VBox(2, b, f);

        Text dt = new Text(date);
        dt.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-fill: #5C4033;");

        Text amt = new Text(amount);
        amt.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 15px; -fx-font-weight: bold; -fx-fill: #2E7D32;");

        Label st = new Label(status);
        st.setStyle("-fx-background-color: #E8F5E9; -fx-text-fill: #2E7D32; -fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-padding: 4 10 4 10; -fx-background-radius: 6;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox row = new HBox(40, bBox, dt, amt, spacer, st);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(12, 16, 12, 16));
        row.setStyle("-fx-background-color: #F5EFE6; -fx-background-radius: 10; -fx-border-color: #D8C7B5; -fx-border-width: 1; -fx-border-radius: 10;");
        return row;
    }

    private static VBox createDashboardCard(String icon, String title, String value, String subtext, String badgeColor, Runnable onClick) {
        Text iconText = new Text(icon);
        iconText.setStyle("-fx-font-size: 24px;");

        StackPane iconHolder = new StackPane(iconText);
        iconHolder.setPrefSize(42, 42);
        iconHolder.setStyle("-fx-background-color: #E4D3C2; -fx-background-radius: 10;");

        Text titleText = new Text(title);
        titleText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #806A5B;");

        Text valueText = new Text(value);
        valueText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 20px; -fx-font-weight: bold; -fx-fill: #4A2C20;");

        Text subText = new Text(subtext);
        subText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 10px; -fx-font-weight: bold; -fx-fill: " + badgeColor + ";");

        HBox topH = new HBox(10, iconHolder, new VBox(2, titleText, valueText));
        topH.setAlignment(Pos.CENTER_LEFT);

        VBox card = new VBox(6, topH, subText);
        card.setPrefWidth(210);
        card.setPrefHeight(115);
        card.setPadding(new Insets(14));

        String normalStyle = "-fx-background-color: #F5EFE6; -fx-background-radius: 14; -fx-border-color: #D8C7B5; -fx-border-width: 1; -fx-border-radius: 14; -fx-cursor: hand;";
        String hoverStyle = "-fx-background-color: #FFF9F0; -fx-background-radius: 14; -fx-border-color: #8B6F47; -fx-border-width: 1.5; -fx-border-radius: 14; -fx-effect: dropshadow(gaussian, rgba(74,44,32,0.25), 12, 0.2, 0, 4); -fx-cursor: hand;";

        card.setStyle(normalStyle);

        card.setOnMouseEntered(e -> {
            card.setStyle(hoverStyle);
            card.setScaleX(1.02);
            card.setScaleY(1.02);
            card.setTranslateY(-3);
        });

        card.setOnMouseExited(e -> {
            card.setStyle(normalStyle);
            card.setScaleX(1.0);
            card.setScaleY(1.0);
            card.setTranslateY(0);
        });

        if (onClick != null) {
            card.setOnMouseClicked(e -> onClick.run());
        }

        return card;
    }

    private static VBox createWeatherCard(String location, double latitude, double longitude) {
        VBox weatherCard = new VBox(8);
        weatherCard.setPrefWidth(420);
        weatherCard.setPrefHeight(125);
        weatherCard.setPadding(new Insets(12, 18, 12, 18));
        weatherCard.setStyle("-fx-background-color: #6F91D5; -fx-background-radius: 18;");

        HBox topBox = new HBox(12);
        Text weatherIcon = new Text("☀");
        weatherIcon.setStyle("-fx-font-size: 36px;");

        VBox currentInfo = new VBox(2);
        Text temperature = new Text("Loading...");
        temperature.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 26px; -fx-font-weight: bold; -fx-fill: white;");

        Text condition = new Text("Loading weather...");
        condition.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: white;");

        Text locationText = new Text("⌖ " + location + " (Field Operations)");
        locationText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-fill: #E3F2FD;");

        currentInfo.getChildren().addAll(temperature, condition, locationText);
        topBox.getChildren().addAll(weatherIcon, currentInfo);

        HBox forecastBox = new HBox(16);
        Text[] days = new Text[4];
        for (int i = 0; i < 4; i++) {
            days[i] = new Text("Loading...");
            days[i].setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-fill: white;");
            forecastBox.getChildren().add(days[i]);
        }

        weatherCard.getChildren().addAll(topBox, forecastBox);
        loadWeather(latitude, longitude, weatherIcon, temperature, condition, days);
        return weatherCard;
    }

    private static void loadWeather(double latitude, double longitude, Text weatherIcon, Text temperature, Text condition, Text[] days) {
        Task<String> weatherTask = new Task<>() {
            @Override
            protected String call() throws Exception {
                String url = "https://api.open-meteo.com/v1/forecast?latitude=" + latitude + "&longitude=" + longitude
                        + "&current=temperature_2m,weather_code&daily=weather_code,temperature_2m_max,temperature_2m_min&timezone=auto&forecast_days=4";
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() != 200) {
                    throw new RuntimeException("HTTP " + response.statusCode());
                }
                return response.body();
            }
        };

        weatherTask.setOnSucceeded(e -> updateWeatherUI(weatherTask.getValue(), weatherIcon, temperature, condition, days));
        weatherTask.setOnFailed(e -> Platform.runLater(() -> {
            temperature.setText("28°C");
            condition.setText("Clear Field Weather");
            weatherIcon.setText("☀");
            String[] fallbackDays = {"Fri ☀ 30°/22°", "Sat 🌤 31°/21°", "Sun 🌦 29°/20°", "Mon ☀ 32°/23°"};
            for (int i = 0; i < 4 && i < days.length; i++) {
                days[i].setText(fallbackDays[i]);
            }
        }));

        Thread thread = new Thread(weatherTask);
        thread.setDaemon(true);
        thread.start();
    }

    private static void updateWeatherUI(String json, Text weatherIcon, Text temperature, Text condition, Text[] days) {
        try {
            int currentStart = json.indexOf("\"current\":{");
            if (currentStart != -1) {
                int currentEnd = json.indexOf("}", currentStart);
                String currentData = json.substring(currentStart, currentEnd);
                String currentTemperature = extractNumber(currentData, "\"temperature_2m\":");
                String currentCode = extractNumber(currentData, "\"weather_code\":");

                double tempVal = Double.parseDouble(currentTemperature);
                int code = Integer.parseInt(currentCode);

                temperature.setText(Math.round(tempVal) + "°C");
                condition.setText(getWeatherDesc(code));
                weatherIcon.setText(getWeatherIco(code));
            }

            int dailyStart = json.indexOf("\"daily\":{");
            if (dailyStart != -1) {
                String dailyData = json.substring(dailyStart);
                String[] dateArray = extractArray(dailyData, "\"time\":[").split(",");
                String[] maxArray = extractArray(dailyData, "\"temperature_2m_max\":[").split(",");
                String[] minArray = extractArray(dailyData, "\"temperature_2m_min\":[").split(",");
                String[] codeArray = extractArray(dailyData, "\"weather_code\":[").split(",");

                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("EEE", Locale.ENGLISH);
                for (int i = 0; i < 4 && i < days.length && i < dateArray.length; i++) {
                    String dStr = dateArray[i].replace("\"", "").trim();
                    LocalDate ld = LocalDate.parse(dStr);
                    int max = (int) Math.round(Double.parseDouble(maxArray[i].trim()));
                    int min = (int) Math.round(Double.parseDouble(minArray[i].trim()));
                    int dCode = Integer.parseInt(codeArray[i].trim());
                    days[i].setText(ld.format(fmt) + " " + getWeatherIco(dCode) + " " + max + "°/" + min + "°");
                }
            }
        } catch (Exception e) {
            temperature.setText("28°C");
            condition.setText("Ideal for Farming");
        }
    }

    private static String extractNumber(String sec, String key) {
        int st = sec.indexOf(key);
        if (st == -1) return "0";
        st += key.length();
        int end = st;
        while (end < sec.length() && sec.charAt(end) != ',' && sec.charAt(end) != '}') end++;
        return sec.substring(st, end).trim();
    }

    private static String extractArray(String json, String key) {
        int st = json.indexOf(key);
        if (st == -1) return "";
        st += key.length();
        int end = json.indexOf("]", st);
        if (end == -1) return "";
        return json.substring(st, end);
    }

    private static String getWeatherIco(int code) {
        if (code == 0) return "☀";
        if (code == 1 || code == 2) return "🌤";
        if (code == 3) return "☁";
        if (code >= 51 && code <= 67) return "🌧";
        if (code >= 80 && code <= 82) return "🌦";
        if (code >= 95) return "⛈";
        return "☀";
    }

    private static String getWeatherDesc(int code) {
        if (code == 0) return "Clear Field Skies";
        if (code <= 2) return "Partly Cloudy";
        if (code == 3) return "Overcast";
        if (code <= 67) return "Rain Likely";
        if (code <= 82) return "Showers";
        return "Thunderstorm";
    }
}
