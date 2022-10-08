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

import static com.nimikin.TelegramBot.constant.Constants.ADDITIONAL_INFO;

@Component
public class ColorEnteredHandler extends UserRequestHandler {
    private final EmailSenderService senderService;
    private  final TelegramService telegramService;
    private final KeyboardHelper keyboardHelper;
    private  final UserSessionService userSessionService;

    public ColorEnteredHandler(EmailSenderService senderService, TelegramService telegramService, KeyboardHelper keyboardHelper, UserSessionService userSessionService){
        this.senderService = senderService;
        this.telegramService = telegramService;
        this.keyboardHelper = keyboardHelper;
        this.userSessionService = userSessionService;
    }
    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return isTextMessage(userRequest.getUpdate())
                && ConversationState.WAITING_FOR_COLOR.equals(userRequest.getUserSession().getState());
    }

    @Override
    public void handle(UserRequest userRequest) {
        ReplyKeyboardMarkup replyKeyboardMarkup = keyboardHelper.buildMenuWithCancel();
        String messageText = userRequest.getUpdate().getMessage().getText();
        senderService.setColor("Цвет: " + messageText);
        telegramService.sendMessage(userRequest.getChatId(), ADDITIONAL_INFO, replyKeyboardMarkup);

        UserSession session =userRequest.getUserSession();
        session.setState(ConversationState.WAITING_FOR_ADDITIONAL_INFO);
        userSessionService.saveSession(userRequest.getChatId(), session);
    }

    @Override
    public boolean isGlobal() {
        return false;
    }
}
