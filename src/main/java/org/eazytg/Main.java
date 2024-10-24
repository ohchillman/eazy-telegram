package org.eazytg;

import org.eazytg.Logs;
import org.apache.commons.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.TimeZone;

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