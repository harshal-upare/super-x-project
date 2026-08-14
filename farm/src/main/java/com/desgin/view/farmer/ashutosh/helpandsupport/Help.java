package com.desgin.view.farmer.ashutosh.helpandsupport;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Help {

    public ScrollPane getHelp() {

        // ==========================================
        // MAIN HEADER
        // ==========================================

        Label titleLabel = new Label("Help & Support 🛟");

        titleLabel.setStyle(
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 24px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #4A2C20;"
        );

        Label subtitleLabel = new Label(
                "Get help with equipment, bookings and farming questions."
        );

        subtitleLabel.setStyle(
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 14px;" +
                "-fx-text-fill: #806A5B;"
        );

        VBox headerBox = new VBox(
                5,
                titleLabel,
                subtitleLabel
        );


        // ==========================================
        // CONTACT CARDS
        // ==========================================

        VBox callCard = new VBox(8);

        callCard.setPadding(new Insets(15));
        callCard.setPrefWidth(300);

        callCard.setStyle(
                "-fx-background-color: #F5EFE6;" +
                "-fx-background-radius: 10;" +
                "-fx-border-color: #D8C7B5;" +
                "-fx-border-radius: 10;"
        );

        Label callTitle = new Label("📞 Toll-Free Helpline");

        callTitle.setStyle(
                "-fx-font-size: 16px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #4A2C20;"
        );

        Label callNumber = new Label(
                "1800-123-4567\nMon - Sat (8 AM to 8 PM)"
        );

        callNumber.setStyle(
                "-fx-font-size: 12px;" +
                "-fx-text-fill: #806A5B;"
        );

        callCard.getChildren().addAll(
                callTitle,
                callNumber
        );


        VBox whatsappCard = new VBox(8);

        whatsappCard.setPadding(new Insets(15));
        whatsappCard.setPrefWidth(300);

        whatsappCard.setStyle(
                "-fx-background-color: #F5EFE6;" +
                "-fx-background-radius: 10;" +
                "-fx-border-color: #D8C7B5;" +
                "-fx-border-radius: 10;"
        );

        Label whatsappTitle = new Label(
                "💬 WhatsApp Support"
        );

        whatsappTitle.setStyle(
                "-fx-font-size: 16px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #4A2C20;"
        );

        Label whatsappNumber = new Label(
                "+91 98765 43210\nGet instant reply on chat"
        );

        whatsappNumber.setStyle(
                "-fx-font-size: 12px;" +
                "-fx-text-fill: #806A5B;"
        );

        whatsappCard.getChildren().addAll(
                whatsappTitle,
                whatsappNumber
        );


        HBox contactBox = new HBox(
                20,
                callCard,
                whatsappCard
        );


        // ==========================================
        // AI FARMER ASSISTANT
        // ==========================================

        Label aiTitle = new Label(
                "🤖 AI Farmer Assistant"
        );

        aiTitle.setStyle(
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 20px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #4A2C20;"
        );

        Label aiSubtitle = new Label(
                "Ask me about equipment, bookings and common farming problems."
        );

        aiSubtitle.setStyle(
                "-fx-font-size: 13px;" +
                "-fx-text-fill: #806A5B;"
        );


        // Chat area

        VBox chatBox = new VBox(10);

        chatBox.setPadding(new Insets(15));

        chatBox.setStyle(
                "-fx-background-color: #FFFDF9;" +
                "-fx-background-radius: 10;" +
                "-fx-border-color: #D8C7B5;" +
                "-fx-border-radius: 10;"
        );


        // Initial AI message

        Label welcomeMessage = createAIMessage(
                "Hello! 👋 I am your AI Farmer Assistant.\n\n" +
                "You can ask me things like:\n" +
                "• How do I book a tractor?\n" +
                "• My tractor booking failed.\n" +
                "• What should I do if equipment breaks down?\n" +
                "• How can I improve my crop production?\n\n" +
                "How can I help you today?"
        );

        chatBox.getChildren().add(
                welcomeMessage
        );


        // Chat scroll

        ScrollPane chatScroll = new ScrollPane(
                chatBox
        );

        chatScroll.setPrefHeight(300);
        chatScroll.setFitToWidth(true);

        chatScroll.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER
        );

        chatScroll.setVbarPolicy(
                ScrollPane.ScrollBarPolicy.AS_NEEDED
        );

        chatScroll.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-background: transparent;"
        );


        // ==========================================
        // USER INPUT
        // ==========================================

        TextField questionField = new TextField();

        questionField.setPromptText(
                "Ask your farming question..."
        );

        questionField.setPrefHeight(42);

        questionField.setStyle(
                "-fx-background-color: white;" +
                "-fx-background-radius: 8;" +
                "-fx-border-color: #D8C7B5;" +
                "-fx-border-radius: 8;" +
                "-fx-font-size: 13px;"
        );


        Button sendButton = new Button(
                "Send ➤"
        );

        sendButton.setPrefHeight(42);

        sendButton.setPrefWidth(90);

        sendButton.setStyle(
                "-fx-background-color: #8B6F47;" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 8;" +
                "-fx-cursor: hand;"
        );


        HBox inputBox = new HBox(
                10,
                questionField,
                sendButton
        );

        inputBox.setAlignment(
                Pos.CENTER_LEFT
        );

        HBox.setHgrow(
                questionField,
                javafx.scene.layout.Priority.ALWAYS
        );


        // ==========================================
        // SEND MESSAGE
        // ==========================================

        sendButton.setOnAction(event -> {

            String question =
                    questionField.getText().trim();

            if (question.isEmpty()) {
                return;
            }


            // Display user message

            Label userMessage =
                    createUserMessage(question);

            chatBox.getChildren().add(
                    userMessage
            );

            questionField.clear();

            sendButton.setDisable(true);


            // Loading message

            Label loadingMessage =
                    createAIMessage(
                            "🤔 Thinking..."
                    );

            chatBox.getChildren().add(
                    loadingMessage
            );


            // Scroll to bottom

            Platform.runLater(() -> {
                chatScroll.setVvalue(1.0);
            });


            // ==========================================
            // CALL AI IN BACKGROUND
            // ==========================================

            Thread aiThread = new Thread(() -> {

                String response;

                try {

                    response =
                            FarmerAI.getAnswer(question);

                } catch (Exception e) {

                    response =
                            "Sorry, I could not connect to the AI service right now.\n\n" +
                            "Please try again or contact our support team.";

                    e.printStackTrace();
                }


                final String finalResponse =
                        response;


                // Update JavaFX UI

                Platform.runLater(() -> {

                    chatBox.getChildren().remove(
                            loadingMessage
                    );

                    Label aiMessage =
                            createAIMessage(
                                    finalResponse
                            );

                    chatBox.getChildren().add(
                            aiMessage
                    );

                    sendButton.setDisable(false);

                    chatScroll.setVvalue(1.0);
                });

            });


            aiThread.setDaemon(true);

            aiThread.start();
        });


        // Press ENTER to send

        questionField.setOnAction(
                event -> sendButton.fire()
        );


        VBox aiAssistantBox = new VBox(
                10,
                aiTitle,
                aiSubtitle,
                chatScroll,
                inputBox
        );

        aiAssistantBox.setPadding(
                new Insets(15)
        );

        aiAssistantBox.setStyle(
                "-fx-background-color: #F5EFE6;" +
                "-fx-background-radius: 12;" +
                "-fx-border-color: #D8C7B5;" +
                "-fx-border-radius: 12;"
        );


        // ==========================================
        // FAQ SECTION
        // ==========================================

        Label faqTitle = new Label(
                "Frequently Asked Questions (FAQs)"
        );

        faqTitle.setStyle(
                "-fx-font-size: 18px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #4A2C20;"
        );


        VBox faq1 = createFaqItem(
                "Q1: How do I book a tractor?",
                "Go to the Equipment page, choose your tractor, " +
                "select dates and click 'Book Now'."
        );


        VBox faq2 = createFaqItem(
                "Q2: What if the equipment breaks down?",
                "Stop using the equipment if it is unsafe and " +
                "contact the support team immediately."
        );


        VBox faq3 = createFaqItem(
                "Q3: When do I get my deposit back?",
                "The deposit is refunded according to the booking " +
                "and equipment return policy."
        );


        VBox faqContainer = new VBox(
                10,
                faqTitle,
                faq1,
                faq2,
                faq3
        );


        // ==========================================
        // SEND QUERY FORM
        // ==========================================

        Label formTitle = new Label(
                "Send Us a Message"
        );

        formTitle.setStyle(
                "-fx-font-size: 18px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #4A2C20;"
        );


        Label nameLabel = new Label(
                "Your Name:"
        );

        nameLabel.setStyle(
                "-fx-text-fill: #4A2C20;" +
                "-fx-font-weight: bold;"
        );


        TextField nameField = new TextField();

        nameField.setPromptText(
                "Enter your name"
        );


        Label msgLabel = new Label(
                "Describe Your Problem:"
        );

        msgLabel.setStyle(
                "-fx-text-fill: #4A2C20;" +
                "-fx-font-weight: bold;"
        );


        TextArea msgArea = new TextArea();

        msgArea.setPromptText(
                "Write your problem or question here..."
        );

        msgArea.setPrefRowCount(4);

        msgArea.setWrapText(true);


        Button submitButton = new Button(
                "Submit Problem"
        );

        submitButton.setStyle(
                "-fx-background-color: #8B6F47;" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 6;" +
                "-fx-padding: 8 16;"
        );


        Label resultLabel = new Label();


        submitButton.setOnAction(e -> {

            String name =
                    nameField.getText().trim();

            String msg =
                    msgArea.getText().trim();


            if (name.isEmpty() || msg.isEmpty()) {

                resultLabel.setText(
                        "⚠️ Please fill in all fields!"
                );

                resultLabel.setStyle(
                        "-fx-text-fill: red;" +
                        "-fx-font-weight: bold;"
                );

            } else {

                resultLabel.setText(
                        "✅ Thank you, " +
                        name +
                        "! Your request has been submitted."
                );

                resultLabel.setStyle(
                        "-fx-text-fill: green;" +
                        "-fx-font-weight: bold;"
                );

                nameField.clear();

                msgArea.clear();
            }
        });


        VBox formBox = new VBox(
                10,
                formTitle,
                nameLabel,
                nameField,
                msgLabel,
                msgArea,
                submitButton,
                resultLabel
        );

        formBox.setPadding(
                new Insets(15)
        );

        formBox.setStyle(
                "-fx-background-color: #F5EFE6;" +
                "-fx-background-radius: 10;" +
                "-fx-border-color: #D8C7B5;" +
                "-fx-border-radius: 10;"
        );


        // ==========================================
        // MAIN PAGE
        // ==========================================

        VBox mainLayout = new VBox(
                20,
                headerBox,
                contactBox,
                aiAssistantBox,
                faqContainer,
                formBox
        );

        mainLayout.setPadding(
                new Insets(25)
        );


        ScrollPane mainScroll =
                new ScrollPane(mainLayout);

        mainScroll.setFitToWidth(true);

        mainScroll.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER
        );

        mainScroll.setVbarPolicy(
                ScrollPane.ScrollBarPolicy.AS_NEEDED
        );

        mainScroll.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-background: transparent;"
        );


        return mainScroll;
    }


    // ==========================================
    // CREATE USER CHAT MESSAGE
    // ==========================================

    private Label createUserMessage(
            String message
    ) {

        Label label = new Label(
                "You: " + message
        );

        label.setWrapText(true);

        label.setMaxWidth(650);

        label.setStyle(
                "-fx-background-color: #E4D3C2;" +
                "-fx-background-radius: 10;" +
                "-fx-padding: 10;" +
                "-fx-text-fill: #4A2C20;" +
                "-fx-font-size: 13px;"
        );

        return label;
    }


    // ==========================================
    // CREATE AI CHAT MESSAGE
    // ==========================================

    private Label createAIMessage(
            String message
    ) {

        Label label = new Label(
                "🤖 AI Farmer Assistant:\n" + message
        );

        label.setWrapText(true);

        label.setMaxWidth(700);

        label.setStyle(
                "-fx-background-color: #F5EFE6;" +
                "-fx-background-radius: 10;" +
                "-fx-border-color: #D8C7B5;" +
                "-fx-border-radius: 10;" +
                "-fx-padding: 10;" +
                "-fx-text-fill: #4A2C20;" +
                "-fx-font-size: 13px;"
        );

        return label;
    }


    // ==========================================
    // FAQ HELPER
    // ==========================================

    private VBox createFaqItem(
            String question,
            String answer
    ) {

        Label qLabel =
                new Label(question);

        qLabel.setStyle(
                "-fx-font-size: 14px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #4A2C20;"
        );


        Label aLabel =
                new Label(answer);

        aLabel.setWrapText(true);

        aLabel.setStyle(
                "-fx-font-size: 12px;" +
                "-fx-text-fill: #5C4033;"
        );


        VBox box = new VBox(
                4,
                qLabel,
                aLabel
        );

        box.setPadding(
                new Insets(10)
        );

        box.setStyle(
                "-fx-background-color: #F5EFE6;" +
                "-fx-background-radius: 8;" +
                "-fx-border-color: #D8C7B5;" +
                "-fx-border-radius: 8;"
        );

        return box;
    }
}