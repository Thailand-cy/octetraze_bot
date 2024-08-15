package io.tonme.minebot.util;

import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.GetUserProfilePhotos;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.UserProfilePhotos;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.concurrent.ExecutionException;

public class ProfilePhotoUtil {

    public static String getUserProfilePhoto(TelegramClient telegramClient, Long userId) {
        try {
            GetUserProfilePhotos getUserProfilePhotos = GetUserProfilePhotos.builder()
                    .userId(userId)
                    .limit(1)
                    .build();

            UserProfilePhotos userProfilePhotos = telegramClient.executeAsync(getUserProfilePhotos).get();

            if (userProfilePhotos.getTotalCount() > 0) {
                String fileId = userProfilePhotos.getPhotos().get(0).get(0).getFileId();
                return getProfilePhotoUrl(telegramClient, fileId);
            }
        } catch (TelegramApiException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getProfilePhotoUrl(TelegramClient telegramClient, String fileId) {
        try {
            GetFile getFileMethod = GetFile.builder().fileId(fileId).build();
            File file = telegramClient.executeAsync(getFileMethod).get();
            return file.getFilePath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
