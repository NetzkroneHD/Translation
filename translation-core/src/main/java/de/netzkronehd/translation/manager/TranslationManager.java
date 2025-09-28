package de.netzkronehd.translation.manager;

import de.netzkronehd.translation.exception.UnknownLocaleException;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.translation.MiniMessageTranslationStore;
import net.kyori.adventure.translation.GlobalTranslator;
import net.kyori.adventure.translation.Translator;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class TranslationManager {

    public static final Locale DEFAULT_LOCALE = Locale.ENGLISH;

    private final Set<Locale> installed;
    private final Key key;
    private MiniMessageTranslationStore translationStore;

    public TranslationManager(Key key) {
        this.installed = ConcurrentHashMap.newKeySet();
        this.key = key;
    }

    public MiniMessageTranslationStore registerStoreInGlobalTranslator() {
        if (this.translationStore != null) {
            GlobalTranslator.translator().removeSource(translationStore);
            this.installed.clear();
        }
        this.translationStore = MiniMessageTranslationStore.create(key);
        this.translationStore.defaultLocale(DEFAULT_LOCALE);
        GlobalTranslator.translator().addSource(translationStore);
        return translationStore;
    }

    public void loadFromFileSystem(Path directory) throws IOException, UnknownLocaleException {
        loadFromFileSystem(directory, (path) -> {});
    }

    public void loadFromFileSystem(Path directory, Consumer<Path> fileCallback) throws IOException, UnknownLocaleException {
        final List<Path> translationFiles;

        try (Stream<Path> stream = Files.list(directory)) {
            translationFiles = stream.filter(TranslationManager::isTranslationFile).toList();
        }

        final Map<Locale, ResourceBundle> loaded = new HashMap<>();
        for (Path translationFile : translationFiles) {
            fileCallback.accept(translationFile);
            final Map.Entry<Locale, ResourceBundle> result = loadTranslationFile(translationFile);
            loaded.put(result.getKey(), result.getValue());
        }
        loaded.forEach((locale, bundle) -> {
            final Locale localeWithoutCountry = Locale.of(locale.getLanguage());
            if (!locale.equals(localeWithoutCountry) && !localeWithoutCountry.equals(DEFAULT_LOCALE) && this.installed.add(localeWithoutCountry)) {
                this.translationStore.registerAll(localeWithoutCountry, bundle, false);
            }
        });
    }

    public Map.Entry<Locale, ResourceBundle> loadTranslationFile(Path translationFile) throws IOException, UnknownLocaleException {
        final String fileName = translationFile.getFileName().toString();
        final String localeString = fileName.substring(0, fileName.length() - ".properties".length());
        final Locale locale = parseLocale(localeString)
                .orElseThrow(() -> new UnknownLocaleException("Unknown locale '" + localeString + "' - unable to register."));

        final PropertyResourceBundle bundle;
        try (BufferedReader reader = Files.newBufferedReader(translationFile, StandardCharsets.UTF_8)) {
            bundle = new PropertyResourceBundle(reader);
        }

        this.translationStore.registerAll(locale, bundle, false);
        this.installed.add(locale);
        return new AbstractMap.SimpleImmutableEntry<>(locale, bundle);
    }

    public Set<Locale> getInstalled() {
        return installed;
    }

    public Key getKey() {
        return key;
    }

    public MiniMessageTranslationStore getTranslationStore() {
        return translationStore;
    }

    public static Component render(Component component, @Nullable Locale locale) {
        return GlobalTranslator.render(component, (locale == null ? DEFAULT_LOCALE:locale));
    }

    public static Optional<Locale> parseLocale(@Nullable String locale) {
        if(!isValidLocaleString(locale)) {
            return Optional.empty();
        }
        return Optional.ofNullable(Translator.parseLocale(locale));
    }

    public static boolean isTranslationFile(Path path) {
        return path.getFileName().toString().endsWith(".properties");
    }

    private static boolean isValidLocaleString(String locale) {
        return locale != null && !locale.isBlank();
    }

}
