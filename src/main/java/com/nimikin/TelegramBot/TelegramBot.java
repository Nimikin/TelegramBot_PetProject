package com.nimikin.TelegramBot;

import com.nimikin.TelegramBot.config.BotConfig;
import com.nimikin.TelegramBot.model.User;
import com.nimikin.TelegramBot.model.UserRepository;
import com.nimikin.TelegramBot.model.UserRequest;
import com.nimikin.TelegramBot.model.UserSession;
import com.nimikin.TelegramBot.service.UserSessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.sql.Timestamp;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {
    @Autowired
    private UserRepository userRepository;
    final BotConfig config;

    private final Dispatcher dispatcher;
    private final UserSessionService userSessionService;

    public TelegramBot(BotConfig config, Dispatcher dispatcher, UserSessionService userSessionService) {
        this.config = config;
        this.dispatcher = dispatcher;
        this.userSessionService = userSessionService;
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String textFromUser = update.getMessage().getText();
            Long userId = update.getMessage().getFrom().getId();
            String userFirstName = update.getMessage().getFrom().getFirstName();

            registerUser(update.getMessage());

            log.info("[{}, {}] : {}", userId, userFirstName, textFromUser);

            Long chatId = update.getMessage().getChatId();
            UserSession session = userSessionService.getSession(chatId);

            UserRequest userRequest = UserRequest
                    .builder()
                    .update(update)
                    .userSession(session)
                    .chatId(chatId)
                    .build();

            boolean dispatched = dispatcher.dispatch(userRequest);

            if (!dispatched){
                log.warn("Unexpected update from user");
            }
        }else {
            log.warn("Unexpected update from user");
        }
    }

    private void registerUser(Message message) {
        if (userRepository.findById(message.getChatId()).isEmpty()){
            var chatId = message.getChatId();
            var chat = message.getChat();

            User user = createNewUser(chatId, chat);
            userRepository.save(user);
            log.info("user has been saved: " + user);
        }
    }

    private User createNewUser(Long chatId, Chat chat) {
        User user = new User();
        user.setChatId(chatId);
        user.setFirstName(chat.getFirstName());
        user.setLastName(chat.getLastName());
        user.setRegisteredAt(new Timestamp(System.currentTimeMillis()));
        return user;
    }
}
