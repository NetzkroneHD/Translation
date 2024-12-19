package de.netzkronehd.translation.sender;

import java.util.UUID;

import net.kyori.adventure.text.Component;

public interface Sender {

    /** The uuid used by the console sender. */
    UUID CONSOLE_UUID = new UUID(0, 0); // 00000000-0000-0000-0000-000000000000
    /** The name used by the console sender. */
    String CONSOLE_NAME = "Console";

    void sendMessage(Component message);

    boolean hasPermission(String permission);
    boolean isConsole();

    UUID getUniqueId();
    String getName();


}
