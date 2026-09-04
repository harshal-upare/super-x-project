package com.desgin.view.provider;

import java.util.ArrayList;
import java.util.List;

import com.desgin.dao.SupportQueryDAO;
import com.desgin.model.SupportMessageModel;
import com.desgin.model.SupportQueryModel;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
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

public class ProviderHelp {

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
        Text headerTitle = new Text("Provider Help, Support & Claims Desk");
        headerTitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 26px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text headerSubtitle = new Text("Access 24/7 dedicated provider assistance, machinery insurance claims, farmer dispute resolution, and rental guidelines.");
        headerSubtitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-fill: #4B5563;");

        VBox titleBox = new VBox(4, headerTitle, headerSubtitle);

        // WhatsApp-like Live Chat Card for Provider
        chatCard = createChatCard();

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

        VBox content = new VBox(22, titleBox, chatCard, channels, insuranceCard, faqCard);
        content.setPadding(new Insets(25, 30, 40, 30));

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        // Start live polling
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

        Text subtitleText = new Text("🟢 Online • Real-time query support for machinery providers & fleet owners");
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

        // Messages Container inside ScrollPane
        messagesContainer = new VBox(12);
        messagesContainer.setPadding(new Insets(16, 18, 16, 18));
        messagesContainer.setStyle("-fx-background-color: #EFEAE2;");

        chatScrollPane = new ScrollPane(messagesContainer);
        chatScrollPane.setFitToWidth(true);
        chatScrollPane.setPrefHeight(340);
        chatScrollPane.setMinHeight(280);
        chatScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        chatScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        chatScrollPane.setStyle("-fx-background-color: #EFEAE2; -fx-background: #EFEAE2;");

        inputBar = createInputBar();

        feedbackBox = new VBox(10);
        feedbackBox.setPadding(new Insets(14, 18, 14, 18));
        feedbackBox.setStyle("-fx-background-color: #F8FAF9; -fx-border-color: #E2EBE5; -fx-border-width: 1 0 0 0;");
        feedbackBox.setVisible(false);
        feedbackBox.setManaged(false);

        card.getChildren().addAll(header, chatScrollPane, feedbackBox, inputBar);

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
        String email = ProviderProfileStore.email != null ? ProviderProfileStore.email : "provider@farmequip.com";
        String name = ProviderProfileStore.name != null ? ProviderProfileStore.name : "Provider";
        String phone = ProviderProfileStore.phone != null ? ProviderProfileStore.phone : "";

        if (activeQuery == null || "RESOLVED".equalsIgnoreCase(activeQuery.getStatus())) {
            activeQuery = new SupportQueryModel(
                    "QRY_" + System.currentTimeMillis(),
                    email,
                    name,
                    "Provider",
                    phone,
                    text
            );
        }

