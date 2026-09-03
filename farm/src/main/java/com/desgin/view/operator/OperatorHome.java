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

    private static Text cardMachinesVal;
    private static Text cardMachinesSub;
    private static Text cardJobsVal;
    private static Text cardJobsSub;
    private static Text cardHoursVal;
    private static Text cardHoursSub;
    private static Text cardWagesVal;
    private static Text cardWagesSub;

    static {
        OperatorProfileStore.addProfileListener(() -> {
            Platform.runLater(OperatorHome::refreshDynamicMetrics);
        });
    }

    public static void refreshDynamicMetrics() {
        if (cardMachinesVal != null) cardMachinesVal.setText(OperatorProfileStore.assignedMachinery);
        if (cardMachinesSub != null) cardMachinesSub.setText(OperatorProfileStore.assignedMachinerySub);
        if (cardJobsVal != null) cardJobsVal.setText(OperatorProfileStore.activeJobs);
        if (cardJobsSub != null) cardJobsSub.setText(OperatorProfileStore.activeJobsSub);
        if (cardHoursVal != null) cardHoursVal.setText(OperatorProfileStore.engineHours);
        if (cardHoursSub != null) cardHoursSub.setText(OperatorProfileStore.engineHoursSub);
        if (cardWagesVal != null) cardWagesVal.setText(OperatorProfileStore.wagesEarned);
        if (cardWagesSub != null) cardWagesSub.setText(OperatorProfileStore.wagesEarnedSub);
    }

    public static void loadOperatorMetricsFromFirestore() {
        new Thread(() -> {
            try {
                String opEmail = OperatorProfileStore.email;
                if (opEmail == null || opEmail.trim().isEmpty()) return;
                java.util.List<com.desgin.model.RentalRequestModel> list = new com.desgin.dao.RentalRequestDAO().getRequestsByOperator(opEmail);
                int totalJobs = list.size();
                int activeJobsCount = 0;
                int completedJobsCount = 0;
                int totalWages = 0;
                java.util.Set<String> machines = new java.util.HashSet<>();

                for (com.desgin.model.RentalRequestModel r : list) {
                    if (r.getMachineryName() != null) machines.add(r.getMachineryName());
                    String st = r.getStatus() != null ? r.getStatus().toUpperCase() : "";
                    int wage = r.getOperatorAmount() > 0 ? r.getOperatorAmount() : (500 * Math.max(1, r.getDays()));
                    if ("ACTIVE".equals(st) || "CONFIRMED".equals(st) || "ACCEPTED".equals(st)) {
                        activeJobsCount++;
                        totalWages += wage;
                    } else if ("COMPLETED".equals(st)) {
                        completedJobsCount++;
                        totalWages += wage;
                    }
                }

                final int finalActive = activeJobsCount;
                final int finalMach = machines.size();
                final int finalWages = totalWages;
                final int finalCompleted = completedJobsCount;

                javafx.application.Platform.runLater(() -> {
                    OperatorProfileStore.assignedMachinery = finalMach + " Units";
                    OperatorProfileStore.assignedMachinerySub = finalActive + " In Shift • " + Math.max(0, finalMach - finalActive) + " Ready";
                    OperatorProfileStore.activeJobs = finalActive + " Active";
                    OperatorProfileStore.activeJobsSub = finalCompleted + " Completed • " + totalJobs + " Total";
                    OperatorProfileStore.wagesEarned = "₹" + String.format("%,d", finalWages);
                    OperatorProfileStore.wagesEarnedSub = "Secured via Platform Escrow";
                    refreshDynamicMetrics();
                });
            } catch (Exception ignored) {}
        }).start();
    }

    public static ScrollPane getPage() {
        OperatorProfileManagement.updateHeaderGreeting();
        loadOperatorMetricsFromFirestore();

        // Weather Card
        HBox weatherCard = createWeatherCard("Pune Agri Zone", 18.5204, 73.8567);

        // Search Bar & Filter
        TextField searchField = new TextField();
        searchField.setPromptText("Search assigned jobs, machinery, farmer name, or farm plot...");
        searchField.setPrefHeight(42);
        searchField.setPrefWidth(460);
        searchField.setStyle(
                "-fx-background-color: #FFFFFF;" +
                "-fx-background-radius: 10;" +
                "-fx-border-color: #E2EBE5;" +
                "-fx-border-radius: 10;" +
                "-fx-border-width: 1;" +
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 13px;" +
                "-fx-text-fill: #1B4332;" +
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

        // 4 Dynamic KPI Operator Metric Cards
        cardMachinesVal = new Text(OperatorProfileStore.assignedMachinery);
        cardMachinesSub = new Text(OperatorProfileStore.assignedMachinerySub);
        VBox cardMachines = createDynamicDashboardCard("🚜", "Assigned Machinery", cardMachinesVal, cardMachinesSub, "#2E7D32", () -> {
            OperatorLeftSideBar.setActiveButton(OperatorLeftSideBar.jobsBtn, OperatorLeftSideBar.navigationButtons);
            OperatorProfileManagement.setHeaderTitle("Field Tasks & Schedule 📋", "Manage active assignments, shift timesheets & field plots");
            OperatorDashboard.borderPane.setCenter(OperatorJobs.getJobsSection(OperatorDashboard.root));
        });

        cardJobsVal = new Text(OperatorProfileStore.activeJobs);
        cardJobsSub = new Text(OperatorProfileStore.activeJobsSub);
        VBox cardActiveJobs = createDynamicDashboardCard("📋", "Active Field Jobs", cardJobsVal, cardJobsSub, "#E65100", () -> {
            OperatorLeftSideBar.setActiveButton(OperatorLeftSideBar.jobsBtn, OperatorLeftSideBar.navigationButtons);
            OperatorProfileManagement.setHeaderTitle("Field Tasks & Schedule 📋", "Manage active assignments, shift timesheets & field plots");
            OperatorDashboard.borderPane.setCenter(OperatorJobs.getJobsSection(OperatorDashboard.root));
        });

        cardHoursVal = new Text(OperatorProfileStore.engineHours);
        cardHoursSub = new Text(OperatorProfileStore.engineHoursSub);
        VBox cardHours = createDynamicDashboardCard("⏱", "Engine Hours (Month)", cardHoursVal, cardHoursSub, "#1976D2", () -> {
            OperatorLeftSideBar.setActiveButton(OperatorLeftSideBar.jobsBtn, OperatorLeftSideBar.navigationButtons);
            OperatorProfileManagement.setHeaderTitle("Field Tasks & Schedule 📋", "Manage active assignments, shift timesheets & field plots");
            OperatorDashboard.borderPane.setCenter(OperatorJobs.getJobsSection(OperatorDashboard.root));
        });

        cardWagesVal = new Text(OperatorProfileStore.wagesEarned);
        cardWagesSub = new Text(OperatorProfileStore.wagesEarnedSub);
        VBox cardEarnings = createDynamicDashboardCard("💰", "Wages Earned (Month)", cardWagesVal, cardWagesSub, "#2E7D32", () -> {
            OperatorLeftSideBar.setActiveButton(OperatorLeftSideBar.earningsBtn, OperatorLeftSideBar.navigationButtons);
            OperatorProfileManagement.setHeaderTitle("Daily Wages & Cashout 💵", "Track completed job settlements, incentives & bank withdrawals");
            OperatorDashboard.borderPane.setCenter(OperatorEarnings.getEarningsSection(OperatorDashboard.root));
        });

        HBox cards = new HBox(15, cardMachines, cardActiveJobs, cardHours, cardEarnings);
        cards.setAlignment(Pos.CENTER_LEFT);

        // Upcoming Work Orders Section
        VBox scheduleSection = createUpcomingScheduleSection();

        // Recent Work Logs
        VBox recentLogsSection = createRecentLogsSection();

        VBox centerContent = new VBox(
                20,
                weatherCard,
                searchBox,
                cards,
                scheduleSection,
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

    private static VBox createUpcomingScheduleSection() {
        Text title = new Text("Upcoming Work Orders & Field Schedule");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 18px; -fx-font-weight: bold; -fx-fill: #1B4332;");

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

        HBox job1 = createScheduleRow("🌾 Wheat Harvesting (18.0 Acres)", "Farmer: Vikas More (📞 +91 98502 11234) • Gat No. 112, Daund Road, Pune", "Tomorrow, 07:00 AM", "₹450 / Acre (₹8,100)", "CONFIRMED", "Wheat Crop");
        HBox job2 = createScheduleRow("🚜 Laser Land Leveling (8.0 Acres)", "Farmer: Kiran Bhosale (📞 +91 94220 89761) • Shiraswadi Farm, Baramati", "16 Aug 2026, 08:30 AM", "₹700 / Hour (₹4,200)", "SCHEDULED", "Cotton Land");
        HBox job3 = createScheduleRow("🚁 Micronutrient Foliar Spraying (12.0 Acres)", "Farmer: Ganesh Jadhav (📞 +91 97631 55670) • Hol Village, Baramati", "17 Aug 2026, 06:00 AM", "₹350 / Acre (₹4,200)", "PENDING_ACCEPT", "Soybean Crop");

        VBox list = new VBox(10, header, job1, job2, job3);
        return list;
    }

    private static HBox createScheduleRow(String jobName, String details, String timing, String pay, String status, String cropTag) {
        Text t1 = new Text(jobName);
        t1.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 14px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text t2 = new Text(details);
        t2.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #4B5563;");

        Label cropBadge = new Label(cropTag);
        cropBadge.setStyle("-fx-background-color: #F4F9F4; -fx-text-fill: #374151; -fx-font-family: 'Poppins'; -fx-font-size: 10px; -fx-font-weight: bold; -fx-padding: 2 6 2 6; -fx-background-radius: 4;");

        VBox info = new VBox(3, new HBox(8, t1, cropBadge), t2);

        Text timeText = new Text("📅 " + timing);
        timeText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-fill: #374151;");

        Text payText = new Text("💰 " + pay);
        payText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-font-weight: bold; -fx-fill: #2E7D32;");

        Text statusBadge = new Text("● " + status);
        statusBadge.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-fill: #2D6A4F;");

        Button actionBtn = new Button("View Job ➔");
        actionBtn.setStyle("-fx-background-color: #2D6A4F; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand;");
        actionBtn.setOnAction(e -> {
            OperatorLeftSideBar.setActiveButton(OperatorLeftSideBar.jobsBtn, OperatorLeftSideBar.navigationButtons);
            OperatorDashboard.borderPane.setCenter(OperatorJobs.getJobsSection(OperatorDashboard.root));
        });

        Region s1 = new Region();
        HBox.setHgrow(s1, Priority.ALWAYS);

        HBox row = new HBox(20, info, s1, timeText, payText, statusBadge, actionBtn);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(12, 16, 12, 16));
        row.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 10; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 10;");

        return row;
    }

    private static VBox createSafetyChecklistCard() {
        Text title = new Text("📋 Daily Pre-Operation Safety & Machinery Checklist");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 16px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text sub = new Text("Verify standard safety checklist before initiating field operations.");
        sub.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #4B5563;");

        CheckBox c1 = new CheckBox("Engine Oil, Coolant & Diesel Levels Verified (OK)");
        CheckBox c2 = new CheckBox("Hydraulic Pressure & Hoses Inspected for Leakage (OK)");
        CheckBox c3 = new CheckBox("Tire Pressure & Implement Coupling Linchpin Locked (OK)");
        CheckBox c4 = new CheckBox("Brakes, Safety Indicators & Hazard Flashers Operational (OK)");

        c1.setSelected(true);
        c2.setSelected(true);
        c3.setSelected(true);
        c4.setSelected(true);

        String cbStyle = "-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-text-fill: #1B4332;";
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
        card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 12; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 12;");

        return card;
    }

    private static VBox createRecentLogsSection() {
        Text title = new Text("Recent Completed Field Logs");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 18px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        HBox r1 = createLogRow("John Deere 5310 (Plowing)", "13 Aug 2026", "6.5 hrs", "12.0 Acres", "₹2,800 Payout", "VERIFIED");
        HBox r2 = createLogRow("Mahindra 575 DI (Cultivator)", "11 Aug 2026", "4.0 hrs", "8.5 Acres", "₹1,900 Payout", "VERIFIED");
        HBox r3 = createLogRow("Preet 987 (Paddy Harvest)", "09 Aug 2026", "8.0 hrs", "15.0 Acres", "₹6,000 Payout", "VERIFIED");

        return new VBox(10, title, r1, r2, r3);
    }

    private static HBox createLogRow(String machine, String date, String hours, String acres, String wage, String status) {
        Text mText = new Text("🚜 " + machine);
        mText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13.5px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text dText = new Text("📅 " + date);
        dText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #4B5563;");

        Text hText = new Text("⏱ " + hours);
        hText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-font-weight: bold; -fx-fill: #374151;");

        Text aText = new Text("🌾 " + acres);
        aText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-font-weight: bold; -fx-fill: #374151;");

        Text wText = new Text(wage);
        wText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13.5px; -fx-font-weight: bold; -fx-fill: #2E7D32;");

        Text sText = new Text("✓ " + status);
        sText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-fill: #2E7D32;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox row = new HBox(25, mText, dText, spacer, hText, aText, wText, sText);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(12, 16, 12, 16));
        row.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 10; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 10;");

        return row;
    }

    private static VBox createDynamicDashboardCard(String icon, String title, Text valueText, Text subText, String color, Runnable onClick) {
        Text iconText = new Text(icon);
        iconText.setStyle("-fx-font-size: 24px;");

        StackPane iconHolder = new StackPane(iconText);
        iconHolder.setPrefSize(42, 42);
        iconHolder.setStyle("-fx-background-color: #E8F5E9; -fx-background-radius: 10;");

        Text titleText = new Text(title);
        titleText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #4B5563;");

        valueText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 20px; -fx-font-weight: bold; -fx-fill: #1B4332;");
        subText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 10px; -fx-fill: " + color + "; -fx-font-weight: bold;");

        HBox topH = new HBox(10, iconHolder, new VBox(2, titleText, valueText));
        topH.setAlignment(Pos.CENTER_LEFT);

        VBox card = new VBox(6, topH, subText);
        card.setPrefWidth(240);
        card.setPrefHeight(115);
        card.setPadding(new Insets(14));

        String normalStyle = "-fx-background-color: #FFFFFF; -fx-background-radius: 14; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 14; -fx-cursor: hand;";
        String hoverStyle = "-fx-background-color: #F0FDF4; -fx-background-radius: 14; -fx-border-color: #2D6A4F; -fx-border-width: 1.5; -fx-border-radius: 14; -fx-effect: dropshadow(gaussian, rgba(27,67,50,0.2), 10, 0.2, 0, 4); -fx-cursor: hand;";

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

    private static VBox createDashboardCard(String icon, String title, String value, String subtitle, String color, Runnable onClick) {
        Text v = new Text(value);
        Text s = new Text(subtitle);
        return createDynamicDashboardCard(icon, title, v, s, color, onClick);
    }

    private static HBox createWeatherCard(
            String location,
            double latitude,
            double longitude) {

        HBox weatherCard = new HBox(20);
        weatherCard.setAlignment(Pos.CENTER_LEFT);
        weatherCard.setPadding(new Insets(14, 22, 14, 22));
        weatherCard.setMaxWidth(Double.MAX_VALUE);
        weatherCard.setStyle(
                "-fx-background-color: linear-gradient(to right, #1B5E20, #2E7D32, #388E3C);" +
                "-fx-background-radius: 14px;" +
                "-fx-effect: dropshadow(gaussian, rgba(27, 94, 32, 0.22), 12, 0.15, 0, 4);");

        // Left: Current Weather Icon, Temperature & Details
        Text weatherIcon = new Text("☀");
        weatherIcon.setStyle("-fx-font-size: 34px; -fx-fill: #FFF9C4;");

        Text temperature = new Text("Loading...");
        temperature.setStyle(
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 26px;" +
                "-fx-font-weight: bold;" +
                "-fx-fill: white;");

        Text condition = new Text("Loading weather...");
        condition.setStyle(
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 13px;" +
                "-fx-font-weight: bold;" +
                "-fx-fill: #E8F5E9;");

        Text locationText = new Text("📍 " + location + ", Maharashtra");
        locationText.setStyle(
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 11px;" +
                "-fx-fill: #C8E6C9;");

        VBox currentDetails = new VBox(2, condition, locationText);
        currentDetails.setAlignment(Pos.CENTER_LEFT);

        HBox leftCurrentBox = new HBox(12, weatherIcon, temperature, currentDetails);
        leftCurrentBox.setAlignment(Pos.CENTER_LEFT);

        // Middle: Field operations advisory badge
        Text agriTip = new Text("🌱 Field Status: Favorable conditions for farming operations");
        agriTip.setStyle(
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 11.5px;" +
                "-fx-font-weight: 500;" +
                "-fx-fill: #E8F5E9;");
        HBox agriChip = new HBox(agriTip);
        agriChip.setAlignment(Pos.CENTER);
        agriChip.setPadding(new Insets(6, 14, 6, 14));
        agriChip.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.15);" +
                "-fx-background-radius: 20px;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Right: 4-Day Forecast in individual mini column cards
        HBox forecastBox = new HBox(10);
        forecastBox.setAlignment(Pos.CENTER_RIGHT);

        Text[] dayNames = new Text[4];
        Text[] dayIcons = new Text[4];
        Text[] dayTemps = new Text[4];

        for (int i = 0; i < 4; i++) {
            dayNames[i] = new Text("Day");
            dayNames[i].setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-fill: #E8F5E9;");

            dayIcons[i] = new Text("☀");
            dayIcons[i].setStyle("-fx-font-size: 13px; -fx-fill: #FFF9C4;");

            dayTemps[i] = new Text("--° / --°");
            dayTemps[i].setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-fill: white;");

            HBox dayHeader = new HBox(4, dayNames[i], dayIcons[i]);
            dayHeader.setAlignment(Pos.CENTER);

            VBox dayCard = new VBox(2, dayHeader, dayTemps[i]);
            dayCard.setAlignment(Pos.CENTER);
            dayCard.setPadding(new Insets(4, 10, 4, 10));
            dayCard.setStyle("-fx-background-color: rgba(255, 255, 255, 0.12); -fx-background-radius: 8px;");

            forecastBox.getChildren().add(dayCard);
        }

        weatherCard.getChildren().addAll(leftCurrentBox, agriChip, spacer, forecastBox);

        // Load live weather
        loadWeather(
                latitude,
                longitude,
                weatherIcon,
                temperature,
                condition,
                dayNames,
                dayIcons,
                dayTemps);

        return weatherCard;
    }

    private static void loadWeather(
            double latitude,
            double longitude,
            Text weatherIcon,
            Text temperature,
            Text condition,
            Text[] dayNames,
            Text[] dayIcons,
            Text[] dayTemps) {

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
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .GET()
                        .build();

                HttpResponse<String> response = client.send(
                        request,
                        HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() != 200) {
                    throw new RuntimeException("API returned HTTP " + response.statusCode());
                }

                return response.body();
            }
        };

        weatherTask.setOnSucceeded(e -> {
            updateWeatherUI(
                    weatherTask.getValue(),
                    weatherIcon,
                    temperature,
                    condition,
                    dayNames,
                    dayIcons,
                    dayTemps);
        });

        weatherTask.setOnFailed(e -> {
            Platform.runLater(() -> {
                temperature.setText("--°C");
                condition.setText("Weather unavailable");
                weatherIcon.setText("☀");
                for (int i = 0; i < 4 && i < dayNames.length; i++) {
                    dayNames[i].setText("Day");
                    dayIcons[i].setText("☀");
                    dayTemps[i].setText("--°");
                }
            });
        });

        Thread thread = new Thread(weatherTask);
        thread.setDaemon(true);
        thread.start();
    }

    private static void updateWeatherUI(
            String json,
            Text weatherIcon,
            Text temperature,
            Text condition,
            Text[] dayNames,
            Text[] dayIcons,
            Text[] dayTemps) {

        try {
            // ---------------- CURRENT WEATHER ----------------
            int currentStart = json.indexOf("\"current\":{");
            if (currentStart == -1) {
                throw new RuntimeException("Current weather data not found");
            }

            int currentEnd = json.indexOf("}", currentStart);
            String currentData = json.substring(currentStart, currentEnd);

            String currentTemperature = extractNumberFromSection(currentData, "\"temperature_2m\":");
            String currentCode = extractNumberFromSection(currentData, "\"weather_code\":");

            double temperatureValue = Double.parseDouble(currentTemperature);
            int weatherCode = Integer.parseInt(currentCode);

            temperature.setText(Math.round(temperatureValue) + "°C");
            condition.setText(getWeatherDescription(weatherCode));
            weatherIcon.setText(getWeatherIcon(weatherCode));

            // ---------------- DAILY WEATHER ----------------
            int dailyStart = json.indexOf("\"daily\":{");
            if (dailyStart == -1) {
                return;
            }

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

            for (int i = 0; i < 4 && i < dayNames.length; i++) {
                String date = dateArray[i].replace("\"", "").trim();
                LocalDate localDate = LocalDate.parse(date);

                double maxTemperature = Double.parseDouble(maxArray[i].trim());
                double minTemperature = Double.parseDouble(minArray[i].trim());
                int dailyWeatherCode = Integer.parseInt(codeArray[i].trim());

                String dayName = localDate.format(formatter);
                String max = Math.round(maxTemperature) + "°";
                String min = Math.round(minTemperature) + "°";

                dayNames[i].setText(dayName);
                dayIcons[i].setText(getWeatherIcon(dailyWeatherCode));
                dayTemps[i].setText(max + "/" + min);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            Platform.runLater(() -> {
                temperature.setText("--°C");
                condition.setText("Unable to load weather");
            });
        }
    }

    private static String extractNumberFromSection(String section, String key) {
        int start = section.indexOf(key);
        if (start == -1) {
            throw new RuntimeException("Key not found: " + key);
        }
        start += key.length();
        int end = start;
        while (end < section.length() && section.charAt(end) != ',' && section.charAt(end) != '}') {
            end++;
        }
        return section.substring(start, end).trim();
    }

    private static String extractArray(String json, String key) {
        int start = json.indexOf(key);
        if (start == -1) {
            return "";
        }
        start += key.length();
        int end = json.indexOf("]", start);
        if (end == -1) {
            return "";
        }
        return json.substring(start, end);
    }

    private static String getWeatherIcon(int code) {
        if (code == 0) {
            return "☀";
        }
        if (code == 1 || code == 2) {
            return "🌤";
        }
        if (code == 3) {
            return "☁";
        }
        if (code >= 45 && code <= 48) {
            return "🌫";
        }
        if (code >= 51 && code <= 67) {
            return "🌧";
        }
        if (code >= 71 && code <= 77) {
            return "❄";
        }
        if (code >= 80 && code <= 82) {
            return "🌦";
        }
        if (code >= 95 && code <= 99) {
            return "⛈";
        }
        return "☁";
    }

    private static String getWeatherDescription(int code) {
        if (code == 0) {
            return "Clear sky";
        }
        if (code == 1 || code == 2) {
            return "Partly cloudy";
        }
        if (code == 3) {
            return "Cloudy";
        }
        if (code >= 45 && code <= 48) {
            return "Foggy";
        }
        if (code >= 51 && code <= 67) {
            return "Rain likely";
        }
        if (code >= 71 && code <= 77) {
            return "Snow";
        }
        if (code >= 80 && code <= 82) {
            return "Rain showers";
        }
        if (code >= 95 && code <= 99) {
            return "Thunderstorm";
        }
        return "Unknown";
    }
}
