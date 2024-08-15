package io.tonme.minebot.util;

import java.util.HashMap;
import java.util.Map;

public class FileTypeUtils {

    private static final Map<String, String> fileTypes = new HashMap<>();

    static {
        fileTypes.put("jpg", "image");
        fileTypes.put("jpeg", "image");
        fileTypes.put("png", "image");
        fileTypes.put("gif", "image");
        fileTypes.put("bmp", "image");
        fileTypes.put("mp4", "video");
        fileTypes.put("mkv", "video");
        fileTypes.put("avi", "video");
        fileTypes.put("mov", "video");
        fileTypes.put("wmv", "video");
        fileTypes.put("flv", "video");
        fileTypes.put("mp3", "audio");
        fileTypes.put("wav", "audio");
        fileTypes.put("aac", "audio");
        fileTypes.put("flac", "audio");
        fileTypes.put("ogg", "audio");
    }

    public static String getFileType(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "unknown";
        }

        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        return fileTypes.getOrDefault(fileExtension, "unknown");
    }
}
