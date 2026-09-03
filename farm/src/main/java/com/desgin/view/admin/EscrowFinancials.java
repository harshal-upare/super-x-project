package com.desgin.view.admin;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class EscrowFinancials {

    public static class PayoutRequest {
        public String id;
        public String payee;
        public String role;
        public int amount;
        public String bankDetails;
        public String status; // "PENDING", "APPROVED"

        public PayoutRequest(String id, String payee, String role, int amount, String bankDetails, String status) {
            this.id = id;
            this.payee = payee;
            this.role = role;
            this.amount = amount;
            this.bankDetails = bankDetails;
            this.status = status;
        }
    }

    private static List<PayoutRequest> payoutList = new ArrayList<>();
    private static VBox payoutContainer;

    static {
        initPayouts();
    }

    private static void initPayouts() {
        if (!payoutList.isEmpty()) return;
        payoutList.add(new PayoutRequest("PO-902", "Rajesh Patil (Agro Services)", "PROVIDER", 25000, "HDFC Bank (•••• 8842, IFSC: HDFC0001024)", "PENDING"));
        payoutList.add(new PayoutRequest("PO-905", "Vikas More (AgriFleet Indapur)", "PROVIDER", 40000, "State Bank of India (•••• 4120, IFSC: SBIN0004512)", "PENDING"));
        payoutList.add(new PayoutRequest("PO-908", "Ramesh Chavan", "OPERATOR", 15000, "UPI Direct: ramesh.operator@okhdfcbank", "PENDING"));
    }

    public static ScrollPane getPage(StackPane root) {
        Text title = new Text("Escrow Vault & Platform Commission Control");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 24px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text subtitle = new Text("Monitor RBI-compliant escrow liquidity, configure platform commission tariffs, and audit high-value bank withdrawals.");
        subtitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-fill: #4B5563;");

        VBox titleBox = new VBox(3, title, subtitle);

        // 4 Financial Master KPI Cards in responsive GridPane
        GridPane kpiGrid = createEscrowKPIGrid();

        // Commission Rate Configuration Card
        VBox commissionCard = createCommissionConfigCard();

        // Pending Withdrawal Authorization Queue
        VBox withdrawalQueueCard = createWithdrawalQueue(root);

        VBox content = new VBox(20, titleBox, kpiGrid, commissionCard, withdrawalQueueCard);
        content.setPadding(new Insets(20, 25, 35, 25));
        content.setMinWidth(0);
        content.setMaxWidth(Double.MAX_VALUE);

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        return scrollPane;
    }

    private static GridPane createEscrowKPIGrid() {
        VBox c1 = createMetricCard("🔒 Escrow Vault", "₹6,45,000", "100% Nodal", "Held across 28 active rentals", "#1976D2", "#E0F2FE");
        VBox c2 = createMetricCard("💰 Realized Commission", "₹3,39,500", "7.0% Take", "Net platform earnings YTD", "#2E7D32", "#E8F5E9");
        VBox c3 = createMetricCard("⏳ Pending Payouts", "₹80,000", "3 Requests", "Awaiting admin authorization", "#E65100", "#FFF3E0");
        VBox c4 = createMetricCard("💳 Total Settled", "₹42.1 Lakh", "520+ IMPS", "Direct bank settlements", "#374151", "#FFFFFF");

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

    private static VBox createMetricCard(String title, String value, String badge, String sub, String color, String bgColor) {
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
        card.setStyle("-fx-background-color: " + bgColor + "; -fx-background-radius: 12; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 12;");
        return card;
    }

    private static VBox createCommissionConfigCard() {
        Text title = new Text("⚙ Platform Commission Rate Tiers (Category-Wise)");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 15px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text sub = new Text("Configure platform commission percentages deducted from gross rental tariffs before provider payout.");
        sub.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-fill: #4B5563;");

        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(10);
        grid.setMinWidth(0);
        grid.setMaxWidth(Double.MAX_VALUE);

        ColumnConstraints c1 = new ColumnConstraints();
        c1.setPercentWidth(50);
        ColumnConstraints c2 = new ColumnConstraints();
        c2.setPercentWidth(50);
        grid.getColumnConstraints().addAll(c1, c2);

        TextField t1 = new TextField("5.0");
        TextField t2 = new TextField("7.0");
        TextField t3 = new TextField("5.0");
        TextField t4 = new TextField("10.0");

        String tfStyle = "-fx-background-color: white; -fx-border-color: #E2EBE5; -fx-border-radius: 6; -fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-pref-width: 60px; -fx-max-width: 60px;";
        t1.setStyle(tfStyle);
        t2.setStyle(tfStyle);
        t3.setStyle(tfStyle);
        t4.setStyle(tfStyle);

        HBox item1 = new HBox(8, createTierLabel("🚜 Tractors (Mahindra / JD):"), t1, new Label("%"));
        item1.setAlignment(Pos.CENTER_LEFT);

        HBox item2 = new HBox(8, createTierLabel("🌾 Combine Harvesters (Kartar):"), t2, new Label("%"));
        item2.setAlignment(Pos.CENTER_LEFT);

        HBox item3 = new HBox(8, createTierLabel("⚙ Rotavators & Tillers:"), t3, new Label("%"));
        item3.setAlignment(Pos.CENTER_LEFT);

        HBox item4 = new HBox(8, createTierLabel("🚁 Agri-Drones:"), t4, new Label("%"));
        item4.setAlignment(Pos.CENTER_LEFT);

        grid.add(item1, 0, 0);
        grid.add(item3, 1, 0);
        grid.add(item2, 0, 1);
        grid.add(item4, 1, 1);

        Button saveConfigBtn = new Button("💾  Update Commission Rules");
        saveConfigBtn.setStyle("-fx-background-color: #2E7D32; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 7 16 7 16;");

        VBox card = new VBox(10, new VBox(2, title, sub), grid, saveConfigBtn);
        card.setPadding(new Insets(16));
        card.setMinWidth(0);
        card.setMaxWidth(Double.MAX_VALUE);
        card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 14; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 14;");
        return card;
    }

    private static Label createTierLabel(String text) {
        Label l = new Label(text);
        l.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #1B4332;");
        return l;
    }

    private static VBox createWithdrawalQueue(StackPane root) {
        Text title = new Text("🏦 High-Value Bank Withdrawal Authorization Queue");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 16px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        payoutContainer = new VBox(10);
        payoutContainer.setMinWidth(0);
        renderPayouts(root);

        VBox card = new VBox(10, title, payoutContainer);
        card.setMinWidth(0);
        return card;
    }

    private static void renderPayouts(StackPane root) {
        payoutContainer.getChildren().clear();

        for (PayoutRequest po : payoutList) {
            Text id = new Text(po.id);
            id.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-font-weight: bold; -fx-fill: #2D6A4F;");

            Text name = new Text(po.payee + " (" + po.role + ")");
            name.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13.5px; -fx-font-weight: bold; -fx-fill: #1B4332;");

            Text bank = new Text("🏦 Bank A/C: " + po.bankDetails);
            bank.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-fill: #4B5563;");

            VBox info = new VBox(1, id, name, bank);
            info.setMinWidth(0);

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            Text amt = new Text("₹" + String.format("%,d", po.amount));
            amt.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 16px; -fx-font-weight: bold; -fx-fill: #2E7D32;");

            Button approveBtn = new Button("✔ Authorize IMPS");
            approveBtn.setStyle("-fx-background-color: #2E7D32; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 6 12 6 12;");
            approveBtn.setOnAction(e -> {
                po.status = "APPROVED";
                approveBtn.setText("✔ Payout Released");
                approveBtn.setDisable(true);
                approveBtn.setStyle("-fx-background-color: #E8F5E9; -fx-text-fill: #2E7D32; -fx-font-family: 'Poppins'; -fx-font-size: 10.5px; -fx-font-weight: bold;");
            });

            HBox row = new HBox(12, info, spacer, amt, approveBtn);
            row.setAlignment(Pos.CENTER_LEFT);
            row.setPadding(new Insets(10, 14, 10, 14));
            row.setMinWidth(0);
            row.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 10; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 10;");

            payoutContainer.getChildren().add(row);
        }
    }
}
