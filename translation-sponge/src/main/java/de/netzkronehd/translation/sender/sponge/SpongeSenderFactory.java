package de.netzkronehd.translation.sender.sponge;

import de.netzkronehd.translation.manager.TranslationManager;
import de.netzkronehd.translation.sender.Sender;
import de.netzkronehd.translation.sender.SenderFactory;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.spongepowered.api.Server;
import org.spongepowered.api.SystemSubject;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.permission.Subject;

import java.util.Locale;
import java.util.UUID;

public class SpongeSenderFactory extends SenderFactory<Audience> {

    private final Server server;

    public SpongeSenderFactory(Server server) {
        this.server = server;
    }

    @Override
    public void sendMessage(Audience sender, Component message) {
        final Locale locale;
        if(sender instanceof final Player p) {
            locale = p.locale();
        } else {
            locale = null;
        }
        sender.sendMessage(TranslationManager.render(message, locale));
    }

    @Override
    public void performCommand(Audience sender, String command) {
        try {
            server.commandManager().process(((Subject) sender), sender, command);
        } catch (CommandException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void showTitle(Audience sender, Title title) {
        sender.showTitle(title);
    }

    @Override
    public void resetTitle(Audience sender) {
        sender.resetTitle();
    }

    @Override
    public UUID getUniqueId(Audience sender) {
        if (sender instanceof final Player p) {
            return p.uniqueId();
        }
        return Sender.CONSOLE_UUID;
    }

    @Override
    public String getName(Audience sender) {
        if (sender instanceof final Player p) {
            return p.name();
        }
        return Sender.CONSOLE_NAME;
    }

    @Override
    public boolean hasPermission(Audience sender, String permission) {
        if(sender instanceof final Subject subject) {
            return subject.hasPermission(permission);
        }
        throw new IllegalStateException("Sender is not a subject");
    }

    @Override
    public boolean isConsole(Audience sender) {
        return sender instanceof SystemSubject;
    }
}
