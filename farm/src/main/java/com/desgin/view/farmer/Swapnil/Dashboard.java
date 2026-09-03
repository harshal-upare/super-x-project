package com.desgin.view.farmer.Swapnil;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import com.desgin.view.farmer.LeftSideBar;
import com.desgin.view.farmer.ashutosh.helpandsupport.Help;
import com.desgin.view.farmer.ashutosh.profile.ProfileManagement;
import com.desgin.view.farmer.ashutosh.settings.Settings;
import com.desgin.view.farmer.harshal.MyBookings;
import com.desgin.view.farmer.om.BrowseEquip;
import com.desgin.view.farmer.om.EquipmentDetailPage;
import com.desgin.view.farmer.pratik.Payment;
import com.desgin.view.farmer.pratik.WishList;
import com.desgin.view.farmer.review.ReviewRating;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
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

                Text searchIcon = new Text("🔍");

                searchIcon.setStyle(
                                "-fx-font-size: 16px;");

                TextField searchField = new TextField();

                searchField.setPromptText(
                                "Search anything (Equipment, Bookings, Wishlist, Payments, Settings, Help)...");

                searchField.setPrefWidth(420);
                searchField.setPrefHeight(45);

                searchField.setStyle(
                                "-fx-background-color: rgba(255, 255, 255, 0.95);" +
                                                "-fx-background-radius: 12;" +
                                                "-fx-border-color: rgba(45, 106, 79, 0.3);" +
                                                "-fx-border-width: 1.2;" +
                                                "-fx-border-radius: 12;" +
                                                "-fx-padding: 0 15 0 15;" +
                                                "-fx-font-family: 'Poppins';" +
                                                "-fx-font-size: 13px;" +
                                                "-fx-text-fill: #1F2937;");

                Button searchButton = new Button("Search");

                searchButton.setPrefWidth(95);
                searchButton.setPrefHeight(45);

                searchButton.setStyle(
                                "-fx-background-color: linear-gradient(to right, #2D6A4F, #40916C);" +
                                                "-fx-background-radius: 12;" +
                                                "-fx-text-fill: white;" +
                                                "-fx-font-family: 'Poppins';" +
                                                "-fx-font-size: 13px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-cursor: hand;" +
                                                "-fx-effect: dropshadow(gaussian, rgba(45, 106, 79, 0.25), 8, 0, 0, 2);");

                searchButton.setOnMouseEntered(e -> searchButton.setStyle(
                                "-fx-background-color: linear-gradient(to right, #1B4332, #2D6A4F);" +
                                                "-fx-background-radius: 12;" +
                                                "-fx-text-fill: white;" +
                                                "-fx-font-family: 'Poppins';" +
                                                "-fx-font-size: 13px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-cursor: hand;" +
                                                "-fx-effect: dropshadow(gaussian, rgba(45, 106, 79, 0.35), 10, 0, 0, 3);"));

                searchButton.setOnMouseExited(e -> searchButton.setStyle(
                                "-fx-background-color: linear-gradient(to right, #2D6A4F, #40916C);" +
                                                "-fx-background-radius: 12;" +
                                                "-fx-text-fill: white;" +
                                                "-fx-font-family: 'Poppins';" +
                                                "-fx-font-size: 13px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-cursor: hand;" +
                                                "-fx-effect: dropshadow(gaussian, rgba(45, 106, 79, 0.25), 8, 0, 0, 2);"));

                // Universal Global Search Handler for Farmer Dashboard
                Runnable doSearch = () -> {
                        String raw = searchField.getText() != null ? searchField.getText().trim() : "";
                        if (raw.isEmpty()) return;
                        String q = raw.toLowerCase();

                        if (q.contains("book") || q.contains("order") || q.contains("rent") || q.contains("hist")) {
                                LeftSideBar.setActiveButton(LeftSideBar.bookingBtn1, LeftSideBar.navigationButtons);
                                ProfileManagement.setHeaderTitle("My Bookings 📅", "Track and manage your equipment rentals & schedules");
                                MyBookings obj = new MyBookings();
                                FarmerDashboard.borderPane.setCenter(obj.getBooking(FarmerDashboard.root));
                        } else if (q.contains("wish") || q.contains("save") || q.contains("fav") || q.contains("like")) {
                                LeftSideBar.setActiveButton(LeftSideBar.wishlistBtn1, LeftSideBar.navigationButtons);
                                ProfileManagement.setHeaderTitle("My Saved Wishlist ❤️", "Saved machinery and certified operators for quick booking");
                                FarmerDashboard.borderPane.setCenter(WishList.getWishList());
                        } else if (q.contains("oper") || q.contains("driver") || q.contains("pilot") || q.contains("worker") || q.contains("hire") || q.contains("manpower") || q.contains("pay") || q.contains("bill")) {
                                LeftSideBar.setActiveButton(LeftSideBar.paymentBtn1, LeftSideBar.navigationButtons);
                                ProfileManagement.setHeaderTitle("Search Machinery Operators 👷", "Find and hire certified drivers and machine operators");
                                FarmerDashboard.borderPane.setCenter(com.desgin.view.farmer.pratik.SearchOperator.getSearchOperatorSection(FarmerDashboard.root));
                        } else if (q.contains("review") || q.contains("rat") || q.contains("star") || q.contains("feed")) {
                                LeftSideBar.setActiveButton(LeftSideBar.reviewBtn1, LeftSideBar.navigationButtons);
                                ProfileManagement.setHeaderTitle("Reviews & Field Ratings ⭐", "Rate your completed rentals and machine operators");
                                FarmerDashboard.borderPane.setCenter(ReviewRating.getReviewRatingPage(FarmerDashboard.root));
                        } else if (q.contains("sett") || q.contains("pref") || q.contains("pass") || q.contains("notif")) {
                                LeftSideBar.setActiveButton(LeftSideBar.settingsBtn1, LeftSideBar.navigationButtons);
                                ProfileManagement.setHeaderTitle("Settings & Preferences ⚙", "Manage your account credentials and security");
                                FarmerDashboard.borderPane.setCenter(Settings.getSetting());
                        } else if (q.contains("help") || q.contains("supp") || q.contains("ai") || q.contains("faq") || q.contains("chat") || q.contains("bot") || q.contains("issue")) {
                                LeftSideBar.setActiveButton(LeftSideBar.supportBtn1, LeftSideBar.navigationButtons);
                                ProfileManagement.setHeaderTitle("Help & Support Desk 🛟", "Get 24x7 farmer assistance and AI advisory");
                                FarmerDashboard.borderPane.setCenter(Help.getHelp());
                        } else if (q.contains("prof") || q.contains("user") || q.contains("acc") || q.contains("harshal")) {
                                ProfileManagement obj = new ProfileManagement();
                                FarmerDashboard.borderPane.setCenter(obj.getProfile(FarmerDashboard.root));
                        } else if (q.contains("weath") || q.contains("temp") || q.contains("rain") || q.contains("pune") || q.contains("clim")) {
                                LeftSideBar.setActiveButton(LeftSideBar.dashboardBtn1, LeftSideBar.navigationButtons);
                                ProfileManagement.updateHeaderGreeting();
                                FarmerDashboard.borderPane.setCenter(Dashboard.getPage());
                        } else {
                                // Equipment keyword, category, or general machinery search
                                LeftSideBar.setActiveButton(LeftSideBar.equipmentBtn1, LeftSideBar.navigationButtons);
                                ProfileManagement.setHeaderTitle("Browse Equipment ⚒", "Find and rent the right machinery for your farm");
                                FarmerDashboard.borderPane.setCenter(BrowseEquip.getBrowseEquip());
                                BrowseEquip.setSearchQuery(raw);
                        }
                };

                searchButton.setOnAction(e -> doSearch.run());
                searchField.setOnAction(e -> doSearch.run());

                HBox searchBox = new HBox(
                                10,
                                searchIcon,
                                searchField,
                                searchButton);

                searchBox.setAlignment(
                                Pos.CENTER_LEFT);

                HBox weatherCard = createWeatherCard(
                                "Pune",
                                18.5204,
                                73.8567);

                // Dynamic KPI cards reflecting live counts
                VBox equipmentCard = createDashboardCard(
                                "⚒",
                                "Available Equipment",
                                String.valueOf(EquipmentDataStore.getAvailableCount()));

                equipmentCard.setOnMouseClicked(e -> {
                        LeftSideBar.setActiveButton(
                                        LeftSideBar.equipmentBtn1,
                                        LeftSideBar.navigationButtons);
                        ProfileManagement.setHeaderTitle("Browse Equipment ⚒", "Find and rent the right machinery for your farm");
                        FarmerDashboard.borderPane.setCenter(BrowseEquip.getBrowseEquip());
                });

                VBox bookingCard = createDashboardCard(
                                "📅",
                                "My Bookings",
                                String.valueOf(BookingDataStore.getTotalCount()));

                bookingCard.setOnMouseClicked(e -> {
                        LeftSideBar.setActiveButton(
                                        LeftSideBar.bookingBtn1,
                                        LeftSideBar.navigationButtons);
                        ProfileManagement.setHeaderTitle("My Bookings 📅", "Track and manage your equipment rentals & schedules");
                        MyBookings obj = new MyBookings();
                        FarmerDashboard.borderPane.setCenter(obj.getBooking(FarmerDashboard.root));
                });

                VBox activebookingCard = createDashboardCard(
                                "🚜",
                                "Active Booking",
                                String.valueOf(BookingDataStore.getActiveCount()));

                activebookingCard.setOnMouseClicked(e -> {
                        LeftSideBar.setActiveButton(
                                        LeftSideBar.bookingBtn1,
                                        LeftSideBar.navigationButtons);
                        ProfileManagement.setHeaderTitle("My Bookings 📅", "Track and manage your equipment rentals & schedules");
                        MyBookings obj = new MyBookings();
                        FarmerDashboard.borderPane.setCenter(obj.getBooking(FarmerDashboard.root));
                });

                VBox pendingbookingCard = createDashboardCard(
                                "🕐",
                                "Pending Booking",
                                String.valueOf(BookingDataStore.getPendingCount()));

                pendingbookingCard.setOnMouseClicked(e -> {
                        LeftSideBar.setActiveButton(
                                        LeftSideBar.bookingBtn1,
                                        LeftSideBar.navigationButtons);
                        ProfileManagement.setHeaderTitle("My Bookings 📅", "Track and manage your equipment rentals & schedules");
                        MyBookings obj = new MyBookings();
                        FarmerDashboard.borderPane.setCenter(obj.getBooking(FarmerDashboard.root));
                });

                HBox cards = new HBox(
                                16,
                                equipmentCard,
                                bookingCard,
                                activebookingCard,
                                pendingbookingCard);

                cards.setAlignment(Pos.CENTER_LEFT);
                HBox.setHgrow(equipmentCard, Priority.ALWAYS);
                HBox.setHgrow(bookingCard, Priority.ALWAYS);
                HBox.setHgrow(activebookingCard, Priority.ALWAYS);
                HBox.setHgrow(pendingbookingCard, Priority.ALWAYS);

                // ----------------- RECOMMENDED EQUIPMENT SECTION -----------------
                Text recommendedTitle = new Text("Recommended Equipment");

                recommendedTitle.setStyle(
                                "-fx-font-family: 'Poppins';" +
                                                "-fx-font-size: 20px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-fill: #1B4332;");

                try {
                    List<com.desgin.model.MachineryModel> firestoreList = new com.desgin.dao.MachineryDAO().getAllMachinery();
                    EquipmentDataStore.syncFromFirestore(firestoreList);
                } catch (Exception ignored) {}

                List<EquipmentDataStore.EquipmentItem> recommendedList = EquipmentDataStore.getRandomRecommended(5);
                Node recommendedNode;

                if (recommendedList.isEmpty()) {
                        VBox emptyRecBox = new VBox(8);
                        emptyRecBox.setAlignment(Pos.CENTER);
                        emptyRecBox.setPadding(new Insets(24, 30, 24, 30));
                        emptyRecBox.setMaxWidth(Double.MAX_VALUE);
                        emptyRecBox.setStyle(
                                "-fx-background-color: #FFFFFF;" +
                                "-fx-background-radius: 14;" +
                                "-fx-border-color: #D1E7DD;" +
                                "-fx-border-width: 1.2;" +
                                "-fx-border-radius: 14;"
                        );
                        Text emptyIcon = new Text("🚜");
                        emptyIcon.setStyle("-fx-font-size: 30px;");

                        Text emptyTitle = new Text("No Recommended Equipment Available Yet");
                        emptyTitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 15.5px; -fx-font-weight: bold; -fx-fill: #1B4332;");

                        Text emptyDesc = new Text("Currently no machinery is available. When providers add equipment to the platform, recommended machinery will appear here automatically.");
                        emptyDesc.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-fill: #6B7280;");

                        emptyRecBox.getChildren().addAll(emptyIcon, emptyTitle, emptyDesc);
                        recommendedNode = emptyRecBox;
                } else {
                        HBox equipmentSection = new HBox(18);
                        equipmentSection.setAlignment(Pos.CENTER_LEFT);
                        equipmentSection.setPadding(new Insets(6, 0, 6, 0));

                        for (EquipmentDataStore.EquipmentItem item : recommendedList) {
                                VBox card = createEquipmentCard(
                                        item.imagePath,
                                        item.name,
                                        "₹" + item.pricePerDay + " / day",
                                        item.category,
                                        item.rating,
                                        item.location
                                );
                                equipmentSection.getChildren().add(card);
                        }

                        Text viewMore = new Text("View More →");
                        viewMore.setStyle(
                                        "-fx-fill: #16A34A;" +
                                                        "-fx-font-size: 14px;" +
                                                        "-fx-font-weight: bold;" +
                                                        "-fx-cursor: hand;");

                        viewMore.setOnMouseClicked(event -> {
                                LeftSideBar.setActiveButton(LeftSideBar.equipmentBtn1, LeftSideBar.navigationButtons);
                                ProfileManagement.setHeaderTitle("Browse Equipment ⚒", "Find and rent the right machinery for your farm");
                                FarmerDashboard.borderPane.setCenter(BrowseEquip.getBrowseEquip());
                        });

                        equipmentSection.getChildren().add(viewMore);

                        ScrollPane equipmentScroll = new ScrollPane();
                        equipmentScroll.setContent(equipmentSection);
                        equipmentScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
                        equipmentScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
                        equipmentScroll.setFitToHeight(true);
                        equipmentScroll.setStyle("-fx-background-color: transparent; -fx-background: transparent; -fx-border-color: transparent;");

                        recommendedNode = equipmentScroll;
                }

                // ----------------- ACTIVE BOOKING SECTION -----------------
                Text activeBookingTitle = new Text("Active Booking");

                activeBookingTitle.setStyle(
                                "-fx-font-family: 'Poppins';" +
                                                "-fx-font-size: 20px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-fill: #1B4332;");

                Text viewMore2 = new Text("View More →");

                viewMore2.setStyle(
                                "-fx-font-family: 'Poppins';" +
                                                "-fx-font-size: 13px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-fill: #16A34A;" +
                                                "-fx-cursor: hand;");

                viewMore2.setOnMouseClicked(e -> {
                        LeftSideBar.setActiveButton(LeftSideBar.bookingBtn1, LeftSideBar.navigationButtons);
                        ProfileManagement.setHeaderTitle("My Bookings 📅", "Track and manage your equipment rentals & schedules");
                        MyBookings obj = new MyBookings();
                        FarmerDashboard.borderPane.setCenter(obj.getBooking(FarmerDashboard.root));
                });

                HBox activeBookingHeader = new HBox();
                activeBookingHeader.setAlignment(Pos.CENTER_LEFT);

                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                activeBookingHeader.getChildren().addAll(
                                activeBookingTitle,
                                spacer,
                                viewMore2);

                List<BookingDataStore.BookingItem> activeList = BookingDataStore.getActiveBookings();
                VBox activeBookingSection = new VBox(12);

                if (activeList.isEmpty()) {
                        VBox emptyActiveBox = new VBox(6);
                        emptyActiveBox.setPadding(new Insets(14, 18, 14, 18));
                        emptyActiveBox.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 12; -fx-border-color: #D1E7DD; -fx-border-width: 1.2; -fx-border-radius: 12;");
                        Text emptyActiveText = new Text("No active equipment rentals at the moment.");
                        emptyActiveText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-fill: #6B7280;");
                        emptyActiveBox.getChildren().add(emptyActiveText);
                        activeBookingSection.getChildren().addAll(activeBookingHeader, emptyActiveBox);
                } else {
                        BookingDataStore.BookingItem firstActive = activeList.get(0);
                        VBox activeBookingCard = createActiveBookingCard(
                                        firstActive.equipmentName,
                                        firstActive.startDate + " → " + firstActive.endDate,
                                        firstActive.status);
                        activeBookingSection.getChildren().addAll(activeBookingHeader, activeBookingCard);
                }

                // ----------------- RECENT BOOKINGS SECTION -----------------
                Text recentBookingTitle = new Text("Recent Bookings");

                recentBookingTitle.setStyle(
                                "-fx-font-family: 'Poppins';" +
                                                "-fx-font-size: 20px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-fill: #1B4332;");

                List<BookingDataStore.BookingItem> allBookings = BookingDataStore.getAllBookings();
                Node recentBookingNode;

                if (allBookings.isEmpty()) {
                        VBox emptyRecentBox = new VBox(6);
                        emptyRecentBox.setPadding(new Insets(14, 18, 14, 18));
                        emptyRecentBox.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 12; -fx-border-color: #D1E7DD; -fx-border-width: 1.2; -fx-border-radius: 12;");
                        Text emptyRecentText = new Text("No recent bookings recorded.");
                        emptyRecentText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-fill: #6B7280;");
                        emptyRecentBox.getChildren().add(emptyRecentText);
                        recentBookingNode = emptyRecentBox;
                } else {
                        VBox recentRows = new VBox(8);
                        for (int i = 0; i < Math.min(3, allBookings.size()); i++) {
                                BookingDataStore.BookingItem b = allBookings.get(i);
                                recentRows.getChildren().add(createBookingRow(b.equipmentName, b.startDate, b.status));
                        }
                        recentBookingNode = recentRows;
                }

                VBox centerContent = new VBox(
                                18,

                                weatherCard,
                                searchBox,
                                cards,
                                recommendedTitle,
                                recommendedNode,
                                activeBookingSection,
                                recentBookingTitle,
                                recentBookingNode);

                centerContent.setPadding(new Insets(14, 30, 30, 30));
                centerContent.setStyle("-fx-background-color: #F4F9F4;");

                ScrollPane scrollPane = new ScrollPane();

                scrollPane.setContent(centerContent);

                scrollPane.setFitToWidth(true);
                scrollPane.setFitToHeight(false);
                scrollPane.setPannable(false);
                scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
                scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

                scrollPane.hvalueProperty().addListener((obs, oldVal, newVal) -> {
                        if (newVal.doubleValue() != 0.0) {
                                scrollPane.setHvalue(0.0);
                        }
                });

                scrollPane.setStyle("-fx-background-color: #F4F9F4; -fx-background: #F4F9F4;");
        
                FarmerDashboard.borderPane.setCenter(scrollPane);

                return scrollPane;
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

                // Middle: Farming advisory badge
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
                                        throw new RuntimeException(
                                                        "API returned HTTP "
                                                                        + response.statusCode());
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
                iconText.setStyle("-fx-font-size: 20px;");

                StackPane iconBadge = new StackPane(iconText);
                iconBadge.setPrefSize(40, 40);
                iconBadge.setMinSize(40, 40);
                iconBadge.setMaxSize(40, 40);
                iconBadge.setStyle(
                                "-fx-background-color: #E8F5E9;" +
                                "-fx-background-radius: 10px;" +
                                "-fx-border-color: #C8E6C9;" +
                                "-fx-border-radius: 10px;" +
                                "-fx-border-width: 1px;");

                Text titleText = new Text(title);

                titleText.setStyle(
                                "-fx-font-size: 13px;" +
                                                "-fx-font-family: 'Poppins';" +
                                                "-fx-font-weight: 600;" +
                                                "-fx-fill: #4B5563;");

                Text valueText = new Text(value);

                valueText.setStyle(
                                "-fx-font-size: 26px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-font-family: 'Poppins';" +
                                                "-fx-fill: #1B4332;");

                VBox card = new VBox(
                                10,
                                iconBadge,
                                titleText,
                                valueText);

                card.setPrefWidth(220);
                card.setPrefHeight(142);

                card.setPadding(
                                new Insets(16, 18, 16, 18));

                // Normal style
                String normalStyle = "-fx-background-color: #FFFFFF;" +
                                "-fx-background-radius: 14;" +
                                "-fx-border-color: #D1E7DD;" +
                                "-fx-border-width: 1.2;" +
                                "-fx-border-radius: 14;";

                // Hover style
                String hoverStyle = "-fx-background-color: #FFFFFF;" +
                                "-fx-background-radius: 14;" +
                                "-fx-border-color: #2D6A4F;" +
                                "-fx-border-width: 1.5;" +
                                "-fx-border-radius: 14;";

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
                        String price,
                        String category,
                        String rating,
                        String location) {

                Image equipmentImage = null;
                try {
                        if (imagePath != null && !imagePath.isEmpty()) {
                                equipmentImage = new Image(imagePath);
                        }
                } catch (Exception ignored) {}

                ImageView imageView = new ImageView();
                if (equipmentImage != null && !equipmentImage.isError()) {
                        imageView.setImage(equipmentImage);
                } else {
                        try {
                                imageView.setImage(new Image("file:farm/src/main/resources/assets/Images/tractor.png"));
                        } catch (Exception ignored) {}
                }

                imageView.setFitWidth(196);
                imageView.setFitHeight(110);
                imageView.setPreserveRatio(true);
                imageView.setSmooth(true);

                StackPane imageBox = new StackPane(imageView);
                imageBox.setPrefHeight(110);
                imageBox.setMaxWidth(Double.MAX_VALUE);
                imageBox.setStyle("-fx-background-color: #E8F5E9; -fx-background-radius: 10;");

                Text nameText = new Text(equipmentName);
                nameText.setStyle(
                                "-fx-font-family: 'Poppins';" +
                                                "-fx-font-size: 15px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-fill: #1B4332;");

                Text priceText = new Text(price);
                priceText.setStyle(
                                "-fx-font-family: 'Poppins';" +
                                                "-fx-font-size: 13px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-fill: #2D6A4F;");

                Button viewButton = new Button("View Details");
                viewButton.setPrefWidth(196);
                viewButton.setPrefHeight(35);

                viewButton.setStyle(
                                "-fx-background-color: linear-gradient(to right, #2D6A4F, #40916C);" +
                                                "-fx-background-radius: 8;" +
                                                "-fx-text-fill: white;" +
                                                "-fx-font-family: 'Poppins';" +
                                                "-fx-font-size: 12px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-cursor: hand;");

                viewButton.setOnMouseEntered(e -> {
                        viewButton.setStyle(
                                        "-fx-background-color: linear-gradient(to right, #1B4332, #2D6A4F);" +
                                                        "-fx-background-radius: 8;" +
                                                        "-fx-text-fill: white;" +
                                                        "-fx-font-family: 'Poppins';" +
                                                        "-fx-font-size: 12px;" +
                                                        "-fx-font-weight: bold;" +
                                                        "-fx-cursor: hand;");
                });

                viewButton.setOnMouseExited(e -> {
                        viewButton.setStyle(
                                        "-fx-background-color: linear-gradient(to right, #2D6A4F, #40916C);" +
                                                        "-fx-background-radius: 8;" +
                                                        "-fx-text-fill: white;" +
                                                        "-fx-font-family: 'Poppins';" +
                                                        "-fx-font-size: 12px;" +
                                                        "-fx-font-weight: bold;" +
                                                        "-fx-cursor: hand;");
                });

                Runnable openDetails = () -> {
                        LeftSideBar.setActiveButton(LeftSideBar.equipmentBtn1, LeftSideBar.navigationButtons);
                        ProfileManagement.setHeaderTitle("Equipment Details ⚒", "Detailed machinery specifications, rent & operator options");
                        EquipmentDetailPage detailPage = new EquipmentDetailPage(
                                equipmentName,
                                category != null ? category : "Agricultural Equipment",
                                price.replace("₹", "").replace("/ day", "").trim(),
                                rating != null ? rating : "4.8",
                                location != null ? location : "Pune, Maharashtra",
                                imagePath,
                                () -> {
                                        LeftSideBar.setActiveButton(LeftSideBar.dashboardBtn1, LeftSideBar.navigationButtons);
                                        ProfileManagement.updateHeaderGreeting();
                                        FarmerDashboard.borderPane.setCenter(Dashboard.getPage());
                                }
                        );
                        FarmerDashboard.borderPane.setCenter(detailPage.getDetailPage());
                };

                viewButton.setOnAction(e -> openDetails.run());

                VBox card = new VBox(
                                10,
                                imageBox,
                                nameText,
                                priceText,
                                viewButton);

                card.setPrefWidth(220);
                card.setPrefHeight(210);
                card.setCursor(javafx.scene.Cursor.HAND);
                card.setOnMouseClicked(e -> openDetails.run());

                card.setPadding(new Insets(12));

                card.setStyle(
                                "-fx-background-color: #FFFFFF;" +
                                                "-fx-background-radius: 14;" +
                                                "-fx-border-color: #D1E7DD;" +
                                                "-fx-border-width: 1.2;" +
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
                                                "-fx-fill: #1B4332;");

                Text dateText = new Text(date);

                dateText.setStyle(
                                "-fx-font-family: 'Poppins';" +
                                                "-fx-font-size: 13px;" +
                                                "-fx-fill: #4B5563;");

                Text statusText = new Text(status);

                statusText.setStyle(
                                "-fx-font-family: 'Poppins';" +
                                                "-fx-font-size: 13px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-fill: #2E7D32;");

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
                row.setCursor(javafx.scene.Cursor.HAND);

                row.setStyle(
                                "-fx-background-color: #FFFFFF;" +
                                                "-fx-background-radius: 10;" +
                                                "-fx-border-color: #D1E7DD;" +
                                                "-fx-border-width: 1.2;" +
                                                "-fx-border-radius: 10;");

                row.setOnMouseClicked(e -> {
                        LeftSideBar.setActiveButton(LeftSideBar.bookingBtn1, LeftSideBar.navigationButtons);
                        ProfileManagement.setHeaderTitle("My Bookings 📅", "Track and manage your equipment rentals & schedules");
                        MyBookings obj = new MyBookings();
                        FarmerDashboard.borderPane.setCenter(obj.getBooking(FarmerDashboard.root));
                });

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
                                                "-fx-fill: #1B4332;");

                Text dateText = new Text(bookingDate);

                dateText.setStyle(
                                "-fx-font-family: 'Poppins';" +
                                                "-fx-font-size: 13px;" +
                                                "-fx-fill: #4B5563;");

                Text statusText = new Text("Status: " + status);

                statusText.setStyle(
                                "-fx-font-family: 'Poppins';" +
                                                "-fx-font-size: 13px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-fill: #2E7D32;");

                Button viewButton = new Button("View Booking");

                viewButton.setPrefWidth(130);
                viewButton.setPrefHeight(35);

                viewButton.setStyle(
                                "-fx-background-color: linear-gradient(to right, #2D6A4F, #40916C);" +
                                                "-fx-background-radius: 8;" +
                                                "-fx-text-fill: white;" +
                                                "-fx-font-family: 'Poppins';" +
                                                "-fx-font-size: 12px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-cursor: hand;");

                viewButton.setOnMouseEntered(e -> viewButton.setStyle(
                                "-fx-background-color: linear-gradient(to right, #1B4332, #2D6A4F);" +
                                                "-fx-background-radius: 8;" +
                                                "-fx-text-fill: white;" +
                                                "-fx-font-family: 'Poppins';" +
                                                "-fx-font-size: 12px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-cursor: hand;"));

                viewButton.setOnMouseExited(e -> viewButton.setStyle(
                                "-fx-background-color: linear-gradient(to right, #2D6A4F, #40916C);" +
                                                "-fx-background-radius: 8;" +
                                                "-fx-text-fill: white;" +
                                                "-fx-font-family: 'Poppins';" +
                                                "-fx-font-size: 12px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-cursor: hand;"));

                Runnable openBooking = () -> {
                        LeftSideBar.setActiveButton(LeftSideBar.bookingBtn1, LeftSideBar.navigationButtons);
                        ProfileManagement.setHeaderTitle("My Bookings 📅", "Track and manage your equipment rentals & schedules");
                        MyBookings obj = new MyBookings();
                        FarmerDashboard.borderPane.setCenter(obj.getBooking(FarmerDashboard.root));
                };

                viewButton.setOnAction(e -> openBooking.run());

                VBox card = new VBox(
                                10,
                                equipmentText,
                                dateText,
                                statusText,
                                viewButton);

                card.setPadding(
                                new Insets(18));

                card.setPrefHeight(150);
                card.setCursor(javafx.scene.Cursor.HAND);
                card.setOnMouseClicked(e -> openBooking.run());

                card.setStyle(
                                "-fx-background-color: #FFFFFF;" +
                                                "-fx-background-radius: 14;" +
                                                "-fx-border-color: #D1E7DD;" +
                                                "-fx-border-width: 1.2;" +
                                                "-fx-border-radius: 14;");

                return card;
        }

        
}