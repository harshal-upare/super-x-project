package com.desgin.view.admin;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
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

public class AdminSettings {

    public static ScrollPane getPage(StackPane root) {
        // Card 1: Core System Automation Toggles
        VBox autoCard = createAutomationTogglesCard();

        // Card 2: Platform Communication & Helpline Info
        VBox helplineCard = createHelplineConfigCard();

        // Card 3: Security & Database Backup
        VBox backupCard = createSecurityAuditCard();

        VBox content = new VBox(18, autoCard, helplineCard, backupCard);
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

    private static VBox createAutomationTogglesCard() {
        Text title = new Text("⚡ Automated Operations & Protocol Switches");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 15px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        CheckBox c1 = new CheckBox("Enable Instant Escrow Release on Farmer OTP Verification");
        CheckBox c2 = new CheckBox("Broadcast Weather Hazard & Rain Alert Push Notifications to Active Operators");
        CheckBox c3 = new CheckBox("Enforce Mandatory Operator Heavy Driving License Check Before Job Dispatch");
        CheckBox c4 = new CheckBox("Enable Automated 15-Second GPS Telematics Stream");

        c1.setSelected(true);
        c2.setSelected(true);
        c3.setSelected(true);
        c4.setSelected(true);

        String cbStyle = "-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-text-fill: #1B4332;";
        c1.setStyle(cbStyle);
        c2.setStyle(cbStyle);
        c3.setStyle(cbStyle);
        c4.setStyle(cbStyle);

        VBox list = new VBox(8, c1, c2, c3, c4);
        list.setMinWidth(0);

        Button saveBtn = new Button("💾  Save Automation Protocols");
        saveBtn.setStyle("-fx-background-color: #2E7D32; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 7 16 7 16;");

        VBox card = new VBox(10, title, list, saveBtn);
        card.setPadding(new Insets(16));
        card.setMinWidth(0);
        card.setMaxWidth(Double.MAX_VALUE);
        card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 14; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 14;");
        return card;
    }

    private static VBox createHelplineConfigCard() {
        Text title = new Text("📞 24/7 Farmer & Provider Support Center Configuration");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 15px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        GridPane grid = new GridPane();
        grid.setHgap(12);
        grid.setVgap(8);
        grid.setMinWidth(0);
        grid.setMaxWidth(Double.MAX_VALUE);

        TextField phoneField = new TextField("1800-425-FARM (1800-425-3276)");
        phoneField.setPrefWidth(260);
        TextField emailField = new TextField("support@farm-equip.gov.in");
        emailField.setPrefWidth(260);
        TextField whatsappField = new TextField("+91 98220 99999");
        whatsappField.setPrefWidth(260);

        String tfStyle = "-fx-background-color: white; -fx-border-color: #E2EBE5; -fx-border-radius: 6; -fx-font-family: 'Poppins'; -fx-font-size: 12px;";
        phoneField.setStyle(tfStyle);
        emailField.setStyle(tfStyle);
        whatsappField.setStyle(tfStyle);

        grid.add(createLabel("Toll-Free Helpline:"), 0, 0);
        grid.add(phoneField, 1, 0);
        grid.add(createLabel("Support Email:"), 0, 1);
        grid.add(emailField, 1, 1);
        grid.add(createLabel("WhatsApp Direct Desk:"), 0, 2);
        grid.add(whatsappField, 1, 2);

        VBox card = new VBox(10, title, grid);
        card.setPadding(new Insets(16));
        card.setMinWidth(0);
        card.setMaxWidth(Double.MAX_VALUE);
        card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 14; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 14;");
        return card;
    }

    private static VBox createSecurityAuditCard() {
        Text title = new Text("🛡️ System Audit Logs & Data Backup");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 15px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text desc = new Text("Platform database backup last synchronized: Today, 04:00 AM (Encrypted Nodal Ledger).");
        desc.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-fill: #4B5563;");

        Button exportBtn = new Button("📥  Export Encrypted System Audit Log (CSV/PDF)");
        exportBtn.setStyle("-fx-background-color: #374151; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 7 16 7 16;");

        VBox card = new VBox(8, title, desc, exportBtn);
        card.setPadding(new Insets(16));
        card.setMinWidth(0);
        card.setMaxWidth(Double.MAX_VALUE);
        card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 14; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 14;");
        return card;
    }

    private static Label createLabel(String text) {
        Label l = new Label(text);
        l.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #1B4332;");
        return l;
    }
}
