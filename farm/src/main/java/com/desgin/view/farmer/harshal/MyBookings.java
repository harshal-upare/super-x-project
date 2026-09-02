package com.desgin.view.farmer.harshal;

import java.util.ArrayList;
import java.util.List;

import com.desgin.view.farmer.Swapnil.BookingDataStore;
import com.desgin.view.farmer.Swapnil.BookingDataStore.BookingItem;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class MyBookings {

    private VBox bookingList;
    private Button allBtn;
    private Button upcomingBtn;
    private Button activeBtn;
    private Button completedBtn;
    private Button cancelledBtn;
    private StackPane innerRoot;
    private Label totalStatLabel;
    private Label upcomingStatLabel;
    private Label activeStatLabel;
    private Label completedStatLabel;

    public VBox getBooking(StackPane root) {
        innerRoot = root;

        // ================= SEARCH FIELD =================
        TextField searchField = new TextField();
        searchField.setPromptText("🔍  Search by equipment, booking ID, or status...");
        searchField.setPrefWidth(420);
        searchField.setPrefHeight(44);
        searchField.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.95);" +
                "-fx-background-radius: 12px;" +
                "-fx-border-color: rgba(45, 106, 79, 0.3);" +
                "-fx-border-width: 1.2px;" +
                "-fx-border-radius: 12px;" +
                "-fx-padding: 0 14px;" +
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 13px;" +
                "-fx-text-fill: #1F2937;"
        );
        searchField.textProperty().addListener((obs, oldV, newV) -> searchBookings(newV));

        HBox header = new HBox(12, searchField);
        header.setAlignment(Pos.CENTER_LEFT);

        // ================= KPI SUMMARY CARDS =================
        VBox c1 = createSummaryCard("Total Bookings", String.valueOf(BookingDataStore.getTotalCount()), "All lifetime requests", "#1B4332");
        VBox c2 = createSummaryCard("Upcoming", String.valueOf(BookingDataStore.getPendingCount()), "Awaiting start date", "#2D6A4F");
        VBox c3 = createSummaryCard("Active On-Field", String.valueOf(BookingDataStore.getActiveCount()), "Currently in operation", "#15803D");
        VBox c4 = createSummaryCard("Completed", String.valueOf(BookingDataStore.getCompletedCount()), "Ready to review", "#B45309");

        HBox summaryCards = new HBox(14, c1, c2, c3, c4);
        summaryCards.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(c1, Priority.ALWAYS);
        HBox.setHgrow(c2, Priority.ALWAYS);
        HBox.setHgrow(c3, Priority.ALWAYS);
        HBox.setHgrow(c4, Priority.ALWAYS);

        // ================= TABS =================
        allBtn = createTabButton("All Bookings", true);
        upcomingBtn = createTabButton("Upcoming", false);
        activeBtn = createTabButton("Active", false);
        completedBtn = createTabButton("Completed", false);
        cancelledBtn = createTabButton("Cancelled", false);

        allBtn.setOnAction(e -> showAllBookings());
        upcomingBtn.setOnAction(e -> showUpcomingBookings());
        activeBtn.setOnAction(e -> showActiveBookings());
        completedBtn.setOnAction(e -> showCompletedBookings());
        cancelledBtn.setOnAction(e -> showCancelledBookings());

        HBox tabs = new HBox(10, allBtn, upcomingBtn, activeBtn, completedBtn, cancelledBtn);
        tabs.setAlignment(Pos.CENTER_LEFT);

        // ================= BOOKING LIST & SCROLLER =================
        bookingList = new VBox(14);
        showAllBookings();

        ScrollPane scrollPane = new ScrollPane(bookingList);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(false);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        VBox mainContent = new VBox(18, header, summaryCards, tabs, scrollPane);
        mainContent.setPadding(new Insets(20, 30, 35, 30));
        mainContent.setStyle("-fx-background-color: transparent;");

        return mainContent;
    }

    private VBox createSummaryCard(String title, String value, String subText, String valColor) {
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-font-weight: bold; -fx-text-fill: #1B4332;");

        Label valueLabel = new Label(value);
        valueLabel.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: " + valColor + ";");

        Text sub = new Text(subText);
        sub.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-fill: #5C6B5F;");

        VBox card = new VBox(3, titleLabel, valueLabel, sub);
        card.setPadding(new Insets(14, 18, 14, 18));
        card.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.95);" +
                "-fx-background-radius: 14px;" +
                "-fx-border-color: rgba(45, 106, 79, 0.25);" +
                "-fx-border-width: 1.2px;" +
                "-fx-border-radius: 14px;" +
                "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.04), 8, 0, 0, 2);"
        );
        return card;
    }

    private Button createTabButton(String text, boolean active) {
        Button button = new Button(text);
        button.setPrefHeight(38);
        styleTabPill(button, active);
        return button;
    }

    private void styleTabPill(Button btn, boolean active) {
        if (active) {
            btn.setStyle(
                    "-fx-background-color: linear-gradient(to right, #2D6A4F, #40916C);" +
                    "-fx-text-fill: white;" +
                    "-fx-font-family: 'Poppins';" +
                    "-fx-font-size: 13px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-background-radius: 20px;" +
                    "-fx-padding: 8px 20px;" +
                    "-fx-cursor: hand;" +
                    "-fx-effect: dropshadow(gaussian, rgba(45, 106, 79, 0.25), 8, 0, 0, 2);"
            );
        } else {
            btn.setStyle(
                    "-fx-background-color: rgba(255, 255, 255, 0.95);" +
                    "-fx-text-fill: #4B5563;" +
                    "-fx-font-family: 'Poppins';" +
                    "-fx-font-size: 13px;" +
                    "-fx-font-weight: 500;" +
                    "-fx-background-radius: 20px;" +
                    "-fx-border-color: rgba(45, 106, 79, 0.25);" +
                    "-fx-border-width: 1.2px;" +
                    "-fx-border-radius: 20px;" +
                    "-fx-padding: 8px 20px;" +
                    "-fx-cursor: hand;"
            );
        }
    }

    private void renderBookings(List<BookingItem> list, String emptyMessage) {
        bookingList.getChildren().clear();
        if (list.isEmpty()) {
            VBox emptyBox = new VBox(10);
            emptyBox.setAlignment(Pos.CENTER);
            emptyBox.setPadding(new Insets(50));
            emptyBox.setStyle(
                    "-fx-background-color: rgba(255, 255, 255, 0.95);" +
                    "-fx-background-radius: 14px;" +
                    "-fx-border-color: rgba(45, 106, 79, 0.25);" +
                    "-fx-border-width: 1.2px;" +
                    "-fx-border-radius: 14px;" +
                    "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.04), 8, 0, 0, 2);"
            );
            Label icon = new Label("📅");
            icon.setStyle("-fx-font-size: 36px;");
            Label emptyLabel = new Label(emptyMessage);
            emptyLabel.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #1B4332;");
            Label sub = new Label("Browse the machinery catalog and rent farm equipment to see your bookings here.");
            sub.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-text-fill: #4B5563;");
            emptyBox.getChildren().addAll(icon, emptyLabel, sub);
            bookingList.getChildren().add(emptyBox);
        } else {
            for (BookingItem item : list) {
                bookingList.getChildren().add(
                        createBookingCard(
                                item.equipmentName,
                                item.category,
                                item.bookingId,
                                item.startDate,
                                item.endDate,
                                item.dailyRate,
                                item.totalAmount,
                                item.status,
                                item.imagePath,
                                innerRoot
                        )
                );
            }
        }
    }

    private void showAllBookings() {
        renderBookings(BookingDataStore.getAllBookings(), "No bookings found");
        setActiveTab(allBtn);
    }

    private void showActiveBookings() {
        renderBookings(BookingDataStore.getActiveBookings(), "No active bookings on-field");
        setActiveTab(activeBtn);
    }

    private void showCompletedBookings() {
        renderBookings(BookingDataStore.getCompletedBookings(), "No completed bookings yet");
        setActiveTab(completedBtn);
    }

    private void showCancelledBookings() {
        List<BookingItem> cancelled = new ArrayList<>();
        for (BookingItem b : BookingDataStore.getAllBookings()) {
            if ("CANCELLED".equalsIgnoreCase(b.status)) cancelled.add(b);
        }
        renderBookings(cancelled, "No cancelled bookings");
        setActiveTab(cancelledBtn);
    }

    private void showUpcomingBookings() {
        renderBookings(BookingDataStore.getPendingBookings(), "No upcoming bookings");
        setActiveTab(upcomingBtn);
    }

    private void searchBookings(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            showAllBookings();
            return;
        }
        String q = keyword.trim().toLowerCase();
        List<BookingItem> filtered = new ArrayList<>();
        for (BookingItem item : BookingDataStore.getAllBookings()) {
            if (item.equipmentName.toLowerCase().contains(q)
                    || item.bookingId.toLowerCase().contains(q)
                    || item.category.toLowerCase().contains(q)
                    || item.status.toLowerCase().contains(q)) {
                filtered.add(item);
            }
        }
        renderBookings(filtered, "No bookings matching '" + keyword + "'");
    }

    private void setActiveTab(Button selectedButton) {
        for (Button btn : new Button[]{allBtn, upcomingBtn, activeBtn, completedBtn, cancelledBtn}) {
            if (btn != null) styleTabPill(btn, btn == selectedButton);
        }
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
        card.setPadding(new Insets(16, 20, 16, 20));
        card.setAlignment(Pos.CENTER_LEFT);
        card.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.95);" +
                "-fx-background-radius: 14px;" +
                "-fx-border-color: rgba(45, 106, 79, 0.25);" +
                "-fx-border-width: 1.2px;" +
                "-fx-border-radius: 14px;" +
                "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.04), 8, 0, 0, 2);"
        );

        // Machinery Icon Box
        String iconChar = equipmentType.contains("Harvester") ? "🌾" :
                (equipmentType.contains("Drone") ? "🚁" :
                (equipmentType.contains("Rotavator") ? "⚙️" :
                (equipmentType.contains("Cultivator") ? "🌱" : "🚜")));

        VBox imageBox = new VBox();
        imageBox.setPrefWidth(90);
        imageBox.setPrefHeight(90);
        imageBox.setAlignment(Pos.CENTER);
        imageBox.setStyle(
                "-fx-background-color: #E8F5E9;" +
                "-fx-background-radius: 12px;" +
                "-fx-border-color: rgba(45, 106, 79, 0.2);" +
                "-fx-border-radius: 12px;"
        );

        Label iconLabel = new Label(iconChar);
        iconLabel.setStyle("-fx-font-size: 38px;");
        imageBox.getChildren().add(iconLabel);

        // Information Column
        VBox infoBox = new VBox(5);
        Label nameLabel = new Label(equipmentName);
        nameLabel.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #1B4332;");

        Label typeLabel = new Label("🏷  " + equipmentType + "  •  ID: " + bookingId);
        typeLabel.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-text-fill: #4B5563;");

        Label dateLabel = new Label("📅  " + startDate + "  →  " + endDate);
        dateLabel.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-font-weight: bold; -fx-text-fill: #2D6A4F;");

        infoBox.getChildren().addAll(nameLabel, typeLabel, dateLabel);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Price Column
        VBox priceBox = new VBox(2);
        priceBox.setAlignment(Pos.CENTER_RIGHT);

        Label rateLabel = new Label(pricePerDay + " / day");
        rateLabel.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-text-fill: #5C6B5F;");

        Label totalLabel = new Label("Total: " + totalPrice);
        totalLabel.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #1B4332;");

        priceBox.getChildren().addAll(rateLabel, totalLabel);

        // Status Badge
        Label statusLabel = new Label(status);
        String stBg = "#E8F5E9";
        String stColor = "#15803D";
        if ("PENDING".equalsIgnoreCase(status)) {
            stBg = "#FFF3E0";
            stColor = "#E65100";
        } else if ("CANCELLED".equalsIgnoreCase(status)) {
            stBg = "#FEE2E2";
            stColor = "#B91C1C";
        }
        statusLabel.setStyle(
                "-fx-background-color: " + stBg + ";" +
                "-fx-text-fill: " + stColor + ";" +
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 11px;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 4px 12px;" +
                "-fx-background-radius: 12px;"
        );

        // Action Buttons
        Button viewButton = new Button("View Details");
        viewButton.setPrefHeight(34);
        viewButton.setStyle(
                "-fx-background-color: linear-gradient(to right, #2D6A4F, #40916C);" +
                "-fx-text-fill: white;" +
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 12px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 8px;" +
                "-fx-cursor: hand;" +
                "-fx-effect: dropshadow(gaussian, rgba(45, 106, 79, 0.2), 6, 0, 0, 2);"
        );
        viewButton.setOnMouseEntered(e -> viewButton.setStyle("-fx-background-color: linear-gradient(to right, #1B4332, #2D6A4F); -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 8px; -fx-cursor: hand;"));
        viewButton.setOnMouseExited(e -> viewButton.setStyle("-fx-background-color: linear-gradient(to right, #2D6A4F, #40916C); -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 8px; -fx-cursor: hand;"));

        viewButton.setOnAction(e -> {
            StackPane overlay = new StackPane();
            overlay.setStyle("-fx-background-color: rgba(0,0,0,0.45);");

            BookingDetails details = new BookingDetails();
            VBox detailsBox = details.getBookingDetails(
                    bookingId,
                    equipmentName,
                    equipmentType,
                    startDate,
                    endDate,
                    pricePerDay,
                    totalPrice,
                    status,
                    () -> root.getChildren().remove(overlay),
                    () -> {
                        root.getChildren().remove(overlay);
                        showAllBookings();
                    }
            );

            detailsBox.setMaxWidth(600);
            detailsBox.setMaxHeight(650);
            overlay.getChildren().add(detailsBox);
            StackPane.setAlignment(detailsBox, Pos.CENTER);
            root.getChildren().add(overlay);
        });

        HBox btnRow = new HBox(8, statusLabel, viewButton);
        btnRow.setAlignment(Pos.CENTER_RIGHT);

        if ("COMPLETED".equalsIgnoreCase(status)) {
            Button rateReviewBtn = new Button("⭐ Rate & Review");
            rateReviewBtn.setPrefHeight(34);
            rateReviewBtn.setStyle(
                    "-fx-background-color: #E8F5E9;" +
                    "-fx-text-fill: #2D6A4F;" +
                    "-fx-font-family: 'Poppins';" +
                    "-fx-font-weight: bold;" +
                    "-fx-font-size: 12px;" +
                    "-fx-background-radius: 8px;" +
                    "-fx-cursor: hand;" +
                    "-fx-border-color: rgba(45, 106, 79, 0.3);" +
                    "-fx-border-radius: 8px;"
            );
            rateReviewBtn.setOnAction(e -> {
                com.desgin.view.farmer.LeftSideBar.setActiveButton(
                        com.desgin.view.farmer.LeftSideBar.reviewBtn1,
                        com.desgin.view.farmer.LeftSideBar.navigationButtons
                );
                com.desgin.view.farmer.Swapnil.FarmerDashboard.borderPane.setCenter(
                        com.desgin.view.farmer.review.ReviewRating.getReviewRatingPage(root)
                );
            });
            btnRow.getChildren().add(rateReviewBtn);
        }

        VBox rightBox = new VBox(8, priceBox, btnRow);
        rightBox.setAlignment(Pos.CENTER_RIGHT);

        card.getChildren().addAll(imageBox, infoBox, spacer, rightBox);

        card.setOnMouseEntered(e -> {
            card.setStyle(
                    "-fx-background-color: rgba(255, 255, 255, 0.95);" +
                    "-fx-background-radius: 14px;" +
                    "-fx-border-color: #2D6A4F;" +
                    "-fx-border-width: 1.5px;" +
                    "-fx-border-radius: 14px;" +
                    "-fx-effect: dropshadow(gaussian, rgba(45, 106, 79, 0.18), 12, 0, 0, 3);"
            );
            card.setTranslateY(-1.5);
        });

        card.setOnMouseExited(e -> {
            card.setStyle(
                    "-fx-background-color: rgba(255, 255, 255, 0.95);" +
                    "-fx-background-radius: 14px;" +
                    "-fx-border-color: rgba(45, 106, 79, 0.25);" +
                    "-fx-border-width: 1.2px;" +
                    "-fx-border-radius: 14px;" +
                    "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.04), 8, 0, 0, 2);"
            );
            card.setTranslateY(0);
        });

        return card;
    }
}
