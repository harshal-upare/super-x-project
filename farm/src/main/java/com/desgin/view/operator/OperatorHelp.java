package com.desgin.view.operator;

import java.util.ArrayList;
import java.util.List;

import com.desgin.dao.SupportQueryDAO;
import com.desgin.model.SupportMessageModel;
import com.desgin.model.SupportQueryModel;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

public class OperatorHelp {

    private static SupportQueryDAO queryDAO = new SupportQueryDAO();
    private static SupportQueryModel activeQuery = null;
    private static VBox messagesContainer;
    private static ScrollPane chatScrollPane;
    private static VBox chatCard;
    private static VBox feedbackBox;
    private static HBox inputBar;
    private static Timeline refreshTimeline;
    private static int selectedStarRating = 5;

    public static ScrollPane getHelpSection() {
        // WhatsApp-like Live Chat Card
        chatCard = createChatCard();

        VBox content = new VBox(20, chatCard);
        content.setPadding(new Insets(20, 30, 40, 30));
        content.setMaxWidth(Double.MAX_VALUE);

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        // Start real-time polling every 3 seconds
        startPolling();

        return scrollPane;
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

        Text subtitleText = new Text("🟢 Online • Instant query support & issue assistance");
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
        chatScrollPane.setPrefHeight(480);
        chatScrollPane.setMinHeight(360);
        chatScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        chatScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        chatScrollPane.setStyle(
                "-fx-background-color: #EFEAE2;" +
                "-fx-background: #EFEAE2;");

        // Input bar
        inputBar = createInputBar();

        // Feedback Container (dynamically shown if query is resolved)
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
        String email = OperatorProfileStore.email != null ? OperatorProfileStore.email : "operator@farm.com";
        String name = OperatorProfileStore.name != null ? OperatorProfileStore.name : "Operator";
        String phone = OperatorProfileStore.phone != null ? OperatorProfileStore.phone : "";

        // If no active query or if existing active query is resolved, spin a new one
        if (activeQuery == null || "RESOLVED".equalsIgnoreCase(activeQuery.getStatus())) {
            activeQuery = new SupportQueryModel(
                    "QRY_" + System.currentTimeMillis(),
                    email,
                    name,
                    "Operator",
                    phone,
                    text
            );
        }

        SupportMessageModel msg = new SupportMessageModel(
                "MSG_" + System.currentTimeMillis(),
                email,
                name,
                "Operator",
                text
        );

        activeQuery.getMessages().add(msg);
        activeQuery.setLastMessage(text);
        activeQuery.setLastUpdated(System.currentTimeMillis());

        // Update UI immediately
        renderChatMessages();

        // Persist to Firestore in background
        new Thread(() -> {
            queryDAO.saveQuery(activeQuery);
        }).start();
    }

    private static void startFreshQuery() {
        activeQuery = null;
        renderChatMessages();
    }

    private static void loadActiveQueryAsync() {
        String email = OperatorProfileStore.email != null ? OperatorProfileStore.email : "operator@farm.com";
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
            String email = OperatorProfileStore.email != null ? OperatorProfileStore.email : "operator@farm.com";
            new Thread(() -> {
                SupportQueryModel q = queryDAO.getActiveQueryForUser(email);
                if (q != null) {
                    Platform.runLater(() -> {
                        // Check if new messages arrived or status changed
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

            Text wTitle = new Text("Welcome to FarmEquip Operator Support Desk");
            wTitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 15px; -fx-font-weight: bold; -fx-fill: #1B4332;");

            Text wDesc = new Text("Have a question about field machinery, tractor bookings, payouts, or SOS mechanics?\nSend a query below to connect instantly with the platform administration.");
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

        // Render existing messages
        for (SupportMessageModel msg : activeQuery.getMessages()) {
            boolean isOperator = "Operator".equalsIgnoreCase(msg.getSenderRole()) ||
                                 (msg.getSenderEmail() != null && msg.getSenderEmail().equalsIgnoreCase(OperatorProfileStore.email));

            HBox row = new HBox();
            row.setAlignment(isOperator ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);

            VBox bubble = new VBox(4);
            bubble.setMaxWidth(480);
            bubble.setPadding(new Insets(9, 14, 8, 14));

            if (isOperator) {
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

        // Scroll to bottom
        Platform.runLater(() -> chatScrollPane.setVvalue(1.0));

        // Check resolved status and feedback
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
            // Already submitted feedback display
            HBox settledBanner = new HBox(10);
            settledBanner.setAlignment(Pos.CENTER_LEFT);
            settledBanner.setPadding(new Insets(10, 14, 10, 14));
            settledBanner.setStyle("-fx-background-color: #E8F5E9; -fx-background-radius: 8px; -fx-border-color: #A5D6A7; -fx-border-radius: 8px;");

            Text checkIcon = new Text("✅");
            checkIcon.setStyle("-fx-font-size: 18px;");

            VBox info = new VBox(2);
            Text title = new Text("Support Session Resolved • Thank you for your feedback!");
            title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-font-weight: bold; -fx-fill: #1B4332;");

            String stars = "⭐".repeat(Math.max(1, Math.min(5, activeQuery.getFeedbackRating())));
            Text ratingText = new Text("Your Rating: " + stars + " (" + activeQuery.getFeedbackRating() + "/5)" +
                    (activeQuery.getFeedbackComment() != null && !activeQuery.getFeedbackComment().isEmpty() ? " • \"" + activeQuery.getFeedbackComment() + "\"" : ""));
            ratingText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #2D6A4F;");

            info.getChildren().addAll(title, ratingText);
            settledBanner.getChildren().addAll(checkIcon, info);

            feedbackBox.getChildren().add(settledBanner);
        } else {
            // Active Feedback Form
            VBox form = new VBox(10);
            form.setPadding(new Insets(12, 16, 14, 16));
            form.setStyle("-fx-background-color: #FFFDE7; -fx-background-radius: 10px; -fx-border-color: #FFE082; -fx-border-width: 1.5px; -fx-border-radius: 10px;");

            Text title = new Text("⭐ Rate Your Support Experience");
            title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 14px; -fx-font-weight: bold; -fx-fill: #B78103;");

            Text subtitle = new Text("The platform administration has resolved this query. Please share your feedback to help us serve you better:");
            subtitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #5D4037;");

            // 5 Stars selector
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
            commentField.setPromptText("Optional: Write comments on how your query was handled...");
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
}
