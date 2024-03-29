package ua.javarush;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.Video;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.File;
import java.util.Map;

import static ua.javarush.TelegramBotContent.*;
import static ua.javarush.TelegramBotUtils.*;

public class MyFirstTelegramBot extends TelegramLongPollingBot {
    public static void main(String[] args) throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(new MyFirstTelegramBot());
    }

    @Override
    public String getBotUsername() {
        // TODO: додай ім'я бота в лапки нижче
        return "TryamkinsBot";
    }

    @Override
    public String getBotToken() {
        // TODO: додай токен бота в лапки нижче
        return "5355288386:AAFEoSF-H7A592K1xziUay1J6DUfMXeoIlE";
    }



    @Override
    public void onUpdateReceived(Update update) {
        // TODO: основний функціонал бота будемо писати тут
        Long chatId = getChatId(update);


        if (update.hasMessage() && update.getMessage().getText().equals("/time")) {
            new Thread(() -> {
                int seconds = 5;
                while (seconds >= 0) {
                    try {
                        // Отправить сообщение
                        SendMessage sendMessage = new SendMessage();
                        sendMessage.setChatId(chatId);
                        sendMessage.setText("Таймер: " + seconds + " секунд");
                        execute(sendMessage);

                        Thread.sleep(1000);
                    } catch (TelegramApiException | InterruptedException e) {
                        e.printStackTrace();
                    }
                    seconds--;
                }

                // Отправить сообщение о завершении
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(chatId);
                sendMessage.setText("Время вышло!");
                try {
                    execute(sendMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }).start();
        }

        if (update.hasMessage() && update.getMessage().getText().equals("/start")) {
            SendVideo sendVideo = createVideoMessage(chatId,"very");
            executeAsync(sendVideo);
            executeAsync( createAudioMessage(chatId,"Ouch-6"));

            Map map = Map.of(
                    "Злам холодильника ", "step_1_btn",
                    "Мишко тиріт сосиски з холодильнику ", "step_1_btn"
            );
            sendMessage(chatId, 0, "step_1_pic", STEP_1_TEXT, map);
        }
        if (update.hasCallbackQuery()) {
            if (update.getCallbackQuery().getData().equals("step_1_btn") && getGlories(chatId) == 0) {
                Map map = Map.of("Взяти сосиску! +20 слави", "step_2_btn",
                        "Взяти рибку! +20 слави", "step_2_btn",
                        "Скинути банку з огірками! +20 слави", "step_2_btn");
                sendMessage(chatId, 20, "step_2_pic", STEP_2_TEXT, map);
            }
            if (update.getCallbackQuery().getData   ().equals("step_2_btn") && getGlories(chatId) == 20) {
                Map map = Map.of("Злам робота пилососа", "step_3_btn");
                sendMessage(chatId, 20, "step_3_pic", STEP_3_TEXT, map);
            }
            if (update.getCallbackQuery().getData().equals("step_3_btn") && getGlories(chatId) == 40) {
                Map map = Map.of("Відправити робопилосос за їжею! +30 слави", "step_4_btn",
                        "Проїхатися на робопилососі! +30 слави", "step_4_btn",
                        "Тікати від робопилососа! +30 слави", "step_4_btn");
                sendMessage(chatId, 30, "step_4_pic", STEP_4_TEXT, map);
            }
            if (update.getCallbackQuery().getData().equals("step_4_btn") && getGlories(chatId) == 70) {
                Map map = Map.of("Одягнути та включити GoPro!", "step_5_btn");
                sendMessage(chatId, 30, "step_5_pic", STEP_5_TEXT, map);
            }
            if (update.getCallbackQuery().getData().equals("step_5_btn") && getGlories(chatId) == 100) {
                Map map = Map.of("Бігати дахами, знімати на GoPro! +40 слави","step_6_btn",
                        "З GoPro нападати на інших котів із засідки! +40 слави", "step_6_btn",
                        "З GoPro нападати на собак із засідки! +40 слави", "step_6_btn");
                sendMessage(chatId, 40, "step_6_pic", STEP_6_TEXT, map);
            }
            if (update.getCallbackQuery().getData().equals("step_6_btn") && getGlories(chatId) == 140) {
                Map map = Map.of("Злам пароля!", "step_7_btn");
                sendMessage(chatId, 40, "step_7_pic", STEP_7_TEXT, map);
            }
            if (update.getCallbackQuery().getData().equals("step_7_btn") && getGlories(chatId) == 180) {
                Map map = Map.of("Вийти на подвір'я", "step_8_btn");
                sendMessage(chatId, 50, "step_8_pic", STEP_8_TEXT, map);
            }
            if (update.getCallbackQuery().getData().equals("step_8_btn") && getGlories(chatId) == 230) {
                sendMessage(chatId, 50, "final_pic", FINAL_TEXT, Map.of());
            }
        }
    }
    private void sendMessage(Long chatId, int glories, String picName, String text, Map<String, String> buttons) {
        addGlories(chatId, glories);
        SendPhoto photomessage = createPhotoMessage(chatId, picName);
        executeAsync(photomessage);
        SendMessage message = createMessage(chatId, text, buttons);
        sendApiMethodAsync(message);

    }
}