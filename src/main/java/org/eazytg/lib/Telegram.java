package org.eazytg.lib;

import org.eazytg.lib.Logs;
import org.eazytg.lib.Button;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaVideo;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.eazytg.lib.TelegramBotUtils.validateChatId;

public class Telegram {

    /*
        Method for sending messages to the user
        The method takes the bot, the user ID, the text of the message, the keyboard and the ID of the message to which the response is sent
        The method returns the Message
     */
    public static Message sendMessage(TelegramLongPollingBot bot, Object userId, String messageText, Object keyboardMarkup, Integer replyToMessageId) {
        SendMessage message = new SendMessage();

        String chatId = validateChatId(userId);
        if (chatId == null) return null;

        message.setChatId(chatId);
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

    /*
        Method for sending photos to the user
        The method takes the bot, the user ID, the text of the message, the URL of the photo, the keyboard and the ID of the message to which the response is sent
        The method returns the Message
     */
    public static Message sendPhoto(TelegramLongPollingBot bot, Object userId, String messageText, String photoUrl, Object keyboardMarkup, Integer replyToMessageId) {
        SendPhoto message = new SendPhoto();

        String chatId = validateChatId(userId);
        if (chatId == null) return null;

        message.setChatId(chatId);
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

    /*
        Overloaded methods
        Method for sending an album of photos to the user
        The method takes the bot, the user ID, the text of the message, the URL of the photos, the keyboard and the ID of the message to which the response is sent
        The method returns the List of Messages
     */
    public static List<Message> sendPhoto(TelegramLongPollingBot bot, Object userId, String messageText, List<String> photoUrls, Integer replyToMessageId) {
        if (photoUrls.size() == 1) {
            sendPhoto(bot, userId, messageText, photoUrls.getFirst(), null, replyToMessageId);
            return null;
        }

        SendMediaGroup mediaGroup = new SendMediaGroup();

        String chatId = validateChatId(userId);
        if (chatId == null) return null;

        List<InputMedia> media = new ArrayList<>();
        for (int i = 0; i < photoUrls.size(); i++) {
            InputMediaPhoto photo = new InputMediaPhoto(photoUrls.get(i));
            photo.setParseMode("HTML");

            if (i == 0) {
                photo.setCaption(messageText);
            }

            media.add(photo);
        }

        mediaGroup.setChatId(chatId);
        mediaGroup.setMedias(media);

        if (replyToMessageId != null) {
            mediaGroup.setReplyToMessageId(replyToMessageId);
        }

        try {
            return bot.execute(mediaGroup);
        } catch (TelegramApiException e) {
            Logs.error(userId, e.getMessage());
            return null;
        }
    }

    /*
        Method for sending documents to the user
        The method takes the bot, the user ID, the text of the message, the URL of the document, the keyboard and the ID of the message to which the response is sent
        The method returns the Message

        Supported document formats: .pdf, .doc, .docx, .xls, .xlsx, .ppt, .pptx, .txt, .zip, .rar, .7z, .tar, .gz, .mp3, .wav, .flac, .mp4, .avi, .mov
     */
    public static Message sendDocument(TelegramLongPollingBot bot, Object userId, String messageText, String documentUrl, Object keyboardMarkup, Integer replyToMessageId) {
        SendDocument message = new SendDocument();

        String chatId = validateChatId(userId);
        if (chatId == null) return null;

        message.setChatId(chatId);
        message.setParseMode("HTML");
        message.setDocument(new InputFile(documentUrl));
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

    /*
        Overloaded methods
        Method for creating buttons using the Button class
        The method creates a keyboard with the specified number of columns
        The method returns the InlineKeyboardMarkup
    */
    public static InlineKeyboardMarkup createKeyboard(List<Button> buttons, int columns) {
        return createKeyboardButtons(buttons, columns, 17);
    }

    /*
        Method for creating buttons using the Button class
        The method creates a keyboard with the specified number of columns and a maximum button length
        If the number of characters in a button exceeds the maximum, then the button is moved to a separate line
        The method returns the InlineKeyboardMarkup
    */
    public static InlineKeyboardMarkup createKeyboard(List<Button> buttons, int columns, int maxButtonLength) {
        return createKeyboardButtons(buttons, columns, maxButtonLength);
    }

    /*
        Method for creating buttons using the String class
        The method creates a keyboard with the specified number of columns
        The method returns the ReplyKeyboardMarkup
    */
    public static ReplyKeyboardMarkup createReplyKeyboard(List<String> buttonLabels, int columns) {
        List<List<KeyboardButton>> rowsInline = new ArrayList<>();
        List<KeyboardButton> rowInline = new ArrayList<>();

        for (int i = 0; i < buttonLabels.size(); i++) {
            KeyboardButton button = new KeyboardButton();
            button.setText(buttonLabels.get(i));
            rowInline.add(button);

            if ((i + 1) % columns == 0 || i == buttonLabels.size() - 1) {
                rowsInline.add(rowInline);
                rowInline = new ArrayList<>();
            }
        }

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        for (List<KeyboardButton> row : rowsInline) {
            KeyboardRow keyboardRow = new KeyboardRow();
            keyboardRow.addAll(row);
            keyboardRows.add(keyboardRow);
        }
        keyboardMarkup.setKeyboard(keyboardRows);
        keyboardMarkup.setResizeKeyboard(true);
        return keyboardMarkup;
    }

    /*
        Method for creating buttons using the String class
        The method creates a keyboard with the specified number of columns and a maximum button length
        If the number of characters in a button exceeds the maximum, then the button is moved to a separate line
        The method returns the InlineKeyboardMarkup
    */
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

    /*
        Method for editing messages
        The method takes the bot, the userID, messageID to be edited, the new text of the message and new keyboard
     */
    public static void editMessage(TelegramLongPollingBot bot, Object userId, int messageId, String newText, InlineKeyboardMarkup keyboardMarkup) {
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setParseMode("HTML");

        String chatId = validateChatId(userId);
        if (chatId == null) return;

        editMessageText.setChatId(chatId);
        editMessageText.setMessageId(messageId);
        editMessageText.setText(newText);
        editMessageText.setReplyMarkup(keyboardMarkup);

        try {
            bot.execute(editMessageText);
        } catch (TelegramApiException e) {
            Logs.error(userId, e.getMessage());
        }
    }

    /*
        Method for editing media
        The method takes the bot, the userID, messageID to be edited, the new caption, the URL of the media and new keyboard
        Supported media formats: .jpg, .jpeg, .png, .mp4, .avi, .mov
     */
    public static void editMedia(TelegramLongPollingBot bot, Object userId, int messageId, String caption, String mediaUrl, InlineKeyboardMarkup keyboardMarkup) {
        EditMessageMedia editMessageMedia = new EditMessageMedia();

        String chatId = validateChatId(userId);
        if (chatId == null) return;

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
            Logs.error(userId, e.getMessage());
        }
    }

    /*
        Method for deleting messages
        The method takes the bot, the userID and messageID to be deleted
     */
    public static void deleteMessage(TelegramLongPollingBot bot, Object userId, int messageId) {
        DeleteMessage deleteMessage = new DeleteMessage();

        String chatId = validateChatId(userId);
        if (chatId == null) return;

        deleteMessage.setChatId(chatId);
        deleteMessage.setMessageId(messageId);

        try {
            bot.execute(deleteMessage);
        } catch (Exception e) {
            Logs.error(userId, e.getMessage());
        }
    }

}
