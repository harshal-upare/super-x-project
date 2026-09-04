package com.desgin.view.handling_start;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class AboutUsPage {

    public Scene getAboutUsScene() {
        // --- 1. Top Sticky Navigation Bar ---
        HBox topNavBar = createTopNavBar();

        // --- 2. Hero Section: About FarmEquip ---
        VBox heroSection = createHeroSection();

        // --- 3. Core2Web Institute Section ---
        VBox instituteSection = createInstituteSection();

        // --- 4. Master – Shashi Sir ---
        VBox masterSection = createMasterSection();

        // --- 5. Our Instructors ---
        VBox instructorsSection = createInstructorsSection();

        // --- 6. Super Mentor & Team Lead ---
        VBox superMentorSection = createSuperMentorSection();

        // --- 7. Meet Our Team (Group Members) ---
        VBox teamSection = createTeamSection();

        // --- 8. Professional Project Presentation Footer ---
        VBox footerSection = createFooterSection();

        VBox pageContent = new VBox(32,
                heroSection,
                instituteSection,
                masterSection,
                instructorsSection,
                superMentorSection,
                teamSection,
                footerSection
        );
        pageContent.setPadding(new Insets(24, 40, 48, 40));
        pageContent.setMaxWidth(1160);
        pageContent.setAlignment(Pos.TOP_CENTER);

        HBox centerWrapper = new HBox(pageContent);
        centerWrapper.setAlignment(Pos.TOP_CENTER);
        centerWrapper.setStyle("-fx-background-color: #F8FAF8;");

        VBox mainContainer = new VBox(topNavBar, centerWrapper);
        mainContainer.setStyle("-fx-background-color: #F8FAF8;");

        ScrollPane scrollPane = new ScrollPane(mainContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: #F8FAF8; -fx-background: #F8FAF8;");

        return new Scene(scrollPane, 1280, 800);
    }

    // ============================================================
    // NAVIGATION BAR
    // ============================================================
    private HBox createTopNavBar() {
        Button backBtn = new Button("← Back to Home");
        backBtn.setStyle(
                "-fx-background-color: #FFFFFF;" +
                "-fx-text-fill: #16723A;" +
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 13px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 20px;" +
                "-fx-border-color: #2D6A4F;" +
                "-fx-border-radius: 20px;" +
                "-fx-border-width: 1.4px;" +
                "-fx-padding: 7 18;" +
                "-fx-cursor: hand;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.06), 6, 0, 0, 2);");

        backBtn.setOnMouseEntered(e -> backBtn.setStyle(
                "-fx-background-color: #16723A;" +
                "-fx-text-fill: #FFFFFF;" +
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 13px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 20px;" +
                "-fx-border-color: #16723A;" +
                "-fx-border-radius: 20px;" +
                "-fx-border-width: 1.4px;" +
                "-fx-padding: 7 18;" +
                "-fx-cursor: hand;"));

        backBtn.setOnMouseExited(e -> backBtn.setStyle(
                "-fx-background-color: #FFFFFF;" +
                "-fx-text-fill: #16723A;" +
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 13px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 20px;" +
                "-fx-border-color: #2D6A4F;" +
                "-fx-border-radius: 20px;" +
                "-fx-border-width: 1.4px;" +
                "-fx-padding: 7 18;" +
                "-fx-cursor: hand;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.06), 6, 0, 0, 2);"));

        backBtn.setOnAction(e -> WelcomePage.navigateToWelcomePage());

        // FarmEquip Brand Logo + Text
        Image logoImg = resolveImage("/assets/Images/logo.png");
        if (logoImg == null || logoImg.isError()) {
            logoImg = resolveImage("/assets/Images/logo.jpeg");
        }

        Node logoNode = null;
        if (logoImg != null && !logoImg.isError()) {
            ImageView logoIv = new ImageView(logoImg);
            logoIv.setFitWidth(32);
            logoIv.setFitHeight(32);
            logoIv.setPreserveRatio(true);
            logoIv.setSmooth(true);

            Rectangle logoClip = new Rectangle(32, 32);
            logoClip.setArcWidth(8);
            logoClip.setArcHeight(8);
            logoIv.setClip(logoClip);

            StackPane logoContainer = new StackPane(logoIv);
            logoContainer.setPrefSize(38, 38);
            logoContainer.setMinSize(38, 38);
            logoContainer.setMaxSize(38, 38);
            logoContainer.setAlignment(Pos.CENTER);
            logoContainer.setStyle(
                    "-fx-background-color: #FFFFFF;" +
                    "-fx-background-radius: 10px;" +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.18), 6, 0, 0, 1);");
            logoNode = logoContainer;
        }

        Text brandFarm = new Text("Farm");
        brandFarm.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 24px; -fx-font-weight: bold; -fx-fill: #FFFFFF;");
        Text brandEquip = new Text("Equip");
        brandEquip.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 24px; -fx-font-weight: bold; -fx-fill: #A5D65E;");
        HBox brandText = new HBox(0, brandFarm, brandEquip);
        brandText.setAlignment(Pos.CENTER_LEFT);

        HBox brandBox = (logoNode != null) ? new HBox(10, logoNode, brandText) : new HBox(0, brandText);
        brandBox.setAlignment(Pos.CENTER);

        Label presentationBadge = new Label("🎓 Academic Project Presentation • 2026");
        presentationBadge.setStyle(
                "-fx-background-color: rgba(255,255,255,0.18);" +
                "-fx-text-fill: #E8F5E9;" +
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 11.5px;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 6 14;" +
                "-fx-background-radius: 16px;");

        Region sp1 = new Region();
        HBox.setHgrow(sp1, Priority.ALWAYS);
        Region sp2 = new Region();
        HBox.setHgrow(sp2, Priority.ALWAYS);

        HBox topNav = new HBox(16, backBtn, sp1, brandBox, sp2, presentationBadge);
        topNav.setAlignment(Pos.CENTER_LEFT);
        topNav.setPadding(new Insets(12, 36, 12, 36));
        topNav.setStyle("-fx-background-color: #16723A; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 10, 0, 0, 3);");
        return topNav;
    }

    // ============================================================
    // 1. HERO SECTION & ABOUT FARMEQUIP
    // ============================================================
    private VBox createHeroSection() {
        Label eyebrow = new Label("🌱 EMPOWERING FARMERS WITH EASY ACCESS TO AGRICULTURAL EQUIPMENT");
        eyebrow.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-font-weight: bold; -fx-text-fill: #2D6A4F; -fx-background-color: #DCFCE7; -fx-padding: 4 12; -fx-background-radius: 12;");

        Text title = new Text("About FarmEquip");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 36px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text quote = new Text("“Technology that connects farmers with the equipment they need.”");
        quote.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 18px; -fx-font-weight: 600; -fx-fill: #15803D;");

        Text desc = new Text(
                "FarmEquip is an agriculture equipment rental platform designed to help farmers easily find and rent agricultural machinery and equipment. " +
                "The platform connects farmers with equipment providers, making agricultural equipment more accessible, affordable, and convenient. " +
                "Equipped with live shift tracking, verified professional operator hiring, and transparent digital escrow settlements, FarmEquip transforms rural farm mechanization into an efficient digital ecosystem."
        );
        desc.setWrappingWidth(920);
        desc.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 14.5px; -fx-fill: #4B5563; -fx-line-spacing: 5px;");

        // 3 Key Pillars
        HBox p1 = createFeaturePill("🚜  Machinery On-Demand", "Tractors, Harvesters, Tillers & Sprayers");
        HBox p2 = createFeaturePill("🔒  Escrow Security", "Transparent pricing & protected wages");
        HBox p3 = createFeaturePill("👨‍🌾  Certified Operators", "Verified field drivers with duty status");

        HBox pillarsBox = new HBox(16, p1, p2, p3);
        pillarsBox.setAlignment(Pos.CENTER_LEFT);

        VBox heroCard = new VBox(16, eyebrow, title, quote, desc, pillarsBox);
        heroCard.setPadding(new Insets(32, 36, 32, 36));
        heroCard.setMaxWidth(Double.MAX_VALUE);
        heroCard.setStyle(
                "-fx-background-color: linear-gradient(to bottom right, #FFFFFF, #F1F8F3);" +
                "-fx-background-radius: 18px;" +
                "-fx-border-color: #D1E7D8;" +
                "-fx-border-width: 1.5px;" +
                "-fx-border-radius: 18px;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.04), 10, 0, 0, 2);");

        applyHoverScale(heroCard);
        return heroCard;
    }

    private HBox createFeaturePill(String header, String subtitle) {
        Text h = new Text(header);
        h.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13.5px; -fx-font-weight: bold; -fx-fill: #1B4332;");
        Text s = new Text(subtitle);
        s.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-fill: #6B7280;");
        VBox box = new VBox(2, h, s);

        HBox pill = new HBox(box);
        pill.setPadding(new Insets(10, 16, 10, 16));
        pill.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 12px; -fx-border-color: #E2EBE5; -fx-border-width: 1px; -fx-border-radius: 12px;");
        HBox.setHgrow(pill, Priority.ALWAYS);
        return pill;
    }

    // ============================================================
    // 2. INSTITUTE – CORE2WEB
    // ============================================================
    private VBox createInstituteSection() {
        Label badge = new Label("🏛️ OUR ACADEMIC & TECHNICAL FOUNDATION");
        badge.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: #1E3A8A; -fx-background-color: #DBEAFE; -fx-padding: 3 10; -fx-background-radius: 10;");

        Text title = new Text("Institute – Core2Web Technologies");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 26px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        // Core2Web Official Logo & Info
        Node emblem = createInstituteLogoNode("/assets/Images/about/core2web_logo.jpg");

        Text instituteHeading = new Text("Core2Web Technologies, Pune");
        instituteHeading.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 18px; -fx-font-weight: bold; -fx-fill: #111827;");

        Text instituteTag = new Text("Pioneering Core Engineering Discipline & Enterprise Software Innovation");
        instituteTag.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-font-weight: 600; -fx-fill: #2563EB;");

        Text instituteDesc = new Text(
                "Core2Web Technologies is a renowned software mentorship institute based in Pune, founded on the principle of imparting deep technical mastery from fundamental computer architecture to enterprise application engineering. " +
                "Under Core2Web's high-rigor curriculum, students master Core Java, Operating System internals, multithreading, and advanced frameworks. " +
                "The FarmEquip platform was built as a capstone enterprise project, reflecting Core2Web's focus on building production-grade solutions that solve real Indian societal problems."
        );
        instituteDesc.setWrappingWidth(780);
        instituteDesc.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13.5px; -fx-fill: #4B5563; -fx-line-spacing: 4px;");

        VBox infoBox = new VBox(6, instituteHeading, instituteTag, instituteDesc);
        HBox.setHgrow(infoBox, Priority.ALWAYS);

        HBox topRow = new HBox(22, emblem, infoBox);
        topRow.setAlignment(Pos.CENTER_LEFT);

        VBox card = new VBox(14, badge, topRow);
        card.setPadding(new Insets(26, 32, 26, 32));
        card.setMaxWidth(Double.MAX_VALUE);
        card.setStyle(
                "-fx-background-color: #FFFFFF;" +
                "-fx-background-radius: 16px;" +
                "-fx-border-color: #E2E8F0;" +
                "-fx-border-width: 1.4px;" +
                "-fx-border-radius: 16px;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.03), 8, 0, 0, 2);");

        applyHoverScale(card);
        return card;
    }

    // ============================================================
    // 3. MASTER – SHASHI SIR
    // ============================================================
    private VBox createMasterSection() {
        Label badge = new Label("👑 MASTER & CHIEF MENTOR");
        badge.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: #B45309; -fx-background-color: #FEF3C7; -fx-padding: 3 10; -fx-background-radius: 10;");

        Text sectionTitle = new Text("Master – Shashi Sir");
        sectionTitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 26px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        // Profile Photo: User Uploaded 1st Image (Shashi Sir)
        Node photoNode = createCircularProfile(
                "/assets/Images/about/shashi_sir.png",
                140,
                "SB",
                "#1B4332",
                "#2D6A4F"
        );

        Text name = new Text("Shashi Bagal Sir");
        name.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 22px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Label designation = new Label("Founder & Master Mentor • Core2Web Technologies");
        designation.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #15803D; -fx-background-color: #ECFDF5; -fx-padding: 3 10; -fx-background-radius: 8;");

        Text quote = new Text("“True engineering begins when you connect computer fundamentals with real human impact.”");
        quote.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13.5px; -fx-font-style: italic; -fx-font-weight: 600; -fx-fill: #065F46;");

        Text bio = new Text(
                "Shashi Sir is the visionary educator and driving force behind Core2Web Technologies. With unyielding dedication to teaching and architectural purity, " +
                "he has personally inspired and guided over 50,000+ students across Maharashtra and India into high-impact software careers. " +
                "His philosophy of holistic code discipline, relentless practice, and practical problem-solving has laid the architectural foundation for FarmEquip."
        );
        bio.setWrappingWidth(740);
        bio.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13.5px; -fx-fill: #4B5563; -fx-line-spacing: 4px;");

        VBox detailsBox = new VBox(8, name, designation, quote, bio);
        HBox.setHgrow(detailsBox, Priority.ALWAYS);

        HBox contentBox = new HBox(28, photoNode, detailsBox);
        contentBox.setAlignment(Pos.CENTER_LEFT);

        VBox card = new VBox(14, badge, contentBox);
        card.setPadding(new Insets(28, 34, 28, 34));
        card.setMaxWidth(Double.MAX_VALUE);
        card.setStyle(
                "-fx-background-color: linear-gradient(to right, #FFFFFF, #FAFDF9);" +
                "-fx-background-radius: 18px;" +
                "-fx-border-color: #C2E0CE;" +
                "-fx-border-width: 1.8px;" +
                "-fx-border-radius: 18px;" +
                "-fx-effect: dropshadow(gaussian, rgba(27,67,50,0.08), 12, 0, 0, 3);");

        applyHoverScale(card);
        return card;
    }

    // ============================================================
    // 4. OUR INSTRUCTORS
    // ============================================================
    private VBox createInstructorsSection() {
        Label badge = new Label("👨‍🏫 TECHNICAL INSTRUCTION & SPRINT GUIDANCE");
        badge.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: #4338CA; -fx-background-color: #EEF2FF; -fx-padding: 3 10; -fx-background-radius: 10;");

        Text title = new Text("Our Instructors");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 26px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text sub = new Text("Expert educators driving foundational concepts, debugging excellence, and engineering methodology");
        sub.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-fill: #6B7280;");

        VBox header = new VBox(4, badge, title, sub);

        // 3 Cards
        VBox card1 = createInstructorCard(
                "Sachin Sir",
                "Instructor & Tech Lead",
                "Specializing in Java language internals, OOP architecture, and data structures. Guided module decomposition and robust data modeling.",
                "SS",
                "#1E40AF"
        );

        VBox card2 = createInstructorCard(
                "Pramod Sir",
                "Instructor & Tech Lead",
                "Expert in database concurrency, transaction pipelines, and clean API abstractions. Mentored database persistence and async operations.",
                "PS",
                "#065F46"
        );

        VBox card3 = createInstructorCard(
                "Akshay Sir",
                "Instructor & Tech Lead",
                "Expert in software design patterns, UI architecture, and testing methodologies. Mentored workflow integration and presentation readiness.",
                "AS",
                "#9D174D"
        );

        HBox grid = new HBox(16, card1, card2, card3);
        grid.setAlignment(Pos.CENTER);
        HBox.setHgrow(card1, Priority.ALWAYS);
        HBox.setHgrow(card2, Priority.ALWAYS);
        HBox.setHgrow(card3, Priority.ALWAYS);

        VBox section = new VBox(16, header, grid);
        return section;
    }

    private VBox createInstructorCard(String name, String designation, String desc, String initials, String accentColor) {
        Node avatar = createCircularProfile("/assets/Images/about/" + name.toLowerCase().replace(" ", "_") + ".jpg", 80, initials, accentColor, accentColor);

        Text n = new Text(name);
        n.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 16px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Label d = new Label(designation);
        d.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: #15803D; -fx-background-color: #ECFDF5; -fx-padding: 2 8; -fx-background-radius: 6;");

        Text bio = new Text(desc);
        bio.setWrappingWidth(260);
        bio.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #4B5563; -fx-line-spacing: 3px; -fx-text-alignment: center;");

        VBox card = new VBox(10, avatar, n, d, bio);
        card.setAlignment(Pos.TOP_CENTER);
        card.setPadding(new Insets(20, 16, 20, 16));
        card.setStyle(
                "-fx-background-color: #FFFFFF;" +
                "-fx-background-radius: 14px;" +
                "-fx-border-color: #E2EBE5;" +
                "-fx-border-width: 1.2px;" +
                "-fx-border-radius: 14px;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.03), 8, 0, 0, 2);");

        applyHoverScale(card);
        return card;
    }

    // ============================================================
    // 5. SUPER MENTOR & TEAM LEAD (HIGHLIGHTED / PROMINENT)
    // ============================================================
    private VBox createSuperMentorSection() {
        Label badge = new Label("⭐ PROJECT ARCHITECTURE & LEADERSHIP REVIEW");
        badge.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: #B45309; -fx-background-color: #FEF3C7; -fx-padding: 3 12; -fx-background-radius: 10;");

        Text title = new Text("Super Mentor & Team Lead");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 26px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Node avatar = createCircularProfile(
                "/assets/Images/about/super_mentor.jpg",
                96,
                "SM",
                "#065F46",
                "#10B981"
        );

        Text roleTitle = new Text("Dedicated Project Leadership & Technical Guidance");
        roleTitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 18px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Label tag = new Label("Super Mentor & Team Lead • FarmEquip Project");
        tag.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-font-weight: bold; -fx-text-fill: #15803D; -fx-background-color: #DCFCE7; -fx-padding: 3 10; -fx-background-radius: 6;");

        Text description = new Text(
                "Providing continuous end-to-end technical leadership, milestone oversight, code hygiene reviews, and sprint coordination throughout the development of FarmEquip. " +
                "Guided the engineering group across role-based authentication, Google Firestore state synchronization, multi-party agricultural workflows, and final presentation readiness."
        );
        description.setWrappingWidth(740);
        description.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13.5px; -fx-fill: #374151; -fx-line-spacing: 4px;");

        VBox rightBox = new VBox(6, roleTitle, tag, description);
        HBox.setHgrow(rightBox, Priority.ALWAYS);

        HBox row = new HBox(24, avatar, rightBox);
        row.setAlignment(Pos.CENTER_LEFT);

        VBox card = new VBox(14, badge, row);
        card.setPadding(new Insets(26, 32, 26, 32));
        card.setMaxWidth(Double.MAX_VALUE);
        card.setStyle(
                "-fx-background-color: linear-gradient(to right, #F0FDF4, #ECFDF5);" +
                "-fx-background-radius: 18px;" +
                "-fx-border-color: #10B981;" +
                "-fx-border-width: 2px;" +
                "-fx-border-radius: 18px;" +
                "-fx-effect: dropshadow(gaussian, rgba(16,185,129,0.12), 12, 0, 0, 3);");

        applyHoverScale(card);
        return card;
    }

    // ============================================================
    // 6. MEET OUR TEAM (GROUP MEMBERS)
    // ============================================================
    private VBox createTeamSection() {
        Label badge = new Label("👥 THE DEVELOPERS");
        badge.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: #065F46; -fx-background-color: #D1FAE5; -fx-padding: 3 10; -fx-background-radius: 10;");

        Text title = new Text("Meet Our Team");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 26px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text sub = new Text("The 5 Core2Web student developers behind the FarmEquip Agriculture Rental Platform");
        sub.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-fill: #6B7280;");

        VBox header = new VBox(4, badge, title, sub);

        // 5 Team Member Cards with respective uploaded photos
        VBox c1 = createMemberCard("Harshal Upare", "/assets/Images/about/harshal_upare.jpg", "Team Member / Developer", "HU", "#15803D");
        VBox c2 = createMemberCard("Pratik Karhale", "/assets/Images/about/pratik_karhale.jpg", "Team Member / Developer", "PK", "#047857");
        VBox c3 = createMemberCard("Swapnil Jadhav", "/assets/Images/about/swapnil_jadhav.png", "Team Member / Developer", "SJ", "#2D6A4F");
        VBox c4 = createMemberCard("Om Dalvi", "/assets/Images/about/om_dalvi.jpg", "Team Member / Developer", "OD", "#1B4332");
        VBox c5 = createMemberCard("Ashutosh Thamke", "/assets/Images/about/ashutosh_thamke.jpg", "Team Member / Developer", "AT", "#065F46");

        HBox teamRow = new HBox(14, c1, c2, c3, c4, c5);
        teamRow.setAlignment(Pos.CENTER);
        HBox.setHgrow(c1, Priority.ALWAYS);
        HBox.setHgrow(c2, Priority.ALWAYS);
        HBox.setHgrow(c3, Priority.ALWAYS);
        HBox.setHgrow(c4, Priority.ALWAYS);
        HBox.setHgrow(c5, Priority.ALWAYS);

        VBox section = new VBox(16, header, teamRow);
        return section;
    }

    private VBox createMemberCard(String name, String imagePath, String role, String initials, String accentColor) {
        Node avatar = createCircularProfile(imagePath, 92, initials, accentColor, "#2D6A4F");

        Text n = new Text(name);
        n.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 14.5px; -fx-font-weight: bold; -fx-fill: #1B4332; -fx-text-alignment: center;");

        Label r = new Label(role);
        r.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 10.5px; -fx-font-weight: bold; -fx-text-fill: #15803D; -fx-background-color: #DCFCE7; -fx-padding: 2 6; -fx-background-radius: 6;");

        Text tag = new Text("Core2Web Java 2026");
        tag.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 10.5px; -fx-fill: #6B7280;");

        VBox card = new VBox(8, avatar, n, r, tag);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(18, 12, 18, 12));
        card.setStyle(
                "-fx-background-color: #FFFFFF;" +
                "-fx-background-radius: 14px;" +
                "-fx-border-color: #E2EBE5;" +
                "-fx-border-width: 1.2px;" +
                "-fx-border-radius: 14px;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.03), 8, 0, 0, 2);");

        applyHoverScale(card);
        return card;
    }

    // ============================================================
    // 7. FOOTER SECTION
    // ============================================================
    private VBox createFooterSection() {
        Text footerTitle = new Text("FarmEquip – Agriculture Equipment Rental Platform");
        footerTitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 14.5px; -fx-font-weight: bold; -fx-fill: #FFFFFF;");

        Text footerSub = new Text("© 2026 FarmEquip • Developed by Core2Web Project Group • Mentored by Shashi Sir & Core2Web Instructors");
        footerSub.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #D8F3DC;");

        Text location = new Text("Pune, Maharashtra, India • Empowering Indian Agriculture Through Technology");
        location.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-fill: #B7E4C7;");

        VBox footer = new VBox(4, footerTitle, footerSub, location);
        footer.setAlignment(Pos.CENTER);
        footer.setPadding(new Insets(24, 20, 24, 20));
        footer.setStyle("-fx-background-color: #16723A; -fx-background-radius: 14px;");
        return footer;
    }

    // ============================================================
    // HELPER: CIRCULAR PROFILE IMAGE WITH INITIALS FALLBACK
    // ============================================================
    private static Node createCircularProfile(String imagePath, double diameter, String initials, String bgStart, String bgEnd) {
        Image img = resolveImage(imagePath);

        if (img != null && !img.isError()) {
            ImageView iv = new ImageView(img);
            iv.setFitWidth(diameter);
            iv.setFitHeight(diameter);
            iv.setPreserveRatio(false);
            iv.setSmooth(true);

            Circle clip = new Circle(diameter / 2, diameter / 2, diameter / 2);
            iv.setClip(clip);

            // Container with border & soft shadow
            Circle borderRing = new Circle(diameter / 2, diameter / 2, diameter / 2);
            borderRing.setFill(Color.TRANSPARENT);
            borderRing.setStroke(Color.web("#2D6A4F"));
            borderRing.setStrokeWidth(2.5);

            StackPane container = new StackPane(iv, borderRing);
            container.setEffect(new DropShadow(8, 0, 2, Color.rgb(0, 0, 0, 0.12)));
            return container;
        }

        // Initials fallback badge with clean gradient
        Circle bg = new Circle(diameter / 2);
        LinearGradient gradient = new LinearGradient(
                0, 0, 1, 1, true, javafx.scene.paint.CycleMethod.NO_CYCLE,
                new Stop(0, Color.web(bgStart)),
                new Stop(1, Color.web(bgEnd))
        );
        bg.setFill(gradient);
        bg.setStroke(Color.web("#FFFFFF"));
        bg.setStrokeWidth(2.5);

        Text text = new Text(initials);
        text.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: " + (diameter * 0.38) + "px; -fx-font-weight: bold; -fx-fill: #FFFFFF;");

        StackPane container = new StackPane(bg, text);
        container.setEffect(new DropShadow(8, 0, 2, Color.rgb(0, 0, 0, 0.12)));
        return container;
    }

    private static Node createInstituteLogoNode(String path) {
        Image img = resolveImage(path);
        if (img != null && !img.isError()) {
            ImageView iv = new ImageView(img);
            iv.setFitWidth(84);
            iv.setFitHeight(84);
            iv.setPreserveRatio(true);
            iv.setSmooth(true);

            Rectangle clip = new Rectangle(84, 84);
            clip.setArcWidth(18);
            clip.setArcHeight(18);
            iv.setClip(clip);

            StackPane card = new StackPane(iv);
            card.setPrefSize(92, 92);
            card.setMinSize(92, 92);
            card.setMaxSize(92, 92);
            card.setAlignment(Pos.CENTER);
            card.setStyle(
                    "-fx-background-color: #FFFFFF;" +
                    "-fx-background-radius: 18px;" +
                    "-fx-border-color: #CBD5E1;" +
                    "-fx-border-width: 1.5px;" +
                    "-fx-border-radius: 18px;");
            card.setEffect(new DropShadow(10, 0, 3, Color.rgb(0, 0, 0, 0.08)));
            return card;
        }
        return createBrandEmblem("C2W", "#0F172A", "#3B82F6");
    }

    private static StackPane createBrandEmblem(String txt, String bgStart, String bgEnd) {
        Rectangle rect = new Rectangle(80, 80);
        rect.setArcWidth(20);
        rect.setArcHeight(20);
        rect.setFill(Color.web(bgStart));
        rect.setStroke(Color.web(bgEnd));
        rect.setStrokeWidth(2);

        Text t = new Text(txt);
        t.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 24px; -fx-font-weight: bold; -fx-fill: #38BDF8;");

        StackPane sp = new StackPane(rect, t);
        sp.setEffect(new DropShadow(8, 0, 2, Color.rgb(0, 0, 0, 0.15)));
        return sp;
    }

    private static Image resolveImage(String path) {
        if (path == null || path.isEmpty()) return null;
        try {
            var is = AboutUsPage.class.getResourceAsStream(path);
            if (is != null) return new Image(is);

            String[] candidates = {
                "farm/src/main/resources" + path,
                "src/main/resources" + path,
                "farm/target/classes" + path,
                "target/classes" + path,
                path.startsWith("/") ? path.substring(1) : path
            };
            for (String c : candidates) {
                java.io.File f = new java.io.File(c);
                if (f.exists()) return new Image(f.toURI().toString());
            }
        } catch (Exception ignored) {}
        return null;
    }

    private static void applyHoverScale(Node card) {
        card.setOnMouseEntered(e -> {
            card.setScaleX(1.012);
            card.setScaleY(1.012);
        });
        card.setOnMouseExited(e -> {
            card.setScaleX(1.0);
            card.setScaleY(1.0);
        });
    }
}
