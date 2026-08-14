package com.desgin.view.farmer.harshal;

import javax.swing.Scrollable;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class MyBookings {

        private VBox bookingList;

        private Button allBtn;
        private Button upcomingBtn;
        private Button activeBtn;
        private Button completedBtn;
        private Button cancelledBtn;

        private StackPane innerRoot;

        public VBox getBooking(StackPane root) {

                innerRoot = root;
                HBox header = new HBox();
                header.setAlignment(Pos.CENTER_LEFT);

                VBox titleBox = new VBox(5);

                Label title = new Label("My Bookings");
                title.setStyle("-fx-font-size: 28px;" + "-fx-font-weight: bold;" + "-fx-text-fill: #1B4332;");

                Label subtitle = new Label("Track and manage all your equipment rentals.");
                subtitle.setStyle("-fx-font-size: 14px;" + "-fx-text-fill: #6B7280;");

                titleBox.getChildren().addAll(title, subtitle);

                TextField searchField = new TextField();
                searchField.setPromptText("Search bookings...");
                searchField.setPrefWidth(230);
                searchField.setPrefHeight(40);
                searchField.textProperty().addListener((observable, oldValue, newValue) -> {

                        searchBookings(newValue);
                });

                searchField.setStyle(
                                "-fx-background-color: white;" +
                                                "-fx-background-radius: 8;" +
                                                "-fx-border-color: #D9E2DC;" +
                                                "-fx-border-radius: 8;" +
                                                "-fx-padding: 0 12 0 12;" +
                                                "-fx-font-size: 13px;");

                HBox.setHgrow(titleBox, Priority.ALWAYS);

                header.getChildren().addAll(titleBox, searchField);

                HBox summaryCards = new HBox(15);

                summaryCards.getChildren().addAll(
                                createSummaryCard("Total Bookings", "12"),
                                createSummaryCard("Upcoming", "2"),
                                createSummaryCard("Active", "1"),
                                createSummaryCard("Completed", "9"));

                HBox tabs = new HBox(10);
                tabs.setAlignment(Pos.CENTER_LEFT);

                allBtn = createTabButton("All", true);
                upcomingBtn = createTabButton("Upcoming", false);
                activeBtn = createTabButton("Active", false);
                completedBtn = createTabButton("Completed", false);
                cancelledBtn = createTabButton("Cancelled", false);

                allBtn.setOnAction(e -> showAllBookings());
                upcomingBtn.setOnAction(e -> showUpcomingBookings());
                activeBtn.setOnAction(e -> showActiveBookings());
                completedBtn.setOnAction(e -> showCompletedBookings());
                cancelledBtn.setOnAction(e -> showCancelledBookings());

                tabs.getChildren().addAll(
                                allBtn,
                                upcomingBtn,
                                activeBtn,
                                completedBtn,
                                cancelledBtn);

                bookingList = new VBox(15);

                bookingList.getChildren().addAll(
                                createBookingCard(
                                                "John Deere Tractor",
                                                "Heavy Duty Tractor",
                                                "BK00123",
                                                "15 Aug 2026",
                                                "18 Aug 2026",
                                                "₹2,500/day",
                                                "₹7,500",
                                                "ACTIVE",
                                                null,
                                                innerRoot),

                                createBookingCard(
                                                "Mahindra Rotavator",
                                                "Agricultural Equipment",
                                                "BK00119",
                                                "05 Aug 2026",
                                                "07 Aug 2026",
                                                "₹1,500/day",
                                                "₹3,000",
                                                "COMPLETED",
                                                null,
                                                innerRoot),

                                createBookingCard(
                                                "Swaraj Cultivator",
                                                "Farm Cultivation Equipment",
                                                "BK00115",
                                                "20 Jul 2026",
                                                "22 Jul 2026",
                                                "₹1,200/day",
                                                "₹3,600",
                                                "CANCELLED",
                                                null,
                                                innerRoot));

                VBox mainContent = new VBox(20);
                mainContent.setPadding(new Insets(25));
                mainContent.setStyle("-fx-background-color: #F5F7F5;");
                ;

                ScrollPane scrollPane = new ScrollPane();
                scrollPane.setContent(bookingList);
                scrollPane.setFitToWidth(true);
                scrollPane.setFitToHeight(false);
                scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
                scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
                scrollPane.setStyle("-fx-background-color: transparent;" + "-fx-background: transparent;");

                VBox.setVgrow(scrollPane, Priority.ALWAYS);

                mainContent.getChildren().addAll(
                                header,
                                summaryCards,
                                tabs,
                                scrollPane);

                return mainContent;
        }

        private VBox createSummaryCard(String title, String value) {

                VBox card = new VBox(8);
                card.setPadding(new Insets(18));
                card.setPrefHeight(100);

                card.setStyle("-fx-background-color: white;" + "-fx-background-radius: 12;"
                                + "-fx-border-color: #E2E8E4;" + "-fx-border-radius: 12;");

                Label titleLabel = new Label(title);
                titleLabel.setStyle("-fx-font-size: 13px;" + "-fx-text-fill: #6B7280;");

                Label valueLabel = new Label(value);
                valueLabel.setStyle("-fx-font-size: 25px;" + "-fx-font-weight: bold;" + "-fx-text-fill: #1B4332;");

                card.getChildren().addAll(titleLabel, valueLabel);

                HBox.setHgrow(card, Priority.ALWAYS);

                return card;
        }

        private Button createTabButton(String text, boolean active) {

                Button button = new Button(text);
                button.setPrefHeight(38);

                if (active) {

                        button.setStyle("-fx-background-color: #1B4332;" + "-fx-text-fill: white;"
                                        + "-fx-font-weight: bold;" + "-fx-background-radius: 8;"
                                        + "-fx-padding: 0 18 0 18;");
                } else {

                        button.setStyle("-fx-background-color: transparent;" + "-fx-text-fill: #4B5563;"
                                        + "-fx-font-weight: bold;" + "-fx-background-radius: 8;"
                                        + "-fx-padding: 0 18 0 18;");
                }

                return button;
        }

        private void showAllBookings() {

                bookingList.getChildren().clear();

                bookingList.getChildren().addAll(

                                createBookingCard(
                                                "John Deere Tractor",
                                                "Heavy Duty Tractor",
                                                "BK00123",
                                                "15 Aug 2026",
                                                "18 Aug 2026",
                                                "₹2,500/day",
                                                "₹7,500",
                                                "ACTIVE",
                                                null,
                                                innerRoot),

                                createBookingCard(
                                                "Mahindra Rotavator",
                                                "Agricultural Equipment",
                                                "BK00119",
                                                "05 Aug 2026",
                                                "07 Aug 2026",
                                                "₹1,500/day",
                                                "₹3,000",
                                                "COMPLETED",
                                                null,
                                                innerRoot),

                                createBookingCard(
                                                "Swaraj Cultivator",
                                                "Farm Cultivation Equipment",
                                                "BK00115",
                                                "20 Jul 2026",
                                                "22 Jul 2026",
                                                "₹1,200/day",
                                                "₹3,600",
                                                "CANCELLED",
                                                null,
                                                innerRoot));

                setActiveTab(allBtn);
        }

        private void showActiveBookings() {

                bookingList.getChildren().clear();

                bookingList.getChildren().add(
                                createBookingCard(
                                                "John Deere Tractor",
                                                "Heavy Duty Tractor",
                                                "BK00123",
                                                "15 Aug 2026",
                                                "18 Aug 2026",
                                                "₹2,500/day",
                                                "₹7,500",
                                                "ACTIVE",
                                                null,
                                                innerRoot));

                setActiveTab(activeBtn);
        }

        private void showCompletedBookings() {

                bookingList.getChildren().clear();

                bookingList.getChildren().add(
                                createBookingCard(
                                                "Mahindra Rotavator",
                                                "Agricultural Equipment",
                                                "BK00119",
                                                "05 Aug 2026",
                                                "07 Aug 2026",
                                                "₹1,500/day",
                                                "₹3,000",
                                                "COMPLETED",
                                                null,
                                                innerRoot));

                setActiveTab(completedBtn);
        }

        private void showCancelledBookings() {

                bookingList.getChildren().clear();

                bookingList.getChildren().add(
                                createBookingCard(
                                                "Swaraj Cultivator",
                                                "Farm Cultivation Equipment",
                                                "BK00115",
                                                "20 Jul 2026",
                                                "22 Jul 2026",
                                                "₹1,200/day",
                                                "₹3,600",
                                                "CANCELLED",
                                                null,
                                                innerRoot));

                setActiveTab(cancelledBtn);
        }

        private void showUpcomingBookings() {

                bookingList.getChildren().clear();

                Label emptyLabel = new Label("No upcoming bookings.");

                emptyLabel.setStyle(
                                "-fx-font-size: 15px;" +
                                                "-fx-text-fill: #6B7280;");

                bookingList.getChildren().add(emptyLabel);

                setActiveTab(upcomingBtn);
        }

        private void setActiveTab(Button selectedButton) {

                Button[] buttons = {
                                allBtn,
                                upcomingBtn,
                                activeBtn,
                                completedBtn,
                                cancelledBtn
                };

                for (Button button : buttons) {

                        button.setStyle(
                                        "-fx-background-color: transparent;" +
                                                        "-fx-text-fill: #4B5563;" +
                                                        "-fx-font-weight: bold;" +
                                                        "-fx-background-radius: 8;" +
                                                        "-fx-padding: 0 18 0 18;");
                }

                selectedButton.setStyle(
                                "-fx-background-color: #1B4332;" +
                                                "-fx-text-fill: white;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-background-radius: 8;" +
                                                "-fx-padding: 0 18 0 18;");
        }

        private HBox createBookingCard(
                        String equipmentName,
                        String equipmentType,
                        String bookingId,
                        String startDate,
                        String endDate,
                        String pricePerDay,
                        String totalPrice,
                        String status,
                        String imagePath,
                        StackPane root) {

                HBox card = new HBox(20);

                card.setPadding(new Insets(18));
                card.setAlignment(Pos.CENTER_LEFT);

                card.setStyle(
                                "-fx-background-color: white;" +
                                                "-fx-background-radius: 12;" +
                                                "-fx-border-color: #E2E8E4;" +
                                                "-fx-border-radius: 12;");

                VBox imageBox = new VBox();
                imageBox.setPrefWidth(140);
                imageBox.setPrefHeight(120);
                imageBox.setAlignment(Pos.CENTER);

                imageBox.setStyle(
                                "-fx-background-color: #E8F1EB;" +
                                                "-fx-background-radius: 10;");

                Label imagePlaceholder = new Label("Equipment");
                imagePlaceholder.setStyle(
                                "-fx-text-fill: #52796F;" +
                                                "-fx-font-weight: bold;");

                imageBox.getChildren().add(imagePlaceholder);

                VBox information = new VBox(8);

                Label nameLabel = new Label(equipmentName);
                nameLabel.setStyle(
                                "-fx-font-size: 18px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: #1B4332;");

                Label typeLabel = new Label(equipmentType);
                typeLabel.setStyle(
                                "-fx-font-size: 13px;" +
                                                "-fx-text-fill: #6B7280;");

                Label bookingLabel = new Label(
                                "Booking ID: " + bookingId);
                bookingLabel.setStyle(
                                "-fx-font-size: 12px;" +
                                                "-fx-text-fill: #6B7280;");

                Label dateLabel = new Label(
                                startDate + "  →  " + endDate);
                dateLabel.setStyle(
                                "-fx-font-size: 13px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: #374151;");

                information.getChildren().addAll(
                                nameLabel,
                                typeLabel,
                                bookingLabel,
                                dateLabel);

                HBox.setHgrow(information, Priority.ALWAYS);

                VBox priceBox = new VBox(5);
                priceBox.setAlignment(Pos.CENTER_RIGHT);

                Label priceLabel = new Label(pricePerDay);
                priceLabel.setStyle(
                                "-fx-font-size: 12px;" +
                                                "-fx-text-fill: #6B7280;");

                Label totalLabel = new Label(totalPrice);
                totalLabel.setStyle(
                                "-fx-font-size: 20px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: #1B4332;");

                priceBox.getChildren().addAll(
                                priceLabel,
                                totalLabel);

                Label statusLabel = new Label(status);
                statusLabel.setPadding(
                                new Insets(6, 12, 6, 12));

                if (status.equals("ACTIVE")) {

                        statusLabel.setStyle(
                                        "-fx-background-color: #DCFCE7;" +
                                                        "-fx-text-fill: #166534;" +
                                                        "-fx-background-radius: 20;" +
                                                        "-fx-font-weight: bold;" +
                                                        "-fx-font-size: 11px;");
                } else if (status.equals("COMPLETED")) {

                        statusLabel.setStyle(
                                        "-fx-background-color: #DBEAFE;" +
                                                        "-fx-text-fill: #1D4ED8;" +
                                                        "-fx-background-radius: 20;" +
                                                        "-fx-font-weight: bold;" +
                                                        "-fx-font-size: 11px;");
                } else if (status.equals("CANCELLED")) {

                        statusLabel.setStyle(
                                        "-fx-background-color: #FEE2E2;" +
                                                        "-fx-text-fill: #B91C1C;" +
                                                        "-fx-background-radius: 20;" +
                                                        "-fx-font-weight: bold;" +
                                                        "-fx-font-size: 11px;");
                }

                Button viewButton = new Button("View Details");
                viewButton.setPrefHeight(35);
                viewButton.setStyle(
                                "-fx-background-color: #1B4332;" +
                                                "-fx-text-fill: white;" +
                                                "-fx-background-radius: 7;" +
                                                "-fx-font-weight: bold;");
                viewButton.setOnAction(e -> {

                        StackPane overlay = new StackPane();

                        overlay.setStyle(
                                        "-fx-background-color: rgba(0,0,0,0.45);");

                        BookingDetails details = new BookingDetails();

                        VBox detailsBox = details.getBookingDetails(

                                        // Close details
                                        () -> root.getChildren().remove(overlay),

                                        // Cancel booking
                                        () -> {

                                                root.getChildren().remove(overlay);

                                                showAllBookings();
                                        });

                        detailsBox.setMaxWidth(600);
                        detailsBox.setMaxHeight(650);

                        overlay.getChildren().add(detailsBox);

                        StackPane.setAlignment(
                                        detailsBox,
                                        Pos.CENTER);

                        root.getChildren().add(overlay);
                });

                VBox actionBox = new VBox(10);
                actionBox.setAlignment(Pos.CENTER_RIGHT);
                actionBox.getChildren().addAll(
                                statusLabel,
                                viewButton);

                card.getChildren().addAll(
                                imageBox,
                                information,
                                priceBox,
                                actionBox);

                return card;
        }

        private void searchBookings(String searchText) {

                bookingList.getChildren().clear();

                String search = searchText.toLowerCase().trim();

                // John Deere
                if ("John Deere Tractor".toLowerCase().contains(search) ||
                                "BK00123".toLowerCase().contains(search) ||
                                "inactive".toLowerCase().contains(search)) {

                        bookingList.getChildren().add(
                                        createBookingCard(
                                                        "John Deere Tractor",
                                                        "Heavy Duty Tractor",
                                                        "BK00123",
                                                        "15 Aug 2026",
                                                        "18 Aug 2026",
                                                        "₹2,500/day",
                                                        "₹7,500",
                                                        "inactive",
                                                        null,
                                                        innerRoot));
                }

                // Mahindra Rotavator
                if ("Mahindra Rotavator".toLowerCase().contains(search) ||
                                "BK00119".toLowerCase().contains(search) ||
                                "COMPLETED".toLowerCase().contains(search)) {

                        bookingList.getChildren().add(
                                        createBookingCard(
                                                        "Mahindra Rotavator",
                                                        "Agricultural Equipment",
                                                        "BK00119",
                                                        "05 Aug 2026",
                                                        "07 Aug 2026",
                                                        "₹1,500/day",
                                                        "₹3,000",
                                                        "COMPLETED",
                                                        null,
                                                        innerRoot));
                }

                // Swaraj Cultivator
                if ("Swaraj Cultivator".toLowerCase().contains(search) ||
                                "BK00115".toLowerCase().contains(search) ||
                                "CANCELLED".toLowerCase().contains(search)) {

                        bookingList.getChildren().add(
                                        createBookingCard(
                                                        "Swaraj Cultivator",
                                                        "Farm Cultivation Equipment",
                                                        "BK00115",
                                                        "20 Jul 2026",
                                                        "22 Jul 2026",
                                                        "₹1,200/day",
                                                        "₹3,600",
                                                        "CANCELLED",
                                                        null,
                                                        innerRoot));
                }

                // No results
                if (bookingList.getChildren().isEmpty()) {

                        Label noResult = new Label(
                                        "No bookings found.");

                        noResult.setStyle(
                                        "-fx-font-size: 15px;" +
                                                        "-fx-text-fill: #6B7280;");

                        bookingList.getChildren().add(noResult);
                }
        }
}
