package ru.aquamarina.api.bot.telegram;

import ru.aquamarina.api.bot.DrawContext;

public record TelegramDrawContext(String chatId) implements DrawContext {
}
