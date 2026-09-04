package com.desgin.view.admin;

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
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

public class UserQueries {

    private static SupportQueryDAO queryDAO = new SupportQueryDAO();
    private static List<SupportQueryModel> allQueries = new ArrayList<>();
    private static SupportQueryModel selectedQuery = null;

    private static String currentStatusFilter = "ALL"; // "ALL", "OPEN", "RESOLVED"
    private static String currentRoleFilter = "ALL";   // "ALL", "OPERATOR", "FARMER", "PROVIDER"
    private static String searchQuery = "";

    private static VBox queryListContainer;
    private static VBox chatAreaContainer;
    private static VBox messagesVBox;
    private static ScrollPane chatMessagesScroll;
    private static HBox replyBar;
    private static VBox feedbackCard;
    private static Timeline refreshTimeline;

    public static BorderPane getPage(StackPane root) {
        BorderPane mainLayout = new BorderPane();
        mainLayout.setStyle("-fx-background-color: #F8FAF9;");

        // 2-pane layout: Left is thread list, Right is active chat
        HBox contentBox = new HBox(0);
        HBox.setHgrow(contentBox, Priority.ALWAYS);

        VBox leftPane = createLeftPane();
        VBox rightPane = createRightPane();

        contentBox.getChildren().addAll(leftPane, rightPane);
        mainLayout.setCenter(contentBox);

        // Load data and start periodic sync
        loadQueriesAsync();
        startPolling();

        return mainLayout;
    }

