package com.desgin.view.farmer.ashutosh.helpandsupport;

public class FarmerAI {

    public static String getAnswer(String question) {

        String q = question.toLowerCase().trim();


        // ==========================================
        // GREETINGS
        // ==========================================

        if (q.contains("hello")
                || q.contains("hi")
                || q.contains("hey")
                || q.contains("namaste")) {

            return "Namaste! 👋\n\n"
                    + "I am your Farmer Assistant. "
                    + "I can help you with tractor bookings, "
                    + "equipment problems, crops, irrigation, "
                    + "soil and general farming questions.";
        }


        // ==========================================
        // TRACTOR BOOKING
        // ==========================================

        if (q.contains("book tractor")
                || q.contains("tractor booking")
                || q.contains("book a tractor")
                || q.contains("how do i book")) {

            return "🚜 Tractor Booking\n\n"
                    + "1. Open the Equipment page.\n"
                    + "2. Select the tractor you need.\n"
                    + "3. Select your required date.\n"
                    + "4. Check availability.\n"
                    + "5. Click 'Book Now'.\n\n"
                    + "If the booking does not work, "
                    + "please contact support.";
        }


        // ==========================================
        // BOOKING FAILED
        // ==========================================

        if (q.contains("booking failed")
                || q.contains("booking problem")
                || q.contains("cannot book")
                || q.contains("can't book")
                || q.contains("unable to book")) {

            return "⚠️ Booking Problem\n\n"
                    + "Please try these steps:\n"
                    + "1. Check your internet connection.\n"
                    + "2. Check whether the equipment is available.\n"
                    + "3. Check that your selected dates are correct.\n"
                    + "4. Try booking again.\n\n"
                    + "If the problem continues, contact our support team.";
        }


        // ==========================================
        // EQUIPMENT NOT WORKING
        // ==========================================

        if (q.contains("tractor not working")
                || q.contains("equipment not working")
                || q.contains("machine not working")
                || q.contains("tractor problem")
                || q.contains("equipment problem")) {

            return "🔧 Equipment Problem\n\n"
                    + "If the equipment suddenly stops working:\n"
                    + "1. Stop using the machine if it is unsafe.\n"
                    + "2. Move to a safe location if possible.\n"
                    + "3. Do not attempt dangerous repairs yourself.\n"
                    + "4. Contact our support team.\n\n"
                    + "A qualified mechanic should inspect serious equipment problems.";
        }


        // ==========================================
        // EQUIPMENT BREAKDOWN
        // ==========================================

        if (q.contains("breakdown")
                || q.contains("break down")
                || q.contains("broke down")
                || q.contains("broken")) {

            return "🚜 Equipment Breakdown\n\n"
                    + "Please stop operating the equipment if it is unsafe.\n\n"
                    + "Contact the equipment support team and provide:\n"
                    + "• Your booking ID\n"
                    + "• Equipment name\n"
                    + "• Location\n"
                    + "• Description of the problem\n\n"
                    + "Do not attempt major mechanical repairs yourself.";
        }


        // ==========================================
        // CANCELLATION
        // ==========================================

        if (q.contains("cancel")
                || q.contains("cancellation")) {

            return "❌ Booking Cancellation\n\n"
                    + "Open your bookings section and select "
                    + "the booking you want to cancel.\n\n"
                    + "Cancellation and refund rules may depend "
                    + "on how close the cancellation is to the "
                    + "booking date.";
        }


        // ==========================================
        // DEPOSIT
        // ==========================================

        if (q.contains("deposit")
                || q.contains("refund")
                || q.contains("money back")) {

            return "💰 Deposit & Refund\n\n"
                    + "The deposit is normally returned after "
                    + "the equipment has been returned and checked.\n\n"
                    + "The exact refund time depends on your "
                    + "application's refund policy.";
        }


        // ==========================================
        // IRRIGATION
        // ==========================================

        if (q.contains("irrigation")
                || q.contains("water crop")
                || q.contains("watering")
                || q.contains("water my crop")) {

            return "💧 Irrigation Advice\n\n"
                    + "Avoid both over-watering and under-watering.\n\n"
                    + "Check:\n"
                    + "• Soil moisture\n"
                    + "• Crop growth stage\n"
                    + "• Weather conditions\n"
                    + "• Soil type\n\n"
                    + "Drip irrigation can help reduce water wastage "
                    + "for suitable crops.";
        }


        // ==========================================
        // SOIL
        // ==========================================

        if (q.contains("soil")
                || q.contains("fertility")
                || q.contains("fertilizer")) {

            return "🌱 Soil Advice\n\n"
                    + "Healthy soil is important for good crop growth.\n\n"
                    + "Consider:\n"
                    + "• Testing your soil\n"
                    + "• Maintaining organic matter\n"
                    + "• Using suitable fertilizers\n"
                    + "• Avoiding excessive fertilizer use\n"
                    + "• Maintaining proper soil moisture\n\n"
                    + "For fertilizer quantities, follow a soil test "
                    + "and advice from a qualified agricultural expert.";
        }


        // ==========================================
        // CROP
        // ==========================================

        if (q.contains("crop")
                || q.contains("farming")
                || q.contains("farm")) {

            return "🌾 Farming Advice\n\n"
                    + "Good farming decisions depend on your crop, "
                    + "soil, season and local weather.\n\n"
                    + "Tell me more about your crop, for example:\n"
                    + "• Crop name\n"
                    + "• Current growth stage\n"
                    + "• Soil type\n"
                    + "• Your main problem\n\n"
                    + "Then I can give you general guidance.";
        }


        // ==========================================
        // PEST
        // ==========================================

        if (q.contains("pest")
                || q.contains("insect")
                || q.contains("bug")) {

            return "🐛 Pest Problem\n\n"
                    + "First identify the pest before choosing "
                    + "a treatment.\n\n"
                    + "You can check:\n"
                    + "• Which part of the plant is affected\n"
                    + "• Leaf damage pattern\n"
                    + "• Presence of insects or eggs\n"
                    + "• Recent weather conditions\n\n"
                    + "For pesticide selection and dosage, "
                    + "consult a qualified agricultural officer "
                    + "and follow the product label.";
        }


        // ==========================================
        // WEATHER
        // ==========================================

        if (q.contains("weather")
                || q.contains("rain")
                || q.contains("rainfall")) {

            return "🌦️ Weather Advice\n\n"
                    + "Weather can strongly affect irrigation, "
                    + "spraying and harvesting.\n\n"
                    + "Before major farm work, check your local "
                    + "weather forecast.\n\n"
                    + "Avoid unnecessary irrigation before heavy rain.";
        }


        // ==========================================
        // HARVEST
        // ==========================================

        if (q.contains("harvest")
                || q.contains("harvesting")) {

            return "🌾 Harvesting Advice\n\n"
                    + "Harvest timing depends on the crop and "
                    + "its maturity.\n\n"
                    + "Look for:\n"
                    + "• Appropriate crop maturity\n"
                    + "• Suitable weather\n"
                    + "• Proper moisture level\n\n"
                    + "For crop-specific harvesting advice, "
                    + "tell me the name of your crop.";
        }


        // ==========================================
        // HELPLINE
        // ==========================================

        if (q.contains("helpline")
                || q.contains("support number")
                || q.contains("contact support")
                || q.contains("customer care")) {

            return "📞 Farmer Support\n\n"
                    + "Toll-Free Helpline: 1800-123-4567\n"
                    + "Available: Monday - Saturday\n"
                    + "Time: 8 AM - 8 PM\n\n"
                    + "You can also use WhatsApp Support.";
        }


        // ==========================================
        // THANK YOU
        // ==========================================

        if (q.contains("thank")
                || q.contains("thanks")) {

            return "You're welcome! 😊\n\n"
                    + "I am always happy to help.";
        }


        // ==========================================
        // DEFAULT ANSWER
        // ==========================================

        return "🤔 I am not sure about that question yet.\n\n"
                + "Try asking me something like:\n\n"
                + "🚜 How do I book a tractor?\n"
                + "🔧 My tractor is not working.\n"
                + "💰 How does the deposit refund work?\n"
                + "💧 How should I manage irrigation?\n"
                + "🌱 How can I improve my soil?\n"
                + "🐛 I have a pest problem.\n"
                + "🌾 Tell me about farming.";
    }
}