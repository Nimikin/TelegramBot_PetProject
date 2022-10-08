package com.nimikin.TelegramBot.config;

import com.nimikin.TelegramBot.TelegramBot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Slf4j
@Component
public class BotInitializer {
    @Autowired
    TelegramBot bot;
    @EventListener({ContextRefreshedEvent.class})
    public void init() throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            log.info("Registering bot...");
            telegramBotsApi.registerBot(bot);
        }catch (TelegramApiException e){
            log.error("Failed to register bot(check internet connection / bot token or make sure only one instance of bot is running).", e);
        }
        log.info("Telegram bot is ready to accept updates from user......");
    }
}
