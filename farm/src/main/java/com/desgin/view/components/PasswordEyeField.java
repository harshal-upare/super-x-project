package com.desgin.view.components;

import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;

/**
 * Reusable Password input component with an interactive Eye icon toggle 
 * to show or hide the entered password.
 */
public class PasswordEyeField extends StackPane {

    private static final String EYE_OPEN_PATH = "M12 4.5C7 4.5 2.73 7.61 1 12c1.73 4.39 6 7.5 11 7.5s9.27-3.11 11-7.5c-1.73-4.39-6-7.5-11-7.5zm0 12.5c-2.76 0-5-2.24-5-5s2.24-5 5-5 5 2.24 5 5-2.24 5-5 5zm0-8c-1.66 0-3 1.34-3 3s1.34 3 3 3 3-1.34 3-3-1.34-3-3-3z";
    private static final String EYE_CLOSED_PATH = "M12 7c2.76 0 5 2.24 5 5 0 .65-.13 1.26-.36 1.83l2.92 2.92c1.51-1.26 2.7-2.89 3.44-4.75-1.73-4.39-6-7.5-11-7.5-1.4 0-2.74.25-3.98.7l2.16 2.16C10.74 7.13 11.35 7 12 7zM2 4.27l2.28 2.28.46.46C3.08 8.3 1.78 10.02 1 12c1.73 4.39 6 7.5 11 7.5 1.55 0 3.03-.3 4.38-.84l.42.42L19.73 22 21 20.73 3.27 3 2 4.27zM7.53 9.8l1.55 1.55c-.05.21-.08.43-.08.65 0 1.66 1.34 3 3 3 .22 0 .44-.03.65-.08l1.55 1.55c-.67.33-1.41.53-2.2.53-2.76 0-5-2.24-5-5 0-.79.2-1.53.53-2.2zm4.31-.78l3.15 3.15.02-.17c0-1.66-1.34-3-3-3l-.17.02z";

    private static final String DEFAULT_ICON_COLOR = "#7A8B7B";
    private static final String ACTIVE_ICON_COLOR = "#388E3C";
    private static final String HOVER_ICON_COLOR = "#255D28";

    private final PasswordField hiddenField;
    private final TextField shownField;
    private final Button toggleButton;
    private final SVGPath eyeIcon;
    private final Tooltip tooltip;

    private boolean isPasswordShown = false;

    public PasswordEyeField() {
        this("");
    }

