package ua.javarush;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Timer extends MyFirstTelegramBot{

    void timer(Long chatId) {
        new Thread(() -> {
            int seconds = 5;
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.setText("Таймер: " + seconds + " секунд");
            Message sentMessage = null;
            try {
                sentMessage = execute(sendMessage);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
            while (seconds >= 0) {
                try {
                    // Отправить сообщение
                    EditMessageText editMessageText = new EditMessageText();
                    editMessageText.setChatId(String.valueOf(chatId));
                    editMessageText.setMessageId(sentMessage.getMessageId());
                    editMessageText.setText("Таймер: " + seconds + " секунд");
                    execute(editMessageText);
                    Thread.sleep(1000);
                } catch (TelegramApiException | InterruptedException e) {
                    e.printStackTrace();
                }
                seconds--;
            }
            // Отправить сообщение о завершении
            DeleteMessage deleteMessage = new DeleteMessage();
            deleteMessage.setChatId(String.valueOf(chatId));
            deleteMessage.setMessageId(sentMessage.getMessageId());
            try {
               execute(deleteMessage);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }

            SendMessage sendMessage2 = new SendMessage();
            sendMessage2.setChatId(chatId);
            sendMessage2.setText("Время вышло!");
            try {
               execute(sendMessage2);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }).start();
    }
}