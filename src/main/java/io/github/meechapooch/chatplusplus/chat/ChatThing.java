package io.github.meechapooch.chatplusplus.chat;

public interface ChatThing {
    void sendMessage(String sender, String message);

    String getName();
}
