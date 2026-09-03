package com.desgin.config;

import java.io.File;
import java.util.Map;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

public class CloudinaryConfig {

    private static Cloudinary cloudinary;

    static {
        try {
            cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "uirks0t3",
                "api_key", "898399852596826",
                "api_secret", "2sYdtpZBuYti3dG3J9i6pKVSUSk",
                "secure", true
            ));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Cloudinary getCloudinary() {
        return cloudinary;
    }

    public static String uploadImage(File file) {
        if (file == null || !file.exists()) return null;
        try {
            if (cloudinary == null) {
                cloudinary = new Cloudinary(ObjectUtils.asMap(
                    "cloud_name", "uirks0t3",
                    "api_key", "898399852596826",
                    "api_secret", "2sYdtpZBuYti3dG3J9i6pKVSUSk",
                    "secure", true
                ));
            }
            Map<?, ?> uploadResult = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
            if (uploadResult != null && uploadResult.containsKey("secure_url")) {
                return (String) uploadResult.get("secure_url");
            }
        } catch (Exception e) {
            System.err.println("Cloudinary upload failed: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
