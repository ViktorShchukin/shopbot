package ru.aquamarina.api.bot.telegram;


import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.aquamarina.model.error.UserNotFound;
import ru.aquamarina.util.Result;
import ru.aquamarina.model.entity.User;
import ru.aquamarina.model.error.Error;
import ru.aquamarina.model.error.NotSupportedUpdateType;
import ru.aquamarina.service.TelegramInfoService;

@Singleton
public class TelegramUtils {

    private final Logger log = LoggerFactory.getLogger(TelegramUtils.class);

    private final TelegramInfoService telegramInfoService;

    public TelegramUtils(TelegramInfoService telegramInfoService) {
        this.telegramInfoService = telegramInfoService;
    }

    /**
     * @param update
     * @return {@link NotSupportedUpdateType} {@link UserNotFound}
     */
    public Result<User, Error> getUser(Update update) {
        Result<org.telegram.telegrambots.meta.api.objects.User, Error> telegramUser = extractTelegramUser(update);
        return telegramUser
                .mapValue(telUser -> telUser.getId())
                .map(telegramInfoService::getOrCrateUserByTelegramId)
                .map(user -> telegramUser.mapValue(this::mapToDto)
                        .map(dto -> telegramInfoService.update(
                                dto.telegramId(),
                                dto.firstName(),
                                dto.lastName(),
                                dto.username()))
                        .map(info -> Result.ok(user))
                );
    }

    /**
     * telegram userId can be used as chat id to send message
     *
     * @param update
     * @return {@link NotSupportedUpdateType}
     */
    public static Result<org.telegram.telegrambots.meta.api.objects.User, Error> extractTelegramUser(Update update) {
        if (update.hasMessage()) {
            return Result.ok(update.getMessage().getFrom());
        }
        if (update.hasCallbackQuery()) {
            return Result.ok(update.getCallbackQuery().getFrom());
        }
        return Result.error(new NotSupportedUpdateType(update));
    }

    /**
     * telegram userId can be used as chat id to send message
     *
     * @param update
     * @return {@link NotSupportedUpdateType}
     */
    public static Result<Long, Error> extractTelegramUserId(Update update) {
        if (update.hasMessage()) {
            return Result.ok(update.getMessage().getFrom().getId());
        }
        if (update.hasCallbackQuery()) {
            return Result.ok(update.getCallbackQuery().getFrom().getId());
        }
        return Result.error(new NotSupportedUpdateType(update));
    }


    record TelegramUserDto(Long telegramId, String firstName, String lastName, String username) {
    }

    TelegramUserDto mapToDto(org.telegram.telegrambots.meta.api.objects.User user) {
        return new TelegramUserDto(user.getId(), user.getFirstName(), user.getLastName(), user.getUserName());
    }
}
