package com.desgin.view.farmer.Swapnil;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import com.desgin.view.farmer.LeftSideBar;
import com.desgin.view.farmer.om.BrowseEquip;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class Dashboard {

        public static Dashboard dashboard;



        public static ScrollPane getPage() {

                
                String userName = "FarmerName";
                Text welcomeText = new Text("Welcome back, " + userName + " 👋");

                welcomeText.setStyle(
                                "-fx-font-family: 'Poppins';" +
                                                "-fx-font-size: 28px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-fill: #4A2C20;");

                Text dashboardText = new Text("Farmer Dashboard");

                dashboardText.setStyle(
                                "-fx-font-family: 'Poppins';" +
                                                "-fx-font-size: 18px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-fill: #5C4033;");

                Text descriptionText = new Text("Find the right equipment for your farm");

                descriptionText.setStyle(
                                "-fx-font-family: 'Poppins';" +
                                                "-fx-font-size: 13px;" +
                                                "-fx-fill: #806A5B;");

                VBox headerText = new VBox(
                                5,
                                welcomeText,
                                dashboardText,
                                descriptionText);

                headerText.setAlignment(Pos.TOP_LEFT);

                TextField searchField = new TextField();

                searchField.setPromptText("Search equipment...");

                searchField.setPrefHeight(42);
                searchField.setPrefWidth(500);

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

                searchIcon.setStyle(
                                "-fx-font-size: 18px;");
                

                Button searchButton = new Button("Search");
                searchButton.setPrefWidth(90);
                searchButton.setPrefHeight(45);

                searchButton.setStyle(
                                "-fx-background-color: #6B8E23;" +
                                                "-fx-background-radius: 10;" +
                                                "-fx-text-fill: white;" +
                                                "-fx-font-family: 'Poppins';" +
                                                "-fx-font-size: 13px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-cursor: hand;");

                searchButton.setOnMouseEntered(e -> searchButton.setStyle(
                                "-fx-background-color: #55751C;" +
                                                "-fx-background-radius: 10;" +
                                                "-fx-text-fill: white;" +
                                                "-fx-font-family: 'Poppins';" +
                                                "-fx-font-size: 13px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-cursor: hand;"));

                searchButton.setOnMouseExited(e -> searchButton.setStyle(
                                "-fx-background-color: #6B8E23;" +
                                                "-fx-background-radius: 10;" +
                                                "-fx-text-fill: white;" +
                                                "-fx-font-family: 'Poppins';" +
                                                "-fx-font-size: 13px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-cursor: hand;"));

                HBox searchBox = new HBox(
                                10,
                                searchIcon,
                                searchField,
                        searchButton);

                searchBox.setAlignment(
                                Pos.CENTER_LEFT);
                

                                        
                VBox weatherCard = createWeatherCard(
                                "Pune",
                                18.5204,
                                73.8567);

                weatherCard.setAlignment(Pos.TOP_RIGHT);



                VBox equipmentCard = createDashboardCard(
                                "⚒",
                                "Available Equipment",
                                "24");

                equipmentCard.setOnMouseClicked(e -> {

                        LeftSideBar.setActiveButton(
                                        LeftSideBar.equipmentBtn1,
                                        LeftSideBar.navigationButtons);

                        FarmerDashboard.borderPane.setCenter(BrowseEquip.getBrowseEquip());;
                });

                VBox bookingCard = createDashboardCard(
                                "📅",
                                "My Bookings",
                                "5");

                VBox activebookingCard = createDashboardCard(
                                "🚜",
                                "Active Booking",
                                "2");

                VBox pendingbookingCard = createDashboardCard(
                                "🕐",
                                "Pending Booking",
                                "2");
                HBox cards = new HBox(
                                20,
                                equipmentCard,
                                bookingCard,
                                activebookingCard,
                                pendingbookingCard);

                cards.setAlignment(Pos.CENTER_LEFT);

                VBox header = new VBox(headerText);
                header.setAlignment(Pos.TOP_LEFT);
                header.setPadding(
                                new Insets(25, 30, 20, 30));

                HBox combine = new HBox(400, headerText, weatherCard);

                Text recommendedTitle = new Text("Recommended Equipment");

                recommendedTitle.setStyle(
                                "-fx-font-family: 'Poppins';" +
                                                "-fx-font-size: 20px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-fill: #4A2C20;");
                VBox tractorCard = createEquipmentCard(
                                "file:farm/src/main/resources/assets/Images/tractor.png",
                                "Tractor",
                                "₹1200 / day");

                VBox rotavatorCard = createEquipmentCard(
                                "file:farm/src/main/resources/assets/Images/rotavator.png",
                                "Rotavator",
                                "₹800 / day");

                VBox cultivatorCard = createEquipmentCard(
                                "file:farm/src/main/resources/assets/Images/cultivator.png",
                                "Cultivator",
                                "₹600 / day");

                VBox harvesterCard = createEquipmentCard(
                                "file:farm/src/main/resources/assets/Images/",
                                "Harvester",
                                "₹900 / day");

                VBox seederCard = createEquipmentCard(
                                "file:farm/src/main/resources/assets/Images/",
                                "Seeder",
                                "₹400 / day");

                HBox equipmentSection = new HBox(
                                20,
                                tractorCard,
                                rotavatorCard,
                                cultivatorCard,
                                harvesterCard,
                                seederCard);
                Text viewMore = new Text("View More →");
                viewMore.setOnMouseClicked(event -> {

                        LeftSideBar.setActiveButton(LeftSideBar.equipmentBtn1, LeftSideBar.navigationButtons);
                        FarmerDashboard.borderPane.setCenter(BrowseEquip.getBrowseEquip());
                });

                viewMore.setStyle(
                                "-fx-fill: #2e7d32;" +
                                                "-fx-font-size: 14px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-cursor: hand;");

                equipmentSection.getChildren().add(viewMore);

                equipmentSection.setAlignment(
                                Pos.CENTER_LEFT);
                equipmentSection.setPadding(new Insets(10, 0, 10, 0));

                ScrollPane equipmentScroll = new ScrollPane();

                equipmentScroll.setContent(equipmentSection);

                equipmentScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
                equipmentScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

                equipmentScroll.setFitToHeight(true);

                equipmentScroll.setStyle(
                                "-fx-background-color: transparent;" +
                                                "-fx-border-color: transparent;");

                Text activeBookingTitle = new Text("Active Booking");

                activeBookingTitle.setStyle(
                                "-fx-font-family: 'Poppins';" +
                                                "-fx-font-size: 20px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-fill: #4A2C20;");
                Text viewMore2 = new Text("View More →");

                viewMore2.setStyle(
                                "-fx-font-family: 'Poppins';" +
                                                "-fx-font-size: 13px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-fill: #2E7D32;" +
                                                "-fx-cursor: hand;");

                HBox activeBookingHeader = new HBox();
                activeBookingHeader.setAlignment(Pos.CENTER_LEFT);

                Region spacer = new Region();

                HBox.setHgrow(spacer, Priority.ALWAYS);

                activeBookingHeader.getChildren().addAll(
                                activeBookingTitle,
                                spacer,
                                viewMore2);

                VBox activeBookingCard = createActiveBookingCard(
                                "Tractor",
                                "15 Aug 2026 → 17 Aug 2026",
                                "Confirmed");

                VBox activeBookingSection = new VBox(12);

                activeBookingSection.getChildren().addAll(
                                activeBookingHeader,
                                activeBookingCard);

                Text recentBookingTitle = new Text("Recent Bookings");

                recentBookingTitle.setStyle(
                                "-fx-font-family: 'Poppins';" +
                                                "-fx-font-size: 20px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-fill: #4A2C20;");
                HBox bookingRow = createBookingRow(
                                "Tractor",
                                "12 Aug 2026",
                                "Confirmed");

                VBox centerContent = new VBox(
                                20,
                                combine,
                                // header,
                                searchBox,
                                // searchWeatherBox,
                                cards,
                                recommendedTitle,
                                equipmentScroll,
                                activeBookingSection,
                                recentBookingTitle,
                                bookingRow);
                centerContent.setPadding(new Insets(20, 30, 30, 30));
                ScrollPane scrollPane = new ScrollPane();

                scrollPane.setContent(centerContent);

                scrollPane.setFitToWidth(true);
                scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
                scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

                scrollPane.setStyle("-fx-background-color: transparent;" + "-fx-background: transparent;");
        
               FarmerDashboard.borderPane.setCenter(scrollPane);

                return scrollPane;
        }
        private static VBox createWeatherCard(
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

        private static void loadWeather(
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

        private static void updateWeatherUI(
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

        private static String extractNumberFromSection(
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

        private static String extractArray(
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

        private static String getWeatherIcon(
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

        private static String getWeatherDescription(
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

        private static VBox createDashboardCard(
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

        private static VBox createEquipmentCard(
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

        private static HBox createBookingRow(
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

        private static VBox createActiveBookingCard(
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