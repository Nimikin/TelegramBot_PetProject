package com.nimikin.TelegramBot.handler.impl;

import com.nimikin.TelegramBot.enums.ConversationState;
import com.nimikin.TelegramBot.handler.UserRequestHandler;
import com.nimikin.TelegramBot.helper.KeyboardHelper;
import com.nimikin.TelegramBot.model.UserRequest;
import com.nimikin.TelegramBot.model.UserSession;
import com.nimikin.TelegramBot.service.TelegramService;
import com.nimikin.TelegramBot.service.UserSessionService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import static com.nimikin.TelegramBot.constant.Constants.*;

@Component
public class CancelButtonHandler extends UserRequestHandler {
    private final TelegramService telegramService;
    private final KeyboardHelper keyboardHelper;
    private final UserSessionService userSessionService;

    public CancelButtonHandler(TelegramService telegramService, KeyboardHelper keyboardHelper, UserSessionService userSessionService){
        this.telegramService = telegramService;
        this.keyboardHelper = keyboardHelper;
        this.userSessionService = userSessionService;
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return isTextMessage(userRequest.getUpdate(), BTN_CANCEL);
    }

    @Override
    public void handle(UserRequest userRequest) {
        ReplyKeyboard replyKeyboard = keyboardHelper.buildMainMenu();
        telegramService.sendMessage(userRequest.getChatId(), CANCEL_TEXT, replyKeyboard);

        UserSession userSession = userRequest.getUserSession();
        userSession.setState(ConversationState.CONVERSATION_STARTED);
        userSessionService.saveSession(userSession.getChatId(), userSession);
    }

    @Override
    public boolean isGlobal() {
        return true;
    }
}
