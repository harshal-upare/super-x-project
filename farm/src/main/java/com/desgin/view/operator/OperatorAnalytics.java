package com.desgin.view.operator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

/**
 * Enhanced Operator Performance & Field Safety Analytics View
 * Featuring real JavaFX Charts (AreaChart, BarChart, PieChart),
 * Telematics Metrics, Safety Compliance Matrix, and Farmer Reviews.
 */
@SuppressWarnings("unchecked")
public class OperatorAnalytics {

    public static ScrollPane getAnalyticsSection() {
        // ================= HEADER & FILTER =================
        Text title = new Text("Operator Performance & Field Safety Analytics");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 26px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text subtitle = new Text("Real-time machinery telematics, fuel conservation curves, task distribution, and zero-hazard safety audit.");
        subtitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-fill: #4B5563;");

        VBox titleBox = new VBox(4, title, subtitle);

        // Header Top Row with Rank Badge
        HBox headerRow = createHeaderWithBadge(titleBox);

        // Filter Bar (Interactive Time Horizon Pills)
        HBox filterBar = createFilterBar();

        // ================= 4 KPI METRIC CARDS =================
        HBox metricRow = createKPIRow();

        // ================= CHARTS ROW 1: AreaChart + BarChart =================
        HBox chartsRow1 = createChartsRow1();

        // ================= CHARTS ROW 2: PieChart + Safety Matrix =================
        HBox chartsRow2 = createChartsRow2();

        // ================= OPERATIONAL PRECISION & TELEMATICS =================
        VBox skillBreakdownCard = createSkillBreakdownCard();

        // ================= RECENT FARMER REVIEWS =================
        VBox reviewsSection = createReviewsSection();

        // Assemble Content
        VBox content = new VBox(22, headerRow, filterBar, metricRow, chartsRow1, chartsRow2, skillBreakdownCard, reviewsSection);
        content.setPadding(new Insets(25, 30, 40, 30));
        content.setStyle("-fx-background-color: transparent;");

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        return scrollPane;
    }

    private static HBox createHeaderWithBadge(VBox titleBox) {
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Rank Badge
        Text badgeIcon = new Text("🏆");
        badgeIcon.setStyle("-fx-font-size: 16px;");

        Text badgeTitle = new Text("MASTER OPERATOR (LEVEL IV)");
        badgeTitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-font-weight: bold; -fx-fill: #1B5E20;");

        Text badgeSub = new Text("Top 2% in Western Maharashtra");
        badgeSub.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 10px; -fx-fill: #2E7D32;");

        VBox badgeTextBox = new VBox(1, badgeTitle, badgeSub);
        HBox badgeBox = new HBox(8, badgeIcon, badgeTextBox);
        badgeBox.setAlignment(Pos.CENTER_LEFT);
        badgeBox.setPadding(new Insets(8, 16, 8, 16));
        badgeBox.setStyle("-fx-background-color: #E8F5E9; -fx-background-radius: 20; -fx-border-color: #A5D6A7; -fx-border-radius: 20; -fx-border-width: 1;");

        HBox header = new HBox(15, titleBox, spacer, badgeBox);
        header.setAlignment(Pos.CENTER_LEFT);
        return header;
    }

    private static HBox createFilterBar() {
        Button btn1 = createFilterPill("⚡ Kharif Season 2026", true);
        Button btn2 = createFilterPill("📅 Last 30 Days", false);
        Button btn3 = createFilterPill("📊 6-Month Trend", false);
        Button btn4 = createFilterPill("🌍 All-Time Career", false);

        ObservableList<Button> filterButtons = FXCollections.observableArrayList(btn1, btn2, btn3, btn4);

        for (Button btn : filterButtons) {
            btn.setOnAction(e -> {
                for (Button b : filterButtons) {
                    styleFilterPill(b, b == btn);
                }
            });
        }

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label telematicsLabel = new Label("📡 Telematics Sync: Live (GPS Active)");
        telematicsLabel.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-text-fill: #2E7D32; -fx-font-weight: bold;");

        HBox bar = new HBox(10, btn1, btn2, btn3, btn4, spacer, telematicsLabel);
        bar.setAlignment(Pos.CENTER_LEFT);
        bar.setPadding(new Insets(0, 0, 4, 0));
        return bar;
    }

    private static Button createFilterPill(String text, boolean active) {
        Button btn = new Button(text);
        styleFilterPill(btn, active);
        return btn;
    }