    public PasswordEyeField(String promptText) {
        // Masked Password Field
        hiddenField = new PasswordField();
        hiddenField.setPromptText(promptText);
        hiddenField.setFocusTraversable(false);

        // Visible Plain Text Field
        shownField = new TextField();
        shownField.setPromptText(promptText);
        shownField.setFocusTraversable(false);
        shownField.setVisible(false);
        shownField.setManaged(false);

        // Synchronize text bidirectionally
        shownField.textProperty().bindBidirectional(hiddenField.textProperty());

        // Default Field Styling
        String fieldStyle = "-fx-background-color: #F8FAF7; -fx-border-color : #D6DDD2; -fx-border-radius: 8px; -fx-background-radius: 8px; -fx-padding: 0 36px 0 12px; -fx-font-size: 13px;";
        hiddenField.setStyle(fieldStyle);
        shownField.setStyle(fieldStyle);

        // Eye Vector Icon
        eyeIcon = new SVGPath();
        eyeIcon.setContent(EYE_OPEN_PATH);
        eyeIcon.setFill(Color.web(DEFAULT_ICON_COLOR));
        eyeIcon.setScaleX(0.72);
        eyeIcon.setScaleY(0.72);

        // Toggle Button
        toggleButton = new Button();
        toggleButton.setGraphic(eyeIcon);
        toggleButton.setFocusTraversable(false);
        toggleButton.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-padding: 0; -fx-border-color: transparent;");
        toggleButton.setPrefSize(30, 30);
        toggleButton.setMinSize(30, 30);
        toggleButton.setMaxSize(30, 30);

        tooltip = new Tooltip("Show Password");
        Tooltip.install(toggleButton, tooltip);

        // Hover Effect on Icon
        toggleButton.setOnMouseEntered(e -> {
            if (isPasswordShown) {
                eyeIcon.setFill(Color.web(HOVER_ICON_COLOR));
            } else {
                eyeIcon.setFill(Color.web(ACTIVE_ICON_COLOR));
            }
        });

        toggleButton.setOnMouseExited(e -> {
            if (isPasswordShown) {
                eyeIcon.setFill(Color.web(ACTIVE_ICON_COLOR));
            } else {
                eyeIcon.setFill(Color.web(DEFAULT_ICON_COLOR));
            }
        });

        // Click Event to Toggle
        toggleButton.setOnAction(e -> togglePasswordVisibility());

        // Layout inside StackPane
        StackPane.setAlignment(toggleButton, Pos.CENTER_RIGHT);
        StackPane.setMargin(toggleButton, new Insets(0, 8, 0, 0));

        // Keep focus traversability in sync
        this.setFocusTraversable(false);
        this.focusTraversableProperty().addListener((obs, oldVal, newVal) -> {
            hiddenField.setFocusTraversable(newVal);
            shownField.setFocusTraversable(newVal);
        });

        this.getChildren().addAll(hiddenField, shownField, toggleButton);
        this.setAlignment(Pos.CENTER_LEFT);
    }

    private void togglePasswordVisibility() {
        boolean hadFocus = hiddenField.isFocused() || shownField.isFocused();
        isPasswordShown = !isPasswordShown;

        if (isPasswordShown) {
            eyeIcon.setContent(EYE_CLOSED_PATH);
            eyeIcon.setFill(Color.web(ACTIVE_ICON_COLOR));
            tooltip.setText("Hide Password");

            hiddenField.setVisible(false);
            hiddenField.setManaged(false);

            shownField.setVisible(true);
            shownField.setManaged(true);

            if (hadFocus) {
                shownField.requestFocus();
            }
            int length = shownField.getText() != null ? shownField.getText().length() : 0;
            shownField.positionCaret(length);
        } else {
            eyeIcon.setContent(EYE_OPEN_PATH);
            eyeIcon.setFill(Color.web(DEFAULT_ICON_COLOR));
            tooltip.setText("Show Password");

            shownField.setVisible(false);
            shownField.setManaged(false);

            hiddenField.setVisible(true);
            hiddenField.setManaged(true);

            if (hadFocus) {
                hiddenField.requestFocus();
            }
            int length = hiddenField.getText() != null ? hiddenField.getText().length() : 0;
            hiddenField.positionCaret(length);
        }
    }

    public String getText() {
        return hiddenField.getText();
    }

    public void setText(String text) {
        hiddenField.setText(text);
    }

    public void clear() {
        hiddenField.setText("");
    }

    public StringProperty textProperty() {
        return hiddenField.textProperty();
    }

    public void setPromptText(String prompt) {
        hiddenField.setPromptText(prompt);
        shownField.setPromptText(prompt);
    }

    public void setCustomPrefSize(double width, double height) {
        this.setPrefSize(width, height);
        this.setMaxSize(width, height);
        this.setMinSize(width, height);

        hiddenField.setPrefSize(width, height);
        hiddenField.setMaxSize(width, height);
        hiddenField.setMinSize(width, height);

        shownField.setPrefSize(width, height);
        shownField.setMaxSize(width, height);
        shownField.setMinSize(width, height);
    }

    public PasswordField getHiddenField() {
        return hiddenField;
    }

    public TextField getShownField() {
        return shownField;
    }

    public Button getToggleButton() {
        return toggleButton;
    }

    public boolean isPasswordShown() {
        return isPasswordShown;
    }
}
