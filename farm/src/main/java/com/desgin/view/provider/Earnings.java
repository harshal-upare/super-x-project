package com.desgin.view.provider;

import java.util.ArrayList;
import java.util.List;

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
import javafx.scene.control.ComboBox;
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


/**
 * Enhanced Provider Earnings & Financial Settlements View
 * Featuring real JavaFX Charts (AreaChart, BarChart, PieChart),
 * Live Escrow Security Pipeline, and Digital Receipts.
 */
@SuppressWarnings("unchecked")
public class Earnings {

    public static class Transaction {
        public String txnId;
        public String title;
        public String farmerName;
        public String machine;
        public String date;
        public String channel;
        public int amount;
        public String type; // "CREDIT", "WITHDRAWAL"
        public String status; // "SETTLED", "PROCESSING"
        public String refNo;

        public Transaction(String txnId, String title, String farmerName, String machine, String date, String channel, int amount, String type, String status, String refNo) {
            this.txnId = txnId;
            this.title = title;
            this.farmerName = farmerName;
            this.machine = machine;
            this.date = date;
            this.channel = channel;
            this.amount = amount;
            this.type = type;
            this.status = status;
            this.refNo = refNo;
        }
    }

    private static int totalLifetime = 485200;
    private static int availableBalance = 48200;
    private static int pendingEscrow = 12400;
    private static int totalWithdrawn = 424600;

    private static List<Transaction> txnList = new ArrayList<>();
    private static VBox txnListContainer;
    private static Text availBalanceText;
    private static Text withdrawnText;
    private static String activeTxnFilter = "ALL";
    private static StackPane rootPane;

    static {
        initTransactions();
    }

    private static void initTransactions() {
        if (!txnList.isEmpty()) return;
        txnList.add(new Transaction("#TXN-8921", "Harvester Rental Payout (Balasaheb Shirole)", "Balasaheb Shirole", "Kartar 4000 Harvester (14ft)", "14 Aug 2026", "Direct Bank IMPS", 12600, "CREDIT", "SETTLED", "IMPS-902188412"));
        txnList.add(new Transaction("#TXN-8894", "Rotavator 3-Day Job Payout (Vikas More)", "Vikas More", "Shaktiman Semi-Champion 7ft", "13 Aug 2026", "Direct Bank IMPS", 2280, "CREDIT", "SETTLED", "IMPS-899471029"));
        txnList.add(new Transaction("#WD-5021", "Provider Bank Withdrawal to HDFC Bank •••• 8842", "Rajesh Agro Services", "Fleet Settlement Account", "11 Aug 2026", "IMPS Transfer", 25000, "WITHDRAWAL", "SETTLED", "HDFC-WD-502189"));
        txnList.add(new Transaction("#TXN-8850", "Tractor 5-Day Rental Payout (Ganesh Jadhav)", "Ganesh Jadhav", "Mahindra 575 DI Sarpanch 45HP", "10 Aug 2026", "Direct Bank IMPS", 7125, "CREDIT", "SETTLED", "IMPS-885023910"));
        txnList.add(new Transaction("#TXN-8790", "Drone Spraying Service Payout (Kiran Bhosale)", "Kiran Bhosale", "Agri-Drone 16L Autonomous Sprayer", "05 Aug 2026", "Direct Bank IMPS", 1710, "CREDIT", "SETTLED", "IMPS-879011834"));
        txnList.add(new Transaction("#WD-4980", "Provider Bank Withdrawal to SBI •••• 4120", "Rajesh Agro Services", "Fleet Settlement Account", "01 Aug 2026", "NEFT Transfer", 40000, "WITHDRAWAL", "SETTLED", "SBIN-WD-498002"));
    }

