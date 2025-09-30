package de.netzkronehd.testtranslationplugin;

import de.netzkronehd.translation.exception.UnknownLocaleException;
import de.netzkronehd.translation.manager.TranslationManager;
import net.kyori.adventure.key.Key;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public final class TranslationPlugin extends JavaPlugin {

    private TranslationManager translationManager;

    @Override
    public void onEnable() {
        translationManager = new TranslationManager(Key.key("translationplugin", "messages"));
        saveResource("locales/en.properties", false);
        saveResource("locales/de.properties", false);
        try {
            translationManager.loadFromFileSystem(getDataFolder().toPath().resolve("locales/"));
        } catch (IOException | UnknownLocaleException e) {
            throw new RuntimeException(e);
        }

        getCommand("testtranslation").setExecutor(new TranslateCommand());

        getLogger().info("Translation plugin enabled");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public TranslationManager getTranslationManager() {
        return translationManager;
    }
}