    private static VBox createLeftPane() {
        VBox left = new VBox(10);
        left.setPrefWidth(370);
        left.setMinWidth(330);
        left.setMaxWidth(420);
        left.setStyle(
                "-fx-background-color: #FFFFFF;" +
                "-fx-border-color: #E2EBE5;" +
                "-fx-border-width: 0 1px 0 0;");
        left.setPadding(new Insets(16, 14, 16, 16));

        // Header
        Text title = new Text("💬 User Query Inbox");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 16px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        // Search Bar
        TextField searchField = new TextField();
        searchField.setPromptText("🔍 Search name, role, email or query...");
        searchField.setStyle(
                "-fx-background-color: #F0F2F5;" +
                "-fx-background-radius: 18px;" +
                "-fx-border-color: #E2EBE5;" +
                "-fx-border-radius: 18px;" +
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 12px;" +
                "-fx-padding: 7 14 7 14;");
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            searchQuery = newVal.trim().toLowerCase();
            renderQueryList();
        });

        // Role Filter Bar (Filter by User: Operator / Farmer / Provider)
        Text roleFilterLabel = new Text("Filter by User Role:");
        roleFilterLabel.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: 600; -fx-fill: #4B5563;");

        FlowPane roleFilterBox = new FlowPane(6, 6);
        Button allRoleBtn = createRoleFilterButton("All Roles", "ALL");
        Button opRoleBtn = createRoleFilterButton("🚜 Operator", "OPERATOR");
        Button farmerRoleBtn = createRoleFilterButton("🌾 Farmer", "FARMER");
        Button provRoleBtn = createRoleFilterButton("🏭 Provider", "PROVIDER");
        roleFilterBox.getChildren().addAll(allRoleBtn, opRoleBtn, farmerRoleBtn, provRoleBtn);

        // Status Filter Pills
        Text statusFilterLabel = new Text("Filter by Status:");
        statusFilterLabel.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: 600; -fx-fill: #4B5563;");

        HBox statusFilterBox = new HBox(6);
        Button allStatusBtn = createStatusFilterButton("All Status", "ALL");
        Button openStatusBtn = createStatusFilterButton("Active / Open", "OPEN");
        Button resStatusBtn = createStatusFilterButton("Resolved", "RESOLVED");
        statusFilterBox.getChildren().addAll(allStatusBtn, openStatusBtn, resStatusBtn);

        // Thread List Container inside ScrollPane
        queryListContainer = new VBox(8);
        queryListContainer.setPadding(new Insets(4, 2, 4, 2));

        ScrollPane listScroll = new ScrollPane(queryListContainer);
        listScroll.setFitToWidth(true);
        listScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        listScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        listScroll.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        VBox.setVgrow(listScroll, Priority.ALWAYS);

        left.getChildren().addAll(title, searchField, roleFilterLabel, roleFilterBox, statusFilterLabel, statusFilterBox, listScroll);
        return left;
    }

    private static Button createRoleFilterButton(String label, String roleKey) {
        Button btn = new Button(label);
        boolean isActive = currentRoleFilter.equals(roleKey);
        updateFilterButtonStyle(btn, isActive);

        btn.setOnAction(e -> {
            currentRoleFilter = roleKey;
            FlowPane parent = (FlowPane) btn.getParent();
            for (javafx.scene.Node node : parent.getChildren()) {
                if (node instanceof Button b) {
                    updateFilterButtonStyle(b, b == btn);
                }
            }
            renderQueryList();
        });
        return btn;
    }

    private static Button createStatusFilterButton(String label, String statusKey) {
        Button btn = new Button(label);
        boolean isActive = currentStatusFilter.equals(statusKey);
        updateFilterButtonStyle(btn, isActive);

        btn.setOnAction(e -> {
            currentStatusFilter = statusKey;
            HBox parent = (HBox) btn.getParent();
            for (javafx.scene.Node node : parent.getChildren()) {
                if (node instanceof Button b) {
                    updateFilterButtonStyle(b, b == btn);
                }
            }
            renderQueryList();
        });
        return btn;
    }

    private static void updateFilterButtonStyle(Button btn, boolean active) {
        if (active) {
            btn.setStyle(
                    "-fx-background-color: #1B4332;" +
                    "-fx-text-fill: #FFFFFF;" +
                    "-fx-font-family: 'Poppins';" +
                    "-fx-font-size: 11px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-padding: 4 10;" +
                    "-fx-background-radius: 12px;" +
                    "-fx-cursor: hand;");
        } else {
            btn.setStyle(
                    "-fx-background-color: #F0F2F5;" +
                    "-fx-text-fill: #4B5563;" +
                    "-fx-font-family: 'Poppins';" +
                    "-fx-font-size: 11px;" +
                    "-fx-font-weight: 500;" +
                    "-fx-padding: 4 10;" +
                    "-fx-background-radius: 12px;" +
                    "-fx-cursor: hand;");
        }
    }

    private static VBox createRightPane() {
        chatAreaContainer = new VBox(0);
        HBox.setHgrow(chatAreaContainer, Priority.ALWAYS);
        chatAreaContainer.setStyle("-fx-background-color: #EFEAE2;");

        renderChatAreaPlaceholder();
        return chatAreaContainer;
    }

    private static void renderChatAreaPlaceholder() {
        chatAreaContainer.getChildren().clear();

        VBox placeholder = new VBox(12);
        placeholder.setAlignment(Pos.CENTER);
        placeholder.setPadding(new Insets(40));
        VBox.setVgrow(placeholder, Priority.ALWAYS);

        Text icon = new Text("💬");
        icon.setStyle("-fx-font-size: 50px;");

        Text title = new Text("FarmEquip Admin Support Desk");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 18px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text desc = new Text("Select any user query thread on the left (Operator, Farmer, or Machinery Provider)\nto review messages, reply in real-time, or resolve customer tickets.");
        desc.setTextAlignment(TextAlignment.CENTER);
        desc.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-fill: #6B7280;");

        placeholder.getChildren().addAll(icon, title, desc);
        chatAreaContainer.getChildren().add(placeholder);
    }

    private static void renderActiveChat() {
        chatAreaContainer.getChildren().clear();
        if (selectedQuery == null) {
            renderChatAreaPlaceholder();
            return;
        }

        String role = selectedQuery.getUserRole() != null ? selectedQuery.getUserRole() : "User";
        String roleIcon = getRoleIcon(role);
        String roleColor = getRoleColor(role);

        // WhatsApp Top Conversation Bar
        HBox topBar = new HBox(14);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(12, 20, 12, 20));
        topBar.setStyle(
                "-fx-background-color: #FFFFFF;" +
                "-fx-border-color: #E2EBE5;" +
                "-fx-border-width: 0 0 1px 0;");

        // Avatar
        StackPane avatar = new StackPane();
        avatar.setPrefSize(42, 42);
        avatar.setMinSize(42, 42);
        avatar.setMaxSize(42, 42);
        avatar.setStyle("-fx-background-color: " + roleColor + "; -fx-background-radius: 21px;");
        String initials = getInitials(selectedQuery.getUserName());
        Text initText = new Text(initials);
        initText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 14px; -fx-font-weight: bold; -fx-fill: #FFFFFF;");
        avatar.getChildren().add(initText);

        VBox userInfo = new VBox(2);
        Text nameText = new Text(selectedQuery.getUserName() != null ? selectedQuery.getUserName() : "User");
        nameText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 14px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text roleContactText = new Text(roleIcon + " " + role + " • " + (selectedQuery.getUserEmail() != null ? selectedQuery.getUserEmail() : "") +
                (selectedQuery.getUserPhone() != null && !selectedQuery.getUserPhone().isEmpty() ? " • 📞 " + selectedQuery.getUserPhone() : ""));
        roleContactText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-fill: #4B5563;");

        userInfo.getChildren().addAll(nameText, roleContactText);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        boolean isResolved = "RESOLVED".equalsIgnoreCase(selectedQuery.getStatus());

        // Status Badge
        Text statusBadge = new Text(isResolved ? "⚪ RESOLVED" : "🟢 ACTIVE");
        statusBadge.setStyle(
                "-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-font-weight: bold;" +
                "-fx-fill: " + (isResolved ? "#555555" : "#2E7D32") + ";");

        Button resolveBtn = new Button("✔ Mark as Resolved");
        resolveBtn.setStyle(
                "-fx-background-color: " + (isResolved ? "#9CA3AF" : "#2D6A4F") + ";" +
                "-fx-text-fill: #FFFFFF;" +
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 12px;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 7 14;" +
                "-fx-background-radius: 8px;" +
                "-fx-cursor: hand;");
        resolveBtn.setDisable(isResolved);

        resolveBtn.setOnAction(e -> {
            selectedQuery.setStatus("RESOLVED");
            new Thread(() -> {
                queryDAO.resolveQuery(selectedQuery.getQueryId());
            }).start();
            renderActiveChat();
            renderQueryList();
        });

        topBar.getChildren().addAll(avatar, userInfo, spacer, statusBadge, resolveBtn);

        // Chat Message Stream
        messagesVBox = new VBox(12);
        messagesVBox.setPadding(new Insets(18, 22, 18, 22));
        messagesVBox.setStyle("-fx-background-color: #EFEAE2;");

        chatMessagesScroll = new ScrollPane(messagesVBox);
        chatMessagesScroll.setFitToWidth(true);
        chatMessagesScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        chatMessagesScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        chatMessagesScroll.setStyle("-fx-background-color: #EFEAE2; -fx-background: #EFEAE2;");
        VBox.setVgrow(chatMessagesScroll, Priority.ALWAYS);

        // Feedback Card (if resolved)
        feedbackCard = new VBox(8);
        feedbackCard.setPadding(new Insets(12, 18, 12, 18));
        feedbackCard.setStyle("-fx-background-color: #F8FAF9; -fx-border-color: #E2EBE5; -fx-border-width: 1 0 0 0;");

        if (isResolved) {
            feedbackCard.setVisible(true);
            feedbackCard.setManaged(true);
            if (selectedQuery.isFeedbackGiven()) {
                String stars = "⭐".repeat(Math.max(1, Math.min(5, selectedQuery.getFeedbackRating())));
                Text fTitle = new Text(role + " Satisfaction Feedback: " + stars + " (" + selectedQuery.getFeedbackRating() + "/5)");
                fTitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-font-weight: bold; -fx-fill: #1B4332;");

                Text fNote = new Text(selectedQuery.getFeedbackComment() != null && !selectedQuery.getFeedbackComment().isEmpty()
                        ? "\"" + selectedQuery.getFeedbackComment() + "\""
                        : "No written comments provided.");
                fNote.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #4B5563; -fx-font-style: italic;");

                feedbackCard.getChildren().addAll(fTitle, fNote);
            } else {
                Text awaitText = new Text("Query resolved. Awaiting " + role.toLowerCase() + " feedback & rating...");
                awaitText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #78909C; -fx-font-style: italic;");
                feedbackCard.getChildren().add(awaitText);
            }
        } else {
            feedbackCard.setVisible(false);
            feedbackCard.setManaged(false);
        }

        // WhatsApp Admin Reply Bar
        replyBar = createReplyBar();

        chatAreaContainer.getChildren().addAll(topBar, chatMessagesScroll, feedbackCard, replyBar);

        populateChatBubbles();
    }

    private static void populateChatBubbles() {
        messagesVBox.getChildren().clear();
        if (selectedQuery == null) return;

        String role = selectedQuery.getUserRole() != null ? selectedQuery.getUserRole() : "User";
        String roleIcon = getRoleIcon(role);

        for (SupportMessageModel msg : selectedQuery.getMessages()) {
            boolean isAdmin = "Admin".equalsIgnoreCase(msg.getSenderRole());

            HBox row = new HBox();
            row.setAlignment(isAdmin ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);

            VBox bubble = new VBox(4);
            bubble.setMaxWidth(500);
            bubble.setPadding(new Insets(9, 14, 8, 14));

            if (isAdmin) {
                bubble.setStyle(
                        "-fx-background-color: #D9FDD3;" +
                        "-fx-background-radius: 12px 12px 2px 12px;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.08), 3, 0, 0, 1);");

                Text tag = new Text("🛡️ FarmEquip Admin Desk");
                tag.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 10.5px; -fx-font-weight: bold; -fx-fill: #2D6A4F;");

                Text messageText = new Text(msg.getText());
                messageText.setWrappingWidth(460);
                messageText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-fill: #111B21;");

                HBox meta = new HBox(6);
                meta.setAlignment(Pos.CENTER_RIGHT);

                Text timeText = new Text(msg.getFormattedTime() != null ? msg.getFormattedTime() : "");
                timeText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 10px; -fx-fill: #667781;");

                Text checkmarks = new Text("✔✔");
                checkmarks.setStyle("-fx-font-size: 9.5px; -fx-fill: #53BDEB;");

                meta.getChildren().addAll(timeText, checkmarks);
                bubble.getChildren().addAll(tag, messageText, meta);
            } else {
                bubble.setStyle(
                        "-fx-background-color: #FFFFFF;" +
                        "-fx-background-radius: 12px 12px 12px 2px;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.08), 3, 0, 0, 1);");

                Text senderTag = new Text(roleIcon + " " + (msg.getSenderName() != null ? msg.getSenderName() : role));
                senderTag.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-fill: #1B4332;");

                Text messageText = new Text(msg.getText());
                messageText.setWrappingWidth(460);
                messageText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-fill: #111B21;");

                HBox meta = new HBox(6);
                meta.setAlignment(Pos.CENTER_RIGHT);

                Text timeText = new Text(msg.getFormattedTime() != null ? msg.getFormattedTime() : "");
                timeText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 10px; -fx-fill: #667781;");

                meta.getChildren().add(timeText);
                bubble.getChildren().addAll(senderTag, messageText, meta);
            }

            row.getChildren().add(bubble);
            messagesVBox.getChildren().add(row);
        }

        Platform.runLater(() -> chatMessagesScroll.setVvalue(1.0));
    }

    private static HBox createReplyBar() {
        HBox bar = new HBox(12);
        bar.setAlignment(Pos.CENTER);
        bar.setPadding(new Insets(12, 18, 14, 18));
        bar.setStyle(
                "-fx-background-color: #F0F2F5;" +
                "-fx-border-color: #E2EBE5;" +
                "-fx-border-width: 1 0 0 0;");

        TextField replyInput = new TextField();
        replyInput.setPromptText("Type your reply to the user...");
        replyInput.setStyle(
                "-fx-background-color: #FFFFFF;" +
                "-fx-background-radius: 22px;" +
                "-fx-border-color: #CCD0D5;" +
                "-fx-border-radius: 22px;" +
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 13px;" +
                "-fx-padding: 8 16 8 16;");
        HBox.setHgrow(replyInput, Priority.ALWAYS);

        Button sendBtn = new Button("Send Reply ➤");
        sendBtn.setStyle(
                "-fx-background-color: #2D6A4F;" +
                "-fx-text-fill: #FFFFFF;" +
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 13px;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 8 18 8 16;" +
                "-fx-background-radius: 22px;" +
                "-fx-cursor: hand;" +
                "-fx-effect: dropshadow(gaussian, rgba(45, 106, 79, 0.3), 6, 0, 0, 2);");

        sendBtn.setOnAction(e -> {
            String text = replyInput.getText().trim();
            if (!text.isEmpty()) {
                sendAdminReply(text);
                replyInput.clear();
            }
        });

        replyInput.setOnAction(e -> {
            String text = replyInput.getText().trim();
            if (!text.isEmpty()) {
                sendAdminReply(text);
                replyInput.clear();
            }
        });

        bar.getChildren().addAll(replyInput, sendBtn);
        return bar;
    }

    private static void sendAdminReply(String text) {
        if (selectedQuery == null) return;

        SupportMessageModel msg = new SupportMessageModel(
                "MSG_" + System.currentTimeMillis(),
                "admin@farm.com",
                "FarmEquip Admin",
                "Admin",
                text
        );

        selectedQuery.getMessages().add(msg);
        selectedQuery.setLastMessage(text);
        selectedQuery.setLastUpdated(System.currentTimeMillis());

        populateChatBubbles();
        renderQueryList();

        new Thread(() -> {
            queryDAO.saveQuery(selectedQuery);
        }).start();
    }

    private static void renderQueryList() {
        queryListContainer.getChildren().clear();

        List<SupportQueryModel> filtered = new ArrayList<>();
        for (SupportQueryModel q : allQueries) {
            // Apply Status Filter
            if ("OPEN".equals(currentStatusFilter) && !"OPEN".equalsIgnoreCase(q.getStatus())) continue;
            if ("RESOLVED".equals(currentStatusFilter) && !"RESOLVED".equalsIgnoreCase(q.getStatus())) continue;

            // Apply Role Filter
            if (!"ALL".equals(currentRoleFilter)) {
                String role = q.getUserRole() != null ? q.getUserRole().trim().toUpperCase() : "OPERATOR";
                if (!role.equalsIgnoreCase(currentRoleFilter)) continue;
            }

            // Apply Search Query
            if (!searchQuery.isEmpty()) {
                String name = q.getUserName() != null ? q.getUserName().toLowerCase() : "";
                String email = q.getUserEmail() != null ? q.getUserEmail().toLowerCase() : "";
                String role = q.getUserRole() != null ? q.getUserRole().toLowerCase() : "";
                String msg = q.getLastMessage() != null ? q.getLastMessage().toLowerCase() : "";
                if (!name.contains(searchQuery) && !email.contains(searchQuery) && !role.contains(searchQuery) && !msg.contains(searchQuery)) {
                    continue;
                }
            }
            filtered.add(q);
        }

        if (filtered.isEmpty()) {
            VBox empty = new VBox(6);
            empty.setAlignment(Pos.CENTER);
            empty.setPadding(new Insets(30, 10, 30, 10));
            Text emptyText = new Text("No matching queries found");
            emptyText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-fill: #9CA3AF;");
            empty.getChildren().add(emptyText);
            queryListContainer.getChildren().add(empty);
            return;
        }

        for (SupportQueryModel q : filtered) {
            boolean isSelected = selectedQuery != null && selectedQuery.getQueryId().equals(q.getQueryId());

            String qRole = q.getUserRole() != null ? q.getUserRole() : "User";
            String qRoleIcon = getRoleIcon(qRole);
            String qRoleColor = getRoleColor(qRole);

            HBox item = new HBox(10);
            item.setAlignment(Pos.CENTER_LEFT);
            item.setPadding(new Insets(10, 12, 10, 12));
            item.setStyle(
                    "-fx-background-color: " + (isSelected ? "#E8F5E9" : "#FFFFFF") + ";" +
                    "-fx-background-radius: 10px;" +
                    "-fx-border-color: " + (isSelected ? "#2D6A4F" : "#E2EBE5") + ";" +
                    "-fx-border-radius: 10px;" +
                    "-fx-border-width: 1px;" +
                    "-fx-cursor: hand;");

            // Avatar
            StackPane av = new StackPane();
            av.setPrefSize(36, 36);
            av.setMinSize(36, 36);
            av.setMaxSize(36, 36);
            av.setStyle("-fx-background-color: " + qRoleColor + "; -fx-background-radius: 18px;");
            Text avTxt = new Text(getInitials(q.getUserName()));
            avTxt.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-fill: #FFFFFF;");
            av.getChildren().add(avTxt);

            VBox info = new VBox(2);
            HBox.setHgrow(info, Priority.ALWAYS);

            HBox nameRow = new HBox(6);
            nameRow.setAlignment(Pos.CENTER_LEFT);

            Text name = new Text(q.getUserName() != null ? q.getUserName() : "User");
            name.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-font-weight: bold; -fx-fill: #1B4332;");

            Text roleTag = new Text(qRoleIcon + " " + qRole);
            roleTag.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 10px; -fx-fill: " + qRoleColor + "; -fx-font-weight: 600;");

            Region sp = new Region();
            HBox.setHgrow(sp, Priority.ALWAYS);

            boolean isRes = "RESOLVED".equalsIgnoreCase(q.getStatus());
            Text statusPill = new Text(isRes ? "RESOLVED" : "OPEN");
            statusPill.setStyle(
                    "-fx-font-family: 'Poppins'; -fx-font-size: 10px; -fx-font-weight: bold;" +
                    "-fx-fill: " + (isRes ? "#78909C" : "#2E7D32") + ";");

            nameRow.getChildren().addAll(name, roleTag, sp, statusPill);

            String preview = q.getLastMessage() != null && !q.getLastMessage().isEmpty() ? q.getLastMessage() : "No messages";
            if (preview.length() > 38) {
                preview = preview.substring(0, 35) + "...";
            }
            Text msgPreview = new Text(preview);
            msgPreview.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-fill: #6B7280;");

            info.getChildren().addAll(nameRow, msgPreview);
            item.getChildren().addAll(av, info);

            item.setOnMouseClicked(e -> {
                selectedQuery = q;
                renderQueryList();
                renderActiveChat();
            });

            queryListContainer.getChildren().add(item);
        }
    }

    private static String getRoleIcon(String role) {
        if (role == null) return "👤";
        String r = role.trim().toUpperCase();
        if (r.contains("FARMER")) return "🌾";
        if (r.contains("PROVIDER")) return "🏭";
        return "🚜";
    }

    private static String getRoleColor(String role) {
        if (role == null) return "#2D6A4F";
        String r = role.trim().toUpperCase();
        if (r.contains("FARMER")) return "#15803D";
        if (r.contains("PROVIDER")) return "#1D4ED8";
        return "#2D6A4F";
    }

    private static String getInitials(String name) {
        if (name == null || name.trim().isEmpty()) return "US";
        String[] parts = name.trim().split("\\s+");
        if (parts.length == 1) {
            return parts[0].substring(0, Math.min(2, parts[0].length())).toUpperCase();
        }
        return ("" + parts[0].charAt(0) + parts[1].charAt(0)).toUpperCase();
    }

    private static void loadQueriesAsync() {
        new Thread(() -> {
            List<SupportQueryModel> list = queryDAO.getAllQueries();
            Platform.runLater(() -> {
                allQueries = list;
                if (selectedQuery == null && !allQueries.isEmpty()) {
                    selectedQuery = allQueries.get(0);
                    renderActiveChat();
                }
                renderQueryList();
            });
        }).start();
    }

    private static void startPolling() {
        if (refreshTimeline != null) {
            refreshTimeline.stop();
        }
        refreshTimeline = new Timeline(new KeyFrame(Duration.seconds(3), e -> {
            new Thread(() -> {
                List<SupportQueryModel> list = queryDAO.getAllQueries();
                Platform.runLater(() -> {
                    allQueries = list;
                    if (selectedQuery != null) {
                        for (SupportQueryModel q : allQueries) {
                            if (q.getQueryId().equals(selectedQuery.getQueryId())) {
                                if (q.getMessages().size() != selectedQuery.getMessages().size() ||
                                    !q.getStatus().equals(selectedQuery.getStatus()) ||
                                    q.isFeedbackGiven() != selectedQuery.isFeedbackGiven()) {
                                    selectedQuery = q;
                                    renderActiveChat();
                                }
                                break;
                            }
                        }
                    }
                    renderQueryList();
                });
            }).start();
        }));
        refreshTimeline.setCycleCount(Timeline.INDEFINITE);
        refreshTimeline.play();
    }
}
