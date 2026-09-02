package com.desgin.view.provider;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class ProviderHelp {

    public static ScrollPane getHelpSection() {
        Text headerTitle = new Text("Provider Help, Support & Claims Desk");
        headerTitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 26px; -fx-font-weight: bold; -fx-fill: #4A2C20;");

        Text headerSubtitle = new Text("Access 24/7 dedicated provider assistance, machinery insurance claims, farmer dispute resolution, and rental guidelines.");
        headerSubtitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-fill: #806A5B;");

        VBox titleBox = new VBox(4, headerTitle, headerSubtitle);

        // 3 Support Channels Strip
        HBox channels = new HBox(16,
            createContactCard("📞 24/7 Provider Hotline", "1800-419-8800 (Toll Free)", "Dedicated fleet & logistics support", "#2E7D32"),
            createContactCard("💬 WhatsApp Helpdesk", "+91 98220 99999", "Instant chat for quick issue resolution", "#25D366"),
            createContactCard("✉ Claims & Inquiries", "provider.support@farmequip.com", "Response within 2 business hours", "#1976D2")
        );
        channels.setAlignment(Pos.CENTER_LEFT);

        // Insurance & Protection Claims Card
        VBox insuranceCard = createInsuranceCard();

        // Provider FAQs Accordion
        VBox faqCard = createFaqCard();

        VBox content = new VBox(22, titleBox, channels, insuranceCard, faqCard);
        content.setPadding(new Insets(25, 30, 35, 30));

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        return scrollPane;
    }

    private static VBox createContactCard(String title, String contact, String sub, String color) {
        Text t = new Text(title);
        t.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 14px; -fx-font-weight: bold; -fx-fill: #4A2C20;");

        Text c = new Text(contact);
        c.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 16px; -fx-font-weight: bold; -fx-fill: " + color + ";");

        Text s = new Text(sub);
        s.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-fill: #806A5B;");

        VBox b = new VBox(6, t, c, s);
        b.setPrefWidth(320);
        b.setPadding(new Insets(16));
        b.setStyle("-fx-background-color: #F5EFE6; -fx-background-radius: 12; -fx-border-color: #D8C7B5; -fx-border-width: 1; -fx-border-radius: 12;");
        return b;
    }

    private static VBox createInsuranceCard() {
        Text title = new Text("🛡 FarmEquip Comprehensive Machinery Protection & Damage Shield");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 17px; -fx-font-weight: bold; -fx-fill: #4A2C20;");

        Text desc = new Text("Every booking through FarmEquip is insured up to ₹5,00,000 for accidental machinery damages, overturning, fire, and theft during rental operations.");
        desc.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-fill: #5C4033;");

        HBox step1 = createStepBox("Step 1", "Inspect & Photograph", "Capture 4 clear photos of damaged components on the field.");
        HBox step2 = createStepBox("Step 2", "Submit Incident Log", "Report via app or hotline within 24 hours of incident.");
        HBox step3 = createStepBox("Step 3", "Instant Survey & Repair", "Approved claim amount credited to provider bank within 48h.");

        HBox steps = new HBox(12, step1, step2, step3);

        Button claimBtn = new Button("📄  Initiate New Insurance Claim");
        claimBtn.setStyle("-fx-background-color: #2E7D32; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 8 16 8 16;");

        VBox card = new VBox(12, title, desc, steps, claimBtn);
        card.setPadding(new Insets(18));
        card.setStyle("-fx-background-color: #F5EFE6; -fx-background-radius: 14; -fx-border-color: #D8C7B5; -fx-border-width: 1; -fx-border-radius: 14;");
        return card;
    }

    private static HBox createStepBox(String step, String name, String sub) {
        Text s = new Text(step);
        s.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-fill: #2E7D32;");

        Text n = new Text(name);
        n.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-font-weight: bold; -fx-fill: #4A2C20;");

        Text sb = new Text(sub);
        sb.setWrappingWidth(260);
        sb.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-fill: #806A5B;");

        VBox vb = new VBox(2, s, n, sb);
        vb.setPadding(new Insets(10));
        vb.setStyle("-fx-background-color: #FFFDF9; -fx-background-radius: 8; -fx-border-color: #D8C7B5; -fx-border-width: 1; -fx-border-radius: 8;");
        return new HBox(vb);
    }

    private static VBox createFaqCard() {
        Text title = new Text("Frequently Asked Provider Questions (FAQs)");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 17px; -fx-font-weight: bold; -fx-fill: #4A2C20;");

        VBox q1 = createFaqItem("How are rental earnings credited to my bank account?", "Rental payments from farmers are held securely in platform escrow and automatically released to your registered bank account via IMPS/NEFT upon completion of the rental job.");
        VBox q2 = createFaqItem("What happens if a farmer delays returning the machinery?", "Late returns automatically incur an overdue penalty rate of 1.5x the hourly rate, charged directly to the farmer's security deposit.");
        VBox q3 = createFaqItem("Can I provide trained operators with my heavy machinery?", "Yes! When registering machinery in your Fleet tab, toggle 'Trained Driver / Operator Available'. Farmers can book the machine inclusive of your operator.");
        VBox q4 = createFaqItem("What is FarmEquip's platform commission fee?", "FarmEquip charges a minimal 5% platform fee on completed rentals, which covers 24/7 GPS fleet tracking, payment processing, and comprehensive damage insurance.");

        VBox card = new VBox(12, title, q1, q2, q3, q4);
        card.setPadding(new Insets(18));
        card.setStyle("-fx-background-color: #F5EFE6; -fx-background-radius: 14; -fx-border-color: #D8C7B5; -fx-border-width: 1; -fx-border-radius: 14;");
        return card;
    }

    private static VBox createFaqItem(String question, String answer) {
        Text q = new Text("Q: " + question);
        q.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-font-weight: bold; -fx-fill: #4A2C20;");

        Text a = new Text(answer);
        a.setWrappingWidth(920);
        a.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #5C4033;");

        VBox b = new VBox(4, q, a);
        b.setPadding(new Insets(10));
        b.setStyle("-fx-background-color: #FFFDF9; -fx-background-radius: 8; -fx-border-color: #D8C7B5; -fx-border-width: 0.5; -fx-border-radius: 8;");
        return b;
    }
}
