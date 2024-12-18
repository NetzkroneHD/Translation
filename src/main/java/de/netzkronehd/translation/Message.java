package de.netzkronehd.translation;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.Collection;
import java.util.Iterator;

import static net.kyori.adventure.text.Component.join;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;

public interface Message {

    static Component formatBoolean(boolean bool) {
        return bool ? text("true", GREEN) : text("false", RED);
    }

    static Component joinNewline(final ComponentLike... components) {
        return join(JoinConfiguration.newlines(), components);
    }

    static Component formatStringList(Collection<String> strings) {
        final Iterator<String> it = strings.iterator();
        final TextComponent.Builder builder = text().color(DARK_AQUA).content(it.next());

        while (it.hasNext()) {
            builder.append(text(", ", GRAY));
            builder.append(text(it.next()));
        }

        return builder.build();
    }

    static String serializeLegacySection(Component component) {
        return LegacyComponentSerializer.legacySection().serialize(component);
    }

    static Component formatColoredValue(String value) {
        final boolean containsLegacyFormattingCharacter = value.indexOf(LegacyComponentSerializer.AMPERSAND_CHAR) != -1
                || value.indexOf(LegacyComponentSerializer.SECTION_CHAR) != -1;

        if (containsLegacyFormattingCharacter) {
            return LegacyComponentSerializer.legacyAmpersand().deserialize(value).toBuilder()
                    .build();
        } else {
            return MiniMessage.miniMessage().deserialize(value);
        }
    }

    static Component formatContext(String key, String value) {
        // "&3{}=&b{}"
        return text()
                .content(key)
                .color(DARK_AQUA)
                .append(text('='))
                .append(text(value, AQUA))
                .build();
    }

}
