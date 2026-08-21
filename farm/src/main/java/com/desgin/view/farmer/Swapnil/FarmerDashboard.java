package com.desgin.view.farmer.Swapnil;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import com.desgin.view.farmer.LeftSideBar;
import com.desgin.view.farmer.ashutosh.helpandsupport.Help;
import com.desgin.view.farmer.ashutosh.profile.ProfileManagement;
import com.desgin.view.farmer.ashutosh.settings.Settings;
import com.desgin.view.farmer.om.BrowseEquip;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class FarmerDashboard {

        private Scene farmerDashboardScene;

        public static BorderPane borderPane;
        public static StackPane root;

        public Scene getfarmerDashboardScene(Runnable ref) {

                root = new StackPane();

                // Side Bar
                LeftSideBar objLeftSideBar = new LeftSideBar(this);
                VBox leftVB = objLeftSideBar.getSideBar(ref);

                borderPane = new BorderPane();
                borderPane.setPadding(Insets.EMPTY);
                borderPane.setStyle("-fx-background-color: #F4F9F4;");

                BorderPane subroot = new BorderPane();
                subroot.setLeft(leftVB);
                subroot.setCenter(borderPane);
                subroot.setStyle("-fx-background-color: #F4F9F4;");

                root.getChildren().addAll(subroot);

                ProfileManagement objProfileManagement = new ProfileManagement();
                borderPane.setTop(objProfileManagement.getProfile(root));
                
                Dashboard.getPage();
                
                root.setStyle("-fx-background-color: #F4F9F4;");

                subroot.prefWidthProperty().bind(root.widthProperty());
                subroot.prefHeightProperty().bind(root.heightProperty());
                farmerDashboardScene = new Scene(root);

                return farmerDashboardScene;
        }

        private VBox createWeatherCard(
                        String location,
                        double latitude,
                        double longitude) {

                // Main weather card
                VBox weatherCard = new VBox(8);

                weatherCard.setPrefWidth(430);
                weatherCard.setPrefHeight(125);

                weatherCard.setPadding(
                                new Insets(12, 18, 12, 18));

                weatherCard.setStyle(
                                "-fx-background-color: #6F91D5;" +
                                                "-fx-background-radius: 20;");

                // Top section
                HBox topBox = new HBox(12);

                Text weatherIcon = new Text("☀");

                weatherIcon.setStyle(
                                "-fx-font-size: 38px;");

                VBox currentInfo = new VBox(2);

                Text temperature = new Text("Loading...");

                temperature.setStyle(
                                "-fx-font-family: 'Poppins';" +
                                                "-fx-font-size: 28px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-fill: white;");

                Text condition = new Text("Loading weather...");

                condition.setStyle(
                                "-fx-font-family: 'Poppins';" +
                                                "-fx-font-size: 12px;" +
                                                "-fx-fill: white;");

                Text locationText = new Text(
                                "⌖ " + location);

                locationText.setStyle(
                                "-fx-font-family: 'Poppins';" +
                                                "-fx-font-size: 11px;" +
                                                "-fx-fill: white;");

                currentInfo.getChildren().addAll(
                                temperature,
                                condition,
                                locationText);

                topBox.getChildren().addAll(
                                weatherIcon,
                                currentInfo);

                // Forecast section
                HBox forecastBox = new HBox(20);

                Text[] days = new Text[4];

                for (int i = 0; i < 4; i++) {

                        days[i] = new Text("Loading...");

                        days[i].setStyle(
                                        "-fx-font-family: 'Poppins';" +
                                                        "-fx-font-size: 11px;" +
                                                        "-fx-fill: white;");

                        forecastBox.getChildren().add(
                                        days[i]);
                }

                weatherCard.getChildren().addAll(
                                topBox,
                                forecastBox);

                // Load live weather
                loadWeather(
                                latitude,
                                longitude,
                                weatherIcon,
                                temperature,
                                condition,
                                days);

                return weatherCard;
        }

        private void loadWeather(
                        double latitude,
                        double longitude,
                        Text weatherIcon,
                        Text temperature,
                        Text condition,
                        Text[] days) {

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

                                System.out.println("Weather URL:");
                                System.out.println(url);

                                HttpClient client = HttpClient.newHttpClient();

                                HttpRequest request = HttpRequest.newBuilder()
                                                .uri(URI.create(url))
                                                .GET()
                                                .build();

                                HttpResponse<String> response = client.send(
                                                request,
                                                HttpResponse.BodyHandlers.ofString());

                                System.out.println("Weather Response Code: "
                                                + response.statusCode());

                                System.out.println("Weather Response:");
                                System.out.println(response.body());

                                if (response.statusCode() != 200) {
                                        throw new RuntimeException(
                                                        "API returned HTTP "
                                                                        + response.statusCode());
                                }

                                return response.body();
                        }
                };

                weatherTask.setOnSucceeded(e -> {

                        System.out.println("Weather loaded successfully!");

                        updateWeatherUI(
                                        weatherTask.getValue(),
                                        weatherIcon,
                                        temperature,
                                        condition,
                                        days);
                });

                weatherTask.setOnFailed(e -> {

                        System.out.println("================================");
                        System.out.println("WEATHER API FAILED");
                        System.out.println("================================");

                        weatherTask.getException()
                                        .printStackTrace();

                        Platform.runLater(() -> {

                                temperature.setText("--°C");

                                condition.setText(
                                                "Weather unavailable");

                                weatherIcon.setText("☀");

                                for (Text day : days) {
                                        day.setText("--");
                                }
                        });
                });

                Thread thread = new Thread(weatherTask);

                thread.setDaemon(true);

                thread.start();
        }

        private void updateWeatherUI(
                        String json,
                        Text weatherIcon,
                        Text temperature,
                        Text condition,
                        Text[] days) {

                try {

                        // ---------------- CURRENT WEATHER ----------------

                        // Find current section
                        int currentStart = json.indexOf("\"current\":{");

                        if (currentStart == -1) {
                                throw new RuntimeException(
                                                "Current weather data not found");
                        }

                        int currentEnd = json.indexOf(
                                        "}",
                                        currentStart);

                        String currentData = json.substring(
                                        currentStart,
                                        currentEnd);

                        // Current temperature
                        String currentTemperature = extractNumberFromSection(
                                        currentData,
                                        "\"temperature_2m\":");

                        // Current weather code
                        String currentCode = extractNumberFromSection(
                                        currentData,
                                        "\"weather_code\":");

                        double temperatureValue = Double.parseDouble(
                                        currentTemperature);

                        int weatherCode = Integer.parseInt(
                                        currentCode);

                        // Update current temperature
                        temperature.setText(
                                        Math.round(
                                                        temperatureValue) + "°C");

                        // Update weather description
                        condition.setText(
                                        getWeatherDescription(
                                                        weatherCode));

                        // Update weather icon
                        weatherIcon.setText(
                                        getWeatherIcon(
                                                        weatherCode));

                        // ---------------- DAILY WEATHER ----------------

                        int dailyStart = json.indexOf("\"daily\":{");

                        if (dailyStart == -1) {
                                return;
                        }

                        int dailyEnd = json.indexOf(
                                        "}",
                                        dailyStart);

                        String dailyData = json.substring(
                                        dailyStart,
                                        json.length());

                        // Dates
                        String datesPart = extractArray(
                                        dailyData,
                                        "\"time\":[");

                        // Maximum temperature
                        String maxPart = extractArray(
                                        dailyData,
                                        "\"temperature_2m_max\":[");

                        // Minimum temperature
                        String minPart = extractArray(
                                        dailyData,
                                        "\"temperature_2m_min\":[");

                        // Daily weather codes
                        String codePart = extractArray(
                                        dailyData,
                                        "\"weather_code\":[");

                        String[] dateArray = datesPart.split(",");

                        String[] maxArray = maxPart.split(",");

                        String[] minArray = minPart.split(",");

                        String[] codeArray = codePart.split(",");

                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
                                        "EEE",
                                        Locale.ENGLISH);

                        for (int i = 0; i < 4 && i < days.length; i++) {

                                String date = dateArray[i]
                                                .replace("\"", "")
                                                .trim();

                                LocalDate localDate = LocalDate.parse(date);

                                double maxTemperature = Double.parseDouble(
                                                maxArray[i]
                                                                .trim());

                                double minTemperature = Double.parseDouble(
                                                minArray[i]
                                                                .trim());

                                int dailyWeatherCode = Integer.parseInt(
                                                codeArray[i]
                                                                .trim());

                                String dayName = localDate.format(
                                                formatter);

                                String max = Math.round(
                                                maxTemperature) + "°";

                                String min = Math.round(
                                                minTemperature) + "°";

                                days[i].setText(
                                                dayName
                                                                + "  "
                                                                + getWeatherIcon(
                                                                                dailyWeatherCode)
                                                                + "  "
                                                                + max
                                                                + "/"
                                                                + min);
                        }

                } catch (Exception ex) {

                        ex.printStackTrace();

                        Platform.runLater(() -> {

                                temperature.setText(
                                                "--°C");

                                condition.setText(
                                                "Unable to load weather");

                                weatherIcon.setText(
                                                "☀");

                                for (Text day : days) {
                                        day.setText(
                                                        "Loading...");
                                }
                        });
                }
        }

        private String extractNumberFromSection(
                        String section,
                        String key) {

                int start = section.indexOf(key);

                if (start == -1) {
                        throw new RuntimeException(
                                        "Key not found: " + key);
                }

                start += key.length();

                int end = start;

                while (end < section.length()
                                &&
                                section.charAt(end) != ','
                                &&
                                section.charAt(end) != '}') {
                        end++;
                }

                return section
                                .substring(start, end)
                                .trim();
        }

        private String extractArray(
                        String json,
                        String key) {

                int start = json.indexOf(key);

                if (start == -1) {
                        return "";
                }

                start += key.length();

                int end = json.indexOf(
                                "]",
                                start);

                if (end == -1) {
                        return "";
                }

                return json.substring(
                                start,
                                end);
        }

        private String getWeatherIcon(
                        int code) {

                if (code == 0) {
                        return "☀";
                }

                if (code == 1 ||
                                code == 2) {
                        return "🌤";
                }

                if (code == 3) {
                        return "☁";
                }

                if (code >= 45 &&
                                code <= 48) {
                        return "🌫";
                }

                if (code >= 51 &&
                                code <= 67) {
                        return "🌧";
                }

                if (code >= 71 &&
                                code <= 77) {
                        return "❄";
                }

                if (code >= 80 &&
                                code <= 82) {
                        return "🌦";
                }

                if (code >= 95 &&
                                code <= 99) {
                        return "⛈";
                }

                return "☁";
        }

        private String getWeatherDescription(
                        int code) {

                if (code == 0) {
                        return "Clear sky";
                }

                if (code == 1 ||
                                code == 2) {
                        return "Partly cloudy";
                }

                if (code == 3) {
                        return "Cloudy";
                }

                if (code >= 45 &&
                                code <= 48) {
                        return "Foggy";
                }

                if (code >= 51 &&
                                code <= 67) {
                        return "Rain likely";
                }

                if (code >= 71 &&
                                code <= 77) {
                        return "Snow";
                }

                if (code >= 80 &&
                                code <= 82) {
                        return "Rain showers";
                }

                if (code >= 95 &&
                                code <= 99) {
                        return "Thunderstorm";
                }

                return "Unknown";
        }

        private VBox createDashboardCard(
                        String icon,
                        String title,
                        String value) {

                Text iconText = new Text(icon);

                iconText.setStyle(
                                "-fx-font-size: 25px;");

                Text titleText = new Text(title);

                titleText.setStyle(
                                "-fx-font-size: 13px;" +
                                                "-fx-fill: #806A5B;");

                Text valueText = new Text(value);

                valueText.setStyle(
                                "-fx-font-size: 24px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-fill: #4A2C20;");

                VBox card = new VBox(
                                8,
                                iconText,
                                titleText,
                                valueText);

                card.setPrefWidth(220);
                card.setPrefHeight(135);

                card.setPadding(
                                new Insets(18));

                // Normal style
                String normalStyle = "-fx-background-color: #F5EFE6;" +
                                "-fx-background-radius: 14;" +
                                "-fx-border-color: #D8C7B5;" +
                                "-fx-border-width: 1;" +
                                "-fx-border-radius: 14;";

                // Hover style
                String hoverStyle = "-fx-background-color: #FFF9F0;" +
                                "-fx-background-radius: 14;" +
                                "-fx-border-color: #8B6F47;" +
                                "-fx-border-width: 1.5;" +
                                "-fx-border-radius: 14;" +
                                "-fx-effect: dropshadow(gaussian, rgba(74,44,32,0.25), 12, 0.2, 0, 4);";

                card.setStyle(normalStyle);

                // Smooth animation
                card.setOnMouseEntered(e -> {

                        card.setStyle(hoverStyle);

                        card.setScaleX(1.03);
                        card.setScaleY(1.03);

                        card.setTranslateY(-3);
                });

                card.setOnMouseExited(e -> {

                        card.setStyle(normalStyle);

                        card.setScaleX(1.0);
                        card.setScaleY(1.0);

                        card.setTranslateY(0);
                });

                return card;
        }

        private VBox createEquipmentCard(
                        String imagePath,
                        String equipmentName,
                        String price) {

                Image equipmentImage = new Image(imagePath);

                ImageView imageView = new ImageView(equipmentImage);

                imageView.setFitWidth(196);
                imageView.setFitHeight(110);
                imageView.setPreserveRatio(true);
                imageView.setSmooth(true);

                StackPane imageBox = new StackPane(imageView);

                imageBox.setPrefHeight(110);
                imageBox.setMaxWidth(Double.MAX_VALUE);

                imageBox.setStyle(
                                "-fx-background-color: #E4D3C2;" +
                                                "-fx-background-radius: 10;");

                Text nameText = new Text(equipmentName);

                nameText.setStyle(
                                "-fx-font-family: 'Poppins';" +
                                                "-fx-font-size: 16px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-fill: #4A2C20;");

                Text priceText = new Text(price);

                priceText.setStyle(
                                "-fx-font-family: 'Poppins';" +
                                                "-fx-font-size: 13px;" +
                                                "-fx-fill: #806A5B;");
                Button viewButton = new Button("View Details");

                viewButton.setPrefWidth(196);
                viewButton.setPrefHeight(35);

                viewButton.setStyle(
                                "-fx-background-color: #8B6F47;" +
                                                "-fx-background-radius: 8;" +
                                                "-fx-text-fill: white;" +
                                                "-fx-font-family: 'Poppins';" +
                                                "-fx-font-size: 12px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-cursor: hand;");

                viewButton.setOnMouseEntered(e -> {

                        viewButton.setStyle(
                                        "-fx-background-color: #6F5638;" +
                                                        "-fx-background-radius: 8;" +
                                                        "-fx-text-fill: white;" +
                                                        "-fx-font-family: 'Poppins';" +
                                                        "-fx-font-size: 12px;" +
                                                        "-fx-font-weight: bold;" +
                                                        "-fx-cursor: hand;");
                });

                viewButton.setOnMouseExited(e -> {

                        viewButton.setStyle(
                                        "-fx-background-color: #8B6F47;" +
                                                        "-fx-background-radius: 8;" +
                                                        "-fx-text-fill: white;" +
                                                        "-fx-font-family: 'Poppins';" +
                                                        "-fx-font-size: 12px;" +
                                                        "-fx-font-weight: bold;" +
                                                        "-fx-cursor: hand;");
                });

                VBox card = new VBox(
                                10,
                                imageBox,
                                nameText,
                                priceText,
                                viewButton);

                card.setPrefWidth(220);
                card.setPrefHeight(210);

                card.setPadding(
                                new Insets(12));

                card.setStyle(
                                "-fx-background-color: #F5EFE6;" +
                                                "-fx-background-radius: 14;" +
                                                "-fx-border-color: #D8C7B5;" +
                                                "-fx-border-width: 1;" +
                                                "-fx-border-radius: 14;");

                return card;
        }

        private HBox createBookingRow(
                        String equipmentName,
                        String date,
                        String status) {

                Text equipmentText = new Text(equipmentName);

                equipmentText.setStyle(
                                "-fx-font-family: 'Poppins';" +
                                                "-fx-font-size: 14px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-fill: #4A2C20;");

                Text dateText = new Text(date);

                dateText.setStyle(
                                "-fx-font-family: 'Poppins';" +
                                                "-fx-font-size: 13px;" +
                                                "-fx-fill: #806A5B;");

                Text statusText = new Text(status);

                statusText.setStyle(
                                "-fx-font-family: 'Poppins';" +
                                                "-fx-font-size: 13px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-fill: #6B7D45;");

                VBox equipmentBox = new VBox(equipmentText);

                VBox dateBox = new VBox(dateText);

                VBox statusBox = new VBox(statusText);

                HBox row = new HBox(
                                100,
                                equipmentBox,
                                dateBox,
                                statusBox);

                row.setAlignment(
                                Pos.CENTER_LEFT);

                row.setPadding(
                                new Insets(15));

                row.setPrefHeight(55);

                row.setStyle(
                                "-fx-background-color: #F5EFE6;" +
                                                "-fx-background-radius: 10;" +
                                                "-fx-border-color: #D8C7B5;" +
                                                "-fx-border-width: 1;" +
                                                "-fx-border-radius: 10;");

                return row;
        }

        private VBox createActiveBookingCard(
                        String equipmentName,
                        String bookingDate,
                        String status) {

                Text equipmentText = new Text("🚜  " + equipmentName);

                equipmentText.setStyle(
                                "-fx-font-family: 'Poppins';" +
                                                "-fx-font-size: 17px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-fill: #4A2C20;");

                Text dateText = new Text(bookingDate);

                dateText.setStyle(
                                "-fx-font-family: 'Poppins';" +
                                                "-fx-font-size: 13px;" +
                                                "-fx-fill: #806A5B;");

                Text statusText = new Text("Status: " + status);

                statusText.setStyle(
                                "-fx-font-family: 'Poppins';" +
                                                "-fx-font-size: 13px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-fill: #6B7D45;");

                Button viewButton = new Button("View Booking");

                viewButton.setPrefWidth(130);
                viewButton.setPrefHeight(35);

                viewButton.setStyle(
                                "-fx-background-color: #8B6F47;" +
                                                "-fx-background-radius: 8;" +
                                                "-fx-text-fill: white;" +
                                                "-fx-font-family: 'Poppins';" +
                                                "-fx-font-size: 12px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-cursor: hand;");

                viewButton.setOnAction(e -> {

                        System.out.println("View Booking clicked");

                });

                VBox card = new VBox(
                                10,
                                equipmentText,
                                dateText,
                                statusText,
                                viewButton);

                card.setPadding(
                                new Insets(18));

                card.setPrefHeight(150);

                card.setStyle(
                                "-fx-background-color: #F5EFE6;" +
                                                "-fx-background-radius: 14;" +
                                                "-fx-border-color: #D8C7B5;" +
                                                "-fx-border-width: 1;" +
                                                "-fx-border-radius: 14;");

                return card;
        }


       
}
