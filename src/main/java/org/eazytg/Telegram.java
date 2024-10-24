package org.eazytg;

import org.eazytg.Button;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaVideo;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Telegram {

    public static Message sendMessage(TelegramLongPollingBot bot, String userId, String messageText, Object keyboardMarkup, Integer replyToMessageId) {
        SendMessage message = new SendMessage();
        message.setChatId(userId);
        message.setParseMode("HTML");
        message.setText(messageText);
        message.setReplyMarkup(keyboardMarkup instanceof InlineKeyboardMarkup ? (InlineKeyboardMarkup) keyboardMarkup : (ReplyKeyboardMarkup) keyboardMarkup);

        if (replyToMessageId != null) {
            message.setReplyToMessageId(replyToMessageId);
        }

        try {
            return bot.execute(message);
        } catch (TelegramApiException e) {
            Logs.error(userId, e.getMessage());
            return null;
        }
    }

    public static Message sendPhoto(TelegramLongPollingBot bot, String userId, String messageText, String photoUrl, Object keyboardMarkup, Integer replyToMessageId) {
        SendPhoto message = new SendPhoto();
        message.setChatId(userId);
        message.setParseMode("HTML");
        message.setPhoto(new InputFile(photoUrl));
        message.setCaption(messageText);
        message.setReplyMarkup(keyboardMarkup instanceof InlineKeyboardMarkup ? (InlineKeyboardMarkup) keyboardMarkup : (ReplyKeyboardMarkup) keyboardMarkup);

        if (replyToMessageId != null) {
            message.setReplyToMessageId(replyToMessageId);
        }

        try {
            return bot.execute(message);
        } catch (TelegramApiException e) {
            Logs.error(userId, e.getMessage());
            return null;
        }
    }

    public static InlineKeyboardMarkup createKeyboard(List<Button> buttons, int columns) {
        return createKeyboardButtons(buttons, columns, 17);
    }

    public static InlineKeyboardMarkup createKeyboard(List<Button> buttons, int columns, int maxButtonLength) {
        return createKeyboardButtons(buttons, columns, maxButtonLength);
    }


    public static InlineKeyboardMarkup createKeyboardButtons(List<Button> buttons, int columns, int maxButtonLength) {
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        int currentColumn = 0;

        for (int i = 0; i < buttons.size(); i++) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(buttons.get(i).getText());

            if (buttons.get(i).isUrl()) {
                button.setUrl(buttons.get(i).getData());
            } else {
                button.setCallbackData(buttons.get(i).getData());
            }

            if (buttons.get(i).getText().length() > maxButtonLength) {
                if (!rowInline.isEmpty()) {
                    rowsInline.add(new ArrayList<>(rowInline));
                    rowInline.clear();
                }
                rowsInline.add(Collections.singletonList(button));
                currentColumn = 0;
            } else {
                rowInline.add(button);
                currentColumn++;

                if (currentColumn == columns || i == buttons.size() - 1) {
                    rowsInline.add(new ArrayList<>(rowInline));
                    rowInline.clear();
                    currentColumn = 0;
                }
            }
        }

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        keyboardMarkup.setKeyboard(rowsInline);
        return keyboardMarkup;
    }


    public static void editMessage(TelegramLongPollingBot bot, String chatId, int messageId, String newText, InlineKeyboardMarkup keyboardMarkup) {
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setParseMode("HTML");
        editMessageText.setChatId(chatId);
        editMessageText.setMessageId(messageId);
        editMessageText.setText(newText);
        editMessageText.setReplyMarkup(keyboardMarkup);

        try {
            bot.execute(editMessageText);
        } catch (TelegramApiException e) {
//            throw new RuntimeException(e);
            Logs.error(chatId, e.getMessage());
        }
    }


    public static void editMedia(TelegramLongPollingBot bot, String chatId, int messageId, String caption, String mediaUrl, InlineKeyboardMarkup keyboardMarkup) {
        EditMessageMedia editMessageMedia = new EditMessageMedia();
        editMessageMedia.setChatId(chatId);
        editMessageMedia.setMessageId(messageId);

        if (mediaUrl.endsWith(".jpg") || mediaUrl.endsWith(".jpeg") || mediaUrl.endsWith(".png")) {
            InputMediaPhoto photo = new InputMediaPhoto();
            photo.setMedia(mediaUrl);
            photo.setCaption(caption);
            photo.setParseMode("HTML");
            editMessageMedia.setMedia(photo);
        } else if (mediaUrl.endsWith(".mp4") || mediaUrl.endsWith(".avi") || mediaUrl.endsWith(".mov")) {
            InputMediaVideo video = new InputMediaVideo();
            video.setMedia(mediaUrl);
            video.setCaption(caption);
            video.setParseMode("HTML");
            editMessageMedia.setMedia(video);
        } else {
            throw new IllegalArgumentException("Unsupported media format");
        }

        editMessageMedia.setReplyMarkup(keyboardMarkup);

        try {
            bot.execute(editMessageMedia);
        } catch (TelegramApiException e) {
            Logs.error(chatId, e.getMessage());
        }
    }


    public static void deleteMessage(TelegramLongPollingBot bot, String chatId, int messageId) {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(chatId);
        deleteMessage.setMessageId(messageId);

        try {
            bot.execute(deleteMessage);
        } catch (Exception e) {
            Logs.error(chatId, e.getMessage());
        }
    }

}
