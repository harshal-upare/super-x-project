package com.desgin.view.farmer.review;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.desgin.view.farmer.Swapnil.BookingDataStore;
import com.desgin.view.farmer.pratik.SearchOperator;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class ReviewRating {

    // Data model for completed booking (Equipment or Operator)
    public static class CompletedBooking {
        public String bookingId;
        public String title;
        public String subtitle;
        public String completionDate;
        public String rate;
        public String icon;
        public boolean isOperator;

        public CompletedBooking(String id, String title, String subtitle, String date, String rate, String icon, boolean isOperator) {
            this.bookingId = id;
            this.title = title;
            this.subtitle = subtitle;
            this.completionDate = date;
            this.rate = rate;
            this.icon = icon;
            this.isOperator = isOperator;
        }
    }

    // Data model for a submitted review
    public static class ReviewItem {
        public String reviewId;
        public String title;
        public String subtitle;
        public int rating;
        public String headline;
        public String comment;
        public String date;
        public List<String> tags;
        public String providerReply;
        public boolean isOperator;

        public ReviewItem(String id, String title, String subtitle, int rating, String headline, String comment, String date, List<String> tags, String reply, boolean isOperator) {
            this.reviewId = id;
            this.title = title;
            this.subtitle = subtitle;
            this.rating = rating;
            this.headline = headline;
            this.comment = comment;
            this.date = date;
            this.tags = tags != null ? tags : new ArrayList<>();
            this.providerReply = reply;
            this.isOperator = isOperator;
        }
    }

    private static final List<ReviewItem> reviewHistory = new ArrayList<>();
    private static final Set<String> reviewedBookingIds = new HashSet<>();

    private static boolean isEquipmentMode = true; // true = Equipment, false = Operator

    public static List<CompletedBooking> getPendingCompletedBookings() {
        List<CompletedBooking> list = new ArrayList<>();
        if (isEquipmentMode) {
            for (BookingDataStore.BookingItem b : BookingDataStore.getCompletedBookings()) {
                if (!reviewedBookingIds.contains(b.bookingId)) {
                    String cat = b.category != null ? b.category : "";
                    String icon = cat.contains("Harvester") ? "🌾" :
                                  (cat.contains("Drone") ? "🚁" :
                                  (cat.contains("Rotavator") ? "⚙️" :
                                  (cat.contains("Cultivator") ? "🌱" : "🚜")));
                    list.add(new CompletedBooking(b.bookingId, b.equipmentName, "Verified Machinery Provider", b.endDate, b.dailyRate, icon, false));
                }
            }
        } else {
            // Operator pending jobs
            for (SearchOperator.OperatorItem op : SearchOperator.operatorsList) {
                if (op.requestApproved && !reviewedBookingIds.contains(op.id)) {
                    String icon = op.category.equalsIgnoreCase("Harvester") ? "🌾" :
                                  (op.category.equalsIgnoreCase("Drone") ? "🚁" : "👨‍🌾");
                    list.add(new CompletedBooking(op.id, op.name + " (" + op.specialty + ")", op.locationDisplay, "Recently Completed", op.rate, icon, true));
                }
            }
        }
        return list;
    }

    private static VBox historyContainer;
    private static VBox pendingBookingsBox;
    private static Text statAvgRatingText;
    private static Text statTotalReviewsText;
    private static Text statPendingText;
    private static Label selectedBadge;
    private static CompletedBooking selectedBooking = null;
    private static int selectedStars = 5;
    private static final Set<String> activeTags = new HashSet<>();

    public static ScrollPane getReviewRatingPage(StackPane root) {

        // ================= 2 OPTIONS: EQUIPMENT vs OPERATOR =================
        Button optEquipmentBtn = new Button("🚜  Equipment Reviews");
        Button optOperatorBtn = new Button("👷  Operator Reviews");

        HBox modeSelector = new HBox(12, optEquipmentBtn, optOperatorBtn);
        modeSelector.setAlignment(Pos.CENTER_LEFT);

        // Stats summary cards
        HBox statsBox = createStatsBar();

        // ================= SUB TABS: PENDING vs HISTORY =================
        Button tabWriteBtn = new Button("✍️  Pending Reviews (" + getPendingCompletedBookings().size() + ")");
        Button tabHistoryBtn = new Button("📜  Completed Reviews / History (" + getFilteredHistory().size() + ")");

        styleTabPill(tabWriteBtn, true);
        styleTabPill(tabHistoryBtn, false);

        HBox tabBar = new HBox(12, tabWriteBtn, tabHistoryBtn);
        tabBar.setAlignment(Pos.CENTER_LEFT);

        // Tab views containers
        VBox writeReviewView = createWriteReviewView(tabHistoryBtn, tabWriteBtn);
        VBox historyView = createHistoryView();

        Runnable refreshMode = () -> {
            styleTabPill(optEquipmentBtn, isEquipmentMode);
            styleTabPill(optOperatorBtn, !isEquipmentMode);

            tabWriteBtn.setText("✍️  Pending Reviews (" + getPendingCompletedBookings().size() + ")");
            tabHistoryBtn.setText("📜  Completed Reviews / History (" + getFilteredHistory().size() + ")");

            statTotalReviewsText.setText(String.valueOf(getFilteredHistory().size()));
            statPendingText.setText(String.valueOf(getPendingCompletedBookings().size()));
            statAvgRatingText.setText(calculateAvgRating() + " ★");

            selectedBooking = null;
            if (selectedBadge != null) {
                selectedBadge.setText("Please select a completed " + (isEquipmentMode ? "equipment" : "operator") + " above");
            }
            refreshPendingBookingsList();
            refreshHistoryList();
        };

        optEquipmentBtn.setOnAction(e -> {
            isEquipmentMode = true;
            refreshMode.run();
        });

        optOperatorBtn.setOnAction(e -> {
            isEquipmentMode = false;
            refreshMode.run();
        });

        refreshMode.run();

        tabWriteBtn.setOnAction(e -> {
            styleTabPill(tabWriteBtn, true);
            styleTabPill(tabHistoryBtn, false);
            writeReviewView.setVisible(true);
            writeReviewView.setManaged(true);
            historyView.setVisible(false);
            historyView.setManaged(false);
            refreshPendingBookingsList();
        });

        tabHistoryBtn.setOnAction(e -> {
            styleTabPill(tabWriteBtn, false);
            styleTabPill(tabHistoryBtn, true);
            writeReviewView.setVisible(false);
            writeReviewView.setManaged(false);
            historyView.setVisible(true);
            historyView.setManaged(true);
            refreshHistoryList();
        });

        historyView.setVisible(false);
        historyView.setManaged(false);

        // ================= ROOT CONTAINER =================
        VBox mainContainer = new VBox(20, modeSelector, statsBox, tabBar, writeReviewView, historyView);
        mainContainer.setPadding(new Insets(20, 30, 35, 30));
        mainContainer.setMaxWidth(Double.MAX_VALUE);
        mainContainer.setStyle("-fx-background-color: transparent;");

        ScrollPane rootScroll = new ScrollPane(mainContainer);
        rootScroll.setFitToWidth(true);
        rootScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        rootScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        rootScroll.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        return rootScroll;
    }

    private static List<ReviewItem> getFilteredHistory() {
        List<ReviewItem> list = new ArrayList<>();
        for (ReviewItem r : reviewHistory) {
            if (r.isOperator == !isEquipmentMode) {
                list.add(r);
            }
        }
        return list;
    }

    private static HBox createStatsBar() {
        VBox c1 = createStatCard("⭐ Average Quality Rating", "0.0 ★", "Based on completed work", "#B45309");
        statAvgRatingText = (Text) c1.getChildren().get(1);

        VBox c2 = createStatCard("✍️ Total Reviews Given", "0", "Shared on FarmEquip", "#1B4332");
        statTotalReviewsText = (Text) c2.getChildren().get(1);

        VBox c3 = createStatCard("⏳ Pending to Rate", "0", "Awaiting your feedback", "#2D6A4F");
        statPendingText = (Text) c3.getChildren().get(1);

        HBox box = new HBox(16, c1, c2, c3);
        box.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(c1, Priority.ALWAYS);
        HBox.setHgrow(c2, Priority.ALWAYS);
        HBox.setHgrow(c3, Priority.ALWAYS);
        return box;
    }

    private static VBox createStatCard(String label, String value, String sub, String valColor) {
        Text l = new Text(label);
        l.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text v = new Text(value);
        v.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 22px; -fx-font-weight: bold; -fx-fill: " + valColor + ";");

        Text s = new Text(sub);
        s.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-fill: #4B5563;");

        VBox card = new VBox(3, l, v, s);
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

    private static void styleTabPill(Button btn, boolean active) {
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

    private static VBox createWriteReviewView(Button tabHistoryBtn, Button tabWriteBtn) {
        VBox container = new VBox(20);

        // Section 1: Choose Completed
        VBox selectorCard = new VBox(12);
        selectorCard.setPadding(new Insets(18, 22, 18, 22));
        selectorCard.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.95);" +
                "-fx-background-radius: 14px;" +
                "-fx-border-color: rgba(45, 106, 79, 0.25);" +
                "-fx-border-width: 1.2px;" +
                "-fx-border-radius: 14px;" +
                "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.04), 8, 0, 0, 2);"
        );

        Text s1Title = new Text("1. Select Completed Work to Rate");
        s1Title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 16px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text s1Sub = new Text("Note: A farmer can only rate equipment or operators once the request has been approved and marked Completed.");
        s1Sub.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #4B5563;");

        pendingBookingsBox = new VBox(10);
        refreshPendingBookingsList();

        selectorCard.getChildren().addAll(s1Title, s1Sub, pendingBookingsBox);

        // Section 2: Review Form Card
        VBox formCard = new VBox(16);
        formCard.setPadding(new Insets(20, 24, 20, 24));
        formCard.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.95);" +
                "-fx-background-radius: 14px;" +
                "-fx-border-color: rgba(45, 106, 79, 0.25);" +
                "-fx-border-width: 1.2px;" +
                "-fx-border-radius: 14px;" +
                "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.04), 8, 0, 0, 2);"
        );

        Text s2Title = new Text("2. Rate & Share Your Experience");
        s2Title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 16px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        selectedBadge = new Label("Please select a completed rental above");
        selectedBadge.setStyle("-fx-background-color: #E8F5E9; -fx-text-fill: #1B4332; -fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-font-weight: bold; -fx-padding: 6px 12px; -fx-background-radius: 8px;");

        Text starLabel = new Text("Overall Performance Rating:");
        starLabel.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        HBox starBox = new HBox(8);
        starBox.setAlignment(Pos.CENTER_LEFT);
        List<Button> starButtons = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            final int starVal = i;
            Button starBtn = new Button("★ " + starVal);
            starBtn.setPrefHeight(36);
            starBtn.setStyle(starVal <= selectedStars ?
                    "-fx-background-color: #FEF3C7; -fx-text-fill: #B45309; -fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-font-weight: bold; -fx-background-radius: 8px; -fx-border-color: #FCD34D; -fx-border-radius: 8px; -fx-cursor: hand;" :
                    "-fx-background-color: #FFFFFF; -fx-text-fill: #9CA3AF; -fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-background-radius: 8px; -fx-border-color: #E5E7EB; -fx-border-radius: 8px; -fx-cursor: hand;");

            starBtn.setOnAction(ev -> {
                selectedStars = starVal;
                for (int j = 0; j < starButtons.size(); j++) {
                    int val = j + 1;
                    starButtons.get(j).setStyle(val <= selectedStars ?
                            "-fx-background-color: #FEF3C7; -fx-text-fill: #B45309; -fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-font-weight: bold; -fx-background-radius: 8px; -fx-border-color: #FCD34D; -fx-border-radius: 8px; -fx-cursor: hand;" :
                            "-fx-background-color: #FFFFFF; -fx-text-fill: #9CA3AF; -fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-background-radius: 8px; -fx-border-color: #E5E7EB; -fx-border-radius: 8px; -fx-cursor: hand;");
                }
            });
            starButtons.add(starBtn);
            starBox.getChildren().add(starBtn);
        }

        Text tagLabel = new Text("Performance Highlights:");
        tagLabel.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        FlowPane tagsPane = new FlowPane(8, 8);
        String[] tags = {"⚡ High Fuel Efficiency", "⏱ Delivered on Time", "🛠 Flawless Mechanism", "🌾 Fast Acre Coverage", "🤝 Polite Communication", "🛡 Clean Equipment"};
        for (String t : tags) {
            Button tagBtn = new Button(t);
            tagBtn.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #4B5563; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-padding: 6px 12px; -fx-background-radius: 20px; -fx-border-color: rgba(45, 106, 79, 0.25); -fx-border-radius: 20px; -fx-cursor: hand;");
            tagBtn.setOnAction(ev -> {
                if (activeTags.contains(t)) {
                    activeTags.remove(t);
                    tagBtn.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #4B5563; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-padding: 6px 12px; -fx-background-radius: 20px; -fx-border-color: rgba(45, 106, 79, 0.25); -fx-border-radius: 20px; -fx-cursor: hand;");
                } else {
                    activeTags.add(t);
                    tagBtn.setStyle("-fx-background-color: #DCFCE7; -fx-text-fill: #15803D; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-padding: 6px 12px; -fx-background-radius: 20px; -fx-border-color: #86EFAC; -fx-border-radius: 20px; -fx-cursor: hand;");
                }
            });
            tagsPane.getChildren().add(tagBtn);
        }

        Label titleLbl = new Label("Review Headline");
        titleLbl.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-font-weight: bold; -fx-text-fill: #1B4332;");

        TextField headlineField = new TextField();
        headlineField.setPromptText("E.g., Excellent tractor power and smooth field operation!");
        headlineField.setPrefHeight(42);
        headlineField.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 10px; -fx-border-color: rgba(45, 106, 79, 0.3); -fx-border-width: 1.2px; -fx-border-radius: 10px; -fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-padding: 0 14px;");

        Label feedbackLbl = new Label("Detailed Review & Comments");
        feedbackLbl.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-font-weight: bold; -fx-text-fill: #1B4332;");

        TextArea commentArea = new TextArea();
        commentArea.setPromptText("Share specific details about machine performance, fuel consumption, speed, or on-field experience to help other farmers...");
        commentArea.setPrefRowCount(4);
        commentArea.setWrapText(true);
        commentArea.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 10px; -fx-border-color: rgba(45, 106, 79, 0.3); -fx-border-width: 1.2px; -fx-border-radius: 10px; -fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-padding: 8px;");

        Label statusMsg = new Label();
        statusMsg.setVisible(false);
        statusMsg.setManaged(false);

        Button submitBtn = new Button("Submit Review & Rating ✓");
        submitBtn.setPrefHeight(44);
        submitBtn.setStyle(
                "-fx-background-color: linear-gradient(to right, #2D6A4F, #40916C);" +
                "-fx-text-fill: white;" +
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 13.5px;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 8px 26px;" +
                "-fx-background-radius: 12px;" +
                "-fx-cursor: hand;" +
                "-fx-effect: dropshadow(gaussian, rgba(45, 106, 79, 0.25), 8, 0, 0, 2);"
        );

        submitBtn.setOnAction(e -> {
            if (selectedBooking == null) {
                statusMsg.setText("⚠️ Please select a completed " + (isEquipmentMode ? "rental" : "operator assignment") + " first.");
                statusMsg.setStyle("-fx-background-color: #FEF2F2; -fx-text-fill: #DC2626; -fx-font-weight: bold; -fx-padding: 8px 14px; -fx-background-radius: 8px;");
                statusMsg.setVisible(true);
                statusMsg.setManaged(true);
                return;
            }

            String head = headlineField.getText().trim();
            String comm = commentArea.getText().trim();
            if (head.isEmpty()) head = selectedBooking.title + " Field Experience";
            if (comm.isEmpty()) comm = "Delivered satisfactory field performance on time.";

            String today = LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy"));
            ReviewItem newRev = new ReviewItem(
                    "REV-" + (1000 + reviewHistory.size() + 1),
                    selectedBooking.title,
                    selectedBooking.subtitle,
                    selectedStars,
                    head,
                    comm,
                    today,
                    new ArrayList<>(activeTags),
                    "Thank you for your valuable feedback! We look forward to serving your farm again.",
                    !isEquipmentMode
            );

            reviewHistory.add(0, newRev);
            reviewedBookingIds.add(selectedBooking.bookingId);

            statTotalReviewsText.setText(String.valueOf(getFilteredHistory().size()));
            statPendingText.setText(String.valueOf(getPendingCompletedBookings().size()));
            statAvgRatingText.setText(calculateAvgRating() + " ★");

            selectedBooking = null;
            selectedBadge.setText("Please select a completed work above");
            headlineField.clear();
            commentArea.clear();
            activeTags.clear();
            refreshPendingBookingsList();

            tabHistoryBtn.fire();
        });

        formCard.getChildren().addAll(s2Title, selectedBadge, starLabel, starBox, tagLabel, tagsPane, titleLbl, headlineField, feedbackLbl, commentArea, statusMsg, submitBtn);

        container.getChildren().addAll(selectorCard, formCard);
        return container;
    }

    private static void refreshPendingBookingsList() {
        if (pendingBookingsBox == null) return;
        pendingBookingsBox.getChildren().clear();

        List<CompletedBooking> list = getPendingCompletedBookings();
        if (list.isEmpty()) {
            VBox empty = new VBox(8);
            empty.setAlignment(Pos.CENTER);
            empty.setPadding(new Insets(30));
            empty.setStyle("-fx-background-color: #F4F9F4; -fx-background-radius: 12px; -fx-border-color: rgba(45, 106, 79, 0.2); -fx-border-radius: 12px;");

            Text icon = new Text("⏳");
            icon.setStyle("-fx-font-size: 28px;");

            Text t1 = new Text("No Completed " + (isEquipmentMode ? "Rentals" : "Operator Jobs") + " Awaiting Review");
            t1.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 15px; -fx-font-weight: bold; -fx-fill: #1B4332;");

            Text t2 = new Text("A farmer can only rate " + (isEquipmentMode ? "machinery" : "operators") + " once a booking has been approved and completed on your field.");
            t2.setWrappingWidth(750);
            t2.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-fill: #4B5563; -fx-text-alignment: center;");

            empty.getChildren().addAll(icon, t1, t2);
            pendingBookingsBox.getChildren().add(empty);
            return;
        }

        for (CompletedBooking b : list) {
            HBox row = new HBox(14);
            row.setPadding(new Insets(12, 16, 12, 16));
            row.setAlignment(Pos.CENTER_LEFT);
            row.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 10px; -fx-border-color: rgba(45, 106, 79, 0.25); -fx-border-radius: 10px;");

            Text icon = new Text(b.icon);
            icon.setStyle("-fx-font-size: 24px;");

            Text tName = new Text(b.title);
            tName.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 14px; -fx-font-weight: bold; -fx-fill: #1B4332;");

            Text tInfo = new Text(b.subtitle + " • Finished: " + b.completionDate + " • Rate: " + b.rate);
            tInfo.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #4B5563;");

            VBox infoBox = new VBox(2, tName, tInfo);
            HBox.setHgrow(infoBox, Priority.ALWAYS);

            Button selectBtn = new Button(selectedBooking == b ? "✓ Selected" : "Rate This Work");
            selectBtn.setStyle(selectedBooking == b ?
                    "-fx-background-color: #DCFCE7; -fx-text-fill: #15803D; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 8px;" :
                    "-fx-background-color: linear-gradient(to right, #2D6A4F, #40916C); -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 8px; -fx-cursor: hand;");

            selectBtn.setOnAction(e -> {
                selectedBooking = b;
                if (selectedBadge != null) {
                    selectedBadge.setText("Rating for: " + b.title + " (" + b.bookingId + ")");
                }
                refreshPendingBookingsList();
            });

            row.getChildren().addAll(icon, infoBox, selectBtn);
            pendingBookingsBox.getChildren().add(row);
        }
    }

    private static VBox createHistoryView() {
        VBox container = new VBox(14);
        historyContainer = new VBox(14);
        refreshHistoryList();
        container.getChildren().add(historyContainer);
        return container;
    }

    private static void refreshHistoryList() {
        if (historyContainer == null) return;
        historyContainer.getChildren().clear();

        List<ReviewItem> list = getFilteredHistory();
        if (list.isEmpty()) {
            VBox empty = new VBox(10);
            empty.setAlignment(Pos.CENTER);
            empty.setPadding(new Insets(50));
            empty.setStyle("-fx-background-color: rgba(255, 255, 255, 0.95); -fx-background-radius: 14px; -fx-border-color: rgba(45, 106, 79, 0.25); -fx-border-width: 1.2px; -fx-border-radius: 14px; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.04), 8, 0, 0, 2);");

            Text icon = new Text("📜");
            icon.setStyle("-fx-font-size: 36px;");

            Text t1 = new Text("No Completed " + (isEquipmentMode ? "Equipment" : "Operator") + " Reviews Yet");
            t1.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 16px; -fx-font-weight: bold; -fx-fill: #1B4332;");

            Text t2 = new Text("Once you submit feedback for completed work, your verified reviews and ratings will be recorded here.");
            t2.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-fill: #4B5563;");

            empty.getChildren().addAll(icon, t1, t2);
            historyContainer.getChildren().add(empty);
            return;
        }

        for (ReviewItem item : list) {
            VBox card = createReviewCard(item);
            historyContainer.getChildren().add(card);
        }
    }

    private static VBox createReviewCard(ReviewItem item) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(18, 22, 18, 22));
        card.setStyle("-fx-background-color: rgba(255, 255, 255, 0.95); -fx-background-radius: 14px; -fx-border-color: rgba(45, 106, 79, 0.25); -fx-border-width: 1.2px; -fx-border-radius: 14px; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.04), 8, 0, 0, 2);");

        Text title = new Text(item.title);
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 16px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Label badge = new Label("✓ Verified Rating");
        badge.setStyle("-fx-background-color: #DCFCE7; -fx-text-fill: #15803D; -fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-padding: 2px 8px; -fx-background-radius: 10px;");

        Region sp = new Region();
        HBox.setHgrow(sp, Priority.ALWAYS);

        Text dt = new Text("📅 " + item.date);
        dt.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #4B5563;");

        HBox top = new HBox(8, title, badge, sp, dt);
        top.setAlignment(Pos.CENTER_LEFT);

        StringBuilder stars = new StringBuilder();
        for (int i = 0; i < item.rating; i++) stars.append("★ ");
        for (int i = item.rating; i < 5; i++) stars.append("☆ ");

        Text starText = new Text(stars.toString().trim() + " (" + item.rating + ".0 / 5.0)");
        starText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 14px; -fx-font-weight: bold; -fx-fill: #B45309;");

        Text head = new Text(item.headline);
        head.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13.5px; -fx-font-weight: bold; -fx-fill: #1F2937;");

        Text comm = new Text(item.comment);
        comm.setWrappingWidth(750);
        comm.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-fill: #374151; -fx-line-spacing: 3px;");

        card.getChildren().addAll(top, starText, head, comm);

        if (!item.tags.isEmpty()) {
            FlowPane p = new FlowPane(6, 6);
            for (String tg : item.tags) {
                Label l = new Label(tg);
                l.setStyle("-fx-background-color: #E8F5E9; -fx-text-fill: #2D6A4F; -fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-padding: 3px 8px; -fx-background-radius: 8px;");
                p.getChildren().add(l);
            }
            card.getChildren().add(p);
        }

        if (item.providerReply != null && !item.providerReply.isEmpty()) {
            VBox rep = new VBox(4);
            rep.setPadding(new Insets(10, 14, 10, 14));
            rep.setStyle("-fx-background-color: #F4F9F4; -fx-background-radius: 10px; -fx-border-color: rgba(45, 106, 79, 0.2); -fx-border-radius: 10px;");

            Text rt = new Text("💬 Provider Response:");
            rt.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-fill: #1B4332;");

            Text rc = new Text(item.providerReply);
            rc.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #4B5563;");

            rep.getChildren().addAll(rt, rc);
            card.getChildren().add(rep);
        }

        return card;
    }

    private static String calculateAvgRating() {
        List<ReviewItem> list = getFilteredHistory();
        if (list.isEmpty()) return "0.0";
        double sum = 0;
        for (ReviewItem r : list) sum += r.rating;
        return String.format("%.1f", sum / list.size());
    }
}
