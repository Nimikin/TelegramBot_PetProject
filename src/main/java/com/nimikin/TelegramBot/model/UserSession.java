package com.nimikin.TelegramBot.model;

import com.nimikin.TelegramBot.enums.ConversationState;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserSession {
    private Long chatId;
    private ConversationState state;
    private String text;
}
