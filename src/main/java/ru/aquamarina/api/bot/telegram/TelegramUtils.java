package ru.aquamarina.api.bot.telegram;


import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.aquamarina.api.dto.ProductRowDto;
import ru.aquamarina.model.entity.Product;
import ru.aquamarina.model.error.UserNotFound;
import ru.aquamarina.util.Result;
import ru.aquamarina.model.entity.User;
import ru.aquamarina.model.error.Error;
import ru.aquamarina.model.error.NotSupportedUpdateType;
import ru.aquamarina.service.TelegramInfoService;

import java.util.List;
import java.util.stream.Stream;

@Singleton
public class TelegramUtils {

    private final Logger log = LoggerFactory.getLogger(TelegramUtils.class);

    private final TelegramInfoService telegramInfoService;

    public TelegramUtils(TelegramInfoService telegramInfoService) {
        this.telegramInfoService = telegramInfoService;
    }

    // | productName | quantity | totalSumByThisPosition |
    private static final String PRODUCT_ROW_TABLE_TEMPLATE = "%s  %s  %s";
    private static final String PRODUCT_TABLE_TEMPLATE = "<pre>%s</pre>";

    private static final String NAME_OF_PRODUCT = "Название";
    private static final String QUANTITY_OF_PRODUCT = "Кол-во";
    private static final String TOTAL_SUM_OF_PRODUCT = "Итог";

    private static final String QUANTITY_TEMPLATE = "%d*%.2f";
    private static final String TOTAL_SUM_TEMPLATE = "%.2f руб";

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
                                dto.username(),
                                null))
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


    // todo move this dto
    record TelegramUserDto(Long telegramId, String firstName, String lastName, String username) {
    }

    TelegramUserDto mapToDto(org.telegram.telegrambots.meta.api.objects.User user) {
        return new TelegramUserDto(user.getId(), user.getFirstName(), user.getLastName(), user.getUserName());
    }

    private static String getProductTableHeader() {
        return PRODUCT_ROW_TABLE_TEMPLATE
                .formatted(NAME_OF_PRODUCT, QUANTITY_OF_PRODUCT, TOTAL_SUM_OF_PRODUCT);
    }

    public static String getProductTable(List<ProductRowDto> products) {
        long maxProductNameLength = products.stream()
                .map(ProductRowDto::product)
                .map(Product::getShortName)
                .mapToInt(String::length)
                .map(i -> i + 1)
                .max()
                .orElseGet(() -> 0);

        long maxQuantityLength = products.stream()
                .map(TelegramUtils::quantityToString)
                .mapToInt(String::length)
                .map(i -> i + 5)
                .max()
                .orElseGet(() -> 0);

        long maxTotalSumLength = products.stream()
                .map(TelegramUtils::totalSumToString)
                .mapToInt(String::length)
                .map(i -> i + 5)
                .max()
                .orElseGet(() -> 0);

        List<String> productRowStringList = products.stream()
                .map(productRowDto ->
                        getProductTableRow(productRowDto, maxProductNameLength, maxQuantityLength, maxTotalSumLength))
                .map(rowStr -> rowStr.concat("\n"))
                .toList();

        String productTableHeader = TelegramUtils.getProductTableHeader() + "\n";
        String productTable = productRowStringList.stream()
                .reduce("", String::concat);

        return PRODUCT_TABLE_TEMPLATE.formatted(productTable);
    }

    private static String getProductTableRow(ProductRowDto productRowDto, long maxName, long maxQuantity, long maxTotalSum) {
        String quantityStr = quantityToString(productRowDto);
        String totalSumStr = totalSumToString(productRowDto);
        return getProductRow(
                productRowDto.product().getShortName(),
                quantityStr,
                totalSumStr,
                maxName,
                maxQuantity,
                maxTotalSum
        );
    }

    private static String getProductRow(String productName, String quantity, String totalSum, long maxName, long maxQuantity, long maxTotalSum) {
        String nameNormalized = normalizeByPadding(productName, maxName);
        String quantityNormalized = normalizeByPadding(quantity, maxQuantity);
        String totalSumNormalized = normalizeByPadding(totalSum, maxTotalSum);
        return PRODUCT_ROW_TABLE_TEMPLATE.formatted(nameNormalized, quantityNormalized, totalSumNormalized);
    }

    private static String normalizeByPadding(String value, long maxLineLength) {
        String template = "%-" + maxLineLength + "s";
        return template.formatted(value);
    }

    private static String quantityToString(ProductRowDto productRowDto) {
        double costInRub = (double) productRowDto.product().getCost() / 100;
        return QUANTITY_TEMPLATE.formatted(productRowDto.quantity(), costInRub);
    }

    private static String totalSumToString(ProductRowDto productRowDto) {
        double totalSumInRub = (double) productRowDto.product().getCost() * productRowDto.quantity() / 100;
        return TOTAL_SUM_TEMPLATE.formatted(totalSumInRub);
    }
}
