package de.netzkronehd.translation.sender;

import java.util.UUID;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;

public interface Sender {

    /** The uuid used by the console sender. */
    UUID CONSOLE_UUID = new UUID(0, 0); // 00000000-0000-0000-0000-000000000000
    /** The name used by the console sender. */
    String CONSOLE_NAME = "Console";

    void sendMessage(Component message);
    void performCommand(String command);
    void showTitle(Title title);
    void resetTitle();

    boolean hasPermission(String permission);
    boolean isConsole();

    UUID getUniqueId();
    String getName();


}