    public static ScrollPane getEarningsSection(StackPane root) {
        rootPane = root;

        // ================= HEADER & ACTIONS =================
        Text headerTitle = new Text("Earnings & Payout Settlements");
        headerTitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 26px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Label headerSubtitle = new Label("Monitor your machinery rental earnings, analyze revenue curves by equipment, track escrow releases, and initiate instant bank withdrawals.");
        headerSubtitle.setWrapText(true);
        headerSubtitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-text-fill: #4B5563;");

        VBox titleBox = new VBox(4, headerTitle, headerSubtitle);
        titleBox.setMinWidth(0);
        HBox.setHgrow(titleBox, Priority.ALWAYS);

        Button withdrawBtn = new Button("💸  Withdraw Funds to Bank");
        withdrawBtn.setPrefHeight(42);
        withdrawBtn.setStyle("-fx-background-color: #2E7D32; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 0 20 0 20;");
        withdrawBtn.setOnAction(e -> showWithdrawModal(root));
        withdrawBtn.setMinWidth(Region.USE_PREF_SIZE);

        // Security / Escrow Badge
        HBox escrowBadge = createEscrowSecurityBadge();

        HBox rightActions = new HBox(12, escrowBadge, withdrawBtn);
        rightActions.setAlignment(Pos.CENTER_RIGHT);

        HBox topBar = new HBox(15, titleBox, rightActions);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setMinWidth(0);

        // ================= TIME-RANGE FILTER BAR =================
        HBox filterBar = createTimeFilterBar();

        // ================= 4 FINANCIAL KPI CARDS =================
        HBox metricRow = createFinancialMetrics();

        // ================= CHARTS ROW 1: AreaChart (Monthly Revenue) + PieChart (Equipment Breakdown) =================
        HBox chartsRow1 = createChartsRow1();

        // ================= CHARTS ROW 2: BarChart (Weekly Cash Flow) + Escrow Security Matrix =================
        HBox chartsRow2 = createChartsRow2();

        // ================= TRANSACTION & SETTLEMENT HISTORY =================
        VBox txnSection = createTransactionHistorySection(root);

        VBox content = new VBox(22, topBar, filterBar, metricRow, chartsRow1, chartsRow2, txnSection);
        content.setPadding(new Insets(25, 30, 40, 30));
        content.setStyle("-fx-background-color: transparent;");
        content.setMinWidth(0);

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        return scrollPane;
    }

    private static HBox createEscrowSecurityBadge() {
        Text icon = new Text("🛡️");
        icon.setStyle("-fx-font-size: 16px;");

        Text title = new Text("100% ESCROW PROTECTED");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-fill: #1B5E20;");

        Text sub = new Text("Instant 24/7 IMPS & UPI Settlement");
        sub.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 9.5px; -fx-fill: #2E7D32;");

        VBox textBox = new VBox(1, title, sub);
        HBox box = new HBox(8, icon, textBox);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setPadding(new Insets(6, 14, 6, 14));
        box.setStyle("-fx-background-color: #E8F5E9; -fx-background-radius: 20; -fx-border-color: #A5D6A7; -fx-border-radius: 20; -fx-border-width: 1;");
        box.setMinWidth(Region.USE_PREF_SIZE);
        return box;
    }

