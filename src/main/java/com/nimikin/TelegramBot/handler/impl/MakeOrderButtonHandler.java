package com.nimikin.TelegramBot.handler.impl;

import com.nimikin.TelegramBot.enums.ConversationState;
import com.nimikin.TelegramBot.handler.UserRequestHandler;
import com.nimikin.TelegramBot.helper.KeyboardHelper;
import com.nimikin.TelegramBot.model.UserRequest;
import com.nimikin.TelegramBot.model.UserSession;
import com.nimikin.TelegramBot.service.TelegramService;
import com.nimikin.TelegramBot.service.UserSessionService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import static com.nimikin.TelegramBot.constant.Constants.*;
@Component
public class MakeOrderButtonHandler extends UserRequestHandler {
    private final TelegramService telegramService;
    private final KeyboardHelper keyboardHelper;
    private final UserSessionService userSessionService;

    public MakeOrderButtonHandler(TelegramService telegramService, KeyboardHelper keyboardHelper, UserSessionService userSessionService) {
        this.telegramService = telegramService;
        this.keyboardHelper = keyboardHelper;
        this.userSessionService = userSessionService;
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return isTextMessage(userRequest.getUpdate(), MAKE_ORDER);
    }

    @Override
    public void handle(UserRequest userRequest) {
        ReplyKeyboardMarkup replyKeyboardMarkup = keyboardHelper.buildMenuWithCancel();
        telegramService.sendMessage(userRequest.getChatId(), LINK_TEXT, replyKeyboardMarkup);
        
        UserSession userSession = userRequest.getUserSession();
        userSession.setState(ConversationState.WAITING_FOR_URL);
        userSessionService.saveSession(userSession.getChatId(), userSession);
    }

    @Override
    public boolean isGlobal() {
        return true;
    }
}
