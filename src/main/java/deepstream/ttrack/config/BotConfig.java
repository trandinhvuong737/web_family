package deepstream.ttrack.config;

import deepstream.ttrack.model.BotTelegram;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BotConfig {

    @Bean
    public BotTelegram botTelegram() {
        return new BotTelegram();
    }

}
