package com.desgin.view.provider;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import org.json.JSONObject;

import com.desgin.dao.MachineryDAO;
import com.desgin.dao.RentalRequestDAO;
import com.desgin.model.MachineryModel;
import com.desgin.model.RentalRequestModel;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class Dashboard {

    private static VBox dynamicRequestsContainer;
    private static VBox dynamicFleetContainer;
    private static Text totalFleetCountText;
    private static Text availFleetCountText;
    private static Text pendingRequestsCountText;
    private static Text activeRentalsCountText;

    public static ScrollPane getPage() {
        String operatingTown = ProviderProfileStore.town != null ? ProviderProfileStore.town : "Pune";

        // Full-width Horizontal Weather Card (Styled identical to Farmer Dashboard)
        HBox weatherCard = createHorizontalWeatherCard(operatingTown, 18.5204, 73.8567);

        // 4 Elevated KPI Summary Cards
        totalFleetCountText = new Text("...");
        totalFleetCountText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 22px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        availFleetCountText = new Text("...");
        availFleetCountText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 22px; -fx-font-weight: bold; -fx-fill: #2E7D32;");

        pendingRequestsCountText = new Text("...");
        pendingRequestsCountText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 22px; -fx-font-weight: bold; -fx-fill: #E65100;");

        activeRentalsCountText = new Text("...");
        activeRentalsCountText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 22px; -fx-font-weight: bold; -fx-fill: #1976D2;");

        VBox fleetKpiCard = createKpiBox("🚜", "Total Fleet Listed", totalFleetCountText, "In Inventory", () -> {
            ProviderLeftSideBar.setActiveButton(ProviderLeftSideBar.fleetBtn, ProviderLeftSideBar.navigationButtons);
            ProviderDashboard.borderPane.setCenter(MyEquipment.getFleetSection(ProviderDashboard.root));
        });

        VBox availKpiCard = createKpiBox("✔", "Available Ready", availFleetCountText, "Ready to Deploy", () -> {
            ProviderLeftSideBar.setActiveButton(ProviderLeftSideBar.fleetBtn, ProviderLeftSideBar.navigationButtons);
            ProviderDashboard.borderPane.setCenter(MyEquipment.getFleetSection(ProviderDashboard.root));
        });

        VBox pendingKpiCard = createKpiBox("📥", "Pending Requests", pendingRequestsCountText, "Farmer Bookings", () -> {
            ProviderLeftSideBar.setActiveButton(ProviderLeftSideBar.rentalRequestsBtn, ProviderLeftSideBar.navigationButtons);
            ProviderDashboard.borderPane.setCenter(RentalRequests.getRequestsSection(ProviderDashboard.root));
        });

        VBox activeKpiCard = createKpiBox("⏱", "Active on Field", activeRentalsCountText, "Ongoing Jobs", () -> {
            ProviderLeftSideBar.setActiveButton(ProviderLeftSideBar.rentalRequestsBtn, ProviderLeftSideBar.navigationButtons);
            ProviderDashboard.borderPane.setCenter(RentalRequests.getRequestsSection(ProviderDashboard.root));
        });

        HBox kpiCards = new HBox(16, fleetKpiCard, availKpiCard, pendingKpiCard, activeKpiCard);
        kpiCards.setAlignment(Pos.CENTER_LEFT);

        // Quick Action Command Toolbar
        HBox quickActions = createQuickActionToolbar();

        // Section 1: Real Pending Requests
        dynamicRequestsContainer = new VBox(12);
        VBox pendingSection = createDynamicPendingSection();

        // Section 2: Real Machinery Inventory Highlights
        dynamicFleetContainer = new VBox(12);
        VBox fleetSection = createDynamicFleetSection();

        // Load DB data
        loadDashboardDataFromFirestore();

        VBox centerContent = new VBox(22,
                weatherCard,
                kpiCards,
                quickActions,
                pendingSection,
                fleetSection
        );
        centerContent.setPadding(new Insets(15, 30, 35, 30));

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(centerContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        ProviderDashboard.borderPane.setCenter(scrollPane);
        return scrollPane;
    }

    private static VBox createKpiBox(String icon, String title, Text valText, String sub, Runnable action) {
        Text ic = new Text(icon);
        ic.setStyle("-fx-font-size: 20px;");

        Text t = new Text(title);
        t.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #4B5563; -fx-font-weight: 500;");

        HBox topH = new HBox(8, ic, t);
        topH.setAlignment(Pos.CENTER_LEFT);

        Text s = new Text(sub);
        s.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-fill: #9CA3AF;");

        VBox box = new VBox(6, topH, valText, s);
        box.setPrefWidth(240);
        box.setPadding(new Insets(16));
        box.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 12; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 12; -fx-cursor: hand;");

        box.setOnMouseEntered(e -> {
            box.setStyle("-fx-background-color: #F0FDF4; -fx-background-radius: 12; -fx-border-color: #2D6A4F; -fx-border-width: 1.5; -fx-border-radius: 12; -fx-effect: dropshadow(gaussian, rgba(45, 106, 79, 0.15), 10, 0, 0, 3);");
            box.setTranslateY(-2);
        });
        box.setOnMouseExited(e -> {
            box.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 12; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 12;");
            box.setTranslateY(0);
        });
        if (action != null) {
            box.setOnMouseClicked(e -> action.run());
        }
        return box;
    }

    private static HBox createQuickActionToolbar() {
        Button addEqBtn = createActionButton("➕  Add New Machinery", "#2E7D32", () -> {
            ProviderLeftSideBar.setActiveButton(ProviderLeftSideBar.fleetBtn, ProviderLeftSideBar.navigationButtons);
            ProviderDashboard.borderPane.setCenter(MyEquipment.getFleetSection(ProviderDashboard.root));
        });

        Button reviewBtn = createActionButton("📥  View Rental Requests", "#2D6A4F", () -> {
            ProviderLeftSideBar.setActiveButton(ProviderLeftSideBar.rentalRequestsBtn, ProviderLeftSideBar.navigationButtons);
            ProviderDashboard.borderPane.setCenter(RentalRequests.getRequestsSection(ProviderDashboard.root));
        });

        Button refreshBtn = createActionButton("🔄  Refresh Data", "#374151", () -> {
            loadDashboardDataFromFirestore();
        });

        HBox bar = new HBox(12, addEqBtn, reviewBtn, refreshBtn);
        bar.setAlignment(Pos.CENTER_LEFT);
        return bar;
    }

    private static Button createActionButton(String text, String bg, Runnable action) {
        Button btn = new Button(text);
        btn.setPrefHeight(38);
        btn.setStyle("-fx-background-color: " + bg + "; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 0 18;");
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: derive(" + bg + ", -15%); -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 0 18;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: " + bg + "; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 0 18;"));
        if (action != null) {
            btn.setOnAction(e -> action.run());
        }
        return btn;
    }

    private static VBox createDynamicPendingSection() {
        Text sectionTitle = new Text("Incoming Rental Requests");
        sectionTitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 18px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text viewAll = new Text("Open All Requests →");
        viewAll.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-font-weight: bold; -fx-fill: #2E7D32; -fx-cursor: hand;");
        viewAll.setOnMouseClicked(e -> {
            ProviderLeftSideBar.setActiveButton(ProviderLeftSideBar.rentalRequestsBtn, ProviderLeftSideBar.navigationButtons);
            ProviderDashboard.borderPane.setCenter(RentalRequests.getRequestsSection(ProviderDashboard.root));
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox header = new HBox(sectionTitle, spacer, viewAll);
        header.setAlignment(Pos.CENTER_LEFT);

        return new VBox(12, header, dynamicRequestsContainer);
    }

    private static VBox createDynamicFleetSection() {
        Text sectionTitle = new Text("Active Machinery Inventory");
        sectionTitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 18px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text viewAll = new Text("Manage Fleet →");
        viewAll.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-font-weight: bold; -fx-fill: #2E7D32; -fx-cursor: hand;");
        viewAll.setOnMouseClicked(e -> {
            ProviderLeftSideBar.setActiveButton(ProviderLeftSideBar.fleetBtn, ProviderLeftSideBar.navigationButtons);
            ProviderDashboard.borderPane.setCenter(MyEquipment.getFleetSection(ProviderDashboard.root));
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox header = new HBox(sectionTitle, spacer, viewAll);
        header.setAlignment(Pos.CENTER_LEFT);

        return new VBox(12, header, dynamicFleetContainer);
    }

    private static void loadDashboardDataFromFirestore() {
        Thread bg = new Thread(() -> {
            try {
                MachineryDAO mDao = new MachineryDAO();
                RentalRequestDAO rDao = new RentalRequestDAO();

                String email = ProviderProfileStore.email;
                List<MachineryModel> machineryList = mDao.getMachineryByProvider(email);
                if (machineryList.isEmpty()) {
                    machineryList = mDao.getAllMachinery();
                }

                List<RentalRequestModel> requests = rDao.getRequestsByProvider(email);
                if (requests.isEmpty()) {
                    requests = rDao.getAllRequests();
                }

                int totalMachinery = machineryList.size();
                long availCount = machineryList.stream().filter(m -> "AVAILABLE".equalsIgnoreCase(m.getStatus())).count();
                long pendingCount = requests.stream().filter(r -> "PENDING".equalsIgnoreCase(r.getStatus())).count();
                long activeCount = requests.stream().filter(r -> "APPROVED".equalsIgnoreCase(r.getStatus()) || "ACTIVE".equalsIgnoreCase(r.getStatus())).count();

                List<MachineryModel> finalMachinery = machineryList;
                List<RentalRequestModel> finalRequests = requests;

                Platform.runLater(() -> {
                    if (totalFleetCountText != null) totalFleetCountText.setText(totalMachinery + " Units");
                    if (availFleetCountText != null) availFleetCountText.setText(availCount + " Units");
                    if (pendingRequestsCountText != null) pendingRequestsCountText.setText(pendingCount + " New");
                    if (activeRentalsCountText != null) activeRentalsCountText.setText(activeCount + " Active");

                    renderPendingRequestsUI(finalRequests);
                    renderFleetOverviewUI(finalMachinery);
                });

            } catch (Exception ignored) {}
        });
        bg.setDaemon(true);
        bg.start();
    }

    private static void renderPendingRequestsUI(List<RentalRequestModel> allRequests) {
        if (dynamicRequestsContainer == null) return;
        dynamicRequestsContainer.getChildren().clear();

        List<RentalRequestModel> pending = allRequests.stream()
                .filter(r -> "PENDING".equalsIgnoreCase(r.getStatus()))
                .limit(3)
                .toList();

        if (pending.isEmpty()) {
            VBox emptyBox = new VBox(6);
            emptyBox.setAlignment(Pos.CENTER_LEFT);
            emptyBox.setPadding(new Insets(16, 18, 16, 18));
            emptyBox.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 10; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 10;");
            Text t = new Text("✓ All caught up! No pending rental requests at this moment.");
            t.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13.5px; -fx-fill: #2E7D32; -fx-font-weight: 500;");
            emptyBox.getChildren().add(t);
            dynamicRequestsContainer.getChildren().add(emptyBox);
            return;
        }

        HBox row = new HBox(15);
        row.setAlignment(Pos.CENTER_LEFT);

        for (RentalRequestModel r : pending) {
            VBox card = new VBox(6);
            card.setPrefWidth(320);
            card.setPadding(new Insets(14));
            card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 10; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 10;");

            Text fName = new Text("👨‍🌾 " + (r.getFarmerName() != null ? r.getFarmerName() : "Farmer"));
            fName.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 14px; -fx-font-weight: bold; -fx-fill: #1B4332;");

            Text loc = new Text("📍 " + (r.getFarmerLocation() != null ? r.getFarmerLocation() : "Local Farm"));
            loc.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-fill: #2D6A4F;");

            Text mName = new Text("🚜 " + (r.getMachineryName() != null ? r.getMachineryName() : "Machinery"));
            mName.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-font-weight: 600; -fx-fill: #374151;");

            int total = r.getTotalAmount() > 0 ? r.getTotalAmount() : (r.getDailyRate() * Math.max(1, r.getDays()));
            Text fare = new Text("Total Rent: ₹" + String.format("%,d", total) + " (" + r.getDays() + " days)");
            fare.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #4B5563;");

            Button actionBtn = new Button("Review Request →");
            actionBtn.setStyle("-fx-background-color: #2E7D32; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand;");
            actionBtn.setOnAction(e -> {
                ProviderLeftSideBar.setActiveButton(ProviderLeftSideBar.rentalRequestsBtn, ProviderLeftSideBar.navigationButtons);
                ProviderDashboard.borderPane.setCenter(RentalRequests.getRequestsSection(ProviderDashboard.root));
            });

            card.getChildren().addAll(fName, loc, mName, fare, actionBtn);
            row.getChildren().add(card);
        }

        dynamicRequestsContainer.getChildren().add(row);
    }

    private static void renderFleetOverviewUI(List<MachineryModel> machineryList) {
        if (dynamicFleetContainer == null) return;
        dynamicFleetContainer.getChildren().clear();

        if (machineryList.isEmpty()) {
            VBox emptyBox = new VBox(6);
            emptyBox.setAlignment(Pos.CENTER_LEFT);
            emptyBox.setPadding(new Insets(16, 18, 16, 18));
            emptyBox.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 10; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 10;");
            Text t = new Text("No machinery listed yet. Click 'Add New Machinery' above to register your fleet.");
            t.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-fill: #4B5563;");
            emptyBox.getChildren().add(t);
            dynamicFleetContainer.getChildren().add(emptyBox);
            return;
        }

        HBox row = new HBox(15);
        row.setAlignment(Pos.CENTER_LEFT);

        List<MachineryModel> preview = machineryList.stream().limit(4).toList();
        for (MachineryModel m : preview) {
            VBox card = new VBox(6);
            card.setPrefWidth(240);
            card.setPadding(new Insets(14));
            card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 10; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 10;");

            Text name = new Text("🚜 " + m.getName());
            name.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13.5px; -fx-font-weight: bold; -fx-fill: #1B4332;");

            Text loc = new Text("📍 " + (m.getLocation() != null ? m.getLocation() : "Pune"));
            loc.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-fill: #4B5563;");

            Text rate = new Text("₹" + m.getPricePerDay() + " / day");
            rate.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 14px; -fx-font-weight: bold; -fx-fill: #2E7D32;");

            String st = m.getStatus() != null ? m.getStatus() : "AVAILABLE";
            Label stBadge = new Label(st);
            String bg = "AVAILABLE".equalsIgnoreCase(st) ? "#E8F5E9" : "#FFF3E0";
            String fg = "AVAILABLE".equalsIgnoreCase(st) ? "#2E7D32" : "#E65100";
            stBadge.setStyle("-fx-background-color: " + bg + "; -fx-text-fill: " + fg + "; -fx-font-family: 'Poppins'; -fx-font-size: 10px; -fx-font-weight: bold; -fx-padding: 2 6; -fx-background-radius: 4;");

            card.getChildren().addAll(name, loc, rate, stBadge);
            row.getChildren().add(card);
        }

        dynamicFleetContainer.getChildren().add(row);
    }

    /**
     * Horizontal Weather Card (Identical structure and emerald gradient to Farmer Dashboard)
     */
    private static HBox createHorizontalWeatherCard(String location, double latitude, double longitude) {
        HBox weatherCard = new HBox(20);
        weatherCard.setAlignment(Pos.CENTER_LEFT);
        weatherCard.setPadding(new Insets(14, 22, 14, 22));
        weatherCard.setMaxWidth(Double.MAX_VALUE);
        weatherCard.setStyle(
                "-fx-background-color: linear-gradient(to right, #1B5E20, #2E7D32, #388E3C);" +
                "-fx-background-radius: 14px;" +
                "-fx-effect: dropshadow(gaussian, rgba(27, 94, 32, 0.22), 12, 0.15, 0, 4);"
        );

        // Left Section: Weather Icon, Temperature & Location
        Text weatherIcon = new Text("☀");
        weatherIcon.setStyle("-fx-font-size: 34px; -fx-fill: #FFF9C4;");

        Text tempText = new Text("Loading...");
        tempText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 26px; -fx-font-weight: bold; -fx-fill: white;");

        Text condText = new Text("Loading weather...");
        condText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-font-weight: bold; -fx-fill: #E8F5E9;");

        Text locText = new Text("📍 " + location + ", Maharashtra");
        locText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-fill: #C8E6C9;");

        VBox currentDetails = new VBox(2, condText, locText);
        currentDetails.setAlignment(Pos.CENTER_LEFT);

        HBox leftCurrentBox = new HBox(12, weatherIcon, tempText, currentDetails);
        leftCurrentBox.setAlignment(Pos.CENTER_LEFT);

        // Middle Section: Agricultural machinery fleet advisory chip
        Text agriTip = new Text("🌱 Field Status: Favorable conditions for machinery operations");
        agriTip.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-font-weight: 500; -fx-fill: #E8F5E9;");
        HBox agriChip = new HBox(agriTip);
        agriChip.setAlignment(Pos.CENTER);
        agriChip.setPadding(new Insets(6, 14, 6, 14));
        agriChip.setStyle("-fx-background-color: rgba(255, 255, 255, 0.15); -fx-background-radius: 20px;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Right Section: 4-Day Forecast in individual mini column cards
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

        // Fetch Live Weather Asynchronously
        loadWeather(latitude, longitude, weatherIcon, tempText, condText, dayNames, dayIcons, dayTemps);

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
                HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() != 200) {
                    throw new RuntimeException("API returned HTTP " + response.statusCode());
                }

                return response.body();
            }
        };

        weatherTask.setOnSucceeded(e -> {
            updateWeatherUI(weatherTask.getValue(), weatherIcon, temperature, condition, dayNames, dayIcons, dayTemps);
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
            int currentStart = json.indexOf("\"current\":{");
            if (currentStart == -1) throw new RuntimeException("Current weather data not found");

            int currentEnd = json.indexOf("}", currentStart);
            String currentData = json.substring(currentStart, currentEnd);

            String currentTemperature = extractNumberFromSection(currentData, "\"temperature_2m\":");
            String currentCode = extractNumberFromSection(currentData, "\"weather_code\":");

            double temperatureValue = Double.parseDouble(currentTemperature);
            int weatherCode = Integer.parseInt(currentCode);

            temperature.setText(Math.round(temperatureValue) + "°C");
            condition.setText(getWeatherDescription(weatherCode));
            weatherIcon.setText(getWeatherIcon(weatherCode));

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
        if (start == -1) throw new RuntimeException("Key not found: " + key);
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
        if (code >= 1 && code <= 3) return "⛅";
        if (code >= 45 && code <= 48) return "🌫";
        if (code >= 51 && code <= 67) return "🌧";
        if (code >= 71 && code <= 77) return "❄";
        if (code >= 80 && code <= 82) return "🌦";
        if (code >= 95 && code <= 99) return "⛈";
        return "☀";
    }

    private static String getWeatherDescription(int code) {
        if (code == 0) return "Clear Sky";
        if (code == 1) return "Mainly Clear";
        if (code == 2) return "Partly Cloudy";
        if (code == 3) return "Overcast";
        if (code == 45 || code == 48) return "Foggy";
        if (code >= 51 && code <= 55) return "Drizzle";
        if (code >= 61 && code <= 65) return "Rain";
        if (code >= 80 && code <= 82) return "Rain Showers";
        if (code >= 95) return "Thunderstorm";
        return "Pleasant Weather";
    }
}
