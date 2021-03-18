package io.github.meechapooch.chatplusplus.chat;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import io.github.meechapooch.chatplusplus.ChatPlusPlus;
import io.github.meechapooch.chatplusplus.ProfileMap;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class ChatGroup extends HashSet<String> implements ChatThing {

    private String name;
    private TextComponent hoverableComponent;

    public ChatGroup(String name) {
        this.name = name;
    }

    public ChatGroup(String name, Set<String> members) {
        this(name);
        addAll(members);
        refreshHoverableText();
    }

    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
//        if (!super.equals(o)) return false;
        ChatGroup group = (ChatGroup) o;
        return name.equals(group.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public void refreshHoverableText() {
        String hoverText = "";
        for (String player : this) {
            hoverText = hoverText + player + "\n";
        }
        hoverText = hoverText.trim();

        TextComponent comp = new TextComponent(name);
        comp.setColor(ChatColor.RED);
        comp.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                new Text(hoverText)));
        hoverableComponent = comp;
    }

    public void sendCreationMessage(String maker) {
        TextComponent toAdd;
        TextComponent chatMessage = new TextComponent(maker);
        chatMessage.setColor(ChatColor.GREEN);

        toAdd = new TextComponent(" added you to ");
        toAdd.setColor(ChatColor.GRAY);
        chatMessage.addExtra(toAdd);

        chatMessage.addExtra(hoverableComponent);

        TextComponent makerMessage = new TextComponent("You made new group ");
        chatMessage.setColor(ChatColor.GRAY);
        makerMessage.addExtra(hoverableComponent);

        for (String player : this) {
            if (!player.equals(maker)) sendSpigotMessage(player, chatMessage);
            else sendSpigotMessage(player, makerMessage);
        }
    }

    public void sendMessage(String sender, String message) {
        TextComponent toAdd;
        TextComponent chatMessage = new TextComponent("[");
        chatMessage.setColor(ChatColor.WHITE);

        chatMessage.addExtra(hoverableComponent);

        toAdd = new TextComponent(" | ");
        toAdd.setColor(ChatColor.WHITE);
        chatMessage.addExtra(toAdd);

        toAdd = new TextComponent(sender);
        toAdd.setColor(ChatColor.GREEN);
        chatMessage.addExtra(toAdd);

        toAdd = new TextComponent("] " + message);
        toAdd.setColor(ChatColor.WHITE);
        chatMessage.addExtra(toAdd);

        for (String playerName : this) {
            sendSpigotMessage(playerName, chatMessage);
        }
    }

    public void addMembers(String adder, HashSet<String> players) {
        for (String player : players) {
            addMember(adder, player);
        }
    }

    public void addMember(String adder, String player) {
        add(player);
        refreshHoverableText();
        ProfileMap.getProfile(player).getGroups().add(this);

        TextComponent toAdd;
        TextComponent chatMessage = new TextComponent(adder);
        chatMessage.setColor(ChatColor.GREEN);

        toAdd = new TextComponent(" added ");
        toAdd.setColor(ChatColor.GRAY);
        chatMessage.addExtra(toAdd);

        TextComponent addedMessage = chatMessage.duplicate();
        toAdd = new TextComponent("you");
        toAdd.setColor(ChatColor.GRAY);
        addedMessage.addExtra(toAdd);
        toAdd = new TextComponent(player);
        toAdd.setColor(ChatColor.LIGHT_PURPLE);
        chatMessage.addExtra(toAdd);

        toAdd = new TextComponent(" to ");
        toAdd.setColor(ChatColor.GRAY);
        chatMessage.addExtra(toAdd);
        addedMessage.addExtra(toAdd);

        chatMessage.addExtra(hoverableComponent);
        addedMessage.addExtra(hoverableComponent);

        for (String playerName : this) {
            if (!playerName.equalsIgnoreCase(player)) sendSpigotMessage(playerName, chatMessage);
        }
        sendSpigotMessage(player, addedMessage);
    }

    private boolean sendSpigotMessage(String playerName, BaseComponent message) {
        if (Bukkit.getPlayer(playerName) == null) return false;
        if (playerName.equalsIgnoreCase("console")) Bukkit.getConsoleSender().spigot().sendMessage(message);
        else Bukkit.getPlayer(playerName).spigot().sendMessage(message);
        return true;
    }

    public void removeMember(String remover, String player) {
        remove(player);
        refreshHoverableText();

        TextComponent toAdd;
        TextComponent chatMessage = new TextComponent(remover);
        chatMessage.setColor(ChatColor.GREEN);

        toAdd = new TextComponent(" removed ");
        toAdd.setColor(ChatColor.GRAY);
        chatMessage.addExtra(toAdd);

        TextComponent addedMessage = chatMessage.duplicate();
        toAdd = new TextComponent("you");
        toAdd.setColor(ChatColor.GRAY);
        addedMessage.addExtra(toAdd);
        toAdd = new TextComponent(player);
        toAdd.setColor(ChatColor.LIGHT_PURPLE);
        chatMessage.addExtra(toAdd);

        toAdd = new TextComponent(" from ");
        toAdd.setColor(ChatColor.GRAY);
        chatMessage.addExtra(toAdd);
        addedMessage.addExtra(toAdd);

        chatMessage.addExtra(hoverableComponent);
        addedMessage.addExtra(hoverableComponent);

        for (String playerName : this) {
            sendSpigotMessage(playerName, chatMessage);
        }

        //TODO SEE WHY THIS IS NOT REMOVING
//        ProfileMap.getProfile(player).groups.remove(this);
        ProfileMap.getProfile(player).removeGroup(name);
        if (ChatPlusPlus.debug)
            System.out.println("Groups removed from player, ending group list: " + Arrays.toString(ProfileMap.getProfile(player).getGroups().toArray()));
        sendSpigotMessage(player, addedMessage);
    }

    public void removeMembers(String remover, HashSet<String> players) {
        for (String player : players) {
            removeMember(remover, player);
        }
    }

    public TextComponent getHoverableComponent() {
        return hoverableComponent;
    }
}
