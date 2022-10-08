package com.nimikin.TelegramBot.handler.impl;

import com.nimikin.TelegramBot.enums.ConversationState;
import com.nimikin.TelegramBot.handler.UserRequestHandler;
import com.nimikin.TelegramBot.helper.KeyboardHelper;
import com.nimikin.TelegramBot.model.UserRequest;
import com.nimikin.TelegramBot.model.UserSession;
import com.nimikin.TelegramBot.service.EmailSenderService;
import com.nimikin.TelegramBot.service.TelegramService;
import com.nimikin.TelegramBot.service.UserSessionService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import static com.nimikin.TelegramBot.constant.Constants.*;

@Component
public class SizeEnteredHelper extends UserRequestHandler {
    private final EmailSenderService senderService;
    private final TelegramService telegramService;
    private final KeyboardHelper keyboardHelper;
    private final UserSessionService userSessionService;

    public SizeEnteredHelper(EmailSenderService senderService, TelegramService telegramService, KeyboardHelper keyboardHelper, UserSessionService userSessionService) {
        this.senderService = senderService;
        this.telegramService = telegramService;
        this.keyboardHelper = keyboardHelper;
        this.userSessionService = userSessionService;
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return isTextMessage(userRequest.getUpdate()) && ConversationState.WAITING_FOR_SIZE.equals(userRequest.getUserSession().getState());
    }

    @Override
    public void handle(UserRequest userRequest) {
        ReplyKeyboardMarkup replyKeyboardMarkup = keyboardHelper.buildMenuWithCancel();
        String messageText = userRequest.getUpdate().getMessage().getText();

        if (!sizeValidation(messageText)) {
            telegramService.sendMessage(userRequest.getChatId(), WRONG_SIZE, replyKeyboardMarkup);
        } else {
            ordersNextStage(userRequest, replyKeyboardMarkup, messageText);
        }
    }

    private void ordersNextStage(UserRequest userRequest, ReplyKeyboardMarkup replyKeyboardMarkup, String messageText) {
        senderService.setSize("Размер: " + messageText);
        telegramService.sendMessage(userRequest.getChatId(), USER_COLOR, replyKeyboardMarkup);

        UserSession session = userRequest.getUserSession();
        session.setState(ConversationState.WAITING_FOR_COLOR);
        userSessionService.saveSession(userRequest.getChatId(), session);
    }

    private boolean sizeValidation(String messageText) {
        if (letterSize(messageText)) {
            return true;
        } else return numberSize(messageText);
    }

    private boolean letterSize(String userSize) {
        for (String size : VALID_SIZES) {
            if (userSize.equalsIgnoreCase(size)) {
                return true;
            }
        }
        return false;
    }

    private boolean numberSize(String userSize) {
        try {
            double size = Double.parseDouble(userSize);
            for (double i = 0; i <= 150; i += 0.5) {
                if (size == i) {
                    return true;
                }
            }
        } catch (NumberFormatException e) {
            return false;
        }
        return false;
    }

    @Override
    public boolean isGlobal() {
        return false;
    }
}