    private static HBox createTimeFilterBar() {
        Button btn1 = createFilterPill("⚡ Kharif Season 2026", true);
        Button btn2 = createFilterPill("📅 This Month (Aug 2026)", false);
        Button btn3 = createFilterPill("📊 6-Month Trajectory", false);
        Button btn4 = createFilterPill("🌍 All-Time Lifetime", false);

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

        Label bankStatus = new Label("🏦 Primary Payout Bank: HDFC Bank (•••• 8842) • Instant Active");
        bankStatus.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-text-fill: #1B4332; -fx-font-weight: bold;");
        bankStatus.setMinWidth(Region.USE_PREF_SIZE);

        HBox bar = new HBox(10, btn1, btn2, btn3, btn4, spacer, bankStatus);
        bar.setAlignment(Pos.CENTER_LEFT);
        bar.setMinWidth(0);
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

    private static HBox createFinancialMetrics() {
        VBox c1 = createMetricCard("💰 Lifetime Gross Revenue", "₹" + String.format("%,d", totalLifetime), "▲ +18.4% vs last cycle", "From 142 completed rentals", "#1B4332", "#FFFFFF");

        availBalanceText = new Text("₹" + String.format("%,d", availableBalance));
        availBalanceText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 22px; -fx-font-weight: bold; -fx-fill: #2E7D32;");
        VBox c2 = createCustomMetricCard("🏦 Available for Payout", availBalanceText, "● Ready to Withdraw", "Instant 24/7 bank transfer", "#2E7D32", "#E8F5E9");

        VBox c3 = createMetricCard("⏱ Pending Escrow", "₹" + String.format("%,d", pendingEscrow), "3 Active Jobs", "Releases upon farmer sign-off", "#E65100", "#FFF3E0");

        withdrawnText = new Text("₹" + String.format("%,d", totalWithdrawn));
        withdrawnText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 22px; -fx-font-weight: bold; -fx-fill: #374151;");
        VBox c4 = createCustomMetricCard("💳 Total Settled to Bank", withdrawnText, "18 Withdrawals", "100% successful transfers", "#374151", "#FFFFFF");

        c1.setMinWidth(0);
        c2.setMinWidth(0);
        c3.setMinWidth(0);
        c4.setMinWidth(0);

        HBox row = new HBox(15, c1, c2, c3, c4);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setMinWidth(0);
        HBox.setHgrow(c1, Priority.ALWAYS);
        HBox.setHgrow(c2, Priority.ALWAYS);
        HBox.setHgrow(c3, Priority.ALWAYS);
        HBox.setHgrow(c4, Priority.ALWAYS);
        return row;
    }

    private static VBox createMetricCard(String title, String value, String badge, String sub, String color, String bgColor) {
        Text t = new Text(title);
        t.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #4B5563;");

        Text b = new Text(badge);
        b.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 10px; -fx-font-weight: bold; -fx-fill: " + color + ";");
        StackPane badgeBox = new StackPane(b);
        badgeBox.setPadding(new Insets(2, 6, 2, 6));
        badgeBox.setStyle("-fx-background-color: rgba(255,255,255,0.8); -fx-background-radius: 6; -fx-border-color: #E2EBE5; -fx-border-radius: 6;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox top = new HBox(t, spacer, badgeBox);
        top.setAlignment(Pos.CENTER_LEFT);

        Text v = new Text(value);
        v.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 22px; -fx-font-weight: bold; -fx-fill: " + color + ";");

        Text s = new Text(sub);
        s.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 10.5px; -fx-fill: #374151;");

        VBox card = new VBox(6, top, v, s);
        card.setPadding(new Insets(16));
        card.setStyle("-fx-background-color: " + bgColor + "; -fx-background-radius: 12; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 12;");
        card.setMinWidth(0);
        return card;
    }

    private static VBox createCustomMetricCard(String title, Text vText, String badge, String sub, String color, String bgColor) {
        Text t = new Text(title);
        t.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #4B5563;");

        Text b = new Text(badge);
        b.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 10px; -fx-font-weight: bold; -fx-fill: " + color + ";");
        StackPane badgeBox = new StackPane(b);
        badgeBox.setPadding(new Insets(2, 6, 2, 6));
        badgeBox.setStyle("-fx-background-color: rgba(255,255,255,0.8); -fx-background-radius: 6; -fx-border-color: #E2EBE5; -fx-border-radius: 6;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox top = new HBox(t, spacer, badgeBox);
        top.setAlignment(Pos.CENTER_LEFT);

        Text s = new Text(sub);
        s.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 10.5px; -fx-fill: #374151;");

        VBox card = new VBox(6, top, vText, s);
        card.setPadding(new Insets(16));
        card.setStyle("-fx-background-color: " + bgColor + "; -fx-background-radius: 12; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 12;");
        card.setMinWidth(0);
        return card;
    }

    // =========================================================
    // CHARTS ROW 1: AreaChart (Monthly Revenue Trend) + PieChart (Machinery Share)
    // =========================================================
    private static HBox createChartsRow1() {
        VBox chart1 = createMonthlyRevenueAreaChart();
        VBox chart2 = createCategoryRevenuePieChart();

        chart1.setMinWidth(0);
        chart2.setMinWidth(0);

        HBox row = new HBox(18, chart1, chart2);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setMinWidth(0);
        HBox.setHgrow(chart1, Priority.ALWAYS);
        HBox.setHgrow(chart2, Priority.ALWAYS);
        return row;
    }

    private static VBox createMonthlyRevenueAreaChart() {
        Text cardTitle = new Text("📈 Monthly Rental Revenue & Settlement Curve");
        cardTitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 16px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text cardSub = new Text("Gross booking income vs net settled payouts across recent months (in ₹)");
        cardSub.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-fill: #4B5563;");

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Operating Month");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Amount (₹)");

        AreaChart<String, Number> areaChart = new AreaChart<>(xAxis, yAxis);
        areaChart.setAnimated(false);
        areaChart.setLegendVisible(true);
        areaChart.setPrefHeight(275);
        areaChart.setMinHeight(275);
        areaChart.setMinWidth(0);

        XYChart.Series<String, Number> seriesGross = new XYChart.Series<>();
        seriesGross.setName("Gross Rental Revenue (₹)");
        seriesGross.getData().add(new XYChart.Data<>("Mar 26", 42000));
        seriesGross.getData().add(new XYChart.Data<>("Apr 26", 58000));
        seriesGross.getData().add(new XYChart.Data<>("May 26", 72000));
        seriesGross.getData().add(new XYChart.Data<>("Jun 26", 65000));
        seriesGross.getData().add(new XYChart.Data<>("Jul 26", 89000));
        seriesGross.getData().add(new XYChart.Data<>("Aug 26", 108000));

        XYChart.Series<String, Number> seriesSettled = new XYChart.Series<>();
        seriesSettled.setName("Bank Payouts Settled (₹)");
        seriesSettled.getData().add(new XYChart.Data<>("Mar 26", 36000));
        seriesSettled.getData().add(new XYChart.Data<>("Apr 26", 51000));
        seriesSettled.getData().add(new XYChart.Data<>("May 26", 64000));
        seriesSettled.getData().add(new XYChart.Data<>("Jun 26", 59000));
        seriesSettled.getData().add(new XYChart.Data<>("Jul 26", 78000));
        seriesSettled.getData().add(new XYChart.Data<>("Aug 26", 96000));

        areaChart.getData().addAll(seriesGross, seriesSettled);

        VBox card = new VBox(8, new VBox(2, cardTitle, cardSub), areaChart);
        card.setPadding(new Insets(18));
        card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 14; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 14;");
        card.setMinWidth(0);
        return card;
    }

    private static VBox createCategoryRevenuePieChart() {
        Text cardTitle = new Text("🥧 Machinery Revenue Contribution Share");
        cardTitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 16px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text cardSub = new Text("Proportion of rental income generated per equipment category");
        cardSub.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-fill: #4B5563;");

        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList(
            new PieChart.Data("Combine Harvesters (45%)", 217000),
            new PieChart.Data("Tractors (32%)", 155000),
            new PieChart.Data("Rotavators & Tillers (14%)", 68000),
            new PieChart.Data("Agri Drones (9%)", 45200)
        );

        PieChart pieChart = new PieChart(pieData);
        pieChart.setAnimated(false);
        pieChart.setLegendVisible(true);
        pieChart.setPrefHeight(275);
        pieChart.setMinHeight(275);
        pieChart.setMinWidth(0);

        VBox card = new VBox(8, new VBox(2, cardTitle, cardSub), pieChart);
        card.setPadding(new Insets(18));
        card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 14; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 14;");
        card.setMinWidth(0);
        return card;
    }

    // =========================================================
    // CHARTS ROW 2: BarChart (Weekly Cash Flow) + Escrow Security Matrix
    // =========================================================
    private static HBox createChartsRow2() {
        VBox barCard = createWeeklyInflowBarChart();
        VBox escrowCard = createEscrowPipelineCard();

        barCard.setMinWidth(0);
        escrowCard.setMinWidth(0);

        HBox row = new HBox(18, barCard, escrowCard);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setMinWidth(0);
        HBox.setHgrow(barCard, Priority.ALWAYS);
        HBox.setHgrow(escrowCard, Priority.ALWAYS);
        return row;
    }

    private static VBox createWeeklyInflowBarChart() {
        Text cardTitle = new Text("📊 Day-of-Week Inflow Cash Velocity");
        cardTitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 16px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text cardSub = new Text("Daily booking revenue distribution showing peak agricultural weekend demand");
        cardSub.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-fill: #4B5563;");

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Day of Week");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Daily Revenue (₹)");

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setAnimated(false);
        barChart.setLegendVisible(false);
        barChart.setPrefHeight(270);
        barChart.setMinHeight(270);
        barChart.setMinWidth(0);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Revenue (₹)");
        series.getData().add(new XYChart.Data<>("Mon", 14200));
        series.getData().add(new XYChart.Data<>("Tue", 18600));
        series.getData().add(new XYChart.Data<>("Wed", 22400));
        series.getData().add(new XYChart.Data<>("Thu", 19800));
        series.getData().add(new XYChart.Data<>("Fri", 28500));
        series.getData().add(new XYChart.Data<>("Sat (Peak)", 38200));
        series.getData().add(new XYChart.Data<>("Sun", 32100));

        barChart.getData().add(series);

        VBox card = new VBox(8, new VBox(2, cardTitle, cardSub), barChart);
        card.setPadding(new Insets(18));
        card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 14; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 14;");
        card.setMinWidth(0);
        return card;
    }

    private static VBox createEscrowPipelineCard() {
        Text cardTitle = new Text("🛡️ Escrow Settlement Pipeline & Financial Guarantee");
        cardTitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 16px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text cardSub = new Text("Automated 3-tier escrow security protecting provider funds against payment default");
        cardSub.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-fill: #4B5563;");

        VBox step1 = createEscrowStep("1. 💳 Farmer Escrow Deposit", "100% of rental tariff is locked in RBI-compliant escrow before machine dispatch.", "LOCKED", "#1976D2");
        VBox step2 = createEscrowStep("2. 🚜 Machine Telematics & Farmer OTP", "Hours and acreage verified in real-time via telematics sensor & farmer completion OTP.", "VERIFIED", "#2E7D32");
        VBox step3 = createEscrowStep("3. ⚡ Instant Bank Settlement", "Funds immediately transfer to provider balance; 24/7 withdrawable via IMPS / UPI.", "ACTIVE", "#388E3C");
        VBox step4 = createEscrowStep("4. ⚖ Zero-Default Dispute Protection", "Platform absorbs cancellation/weather disruption risk with 100% provider guarantee.", "PROTECTED", "#2D6A4F");

        VBox stepsList = new VBox(8, step1, step2, step3, step4);

        VBox card = new VBox(10, new VBox(2, cardTitle, cardSub), stepsList);
        card.setPadding(new Insets(18));
        card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 14; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 14;");
        card.setMinWidth(0);
        return card;
    }

    private static VBox createEscrowStep(String title, String desc, String badgeText, String badgeColor) {
        Text t = new Text(title);
        t.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Label d = new Label(desc);
        d.setWrapText(true);
        d.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 10.5px; -fx-text-fill: #4B5563;");

        VBox left = new VBox(2, t, d);
        left.setMinWidth(0);
        HBox.setHgrow(left, Priority.ALWAYS);

        Text badge = new Text(badgeText);
        badge.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 9.5px; -fx-font-weight: bold; -fx-fill: white;");

        StackPane badgeContainer = new StackPane(badge);
        badgeContainer.setPadding(new Insets(3, 8, 3, 8));
        badgeContainer.setStyle("-fx-background-color: " + badgeColor + "; -fx-background-radius: 10;");
        badgeContainer.setMinWidth(Region.USE_PREF_SIZE);

        HBox row = new HBox(8, left, badgeContainer);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(7, 10, 7, 10));
        row.setStyle("-fx-background-color: #FFFDFC; -fx-background-radius: 8; -fx-border-color: #E2D7CB; -fx-border-radius: 8; -fx-border-width: 1;");
        row.setMinWidth(0);

        return new VBox(row);
    }

