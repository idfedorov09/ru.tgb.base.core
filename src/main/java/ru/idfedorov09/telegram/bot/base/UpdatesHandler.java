package ru.idfedorov09.telegram.bot.base;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;


public interface UpdatesHandler {

    void handle(TelegramLongPollingBot telegramLongPollingBot, Update update);
}
