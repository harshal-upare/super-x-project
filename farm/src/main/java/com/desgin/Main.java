package com.desgin;

import com.desgin.view.WelcomePage;

import javafx.application.Application;

public class Main {
    public static void main(String[] args) {
        System.out.print(args);

        Application.launch(WelcomePage.class,args);
    }
}