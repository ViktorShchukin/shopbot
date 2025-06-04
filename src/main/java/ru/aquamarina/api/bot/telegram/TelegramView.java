package ru.aquamarina.api.bot.telegram;

import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.aquamarina.api.bot.View;
import ru.aquamarina.api.dto.ProductRowDto;
import ru.aquamarina.api.mapper.ProductMapper;
import ru.aquamarina.fsm.form.*;
import ru.aquamarina.model.command.*;
import ru.aquamarina.model.entity.Folder;
import ru.aquamarina.model.entity.Product;
import ru.aquamarina.model.error.Error;
import ru.aquamarina.service.ProductService;
import ru.aquamarina.util.PathUtil;
import ru.aquamarina.util.ResultError;
import ru.aquamarina.util.ResultOk;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record TelegramView(OkHttpTelegramClient client, Update update, ProductService productService) implements View {

    private static final Logger log = LoggerFactory.getLogger(TelegramView.class);
    private static final ProductMapper productMapper = Mappers.getMapper(ProductMapper.class);

    // | productName | quantity | totalSumByThisPosition |
    private static final String PRODUCT_ROW_TABLE_TEMPLATE = " %s  %s  %s ";
    private static final String NAME_OF_PRODUCT = "Название";
    private static final String QUANTITY_OF_PRODUCT = "Кол-во";
    private static final String TOTAL_SUM_OF_PRODUCT = "Итог";

    private static final String QUANTITY_TEMPLATE = "%dx%.2f";
    private static final String TOTAL_SUM_TEMPLATE = "%.2f";

    @Override
    public void drawAboutForm(AboutForm form) {
        String chatId;
        switch (TelegramUtils.extractTelegramUserId(update)) {
            case ResultOk<Long, Error> ok -> chatId = ok.unwrap().toString();
            case ResultError<Long, Error> err -> {
                draw(err.err());
                return;
            }
        }
        InlineKeyboardRow keyboardRow = new InlineKeyboardRow(
                getButton(new IndexCmd(null))
        );

        String messageText = "Я есть магазин";

        var keyBoard = InlineKeyboardMarkup.builder()
                .keyboardRow(keyboardRow)
                .build();
        Integer messageId = update.getCallbackQuery().getMessage().getMessageId();
        AnswerCallbackQuery close = AnswerCallbackQuery.builder()
                .callbackQueryId(update.getCallbackQuery().getId())
                .build();
        EditMessageText message = EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text(messageText)
                .build();
        EditMessageReplyMarkup replyMarkup = EditMessageReplyMarkup.builder()
                .chatId(chatId)
                .messageId(messageId)
                .replyMarkup(keyBoard)
                .build();
        closeQueryAndRewriteMessage(close, message, replyMarkup);
    }

    @Override
    public void drawIndexForm(IndexForm form) {
        String chatId;
        switch (TelegramUtils.extractTelegramUserId(update)) {
            case ResultOk<Long, Error> ok -> chatId = ok.unwrap().toString();
            case ResultError<Long, Error> err -> {
                draw(err.err());
                return;
            }
        }
        InlineKeyboardRow keyboardRow = new InlineKeyboardRow(
                getButton(new AboutCmd(null)),
                getButton(new CatalogCmd(null))
        );

        InlineKeyboardRow keyboardRow1 = new InlineKeyboardRow(
                getButton(new ForWholesalerCmd(null)),
                getButton(new PayAndDeliveryCmd(null))
        );

        String messageText = "Привет. Чего желаете";

        var keyBoard = InlineKeyboardMarkup.builder()
                .keyboard(List.of(keyboardRow, keyboardRow1))
                .build();
        if (update.hasMessage() && update.getMessage().getText().equals("/start")) {
            SendMessage message = SendMessage.builder()
                    .chatId(chatId)
                    .text(messageText)
                    .replyMarkup(keyBoard)
                    .build();
            sendMessage(message);
            return;
        }

        Integer messageId = update.getCallbackQuery().getMessage().getMessageId();
        AnswerCallbackQuery close = AnswerCallbackQuery.builder()
                .callbackQueryId(update.getCallbackQuery().getId())
                .build();
        EditMessageText message = EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text(messageText)
                .build();
        EditMessageReplyMarkup replyMarkup = EditMessageReplyMarkup.builder()
                .chatId(chatId)
                .messageId(messageId)
                .replyMarkup(keyBoard)
                .build();
        closeQueryAndRewriteMessage(close, message, replyMarkup);
    }

    @Override
    public void drawCatalogForm(CatalogForm form) {
        String chatId;
        switch (TelegramUtils.extractTelegramUserId(update)) {
            case ResultOk<Long, Error> ok -> chatId = ok.unwrap().toString();
            case ResultError<Long, Error> err -> {
                draw(err.err());
                return;
            }
        }
        List<Command> productInFolder = form.products().stream()
                .map(product -> new ProductAboutCmd(null, product.getId()))
                .collect(Collectors.toList());
        List<Command> folderInFolder = form.folders().stream()
                .map(Folder::path)
                .map(pth -> new FolderCmd(null, pth))
                .collect(Collectors.toList());
        List<Command> commands;
        if (form.path().equals("/")) {
            commands = List.of(
                    new BasketCmd(null),
                    new IndexCmd(null)
            );
        } else {
            commands = List.of(
                    new BasketCmd(null),
                    new CatalogCmd(null),
                    new IndexCmd(null)
            );
        }

        List<InlineKeyboardRow> keyboardRowList = new ArrayList<>();
        Stream.of(productInFolder, folderInFolder, commands)
                .flatMap(Collection::stream)
                .forEach(command -> {
                    InlineKeyboardRow keyboardRow = new InlineKeyboardRow();
                    keyboardRow.add(getButton(command));
                    keyboardRowList.add(keyboardRow);
                });

        String messageText = "Выберете нужный товар";

        var keyBoard = InlineKeyboardMarkup.builder()
                .keyboard(keyboardRowList)
                .build();
        Integer messageId = update.getCallbackQuery().getMessage().getMessageId();
        AnswerCallbackQuery close = AnswerCallbackQuery.builder()
                .callbackQueryId(update.getCallbackQuery().getId())
                .build();
        EditMessageText message = EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text(messageText)
                .build();
        EditMessageReplyMarkup replyMarkup = EditMessageReplyMarkup.builder()
                .chatId(chatId)
                .messageId(messageId)
                .replyMarkup(keyBoard)
                .build();
        closeQueryAndRewriteMessage(close, message, replyMarkup);
    }

    @Override
    public void drawProductAboutForm(ProductAboutForm form) {
        String chatId;
        switch (TelegramUtils.extractTelegramUserId(update)) {
            case ResultOk<Long, Error> ok -> chatId = ok.unwrap().toString();
            case ResultError<Long, Error> err -> {
                draw(err.err());
                return;
            }
        }
        Product product = form.product();
        List<InlineKeyboardRow> keyboardRowList = new ArrayList<>();
        keyboardRowList.add(new InlineKeyboardRow(
                getButton(String.valueOf(form.quantity()), new DoNothing(null))
        ));
        keyboardRowList.add(new InlineKeyboardRow(
                getButton(new QuantityMinusCmd(null)),
                getButton(new QuantityPlusCmd(null))
        ));
        keyboardRowList.add(new InlineKeyboardRow(
                getButton(new BasketCmd(null)),
                getButton(new InstructionCmd(null))
        ));
        keyboardRowList.add(new InlineKeyboardRow(
                getButton(new CatalogCmd(null))
        ));
        keyboardRowList.add(new InlineKeyboardRow(
                getButton(new IndexCmd(null))
        ));

        String messageText = product.getName() + "\n" + "Цена: " + (double) product.getCost() / 100 + " руб" + "\n" + "В корзине: " + form.quantity();

        var keyBoard = InlineKeyboardMarkup.builder()
                .keyboard(keyboardRowList)
                .build();
        Integer messageId = update.getCallbackQuery().getMessage().getMessageId();
        AnswerCallbackQuery close = AnswerCallbackQuery.builder()
                .callbackQueryId(update.getCallbackQuery().getId())
                .build();
        EditMessageText message = EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text(messageText)
                .build();
        EditMessageReplyMarkup replyMarkup = EditMessageReplyMarkup.builder()
                .chatId(chatId)
                .messageId(messageId)
                .replyMarkup(keyBoard)
                .build();
        closeQueryAndRewriteMessage(close, message, replyMarkup);
    }

    @Override
    public void drawOrderForm(OrderForm form) {
        String chatId;
        switch (TelegramUtils.extractTelegramUserId(update)) {
            case ResultOk<Long, Error> ok -> chatId = ok.unwrap().toString();
            case ResultError<Long, Error> err -> {
                draw(err.err());
                return;
            }
        }
        List<InlineKeyboardRow> keyboardRowList = new ArrayList<>();
        InlineKeyboardRow keyboardRow = new InlineKeyboardRow(
                getButton(new IndexCmd(null))
        );
        keyboardRowList.add(keyboardRow);


        List<ProductRowDto> products = form.rows().stream()
                .map(basketRow -> productMapper.mapTo(basketRow, productService::getById))
                // todo how to handle that product exist in basket and doesn't exist in product table???
                .map(res -> res.ok())
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        String productTable = getProductTable(products);
        String messageText = productTable + "\n" + "Сумма заказа: " + (double) form.totalCost() / 100 + "\n\nСпасибо за заказ.\nМы свяжемся с вами позже.";

        var keyBoard = InlineKeyboardMarkup.builder()
                .keyboard(keyboardRowList)
                .build();
        Integer messageId = update.getCallbackQuery().getMessage().getMessageId();
        AnswerCallbackQuery close = AnswerCallbackQuery.builder()
                .callbackQueryId(update.getCallbackQuery().getId())
                .build();
        EditMessageText message = EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text(messageText)
                .build();
        EditMessageReplyMarkup replyMarkup = EditMessageReplyMarkup.builder()
                .chatId(chatId)
                .messageId(messageId)
                .replyMarkup(keyBoard)
                .build();
        closeQueryAndRewriteMessage(close, message, replyMarkup);
    }

    @Override
    public void drawBasketForm(BasketForm form) {
        String chatId;
        switch (TelegramUtils.extractTelegramUserId(update)) {
            case ResultOk<Long, Error> ok -> chatId = ok.unwrap().toString();
            case ResultError<Long, Error> err -> {
                draw(err.err());
                return;
            }
        }
        List<InlineKeyboardRow> keyboardRowList = new ArrayList<>();
        InlineKeyboardRow keyboardRow = new InlineKeyboardRow(
                getButton(new DoOrderCmd(null)),
                getButton(new CatalogCmd(null))
        );
        InlineKeyboardRow clearBasketRow = new InlineKeyboardRow(
                getButton(new ClearBasketCmd(null))
        );
        keyboardRowList.add(keyboardRow);
        keyboardRowList.add(clearBasketRow);

        List<ProductRowDto> products = form.rows().stream()
                .map(basketRow -> productMapper.mapTo(basketRow, productService::getById))
                // todo how to handle that product exist in basket and doesn't exist in product table???
                .map(res -> res.ok())
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        String productTable = getProductTable(products);
        String messageText = "Корзина" + "\n\n" + productTable + "\n" + "Сумма: " + (double) form.totalCost() / 100;

        var keyBoard = InlineKeyboardMarkup.builder()
                .keyboard(keyboardRowList)
                .build();
        Integer messageId = update.getCallbackQuery().getMessage().getMessageId();
        AnswerCallbackQuery close = AnswerCallbackQuery.builder()
                .callbackQueryId(update.getCallbackQuery().getId())
                .build();
        EditMessageText message = EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text(messageText)
                .build();
        EditMessageReplyMarkup replyMarkup = EditMessageReplyMarkup.builder()
                .chatId(chatId)
                .messageId(messageId)
                .replyMarkup(keyBoard)
                .build();
        closeQueryAndRewriteMessage(close, message, replyMarkup);
    }

    @Override
    public void drawForWholesalerForm(ForWholesalerForm form) {
        String chatId;
        switch (TelegramUtils.extractTelegramUserId(update)) {
            case ResultOk<Long, Error> ok -> chatId = ok.unwrap().toString();
            case ResultError<Long, Error> err -> {
                draw(err.err());
                return;
            }
        }
        InlineKeyboardRow keyboardRow = new InlineKeyboardRow(
                getButton(new IndexCmd(null))
        );

        String messageText = "Оптовикам";

        var keyBoard = InlineKeyboardMarkup.builder()
                .keyboardRow(keyboardRow)
                .build();
        Integer messageId = update.getCallbackQuery().getMessage().getMessageId();
        AnswerCallbackQuery close = AnswerCallbackQuery.builder()
                .callbackQueryId(update.getCallbackQuery().getId())
                .build();
        EditMessageText message = EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text(messageText)
                .build();
        EditMessageReplyMarkup replyMarkup = EditMessageReplyMarkup.builder()
                .chatId(chatId)
                .messageId(messageId)
                .replyMarkup(keyBoard)
                .build();
        closeQueryAndRewriteMessage(close, message, replyMarkup);
    }

    @Override
    public void drawPayAndDeliveryFormForm(PayAndDeliveryForm form) {
        String chatId;
        switch (TelegramUtils.extractTelegramUserId(update)) {
            case ResultOk<Long, Error> ok -> chatId = ok.unwrap().toString();
            case ResultError<Long, Error> err -> {
                draw(err.err());
                return;
            }
        }
        InlineKeyboardRow keyboardRow = new InlineKeyboardRow(
                getButton(new IndexCmd(null))
        );

        String messageText = "Оплата и доставка";

        var keyBoard = InlineKeyboardMarkup.builder()
                .keyboardRow(keyboardRow)
                .build();
        Integer messageId = update.getCallbackQuery().getMessage().getMessageId();
        AnswerCallbackQuery close = AnswerCallbackQuery.builder()
                .callbackQueryId(update.getCallbackQuery().getId())
                .build();
        EditMessageText message = EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text(messageText)
                .build();
        EditMessageReplyMarkup replyMarkup = EditMessageReplyMarkup.builder()
                .chatId(chatId)
                .messageId(messageId)
                .replyMarkup(keyBoard)
                .build();
        closeQueryAndRewriteMessage(close, message, replyMarkup);
    }

    @Override
    public void drawProductInstructionForm(ProductInstructionForm form) {
        String chatId;
        switch (TelegramUtils.extractTelegramUserId(update)) {
            case ResultOk<Long, Error> ok -> chatId = ok.unwrap().toString();
            case ResultError<Long, Error> err -> {
                draw(err.err());
                return;
            }
        }
        InlineKeyboardRow keyboardRow = new InlineKeyboardRow(
                getButton("Назад", new ProductAboutCmd(null, form.product().getId()))
        );

        String messageText = "Описание товара\n\n" + form.product().getDescription();

        var keyBoard = InlineKeyboardMarkup.builder()
                .keyboardRow(keyboardRow)
                .build();
        Integer messageId = update.getCallbackQuery().getMessage().getMessageId();
        AnswerCallbackQuery close = AnswerCallbackQuery.builder()
                .callbackQueryId(update.getCallbackQuery().getId())
                .build();
        EditMessageText message = EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text(messageText)
                .build();
        EditMessageReplyMarkup replyMarkup = EditMessageReplyMarkup.builder()
                .chatId(chatId)
                .messageId(messageId)
                .replyMarkup(keyBoard)
                .build();
        closeQueryAndRewriteMessage(close, message, replyMarkup);
    }

    @Override
    public void draw(Error error) {
        // todo implement this
        log.error("=== error inside the app: {}", error.toString());
    }

    private InlineKeyboardButton getButton(String buttonText, Command command) {
        return InlineKeyboardButton.builder()
                .text(buttonText)
                .callbackData(command.toString())
                .build();
    }

    private InlineKeyboardButton getButton(Command command) {
        String text = switch (command) {
            case AboutCmd cmd -> "О нас";
            case ForWholesalerCmd cmd -> "Оптовикам";
            case PayAndDeliveryCmd cmd -> "Оплата и доставка";
            case AddToBasketCmd cmd -> "Добавить в корзину";
            case BasketCmd cmd -> "Посмотреть корзину";
            case CatalogCmd cmd -> "Каталог товаров";
            case DoOrderCmd cmd -> "Оформить заказ";
            case FolderCmd cmd -> PathUtil.getFolderName(cmd.path());
            case IndexCmd cmd -> "На главную";
            case InstructionCmd cmd -> "Описание товара";
            case ProductAboutCmd cmd -> productService.getById(cmd.productId()).ok().get().getName();
            case QuantityMinusCmd cmd -> "Убрать из корзины";
            case QuantityPlusCmd cmd -> "Добавить в корзину";
            case ClearBasketCmd cmd -> "Очистить корзину";
            case DoNothing cmd -> "¯\\_(ツ)_/¯";
            case StartCmd cmd -> "Restart session. This command should not appear in user interface.";
        };
        return InlineKeyboardButton.builder()
                .text(text)
                .callbackData(command.toString())
                .build();
    }

    private void sendMessage(SendMessage message) {
        try {
            log.trace("=== try to send message ===");
            Message res = client.execute(message);
            log.trace("=== send message: {} ===", res);
        } catch (TelegramApiException e) {
            log.error("Telegram error during sending message: ", e);
        }
    }

    private void rewriteMessage(EditMessageText messageText, EditMessageReplyMarkup messageReplyMarkup) {
        try {
            log.trace("Try to rewrite telegram message");
            client.execute(messageText);
            client.execute(messageReplyMarkup);
        } catch (TelegramApiException e) {
            log.error("Telegram error during rewriting message: ", e);
        }
    }

    private void closeQuery(AnswerCallbackQuery answerCallbackQuery) {
        try {
            log.trace("Try to close telegram query");
            client.execute(answerCallbackQuery);
        } catch (TelegramApiException e) {
            log.error("Telegram error during closing query: ", e);
        }
    }

    private void closeQueryAndRewriteMessage(AnswerCallbackQuery answerCallbackQuery,
                                             EditMessageText messageText,
                                             EditMessageReplyMarkup messageReplyMarkup) {
        closeQuery(answerCallbackQuery);
        rewriteMessage(messageText, messageReplyMarkup);

    }

    private String getProductTableHeader() {
        return PRODUCT_ROW_TABLE_TEMPLATE
                .formatted(NAME_OF_PRODUCT, QUANTITY_OF_PRODUCT, TOTAL_SUM_OF_PRODUCT);
    }

    private String getProductTable(List<ProductRowDto> products) {
        long maxProductNameLength = products.stream()
                .map(ProductRowDto::product)
                .map(Product::getName)
                .mapToInt(String::length)
                .map(i -> i + 5)
                .max()
                .orElseGet(() -> 0);

        long maxQuantityLength = products.stream()
                .map(this::quantityToString)
                .mapToInt(String::length)
                .map(i -> i + 5)
                .max()
                .orElseGet(() -> 0);

        long maxTotalSumLength = products.stream()
                .map(this::totalSumToString)
                .mapToInt(String::length)
                .map(i -> i + 5)
                .max()
                .orElseGet(() -> 0);

        List<String> productRowStringList = products.stream()
                .map(productRowDto ->
                        getProductTableRow(productRowDto, maxProductNameLength, maxQuantityLength, maxTotalSumLength))
                .map(rowStr -> rowStr.concat("\n"))
                .toList();

        String productTableHeader = getProductTableHeader() + "\n";
        String productTable = Stream.concat(Stream.of(productTableHeader, "\n"), productRowStringList.stream())
                .reduce("", String::concat);

        return productTable;
    }

    private String getProductTableRow(ProductRowDto productRowDto, long maxName, long maxQuantity, long maxTotalSum) {
        String quantityStr = quantityToString(productRowDto);
        String totalSumStr = totalSumToString(productRowDto);
        return getProductRow(
                productRowDto.product().getName(),
                quantityStr,
                totalSumStr,
                maxName,
                maxQuantity,
                maxTotalSum
        );
    }

    private String getProductRow(String productName, String quantity, String totalSum, long maxName, long maxQuantity, long maxTotalSum) {
        String nameNormalized = normalizeByPadding(productName, maxName);
        String quantityNormalized = normalizeByPadding(quantity, maxQuantity);
        String totalSumNormalized = normalizeByPadding(totalSum, maxTotalSum);
        return PRODUCT_ROW_TABLE_TEMPLATE.formatted(nameNormalized, quantityNormalized, totalSumNormalized);
    }

    private String normalizeByPadding(String value, long maxLineLength) {
        String template = new StringBuilder("%-").append(maxLineLength).append("s").toString();
        return template.formatted(value);
    }

    private String quantityToString(ProductRowDto productRowDto) {
        double costInRub = (double) productRowDto.product().getCost() / 100;
        return QUANTITY_TEMPLATE.formatted(productRowDto.quantity(), costInRub);
    }

    private String totalSumToString(ProductRowDto productRowDto) {
        double totalSumInRub = (double) productRowDto.product().getCost() * productRowDto.quantity() / 100;
        return TOTAL_SUM_TEMPLATE.formatted(totalSumInRub);
    }
}
