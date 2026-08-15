package com.desgin.view.operator;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class OperatorAnalytics {

    public static ScrollPane getAnalyticsSection() {
        Text title = new Text("Operator Performance & Field Safety Analytics");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 26px; -fx-font-weight: bold; -fx-fill: #4A2C20;");

        Text subtitle = new Text("Analyze your machinery fuel efficiency metrics, client satisfaction ratings, safe operation scores, and seasonal productivity.");
        subtitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-fill: #806A5B;");

        VBox titleBox = new VBox(4, title, subtitle);

        // 4 KPI Cards
        HBox metricRow = createKPIRow();

        // Efficiency & Skills Breakdown Card
        VBox skillBreakdownCard = createSkillBreakdownCard();

        // Client Farmer Reviews Section
        VBox reviewsSection = createReviewsSection();

        VBox content = new VBox(22, titleBox, metricRow, skillBreakdownCard, reviewsSection);
        content.setPadding(new Insets(25, 30, 35, 30));

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        return scrollPane;
    }

    private static HBox createKPIRow() {
        VBox c1 = createMetricCard("⭐ Operator Rating", "4.9 / 5.0", "Based on 38 farmer reviews", "#4A2C20");
        VBox c2 = createMetricCard("🛡 Safety & Incident Score", "99.4%", "Zero machinery collision record", "#2E7D32");
        VBox c3 = createMetricCard("⛽ Fuel Efficiency Index", "94% Optimal", "Save ~1.2L per acre vs avg", "#2E7D32");
        VBox c4 = createMetricCard("🌾 Total Land Operated", "115.0 Hectares", "284.0 Acres across 92 shifts", "#5C4033");

        HBox row = new HBox(15, c1, c2, c3, c4);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    private static VBox createMetricCard(String title, String value, String sub, String color) {
        Text t = new Text(title);
        t.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #806A5B;");

        Text v = new Text(value);
        v.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 22px; -fx-font-weight: bold; -fx-fill: " + color + ";");

        Text s = new Text(sub);
        s.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 10.5px; -fx-fill: #5C4033;");

        VBox b = new VBox(6, t, v, s);
        b.setPrefWidth(240);
        b.setPadding(new Insets(16));
        b.setStyle("-fx-background-color: #F5EFE6; -fx-background-radius: 12; -fx-border-color: #D8C7B5; -fx-border-width: 1; -fx-border-radius: 12;");
        return b;
    }

    private static VBox createSkillBreakdownCard() {
        Text title = new Text("Machinery Operation Skill & Precision Breakdown");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 17px; -fx-font-weight: bold; -fx-fill: #4A2C20;");

        VBox r1 = createProgressRow("🎯 Plowing Depth & Tillage Consistency", "96% Precision", 0.96, "#2E7D32");
        VBox r2 = createProgressRow("🌾 Harvester Grain Loss Minimization (<1.5%)", "98% Clean Output", 0.98, "#8B6F47");
        VBox r3 = createProgressRow("⚡ Engine RPM & Fuel Conservation Index", "92% Eco-Friendly", 0.92, "#2E7D32");
        VBox r4 = createProgressRow("⏱ On-Time Field Arrival & Job Execution", "95% Punctuality", 0.95, "#1976D2");

        VBox card = new VBox(12, title, r1, r2, r3, r4);
        card.setPadding(new Insets(18));
        card.setStyle("-fx-background-color: #F5EFE6; -fx-background-radius: 14; -fx-border-color: #D8C7B5; -fx-border-width: 1; -fx-border-radius: 14;");
        return card;
    }

    private static VBox createProgressRow(String label, String value, double progress, String barColor) {
        Text l = new Text(label);
        l.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-font-weight: bold; -fx-fill: #4A2C20;");

        Text v = new Text(value);
        v.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-font-weight: bold; -fx-fill: #5C4033;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox top = new HBox(l, spacer, v);

        ProgressBar pb = new ProgressBar(progress);
        pb.setPrefWidth(980);
        pb.setPrefHeight(8);
        pb.setStyle("-fx-accent: " + barColor + ";");

        return new VBox(4, top, pb);
    }

    private static VBox createReviewsSection() {
        Text title = new Text("Recent Client Farmer Ratings & Feedback");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 18px; -fx-font-weight: bold; -fx-fill: #4A2C20;");

        HBox rev1 = createReviewCard("Balasaheb Shirole", "⭐⭐⭐⭐⭐ (5.0)", "14 Aug 2026", "Ramesh is one of the best tractor operators in Baramati. Clean tillage with zero soil clumping!");
        HBox rev2 = createReviewCard("Vikas More", "⭐⭐⭐⭐⭐ (5.0)", "10 Aug 2026", "Operated the combine harvester very carefully without any grain loss. Highly recommended!");
        HBox rev3 = createReviewCard("Kiran Bhosale", "⭐⭐⭐⭐☆ (4.8)", "05 Aug 2026", "Prompt arrival and good laser land leveling precision on our sugarcane plots.");

        return new VBox(12, title, rev1, rev2, rev3);
    }

    private static HBox createReviewCard(String farmer, String rating, String date, String comment) {
        Text nameText = new Text("👨‍🌾 " + farmer);
        nameText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13.5px; -fx-font-weight: bold; -fx-fill: #4A2C20;");

        Text ratingText = new Text(rating);
        ratingText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-font-weight: bold; -fx-fill: #E65100;");

        Text dateText = new Text("📅 " + date);
        dateText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-fill: #806A5B;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox top = new HBox(12, nameText, ratingText, spacer, dateText);
        top.setAlignment(Pos.CENTER_LEFT);

        Text commentText = new Text("\"" + comment + "\"");
        commentText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-fill: #5C4033; -fx-font-style: italic;");

        VBox b = new VBox(4, top, commentText);
        b.setPadding(new Insets(12, 16, 12, 16));
        b.setStyle("-fx-background-color: #F5EFE6; -fx-background-radius: 10; -fx-border-color: #D8C7B5; -fx-border-width: 1; -fx-border-radius: 10;");

        HBox wrapper = new HBox(b);
        HBox.setHgrow(b, Priority.ALWAYS);
        return wrapper;
    }
}
