package com.nimikin.TelegramBot.handler.impl;

import com.nimikin.TelegramBot.enums.ConversationState;
import com.nimikin.TelegramBot.handler.UserRequestHandler;
import com.nimikin.TelegramBot.helper.KeyboardHelper;
import com.nimikin.TelegramBot.model.UserRequest;
import com.nimikin.TelegramBot.model.UserSession;
import com.nimikin.TelegramBot.service.EmailSenderService;
import com.nimikin.TelegramBot.service.TelegramService;
import com.nimikin.TelegramBot.service.UserSessionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import static com.nimikin.TelegramBot.constant.Constants.SUCCESSFUL_ORDER;

@Component
public class AdditionalInformationHandler extends UserRequestHandler {
    @Value("${mail.receiver.username}")
    private String receiverEmail;
    private final EmailSenderService senderService;
    private final TelegramService telegramService;
    private final KeyboardHelper keyboardHelper;
    private final UserSessionService userSessionService;

    public AdditionalInformationHandler(EmailSenderService senderService,
                                        TelegramService telegramService,
                                        KeyboardHelper keyboardHelper,
                                        UserSessionService userSessionService) {
        this.senderService = senderService;
        this.telegramService = telegramService;
        this.keyboardHelper = keyboardHelper;
        this.userSessionService = userSessionService;
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return isTextMessage(userRequest.getUpdate())
                && ConversationState.WAITING_FOR_ADDITIONAL_INFO.equals(userRequest.getUserSession().getState());
    }

    @Override
    public void handle(UserRequest userRequest) {
        ReplyKeyboardMarkup replyKeyboardMarkup = keyboardHelper.buildMainMenu();
        mailingOrder(userRequest);
        telegramService.sendMessage(userRequest.getChatId(), SUCCESSFUL_ORDER, replyKeyboardMarkup);

        UserSession session = userRequest.getUserSession();
        session.setState(ConversationState.CONVERSATION_STARTED);
        userSessionService.saveSession(userRequest.getChatId(), session);
    }

    private void mailingOrder(UserRequest userRequest) {
        String messageText = userRequest.getUpdate().getMessage().getText();
        String username = userRequest.getUpdate().getMessage().getFrom().getUserName();
        String userFirstName = userRequest.getUpdate().getMessage().getFrom().getFirstName();
        String userLastName = userRequest.getUpdate().getMessage().getFrom().getLastName();

        senderService.setAdditionalInfo("Доп.информация: " + messageText);
        senderService.sendEmail(receiverEmail, "Заказ от " + username +
                "(" + userFirstName + " " + userLastName + ")", senderService.messageToMail());
    }

    @Override
    public boolean isGlobal() {
        return false;
    }
}
