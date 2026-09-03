package com.desgin.view.farmer.Swapnil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class EquipmentDataStore {

    public static class EquipmentItem {
        public String id;
        public String name;
        public String category;
        public int pricePerDay;
        public String rating;
        public String location;
        public String imagePath;
        public String specs;
        public String status; // "AVAILABLE", "RENTED OUT", "IN SERVICE"
        public boolean hasOperator;
        public int totalRentals;
        public int lifetimeEarned;

        public EquipmentItem(String id, String name, String category, int pricePerDay, String rating,
                             String location, String imagePath, String specs, String status, boolean hasOperator) {
            this.id = id != null ? id : "EQ" + (1000 + (int)(Math.random() * 9000));
            this.name = name;
            this.category = category != null ? category : "Equipment";
            this.pricePerDay = pricePerDay;
            this.rating = rating != null ? rating : "4.8";
            this.location = location != null ? location : "Pune, Maharashtra";
            this.imagePath = (imagePath != null && !imagePath.isEmpty()) ? imagePath : "file:farm/src/main/resources/assets/Images/tractor.png";
            this.specs = specs != null ? specs : "Standard Machinery Specification";
            this.status = status != null ? status : "AVAILABLE";
            this.hasOperator = hasOperator;
            this.totalRentals = 0;
            this.lifetimeEarned = 0;
        }
    }

    private static final List<EquipmentItem> equipmentList = new ArrayList<>();

    // Starts empty in building phase
    static {
        // Empty by default
    }

    public static synchronized List<EquipmentItem> getAllEquipment() {
        return new ArrayList<>(equipmentList);
    }

    public static synchronized List<EquipmentItem> getAvailableEquipment() {
        List<EquipmentItem> avail = new ArrayList<>();
        for (EquipmentItem item : equipmentList) {
            if ("AVAILABLE".equalsIgnoreCase(item.status)) {
                avail.add(item);
            }
        }
        return avail;
    }

    public static synchronized int getAvailableCount() {
        return getAvailableEquipment().size();
    }

    public static synchronized int getTotalCount() {
        return equipmentList.size();
    }

    public static synchronized void addEquipment(EquipmentItem item) {
        if (item != null) {
            equipmentList.add(0, item);
        }
    }

    public static synchronized void removeEquipment(String idOrName) {
        if (idOrName == null) return;
        equipmentList.removeIf(item -> idOrName.equalsIgnoreCase(item.id) || idOrName.equalsIgnoreCase(item.name));
    }

    public static synchronized List<EquipmentItem> getRandomRecommended(int maxCount) {
        String currentTown = FarmerProfileStore.town != null ? FarmerProfileStore.town.trim().toLowerCase() : "";
        List<EquipmentItem> available = new ArrayList<>();
        for (EquipmentItem item : equipmentList) {
            if ("AVAILABLE".equalsIgnoreCase(item.status)) {
                if (currentTown.isEmpty() || (item.location != null && item.location.toLowerCase().contains(currentTown))) {
                    available.add(item);
                }
            }
        }
        if (available.isEmpty()) {
            return new ArrayList<>();
        }
        List<EquipmentItem> copy = new ArrayList<>(available);
        Collections.shuffle(copy, new Random());
        int count = Math.min(maxCount, copy.size());
        return new ArrayList<>(copy.subList(0, count));
    }

    public static synchronized void updateStatus(String name, String newStatus) {
        if (name == null || newStatus == null) return;
        for (EquipmentItem item : equipmentList) {
            if (name.equalsIgnoreCase(item.name) || name.equalsIgnoreCase(item.id)) {
                item.status = newStatus;
                break;
            }
        }
    }

    public static synchronized void updatePrice(String name, int newPrice) {
        if (name == null) return;
        for (EquipmentItem item : equipmentList) {
            if (name.equalsIgnoreCase(item.name) || name.equalsIgnoreCase(item.id)) {
                item.pricePerDay = newPrice;
                break;
            }
        }
    }

    public static synchronized void clearAll() {
        equipmentList.clear();
    }
}