    private static void styleFilterPill(Button btn, boolean active) {
        if (active) {
            btn.setStyle("-fx-background-color: #1B4332; -fx-text-fill: #FFFFFF; -fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-font-weight: bold; -fx-background-radius: 20; -fx-padding: 6 14 6 14; -fx-cursor: hand;");
        } else {
            btn.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #374151; -fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-font-weight: normal; -fx-background-radius: 20; -fx-border-color: #E2EBE5; -fx-border-radius: 20; -fx-padding: 5 13 5 13; -fx-cursor: hand;");
        }
    }

    private static HBox createKPIRow() {
        VBox c1 = createMetricCard("⭐ Operator Rating", "4.9 / 5.0", "Based on 38 farmer reviews (98% 5★)", "#1B4332", "#FFFFFF");
        VBox c2 = createMetricCard("🛡 Safety & Incident Score", "99.4%", "0 Collisions • 480+ Safe Shift Hours", "#2E7D32", "#E8F5E9");
        VBox c3 = createMetricCard("⛽ Fuel Efficiency Index", "94.2% Optimal", "Saved ~1.4L / acre vs state benchmark", "#2E7D32", "#FFFFFF");
        VBox c4 = createMetricCard("🌾 Total Land Cultivated", "115.0 Ha", "284.0 Acres operated across 92 shifts", "#374151", "#FFFFFF");

        HBox row = new HBox(16, c1, c2, c3, c4);
        row.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(c1, Priority.ALWAYS);
        HBox.setHgrow(c2, Priority.ALWAYS);
        HBox.setHgrow(c3, Priority.ALWAYS);
        HBox.setHgrow(c4, Priority.ALWAYS);
        return row;
    }

    private static VBox createMetricCard(String title, String value, String sub, String color, String bgColor) {
        Text t = new Text(title);
        t.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #4B5563;");

        Text v = new Text(value);
        v.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 22px; -fx-font-weight: bold; -fx-fill: " + color + ";");

        Text s = new Text(sub);
        s.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-fill: #374151;");

        VBox b = new VBox(6, t, v, s);
        b.setPadding(new Insets(16, 18, 16, 18));
        b.setStyle("-fx-background-color: " + bgColor + "; -fx-background-radius: 12; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 12;");
        return b;
    }

    // =========================================================
    // CHARTS ROW 1: AreaChart (Monthly Output) + BarChart (Machine Hours)
    // =========================================================
    private static HBox createChartsRow1() {
        // Chart 1: Monthly Output & Fuel Conservation
        VBox chartCard1 = createAreaChartCard();

        // Chart 2: Machinery Utilization
        VBox chartCard2 = createBarChartCard();

        HBox row = new HBox(18, chartCard1, chartCard2);
        row.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(chartCard1, Priority.ALWAYS);
        HBox.setHgrow(chartCard2, Priority.ALWAYS);
        return row;
    }

    private static VBox createAreaChartCard() {
        Text cardTitle = new Text("📈 Monthly Output & Fuel Conservation Trend");
        cardTitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 16px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text cardSub = new Text("Acres cultivated vs diesel conserved over the past 6 months");
        cardSub.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-fill: #4B5563;");

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Operating Month");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Volume / Area Units");

        AreaChart<String, Number> areaChart = new AreaChart<>(xAxis, yAxis);
        areaChart.setAnimated(false);
        areaChart.setLegendVisible(true);
        areaChart.setPrefHeight(270);
        areaChart.setMinHeight(270);

        XYChart.Series<String, Number> seriesAcres = new XYChart.Series<>();
        seriesAcres.setName("Acres Cultivated");
        seriesAcres.getData().add(new XYChart.Data<>("Mar 26", 34));
        seriesAcres.getData().add(new XYChart.Data<>("Apr 26", 46));
        seriesAcres.getData().add(new XYChart.Data<>("May 26", 58));
        seriesAcres.getData().add(new XYChart.Data<>("Jun 26", 52));
        seriesAcres.getData().add(new XYChart.Data<>("Jul 26", 65));
        seriesAcres.getData().add(new XYChart.Data<>("Aug 26", 74));

        XYChart.Series<String, Number> seriesFuel = new XYChart.Series<>();
        seriesFuel.setName("Diesel Saved (Liters)");
        seriesFuel.getData().add(new XYChart.Data<>("Mar 26", 28));
        seriesFuel.getData().add(new XYChart.Data<>("Apr 26", 41));
        seriesFuel.getData().add(new XYChart.Data<>("May 26", 55));
        seriesFuel.getData().add(new XYChart.Data<>("Jun 26", 48));
        seriesFuel.getData().add(new XYChart.Data<>("Jul 26", 62));
        seriesFuel.getData().add(new XYChart.Data<>("Aug 26", 70));

