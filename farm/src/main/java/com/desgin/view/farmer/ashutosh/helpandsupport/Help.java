package com.desgin.view.farmer.ashutosh.helpandsupport;

import java.util.ArrayList;
import java.util.List;

import com.desgin.dao.SupportQueryDAO;
import com.desgin.model.SupportMessageModel;
import com.desgin.model.SupportQueryModel;
import com.desgin.view.farmer.Swapnil.FarmerProfileStore;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

public class Help {

    private static SupportQueryDAO queryDAO = new SupportQueryDAO();
    private static SupportQueryModel activeQuery = null;
    private static VBox messagesContainer;
    private static ScrollPane chatScrollPane;
    private static VBox chatCard;
    private static VBox feedbackBox;
    private static HBox inputBar;
    private static Timeline refreshTimeline;
    private static int selectedStarRating = 5;

    public static ScrollPane getHelp() {
        // WhatsApp-like Live Chat with Platform Admin
        chatCard = createChatCard();

        // Top Contact Cards (Helpline & Mobile Dispatch)
        VBox callCard = createContactCard("📞 National Kisan Helpline", "1800-180-1551 (Toll-Free)\nAvailable 6:00 AM – 10:00 PM (All Languages)", "#E8F5E9", "#1B4332");
        VBox whatsappCard = createContactCard("💬 Emergency Breakdown Dispatch", "+91 98220 54321 (Instant Field Dispatch)\nShare tractor/implement breakdown location", "#DCFCE7", "#15803D");

        HBox contactBox = new HBox(14, callCard, whatsappCard);
        contactBox.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(callCard, Priority.ALWAYS);
        HBox.setHgrow(whatsappCard, Priority.ALWAYS);

        // Kisan AI Quick Assistant Card
        VBox aiCard = createAIAssistantCard();

        // NOTE: FAQ Section was removed per user request.
        VBox mainContainer = new VBox(22, chatCard, contactBox, aiCard);
        mainContainer.setPadding(new Insets(20, 30, 40, 30));
        mainContainer.setMaxWidth(Double.MAX_VALUE);
        mainContainer.setStyle("-fx-background-color: transparent;");

        ScrollPane rootScroll = new ScrollPane(mainContainer);
        rootScroll.setFitToWidth(true);
        rootScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        rootScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        rootScroll.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        // Start real-time polling every 3 seconds
        startPolling();

        return rootScroll;
    }

    private static VBox createChatCard() {
        VBox card = new VBox(0);
        card.setStyle(
                "-fx-background-color: #FFFFFF;" +
                "-fx-background-radius: 14px;" +
                "-fx-border-color: #D1E0D7;" +
                "-fx-border-width: 1.5px;" +
                "-fx-border-radius: 14px;" +
                "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.05), 10, 0, 0, 4);");

        // WhatsApp Header
        HBox header = new HBox(12);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(14, 18, 14, 18));
        header.setStyle(
                "-fx-background-color: #1B4332;" +
                "-fx-background-radius: 12px 12px 0 0;");

        Text icon = new Text("🛡️");
        icon.setStyle("-fx-font-size: 24px;");

        VBox titleBox = new VBox(2);
        Text titleText = new Text("FarmEquip Admin Helpdesk (WhatsApp Live)");
        titleText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 15px; -fx-font-weight: bold; -fx-fill: #FFFFFF;");