        SupportMessageModel msg = new SupportMessageModel(
                "MSG_" + System.currentTimeMillis(),
                email,
                name,
                "Provider",
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
        String email = ProviderProfileStore.email != null ? ProviderProfileStore.email : "provider@farmequip.com";
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
            String email = ProviderProfileStore.email != null ? ProviderProfileStore.email : "provider@farmequip.com";
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

            Text wTitle = new Text("Welcome to FarmEquip Machinery Provider Support Desk");
            wTitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 15px; -fx-font-weight: bold; -fx-fill: #1B4332;");

            Text wDesc = new Text("Have a question about equipment approvals, rental settlements, damage insurance, or farmer disputes?\nSend a query below to connect instantly with the platform administration.");
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
            boolean isProvider = "Provider".equalsIgnoreCase(msg.getSenderRole()) ||
                                 (msg.getSenderEmail() != null && msg.getSenderEmail().equalsIgnoreCase(ProviderProfileStore.email));

            HBox row = new HBox();
            row.setAlignment(isProvider ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);

            VBox bubble = new VBox(4);
            bubble.setMaxWidth(480);
            bubble.setPadding(new Insets(9, 14, 8, 14));

            if (isProvider) {
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

            Text subtitle = new Text("The platform administration has resolved this query. Please share your rating and feedback:");
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
            commentField.setPromptText("Optional: Comments on assistance provided...");
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

    private static VBox createContactCard(String title, String contact, String sub, String color) {
        Text t = new Text(title);
        t.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 14px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text c = new Text(contact);
        c.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 16px; -fx-font-weight: bold; -fx-fill: " + color + ";");

        Text s = new Text(sub);
        s.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-fill: #4B5563;");

        VBox b = new VBox(6, t, c, s);
        b.setPrefWidth(320);
        b.setPadding(new Insets(16));
        b.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 12; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 12;");
        return b;
    }

    private static VBox createInsuranceCard() {
        Text title = new Text("🛡 FarmEquip Comprehensive Machinery Protection & Damage Shield");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 17px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text desc = new Text("Every booking through FarmEquip is insured up to ₹5,00,000 for accidental machinery damages, overturning, fire, and theft during rental operations.");
        desc.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-fill: #374151;");

        HBox step1 = createStepBox("Step 1", "Inspect & Photograph", "Capture 4 clear photos of damaged components on the field.");
        HBox step2 = createStepBox("Step 2", "Submit Incident Log", "Report via app or hotline within 24 hours of incident.");
        HBox step3 = createStepBox("Step 3", "Instant Survey & Repair", "Approved claim amount credited to provider bank within 48h.");

        HBox steps = new HBox(12, step1, step2, step3);

        Button claimBtn = new Button("📄  Initiate New Insurance Claim");
        claimBtn.setStyle("-fx-background-color: #2E7D32; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 8 16 8 16;");

        VBox card = new VBox(12, title, desc, steps, claimBtn);
        card.setPadding(new Insets(18));
        card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 14; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 14;");
        return card;
    }

    private static HBox createStepBox(String step, String name, String sub) {
        Text s = new Text(step);
        s.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-fill: #2E7D32;");

        Text n = new Text(name);
        n.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text sb = new Text(sub);
        sb.setWrappingWidth(260);
        sb.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-fill: #4B5563;");

        VBox vb = new VBox(2, s, n, sb);
        vb.setPadding(new Insets(10));
        vb.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 8; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 8;");
        return new HBox(vb);
    }

    private static VBox createFaqCard() {
        Text title = new Text("Frequently Asked Provider Questions (FAQs)");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 17px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        VBox q1 = createFaqItem("How are rental earnings credited to my bank account?", "Rental payments from farmers are held securely in platform escrow and automatically released to your registered bank account via IMPS/NEFT upon completion of the rental job.");
        VBox q2 = createFaqItem("What happens if a farmer delays returning the machinery?", "Late returns automatically incur an overdue penalty rate of 1.5x the hourly rate, charged directly to the farmer's security deposit.");
        VBox q3 = createFaqItem("Can I provide trained operators with my heavy machinery?", "Yes! When registering machinery in your Fleet tab, toggle 'Trained Driver / Operator Available'. Farmers can book the machine inclusive of your operator.");
        VBox q4 = createFaqItem("What is FarmEquip's platform commission fee?", "FarmEquip charges a minimal 5% platform fee on completed rentals, which covers 24/7 GPS fleet tracking, payment processing, and comprehensive damage insurance.");

        VBox card = new VBox(12, title, q1, q2, q3, q4);
        card.setPadding(new Insets(18));
        card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 14; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 14;");
        return card;
    }

    private static VBox createFaqItem(String question, String answer) {
        Text q = new Text("Q: " + question);
        q.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text a = new Text(answer);
        a.setWrappingWidth(920);
        a.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #374151;");

        VBox b = new VBox(4, q, a);
        b.setPadding(new Insets(10));
        b.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 8; -fx-border-color: #E2EBE5; -fx-border-width: 0.5; -fx-border-radius: 8;");
        return b;
    }
}
