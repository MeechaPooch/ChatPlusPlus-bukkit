package io.github.meechapooch.chatplusplus;

import io.github.meechapooch.chatplusplus.chat.ChatGroup;
import io.github.meechapooch.chatplusplus.chat.ChatThing;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class PlayerProfile {
    private ChatThing activeChat = null;
    private final HashSet<ChatGroup> groups = new HashSet<>();
    private final String player;

    public PlayerProfile(String player) {
        this.player = player;
    }

    public ChatGroup getGroup(String name) {
        for (ChatGroup group : groups) {
            if (group.getName().equals(name)) {
                if (ChatPlusPlus.debug) System.out.println("[DEBUG] Group " + name + " found in player " + player);
                return group;
            }
        }
        return null;
    }

    public List<String> getGroupNames() {
        LinkedList<String> ret = new LinkedList<String>();
        for (ChatGroup group : groups) {
            ret.add(group.getName());
        }
        return ret;
    }

    public boolean removeGroup(String groupName) {
        if (activeChat != null && activeChat.getName().equals(groupName)) activeChat = null;
        return groups.removeIf(group -> group.getName().equals(groupName));
    }

    public ChatThing getActiveChat() {
        return activeChat;
    }

    public void setActiveChat(ChatThing activeChat) {
        this.activeChat = activeChat;
    }

    public HashSet<ChatGroup> getGroups() {
        return groups;
    }

    public String getPlayer() {
        return player;
    }
}
