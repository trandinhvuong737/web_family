package deepstream.ttrack.config;

import deepstream.ttrack.model.BotTelegram;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
public class BotConfig {

    @Bean
    public BotTelegram botTelegram() {
        return new BotTelegram();
    }

    @Bean
    public TelegramBotsApi telegramBotsApi(BotTelegram botTelegram) throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            botsApi.registerBot(botTelegram);
        } catch (TelegramApiException e) {
            throw new IllegalStateException("Failed to register bot", e);
        }
        return botsApi;
    }
}