    // =========================================================
    // TRANSACTION & SETTLEMENT HISTORY
    // =========================================================
    private static VBox createTransactionHistorySection(StackPane root) {
        Text title = new Text("Settlement & Withdrawal History");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 20px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        // Filter Pills for Transactions
        Button fAll = new Button("All Transactions");
        Button fCredits = new Button("Payout Credits (+)");
        Button fWithdrawals = new Button("Bank Withdrawals (-)");

        styleTxnFilterBtn(fAll, true);
        styleTxnFilterBtn(fCredits, false);
        styleTxnFilterBtn(fWithdrawals, false);

        ObservableList<Button> tFilters = FXCollections.observableArrayList(fAll, fCredits, fWithdrawals);

        fAll.setOnAction(e -> {
            activeTxnFilter = "ALL";
            for (Button b : tFilters) styleTxnFilterBtn(b, b == fAll);
            renderTxnList();
        });
        fCredits.setOnAction(e -> {
            activeTxnFilter = "CREDIT";
            for (Button b : tFilters) styleTxnFilterBtn(b, b == fCredits);
            renderTxnList();
        });
        fWithdrawals.setOnAction(e -> {
            activeTxnFilter = "WITHDRAWAL";
            for (Button b : tFilters) styleTxnFilterBtn(b, b == fWithdrawals);
            renderTxnList();
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        TextField searchTxn = new TextField();
        searchTxn.setPromptText("Search TXN ID or farmer name...");
        searchTxn.setPrefHeight(34);
        searchTxn.setPrefWidth(220);
        searchTxn.setMinWidth(140);
        searchTxn.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #E2EBE5; -fx-border-radius: 6; -fx-font-family: 'Poppins'; -fx-font-size: 11.5px;");
        searchTxn.textProperty().addListener((obs, oldV, newV) -> renderTxnListFiltered(newV.trim().toLowerCase()));

        HBox filterRow = new HBox(8, fAll, fCredits, fWithdrawals, spacer, searchTxn);
        filterRow.setAlignment(Pos.CENTER_LEFT);
        filterRow.setMinWidth(0);

        txnListContainer = new VBox(10);
        txnListContainer.setMinWidth(0);
        renderTxnList();

        VBox txnSection = new VBox(14, title, filterRow, txnListContainer);
        txnSection.setMinWidth(0);
        return txnSection;
    }

    private static void styleTxnFilterBtn(Button btn, boolean active) {
        if (active) {
            btn.setStyle("-fx-background-color: #1B4332; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 5 12 5 12;");
        } else {
            btn.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #374151; -fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-background-radius: 6; -fx-border-color: #E2EBE5; -fx-border-radius: 6; -fx-cursor: hand; -fx-padding: 5 12 5 12;");
        }
    }

    private static void renderTxnList() {
        renderTxnListFiltered("");
    }

    private static void renderTxnListFiltered(String query) {
        txnListContainer.getChildren().clear();

        for (Transaction txn : txnList) {
            if (!"ALL".equals(activeTxnFilter) && !txn.type.equals(activeTxnFilter)) {
                continue;
            }

            if (!query.isEmpty()) {
                boolean match = txn.txnId.toLowerCase().contains(query)
                        || txn.title.toLowerCase().contains(query)
                        || txn.farmerName.toLowerCase().contains(query)
                        || txn.machine.toLowerCase().contains(query);
                if (!match) continue;
            }

            Text id = new Text(txn.txnId);
            id.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-fill: #2D6A4F;");

            Text desc = new Text(txn.title);
            desc.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13.5px; -fx-font-weight: bold; -fx-fill: #1B4332;");

            Text dt = new Text("📅 " + txn.date + " • " + txn.channel + " • Ref: " + txn.refNo);
            dt.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-fill: #4B5563;");

            VBox infoBox = new VBox(2, id, desc, dt);
            infoBox.setMinWidth(0);
            HBox.setHgrow(infoBox, Priority.ALWAYS);

            boolean isCredit = "CREDIT".equals(txn.type);
            Text amt = new Text((isCredit ? "+ ₹" : "- ₹") + String.format("%,d", txn.amount));
            amt.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 16px; -fx-font-weight: bold; -fx-fill: " + (isCredit ? "#2E7D32" : "#8B3A3A") + ";");

            Label status = new Label(txn.status);
            status.setStyle("-fx-background-color: #E8F5E9; -fx-text-fill: #2E7D32; -fx-font-family: 'Poppins'; -fx-font-size: 10px; -fx-font-weight: bold; -fx-padding: 3 8 3 8; -fx-background-radius: 4;");
            status.setMinWidth(Region.USE_PREF_SIZE);

            Button receipt = new Button("📄 Receipt");
            receipt.setStyle("-fx-background-color: #E8F5E9; -fx-text-fill: #1B4332; -fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 5 12 5 12;");
            receipt.setMinWidth(Region.USE_PREF_SIZE);
            receipt.setOnAction(e -> showReceiptModal(txn, rootPane));

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            HBox row = new HBox(16, infoBox, spacer, amt, status, receipt);
            row.setAlignment(Pos.CENTER_LEFT);
            row.setPadding(new Insets(12, 16, 12, 16));
            row.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 10; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 10;");
            row.setMinWidth(0);

            txnListContainer.getChildren().add(row);
        }

        if (txnListContainer.getChildren().isEmpty()) {
            VBox emptyBox = new VBox(8);
            emptyBox.setAlignment(Pos.CENTER);
            emptyBox.setPadding(new Insets(30));
            Text empty = new Text("No transaction records found matching your filter.");
            empty.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 14px; -fx-fill: #4B5563;");
            txnListContainer.getChildren().add(emptyBox);
        }
    }

    // =========================================================
    // DIGITAL RECEIPT MODAL
    // =========================================================
    private static void showReceiptModal(Transaction txn, StackPane root) {
        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.55);");

        VBox modal = new VBox(14);
        modal.setPrefWidth(480);
        modal.setMaxWidth(480);
        modal.setPadding(new Insets(24));
        modal.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 16; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 16; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.35), 25, 0.3, 0, 8);");

