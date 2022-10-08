package com.nimikin.TelegramBot.handler.impl;

import com.nimikin.TelegramBot.handler.UserRequestHandler;
import com.nimikin.TelegramBot.helper.KeyboardHelper;
import com.nimikin.TelegramBot.model.UserRequest;
import com.nimikin.TelegramBot.service.TelegramService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import static com.nimikin.TelegramBot.constant.Constants.*;

@Component
public class StartCommandHandler extends UserRequestHandler {
    private final TelegramService telegramService;
    private final KeyboardHelper keyboardHelper;

    public StartCommandHandler(TelegramService telegramService, KeyboardHelper keyboardHelper){
        this.telegramService = telegramService;
        this.keyboardHelper = keyboardHelper;
    }
    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return isCommand(userRequest.getUpdate(), START);
    }

    @Override
    public void handle(UserRequest userRequest) {
        ReplyKeyboard replyKeyboard = keyboardHelper.buildMainMenu();
        telegramService.sendMessage(userRequest.getChatId(), START_TEXT, replyKeyboard);
        telegramService.sendMessage(userRequest.getChatId(), CHOOSE_BELOW);
    }

    @Override
    public boolean isGlobal() {
        return true;
    }
}
