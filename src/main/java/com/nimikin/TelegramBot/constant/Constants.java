package com.nimikin.TelegramBot.constant;

public class Constants {
    public static final String START = "/start";
    public static final String START_TEXT = "Здравствуйте!\n" +
            "Если вы хотите сделать заказ, нажмите кнопку \"Сделать заказ\".\n" +
            "Если вы у нас впервые, пожалуйста, нажмите кнопку \"Помощь\"," +
            " там есть ответы на все ваши вопросы)";
    public static final String[] SCHEMES = {"http", "https"};
    public static final String[] VALID_SIZES = {"XS", "S", "M", "L", "XL", "XXL", "XXXL", ""};
    public static final String MAKE_ORDER = "Сделать заказ";
    public static final String CHOOSE_BELOW = "Выберите пункт из меню ниже ⬇";
    public static final String WRONG_LINK = "Неправильная ссылка. Пожалуйста убедитесь, что правильно скопировали ссылку";
    public static final String LINK_TEXT = "Пожалуйста, введите ссылку на желаемый товар: ";
    public static final String BTN_CANCEL = "Отменить";
    public static final String CANCEL_TEXT = "Вы отменили создание заказа.\n" +
            "Выберите пункт из меню ниже ⬇";
    public static final String BTN_HELP = "Помощь";
    public static final String HELP_TEXT = "Итак, вы хотите сделать заказ. Для начала вам нужно будет скопировать ссылку на желаемую вещь.\n" +
            "Она находится наверху вашего экрана смартфона / компьютера, как на картинке: \n" +
            "После того, как вы укажете ссылку, вам нужно указать размер желаемой вещи.\n" +
            "Размер нужно указывать как на сайте: буквенно (xs, s, m, l, xl ...)\n" +
            "числом от 1 до 150.\n" +
            "Последнее, что вам надо указать - это цвет желаемой вещи.\n" +
            "Вот и всё!";
    public static final String USER_SIZE = "Хорошо, теперь введите размер желаемого товара.\n" +
            "Размер можно указывать буквенно (xs, s, m, l, xl ...)\n" +
            "или номером от 1 до 150: ";
    public static final String WRONG_SIZE = "Такого размера не существует!\n" +
            "Пожалуйста, ведите размер ещё раз: ";
    public static final String USER_COLOR = "Отлично! Теперь, ведите цвет так, как написано на сайте: ";
    public static final String ADDITIONAL_INFO = "Желаете дополнить свой заказ дополнительной информацией?\n" +
            "Если нет, напишите прочерк \"-\"";
    public static final String SUCCESSFUL_ORDER = "Спасибо, ваш заказ принят!";
}
