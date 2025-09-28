package de.netzkronehd.translation.manager;

import de.netzkronehd.translation.exception.UnknownLocaleException;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.translation.GlobalTranslator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class TranslationManagerTest {

    private static final Path TEST_LOCALES_PATH = Path.of("src/test/resources/test-locales");
    private static final Path TEST_LOCALES_WITH_INCORRECT = Path.of("src/test/resources/test-locales-with-incorrect");

    private final TranslationManager manager = new TranslationManager(Key.key("test", "translations"));

    @AfterEach
    void tearDown() {
        GlobalTranslator.translator().removeSource(manager.getStore());
    }

    @Test
    void registerStoreInGlobalTranslatorShouldRegisterInGlobalTranslator() {
        var registered = new TranslationManager(Key.key("register-test", "translations")).registerStoreInGlobalTranslator();
        assertTrue(registered);
    }

    @Test
    void loadFromFileSystemWithNull() {
        assertThrows(NullPointerException.class, () -> manager.loadFromFileSystem(null));
    }

    @Test
    void loadFromFileSystemWithIncorrectLocalesShouldThrowException() {
        assertThrows(UnknownLocaleException.class, () -> manager.loadFromFileSystem(TEST_LOCALES_WITH_INCORRECT));
    }

    @Test
    void loadFromFileSystemWithCorrectLocalesShouldLoadLocales() throws UnknownLocaleException, IOException {
        manager.loadFromFileSystem(TEST_LOCALES_PATH);

        assertAll(
                () -> assertTrue(manager.getStore().canTranslate("test.with.0.args", Locale.ENGLISH), "Should be able to translate in English"),
                () -> assertTrue(manager.getStore().canTranslate("test.with.0.args", Locale.GERMAN), "Should be able to translate in German"),
                () -> assertTrue(manager.getStore().canTranslate("test.with.1.args", Locale.ENGLISH), "Should be able to translate in English"),
                () -> assertTrue(manager.getStore().canTranslate("test.with.1.args", Locale.GERMAN), "Should be able to translate in German")
        );

    }

    @Test
    void loadFromFileSystemWithCorrectLocalesShouldBeInGlobalTranslator() throws UnknownLocaleException, IOException {
        manager.loadFromFileSystem(TEST_LOCALES_PATH);

        assertAll(
                () -> assertTrue(GlobalTranslator.translator().canTranslate("test.with.0.args", Locale.ENGLISH), "Should be able to translate in English"),
                () -> assertTrue(GlobalTranslator.translator().canTranslate("test.with.0.args", Locale.GERMAN), "Should be able to translate in German"),
                () -> assertTrue(GlobalTranslator.translator().canTranslate("test.with.1.args", Locale.ENGLISH), "Should be able to translate in English"),
                () -> assertTrue(GlobalTranslator.translator().canTranslate("test.with.1.args", Locale.GERMAN), "Should be able to translate in German")
        );

    }




}
