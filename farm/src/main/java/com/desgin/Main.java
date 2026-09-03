package com.desgin;

import com.desgin.view.handling_start.WelcomePage;

import javafx.application.Application;

public class Main {
    public static void main(String[] args) throws Exception{
        System.out.print(args);
    
        Class.forName("com.desgin.config.FirestoreConfig");
        
        Application.launch(WelcomePage.class,args);
    } 

}