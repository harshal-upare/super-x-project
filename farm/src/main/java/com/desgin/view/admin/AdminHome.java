package com.desgin.view.admin;

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
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

@SuppressWarnings("unchecked")
public class AdminHome {

    public static ScrollPane getPage(StackPane root) {
        // Top Welcome Header
        Text welcomeText = new Text("Platform Master Command & Operations 🛡️");
        welcomeText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 24px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text subtitleText = new Text("Real-time ecosystem intelligence, multi-district fleet deployment telemetry, escrow liquidity, and system moderation.");
        subtitleText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-fill: #4B5563;");

        VBox headerBox = new VBox(3, welcomeText, subtitleText);

        // 4 Master KPI Cards in responsive GridPane
        GridPane kpiGrid = createMasterKPIGrid(root);

        // Quick Action Command Strip
        FlowPane quickActionStrip = createAdminActionStrip(root);

        // Charts Row 1: AreaChart (GMV vs Commission) + PieChart (Category Share)
        GridPane chartsGrid1 = createChartsGrid1();

        // Charts Row 2: BarChart (District Demand) + System Health Audit Matrix
        GridPane chartsGrid2 = createChartsGrid2();

        // Live System Audit Activity Feed
        VBox activityFeed = createLiveActivityFeed();

        VBox content = new VBox(20, headerBox, kpiGrid, quickActionStrip, chartsGrid1, chartsGrid2, activityFeed);
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
        VBox c1 = createMetricCard("👥 Total Registered Users", "1,284", "▲ +14% MoM", "840 Farmers • 320 Providers • 124 Operators", "#1B4332", "#FFFFFF", () -> {
            AdminLeftSideBar.setActiveButton(AdminLeftSideBar.usersBtn, AdminLeftSideBar.navigationButtons);
            AdminDashboard.borderPane.setCenter(UserManagement.getPage(root));
        });

        VBox c2 = createMetricCard("🚜 Listed Fleet", "485 Units", "12 Pending", "390 Live Active • 83 In-Shift", "#2E7D32", "#E8F5E9", () -> {
            AdminLeftSideBar.setActiveButton(AdminLeftSideBar.approvalsBtn, AdminLeftSideBar.navigationButtons);
            AdminDashboard.borderPane.setCenter(MachineryApprovals.getPage(root));
        });

        VBox c3 = createMetricCard("💳 Total Rental GMV", "₹48.5 Lakh", "▲ +24.5%", "₹6.45L secured in escrow", "#374151", "#FFFFFF", () -> {
            AdminLeftSideBar.setActiveButton(AdminLeftSideBar.escrowBtn, AdminLeftSideBar.navigationButtons);
            AdminDashboard.borderPane.setCenter(EscrowFinancials.getPage(root));
        });

        VBox c4 = createMetricCard("🏦 Platform Net Cut (7%)", "₹3,39,500", "● 100% Realized", "Direct Platform Earnings YTD", "#2E7D32", "#E8F5E9", () -> {
            AdminLeftSideBar.setActiveButton(AdminLeftSideBar.escrowBtn, AdminLeftSideBar.navigationButtons);
            AdminDashboard.borderPane.setCenter(EscrowFinancials.getPage(root));
        });

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

    private static VBox createMetricCard(String title, String value, String badge, String sub, String color, String bgColor, Runnable onClick) {
        Text t = new Text(title);
        t.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-fill: #4B5563;");

        Text b = new Text(badge);
        b.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 9.5px; -fx-font-weight: bold; -fx-fill: " + color + ";");
        StackPane badgeBox = new StackPane(b);
        badgeBox.setPadding(new Insets(2, 6, 2, 6));
        badgeBox.setStyle("-fx-background-color: rgba(255,255,255,0.85); -fx-background-radius: 6; -fx-border-color: #E2EBE5; -fx-border-radius: 6;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox top = new HBox(6, t, spacer, badgeBox);
        top.setAlignment(Pos.CENTER_LEFT);

        Text v = new Text(value);
        v.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 20px; -fx-font-weight: bold; -fx-fill: " + color + ";");

        Text s = new Text(sub);
        s.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 10px; -fx-fill: #374151;");

        VBox card = new VBox(5, top, v, s);
        card.setPadding(new Insets(14));
        card.setMinWidth(0);
        card.setMaxWidth(Double.MAX_VALUE);
        card.setStyle("-fx-background-color: " + bgColor + "; -fx-background-radius: 12; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 12; -fx-cursor: hand;");

        card.setOnMouseEntered(e -> {
            card.setStyle("-fx-background-color: #F0FDF4; -fx-background-radius: 12; -fx-border-color: #2D6A4F; -fx-border-width: 1.5; -fx-border-radius: 12; -fx-effect: dropshadow(gaussian, rgba(27,67,50,0.18), 8, 0.2, 0, 2); -fx-cursor: hand;");
            card.setTranslateY(-2);
        });

        card.setOnMouseExited(e -> {
            card.setStyle("-fx-background-color: " + bgColor + "; -fx-background-radius: 12; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 12; -fx-cursor: hand;");
            card.setTranslateY(0);
        });

        if (onClick != null) {
            card.setOnMouseClicked(e -> onClick.run());
        }

        return card;
    }

    private static FlowPane createAdminActionStrip(StackPane root) {
        Button b1 = createAdminBtn("🚜 Verify Machinery (12)", "#2E7D32", () -> {
            AdminLeftSideBar.setActiveButton(AdminLeftSideBar.approvalsBtn, AdminLeftSideBar.navigationButtons);
            AdminDashboard.borderPane.setCenter(MachineryApprovals.getPage(root));
        });

        Button b2 = createAdminBtn("👥 Review KYC (5)", "#2D6A4F", () -> {
            AdminLeftSideBar.setActiveButton(AdminLeftSideBar.usersBtn, AdminLeftSideBar.navigationButtons);
            AdminDashboard.borderPane.setCenter(UserManagement.getPage(root));
        });

        Button b3 = createAdminBtn("💰 Audit Escrow", "#374151", () -> {
            AdminLeftSideBar.setActiveButton(AdminLeftSideBar.escrowBtn, AdminLeftSideBar.navigationButtons);
            AdminDashboard.borderPane.setCenter(EscrowFinancials.getPage(root));
        });

        Button b4 = createAdminBtn("⚖ Resolve Disputes (2)", "#8B3A3A", () -> {
            AdminLeftSideBar.setActiveButton(AdminLeftSideBar.disputesBtn, AdminLeftSideBar.navigationButtons);
            AdminDashboard.borderPane.setCenter(DisputeResolution.getPage(root));
        });

        FlowPane bar = new FlowPane(10, 10, b1, b2, b3, b4);
        bar.setAlignment(Pos.CENTER_LEFT);
        bar.setMinWidth(0);
        bar.setMaxWidth(Double.MAX_VALUE);
        return bar;
    }

    private static Button createAdminBtn(String text, String bg, Runnable action) {
        Button btn = new Button(text);
        btn.setPrefHeight(36);
        btn.setStyle("-fx-background-color: " + bg + "; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 0 14 0 14;");
        btn.setOnAction(e -> action.run());
        return btn;
    }

    // =========================================================
    // CHARTS ROW 1: AreaChart (GMV vs Commission) + PieChart (Category Distribution)
    // =========================================================
    private static GridPane createChartsGrid1() {
        VBox chart1 = createGMVAreaChart();
        VBox chart2 = createCategoryPieChart();

        GridPane grid = new GridPane();
        grid.setHgap(16);
        grid.setVgap(16);
        grid.setMinWidth(0);
        grid.setMaxWidth(Double.MAX_VALUE);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(55);
        col1.setHgrow(Priority.ALWAYS);

        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(45);
        col2.setHgrow(Priority.ALWAYS);

        grid.getColumnConstraints().addAll(col1, col2);

        grid.add(chart1, 0, 0);
        grid.add(chart2, 1, 0);
        return grid;
    }

    private static VBox createGMVAreaChart() {
        Text cardTitle = new Text("📈 Platform Booking Volume & Net Commission");
        cardTitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 15px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text cardSub = new Text("Gross Booking Volume (₹k) vs 7% Platform Commission (₹k)");
        cardSub.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-fill: #4B5563;");

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Operating Month (2026)");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Amount (₹ in Thousands)");

        AreaChart<String, Number> areaChart = new AreaChart<>(xAxis, yAxis);
        areaChart.setAnimated(false);
        areaChart.setLegendVisible(true);
        areaChart.setPrefHeight(250);
        areaChart.setMinHeight(220);
        areaChart.setMinWidth(0);
        areaChart.setMaxWidth(Double.MAX_VALUE);

        XYChart.Series<String, Number> seriesGMV = new XYChart.Series<>();
        seriesGMV.setName("Gross Volume (₹k)");
        seriesGMV.getData().add(new XYChart.Data<>("Mar", 420));
        seriesGMV.getData().add(new XYChart.Data<>("Apr", 580));
        seriesGMV.getData().add(new XYChart.Data<>("May", 720));
        seriesGMV.getData().add(new XYChart.Data<>("Jun", 650));
        seriesGMV.getData().add(new XYChart.Data<>("Jul", 890));
        seriesGMV.getData().add(new XYChart.Data<>("Aug", 1140));

        XYChart.Series<String, Number> seriesComm = new XYChart.Series<>();
        seriesComm.setName("Platform Cut (₹k)");
        seriesComm.getData().add(new XYChart.Data<>("Mar", 29.4));
        seriesComm.getData().add(new XYChart.Data<>("Apr", 40.6));
        seriesComm.getData().add(new XYChart.Data<>("May", 50.4));
        seriesComm.getData().add(new XYChart.Data<>("Jun", 45.5));
        seriesComm.getData().add(new XYChart.Data<>("Jul", 62.3));
        seriesComm.getData().add(new XYChart.Data<>("Aug", 79.8));

        areaChart.getData().addAll(seriesGMV, seriesComm);

        VBox card = new VBox(6, new VBox(2, cardTitle, cardSub), areaChart);
        card.setPadding(new Insets(16));
        card.setMinWidth(0);
        card.setMaxWidth(Double.MAX_VALUE);
        card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 14; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 14;");
        return card;
    }

    private static VBox createCategoryPieChart() {
        Text cardTitle = new Text("🥧 Fleet Category Distribution");
        cardTitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 15px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text cardSub = new Text("485 units across Western Maharashtra clusters");
        cardSub.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-fill: #4B5563;");

        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList(
            new PieChart.Data("Tractors (42%)", 204),
            new PieChart.Data("Harvesters (26%)", 126),
            new PieChart.Data("Tillers (18%)", 87),
            new PieChart.Data("Drones (14%)", 68)
        );

        PieChart pieChart = new PieChart(pieData);
        pieChart.setAnimated(false);
        pieChart.setLegendVisible(true);
        pieChart.setPrefHeight(250);
        pieChart.setMinHeight(220);
        pieChart.setMinWidth(0);
        pieChart.setMaxWidth(Double.MAX_VALUE);

        VBox card = new VBox(6, new VBox(2, cardTitle, cardSub), pieChart);
        card.setPadding(new Insets(16));
        card.setMinWidth(0);
        card.setMaxWidth(Double.MAX_VALUE);
        card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 14; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 14;");
        return card;
    }

    // =========================================================
    // CHARTS ROW 2: BarChart (District Demand) + Security Matrix
    // =========================================================
    private static GridPane createChartsGrid2() {
        VBox barCard = createDistrictDemandBarChart();
        VBox securityCard = createSystemSecurityCard();

        GridPane grid = new GridPane();
        grid.setHgap(16);
        grid.setVgap(16);
        grid.setMinWidth(0);
        grid.setMaxWidth(Double.MAX_VALUE);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(50);
        col1.setHgrow(Priority.ALWAYS);

        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(50);
        col2.setHgrow(Priority.ALWAYS);

        grid.getColumnConstraints().addAll(col1, col2);

        grid.add(barCard, 0, 0);
        grid.add(securityCard, 1, 0);
        return grid;
    }

    private static VBox createDistrictDemandBarChart() {
        Text cardTitle = new Text("📊 District-Wise Rental Velocity");
        cardTitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 15px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text cardSub = new Text("Active rental days logged across top agricultural clusters");
        cardSub.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-fill: #4B5563;");

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("District");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Rental Days");

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setAnimated(false);
        barChart.setLegendVisible(false);
        barChart.setPrefHeight(250);
        barChart.setMinHeight(220);
        barChart.setMinWidth(0);
        barChart.setMaxWidth(Double.MAX_VALUE);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Rental Days");
        series.getData().add(new XYChart.Data<>("Pune", 380));
        series.getData().add(new XYChart.Data<>("Baramati", 495));
        series.getData().add(new XYChart.Data<>("Indapur", 310));
        series.getData().add(new XYChart.Data<>("Satara", 240));
        series.getData().add(new XYChart.Data<>("A'nagar", 290));

        barChart.getData().add(series);

        VBox card = new VBox(6, new VBox(2, cardTitle, cardSub), barChart);
        card.setPadding(new Insets(16));
        card.setMinWidth(0);
        card.setMaxWidth(Double.MAX_VALUE);
        card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 14; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 14;");
        return card;
    }

    private static VBox createSystemSecurityCard() {
        Text cardTitle = new Text("🛡️ Platform Integrity & Escrow Matrix");
        cardTitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 15px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text cardSub = new Text("System-wide compliance and nodal trust status");
        cardSub.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-fill: #4B5563;");

        VBox i1 = createAuditItem("🔒 RBI-Compliant Escrow", "₹6,45,000 in 100% nodal trust", "OPTIMAL", "#2E7D32");
        VBox i2 = createAuditItem("🚜 Machinery RC Verification", "98.4% of active fleet verified", "COMPLIANT", "#2E7D32");
        VBox i3 = createAuditItem("👨‍✈️ Operator License Audit", "124 operators certified", "VERIFIED", "#1976D2");
        VBox i4 = createAuditItem("📡 Telematics Stream", "390 machines syncing live", "ACTIVE", "#388E3C");

        VBox list = new VBox(6, i1, i2, i3, i4);
        list.setMinWidth(0);

        VBox card = new VBox(8, new VBox(2, cardTitle, cardSub), list);
        card.setPadding(new Insets(16));
        card.setMinWidth(0);
        card.setMaxWidth(Double.MAX_VALUE);
        card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 14; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 14;");
        return card;
    }

    private static VBox createAuditItem(String title, String desc, String badgeText, String badgeColor) {
        Text t = new Text(title);
        t.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text d = new Text(desc);
        d.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 10px; -fx-fill: #4B5563;");

        VBox left = new VBox(1, t, d);
        left.setMinWidth(0);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Text badge = new Text(badgeText);
        badge.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 9px; -fx-font-weight: bold; -fx-fill: white;");

        StackPane badgeContainer = new StackPane(badge);
        badgeContainer.setPadding(new Insets(2, 6, 2, 6));
        badgeContainer.setStyle("-fx-background-color: " + badgeColor + "; -fx-background-radius: 8;");

        HBox row = new HBox(6, left, spacer, badgeContainer);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(6, 8, 6, 8));
        row.setMinWidth(0);
        row.setStyle("-fx-background-color: #FFFDFC; -fx-background-radius: 8; -fx-border-color: #E2D7CB; -fx-border-radius: 8; -fx-border-width: 1;");

        return new VBox(row);
    }

