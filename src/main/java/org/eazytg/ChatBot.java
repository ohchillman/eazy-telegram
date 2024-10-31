package org.eazytg;
import org.eazytg.lib.Button;
import org.eazytg.lib.Logs;
import org.eazytg.lib.Telegram;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

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
        String audioUrl = "https://tusic.net/music/0-0-1-30447-20";
        String photoUrl = "https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png";
        String videoUrl = "https://www.w3schools.com/html/mov_bbb.mp4";

        List<String> videoList = new ArrayList();
        videoList.add("https://www.w3schools.com/html/mov_bbb.mp4");

        Telegram.sendPhoto(this, chatId, "Test doc", videoList, null);
    }

    private void callbackHandler(Update update, String callbackData, long chatId) {

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
