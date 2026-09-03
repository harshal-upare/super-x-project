package com.desgin.view.operator;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class OperatorEarnings {

    public static class WageTransaction {
        public String txnId;
        public String title;
        public String date;
        public String channel;
        public int amount;
        public String type; // "CREDIT", "WITHDRAWAL"
        public String status;

        public WageTransaction(String txnId, String title, String date, String channel, int amount, String type, String status) {
            this.txnId = txnId;
            this.title = title;
            this.date = date;
            this.channel = channel;
            this.amount = amount;
            this.type = type;
            this.status = status;
        }
    }

    private static int totalLifetime = 142800;
    private static int availableBalance = 12400;
    private static int pendingEscrow = 4200;
    private static int totalWithdrawn = 126200;

    private static List<WageTransaction> txnList = new ArrayList<>();
    private static VBox txnListContainer;
    private static Text availBalanceText;
    private static Text withdrawnText;

    static {
        initTransactions();
    }

    private static void initTransactions() {
        if (!txnList.isEmpty()) return;
        txnList.add(new WageTransaction("#WAGE-4890", "Harvester Shift Wage (15 Acres, Balasaheb Farm)", "14 Aug 2026", "IMPS to SBI •••• 4120", 6000, "CREDIT", "SETTLED"));
        txnList.add(new WageTransaction("#WAGE-4850", "Rotavator Tillage Wage (12 Acres, Vikas Farm)", "13 Aug 2026", "Direct Bank Credit", 2800, "CREDIT", "SETTLED"));
        txnList.add(new WageTransaction("#WD-OP-201", "Operator Wage Payout to SBI Bank •••• 4120", "10 Aug 2026", "Instant IMPS", 15000, "WITHDRAWAL", "SETTLED"));
        txnList.add(new WageTransaction("#WAGE-4810", "Cultivator Operation Wage (Pravin Farm)", "08 Aug 2026", "Direct Bank Credit", 1900, "CREDIT", "SETTLED"));
        txnList.add(new WageTransaction("#WAGE-4770", "Drone Spraying Mission Pay (10 Acres)", "05 Aug 2026", "Direct Bank Credit", 3500, "CREDIT", "SETTLED"));
        txnList.add(new WageTransaction("#WD-OP-195", "Operator Wage Payout to SBI Bank •••• 4120", "01 Aug 2026", "Instant IMPS", 20000, "WITHDRAWAL", "SETTLED"));
    }

    public static ScrollPane getEarningsSection(StackPane root) {
        Text headerTitle = new Text("Operator Wages & Earnings Settlement");
        headerTitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 26px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text headerSubtitle = new Text("Track your daily operator wages, per-acre incentives, verified timesheet payouts, and initiate instant bank withdrawals.");
        headerSubtitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-fill: #4B5563;");

        VBox titleBox = new VBox(4, headerTitle, headerSubtitle);

        Button withdrawBtn = new Button("💸  Withdraw Wages to Bank");
        withdrawBtn.setPrefHeight(42);
        withdrawBtn.setStyle("-fx-background-color: #2E7D32; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 0 20 0 20;");
        withdrawBtn.setOnAction(e -> showWithdrawModal(root));

        Region topSpacer = new Region();
        HBox.setHgrow(topSpacer, Priority.ALWAYS);
        HBox topBar = new HBox(titleBox, topSpacer, withdrawBtn);
        topBar.setAlignment(Pos.CENTER_LEFT);

        // 4 KPI Financial Cards
        HBox metricRow = createFinancialMetrics();

        // Operation Category Contribution Breakdown
        VBox categoryBreakdownCard = createCategoryBreakdown();

        // Transaction History Table Section
        VBox txnSection = createTransactionHistorySection(root);

        VBox content = new VBox(22, topBar, metricRow, categoryBreakdownCard, txnSection);
        content.setPadding(new Insets(25, 30, 35, 30));

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        return scrollPane;
    }

    private static HBox createFinancialMetrics() {
        VBox c1 = createMetricCard("💰 Lifetime Wages Earned", "₹" + String.format("%,d", totalLifetime), "From 92 completed field operations", "#1B4332");

        availBalanceText = new Text("₹" + String.format("%,d", availableBalance));
        availBalanceText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 22px; -fx-font-weight: bold; -fx-fill: #2E7D32;");
        VBox c2 = createCustomMetricCard("🏦 Available for Payout", availBalanceText, "Instant 24/7 bank transfer", "#2E7D32");

        VBox c3 = createMetricCard("⏱ Pending Job Escrow", "₹" + String.format("%,d", pendingEscrow), "Releases upon farmer job sign-off", "#E65100");

        withdrawnText = new Text("₹" + String.format("%,d", totalWithdrawn));
        withdrawnText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 22px; -fx-font-weight: bold; -fx-fill: #374151;");
        VBox c4 = createCustomMetricCard("💳 Total Settled to Bank", withdrawnText, "12 payouts deposited to SBI", "#374151");

        HBox row = new HBox(15, c1, c2, c3, c4);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
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

    private static VBox createCustomMetricCard(String title, Text vText, String sub, String color) {
        Text t = new Text(title);
        t.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #4B5563;");

        Text s = new Text(sub);
        s.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 10.5px; -fx-fill: #374151;");

        VBox b = new VBox(6, t, vText, s);
        b.setPrefWidth(240);
        b.setPadding(new Insets(16));
        b.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 12; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 12;");
        return b;
    }

    private static VBox createCategoryBreakdown() {
        Text title = new Text("Operator Wage Earnings by Machine Category");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 17px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        VBox r1 = createProgressRow("🌾 Combine Harvester Operations", "₹68,500 (48%)", 0.48, "#2D6A4F");
        VBox r2 = createProgressRow("🚜 Heavy Tractor Tillage & Plowing", "₹45,600 (32%)", 0.32, "#2E7D32");
        VBox r3 = createProgressRow("⚙ Laser Land Leveling & Grading", "₹17,100 (12%)", 0.12, "#E65100");
        VBox r4 = createProgressRow("🚁 Precision Agri-Drone Spraying", "₹11,600 (8%)", 0.08, "#1976D2");

        VBox card = new VBox(12, title, r1, r2, r3, r4);
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

    private static VBox createTransactionHistorySection(StackPane root) {
        Text title = new Text("Wage Settlement & Bank Transfer History");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 20px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        txnListContainer = new VBox(10);
        renderTxnList();

        return new VBox(12, title, txnListContainer);
    }

    private static void renderTxnList() {
        txnListContainer.getChildren().clear();

        for (WageTransaction txn : txnList) {
            Text id = new Text(txn.txnId);
            id.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-fill: #2D6A4F;");

            Text desc = new Text(txn.title);
            desc.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 14px; -fx-font-weight: bold; -fx-fill: #1B4332;");

            Text dt = new Text("📅 " + txn.date + " • " + txn.channel);
            dt.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-fill: #4B5563;");

            VBox infoBox = new VBox(2, id, desc, dt);

            boolean isCredit = "CREDIT".equals(txn.type);
            Text amt = new Text((isCredit ? "+ ₹" : "- ₹") + String.format("%,d", txn.amount));
            amt.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 16px; -fx-font-weight: bold; -fx-fill: " + (isCredit ? "#2E7D32" : "#8B3A3A") + ";");

            Label status = new Label(txn.status);
            status.setStyle("-fx-background-color: #E8F5E9; -fx-text-fill: #2E7D32; -fx-font-family: 'Poppins'; -fx-font-size: 10.5px; -fx-font-weight: bold; -fx-padding: 3 8 3 8; -fx-background-radius: 4;");

            Button receipt = new Button("📄 Pay Slip");
            receipt.setStyle("-fx-background-color: #E8F5E9; -fx-text-fill: #1B4332; -fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand;");

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            HBox row = new HBox(20, infoBox, spacer, amt, status, receipt);
            row.setAlignment(Pos.CENTER_LEFT);
            row.setPadding(new Insets(12, 16, 12, 16));
            row.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 10; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 10;");

            txnListContainer.getChildren().add(row);
        }
    }

    private static void showWithdrawModal(StackPane root) {
        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.5);");

        VBox modal = new VBox(15);
        modal.setPrefWidth(460);
        modal.setMaxWidth(460);
        modal.setPadding(new Insets(24));
        modal.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 14; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 14; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 20, 0.3, 0, 8);");

        Text title = new Text("Withdraw Operator Wages to Bank");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 18px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text avail = new Text("Available Balance: ₹" + String.format("%,d", availableBalance));
        avail.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-font-weight: bold; -fx-fill: #2E7D32;");

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(12);

        TextField amountField = new TextField("5000");
        amountField.setPrefHeight(36);

        ComboBox<String> bankSelect = new ComboBox<>();
        bankSelect.getItems().addAll(
            "State Bank of India (A/C •••• 4120, IFSC: SBIN0004512)",
            "Bank of Maharashtra (A/C •••• 9812, IFSC: MAHB0001092)",
            "UPI Direct: ramesh.operator@okhdfcbank"
        );
        bankSelect.setValue("State Bank of India (A/C •••• 4120, IFSC: SBIN0004512)");
        bankSelect.setPrefWidth(280);

        ComboBox<String> modeSelect = new ComboBox<>();
        modeSelect.getItems().addAll("Instant IMPS (Immediate Credit)", "UPI Transfer (PhonePe / GPay)", "NEFT Settlement");
        modeSelect.setValue("Instant IMPS (Immediate Credit)");
        modeSelect.setPrefWidth(280);

        form.add(createLabel("Payout Amount (₹):"), 0, 0);
        form.add(amountField, 1, 0);

        form.add(createLabel("Payout Account:"), 0, 1);
        form.add(bankSelect, 1, 1);

        form.add(createLabel("Transfer Method:"), 0, 2);
        form.add(modeSelect, 1, 2);

        Button submitBtn = new Button("Confirm & Transfer Wages");
        submitBtn.setStyle("-fx-background-color: #2E7D32; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand;");
        submitBtn.setPrefHeight(36);

        Button cancelBtn = new Button("Cancel");
        cancelBtn.setStyle("-fx-background-color: #8B3A3A; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand;");
        cancelBtn.setPrefHeight(36);

        submitBtn.setOnAction(e -> {
            try {
                int amt = Integer.parseInt(amountField.getText().trim());
                if (amt > 0 && amt <= availableBalance) {
                    availableBalance -= amt;
                    totalWithdrawn += amt;
                    availBalanceText.setText("₹" + String.format("%,d", availableBalance));
                    withdrawnText.setText("₹" + String.format("%,d", totalWithdrawn));
                    txnList.add(0, new WageTransaction("#WD-OP-" + (205 + txnList.size()), "Wage Payout to " + bankSelect.getValue().substring(0, 10), "Today (Instant)", modeSelect.getValue().substring(0, 12), amt, "WITHDRAWAL", "SETTLED"));
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
