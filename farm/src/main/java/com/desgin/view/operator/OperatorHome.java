package com.desgin.view.operator;

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
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class OperatorHome {

    private static boolean isShiftRunning = true;
    private static Text shiftStatusBadge;
    private static Button pauseShiftBtn;

    public static ScrollPane getPage() {
        String operatorName = "Ramesh Chavan";

        Text welcomeText = new Text("Welcome back, " + operatorName + " 👨‍🌾");
        welcomeText.setStyle(
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 26px;" +
                "-fx-font-weight: bold;" +
                "-fx-fill: #4A2C20;");

        Text dashboardText = new Text("Machinery Operator Cockpit & Field Command");
        dashboardText.setStyle(
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 16px;" +
                "-fx-font-weight: bold;" +
                "-fx-fill: #5C4033;");

        Text descriptionText = new Text("Monitor active machine telemetry, field work orders, engine hours & instant wage payouts");
        descriptionText.setStyle(
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 13px;" +
                "-fx-fill: #806A5B;");

        VBox headerText = new VBox(4, welcomeText, dashboardText, descriptionText);
        headerText.setAlignment(Pos.TOP_LEFT);

        // Weather Card
        VBox weatherCard = createWeatherCard("Pune Agri Zone", 18.5204, 73.8567);
        weatherCard.setAlignment(Pos.TOP_RIGHT);

        HBox topRow = new HBox(20, headerText, weatherCard);
        HBox.setHgrow(headerText, Priority.ALWAYS);
        topRow.setAlignment(Pos.CENTER_LEFT);

        // Search Bar & Filter
        TextField searchField = new TextField();
        searchField.setPromptText("Search assigned jobs, machinery, farmer name, or farm plot...");
        searchField.setPrefHeight(42);
        searchField.setPrefWidth(460);
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

        HBox searchBox = new HBox(10, searchIcon, searchField, searchButton);
        searchBox.setAlignment(Pos.CENTER_LEFT);

        // 4 KPI Operator Metric Cards
        VBox cardMachines = createDashboardCard("🚜", "Assigned Machinery", "4 Units", "2 Field Ready • 1 In Shift", "#2E7D32", () -> {
            OperatorLeftSideBar.setActiveButton(OperatorLeftSideBar.machineryBtn, OperatorLeftSideBar.navigationButtons);
            OperatorDashboard.borderPane.setCenter(OperatorMachinery.getMachinerySection(OperatorDashboard.root));
        });

        VBox cardActiveJobs = createDashboardCard("📋", "Active Field Jobs", "2 Assigned", "1 Running • 1 Scheduled", "#E65100", () -> {
            OperatorLeftSideBar.setActiveButton(OperatorLeftSideBar.jobsBtn, OperatorLeftSideBar.navigationButtons);
            OperatorDashboard.borderPane.setCenter(OperatorJobs.getJobsSection(OperatorDashboard.root));
        });

        VBox cardHours = createDashboardCard("⏱", "Engine Hours (Month)", "148.5 hrs", "+18.2 hrs this week ↑", "#1976D2", () -> {
            OperatorLeftSideBar.setActiveButton(OperatorLeftSideBar.fieldLogsBtn, OperatorLeftSideBar.navigationButtons);
            OperatorDashboard.borderPane.setCenter(OperatorFieldLogs.getLogsSection(OperatorDashboard.root));
        });

        VBox cardEarnings = createDashboardCard("💰", "Wages Earned (Month)", "₹28,400", "₹4,200 pending settlement", "#2E7D32", () -> {
            OperatorLeftSideBar.setActiveButton(OperatorLeftSideBar.earningsBtn, OperatorLeftSideBar.navigationButtons);
            OperatorDashboard.borderPane.setCenter(OperatorEarnings.getEarningsSection(OperatorDashboard.root));
        });

        HBox cards = new HBox(15, cardMachines, cardActiveJobs, cardHours, cardEarnings);
        cards.setAlignment(Pos.CENTER_LEFT);

        // Live Shift Cockpit Panel
        VBox liveShiftCard = createLiveCockpitCard();

        // Upcoming Work Orders Section
        VBox scheduleSection = createUpcomingScheduleSection();

        // Daily Safety Checklist
        VBox safetyChecklistCard = createSafetyChecklistCard();

        // Recent Work Logs
        VBox recentLogsSection = createRecentLogsSection();

        VBox centerContent = new VBox(
                22,
                topRow,
                searchBox,
                cards,
                liveShiftCard,
                scheduleSection,
                safetyChecklistCard,
                recentLogsSection
        );
        centerContent.setPadding(new Insets(20, 30, 35, 30));

        ScrollPane scrollPane = new ScrollPane(centerContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        OperatorDashboard.borderPane.setCenter(scrollPane);
        return scrollPane;
    }

    private static VBox createLiveCockpitCard() {
        shiftStatusBadge = new Text("🔴  LIVE SHIFT COCKPIT • RUNNING IN FIELD");
        shiftStatusBadge.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-fill: #2E7D32; -fx-letter-spacing: 1px;");

        Label gpsPill = new Label("● GPS LIVE TRACKING");
        gpsPill.setStyle("-fx-background-color: #E8F5E9; -fx-text-fill: #2E7D32; -fx-font-family: 'Poppins'; -fx-font-size: 10.5px; -fx-font-weight: bold; -fx-padding: 3 8 3 8; -fx-background-radius: 4;");

        HBox badgeRow = new HBox(12, shiftStatusBadge, new Region(), gpsPill);
        HBox.setHgrow(badgeRow.getChildren().get(1), Priority.ALWAYS);
        badgeRow.setAlignment(Pos.CENTER_LEFT);

        Text title = new Text("John Deere 5310 4WD (55 HP) • Heavy Duty Rotavator (7ft)");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 18px; -fx-font-weight: bold; -fx-fill: #4A2C20;");

        Text subInfo = new Text("Client: Balasaheb Shirole  |  Location: Plot B, Sector 4 - Baramati (14.0 Acres)  |  Crop: Sugarcane Seedbed Tillage");
        subInfo.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-fill: #5C4033;");

        // 4 Telemetry Dials
        VBox tel1 = createTelemetryStat("⛽ Fuel Level", "78% (42 Liters Left)", 0.78, "#2E7D32");
        VBox tel2 = createTelemetryStat("⚡ Engine Speed", "1,850 RPM (Optimal)", 0.74, "#2E7D32");
        VBox tel3 = createTelemetryStat("🌡 Coolant Temp", "84°C (Normal)", 0.55, "#8B6F47");
        VBox tel4 = createTelemetryStat("⏱ Shift Duration", "3h 45m (7.5 Acres Done)", 0.65, "#1976D2");

        HBox telRow = new HBox(14, tel1, tel2, tel3, tel4);
        telRow.setAlignment(Pos.CENTER_LEFT);

        // Action Buttons
        pauseShiftBtn = new Button("⏸  Pause Shift");
        pauseShiftBtn.setStyle("-fx-background-color: #E4D3C2; -fx-text-fill: #4A2C20; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 8 16 8 16;");
        pauseShiftBtn.setOnAction(e -> {
            isShiftRunning = !isShiftRunning;
            if (isShiftRunning) {
                pauseShiftBtn.setText("⏸  Pause Shift");
                pauseShiftBtn.setStyle("-fx-background-color: #E4D3C2; -fx-text-fill: #4A2C20; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 8 16 8 16;");
                shiftStatusBadge.setText("🔴  LIVE SHIFT COCKPIT • RUNNING IN FIELD");
                shiftStatusBadge.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-fill: #2E7D32; -fx-letter-spacing: 1px;");
            } else {
                pauseShiftBtn.setText("▶  Resume Shift");
                pauseShiftBtn.setStyle("-fx-background-color: #6B8E23; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 8 16 8 16;");
                shiftStatusBadge.setText("🟡  SHIFT PAUSED • IDLE IN FIELD");
                shiftStatusBadge.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-fill: #D97706; -fx-letter-spacing: 1px;");
            }
        });

        Button completeBtn = new Button("✓  Complete Shift & Submit Log");
        completeBtn.setStyle("-fx-background-color: #2E7D32; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 8 16 8 16;");
        completeBtn.setOnAction(e -> {
            OperatorLeftSideBar.setActiveButton(OperatorLeftSideBar.fieldLogsBtn, OperatorLeftSideBar.navigationButtons);
            OperatorDashboard.borderPane.setCenter(OperatorFieldLogs.getLogsSection(OperatorDashboard.root));
        });

        Button sosBtn = new Button("⚠️  Report Field Breakdown / SOS");
        sosBtn.setStyle("-fx-background-color: #8B3A3A; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 8 16 8 16;");
        sosBtn.setOnAction(e -> {
            OperatorLeftSideBar.setActiveButton(OperatorLeftSideBar.maintenanceBtn, OperatorLeftSideBar.navigationButtons);
            OperatorDashboard.borderPane.setCenter(OperatorMaintenance.getMaintenanceSection(OperatorDashboard.root));
        });

        HBox btnRow = new HBox(12, pauseShiftBtn, completeBtn, sosBtn);
        btnRow.setAlignment(Pos.CENTER_LEFT);

        VBox card = new VBox(14, badgeRow, title, subInfo, telRow, btnRow);
        card.setPadding(new Insets(20));
        card.setStyle(
                "-fx-background-color: #F5EFE6;" +
                "-fx-background-radius: 14;" +
                "-fx-border-color: #2E7D32;" +
                "-fx-border-width: 1.5;" +
                "-fx-border-radius: 14;" +
                "-fx-effect: dropshadow(gaussian, rgba(46,125,50,0.15), 10, 0.2, 0, 4);");

        return card;
    }

    private static VBox createTelemetryStat(String label, String value, double progress, String barColor) {
        Text l = new Text(label);
        l.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-fill: #806A5B;");

        Text v = new Text(value);
        v.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 14px; -fx-font-weight: bold; -fx-fill: #4A2C20;");

        ProgressBar pb = new ProgressBar(progress);
        pb.setPrefWidth(210);
        pb.setPrefHeight(6);
        pb.setStyle("-fx-accent: " + barColor + ";");

        VBox b = new VBox(4, l, v, pb);
        b.setPrefWidth(225);
        b.setPadding(new Insets(10, 12, 10, 12));
        b.setStyle("-fx-background-color: #FFFDF9; -fx-background-radius: 8; -fx-border-color: #D8C7B5; -fx-border-width: 1; -fx-border-radius: 8;");
        return b;
    }

    private static VBox createUpcomingScheduleSection() {
        Text title = new Text("Upcoming Work Orders & Field Schedule");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 18px; -fx-font-weight: bold; -fx-fill: #4A2C20;");

        Text viewAll = new Text("View Full Schedule →");
        viewAll.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-font-weight: bold; -fx-fill: #2E7D32; -fx-cursor: hand;");
        viewAll.setOnMouseClicked(e -> {
            OperatorLeftSideBar.setActiveButton(OperatorLeftSideBar.jobsBtn, OperatorLeftSideBar.navigationButtons);
            OperatorDashboard.borderPane.setCenter(OperatorJobs.getJobsSection(OperatorDashboard.root));
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox header = new HBox(title, spacer, viewAll);
        header.setAlignment(Pos.CENTER_LEFT);

        HBox job1 = createScheduleRow("🌾 Wheat Harvesting (18 Acres)", "Farmer: Vikas More • Preet 987 Harvester", "Tomorrow, 07:00 AM", "₹450 / Acre", "CONFIRMED", "Wheat Crop");
        HBox job2 = createScheduleRow("🚜 Laser Land Leveling (8 Acres)", "Farmer: Kiran Bhosale • Mahindra 575 DI + Laser Unit", "16 Aug 2026, 08:30 AM", "₹700 / Hour", "SCHEDULED", "Cotton Land");
        HBox job3 = createScheduleRow("🚁 Micronutrient Foliar Spraying (12 Acres)", "Farmer: Ganesh Jadhav • Hexacopter Agri-Drone", "17 Aug 2026, 06:00 AM", "₹350 / Acre", "PENDING_ACCEPT", "Soybean Crop");

        VBox list = new VBox(10, header, job1, job2, job3);
        return list;
    }

    private static HBox createScheduleRow(String jobName, String details, String timing, String pay, String status, String cropTag) {
        Text t1 = new Text(jobName);
        t1.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 14px; -fx-font-weight: bold; -fx-fill: #4A2C20;");

        Text t2 = new Text(details);
        t2.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #806A5B;");

        Label cropBadge = new Label(cropTag);
        cropBadge.setStyle("-fx-background-color: #EDE3D5; -fx-text-fill: #5C4033; -fx-font-family: 'Poppins'; -fx-font-size: 10px; -fx-font-weight: bold; -fx-padding: 2 6 2 6; -fx-background-radius: 4;");

        VBox info = new VBox(3, new HBox(8, t1, cropBadge), t2);

        Text timeText = new Text("📅 " + timing);
        timeText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-fill: #5C4033;");

        Text payText = new Text("💰 " + pay);
        payText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-font-weight: bold; -fx-fill: #2E7D32;");

        Text statusBadge = new Text("● " + status);
        statusBadge.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-fill: #8B6F47;");

        Button actionBtn = new Button("View Job ➔");
        actionBtn.setStyle("-fx-background-color: #8B6F47; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand;");
        actionBtn.setOnAction(e -> {
            OperatorLeftSideBar.setActiveButton(OperatorLeftSideBar.jobsBtn, OperatorLeftSideBar.navigationButtons);
            OperatorDashboard.borderPane.setCenter(OperatorJobs.getJobsSection(OperatorDashboard.root));
        });

        Region s1 = new Region();
        HBox.setHgrow(s1, Priority.ALWAYS);

        HBox row = new HBox(20, info, s1, timeText, payText, statusBadge, actionBtn);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(12, 16, 12, 16));
        row.setStyle("-fx-background-color: #F5EFE6; -fx-background-radius: 10; -fx-border-color: #D8C7B5; -fx-border-width: 1; -fx-border-radius: 10;");

        return row;
    }

    private static VBox createSafetyChecklistCard() {
        Text title = new Text("📋 Daily Pre-Operation Safety & Machinery Checklist");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 16px; -fx-font-weight: bold; -fx-fill: #4A2C20;");

        Text sub = new Text("Verify standard safety checklist before initiating field operations.");
        sub.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #806A5B;");

        CheckBox c1 = new CheckBox("Engine Oil, Coolant & Diesel Levels Verified (OK)");
        CheckBox c2 = new CheckBox("Hydraulic Pressure & Hoses Inspected for Leakage (OK)");
        CheckBox c3 = new CheckBox("Tire Pressure & Implement Coupling Linchpin Locked (OK)");
        CheckBox c4 = new CheckBox("Brakes, Safety Indicators & Hazard Flashers Operational (OK)");

        c1.setSelected(true);
        c2.setSelected(true);
        c3.setSelected(true);
        c4.setSelected(true);

        String cbStyle = "-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-text-fill: #4A2C20;";
        c1.setStyle(cbStyle);
        c2.setStyle(cbStyle);
        c3.setStyle(cbStyle);
        c4.setStyle(cbStyle);

        GridPane grid = new GridPane();
        grid.setHgap(30);
        grid.setVgap(10);
        grid.add(c1, 0, 0);
        grid.add(c2, 1, 0);
        grid.add(c3, 0, 1);
        grid.add(c4, 1, 1);

        Button submitChecklist = new Button("✓  Confirm Pre-Shift Safety Verification");
        submitChecklist.setStyle("-fx-background-color: #2E7D32; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 6 16 6 16;");

        VBox card = new VBox(10, title, sub, grid, submitChecklist);
        card.setPadding(new Insets(16));
        card.setStyle("-fx-background-color: #F5EFE6; -fx-background-radius: 12; -fx-border-color: #D8C7B5; -fx-border-width: 1; -fx-border-radius: 12;");

        return card;
    }

    private static VBox createRecentLogsSection() {
        Text title = new Text("Recent Completed Field Logs");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 18px; -fx-font-weight: bold; -fx-fill: #4A2C20;");

        HBox r1 = createLogRow("John Deere 5310 (Plowing)", "13 Aug 2026", "6.5 hrs", "12.0 Acres", "₹2,800 Payout", "VERIFIED");
        HBox r2 = createLogRow("Mahindra 575 DI (Cultivator)", "11 Aug 2026", "4.0 hrs", "8.5 Acres", "₹1,900 Payout", "VERIFIED");
        HBox r3 = createLogRow("Preet 987 (Paddy Harvest)", "09 Aug 2026", "8.0 hrs", "15.0 Acres", "₹6,000 Payout", "VERIFIED");

        return new VBox(10, title, r1, r2, r3);
    }

    private static HBox createLogRow(String machine, String date, String hours, String acres, String wage, String status) {
        Text mText = new Text("🚜 " + machine);
        mText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13.5px; -fx-font-weight: bold; -fx-fill: #4A2C20;");

        Text dText = new Text("📅 " + date);
        dText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #806A5B;");

        Text hText = new Text("⏱ " + hours);
        hText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-font-weight: bold; -fx-fill: #5C4033;");

        Text aText = new Text("🌾 " + acres);
        aText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-font-weight: bold; -fx-fill: #5C4033;");

        Text wText = new Text(wage);
        wText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13.5px; -fx-font-weight: bold; -fx-fill: #2E7D32;");

        Text sText = new Text("✓ " + status);
        sText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-fill: #2E7D32;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox row = new HBox(25, mText, dText, spacer, hText, aText, wText, sText);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(12, 16, 12, 16));
        row.setStyle("-fx-background-color: #F5EFE6; -fx-background-radius: 10; -fx-border-color: #D8C7B5; -fx-border-width: 1; -fx-border-radius: 10;");

        return row;
    }

    private static VBox createDashboardCard(String icon, String title, String value, String subtitle, String color, Runnable onClick) {
        Text iconText = new Text(icon);
        iconText.setStyle("-fx-font-size: 24px;");

        StackPane iconHolder = new StackPane(iconText);
        iconHolder.setPrefSize(42, 42);
        iconHolder.setStyle("-fx-background-color: #E4D3C2; -fx-background-radius: 10;");

        Text titleText = new Text(title);
        titleText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #806A5B;");

        Text valueText = new Text(value);
        valueText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 20px; -fx-font-weight: bold; -fx-fill: #4A2C20;");

        Text subText = new Text(subtitle);
        subText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 10px; -fx-fill: " + color + "; -fx-font-weight: bold;");

        HBox topH = new HBox(10, iconHolder, new VBox(2, titleText, valueText));
        topH.setAlignment(Pos.CENTER_LEFT);

        VBox card = new VBox(6, topH, subText);
        card.setPrefWidth(240);
        card.setPrefHeight(115);
        card.setPadding(new Insets(14));

        String normalStyle = "-fx-background-color: #F5EFE6; -fx-background-radius: 14; -fx-border-color: #D8C7B5; -fx-border-width: 1; -fx-border-radius: 14; -fx-cursor: hand;";
        String hoverStyle = "-fx-background-color: #FFF9F0; -fx-background-radius: 14; -fx-border-color: #8B6F47; -fx-border-width: 1.5; -fx-border-radius: 14; -fx-effect: dropshadow(gaussian, rgba(74,44,32,0.2), 10, 0.2, 0, 4); -fx-cursor: hand;";

        card.setStyle(normalStyle);

        card.setOnMouseEntered(e -> {
            card.setStyle(hoverStyle);
            card.setScaleX(1.02);
            card.setScaleY(1.02);
            card.setTranslateY(-2);
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
        weatherCard.setPrefWidth(430);
        weatherCard.setPrefHeight(125);
        weatherCard.setPadding(new Insets(12, 18, 12, 18));
        weatherCard.setStyle("-fx-background-color: #6F91D5; -fx-background-radius: 18;");

        HBox topBox = new HBox(12);
        Text weatherIcon = new Text("☀");
        weatherIcon.setStyle("-fx-font-size: 38px;");

        VBox currentInfo = new VBox(2);
        Text temperature = new Text("28°C");
        temperature.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 26px; -fx-font-weight: bold; -fx-fill: white;");

        Text condition = new Text("Good for Harvesting & Tillage");
        condition.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-fill: white;");

        Text locationText = new Text("⌖ " + location);
        locationText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 10.5px; -fx-fill: white;");

        currentInfo.getChildren().addAll(temperature, condition, locationText);
        topBox.getChildren().addAll(weatherIcon, currentInfo);

        HBox forecastBox = new HBox(15);
        Text[] days = new Text[4];
        for (int i = 0; i < 4; i++) {
            days[i] = new Text("Loading...");
            days[i].setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 10.5px; -fx-fill: white;");
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
                String url = "https://api.open-meteo.com/v1/forecast"
                        + "?latitude=" + latitude
                        + "&longitude=" + longitude
                        + "&current=temperature_2m,weather_code"
                        + "&daily=weather_code,temperature_2m_max,temperature_2m_min"
                        + "&timezone=auto"
                        + "&forecast_days=4";

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
            condition.setText("Clear field weather");
            weatherIcon.setText("☀");
            days[0].setText("Thu ☀ 31°/22°");
            days[1].setText("Fri 🌤 30°/21°");
            days[2].setText("Sat ⛅ 29°/20°");
            days[3].setText("Sun 🌧 27°/19°");
        }));

        Thread thread = new Thread(weatherTask);
        thread.setDaemon(true);
        thread.start();
    }

    private static void updateWeatherUI(String json, Text weatherIcon, Text temperature, Text condition, Text[] days) {
        try {
            int currentStart = json.indexOf("\"current\":{");
            if (currentStart == -1) return;
            int currentEnd = json.indexOf("}", currentStart);
            String currentData = json.substring(currentStart, currentEnd);

            String currentTemperature = extractNumberFromSection(currentData, "\"temperature_2m\":");
            String currentCode = extractNumberFromSection(currentData, "\"weather_code\":");

            double tempVal = Double.parseDouble(currentTemperature);
            int code = Integer.parseInt(currentCode);

            temperature.setText(Math.round(tempVal) + "°C");
            condition.setText(getWeatherDescription(code));
            weatherIcon.setText(getWeatherIcon(code));

            int dailyStart = json.indexOf("\"daily\":{");
            if (dailyStart == -1) return;
            String dailyData = json.substring(dailyStart);

            String datesPart = extractArray(dailyData, "\"time\":[");
            String maxPart = extractArray(dailyData, "\"temperature_2m_max\":[");
            String minPart = extractArray(dailyData, "\"temperature_2m_min\":[");
            String codePart = extractArray(dailyData, "\"weather_code\":[");

            String[] dateArray = datesPart.split(",");
            String[] maxArray = maxPart.split(",");
            String[] minArray = minPart.split(",");
            String[] codeArray = codePart.split(",");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE", Locale.ENGLISH);

            for (int i = 0; i < 4 && i < days.length && i < dateArray.length; i++) {
                String date = dateArray[i].replace("\"", "").trim();
                LocalDate localDate = LocalDate.parse(date);
                double maxT = Double.parseDouble(maxArray[i].trim());
                double minT = Double.parseDouble(minArray[i].trim());
                int dayCode = Integer.parseInt(codeArray[i].trim());

                String dayName = localDate.format(formatter);
                days[i].setText(dayName + " " + getWeatherIcon(dayCode) + " " + Math.round(maxT) + "°/" + Math.round(minT) + "°");
            }
        } catch (Exception ignored) {}
    }

    private static String extractNumberFromSection(String section, String key) {
        int start = section.indexOf(key);
        if (start == -1) return "0";
        start += key.length();
        int end = start;
        while (end < section.length() && section.charAt(end) != ',' && section.charAt(end) != '}') {
            end++;
        }
        return section.substring(start, end).trim();
    }

    private static String extractArray(String json, String key) {
        int start = json.indexOf(key);
        if (start == -1) return "";
        start += key.length();
        int end = json.indexOf("]", start);
        if (end == -1) return "";
        return json.substring(start, end);
    }

    private static String getWeatherIcon(int code) {
        if (code == 0) return "☀";
        if (code == 1 || code == 2) return "🌤";
        if (code == 3) return "☁";
        if (code >= 45 && code <= 48) return "🌫";
        if (code >= 51 && code <= 67) return "🌧";
        if (code >= 71 && code <= 77) return "❄";
        if (code >= 80 && code <= 82) return "🌦";
        if (code >= 95) return "⛈";
        return "☁";
    }

    private static String getWeatherDescription(int code) {
        if (code == 0) return "Clear Sky • Good Field Work";
        if (code == 1 || code == 2) return "Partly Cloudy • Ideal Operation";
        if (code == 3) return "Overcast • Normal Tillage";
        if (code >= 51 && code <= 67) return "Rain Expected • Caution in Field";
        if (code >= 95) return "Thunderstorm • Cease Machinery Operation";
        return "Normal Operating Conditions";
    }
}
