package org.example;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import com.pengrad.telegrambot.UpdatesListener;

import java.util.Random;

public class FutureWhisperBot {
    private static final String[] Templates = {
            "Сегодня тебя ждёт {adjective} {noun}. Как тебе такое?",
            "Завтра обещает быть {adjective}. И это только начало {noun}.",
            "Не надейся, что {noun} пройдёт {adjective}. Это будет весело... для кого-то.",
            "Твоё будущее такое {adjective}, что даже {noun} завидует.",
            "{noun} уже почти с тобой — и, конечно, это будет {adjective}.",
            "Готовься к {adjective} {noun}, это твоё главное событие недели.",
            "Ну что ж, {adjective} {noun} ждёт только тебя. Радуйся!",
            "{noun} будет настолько {adjective}, что ты пожалеешь, что это не шутка."
    };

    private static final String[] Adjectives = {
            "злоебучий", "абсурдный", "неловкий", "странный", "отвратительный",
            "душераздирающий", "анальный", "токсичный", "грязный", "непредсказуемый", "хуёвый"
    };

    private static final String[] Nouns = {
            "сюрприз", "поворот событий", "выбор", "провал", "дискомфорт",
            "интрига", "случай", "приключение", "ошибка", "урок", "безумие"
    };

    public static void main(String[] args) {
        String token = "7472347197:AAHfGn3gqq28JXr83OgiCyUHMtr2H8KtEa8";
        TelegramBot bot = new TelegramBot(token);

        bot.setUpdatesListener(updates -> {
            for (Update update : updates) {
                if (update.message() != null && update.message().text() != null) {
                    long chatId = update.message().chat().id();
                    String userMessage = update.message().text();

                    // Проверяем, что пользователь ввёл команду /predict
                    if (userMessage.equalsIgnoreCase("/predict")) {
                        // Генерируем предсказание
                        String prediction = generatePrediction();

                        // Отправляем предсказание
                        SendMessage request = new SendMessage(chatId, prediction);
                        SendResponse response = bot.execute(request);

                        if (response.isOk()) {
                            System.out.println("Сообщение отправлено успешно");
                        } else {
                            System.out.println("Ошибка отправки сообщения");
                        }
                    } else {
                        // Отправляем ответ, если команда не распознана
                        SendMessage request = new SendMessage(chatId, "Введите команду /predict для получения предсказания.");
                        bot.execute(request);
                    }
                }
            }
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    // Метод для генерации случайного предсказания
    private static String generatePrediction() {
        Random random = new Random();

        // Случайный выбор шаблона, прилагательного и существительного
        String template = Templates[random.nextInt(Templates.length)];
        String adjective = Adjectives[random.nextInt(Adjectives.length)];
        String noun = Nouns[random.nextInt(Nouns.length)];

        // Заменяем {adjective} и {noun} в шаблоне
        return template.replace("{adjective}", adjective).replace("{noun}", noun);
    }
}