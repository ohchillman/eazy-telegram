package org.eazytg.lib;

public class TelegramBotUtils {

    public static String validateChatId(Object userId) {
        if (userId instanceof Long || userId instanceof String) {
            return userId.toString();
        }
        Logs.error(userId, "userId must be either Long or String");
        return null;
    }

}
