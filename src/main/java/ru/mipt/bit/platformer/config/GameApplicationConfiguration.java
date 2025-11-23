package ru.mipt.bit.platformer.config;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.mipt.bit.platformer.GameDesktopLauncher;
import ru.mipt.bit.platformer.game.factory.DefaultGameFactory;
import ru.mipt.bit.platformer.game.factory.IGameFactory;

@Configuration
public class GameApplicationConfiguration {

    @Bean
    public DefaultGameConfig defaultGameConfig(GameArguments arguments) {
        return arguments.createGameConfig();
    }

    @Bean
    public IGameFactory gameFactory(DefaultGameConfig defaultGameConfig) {
        return new DefaultGameFactory(defaultGameConfig);
    }

    @Bean
    public GameDesktopLauncher gameDesktopLauncher(IGameFactory gameFactory) {
        return new GameDesktopLauncher(gameFactory);
    }

    @Bean
    public Lwjgl3ApplicationConfiguration lwjgl3ApplicationConfiguration(IGameFactory gameFactory) {
        Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
        WindowConfig windowConfig = gameFactory.getWindowConfig();
        configuration.setWindowedMode(windowConfig.getWidth(), windowConfig.getHeight());
        return configuration;
    }
}
