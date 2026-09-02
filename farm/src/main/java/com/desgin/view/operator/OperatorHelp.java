package com.desgin.view.operator;

import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class OperatorHelp {

    public static ScrollPane getHelpSection() {
        Text title = new Text("Operator Help & Field Emergency SOS");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 26px; -fx-font-weight: bold; -fx-fill: #4A2C20;");

        Text subtitle = new Text("Immediate roadside breakdown assistance, equipment operator safety protocols, troubleshooting guides & operator support.");
        subtitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-fill: #806A5B;");

        VBox titleBox = new VBox(4, title, subtitle);

        // Emergency SOS Card
        VBox emergencyCard = createEmergencyCard();

        // Troubleshooting Guides
        VBox troubleshootingCard = createTroubleshootingCard();

        // FAQ Section
        VBox faqCard = createFAQCard();

        VBox content = new VBox(20, titleBox, emergencyCard, troubleshootingCard, faqCard);
        content.setPadding(new Insets(25, 30, 35, 30));

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        return scrollPane;
    }

    private static VBox createEmergencyCard() {
        Text t = new Text("🚨 24/7 Field Breakdown SOS & Mobile Mechanic Dispatch");
        t.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 17px; -fx-font-weight: bold; -fx-fill: #8B3A3A;");

        Text desc = new Text("If you experience an engine stall, hydraulic failure, implement pin break, or tire blowout in the field, call the FarmEquip Rapid Dispatch hotline immediately.");
        desc.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-fill: #5C4033;");

        Text hot1 = new Text("📞 Toll-Free Operator SOS: 1800-419-FARM (1800-419-3276)");
        hot1.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 14px; -fx-font-weight: bold; -fx-fill: #8B3A3A;");

        Text hot2 = new Text("🔧 Pune / Baramati Mobile Service Unit: +91 98220 99881");
        hot2.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-font-weight: bold; -fx-fill: #2E7D32;");

        VBox b = new VBox(8, t, desc, hot1, hot2);
        b.setPadding(new Insets(18));
        b.setStyle("-fx-background-color: #FFF0F0; -fx-background-radius: 12; -fx-border-color: #E57373; -fx-border-width: 1.5; -fx-border-radius: 12;");
        return b;
    }

    private static VBox createTroubleshootingCard() {
        Text title = new Text("Common Field Troubleshooting Quick Guides");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 17px; -fx-font-weight: bold; -fx-fill: #4A2C20;");

        VBox g1 = createGuideItem("🚜 Tractor 3-Point Hitch Not Lifting", "Check hydraulic fluid level dipstick behind seat. Ensure draft control lever is set to position control mode. Verify hydraulic filter is not clogged.");
        VBox g2 = createGuideItem("🌾 Combine Harvester Grain Choking", "Stop engine immediately, disengage threshing drum drive, reverse cylinder manually with socket lever, clear excessive straw accumulation.");
        VBox g3 = createGuideItem("⚡ High Engine Temperature (>95°C)", "Throttle down to idle (do not shut off immediately), check front radiator screen for chaff or dust clogging, inspect fan belt tension.");

        VBox b = new VBox(12, title, g1, g2, g3);
        b.setPadding(new Insets(18));
        b.setStyle("-fx-background-color: #F5EFE6; -fx-background-radius: 12; -fx-border-color: #D8C7B5; -fx-border-width: 1; -fx-border-radius: 12;");
        return b;
    }

    private static VBox createGuideItem(String heading, String solution) {
        Text h = new Text("• " + heading);
        h.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13.5px; -fx-font-weight: bold; -fx-fill: #4A2C20;");

        Text s = new Text(solution);
        s.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #5C4033; -fx-line-spacing: 3px;");

        return new VBox(3, h, s);
    }

    private static VBox createFAQCard() {
        Text title = new Text("Operator Frequently Asked Questions (FAQ)");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 17px; -fx-font-weight: bold; -fx-fill: #4A2C20;");

        VBox f1 = createFAQItem("How and when do I receive wage settlements?", "Wages are released automatically to your linked bank account via IMPS as soon as the client farmer signs off on the field session timesheet.");
        VBox f2 = createFAQItem("What should I do if a farmer asks for extra unassigned acres?", "Use the 'Field Logs' module to record the actual end meter reading and acreage. Extra acres will be automatically added to the billing and your wage payout.");
        VBox f3 = createFAQItem("Are operators insured while operating heavy machinery?", "Yes, all verified operators on the FarmEquip platform are covered by our comprehensive On-Duty Agricultural Operator Personal Accident Insurance policy.");

        VBox b = new VBox(12, title, f1, f2, f3);
        b.setPadding(new Insets(18));
        b.setStyle("-fx-background-color: #F5EFE6; -fx-background-radius: 12; -fx-border-color: #D8C7B5; -fx-border-width: 1; -fx-border-radius: 12;");
        return b;
    }

    private static VBox createFAQItem(String question, String answer) {
        Text q = new Text("Q: " + question);
        q.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-font-weight: bold; -fx-fill: #4A2C20;");

        Text a = new Text("A: " + answer);
        a.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #5C4033; -fx-line-spacing: 3px;");

        return new VBox(3, q, a);
    }
}
