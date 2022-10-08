package com.nimikin.TelegramBot.handler.impl;

import com.nimikin.TelegramBot.enums.ConversationState;
import com.nimikin.TelegramBot.handler.UserRequestHandler;
import com.nimikin.TelegramBot.helper.KeyboardHelper;
import com.nimikin.TelegramBot.model.UserRequest;
import com.nimikin.TelegramBot.model.UserSession;
import com.nimikin.TelegramBot.service.EmailSenderService;
import com.nimikin.TelegramBot.service.TelegramService;
import com.nimikin.TelegramBot.service.UserSessionService;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import static com.nimikin.TelegramBot.constant.Constants.*;

@Component
public class UrlEnteredHandler extends UserRequestHandler {
    private final EmailSenderService senderService;
    private final TelegramService telegramService;
    private final KeyboardHelper keyboardHelper;
    private final UserSessionService userSessionService;
    private final UrlValidator urlValidator = new UrlValidator(SCHEMES);

    public UrlEnteredHandler(EmailSenderService senderService, TelegramService telegramService, KeyboardHelper keyboardHelper, UserSessionService userSessionService) {
        this.senderService = senderService;
        this.telegramService = telegramService;
        this.keyboardHelper = keyboardHelper;
        this.userSessionService = userSessionService;
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return isTextMessage(userRequest.getUpdate()) && ConversationState.WAITING_FOR_URL.equals(userRequest.getUserSession().getState());
    }

    @Override
    public void handle(UserRequest userRequest) {
        ReplyKeyboardMarkup replyKeyboardMarkup = keyboardHelper.buildMenuWithCancel();
        String messageText = userRequest.getUpdate().getMessage().getText();

        if (!urlValidation(messageText)) {
            telegramService.sendMessage(userRequest.getChatId(), WRONG_LINK, replyKeyboardMarkup);
        } else {
            saveLinkAndGoToNextState(userRequest, replyKeyboardMarkup, messageText);
        }
    }

    private void saveLinkAndGoToNextState(UserRequest userRequest, ReplyKeyboardMarkup replyKeyboardMarkup, String messageText) {
        senderService.setLink("Ссылка: " + messageText);
        telegramService.sendMessage(userRequest.getChatId(), USER_SIZE, replyKeyboardMarkup);

        UserSession session = userRequest.getUserSession();
        session.setState(ConversationState.WAITING_FOR_SIZE);
        userSessionService.saveSession(userRequest.getChatId(), session);
    }

    private boolean urlValidation(String messageText) {
        return urlValidator.isValid(messageText);
    }

    @Override
    public boolean isGlobal() {
        return false;
    }
}
