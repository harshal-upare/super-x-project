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
        if (cardWagesVal != null) cardWagesVal.setText(OperatorProfileStore.wagesEarned);
        if (cardWagesSub != null) cardWagesSub.setText(OperatorProfileStore.wagesEarnedSub);
    }

    public static void loadOperatorMetricsFromFirestore() {
        new Thread(() -> {
            try {
                String opEmail = OperatorProfileStore.email;
                if (opEmail == null || opEmail.trim().isEmpty()) return;

                // Sync availability status from Operator doc in Firestore
                com.google.cloud.firestore.Firestore db = com.desgin.config.FirestoreConfig.getFirestore();
                if (db != null) {
                    var opDoc = db.collection("Operator").document(opEmail).get().get();
                    if (opDoc.exists()) {
                        Boolean avail = opDoc.getBoolean("available");
                        if (avail != null) {
                            OperatorProfileStore.availableForShifts = avail;
                            OperatorProfileStore.status = avail ? "Available for Field Shifts" : "Not Available / Off-duty";
                        }
                    }
                }

                java.util.List<com.desgin.model.RentalRequestModel> list = new com.desgin.dao.RentalRequestDAO().getRequestsByOperator(opEmail);
                int totalJobs = list.size();
                int activeJobsCount = 0;
                int completedJobsCount = 0;
                int totalWages = 0;
                int settledWages = 0;
                int pendingWages = 0;
                java.util.Set<String> machines = new java.util.HashSet<>();

                for (com.desgin.model.RentalRequestModel r : list) {
                    if (r.getMachineryName() != null) machines.add(r.getMachineryName());
                    String st = r.getStatus() != null ? r.getStatus().toUpperCase() : "";
                    String opSt = r.getOperatorStatus() != null ? r.getOperatorStatus().toUpperCase() : "";
                    String paySt = r.getPaymentStatus() != null ? r.getPaymentStatus().toUpperCase() : "";
                    int wage = r.getOperatorAmount() > 0 ? r.getOperatorAmount() : (600 * Math.max(1, r.getDays()));

                    boolean isCompleted = "COMPLETED".equals(st) || "COMPLETED".equals(opSt);
                    boolean isCancelled = "CANCELLED".equals(st) || "REJECTED".equals(st) || "REJECTED".equals(opSt);
                    boolean isActive = !isCompleted && !isCancelled && ("IN_PROGRESS".equals(st) || "IN_PROGRESS".equals(opSt) || "PAID".equals(paySt) || r.getShiftStartTime() > 0 || "ACTIVE".equals(st) || "CONFIRMED".equals(st));
                    boolean isPending = !isCompleted && !isCancelled && !isActive;

                    if (isCompleted) {
                        completedJobsCount++;
                        totalWages += wage;
                        settledWages += wage;
                    } else if (isActive) {
                        activeJobsCount++;
                        totalWages += wage;
                        pendingWages += wage;
                    } else if (isPending) {
                        totalWages += wage;
                        pendingWages += wage;
                    }
                }

                final int finalTotal = totalJobs;
                final int finalActive = activeJobsCount;
                final int finalCompleted = completedJobsCount;
                final int finalWages = totalWages;
                final int finalSettled = settledWages;
                final int finalPending = pendingWages;
                final int finalMach = machines.size();

                javafx.application.Platform.runLater(() -> {
                    OperatorProfileStore.assignedMachinery = finalMach + " Units Assigned";
                    OperatorProfileStore.assignedMachinerySub = finalActive + " In Shift • " + Math.max(0, finalMach - finalActive) + " Standby";
                    OperatorProfileStore.activeJobs = finalTotal + " Total Jobs";
                    OperatorProfileStore.activeJobsSub = finalCompleted + " Completed • " + finalActive + " Active Shift";
                    OperatorProfileStore.wagesEarned = "₹" + String.format("%,d", finalWages);
                    OperatorProfileStore.wagesEarnedSub = "₹" + String.format("%,d", finalSettled) + " Settled • ₹" + String.format("%,d", finalPending) + " Escrow";
                    refreshDynamicMetrics();
                });
            } catch (Exception ignored) {}
        }).start();
    }

    public static ScrollPane getPage() {
        OperatorProfileManagement.updateHeaderGreeting();
        loadOperatorMetricsFromFirestore();

        // 1. Weather Card
        HBox weatherCard = createWeatherCard("Pune Agri Zone", 18.5204, 73.8567);

        // 2. Full-Width Search Bar
        TextField searchField = new TextField();
        searchField.setPromptText("🔍  Search assigned jobs, machinery, farmer name, or farm plot...");
        searchField.setPrefHeight(44);
        searchField.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(searchField, Priority.ALWAYS);
        searchField.setStyle(
                "-fx-background-color: #FFFFFF;" +
                "-fx-background-radius: 10;" +
                "-fx-border-color: #C2E0CE;" +
                "-fx-border-radius: 10;" +
                "-fx-border-width: 1.2;" +
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 13px;" +
                "-fx-text-fill: #1B4332;" +
                "-fx-padding: 0 16px;" +
                "-fx-prompt-text-fill: #9CA3AF;");

        Button searchButton = new Button("Search Jobs ➔");
        searchButton.setPrefHeight(44);
        searchButton.setStyle(
                "-fx-background-color: #2D6A4F;" +
                "-fx-background-radius: 10;" +
                "-fx-text-fill: white;" +
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 13px;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 0 20px;" +
                "-fx-cursor: hand;");

        searchButton.setOnAction(e -> OperatorLeftSideBar.navigateToJobs());

        HBox searchBox = new HBox(12, searchField, searchButton);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        searchBox.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(searchBox, Priority.ALWAYS);

        // 3. Top Key Metric Cards (Total Jobs, Total Earnings, Availability Status Toggle)
        cardJobsVal = new Text(OperatorProfileStore.activeJobs);
        cardJobsSub = new Text(OperatorProfileStore.activeJobsSub);
        VBox cardTotalJobs = createDynamicDashboardCard("📋", "Total Field Jobs", cardJobsVal, cardJobsSub, "#E65100", () -> {
            OperatorLeftSideBar.navigateToJobs();
        });

        cardWagesVal = new Text(OperatorProfileStore.wagesEarned);
        cardWagesSub = new Text(OperatorProfileStore.wagesEarnedSub);
        VBox cardTotalEarnings = createDynamicDashboardCard("💰", "Total Earnings", cardWagesVal, cardWagesSub, "#15803D", () -> {
            OperatorLeftSideBar.navigateToEarnings();
        });

        VBox cardAvailability = createAvailabilityCard();

        HBox cards = new HBox(18, cardTotalJobs, cardTotalEarnings, cardAvailability);
        cards.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(cardTotalJobs, Priority.ALWAYS);
        HBox.setHgrow(cardTotalEarnings, Priority.ALWAYS);
        HBox.setHgrow(cardAvailability, Priority.ALWAYS);

        // 4. Quick Overview / Assigned Machinery Banner
        VBox quickOverview = createAssignedMachinerySection();

        VBox centerContent = new VBox(
                20,
                weatherCard,
                searchBox,
                cards,
                quickOverview
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

    private static VBox createAvailabilityCard() {
        VBox card = new VBox(8);
        card.setPrefWidth(260);
        card.setMinHeight(125);
        card.setPadding(new Insets(12, 16, 12, 16));

        Runnable updateUI = () -> {
            card.getChildren().clear();
            boolean isAvail = OperatorProfileStore.availableForShifts;

            Text iconText = new Text(isAvail ? "🟢" : "🔴");
            iconText.setStyle("-fx-font-size: 18px;");

            StackPane iconHolder = new StackPane(iconText);
            iconHolder.setPrefSize(36, 36);
            iconHolder.setMinSize(36, 36);
            iconHolder.setMaxSize(36, 36);
            iconHolder.setStyle("-fx-background-color: " + (isAvail ? "#E8F5E9" : "#FEE2E2") + "; -fx-background-radius: 10;");

            Text titleText = new Text("Duty Status");
            titleText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-fill: #4B5563;");

            Text statusText = new Text(isAvail ? "Available for Shifts" : "Not Available (Busy)");
            statusText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 14.5px; -fx-font-weight: bold; -fx-fill: " + (isAvail ? "#15803D" : "#DC2626") + ";");

            VBox titleBox = new VBox(1, titleText, statusText);
            HBox topRow = new HBox(8, iconHolder, titleBox);
            topRow.setAlignment(Pos.CENTER_LEFT);

            Text descText = new Text(isAvail ? "✓ Visible to farmers for hiring & field requests" : "✕ Hidden from search (Farmers cannot hire)");
            descText.setWrappingWidth(240);
            descText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 10.5px; -fx-fill: " + (isAvail ? "#166534" : "#991B1B") + ";");

            Button toggleBtn = new Button(isAvail ? "🔴 Set as Not Available" : "🟢 Set as Available");
            toggleBtn.setMaxWidth(Double.MAX_VALUE);
            toggleBtn.setStyle(
                isAvail
                ? "-fx-background-color: #FEF2F2; -fx-text-fill: #DC2626; -fx-border-color: #FCA5A5; -fx-border-radius: 8px; -fx-background-radius: 8px; -fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 4 8;"
                : "-fx-background-color: #E8F5E9; -fx-text-fill: #15803D; -fx-border-color: #86EFAC; -fx-border-radius: 8px; -fx-background-radius: 8px; -fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 4 8;"
            );

            toggleBtn.setOnAction(e -> {
                OperatorProfileStore.toggleAvailability();
            });

            card.getChildren().addAll(topRow, descText, toggleBtn);

            card.setStyle(
                isAvail
                ? "-fx-background-color: #F0FDF4; -fx-background-radius: 14; -fx-border-color: #86EFAC; -fx-border-width: 1.2; -fx-border-radius: 14; -fx-effect: dropshadow(gaussian, rgba(22, 101, 52, 0.08), 8, 0, 0, 2);"
                : "-fx-background-color: #FEF2F2; -fx-background-radius: 14; -fx-border-color: #FCA5A5; -fx-border-width: 1.2; -fx-border-radius: 14; -fx-effect: dropshadow(gaussian, rgba(185, 28, 28, 0.08), 8, 0, 0, 2);"
            );
        };

        updateUI.run();
        OperatorProfileStore.addProfileListener(() -> Platform.runLater(updateUI));

        return card;
    }

    private static VBox createAssignedMachinerySection() {
        VBox box = new VBox(12);
        box.setPadding(new Insets(18, 20, 18, 20));
        box.setStyle(
                "-fx-background-color: #FFFFFF;" +
                "-fx-background-radius: 16px;" +
                "-fx-border-color: #E2EBE5;" +
                "-fx-border-radius: 16px;" +
                "-fx-border-width: 1px;"
        );

        Text secTitle = new Text("🚜 Field Assignments & Quick Actions");
        secTitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 15px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text secSub = new Text("Instant access to start field shifts, review farmer requests, and view settlements");
        secSub.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-fill: #6B7280;");

        Button openRequestsBtn = new Button("📥 Review Job Requests");
        openRequestsBtn.setStyle("-fx-background-color: linear-gradient(to right, #2D6A4F, #40916C); -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 8px; -fx-cursor: hand; -fx-padding: 7 14;");
        openRequestsBtn.setOnAction(e -> OperatorLeftSideBar.navigateToJobRequests());

        Button openJobsBtn = new Button("🚜 Jobs Schedule");
        openJobsBtn.setStyle("-fx-background-color: #E8F5E9; -fx-text-fill: #1B4332; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 8px; -fx-cursor: hand; -fx-padding: 7 14; -fx-border-color: #A7F3D0; -fx-border-radius: 8px;");
        openJobsBtn.setOnAction(e -> OperatorLeftSideBar.navigateToJobs());

        Button openEarningsBtn = new Button("💳 Payment Details");
        openEarningsBtn.setStyle("-fx-background-color: #F3F4F6; -fx-text-fill: #374151; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 8px; -fx-cursor: hand; -fx-padding: 7 14; -fx-border-color: #E5E7EB; -fx-border-radius: 8px;");
        openEarningsBtn.setOnAction(e -> OperatorLeftSideBar.navigateToEarnings());

        Button openReviewsBtn = new Button("⭐ Farmer Ratings");
        openReviewsBtn.setStyle("-fx-background-color: #FFFBEB; -fx-text-fill: #D97706; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 8px; -fx-cursor: hand; -fx-padding: 7 14; -fx-border-color: #FDE68A; -fx-border-radius: 8px;");
        openReviewsBtn.setOnAction(e -> OperatorLeftSideBar.navigateToReviews());

        HBox btnRow = new HBox(10, openRequestsBtn, openJobsBtn, openEarningsBtn, openReviewsBtn);
        btnRow.setAlignment(Pos.CENTER_LEFT);

        box.getChildren().addAll(new VBox(3, secTitle, secSub), btnRow);
        return box;
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
