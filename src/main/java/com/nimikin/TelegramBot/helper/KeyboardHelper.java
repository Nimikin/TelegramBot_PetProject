package com.nimikin.TelegramBot.helper;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

import static com.nimikin.TelegramBot.constant.Constants.*;

@Component
public class KeyboardHelper {

    public ReplyKeyboardMarkup buildMainMenu() {
        KeyboardRow keyboardRow1 = new KeyboardRow();
        keyboardRow1.add(MAKE_ORDER);

        KeyboardRow keyboardRow2 = new KeyboardRow();
        keyboardRow2.add(BTN_HELP);

        return ReplyKeyboardMarkup.builder()
                .keyboard(List.of(keyboardRow1, keyboardRow2))
                .selective(true)
                .resizeKeyboard(true)
                .oneTimeKeyboard(false)
                .build();
    }

    public ReplyKeyboardMarkup buildMenuWithCancel() {
        KeyboardRow keyboardRow1 = new KeyboardRow();
        keyboardRow1.add(BTN_CANCEL);

        return ReplyKeyboardMarkup.builder()
                .keyboard(List.of(keyboardRow1))
                .selective(true)
                .resizeKeyboard(true)
                .oneTimeKeyboard(false)
                .build();
    }
}
