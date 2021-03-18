package to.us.ravinesquaad.plugins.chatplusplus;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class PlayerProfile {
    ChatThing activeChat = null;
    HashSet<ChatGroup> groups = new HashSet<>();
    String player;

    public PlayerProfile(String player) {
        this.player = player;
    }

    public ChatGroup getGroup(String name) {
        for (ChatGroup group : groups) {
            if(group.name.equals(name)) {
                if(ChatPlusPlus.debug) System.out.println("[DEBUG] Group " + name + " found in player " + player);
                return group;
            }
        }
        return null;
    }

    public List<String> getGroupNames() {
        LinkedList<String> ret = new LinkedList<String>();
        for (ChatGroup group : groups) {
            ret.add(group.name);
        }
        return ret;
    }

    public boolean removeGroup(String groupName) {
        if(activeChat != null && activeChat.getName().equals(groupName)) activeChat = null;
        return groups.removeIf(group -> group.name.equals(groupName));
    }
}
