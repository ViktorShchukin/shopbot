package ru.aquamarina.api.bot.telegram;


import jakarta.inject.Singleton;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.aquamarina.util.Result;
import ru.aquamarina.model.entity.User;
import ru.aquamarina.model.error.Error;
import ru.aquamarina.model.error.NotSupportedUpdateType;
import ru.aquamarina.service.TelegramInfoService;

@Singleton
public class TelegramUtils {

    public final TelegramInfoService telegramInfoService;

    public TelegramUtils(TelegramInfoService telegramInfoService) {
        this.telegramInfoService = telegramInfoService;
    }

    public Result<User, Error> getUser(Update update) {
        return extractTelegramUserId(update)
                .map(telegramInfoService::getUserByTelegramId);
    }

    private Result<Long, Error> extractTelegramUserId(Update update) {
        if (update.hasMessage()) {
            return Result.ok(update.getMessage().getFrom().getId());
        }
        if (update.hasCallbackQuery()) {
            return Result.ok(update.getCallbackQuery().getFrom().getId());
        }
        return Result.error(new NotSupportedUpdateType(update));
    }
}