        // Header with Logo / Platform Name
        Text appName = new Text("🌱 FarmEquip Digital Settlement Receipt");
        appName.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 17px; -fx-font-weight: bold; -fx-fill: #2E7D32;");

        Text subtitle = new Text("Verified Escrow Transaction Record • Official Tax Invoice");
        subtitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-fill: #4B5563;");

        VBox headBox = new VBox(2, appName, subtitle);

        GridPane grid = new GridPane();
        grid.setHgap(14);
        grid.setVgap(10);
        grid.setPadding(new Insets(12, 0, 12, 0));

        grid.add(createReceiptLabel("Transaction ID:"), 0, 0);
        grid.add(createReceiptValue(txn.txnId), 1, 0);

        grid.add(createReceiptLabel("Reference Auth:"), 0, 1);
        grid.add(createReceiptValue(txn.refNo), 1, 1);

        grid.add(createReceiptLabel("Date & Time:"), 0, 2);
        grid.add(createReceiptValue(txn.date + " (Electronic Transfer)"), 1, 2);

        grid.add(createReceiptLabel("Beneficiary / Party:"), 0, 3);
        grid.add(createReceiptValue(txn.farmerName), 1, 3);

        grid.add(createReceiptLabel("Equipment Deployed:"), 0, 4);
        grid.add(createReceiptValue(txn.machine), 1, 4);

