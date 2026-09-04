package com.desgin.view.admin;

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
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class AdminHome {

    // In-memory instant cache to prevent loading delay / flicker
    private static String cachedTotalUsers = "23";
    private static String cachedUsersSub = "4 Farmers • 4 Providers • 9 Operators";
    private static String cachedTotalFleet = "3";
    private static String cachedFleetSub = "3 Available • 0 In-Use / Reserved";
    private static String cachedTotalGmv = "₹22,950";
    private static String cachedGmvSub = "₹22,950 secured in escrow";
    private static String cachedCommission = "₹1,606";
    private static String cachedCommissionSub = "Direct Platform Earnings YTD";

    public static ScrollPane getPage(StackPane root) {
        // 1. Live Agro-Weather Telemetry Card
        HBox weatherCard = createWeatherCard("Pune Hub", 18.5204, 73.8567);

        // 2. Only 4 Clean Master Metric Cards
        GridPane kpiGrid = createMasterKPIGrid(root);

        VBox content = new VBox(22, weatherCard, kpiGrid);
        content.setPadding(new Insets(20, 25, 35, 25));
        content.setStyle("-fx-background-color: transparent;");
        content.setMinWidth(0);
        content.setPrefWidth(Region.USE_COMPUTED_SIZE);
        content.setMaxWidth(Double.MAX_VALUE);

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        return scrollPane;
    }

    private static GridPane createMasterKPIGrid(StackPane root) {
        Text v1 = new Text(cachedTotalUsers);
        Text s1 = new Text(cachedUsersSub);
        VBox c1 = createMetricCardDynamic("👥 Total Users", v1, s1, "#1B4332", "#FFFFFF", () -> {
            AdminLeftSideBar.setActiveButton(AdminLeftSideBar.usersBtn, AdminLeftSideBar.navigationButtons);
            AdminDashboard.borderPane.setCenter(UserManagement.getPage(root));
        });

        Text v2 = new Text(cachedTotalFleet);
        Text s2 = new Text(cachedFleetSub);
        VBox c2 = createMetricCardDynamic("🚜 Total Equipment", v2, s2, "#2E7D32", "#E8F5E9", () -> {
            AdminLeftSideBar.setActiveButton(AdminLeftSideBar.approvalsBtn, AdminLeftSideBar.navigationButtons);
            AdminDashboard.borderPane.setCenter(MachineryApprovals.getPage(root));
        });

        Text v3 = new Text(cachedTotalGmv);
        Text s3 = new Text(cachedGmvSub);
        VBox c3 = createMetricCardDynamic("💳 Total User Revenue", v3, s3, "#1E3A8A", "#EFF6FF", () -> {
            AdminLeftSideBar.navigateToPaymentsDetail();
        });

        Text v4 = new Text(cachedCommission);
        Text s4 = new Text(cachedCommissionSub);
        VBox c4 = createMetricCardDynamic("🏦 Total Tax / Commission Generated", v4, s4, "#15803D", "#DCFCE7", () -> {
            AdminLeftSideBar.navigateToPaymentsDetail();
        });

        // Query Firestore asynchronously to update values live
        new Thread(() -> {
            try {
                // 1. Users count
                java.util.Map<String, Integer> userCounts = new com.desgin.dao.AuthDAO().getUserRoleCounts();
                int fCount = userCounts.getOrDefault("Farmer", 0);
                int pCount = userCounts.getOrDefault("Provider", 0);
                int oCount = userCounts.getOrDefault("Operator", 0);
                int aCount = userCounts.getOrDefault("Admin", 1);
                int totalUsers = fCount + pCount + oCount + aCount;

                // 2. Equipment count
                java.util.List<com.desgin.model.MachineryModel> machs = new com.desgin.dao.MachineryDAO().getAllMachinery();
                int totalFleet = machs.size();
                long availFleet = machs.stream().filter(m -> "AVAILABLE".equalsIgnoreCase(m.getStatus())).count();
                long inUseFleet = totalFleet - availFleet;

                // 3. Revenue & Commission from RentalRequests
                java.util.List<com.desgin.model.RentalRequestModel> reqs = new com.desgin.dao.RentalRequestDAO().getAllRequests();
                int totalGmv = 0;
                int escrowSecured = 0;
                for (com.desgin.model.RentalRequestModel r : reqs) {
                    int amt = r.getTotalAmount() > 0 ? r.getTotalAmount() : (r.getDailyRate() * Math.max(1, r.getDays()));
                    totalGmv += amt;
                    String pStat = r.getPaymentStatus() != null ? r.getPaymentStatus().toUpperCase() : "";
                    if ("PAID".equals(pStat) || "ESCROW HELD".equals(pStat)) {
                        escrowSecured += amt;
                    }
                }
                int netCommission = (int) (totalGmv * 0.07);

                final int fTotalUsers = totalUsers;
                final int fFCount = fCount;
                final int fPCount = pCount;
                final int fOCount = oCount;
                final int fTotalFleet = totalFleet;
                final long fAvailFleet = availFleet;
                final long fInUseFleet = inUseFleet;
                final int fTotalGmv = totalGmv;
                final int fEscrow = escrowSecured;
                final int fNetCommission = netCommission;

                Platform.runLater(() -> {
                    cachedTotalUsers = String.valueOf(fTotalUsers);
                    cachedUsersSub = fFCount + " Farmers • " + fPCount + " Providers • " + fOCount + " Operators";
                    v1.setText(cachedTotalUsers);
                    s1.setText(cachedUsersSub);

                    cachedTotalFleet = String.valueOf(fTotalFleet);
                    cachedFleetSub = fAvailFleet + " Available • " + fInUseFleet + " In-Use / Reserved";
                    v2.setText(cachedTotalFleet);
                    s2.setText(cachedFleetSub);

                    cachedTotalGmv = "₹" + String.format(Locale.ENGLISH, "%,d", fTotalGmv);
                    cachedGmvSub = "₹" + String.format(Locale.ENGLISH, "%,d", fEscrow) + " secured in escrow";
                    v3.setText(cachedTotalGmv);
                    s3.setText(cachedGmvSub);

                    cachedCommission = "₹" + String.format(Locale.ENGLISH, "%,d", fNetCommission);
                    cachedCommissionSub = "Direct Platform Earnings YTD";
                    v4.setText(cachedCommission);
                    s4.setText(cachedCommissionSub);
                });
            } catch (Exception e) {
                System.err.println("Notice: Could not load dynamic admin stats: " + e.getMessage());
            }
        }).start();

        GridPane grid = new GridPane();
        grid.setHgap(14);
        grid.setVgap(14);
        grid.setMinWidth(0);
        grid.setMaxWidth(Double.MAX_VALUE);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(25);
        col1.setHgrow(Priority.ALWAYS);

        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(25);
        col2.setHgrow(Priority.ALWAYS);

        ColumnConstraints col3 = new ColumnConstraints();
        col3.setPercentWidth(25);
        col3.setHgrow(Priority.ALWAYS);

        ColumnConstraints col4 = new ColumnConstraints();
        col4.setPercentWidth(25);
        col4.setHgrow(Priority.ALWAYS);

        grid.getColumnConstraints().addAll(col1, col2, col3, col4);

        grid.add(c1, 0, 0);
        grid.add(c2, 1, 0);
        grid.add(c3, 2, 0);
        grid.add(c4, 3, 0);

        return grid;
    }

    private static VBox createMetricCardDynamic(String title, Text v, Text s, String color, String bgColor, Runnable onClick) {
        Text t = new Text(title);
        t.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-fill: #4B5563; -fx-font-weight: 600;");

        v.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 23px; -fx-font-weight: bold; -fx-fill: " + color + ";");
        s.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-fill: #374151;");

        VBox card = new VBox(8, t, v, s);
        card.setPadding(new Insets(18, 16, 18, 16));
        card.setMinWidth(0);
        card.setMaxWidth(Double.MAX_VALUE);
        card.setStyle("-fx-background-color: " + bgColor + "; -fx-background-radius: 12; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 12; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.03), 8, 0, 0, 2);");

        card.setOnMouseEntered(e -> {
            card.setStyle("-fx-background-color: #F0FDF4; -fx-background-radius: 12; -fx-border-color: #2D6A4F; -fx-border-width: 1.5; -fx-border-radius: 12; -fx-effect: dropshadow(gaussian, rgba(27,67,50,0.18), 8, 0.2, 0, 2); -fx-cursor: hand;");
            card.setTranslateY(-2);
        });

        card.setOnMouseExited(e -> {
            card.setStyle("-fx-background-color: " + bgColor + "; -fx-background-radius: 12; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 12; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.03), 8, 0, 0, 2);");
            card.setTranslateY(0);
        });

        if (onClick != null) {
            card.setOnMouseClicked(e -> onClick.run());
        }
        return card;
    }

    private static HBox createWeatherCard(String location, double latitude, double longitude) {
        HBox weatherCard = new HBox(20);
        weatherCard.setAlignment(Pos.CENTER_LEFT);
        weatherCard.setPadding(new Insets(14, 22, 14, 22));
        weatherCard.setMaxWidth(Double.MAX_VALUE);
        weatherCard.setStyle(
                "-fx-background-color: linear-gradient(to right, #1B5E20, #2E7D32, #388E3C);" +
                "-fx-background-radius: 14px;" +
                "-fx-effect: dropshadow(gaussian, rgba(27, 94, 32, 0.22), 12, 0.15, 0, 4);"
        );

        // Left: Current Weather Icon, Temperature & Details
        Text weatherIcon = new Text("☀");
        weatherIcon.setStyle("-fx-font-size: 34px; -fx-fill: #FFF9C4;");

        Text temperature = new Text("Loading...");
        temperature.setStyle(
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 26px;" +
                "-fx-font-weight: bold;" +
                "-fx-fill: white;"
        );

        Text condition = new Text("Loading weather...");
        condition.setStyle(
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 13px;" +
                "-fx-font-weight: bold;" +
                "-fx-fill: #E8F5E9;"
        );

        Text locationText = new Text("📍 " + location + ", Maharashtra");
        locationText.setStyle(
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 11px;" +
                "-fx-fill: #C8E6C9;"
        );

        VBox currentDetails = new VBox(2, condition, locationText);
        currentDetails.setAlignment(Pos.CENTER_LEFT);

        HBox leftCurrentBox = new HBox(12, weatherIcon, temperature, currentDetails);
        leftCurrentBox.setAlignment(Pos.CENTER_LEFT);

        // Middle: Platform farming advisory badge
        Text agriTip = new Text("🌱 Field Status: Favorable conditions for farming operations");
        agriTip.setStyle(
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 11.5px;" +
                "-fx-font-weight: 500;" +
                "-fx-fill: #E8F5E9;"
        );
        HBox agriChip = new HBox(agriTip);
        agriChip.setAlignment(Pos.CENTER);
        agriChip.setPadding(new Insets(6, 14, 6, 14));
        agriChip.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.15);" +
                "-fx-background-radius: 20px;"
        );

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

        // Load live weather asynchronously
        loadWeather(
                latitude,
                longitude,
                weatherIcon,
                temperature,
                condition,
                dayNames,
                dayIcons,
                dayTemps
        );

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
            // Current Weather
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

            // Daily Weather Forecast
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
