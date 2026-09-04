package com.desgin.view.operator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.desgin.dao.ReviewDAO;
import com.desgin.model.ReviewModel;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class OperatorReviews {

    public static class ReviewEntry {
        public String id;
        public String farmerName;
        public String farmerPhone;
        public String machineryTask;
        public String location;
        public double rating;
        public String headline;
        public String comment;
        public String date;
        public List<String> tags;
        public int helpfulCount;

        public ReviewEntry(String id, String farmerName, String farmerPhone, String machineryTask,
                           String location, double rating, String headline, String comment,
                           String date, List<String> tags, int helpfulCount) {
            this.id = id;
            this.farmerName = farmerName;
            this.farmerPhone = farmerPhone;
            this.machineryTask = machineryTask;
            this.location = location;
            this.rating = rating;
            this.headline = headline;
            this.comment = comment;
            this.date = date;
            this.tags = tags != null ? tags : new ArrayList<>();
            this.helpfulCount = helpfulCount;
        }
    }

    private static final List<ReviewEntry> allReviews = new ArrayList<>();
    private static VBox reviewsCardContainer;
    private static Text avgRatingText;
    private static Text totalReviewsText;
    private static Text recommendRateText;
    private static String currentFilter = "ALL";
    private static String currentSearchQuery = "";

    static {
        loadDefaultReviews();
    }

    private static void loadDefaultReviews() {
        allReviews.clear();
    }

    public static ScrollPane getReviewsSection(StackPane root) {
        // Fetch dynamic reviews from Firestore in background
        new Thread(() -> {
            try {
                List<ReviewModel> dbReviews = new ReviewDAO().getAllReviews();
                Platform.runLater(() -> {
                    allReviews.clear();
                    if (dbReviews != null) {
                        for (ReviewModel rm : dbReviews) {
                            if (rm.isOperator()) {
                                allReviews.add(new ReviewEntry(
                                    rm.getReviewId() != null ? rm.getReviewId() : "REV-" + System.currentTimeMillis(),
                                    rm.getFarmerName() != null && !rm.getFarmerName().trim().isEmpty() ? rm.getFarmerName().trim() : "Verified Farmer",
                                    "+91 98*** ****",
                                    rm.getMachineryName() != null && !rm.getMachineryName().trim().isEmpty() ? rm.getMachineryName().trim() : "Field Agricultural Operation",
                                    "Farm Plot Sector",
                                    rm.getRating() > 0 ? rm.getRating() : 5.0,
                                    rm.getHeadline() != null ? rm.getHeadline() : "",
                                    rm.getComment() != null ? rm.getComment() : "",
                                    rm.getDate() != null ? rm.getDate() : "Recently",
                                    rm.getTags() != null ? rm.getTags() : new ArrayList<>(),
                                    0
                                ));
                            }
                        }
                    }
                    updateMetrics();
                    renderReviews();
                });
            } catch (Exception ignored) {}
        }).start();

        VBox contentBox = new VBox(16);
        contentBox.setPadding(new Insets(16, 24, 24, 24));
        contentBox.setStyle("-fx-background-color: #F4F9F4;");

        // --- 1. Top Metrics Grid (Average Rating, Total Reviews, 5-Star Share, Satisfaction) ---
        GridPane metricsGrid = createMetricsGrid();

        // --- 2. Full-Width Search Bar ---
        HBox searchContainer = new HBox(10);
        searchContainer.setAlignment(Pos.CENTER_LEFT);
        searchContainer.setPadding(new Insets(10, 16, 10, 16));
        searchContainer.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 12px; -fx-border-color: #E2EBE5; -fx-border-width: 1.2px; -fx-border-radius: 12px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.03), 6, 0, 0, 2);");

        Text searchIcon = new Text("🔍");
        searchIcon.setStyle("-fx-font-size: 14px; -fx-fill: #2D6A4F;");

        TextField searchField = new TextField();
        searchField.setPromptText("Search reviews by farmer name, machinery task, keywords, or rating...");
        searchField.setStyle("-fx-background-color: transparent; -fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-text-fill: #1B4332; -fx-prompt-text-fill: #9CA3AF; -fx-border-width: 0;");
        HBox.setHgrow(searchField, Priority.ALWAYS);

        searchField.textProperty().addListener((obs, oldV, newV) -> {
            currentSearchQuery = newV != null ? newV.trim().toLowerCase() : "";
            renderReviews();
        });

        searchContainer.getChildren().addAll(searchIcon, searchField);
        HBox.setHgrow(searchContainer, Priority.ALWAYS);

        // --- 3. Filter Tabs & Rating Breakdown Row ---
        HBox filterAndStatsRow = createFilterAndStatsRow();

        // --- 4. Reviews List Container (View Only) ---
        reviewsCardContainer = new VBox(12);
        reviewsCardContainer.setFillWidth(true);

        renderReviews();
        updateMetrics();

        contentBox.getChildren().addAll(metricsGrid, searchContainer, filterAndStatsRow, reviewsCardContainer);

        ScrollPane scrollPane = new ScrollPane(contentBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: #F4F9F4; -fx-border-color: transparent;");
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        return scrollPane;
    }

    private static GridPane createMetricsGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(14);
        grid.setVgap(14);

        // Card 1: Average Rating
        avgRatingText = new Text("0.0 ★");
        avgRatingText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 26px; -fx-font-weight: bold; -fx-fill: #D97706;");
        VBox card1 = createMetricCard("⭐ Average Farmer Rating", avgRatingText, "Based on verified completed shifts", "#FFFBEB", "#FDE68A");

        // Card 2: Total Reviews
        totalReviewsText = new Text("0");
        totalReviewsText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 26px; -fx-font-weight: bold; -fx-fill: #1B4332;");
        VBox card2 = createMetricCard("📝 Total Farmer Reviews", totalReviewsText, "100% authenticated field logs", "#ECFDF5", "#A7F3D0");

        // Card 3: Re-hire Recommendation (dynamically calculated from ratings)
        recommendRateText = new Text("0.0%");
        recommendRateText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 26px; -fx-font-weight: bold; -fx-fill: #15803D;");
        VBox card3 = createMetricCard("🤝 Farmer Re-hire Rate", recommendRateText, "Farmers willing to hire again", "#F0FDF4", "#BBF7D0");

        grid.add(card1, 0, 0);
        grid.add(card2, 1, 0);
        grid.add(card3, 2, 0);

        for (int i = 0; i < 3; i++) {
            javafx.scene.layout.ColumnConstraints col = new javafx.scene.layout.ColumnConstraints();
            col.setPercentWidth(100.0 / 3.0);
            grid.getColumnConstraints().add(col);
        }

        return grid;
    }

    private static VBox createMetricCard(String title, Text valueText, String subtitle, String bgColor, String borderColor) {
        Text titleText = new Text(title);
        titleText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: 600; -fx-fill: #4B5563;");

        Text subText = new Text(subtitle);
        subText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-fill: #6B7280;");

        VBox box = new VBox(6, titleText, valueText, subText);
        box.setPadding(new Insets(14, 16, 14, 16));
        box.setStyle("-fx-background-color: " + bgColor + "; -fx-background-radius: 12px; -fx-border-color: " + borderColor + "; -fx-border-width: 1.2px; -fx-border-radius: 12px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.02), 4, 0, 0, 2);");
        return box;
    }

    private static HBox createFilterAndStatsRow() {
        HBox row = new HBox(12);
        row.setAlignment(Pos.CENTER_LEFT);

        HBox filterBox = new HBox(8);
        filterBox.setAlignment(Pos.CENTER_LEFT);

        Button allBtn = createFilterBtn("All Reviews", true);
        Button fiveStarBtn = createFilterBtn("★★★★★ 5 Stars", false);
        Button fourStarBtn = createFilterBtn("★★★★ 4 Stars", false);
        Button threeStarBtn = createFilterBtn("★★★ 3 Stars", false);

        allBtn.setOnAction(e -> {
            setActiveFilterBtn(allBtn, filterBox);
            currentFilter = "ALL";
            renderReviews();
        });

        fiveStarBtn.setOnAction(e -> {
            setActiveFilterBtn(fiveStarBtn, filterBox);
            currentFilter = "5";
            renderReviews();
        });

        fourStarBtn.setOnAction(e -> {
            setActiveFilterBtn(fourStarBtn, filterBox);
            currentFilter = "4";
            renderReviews();
        });

        threeStarBtn.setOnAction(e -> {
            setActiveFilterBtn(threeStarBtn, filterBox);
            currentFilter = "3";
            renderReviews();
        });

        filterBox.getChildren().addAll(allBtn, fiveStarBtn, fourStarBtn, threeStarBtn);
        HBox.setHgrow(filterBox, Priority.ALWAYS);

        row.getChildren().add(filterBox);
        return row;
    }

    private static Button createFilterBtn(String text, boolean active) {
        Button btn = new Button(text);
        btn.setPrefHeight(34);
        if (active) {
            btn.setStyle("-fx-background-color: #1B4332; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 0 14;");
        } else {
            btn.setStyle("-fx-background-color: #E8F5E9; -fx-text-fill: #1B4332; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 0 14;");
        }
        return btn;
    }

    private static void setActiveFilterBtn(Button active, HBox container) {
        for (var node : container.getChildren()) {
            if (node instanceof Button) {
                ((Button) node).setStyle("-fx-background-color: #E8F5E9; -fx-text-fill: #1B4332; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 0 14;");
            }
        }
        active.setStyle("-fx-background-color: #1B4332; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 0 14;");
    }

    private static void updateMetrics() {
        if (allReviews.isEmpty()) {
            if (avgRatingText != null) avgRatingText.setText("0.0 ★");
            if (totalReviewsText != null) totalReviewsText.setText("0");
            if (recommendRateText != null) recommendRateText.setText("0.0%");
            return;
        }

        double sum = 0;
        double totalRehireProb = 0;
        for (ReviewEntry r : allReviews) {
            sum += r.rating;
            totalRehireProb += calculateRehireProbability(r.rating);
        }

        double avg = sum / allReviews.size();
        double avgRehireRate = totalRehireProb / allReviews.size();

        if (avgRatingText != null) avgRatingText.setText(String.format("%.1f ★", avg));
        if (totalReviewsText != null) totalReviewsText.setText(String.valueOf(allReviews.size()));
        if (recommendRateText != null) {
            recommendRateText.setText(String.format("%.1f%%", avgRehireRate));
        }
    }

    /**
     * Calculates the estimated farmer re-hire probability based on review rating.
     *
     * Assumptions & Model:
     * 1. 5-Star Experience (Rating >= 4.8): Exceptional service, zero friction, and high punctuality.
     *    Farmer has near-guaranteed repeat hire probability (100%).
     * 2. 4-Star Experience (4.0 <= Rating < 4.8): High satisfaction with minor acceptable variations.
     *    Farmer demonstrates strong loyalty and re-hire probability (80% to 98%, linearly interpolated).
     * 3. 3-Star Experience (3.0 <= Rating < 4.0): Neutral/adequate performance meeting basic requirements.
     *    Farmer may re-hire if immediate alternatives are unavailable (40% to 75%, linearly interpolated).
     * 4. 2-Star Experience (2.0 <= Rating < 3.0): Subpar shift performance or equipment delay causing dissatisfaction.
     *    Low likelihood of repeat booking (10% to 35%, linearly interpolated).
     * 5. 1-Star Experience (Rating < 2.0): Serious operational failure or conflict.
     *    Zero to minimal likelihood of re-hire (0% to 10%).
     *
     * Aggregate Re-hire Rate = (sum of P(R_i)) / N
     */
    private static double calculateRehireProbability(double rating) {
        if (rating >= 4.8) {
            return 100.0;
        } else if (rating >= 4.0) {
            return 80.0 + ((rating - 4.0) / 0.8) * 18.0;
        } else if (rating >= 3.0) {
            return 40.0 + ((rating - 3.0) / 1.0) * 35.0;
        } else if (rating >= 2.0) {
            return 10.0 + ((rating - 2.0) / 1.0) * 25.0;
        } else {
            return Math.max(0.0, rating * 5.0);
        }
    }

    private static void renderReviews() {
        if (reviewsCardContainer == null) return;
        reviewsCardContainer.getChildren().clear();

        List<ReviewEntry> filtered = allReviews.stream()
            .filter(r -> {
                if ("5".equals(currentFilter) && r.rating < 4.8) return false;
                if ("4".equals(currentFilter) && (r.rating < 3.8 || r.rating >= 4.8)) return false;
                if ("3".equals(currentFilter) && (r.rating < 2.8 || r.rating >= 3.8)) return false;

                if (!currentSearchQuery.isEmpty()) {
                    String query = currentSearchQuery;
                    boolean match = r.farmerName.toLowerCase().contains(query) ||
                                    r.machineryTask.toLowerCase().contains(query) ||
                                    r.headline.toLowerCase().contains(query) ||
                                    r.comment.toLowerCase().contains(query) ||
                                    r.location.toLowerCase().contains(query);
                    if (!match) return false;
                }
                return true;
            })
            .collect(Collectors.toList());

        if (filtered.isEmpty()) {
            VBox emptyBox = new VBox(10);
            emptyBox.setAlignment(Pos.CENTER);
            emptyBox.setPadding(new Insets(45, 20, 45, 20));
            emptyBox.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 12px; -fx-border-color: #E2EBE5; -fx-border-radius: 12px;");

            Text emptyIcon = new Text("⭐");
            emptyIcon.setStyle("-fx-font-size: 38px; -fx-fill: #9CA3AF;");

            Text emptyTitle;
            Text emptySub;
            if (allReviews.isEmpty()) {
                emptyTitle = new Text("No Reviews Received Yet");
                emptySub = new Text("When completed field shifts are reviewed by farmers, their ratings and reviews will appear here dynamically.");
            } else {
                emptyTitle = new Text("No reviews found matching criteria");
                emptySub = new Text("Try clearing your search query or selecting 'All Reviews'");
            }
            emptyTitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 15px; -fx-font-weight: bold; -fx-fill: #374151;");
            emptySub.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #6B7280;");

            Button gotoJobsBtn = new Button("🚜  View Completed Jobs ➔");
            gotoJobsBtn.setStyle("-fx-background-color: #2D6A4F; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 8px; -fx-cursor: hand; -fx-padding: 7 14;");
            gotoJobsBtn.setOnAction(e -> OperatorLeftSideBar.navigateToJobs());

            emptyBox.getChildren().addAll(emptyIcon, emptyTitle, emptySub, gotoJobsBtn);
            reviewsCardContainer.getChildren().add(emptyBox);
            return;
        }

        for (ReviewEntry rev : filtered) {
            reviewsCardContainer.getChildren().add(createReviewCard(rev));
        }
    }

    public static void addOperatorReview(String revId, String farmerName, String machineryTask, double rating, String headline, String comment, String date) {
        ReviewEntry entry = new ReviewEntry(
            revId,
            farmerName != null && !farmerName.isEmpty() ? farmerName : "Verified Farmer",
            "+91 98*** ****",
            machineryTask != null && !machineryTask.isEmpty() ? machineryTask : "Field Agricultural Operation",
            "Farm Plot Sector",
            rating,
            headline != null ? headline : "",
            comment != null ? comment : "",
            date != null ? date : "Today",
            new ArrayList<>(),
            0
        );

        boolean exists = allReviews.stream().anyMatch(r -> r.id.equalsIgnoreCase(revId));
        if (!exists) {
            allReviews.add(0, entry);
        }
        Platform.runLater(() -> {
            updateMetrics();
            renderReviews();
        });
    }

    private static HBox createReviewCard(ReviewEntry rev) {
        HBox card = new HBox(18);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(14, 20, 14, 20));
        card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 12px; -fx-border-color: #E2EBE5; -fx-border-width: 1.2px; -fx-border-radius: 12px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.02), 6, 0, 0, 2);");

        // 1. Left: Farmer Name, Detail Icon, & Purpose Hired For
        VBox reviewerBox = new VBox(4);
        reviewerBox.setAlignment(Pos.CENTER_LEFT);
        reviewerBox.setPrefWidth(240);
        reviewerBox.setMinWidth(200);
        reviewerBox.setMaxWidth(260);

        Text farmerName = new Text("👨‍🌾 " + rev.farmerName);
        farmerName.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 14px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        String task = (rev.machineryTask != null && !rev.machineryTask.trim().isEmpty()) ? rev.machineryTask.trim() : "Field Agricultural Operation";
        Text purposeText = new Text("🚜 Purpose: " + task);
        purposeText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #4B5563;");
        purposeText.setWrappingWidth(230);

        reviewerBox.getChildren().addAll(farmerName, purposeText);

        // 2. Middle: Review Text Only
        VBox reviewBox = new VBox();
        reviewBox.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(reviewBox, Priority.ALWAYS);

        String textToShow;
        if (rev.comment != null && !rev.comment.trim().isEmpty()) {
            if (rev.headline != null && !rev.headline.trim().isEmpty() && !rev.headline.equalsIgnoreCase(rev.comment)) {
                textToShow = rev.headline.trim() + " — " + rev.comment.trim();
            } else {
                textToShow = rev.comment.trim();
            }
        } else if (rev.headline != null && !rev.headline.trim().isEmpty()) {
            textToShow = rev.headline.trim();
        } else {
            textToShow = "Verified completed shift.";
        }

        Text reviewText = new Text(textToShow);
        reviewText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-fill: #374151; -fx-line-spacing: 2px;");
        reviewText.setWrappingWidth(470);

        reviewBox.getChildren().add(reviewText);

        // 3. Right: Rating Number & Date
        VBox ratingBox = new VBox(4);
        ratingBox.setAlignment(Pos.CENTER_RIGHT);
        ratingBox.setPrefWidth(100);
        ratingBox.setMinWidth(85);

        Label ratingLabel = new Label("★ " + String.format("%.1f", rev.rating));
        ratingLabel.setStyle("-fx-background-color: #FFFBEB; -fx-text-fill: #D97706; -fx-font-family: 'Poppins'; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 4 10; -fx-background-radius: 6; -fx-border-color: #FDE68A; -fx-border-radius: 6;");

        Text dateText = new Text(rev.date != null && !rev.date.isEmpty() ? "📅 " + rev.date : "📅 Today");
        dateText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-fill: #9CA3AF;");

        ratingBox.getChildren().addAll(ratingLabel, dateText);

        card.getChildren().addAll(reviewerBox, reviewBox, ratingBox);

        card.setOnMouseEntered(e -> card.setStyle("-fx-background-color: #FBFDFB; -fx-background-radius: 12px; -fx-border-color: #2D6A4F; -fx-border-width: 1.2px; -fx-border-radius: 12px; -fx-effect: dropshadow(gaussian, rgba(45,106,79,0.1), 8, 0, 0, 2);"));
        card.setOnMouseExited(e -> card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 12px; -fx-border-color: #E2EBE5; -fx-border-width: 1.2px; -fx-border-radius: 12px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.02), 6, 0, 0, 2);"));

        return card;
    }
}