        grid.add(createReceiptLabel("Payment Channel:"), 0, 5);
        grid.add(createReceiptValue(txn.channel), 1, 5);

        grid.add(createReceiptLabel("Settlement Status:"), 0, 6);
        Label stLabel = new Label("✔ " + txn.status);
        stLabel.setStyle("-fx-background-color: #E8F5E9; -fx-text-fill: #2E7D32; -fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-padding: 3 8 3 8; -fx-background-radius: 4;");
        grid.add(stLabel, 1, 6);

        // Total Amount Box
        boolean isCredit = "CREDIT".equals(txn.type);
        Text totalTitle = new Text(isCredit ? "Net Payout Credited:" : "Total Withdrawn:");
        totalTitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text totalValue = new Text("₹" + String.format("%,d", txn.amount));
        totalValue.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 20px; -fx-font-weight: bold; -fx-fill: " + (isCredit ? "#2E7D32" : "#8B3A3A") + ";");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox totalRow = new HBox(totalTitle, spacer, totalValue);
        totalRow.setAlignment(Pos.CENTER_LEFT);
        totalRow.setPadding(new Insets(12, 16, 12, 16));
        totalRow.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 8; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 8;");

        Button closeBtn = new Button("Close Receipt");
        closeBtn.setStyle("-fx-background-color: #374151; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 8 20 8 20;");
        closeBtn.setOnAction(e -> root.getChildren().remove(overlay));

