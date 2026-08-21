package com.desgin.view.provider;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class ProviderAnalytics {

    public static ScrollPane getAnalyticsSection() {
        Text headerTitle = new Text("Fleet Analytics & Business Intelligence");
        headerTitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 26px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text headerSubtitle = new Text("Analyze machinery utilization, seasonal agricultural demand patterns, peak rental periods, and farmer satisfaction.");
        headerSubtitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-fill: #4B5563;");

        VBox titleBox = new VBox(4, headerTitle, headerSubtitle);

        // 4 KPI Analytics Cards
        HBox kpiRow = new HBox(15,
            createMetricCard("📈 Fleet Utilization Rate", "78.4%", "+12% vs last quarter", "#2E7D32"),
            createMetricCard("🔁 Repeat Farmer Rate", "64.2%", "High customer loyalty", "#374151"),
            createMetricCard("⚡ Avg Daily Revenue", "₹4,150 / day", "Across all 18 machines", "#1B4332"),
            createMetricCard("⭐ Farmer Satisfaction", "4.9 / 5.0", "128 Verified Ratings", "#2E7D32")
        );
        kpiRow.setAlignment(Pos.CENTER_LEFT);

        // Machinery Utilization Table / Progress Bars
        VBox utilizationCard = createUtilizationCard();

        // Seasonal Farming Demand Trends Guide
        VBox seasonalCard = createSeasonalDemandCard();

        // Farmer Review & Quality Breakdown
        VBox ratingCard = createRatingsBreakdownCard();

        VBox content = new VBox(22, titleBox, kpiRow, utilizationCard, seasonalCard, ratingCard);
        content.setPadding(new Insets(25, 30, 35, 30));

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        return scrollPane;
    }

    private static VBox createMetricCard(String title, String value, String sub, String color) {
        Text t = new Text(title);
        t.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #4B5563;");

        Text v = new Text(value);
        v.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 22px; -fx-font-weight: bold; -fx-fill: " + color + ";");

        Text s = new Text(sub);
        s.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 10.5px; -fx-fill: #374151;");

        VBox b = new VBox(6, t, v, s);
        b.setPrefWidth(240);
        b.setPadding(new Insets(16));
        b.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 12; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 12;");
        return b;
    }

    private static VBox createUtilizationCard() {
        Text title = new Text("Machinery Utilization & Demand Index (Last 90 Days)");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 17px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        VBox r1 = createProgressRow("🌾 Combine Harvesters (Kartar & Preet)", "88% Days Booked", 0.88, "#2D6A4F");
        VBox r2 = createProgressRow("🚜 High-Power Tractors (John Deere 55HP)", "82% Days Booked", 0.82, "#2E7D32");
        VBox r3 = createProgressRow("⚙ Rotavators (Shaktiman 7ft)", "74% Days Booked", 0.74, "#E65100");
        VBox r4 = createProgressRow("🚁 Agri Sprayer Drones (16L)", "65% Days Booked", 0.65, "#1976D2");
        VBox r5 = createProgressRow("🌱 Seed Drills & Cultivators", "58% Days Booked", 0.58, "#6B8E23");

        VBox card = new VBox(12, title, r1, r2, r3, r4, r5);
        card.setPadding(new Insets(18));
        card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 14; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 14;");
        return card;
    }

    private static VBox createProgressRow(String label, String value, double progress, String barColor) {
        Text l = new Text(label);
        l.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text v = new Text(value);
        v.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-font-weight: bold; -fx-fill: #374151;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox top = new HBox(l, spacer, v);

        ProgressBar pb = new ProgressBar(progress);
        pb.setPrefWidth(980);
        pb.setPrefHeight(10);
        pb.setStyle("-fx-accent: " + barColor + ";");

        return new VBox(4, top, pb);
    }

    private static VBox createSeasonalDemandCard() {
        Text title = new Text("Seasonal Agricultural Cycles & Demand Forecast");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 17px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text kharif = new Text("🌾 Kharif Harvest Peak (Aug - Oct): Harvesters & Tractors at 95%+ demand. Increase daily tariffs by 15-20% for spot bookings.");
        kharif.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-fill: #2E7D32; -fx-font-weight: bold;");

        Text rabi = new Text("🌱 Rabi Sowing Season (Nov - Jan): High demand for Seed Drills, Rotary Tillers, and Subsoilers.");
        rabi.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-fill: #374151;");

        Text summer = new Text("☀️ Summer Land Prep (Feb - May): Cultivators, Deep Ploughs, and Laser Levelers.");
        summer.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-fill: #4B5563;");

        VBox card = new VBox(10, title, kharif, rabi, summer);
        card.setPadding(new Insets(18));
        card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 14; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 14;");
        return card;
    }

    private static VBox createRatingsBreakdownCard() {
        Text title = new Text("Farmer Feedback & Quality Ratings Breakdown");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 17px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        HBox stats = new HBox(25,
            createRatingPill("★★★★★ (5 Stars)", "112 Reviews (87%)", "#2E7D32"),
            createRatingPill("★★★★☆ (4 Stars)", "14 Reviews (11%)", "#6B8E23"),
            createRatingPill("★★★☆☆ (3 Stars)", "2 Reviews (2%)", "#E65100")
        );

        VBox card = new VBox(12, title, stats);
        card.setPadding(new Insets(18));
        card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 14; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 14;");
        return card;
    }

    private static VBox createRatingPill(String stars, String count, String color) {
        Text s = new Text(stars);
        s.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-font-weight: bold; -fx-fill: " + color + ";");
        Text c = new Text(count);
        c.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-fill: #374151;");

        VBox b = new VBox(4, s, c);
        b.setPadding(new Insets(10, 16, 10, 16));
        b.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 8; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 8;");
        return b;
    }
}
