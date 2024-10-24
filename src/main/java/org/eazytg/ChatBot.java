package org.eazytg;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class ChatBot extends TelegramLongPollingBot {

    private String botUsername;
    private String botToken;

    public ChatBot() {
        Properties properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new IOException("Unable to find config.properties");
            }
            properties.load(input);
            botUsername = properties.getProperty("bot.username");
            botToken = properties.getProperty("bot.token");
        } catch (IOException ex) {
            Logs.error(ex.getMessage());
        }
    }

    public void onUpdateReceived(Update update) {

        if (update.hasMessage()) {
            // Get main user info
            long chatId = update.getMessage().getChatId();
            String userName = update.getMessage().getFrom().getUserName();
            String userText = update.getMessage().getText();

            messageHandler(userText, chatId, userName);
            return;
        }
        if (update.hasCallbackQuery()) {
            long chatId = update.getCallbackQuery().getMessage().getChatId();
            String callbackData = update.getCallbackQuery().getData();
            callbackHandler(update, callbackData, chatId);
            return;
        }
        if (update.hasChatJoinRequest()) {
            long chatId = update.getChatJoinRequest().getUserChatId();
            return;
        }
    }

    private void messageHandler(String userText, long chatId, String userName) {
        // Check if the user text is a command
        if (userText.equals("❤\uFE0F Donate ❤\uFE0F")) {
            String donateText = "You can donate to the project by clicking on the link below:";
            List<Button> donateButtons = new ArrayList<>();
            donateButtons.add(new Button("Donate", "https://www.paypal.com"));
            Telegram.sendMessage(this, chatId, donateText, Telegram.createKeyboard(donateButtons, 1), null);
            return;
        }

        // Default message
        // Message text
        String greetingText = "Hello, " + userName + "!\n" +
                "This is a demo of the <a href=\"https://github.com/ohchillman/eazy-telegram/tree/develop\">EazyTelegram project</a>.\n";

        // Keyboard buttons
        List<String> buttons = new ArrayList<>();
        buttons.add(("❤\uFE0F Donate ❤\uFE0F"));

        // Send message with keyboard (1 line - 2 button)
        Message mainMessage = Telegram.sendMessage(this, chatId, greetingText, Telegram.createReplyKeyboard(buttons, 2), null);

        //Second message text
        String secondMessageText = "Choose the link:";

        // Inline keyboard buttons
        List<Button> inlineButtons = new ArrayList<>();
        inlineButtons.add(new Button("GitHub", "https://github.com/ohchillman/eazy-telegram/tree/develop"));
        inlineButtons.add(new Button("⚡\uFE0F Deploy on a Server ⚡\uFE0F", "https://mine.hosting"));
        inlineButtons.add(new Button("📚 Show Photo 📚", "photo_callback"));

        // Send message with inline keyboard (1 line - 1 button)
        Telegram.sendMessage(this, chatId, secondMessageText, Telegram.createKeyboard(inlineButtons, 1), mainMessage.getMessageId());
    }

    private void callbackHandler(Update update, String callbackData, long chatId) {
        switch (callbackData) {
            case "photo_callback":
                String photoUrl = "https://picsum.photos/200/300.jpg";
                List<Button> photoButtons = new ArrayList<>();
                photoButtons.add(new Button("Edit photo", "edit_photo_callback"));
                photoButtons.add(new Button("Close", "close_callback"));
                Telegram.sendPhoto(this, chatId, "You clicked on the button: " + callbackData, photoUrl, Telegram.createKeyboard(photoButtons, 1), null);
                break;
            case "edit_photo_callback":
                String editPhotoUrl = "https://picsum.photos/200/301.jpg";
                List<Button> editPhotoButtons = new ArrayList<>();
                editPhotoButtons.add(new Button("Close", "close_callback"));
                Telegram.editMedia(this, chatId, update.getCallbackQuery().getMessage().getMessageId(), "Edited", editPhotoUrl, Telegram.createKeyboard(editPhotoButtons, 1));
                break;
            case "close_callback":
                Telegram.deleteMessage(this, chatId, update.getCallbackQuery().getMessage().getMessageId());
                break;
        }
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }
}
