package org.eazytg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class Logs {
    private static final Logger logger = LoggerFactory.getLogger(Logs.class);

    public static void log(Object message) {
        logWithCallerInfo("info", null, message);
    }

    public static void log(Object chatId, Object message) {
        logWithCallerInfo("info", chatId, message);
    }

    public static void error(Object message) {
        logWithCallerInfo("error", null, message);
    }

    public static void error(Object chatId, Object message) {
        logWithCallerInfo("error", chatId, message);
    }

    private static void logWithCallerInfo(String level, Object chatId, Object message) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StackTraceElement caller = stackTrace[3]; // 3-й элемент стека вызовов - это вызывающий метод
        String callerClass = caller.getClassName();
        String callerMethod = caller.getMethodName();

        // Installing MDC
        if (chatId != null) {
            MDC.put("chatId", chatId.toString());
        }
        MDC.put("callerClass", callerClass);
        MDC.put("callerMethod", callerMethod);

        try {
            // Log messages based on the level
            switch (level) {
                case "info":
                    logger.info(message.toString());
                    break;
                case "error":
                    logger.error(message.toString());
                    break;
                case "debug":
                    logger.debug(message.toString());
                    break;
                default:
                    logger.info(message.toString());
                    break;
            }
        } finally {
            // Deleting MDC
            if (chatId != null) {
                MDC.remove("chatId");
            }
            MDC.remove("callerClass");
            MDC.remove("callerMethod");
        }
    }
}
