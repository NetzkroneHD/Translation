package de.netzkronehd.translation.manager;

import de.netzkronehd.translation.exception.UnknownLocaleException;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.translation.GlobalTranslator;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class TranslationManagerTest {

    private static final Path TEST_LOCALES_PATH = Path.of("src/test/resources/test-locales");
    private static final Path TEST_LOCALES_WITH_INCORRECT = Path.of("src/test/resources/test-locales-with-incorrect");

    private final TranslationManager manager = new TranslationManager(Key.key("test", "translations"));

    @Test
    void registerStoreInGlobalTranslatorShouldRegisterInGlobalTranslator() {
        var store = manager.registerStoreInGlobalTranslator();
        assertNotNull(store);
    }

    @Test
    void loadFromFileSystemWithNull() {
        assertThrows(NullPointerException.class, () -> manager.loadFromFileSystem(null));
    }

    @Test
    void loadFromFileSystemWithIncorrectLocalesShouldThrowException() {
        manager.registerStoreInGlobalTranslator();

        assertThrows(UnknownLocaleException.class, () -> manager.loadFromFileSystem(TEST_LOCALES_WITH_INCORRECT));
    }

    @Test
    void loadFromFileSystemWithCorrectLocalesShouldLoadLocales() throws UnknownLocaleException, IOException {
        // Arrange
        var store = manager.registerStoreInGlobalTranslator();

        // Act
        manager.loadFromFileSystem(TEST_LOCALES_PATH);

        // Assert
        assertAll(
                () -> assertTrue(store.canTranslate("test.with.0.args", Locale.ENGLISH), "Should be able to translate in English"),
                () -> assertTrue(store.canTranslate("test.with.0.args", Locale.GERMAN), "Should be able to translate in German"),
                () -> assertTrue(store.canTranslate("test.with.1.args", Locale.ENGLISH), "Should be able to translate in English"),
                () -> assertTrue(store.canTranslate("test.with.1.args", Locale.GERMAN), "Should be able to translate in German")
        );

    }

    @Test
    void loadFromFileSystemWithCorrectLocalesShouldBeInGlobalTranslator() throws UnknownLocaleException, IOException {
        // Arrange
        manager.registerStoreInGlobalTranslator();

        // Act
        manager.loadFromFileSystem(TEST_LOCALES_PATH);

        // Assert
        assertAll(
                () -> assertTrue(GlobalTranslator.translator().canTranslate("test.with.0.args", Locale.ENGLISH), "Should be able to translate in English"),
                () -> assertTrue(GlobalTranslator.translator().canTranslate("test.with.0.args", Locale.GERMAN), "Should be able to translate in German"),
                () -> assertTrue(GlobalTranslator.translator().canTranslate("test.with.1.args", Locale.ENGLISH), "Should be able to translate in English"),
                () -> assertTrue(GlobalTranslator.translator().canTranslate("test.with.1.args", Locale.GERMAN), "Should be able to translate in German")
        );

    }




}
