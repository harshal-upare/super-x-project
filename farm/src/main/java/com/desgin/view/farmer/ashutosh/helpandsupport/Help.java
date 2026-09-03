package com.desgin.view.farmer.ashutosh.helpandsupport;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class Help {

    public static ScrollPane getHelp() {

        // ================= TOP CONTACT CARDS =================
        VBox callCard = createContactCard("📞 National Kisan Helpline", "1800-180-1551 (Toll-Free)\nAvailable 6:00 AM – 10:00 PM (All Languages)", "#E8F5E9", "#1B4332");
        VBox whatsappCard = createContactCard("💬 WhatsApp Farm Support", "+91 98220 54321 (Instant Chat)\nShare machinery photos & live field location", "#DCFCE7", "#15803D");

        HBox contactBox = new HBox(14, callCard, whatsappCard);
        contactBox.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(callCard, Priority.ALWAYS);
        HBox.setHgrow(whatsappCard, Priority.ALWAYS);

        // ================= AI FARMER ASSISTANT =================
        VBox aiCard = new VBox(14);
        aiCard.setPadding(new Insets(20, 24, 20, 24));
        aiCard.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.95);" +
                "-fx-background-radius: 14px;" +
                "-fx-border-color: rgba(45, 106, 79, 0.25);" +
                "-fx-border-width: 1.2px;" +
                "-fx-border-radius: 14px;" +
                "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.04), 8, 0, 0, 2);"
        );

        Text aiTitle = new Text("🤖 Kisan AI Smart Assistant");
        aiTitle.setStyle(
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 18px;" +
                "-fx-font-weight: bold;" +
                "-fx-fill: #1B4332;"
        );

        Text aiSubtitle = new Text(
                "Ask anything about machinery rent calculations, operator hiring, soil preparation, or troubleshooting."
        );
        aiSubtitle.setStyle(
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 12.5px;" +
                "-fx-fill: #4B5563;"
        );

        // Chat messages box
        VBox chatBox = new VBox(10);
        chatBox.setPadding(new Insets(14));
        chatBox.setStyle(
                "-fx-background-color: #F4F9F4;" +
                "-fx-background-radius: 12px;" +
                "-fx-border-color: rgba(45, 106, 79, 0.2);" +
                "-fx-border-radius: 12px;" +
                "-fx-border-width: 1px;"
        );

        Label welcomeMessage = createAIMessage(
                "Namaste! 🙏 I am your Kisan AI Smart Assistant.\n\n" +
                "You can ask me questions such as:\n" +
                "• How do I rent equipment or hire an operator?\n" +
                "• What implement is best for pre-monsoon sowing?\n" +
                "• What should I do if the tractor breaks down on my field?\n" +
                "• How does the escrow payment protection work?"
        );
        chatBox.getChildren().add(welcomeMessage);

        ScrollPane chatScroll = new ScrollPane(chatBox);
        chatScroll.setPrefHeight(260);
        chatScroll.setFitToWidth(true);
        chatScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        chatScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        chatScroll.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        // Quick Suggestion Chips
        FlowPane quickChips = new FlowPane(8, 8);
        String[] suggestions = {
                "🚜 How to hire an operator?",
                "💳 Escrow security info",
                "🌧 Rain advisory for Pune",
                "⚙ Recommended rotavator HP"
        };

        TextField questionField = new TextField();
        questionField.setPromptText("Type your farming or machinery question here...");
        questionField.setPrefHeight(45);
        questionField.setStyle(
                "-fx-background-color: #FFFFFF;" +
                "-fx-background-radius: 12px;" +
                "-fx-border-color: rgba(45, 106, 79, 0.3);" +
                "-fx-border-width: 1.2px;" +
                "-fx-border-radius: 12px;" +
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 13px;" +
                "-fx-text-fill: #1F2937;" +
                "-fx-padding: 0 14px;"
        );

        for (String chipText : suggestions) {
            Button chip = new Button(chipText);
            chip.setStyle("-fx-background-color: #E8F5E9;" +
                    "-fx-text-fill: #2D6A4F;" +
                    "-fx-font-family: 'Poppins';" +
                    "-fx-font-size: 11.5px;" +
                    "-fx-font-weight: 500;" +
                    "-fx-padding: 5px 12px;" +
                    "-fx-background-radius: 14px;" +
                    "-fx-cursor: hand;" +
                    "-fx-border-color: rgba(45, 106, 79, 0.25);" +
                    "-fx-border-radius: 14px;");
            chip.setOnAction(e -> {
                questionField.setText(chipText.replace("🚜 ", "").replace("💳 ", "").replace("🌧 ", "").replace("⚙ ", ""));
            });
            quickChips.getChildren().add(chip);
        }

        Button sendButton = new Button("Ask AI ➤");
        sendButton.setPrefHeight(45);
        sendButton.setPrefWidth(110);
        sendButton.setStyle(
                "-fx-background-color: linear-gradient(to right, #2D6A4F, #40916C);" +
                "-fx-text-fill: white;" +
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 13px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 12px;" +
                "-fx-cursor: hand;" +
                "-fx-effect: dropshadow(gaussian, rgba(45, 106, 79, 0.25), 8, 0, 0, 2);"
        );
        sendButton.setOnMouseEntered(e -> sendButton.setStyle("-fx-background-color: linear-gradient(to right, #1B4332, #2D6A4F); -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-font-weight: bold; -fx-background-radius: 12px; -fx-cursor: hand;"));
        sendButton.setOnMouseExited(e -> sendButton.setStyle("-fx-background-color: linear-gradient(to right, #2D6A4F, #40916C); -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-font-weight: bold; -fx-background-radius: 12px; -fx-cursor: hand;"));

        Runnable doAsk = () -> {
            String q = questionField.getText() != null ? questionField.getText().trim() : "";
            if (q.isEmpty()) return;

            Label userMsg = createUserMessage(q);
            chatBox.getChildren().add(userMsg);
            questionField.clear();

            String answer = generateAIAnswer(q);
            Label aiMsg = createAIMessage(answer);
            chatBox.getChildren().add(aiMsg);

            Platform.runLater(() -> chatScroll.setVvalue(1.0));
        };

        sendButton.setOnAction(e -> doAsk.run());
        questionField.setOnAction(e -> doAsk.run());

        HBox inputBox = new HBox(10, questionField, sendButton);
        inputBox.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(questionField, Priority.ALWAYS);

        aiCard.getChildren().addAll(aiTitle, aiSubtitle, chatScroll, quickChips, inputBox);

        // ================= FAQ ACCORDION =================
        VBox faqCard = new VBox(12);
        faqCard.setPadding(new Insets(20, 24, 20, 24));
        faqCard.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.95);" +
                "-fx-background-radius: 14px;" +
                "-fx-border-color: rgba(45, 106, 79, 0.25);" +
                "-fx-border-width: 1.2px;" +
                "-fx-border-radius: 14px;" +
                "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.04), 8, 0, 0, 2);"
        );

        Text faqTitle = new Text("❓ Frequently Asked Questions (FAQ)");
        faqTitle.setStyle(
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 18px;" +
                "-fx-font-weight: bold;" +
                "-fx-fill: #1B4332;"
        );

        VBox faq1 = createFaqItem("How do I book equipment and ensure the owner confirms?",
                "Browse machinery in the 'Browse Equipment' catalog. Select duration and click 'Rent Equipment Now'. The owner will receive an instant notification to confirm your slot within 15 minutes.");

        VBox faq2 = createFaqItem("How does the Search Operators section work?",
                "Navigate to 'Search Operators' in the sidebar to view certified tractor drivers and drone pilots near your village. Click 'Send Request' to hire them for field operations.");

        VBox faq3 = createFaqItem("When is money released to the machinery owner?",
                "Your payment is safely held in escrow and is only released after the machinery is delivered and utilized satisfactorily on your farm.");

        VBox faq4 = createFaqItem("What if the machine breaks down during field work?",
                "Contact our 24/7 Breakdown Dispatch at 1800-180-1551. We will dispatch a nearby technician or send a replacement unit promptly.");

        faqCard.getChildren().addAll(faqTitle, faq1, faq2, faq3, faq4);

        // ================= MAIN CONTAINER =================
        VBox mainContainer = new VBox(20, contactBox, aiCard, faqCard);
        mainContainer.setPadding(new Insets(20, 30, 35, 30));
        mainContainer.setMaxWidth(Double.MAX_VALUE);
        mainContainer.setStyle("-fx-background-color: transparent;");

        ScrollPane rootScroll = new ScrollPane(mainContainer);
        rootScroll.setFitToWidth(true);
        rootScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        rootScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        rootScroll.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        return rootScroll;
    }

    private static VBox createContactCard(String title, String desc, String bg, String accent) {
        Text t = new Text(title);
        t.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 14px; -fx-font-weight: bold; -fx-fill: " + accent + ";");

        Text d = new Text(desc);
        d.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #4B5563; -fx-line-spacing: 2px;");

        VBox box = new VBox(6, t, d);
        box.setPadding(new Insets(14, 16, 14, 16));
        box.setStyle(
                "-fx-background-color: " + bg + ";" +
                "-fx-background-radius: 14px;" +
                "-fx-border-color: rgba(45, 106, 79, 0.25);" +
                "-fx-border-width: 1.2px;" +
                "-fx-border-radius: 14px;" +
                "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.04), 8, 0, 0, 2);"
        );
        return box;
    }

    private static Label createAIMessage(String text) {
        Label label = new Label(text);
        label.setWrapText(true);
        label.setMaxWidth(750);
        label.setStyle(
                "-fx-background-color: #FFFFFF;" +
                "-fx-text-fill: #1B4332;" +
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 13px;" +
                "-fx-padding: 12px 16px;" +
                "-fx-background-radius: 12px;" +
                "-fx-border-color: rgba(45, 106, 79, 0.2);" +
                "-fx-border-radius: 12px;"
        );
        return label;
    }

    private static Label createUserMessage(String text) {
        Label label = new Label(text);
        label.setWrapText(true);
        label.setMaxWidth(750);
        label.setStyle(
                "-fx-background-color: #DCFCE7;" +
                "-fx-text-fill: #15803D;" +
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 13px;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 10px 16px;" +
                "-fx-background-radius: 12px;" +
                "-fx-border-color: #86EFAC;" +
                "-fx-border-radius: 12px;"
        );
        return label;
    }

    private static VBox createFaqItem(String question, String answer) {
        Text q = new Text("• " + question);
        q.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13.5px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text a = new Text(answer);
        a.setWrappingWidth(800);
        a.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-fill: #4B5563; -fx-line-spacing: 2px;");

        VBox item = new VBox(4, q, a);
        item.setPadding(new Insets(12, 16, 12, 16));
        item.setStyle("-fx-background-color: #F4F9F4; -fx-background-radius: 10px; -fx-border-color: rgba(45, 106, 79, 0.2); -fx-border-radius: 10px;");
        return item;
    }

    private static String generateAIAnswer(String q) {
        String lower = q.toLowerCase();
        if (lower.contains("operator") || lower.contains("driver") || lower.contains("pilot") || lower.contains("hire")) {
            return "👷 You can hire certified operators directly from the 'Search Operators' tab! We have verified tractor drivers (55HP+), combine harvester masters, and DGCA certified drone pilots available near your village.";
        } else if (lower.contains("rent") || lower.contains("book") || lower.contains("tractor")) {
            return "🚜 To rent equipment, visit 'Browse Equipment', pick the machinery you need, click 'Rent Equipment Now', and choose your rental dates. The provider will approve your booking within 15 minutes.";
        } else if (lower.contains("break") || lower.contains("damage") || lower.contains("repair")) {
            return "🛠 In case of roadside machinery breakdown, call our emergency helpline at 1800-180-1551. We provide instant mechanic dispatch within a 30 km radius.";
        } else if (lower.contains("pay") || lower.contains("escrow") || lower.contains("refund")) {
            return "💳 All transactions are protected via FarmEquip Escrow. Your money remains secure until your farm work is completed successfully.";
        } else if (lower.contains("rotavator") || lower.contains("cultivator") || lower.contains("soil") || lower.contains("tillage")) {
            return "🌱 For black cotton soil, a 7ft 45HP+ Rotary Tiller is optimal for fine seedbed preparation, while a 9-Tyne Cultivator is ideal for deep weed aeration.";
        } else {
            return "🌾 Thank you for your question! For specific personalized field queries or booking support, you can also reach our toll-free kisan helpline at 1800-180-1551.";
        }
    }
}