        HBox btnBox = new HBox(closeBtn);
        btnBox.setAlignment(Pos.CENTER_RIGHT);

        modal.getChildren().addAll(headBox, grid, totalRow, btnBox);
        overlay.getChildren().add(modal);
        root.getChildren().add(overlay);
    }

    private static Text createReceiptLabel(String t) {
        Text txt = new Text(t);
        txt.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-fill: #4B5563;");
        return txt;
    }

    private static Text createReceiptValue(String t) {
        Text txt = new Text(t);
        txt.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-fill: #1B4332;");
        return txt;
    }

    // =========================================================
    // WITHDRAWAL MODAL
    // =========================================================
    private static void showWithdrawModal(StackPane root) {
        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.55);");

        VBox modal = new VBox(15);
        modal.setPrefWidth(480);
        modal.setMaxWidth(480);
        modal.setPadding(new Insets(24));
        modal.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 16; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 16; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.35), 25, 0.3, 0, 8);");

        Text title = new Text("Withdraw Funds to Bank Account");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 19px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text avail = new Text("Available Balance for Instant Withdrawal: ₹" + String.format("%,d", availableBalance));
        avail.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-font-weight: bold; -fx-fill: #2E7D32;");

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(12);

        TextField amountField = new TextField("10000");
        amountField.setPrefHeight(36);
        amountField.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #E2EBE5; -fx-border-radius: 6; -fx-font-family: 'Poppins'; -fx-font-size: 13px;");

        // Quick amount preset pills
        HBox presetBox = new HBox(6);
        int[] presets = {5000, 10000, 25000, availableBalance};
        for (int p : presets) {
            Button pBtn = new Button(p == availableBalance ? "Max (₹" + p + ")" : "₹" + p);
            pBtn.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #374151; -fx-font-family: 'Poppins'; -fx-font-size: 10.5px; -fx-background-radius: 12; -fx-border-color: #E2EBE5; -fx-border-radius: 12; -fx-cursor: hand;");
            pBtn.setOnAction(e -> amountField.setText(String.valueOf(p)));
            presetBox.getChildren().add(pBtn);
        }

        ComboBox<String> bankSelect = new ComboBox<>();
        bankSelect.getItems().addAll(
            "HDFC Bank (A/C •••• 8842, IFSC: HDFC0001024)",
            "State Bank of India (A/C •••• 4120, IFSC: SBIN0004512)",
            "Bank of Baroda (A/C •••• 9912, IFSC: BARB0PUNE)"
        );
        bankSelect.setValue("HDFC Bank (A/C •••• 8842, IFSC: HDFC0001024)");
        bankSelect.setPrefWidth(300);
        bankSelect.setPrefHeight(36);
        bankSelect.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #E2EBE5; -fx-border-radius: 6; -fx-font-family: 'Poppins'; -fx-font-size: 12px;");

        ComboBox<String> modeSelect = new ComboBox<>();
        modeSelect.getItems().addAll("Instant IMPS (Immediate Credit - 24/7)", "NEFT Transfer (Within 2 Hours)", "Direct UPI Payout (PhonePe / GPay)");
        modeSelect.setValue("Instant IMPS (Immediate Credit - 24/7)");
        modeSelect.setPrefWidth(300);
        modeSelect.setPrefHeight(36);
        modeSelect.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #E2EBE5; -fx-border-radius: 6; -fx-font-family: 'Poppins'; -fx-font-size: 12px;");

        VBox amtBox = new VBox(4, amountField, presetBox);

        form.add(createLabel("Withdraw Amount (₹):"), 0, 0);
        form.add(amtBox, 1, 0);

        form.add(createLabel("Select Bank Account:"), 0, 1);
        form.add(bankSelect, 1, 1);

        form.add(createLabel("Transfer Mode:"), 0, 2);
        form.add(modeSelect, 1, 2);

        Button submitBtn = new Button("Confirm & Transfer");
        submitBtn.setStyle("-fx-background-color: #2E7D32; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 8 18 8 18;");

        Button cancelBtn = new Button("Cancel");
        cancelBtn.setStyle("-fx-background-color: #8B3A3A; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 8 18 8 18;");

        submitBtn.setOnAction(e -> {
            try {
                int amt = Integer.parseInt(amountField.getText().trim());
                if (amt > 0 && amt <= availableBalance) {
                    availableBalance -= amt;
                    totalWithdrawn += amt;
                    availBalanceText.setText("₹" + String.format("%,d", availableBalance));
                    withdrawnText.setText("₹" + String.format("%,d", totalWithdrawn));
                    txnList.add(0, new Transaction("#WD-" + (5050 + txnList.size()), "Withdrawal to " + bankSelect.getValue().substring(0, 9), "Rajesh Agro Services", "Fleet Settlement Account", "Today (Instant)", modeSelect.getValue().substring(0, 12), amt, "WITHDRAWAL", "SETTLED", "TXN-WD-" + System.currentTimeMillis() % 1000000));
                    renderTxnList();
                    root.getChildren().remove(overlay);
                }
            } catch (Exception ignored) {}
        });

        cancelBtn.setOnAction(e -> root.getChildren().remove(overlay));

        HBox btnBox = new HBox(10, submitBtn, cancelBtn);
        btnBox.setAlignment(Pos.CENTER_RIGHT);

        modal.getChildren().addAll(title, avail, form, btnBox);
        overlay.getChildren().add(modal);
        root.getChildren().add(overlay);
    }

    private static Label createLabel(String t) {
        Label l = new Label(t);
        l.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #374151;");
        return l;
    }
}