        Text subtitleText = new Text("🟢 Online • Real-time assistance for farmer bookings, machinery & escrow");
        subtitleText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-fill: #A7D7C5;");
        titleBox.getChildren().addAll(titleText, subtitleText);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button newQueryBtn = new Button("➕ New Query");
        newQueryBtn.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.2);" +
                "-fx-text-fill: #FFFFFF;" +
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 12px;" +
                "-fx-font-weight: 600;" +
                "-fx-padding: 6 12;" +
                "-fx-background-radius: 8px;" +
                "-fx-cursor: hand;");
        newQueryBtn.setOnAction(e -> startFreshQuery());

        header.getChildren().addAll(icon, titleBox, spacer, newQueryBtn);

        // Messages Container inside ScrollPane (WhatsApp Wallpaper styling)
        messagesContainer = new VBox(12);
        messagesContainer.setPadding(new Insets(16, 18, 16, 18));
        messagesContainer.setStyle("-fx-background-color: #EFEAE2;");

        chatScrollPane = new ScrollPane(messagesContainer);
        chatScrollPane.setFitToWidth(true);
        chatScrollPane.setPrefHeight(340);
        chatScrollPane.setMinHeight(280);
        chatScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        chatScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        chatScrollPane.setStyle(
                "-fx-background-color: #EFEAE2;" +
                "-fx-background: #EFEAE2;");

        // Input bar
        inputBar = createInputBar();

        // Feedback Container (shown when query is resolved)
        feedbackBox = new VBox(10);
        feedbackBox.setPadding(new Insets(14, 18, 14, 18));
        feedbackBox.setStyle("-fx-background-color: #F8FAF9; -fx-border-color: #E2EBE5; -fx-border-width: 1 0 0 0;");
        feedbackBox.setVisible(false);
        feedbackBox.setManaged(false);

        card.getChildren().addAll(header, chatScrollPane, feedbackBox, inputBar);

        // Initial fetch from Firestore
        loadActiveQueryAsync();

        return card;
    }

    private static HBox createInputBar() {
        HBox bar = new HBox(10);
        bar.setAlignment(Pos.CENTER);
        bar.setPadding(new Insets(12, 16, 14, 16));
        bar.setStyle(
                "-fx-background-color: #F0F2F5;" +
                "-fx-background-radius: 0 0 12px 12px;" +
                "-fx-border-color: #E2EBE5;" +
                "-fx-border-width: 1 0 0 0;");

        TextField messageInput = new TextField();
        messageInput.setPromptText("Type your question or query here for the platform admin...");
        messageInput.setStyle(
                "-fx-background-color: #FFFFFF;" +
                "-fx-background-radius: 22px;" +
                "-fx-border-color: #CCD0D5;" +
                "-fx-border-radius: 22px;" +
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 13px;" +
                "-fx-padding: 8 16 8 16;");
        HBox.setHgrow(messageInput, Priority.ALWAYS);

        Button sendBtn = new Button("➤ Send");
        sendBtn.setStyle(
                "-fx-background-color: #25D366;" +
                "-fx-text-fill: #FFFFFF;" +
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 13px;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 8 18 8 16;" +
                "-fx-background-radius: 22px;" +
                "-fx-cursor: hand;" +
                "-fx-effect: dropshadow(gaussian, rgba(37, 211, 102, 0.3), 6, 0, 0, 2);");

        sendBtn.setOnAction(e -> {
            String text = messageInput.getText().trim();
            if (!text.isEmpty()) {
                sendMessage(text);
                messageInput.clear();
            }
        });

        messageInput.setOnAction(e -> {
            String text = messageInput.getText().trim();
            if (!text.isEmpty()) {
                sendMessage(text);
                messageInput.clear();
            }
        });

        bar.getChildren().addAll(messageInput, sendBtn);
        return bar;
    }

    private static void sendMessage(String text) {
        String email = FarmerProfileStore.email != null ? FarmerProfileStore.email : "farmer@farmmail.com";
        String name = FarmerProfileStore.name != null ? FarmerProfileStore.name : "Farmer";
        String phone = FarmerProfileStore.phone != null ? FarmerProfileStore.phone : "";

        if (activeQuery == null || "RESOLVED".equalsIgnoreCase(activeQuery.getStatus())) {
            activeQuery = new SupportQueryModel(
                    "QRY_" + System.currentTimeMillis(),
                    email,
                    name,
                    "Farmer",
                    phone,
                    text
            );
        }

        SupportMessageModel msg = new SupportMessageModel(
                "MSG_" + System.currentTimeMillis(),
                email,
                name,
                "Farmer",
                text
        );

        activeQuery.getMessages().add(msg);
        activeQuery.setLastMessage(text);
        activeQuery.setLastUpdated(System.currentTimeMillis());

        renderChatMessages();

        new Thread(() -> {
            queryDAO.saveQuery(activeQuery);
        }).start();
    }

    private static void startFreshQuery() {
        activeQuery = null;
        renderChatMessages();
    }

    private static void loadActiveQueryAsync() {
        String email = FarmerProfileStore.email != null ? FarmerProfileStore.email : "farmer@farmmail.com";
        new Thread(() -> {
            SupportQueryModel q = queryDAO.getActiveQueryForUser(email);
            Platform.runLater(() -> {
                if (q != null) {
                    activeQuery = q;
                }
                renderChatMessages();
            });
        }).start();
    }

    private static void startPolling() {
        if (refreshTimeline != null) {
            refreshTimeline.stop();
        }
        refreshTimeline = new Timeline(new KeyFrame(Duration.seconds(3), e -> {
            String email = FarmerProfileStore.email != null ? FarmerProfileStore.email : "farmer@farmmail.com";
            new Thread(() -> {
                SupportQueryModel q = queryDAO.getActiveQueryForUser(email);
                if (q != null) {
                    Platform.runLater(() -> {
                        if (activeQuery == null ||
                            !q.getQueryId().equals(activeQuery.getQueryId()) ||
                            q.getMessages().size() != activeQuery.getMessages().size() ||
                            !q.getStatus().equals(activeQuery.getStatus()) ||
                            q.isFeedbackGiven() != activeQuery.isFeedbackGiven()) {
                            activeQuery = q;
                            renderChatMessages();
                        }
                    });
                }
            }).start();
        }));
        refreshTimeline.setCycleCount(Timeline.INDEFINITE);
        refreshTimeline.play();
    }

    private static void renderChatMessages() {
        messagesContainer.getChildren().clear();

        if (activeQuery == null || activeQuery.getMessages().isEmpty()) {
            VBox welcomeBox = new VBox(10);
            welcomeBox.setAlignment(Pos.CENTER);
            welcomeBox.setPadding(new Insets(30, 20, 30, 20));

            Text wIcon = new Text("💬");
            wIcon.setStyle("-fx-font-size: 38px;");

            Text wTitle = new Text("Welcome to FarmEquip Farmer Support Desk");
            wTitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 15px; -fx-font-weight: bold; -fx-fill: #1B4332;");

            Text wDesc = new Text("Have a question regarding equipment availability, payments, tractor operators, or escrow?\nSend a query below to connect directly with the platform administration in real-time.");
            wDesc.setTextAlignment(TextAlignment.CENTER);
            wDesc.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-fill: #4B5563; -fx-line-spacing: 4px;");

            welcomeBox.getChildren().addAll(wIcon, wTitle, wDesc);
            messagesContainer.getChildren().add(welcomeBox);

            feedbackBox.setVisible(false);
            feedbackBox.setManaged(false);
            inputBar.setVisible(true);
            inputBar.setManaged(true);
            return;
        }

        for (SupportMessageModel msg : activeQuery.getMessages()) {
            boolean isFarmer = "Farmer".equalsIgnoreCase(msg.getSenderRole()) ||
                               (msg.getSenderEmail() != null && msg.getSenderEmail().equalsIgnoreCase(FarmerProfileStore.email));

            HBox row = new HBox();
            row.setAlignment(isFarmer ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);

            VBox bubble = new VBox(4);
            bubble.setMaxWidth(480);
            bubble.setPadding(new Insets(9, 14, 8, 14));

            if (isFarmer) {
                // Outgoing WhatsApp Bubble
                bubble.setStyle(
                        "-fx-background-color: #D9FDD3;" +
                        "-fx-background-radius: 12px 12px 2px 12px;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.08), 3, 0, 0, 1);");

                Text messageText = new Text(msg.getText());
                messageText.setWrappingWidth(440);
                messageText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-fill: #111B21;");

                HBox meta = new HBox(6);
                meta.setAlignment(Pos.CENTER_RIGHT);

                Text timeText = new Text(msg.getFormattedTime() != null ? msg.getFormattedTime() : "");
                timeText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 10px; -fx-fill: #667781;");

                Text checkmarks = new Text("✔✔");
                checkmarks.setStyle("-fx-font-size: 9.5px; -fx-fill: #53BDEB;");

                meta.getChildren().addAll(timeText, checkmarks);
                bubble.getChildren().addAll(messageText, meta);
            } else {
                // Incoming Admin Bubble
                bubble.setStyle(
                        "-fx-background-color: #FFFFFF;" +
                        "-fx-background-radius: 12px 12px 12px 2px;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.08), 3, 0, 0, 1);");

                Text senderTag = new Text("🛡️ " + (msg.getSenderName() != null ? msg.getSenderName() : "Platform Admin"));
                senderTag.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-fill: #2D6A4F;");

                Text messageText = new Text(msg.getText());
                messageText.setWrappingWidth(440);
                messageText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-fill: #111B21;");

                HBox meta = new HBox(6);
                meta.setAlignment(Pos.CENTER_RIGHT);

                Text timeText = new Text(msg.getFormattedTime() != null ? msg.getFormattedTime() : "");
                timeText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 10px; -fx-fill: #667781;");

                meta.getChildren().add(timeText);
                bubble.getChildren().addAll(senderTag, messageText, meta);
            }

            row.getChildren().add(bubble);
            messagesContainer.getChildren().add(row);
        }

        Platform.runLater(() -> chatScrollPane.setVvalue(1.0));

        if ("RESOLVED".equalsIgnoreCase(activeQuery.getStatus())) {
            renderFeedbackSection();
            inputBar.setVisible(false);
            inputBar.setManaged(false);
        } else {
            feedbackBox.setVisible(false);
            feedbackBox.setManaged(false);
            inputBar.setVisible(true);
            inputBar.setManaged(true);
        }
    }

    private static void renderFeedbackSection() {
        feedbackBox.getChildren().clear();
        feedbackBox.setVisible(true);
        feedbackBox.setManaged(true);

        if (activeQuery.isFeedbackGiven()) {
            HBox settledBanner = new HBox(10);
            settledBanner.setAlignment(Pos.CENTER_LEFT);
            settledBanner.setPadding(new Insets(10, 14, 10, 14));
            settledBanner.setStyle("-fx-background-color: #E8F5E9; -fx-background-radius: 8px; -fx-border-color: #A5D6A7; -fx-border-radius: 8px;");

            Text checkIcon = new Text("✅");
            checkIcon.setStyle("-fx-font-size: 18px;");

            VBox info = new VBox(2);
            Text title = new Text("Support Query Resolved • Thank you for your feedback!");
            title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-font-weight: bold; -fx-fill: #1B4332;");

            String stars = "⭐".repeat(Math.max(1, Math.min(5, activeQuery.getFeedbackRating())));
            Text ratingText = new Text("Your Rating: " + stars + " (" + activeQuery.getFeedbackRating() + "/5)" +
                    (activeQuery.getFeedbackComment() != null && !activeQuery.getFeedbackComment().isEmpty() ? " • \"" + activeQuery.getFeedbackComment() + "\"" : ""));
            ratingText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #2D6A4F;");

            info.getChildren().addAll(title, ratingText);
            settledBanner.getChildren().addAll(checkIcon, info);

            feedbackBox.getChildren().add(settledBanner);
        } else {
            VBox form = new VBox(10);
            form.setPadding(new Insets(12, 16, 14, 16));
            form.setStyle("-fx-background-color: #FFFDE7; -fx-background-radius: 10px; -fx-border-color: #FFE082; -fx-border-width: 1.5px; -fx-border-radius: 10px;");

            Text title = new Text("⭐ Rate Your Support Experience");
            title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 14px; -fx-font-weight: bold; -fx-fill: #B78103;");

            Text subtitle = new Text("The platform administration has resolved this query. Please share your rating and comments:");
            subtitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #5D4037;");

            HBox starBox = new HBox(8);
            starBox.setAlignment(Pos.CENTER_LEFT);

            List<Button> starButtons = new ArrayList<>();
            for (int i = 1; i <= 5; i++) {
                final int rating = i;
                Button starBtn = new Button("⭐ " + i);
                starBtn.setStyle(
                        "-fx-background-color: " + (i <= selectedStarRating ? "#FFC107" : "#EEEEEE") + ";" +
                        "-fx-text-fill: " + (i <= selectedStarRating ? "#FFFFFF" : "#666666") + ";" +
                        "-fx-font-family: 'Poppins';" +
                        "-fx-font-size: 12px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 5 12;" +
                        "-fx-background-radius: 6px;" +
                        "-fx-cursor: hand;");

                starBtn.setOnAction(e -> {
                    selectedStarRating = rating;
                    for (int j = 0; j < starButtons.size(); j++) {
                        int r = j + 1;
                        starButtons.get(j).setStyle(
                                "-fx-background-color: " + (r <= selectedStarRating ? "#FFC107" : "#EEEEEE") + ";" +
                                "-fx-text-fill: " + (r <= selectedStarRating ? "#FFFFFF" : "#666666") + ";" +
                                "-fx-font-family: 'Poppins';" +
                                "-fx-font-size: 12px;" +
                                "-fx-font-weight: bold;" +
                                "-fx-padding: 5 12;" +
                                "-fx-background-radius: 6px;" +
                                "-fx-cursor: hand;");
                    }
                });
                starButtons.add(starBtn);
                starBox.getChildren().add(starBtn);
            }

            TextArea commentField = new TextArea();
            commentField.setPromptText("Optional: Share comments on how quickly and well your issue was handled...");
            commentField.setPrefRowCount(2);
            commentField.setStyle(
                    "-fx-background-color: #FFFFFF;" +
                    "-fx-border-color: #D1D5DB;" +
                    "-fx-border-radius: 6px;" +
                    "-fx-font-family: 'Poppins';" +
                    "-fx-font-size: 12px;");

            Button submitFeedbackBtn = new Button("Submit Support Feedback ✔");
            submitFeedbackBtn.setStyle(
                    "-fx-background-color: #1B4332;" +
                    "-fx-text-fill: #FFFFFF;" +
                    "-fx-font-family: 'Poppins';" +
                    "-fx-font-size: 12.5px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-padding: 7 16;" +
                    "-fx-background-radius: 8px;" +
                    "-fx-cursor: hand;");

            submitFeedbackBtn.setOnAction(e -> {
                String comment = commentField.getText().trim();
                activeQuery.setFeedbackRating(selectedStarRating);
                activeQuery.setFeedbackComment(comment);
                activeQuery.setFeedbackGiven(true);
                new Thread(() -> {
                    queryDAO.submitFeedback(activeQuery.getQueryId(), selectedStarRating, comment);
                }).start();
                renderChatMessages();
            });

            form.getChildren().addAll(title, subtitle, starBox, commentField, submitFeedbackBtn);
            feedbackBox.getChildren().add(form);
        }
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
                "-fx-border-width: 1px;" +
                "-fx-border-radius: 14px;");
        return box;
    }

    private static VBox createAIAssistantCard() {
        VBox aiCard = new VBox(14);
        aiCard.setPadding(new Insets(20, 24, 20, 24));
        aiCard.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.95);" +
                "-fx-background-radius: 14px;" +
                "-fx-border-color: rgba(45, 106, 79, 0.25);" +
                "-fx-border-width: 1.2px;" +
                "-fx-border-radius: 14px;" +
                "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.04), 8, 0, 0, 2);");

        Text aiTitle = new Text("🤖 Kisan AI Smart Assistant");
        aiTitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 17px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text aiSubtitle = new Text("Ask anything about machinery rent calculations, operator hiring, soil preparation, or agronomy recommendations.");
        aiSubtitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #4B5563;");

        VBox chatBox = new VBox(10);
        chatBox.setPadding(new Insets(14));
        chatBox.setStyle(
                "-fx-background-color: #F4F9F4;" +
                "-fx-background-radius: 12px;" +
                "-fx-border-color: rgba(45, 106, 79, 0.2);" +
                "-fx-border-radius: 12px;" +
                "-fx-border-width: 1px;");

        Label welcomeMessage = createAIMessage(
                "Namaste! 🙏 I am your Kisan AI Smart Assistant.\n" +
                "You can ask me questions about implements, tractor HP ratings, or escrow security.");
        chatBox.getChildren().add(welcomeMessage);

        ScrollPane chatScroll = new ScrollPane(chatBox);
        chatScroll.setPrefHeight(180);
        chatScroll.setFitToWidth(true);
        chatScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        chatScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        chatScroll.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        FlowPane quickChips = new FlowPane(8, 8);
        String[] suggestions = {
                "🚜 How to hire an operator?",
                "💳 Escrow security info",
                "🌧 Rain advisory for Pune",
                "⚙ Recommended rotavator HP"
        };

        TextField questionField = new TextField();
        questionField.setPromptText("Type your agricultural question here...");
        questionField.setPrefHeight(40);
        questionField.setStyle(
                "-fx-background-color: #FFFFFF;" +
                "-fx-background-radius: 10px;" +
                "-fx-border-color: rgba(45, 106, 79, 0.3);" +
                "-fx-border-width: 1.2px;" +
                "-fx-border-radius: 10px;" +
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 12.5px;" +
                "-fx-text-fill: #1F2937;" +
                "-fx-padding: 0 14px;");

        for (String chipText : suggestions) {
            Button chip = new Button(chipText);
            chip.setStyle("-fx-background-color: #E8F5E9; -fx-text-fill: #2D6A4F; -fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: 500; -fx-padding: 4px 10px; -fx-background-radius: 12px; -fx-cursor: hand; -fx-border-color: rgba(45, 106, 79, 0.25); -fx-border-radius: 12px;");
            chip.setOnAction(e -> questionField.setText(chipText.replace("🚜 ", "").replace("💳 ", "").replace("🌧 ", "").replace("⚙ ", "")));
            quickChips.getChildren().add(chip);
        }

        Button sendButton = new Button("Ask AI ➤");
        sendButton.setPrefHeight(40);
        sendButton.setStyle(
                "-fx-background-color: linear-gradient(to right, #2D6A4F, #40916C);" +
                "-fx-text-fill: white;" +
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 12.5px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 10px;" +
                "-fx-cursor: hand;");

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
        return aiCard;
    }

    private static Label createUserMessage(String text) {
        Label msg = new Label("👨🌾 You: " + text);
        msg.setWrapText(true);
        msg.setStyle(
                "-fx-background-color: #DCFCE7;" +
                "-fx-text-fill: #14532D;" +
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 12px;" +
                "-fx-padding: 8px 12px;" +
                "-fx-background-radius: 8px;" +
                "-fx-alignment: CENTER_RIGHT;");
        return msg;
    }

    private static Label createAIMessage(String text) {
        Label msg = new Label("🤖 AI: " + text);
        msg.setWrapText(true);
        msg.setStyle(
                "-fx-background-color: #FFFFFF;" +
                "-fx-text-fill: #1F2937;" +
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 12px;" +
                "-fx-padding: 8px 12px;" +
                "-fx-background-radius: 8px;" +
                "-fx-border-color: #E5E7EB;" +
                "-fx-border-radius: 8px;");
        return msg;
    }

    private static String generateAIAnswer(String q) {
        String lower = q.toLowerCase();
        if (lower.contains("hire") || lower.contains("operator")) {
            return "To hire an operator, navigate to 'Search Operators' in the sidebar. You can inspect driver ratings, certifications, and dispatch them to your farm plot.";
        } else if (lower.contains("escrow") || lower.contains("payment")) {
            return "FarmEquip uses automated escrow security. Your payment is held in escrow and released only after you approve field work completion.";
        } else if (lower.contains("rotavator") || lower.contains("hp")) {
            return "For a standard 5 to 6-foot rotavator, a tractor with 45–55 HP dual-clutch PTO is recommended for optimal soil tilth.";
        } else if (lower.contains("rain") || lower.contains("weather")) {
            return "Light precipitation forecasted over Pune rural belt in next 48 hours. Ensure post-harvest produce is covered.";
        }
        return "Thank you for asking. Our Kisan support team and automated farm advisors are ready to assist you. You can also chat directly with Platform Admin above!";
    }
}