    private static VBox createLiveActivityFeed() {
        Text title = new Text("⚡ Real-Time Platform Event Feed");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 16px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        HBox e1 = createFeedRow("🚜 New Machinery Submitted", "Provider Rajesh Patil submitted 'Swaraj 855 FE 52HP' for verification.", "2 mins ago", "PENDING", "#E65100");
        HBox e2 = createFeedRow("💰 Escrow Payout Released", "₹12,600 released to Provider (Balasaheb completed 5-day harvest).", "14 mins ago", "SETTLED", "#2E7D32");
        HBox e3 = createFeedRow("👨‍✈️ Operator KYC Submitted", "Operator Ramesh Chavan uploaded Commercial Driving License.", "45 mins ago", "VERIFIED", "#1976D2");
        HBox e4 = createFeedRow("🌾 New Booking Confirmed", "Farmer Anand Kadam booked Harvester for ₹14,000 (Escrow Locked).", "1 hr ago", "ACTIVE", "#2E7D32");

        VBox box = new VBox(8, title, e1, e2, e3, e4);
        box.setMinWidth(0);
        return box;
    }

    private static HBox createFeedRow(String title, String desc, String time, String status, String statusColor) {
        Text t = new Text(title);
        t.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text d = new Text(desc);
        d.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-fill: #374151;");

        VBox left = new VBox(1, t, d);
        left.setMinWidth(0);

        Text tm = new Text("⏱ " + time);
        tm.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 10.5px; -fx-fill: #4B5563;");

        Label badge = new Label(status);
        badge.setStyle("-fx-background-color: " + statusColor + "; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 9.5px; -fx-font-weight: bold; -fx-padding: 2 6 2 6; -fx-background-radius: 4;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox row = new HBox(10, left, spacer, tm, badge);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(10, 14, 10, 14));
        row.setMinWidth(0);
        row.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 10; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 10;");

        return row;
    }
}