        areaChart.getData().addAll(seriesAcres, seriesFuel);

        VBox card = new VBox(8, new VBox(2, cardTitle, cardSub), areaChart);
        card.setPadding(new Insets(18));
        card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 14; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 14;");
        return card;
    }

    private static VBox createBarChartCard() {
        Text cardTitle = new Text("🚜 Machinery Operating Hours & Utilization");
        cardTitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 16px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text cardSub = new Text("Total hours logged per machinery model in Kharif Season");
        cardSub.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-fill: #4B5563;");

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Machinery Model");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Hours Logged");

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setAnimated(false);
        barChart.setLegendVisible(false);
        barChart.setPrefHeight(270);
        barChart.setMinHeight(270);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Hours Logged");
        series.getData().add(new XYChart.Data<>("John Deere 5050D", 142));
        series.getData().add(new XYChart.Data<>("Combine Harvester", 98));
        series.getData().add(new XYChart.Data<>("Rotavator", 86));
        series.getData().add(new XYChart.Data<>("Laser Leveler", 64));
        series.getData().add(new XYChart.Data<>("Transplanter", 45));

        barChart.getData().add(series);

        VBox card = new VBox(8, new VBox(2, cardTitle, cardSub), barChart);
        card.setPadding(new Insets(18));
        card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 14; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 14;");
        return card;
    }

    // =========================================================
    // CHARTS ROW 2: PieChart (Task Distribution) + Safety Audit Matrix
    // =========================================================
    private static HBox createChartsRow2() {
        // Pie Chart: Field Task Distribution
        VBox pieCard = createPieChartCard();

        // Safety Compliance Matrix Card
        VBox safetyCard = createSafetyMatrixCard();

        HBox row = new HBox(18, pieCard, safetyCard);
        row.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(pieCard, Priority.ALWAYS);
        HBox.setHgrow(safetyCard, Priority.ALWAYS);
        return row;
    }

    private static VBox createPieChartCard() {
        Text cardTitle = new Text("📊 Field Task & Operation Distribution");
        cardTitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 16px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text cardSub = new Text("Proportion of time allocated across agricultural operations");
        cardSub.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-fill: #4B5563;");

        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList(
                new PieChart.Data("Tillage & Plowing (38%)", 38),
                new PieChart.Data("Crop Harvesting (28%)", 28),
                new PieChart.Data("Laser Land Leveling (18%)", 18),
                new PieChart.Data("Hauling & Transport (16%)", 16)
        );

        PieChart pieChart = new PieChart(pieData);
        pieChart.setAnimated(false);
        pieChart.setLegendVisible(true);
        pieChart.setPrefHeight(270);
        pieChart.setMinHeight(270);

        VBox card = new VBox(8, new VBox(2, cardTitle, cardSub), pieChart);
        card.setPadding(new Insets(18));
        card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 14; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 14;");
        return card;
    }

    private static VBox createSafetyMatrixCard() {
        Text cardTitle = new Text("🛡️ Field Safety & Zero-Hazard Compliance Audit");
        cardTitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 16px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text cardSub = new Text("Active telematics safety protocols & pre-shift verification logs");
        cardSub.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-fill: #4B5563;");

        VBox item1 = createSafetyItem("🛡 ROPS & 4-Point Seatbelt Protocol", "100% Active on all shifts", "PASSED", "#2E7D32");
        VBox item2 = createSafetyItem("🚜 PTO Rotating Shield & Heavy Guard Check", "Zero component exposure logged", "VERIFIED", "#2E7D32");
        VBox item3 = createSafetyItem("🚨 Slope & Terrain Rollover Warning Monitor", "0 Hazard threshold triggers", "OPTIMAL", "#1976D2");
        VBox item4 = createSafetyItem("⏱ Shift Fatigue & Rest Time Limits (Max 8h)", "100% Adherence to rest breaks", "COMPLIANT", "#2E7D32");
        VBox item5 = createSafetyItem("🧯 Onboard Fire Extinguisher & First Aid Kit", "Inspected & Field-Ready", "READY", "#388E3C");

        VBox list = new VBox(10, item1, item2, item3, item4, item5);

        VBox card = new VBox(12, new VBox(2, cardTitle, cardSub), list);
        card.setPadding(new Insets(18));
        card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 14; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 14;");
        return card;
    }

    private static VBox createSafetyItem(String title, String desc, String badgeText, String badgeColor) {
        Text t = new Text(title);
        t.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text d = new Text(desc);
        d.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-fill: #4B5563;");

        VBox left = new VBox(2, t, d);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Text badge = new Text(badgeText);
        badge.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 10px; -fx-font-weight: bold; -fx-fill: white;");

        StackPane badgeContainer = new StackPane(badge);
        badgeContainer.setPadding(new Insets(3, 8, 3, 8));
        badgeContainer.setStyle("-fx-background-color: " + badgeColor + "; -fx-background-radius: 12;");

        HBox row = new HBox(8, left, spacer, badgeContainer);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(6, 10, 6, 10));
        row.setStyle("-fx-background-color: #FFFDFC; -fx-background-radius: 8; -fx-border-color: #E2D7CB; -fx-border-radius: 8; -fx-border-width: 1;");

        return new VBox(row);
    }

    // =========================================================
    // PRECISION & SKILL BREAKDOWN
    // =========================================================
    private static VBox createSkillBreakdownCard() {
        Text title = new Text("🎯 Machinery Operation Skill & Precision Telematics");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 17px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text sub = new Text("Sensor-derived accuracy ratings for seed drilling, land leveling, and fuel conservation.");
        sub.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #4B5563;");

        VBox r1 = createProgressRow("🎯 Plowing Depth & Tillage Consistency (8-10 inches target)", "96% Precision", 0.96, "#2E7D32");
        VBox r2 = createProgressRow("🌾 Harvester Grain Loss Minimization (<1.2% loss recorded)", "98% Clean Output", 0.98, "#2D6A4F");
        VBox r3 = createProgressRow("⚡ Engine RPM Eco-Torque Zone Optimization", "92% Eco-Drive", 0.92, "#2E7D32");
        VBox r4 = createProgressRow("⏱ On-Time Field Arrival & Job Schedule Punctuality", "98% Punctuality", 0.98, "#1976D2");

        VBox card = new VBox(12, new VBox(2, title, sub), r1, r2, r3, r4);
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
        pb.setMaxWidth(Double.MAX_VALUE);
        pb.setPrefHeight(9);
        pb.setStyle("-fx-accent: " + barColor + ";");

        return new VBox(5, top, pb);
    }

    // =========================================================
    // RECENT FARMER REVIEWS
    // =========================================================
    private static VBox createReviewsSection() {
        Text title = new Text("💬 Recent Client Farmer Ratings & Feedback");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 18px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text sub = new Text("Verified ratings from landowners and farmers across recent rental operations.");
        sub.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #4B5563;");

        HBox rev1 = createReviewCard("Balasaheb Shirole", "⭐⭐⭐⭐⭐ (5.0)", "14 Aug 2026", "Baramati Sugarcane Plot", "Ramesh is one of the best tractor operators in Baramati. Clean tillage with zero soil clumping!");
        HBox rev2 = createReviewCard("Vikas More", "⭐⭐⭐⭐⭐ (5.0)", "10 Aug 2026", "Indapur Wheat Field", "Operated the combine harvester very carefully without any grain loss. Completed 6 acres in record time.");
        HBox rev3 = createReviewCard("Kiran Bhosale", "⭐⭐⭐⭐☆ (4.8)", "05 Aug 2026", "Daund Paddy Field", "Prompt arrival and good laser land leveling precision on our sugarcane plots. Very polite operator.");

        return new VBox(12, new VBox(2, title, sub), rev1, rev2, rev3);
    }

    private static HBox createReviewCard(String farmer, String rating, String date, String location, String comment) {
        Text nameText = new Text("👨‍🌾 " + farmer);
        nameText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13.5px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text locText = new Text("📍 " + location);
        locText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-fill: #4B5563;");

        VBox userBox = new VBox(1, nameText, locText);

        Text ratingText = new Text(rating);
        ratingText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-font-weight: bold; -fx-fill: #E65100;");

        Text dateText = new Text("📅 " + date);
        dateText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-fill: #4B5563;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox top = new HBox(14, userBox, ratingText, spacer, dateText);
        top.setAlignment(Pos.CENTER_LEFT);

        Text commentText = new Text("\"" + comment + "\"");
        commentText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-fill: #374151; -fx-font-style: italic;");

        VBox b = new VBox(6, top, commentText);
        b.setPadding(new Insets(12, 16, 12, 16));
        b.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 10; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 10;");

        HBox wrapper = new HBox(b);
        HBox.setHgrow(b, Priority.ALWAYS);
        return wrapper;
    }
}
