package com.desgin.view.provider;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class ProviderDashboard {

    private Scene providerDashboardScene;
    public static BorderPane borderPane;
    public static StackPane root;

    public Scene getProviderDashboardScene(Runnable ref) {
        root = new StackPane();

        // Left Sidebar
        ProviderLeftSideBar objLeftSideBar = new ProviderLeftSideBar(this);
        VBox leftVB = objLeftSideBar.getSideBar(ref);

        borderPane = new BorderPane();
        borderPane.setPadding(Insets.EMPTY);

        BorderPane subroot = new BorderPane();
        subroot.setLeft(leftVB);
        subroot.setCenter(borderPane);

        root.getChildren().addAll(subroot);

        // Top Profile Management
        ProviderProfileManagement objProfileManagement = new ProviderProfileManagement();
        borderPane.setTop(objProfileManagement.getProfile(root));

        // Initial Home Page
        Dashboard.getPage();

        root.setStyle("-fx-background-color: #EDE3D5;");

        subroot.prefWidthProperty().bind(root.widthProperty());
        subroot.prefHeightProperty().bind(root.heightProperty());
        providerDashboardScene = new Scene(root);

        return providerDashboardScene;
    }
}
