package com.desgin.view.operator;

import java.util.ArrayList;
import java.util.List;

public class OperatorProfileStore {

    public static String name = "Ramesh Chavan";
    public static String phone = "+91 94231 98765";
    public static String zone = "Pune / Baramati Sector";
    public static String licenseNo = "MH-12-HM-88492 (Valid Heavy Agri)";
    public static String badge = "Certified Pro";
    public static String status = "Available for Field Shifts";

    // Dynamic Operator KPI Metrics
    public static String assignedMachinery = "4 Units";
    public static String assignedMachinerySub = "2 Field Ready • 1 In Shift";
    public static String activeJobs = "2 Assigned";
    public static String activeJobsSub = "1 Running • 1 Scheduled";
    public static String engineHours = "148.5 hrs";
    public static String engineHoursSub = "+18.2 hrs this week ↑";
    public static String wagesEarned = "₹28,400";
    public static String wagesEarnedSub = "₹4,200 pending settlement";

    private static final List<Runnable> profileListeners = new ArrayList<>();

    public static synchronized void setProfile(String newName, String newPhone, String newZone, String newLicense) {
        if (newName != null && !newName.trim().isEmpty()) {
            name = newName.trim();
        }
        if (newPhone != null && !newPhone.trim().isEmpty()) {
            phone = newPhone.trim();
        }
        if (newZone != null && !newZone.trim().isEmpty()) {
            zone = newZone.trim();
        }
        if (newLicense != null && !newLicense.trim().isEmpty()) {
            licenseNo = newLicense.trim();
        }

        notifyListeners();
    }

    public static synchronized void setMetrics(String machinery, String machSub, String jobs, String jobsSub, String hours, String hoursSub, String wages, String wageSub) {
        if (machinery != null) assignedMachinery = machinery;
        if (machSub != null) assignedMachinerySub = machSub;
        if (jobs != null) activeJobs = jobs;
        if (jobsSub != null) activeJobsSub = jobsSub;
        if (hours != null) engineHours = hours;
        if (hoursSub != null) engineHoursSub = hoursSub;
        if (wages != null) wagesEarned = wages;
        if (wageSub != null) wagesEarnedSub = wageSub;

        notifyListeners();
    }

    public static void notifyListeners() {
        for (Runnable r : new ArrayList<>(profileListeners)) {
            try {
                r.run();
            } catch (Exception ignored) {}
        }
    }

    public static synchronized void addProfileListener(Runnable r) {
        if (r != null && !profileListeners.contains(r)) {
            profileListeners.add(r);
        }
    }

    public static synchronized void removeProfileListener(Runnable r) {
        if (r != null) {
            profileListeners.remove(r);
        }
    }
}
