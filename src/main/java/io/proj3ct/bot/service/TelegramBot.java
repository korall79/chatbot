package io.proj3ct.bot.service;

import io.proj3ct.bot.config.BotConfig;
import io.proj3ct.bot.model.User;
import io.proj3ct.bot.model.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {
    @Autowired
    private UserRepository userRepository;
    final BotConfig config;
    static final String HELP_TEXT = "This bot created to demonstrate Spring capabilities.\n\n" +
            "You can execute from the main menu on the left or by typing a command:\n\n" +
            "Type /start to see a welcome message\n\n" +
            "Type /mydata to see data stored about yourself\n\n" +
            "Type /deletedata to deleted stored about yourself\n\n" +
            "Type /help to see this message again\n\n" +
            "Type /settings to set your preferences";

    public TelegramBot(BotConfig config) {
        this.config = config;
        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/start"
                , "get a welcome message"));
        listOfCommands.add(new BotCommand("/mydata"
                , "get your data stored"));
        listOfCommands.add(new BotCommand("/deletedata"
                , "delete my data"));
        listOfCommands.add(new BotCommand("/help"
                , "info how to use this bot"));
        listOfCommands.add(new BotCommand("/settings"
                , "set your preferences"));
        try {
            this.execute(new SetMyCommands(listOfCommands
                    , new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Error setting bots command list: " + e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) { // при получении обновления
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            switch (messageText) {
                case "/start":

                    registerUser(update.getMessage());
                    startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    break;

                case "/help":
                    sendMessage(chatId, HELP_TEXT);
                    break;
                default:
                    sendMessage(chatId, "Sorry, command was not recognized");
            }
        }

    }

    private void registerUser(Message msg) {
        if (userRepository.findById(msg.getChatId()).isEmpty()) {
            var chatId = msg.getChatId();
            var chat = msg.getChat();

            User user = new User();
            user.setChatid(chatId);
            user.setFirstname(chat.getFirstName());
            user.setLastname(chat.getLastName());
            user.setUsername(chat.getUserName());
            user.setRegisteredat(new Timestamp(System.currentTimeMillis()));

            userRepository.save(user);
            log.info("user saved: "+ user);
        }
    }

    private void startCommandReceived(long chatId, String name) { //Команда запуска получена
        String answer = "Hi, " + name + ", nice to meet you!"; //отвечать
        log.info("Replied to user " + name);

        sendMessage(chatId, answer);
    }

    private void sendMessage(long chatId, String textToSend) { //Отправить сообщение
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error occurred: \"+ e.getMessage()");
        }

    }
}