package org.eazytg.lib;

import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaVideo;

public class TelegramBotUtils {

    /*
        Validate chatId method
        Returns chatId as String or null if invalid
     */
    public static String validateChatId(Object userId) {
        if (userId instanceof Long || userId instanceof String) {
            return userId.toString();
        }
        Logs.error(userId, "userId must be either Long or String");
        return null;
    }

    /*
        Get InputMedia method
        Returns InputMedia object based on mediaUrl and returns null if unsupported media format
     */
    public static InputMedia getInputMedia(String caption, String mediaUrl) {
        InputMedia media;

        if (mediaUrl.endsWith(".jpg") || mediaUrl.endsWith(".jpeg") || mediaUrl.endsWith(".png")) {
            media = new InputMediaPhoto();
        } else if (mediaUrl.endsWith(".mp4") || mediaUrl.endsWith(".avi") || mediaUrl.endsWith(".mov")) {
            media = new InputMediaVideo();
        } else {
            throw new IllegalArgumentException("Unsupported media format");
        }

        media.setMedia(mediaUrl);
        media.setCaption(caption);
        media.setParseMode("HTML");
        return media;
    }

}
