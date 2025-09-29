package de.netzkronehd.testtranslationplugin;

import de.netzkronehd.translation.args.Args;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.translatable;
import static net.kyori.adventure.text.format.NamedTextColor.YELLOW;

public class JoinListener implements Listener {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss dd.MM.yyyy");

    private static final Args.Args1<LocalDateTime> TEST_MESSAGE_WITH_ARGS = (dateTime) ->
            translatable().key("test.with.1.args").arguments(text(FORMATTER.format(dateTime)).color(YELLOW)).build();

    private static final Args.Args0 TEST_MESSAGE_NO_ARGS = () ->
            translatable().key("test.with.0.args").build();

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.getPlayer().sendMessage(TEST_MESSAGE_NO_ARGS.build());
        event.getPlayer().sendMessage(TEST_MESSAGE_WITH_ARGS.build(LocalDateTime.now()));
    }

}
