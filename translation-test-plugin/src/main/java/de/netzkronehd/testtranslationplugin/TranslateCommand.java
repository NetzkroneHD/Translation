package de.netzkronehd.testtranslationplugin;

import de.netzkronehd.translation.args.Args;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.translatable;

public class TranslateCommand implements CommandExecutor {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss dd.MM.yyyy");

    private static final Args.Args1<LocalDateTime> TEST_MESSAGE_WITH_ARGS = (dateTime) ->
            translatable().key("test.with.1.args").arguments(text(FORMATTER.format(dateTime))).build();

    private static final Args.Args0 TEST_MESSAGE_NO_ARGS = () ->
            translatable().key("test.with.0.args").build();


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        sender.sendMessage(TEST_MESSAGE_NO_ARGS.build());
        sender.sendMessage(TEST_MESSAGE_WITH_ARGS.build(LocalDateTime.now()));
        return true;
    }
}
