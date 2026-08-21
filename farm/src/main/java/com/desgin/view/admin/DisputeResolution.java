package com.desgin.view.admin;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class DisputeResolution {

    public static class DisputeTicket {
        public String id;
        public String title;
        public String farmer;
        public String provider;
        public String operator;
        public String bookingId;
        public int disputedAmount;
        public String description;
        public String status; // "OPEN", "RESOLVED"
        public String filedDate;

        public DisputeTicket(String id, String title, String farmer, String provider, String operator, String bookingId, int disputedAmount, String description, String status, String filedDate) {
            this.id = id;
            this.title = title;
            this.farmer = farmer;
            this.provider = provider;
            this.operator = operator;
            this.bookingId = bookingId;
            this.disputedAmount = disputedAmount;
            this.description = description;
            this.status = status;
            this.filedDate = filedDate;
        }
    }

    private static List<DisputeTicket> ticketList = new ArrayList<>();
    private static VBox ticketContainer;

    static {
        initTickets();
    }

    private static void initTickets() {
        if (!ticketList.isEmpty()) return;
        ticketList.add(new DisputeTicket("DISP-401", "Hydraulic Hose Leakage during Plowing", "Ganesh Jadhav", "Rajesh Patil (Agro Services)", "Ramesh Chavan", "#REQ-9102", 2400, "Machine developed hydraulic fluid leak on hour 2. Farmer requests partial refund for lost operational day.", "OPEN", "Yesterday, 02:30 PM"));
        ticketList.add(new DisputeTicket("DISP-404", "Heavy Rainfall Disruption during Harvest", "Anand Kadam", "Balasaheb Shirole", "Dilip Shinde", "#REQ-8984", 7000, "Unseasonal monsoon shower halted harvester on field after 3 acres. Both parties request fair escrow split.", "OPEN", "14 Aug 2026"));
        ticketList.add(new DisputeTicket("DISP-388", "Overtime Acreage Tillage Calculation", "Vikas More", "Vikas More Fleet", "Ramesh Chavan", "#REQ-8894", 1200, "Operator completed 2 extra acres beyond original booking. Dispute resolved with 100% wage settlement.", "RESOLVED", "10 Aug 2026"));
    }

    public static ScrollPane getPage(StackPane root) {
        Text title = new Text("Dispute Resolution & Grievance Arbitration Desk");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 24px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text subtitle = new Text("Review filed field complaints, mediate billing or breakdown disputes, and authorize escrow refunds or split settlements.");
        subtitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-fill: #4B5563;");

        VBox titleBox = new VBox(3, title, subtitle);

        ticketContainer = new VBox(14);
        ticketContainer.setMinWidth(0);
        renderTickets(root);

        VBox content = new VBox(18, titleBox, ticketContainer);
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

    private static void renderTickets(StackPane root) {
        ticketContainer.getChildren().clear();

        for (DisputeTicket t : ticketList) {
            Text id = new Text(t.id + " • Booking Ref: " + t.bookingId);
            id.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-font-weight: bold; -fx-fill: #2D6A4F;");

            Label status = new Label("● " + t.status);
            status.setStyle("-fx-background-color: " + ("OPEN".equals(t.status) ? "#FFF3E0" : "#E8F5E9") + "; -fx-text-fill: " + ("OPEN".equals(t.status) ? "#E65100" : "#2E7D32") + "; -fx-font-family: 'Poppins'; -fx-font-size: 10px; -fx-font-weight: bold; -fx-padding: 3 7 3 7; -fx-background-radius: 4;");

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);
            HBox top = new HBox(id, spacer, status);
            top.setAlignment(Pos.CENTER_LEFT);

            Text issueTitle = new Text(t.title);
            issueTitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 15px; -fx-font-weight: bold; -fx-fill: #1B4332;");

            Text parties = new Text("👨‍🌾 " + t.farmer + "  |  🚜 " + t.provider + "  |  👨‍🔧 " + t.operator);
            parties.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-fill: #374151; -fx-font-weight: bold;");

            Text desc = new Text("\"" + t.description + "\"");
            desc.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-fill: #4B5563; -fx-font-style: italic;");
            desc.setWrappingWidth(0);

            Text amount = new Text("Disputed Escrow: ₹" + String.format("%,d", t.disputedAmount));
            amount.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13.5px; -fx-font-weight: bold; -fx-fill: #8B3A3A;");

            HBox actions = new HBox(8);
            actions.setAlignment(Pos.CENTER_RIGHT);

            if ("OPEN".equals(t.status)) {
                Button refundBtn = new Button("💸 50% Refund");
                refundBtn.setStyle("-fx-background-color: #2D6A4F; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 5 12 5 12;");
                refundBtn.setOnAction(e -> {
                    t.status = "RESOLVED";
                    renderTickets(root);
                });

                Button resolveFull = new Button("✔ Release & Resolve");
                resolveFull.setStyle("-fx-background-color: #2E7D32; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 5 12 5 12;");
                resolveFull.setOnAction(e -> {
                    t.status = "RESOLVED";
                    renderTickets(root);
                });

                actions.getChildren().addAll(refundBtn, resolveFull);
            } else {
                Label resolvedBadge = new Label("✔ Arbitrated & Closed");
                resolvedBadge.setStyle("-fx-background-color: #E8F5E9; -fx-text-fill: #2E7D32; -fx-font-family: 'Poppins'; -fx-font-size: 10.5px; -fx-font-weight: bold; -fx-padding: 4 10 4 10; -fx-background-radius: 6;");
                actions.getChildren().add(resolvedBadge);
            }

            Region bSpacer = new Region();
            HBox.setHgrow(bSpacer, Priority.ALWAYS);
            HBox bottom = new HBox(amount, bSpacer, actions);
            bottom.setAlignment(Pos.CENTER_LEFT);

            VBox card = new VBox(8, top, issueTitle, parties, desc, bottom);
            card.setPadding(new Insets(14));
            card.setMinWidth(0);
            card.setMaxWidth(Double.MAX_VALUE);
            card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 12; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 12;");

            ticketContainer.getChildren().add(card);
        }
    }
}
