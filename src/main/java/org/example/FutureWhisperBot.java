package org.example;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import com.pengrad.telegrambot.UpdatesListener;

import java.util.List;
import java.util.Random;

public class FutureWhisperBot {
    private static final String[] Templates = {
            "Сегодня тебя ждёт {adjective} {noun}. Как тебе такое?",
            "Готовься к {adjective} {noun}, это твоё главное событие недели.",
            "Ну что ж, {adjective} {noun} ждёт только тебя. Радуйся!",
            "Завтра тебя ждёт {adjective} {noun}.",
            "Сегодня тебя найдёт {adjective} {noun}.",
            "Скоро тебя встретит {adjective} {noun}.",
            "На горизонте появится {adjective} {noun}.",
            "Неожиданно ты получишь {adjective} {noun}."
    };

    // Существительные мужского рода
    private static final String[] masculineNouns = {
            "сюрприз", "поворот событий", "выбор", "провал", "дискомфорт", "урок", "колупатель", "чухан"
    };

    // Существительные женского рода
    private static final String[] feminineNouns = {
            "интрига", "ошибка", "тревожность"
    };

    // Существительные среднего рода
    private static final String[] neuterNouns = {
            "безумие", "приключение"
    };

    // Прилагательные для мужского рода
    private static final String[] masculineAdjectives = {
            "злоебучий", "абсурдный", "неловкий", "странный", "отвратительный", "волшебный",
            "душераздирающий", "анальный", "токсичный", "грязный", "непредсказуемый", "хуёвый"
    };

    // Прилагательные для женского рода
    private static final String[] feminineAdjectives = {
            "злоебучая", "абсурдная", "неловкая", "странная", "отвратительная", "волшебная",
            "душераздирающая", "анальная", "токсичная", "грязная", "непредсказуемая", "хуёвая"
    };

    // Прилагательные для среднего рода
    private static final String[] neuterAdjectives = {
            "злоебучее", "абсурдное", "неловкое", "странное", "отвратительное", "волшебное",
            "душераздирающее", "анальное", "токсичное", "грязное", "непредсказуемое", "хуёвое"
    };

    public static void main(String[] args) {
        String token = "7472347197:AAHfGn3gqq28JXr83OgiCyUHMtr2H8KtEa8";
        TelegramBot bot = new TelegramBot(token);

        // Получаем накопленные обновления
        List<Update> updates = bot.execute(new GetUpdates().limit(100).offset(0)).updates();

        // Устанавливаем начальный offset на одно значение больше максимального update_id
        int offset = updates.isEmpty() ? 0 : updates.getLast().updateId() + 1;

        // Запускаем основной цикл обработки обновлений
        while (true) {
            updates = bot.execute(new GetUpdates().limit(100).offset(offset)).updates();

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
                offset = update.updateId() + 1; // Обновляем offset
            }
        }
    }
    // Метод для генерации случайного предсказания
    private static String generatePrediction() {
        Random random = new Random();
        String template = Templates[random.nextInt(Templates.length)];

        // Определяем случайный род (0 - мужской, 1 - женский, 2 - средний)
        int gender = random.nextInt(3);

        String noun;
        String adjective;

        // Подбираем существительное и прилагательное в зависимости от рода
        if (gender == 0) {
            noun = masculineNouns[random.nextInt(masculineNouns.length)];
            adjective = masculineAdjectives[random.nextInt(masculineAdjectives.length)];
        } else if (gender == 1) {
            noun = feminineNouns[random.nextInt(feminineNouns.length)];
            adjective = feminineAdjectives[random.nextInt(feminineAdjectives.length)];
        } else {
            noun = neuterNouns[random.nextInt(neuterNouns.length)];
            adjective = neuterAdjectives[random.nextInt(neuterAdjectives.length)];
        }

        return template.replace("{adjective}", adjective).replace("{noun}", noun);
    }
}