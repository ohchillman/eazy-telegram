package org.eazytg;

import org.eazytg.lib.Logs;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {

    public static void main(String[] args) throws TelegramApiException {
        ChatBot chatBot = new ChatBot();

        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(chatBot);
            Logs.log("ChatBot registered successfully.");
        } catch (TelegramApiException e) {
            Logs.error(e.getMessage());
        }
    }

}