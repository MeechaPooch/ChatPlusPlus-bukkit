package io.github.meechapooch.chatplusplus;

import io.github.meechapooch.chatplusplus.chat.ChatGroup;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

//TODO Add addMembers function that sends an add message in a single line

public class ProfileMap {
    static File file;
    static HashMap<String, PlayerProfile> profiles = new HashMap<>();
    //    static JSONArray groupList = new JSONArray();
    static JSONObject groups = new JSONObject();

    public static void init() {
        groups = new JSONObject();
        profiles = new HashMap<>();

//        groupList = new JSONArray();
//        ChatPlusPlus.instance.getDataFolder().

        ChatPlusPlus.INSTANCE.getDataFolder().mkdirs();
        file = new File(ChatPlusPlus.INSTANCE.getDataFolder().getAbsolutePath() + File.separator + "groupsV2.json");

        try {
            if (file.createNewFile()) {
//                JSONArray array = new JSONArray();
                new FileWriter(file).write(groups.toJSONString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileReader reader = null;
        try {
            reader = new FileReader(file); ///////////
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        JSONParser parser = new JSONParser();
        try {
//            groupList = (JSONArray) parser.parse(reader); ////////
            groups = (JSONObject) parser.parse(reader); ////////
        } catch (IOException e) {
            e.printStackTrace();
            return;
        } catch (ParseException e) {
            e.printStackTrace();
//            JSONArray array = new JSONArray();
            try {
                FileWriter writer = new FileWriter(file);
                writer.write(new JSONObject().toJSONString());
                writer.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }

        readToProfiles();
    }

    public static void write() {
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(groups.toJSONString());
            writer.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    //TODO Take off of server thread
    public static void writeFromProfiles() {
        Set<ChatGroup> listOfGroups = new HashSet<>();
        Set<ChatGroup> lamGroups = listOfGroups;

        profiles.forEach((n, p) -> {
            lamGroups.addAll(p.getGroups());
            if (ChatPlusPlus.debug)
                System.out.println("[DEBUG] " + p.getGroups().size() + " groups added from player " + n);
        });

        listOfGroups = lamGroups;

        groups = new JSONObject();
        for (ChatGroup group : listOfGroups) {
            JSONObject groupObject = new JSONObject();
            JSONArray members = new JSONArray();
            members.addAll(group);
            groupObject.put("name", group.getName());
            groupObject.put("members", members);
            groups.put(group.getName(), groupObject);
        }

        write();

        if (ChatPlusPlus.debug)
            System.out.println("[DEBUG] File written from profles, group count: " + listOfGroups.size());
    }

    public static void readToProfiles() {
        for (Object group : groups.values()) {
            JSONObject groupOb = (JSONObject) group;
            HashSet<String> members = new HashSet<>();
            for (Object member : ((JSONArray) groupOb.get("members"))) {
                members.add((String) member);
            }
            for (String member : members) {
                addGroupToProfile(member, new ChatGroup((String) groupOb.get("name"), members));
            }
        }
    }

    public static void newGroup(String maker, String name, Set<String> members) {
        if (ChatPlusPlus.debug)
            System.out.println("[DEBUG] Now making new group " + maker + " " + name + " " + members.size());
        JSONObject group = new JSONObject();
        JSONArray memberList = new JSONArray();
        memberList.addAll(members);
        group.put("name", name);
        group.put("members", memberList);
        groups.put(name, group);
        write();
//        Thread.currentThread().stop();

        if (ChatPlusPlus.debug) System.out.println("[DEBUG] New Group added to file");

        ChatGroup groupObject = new ChatGroup(name, members);
        members.forEach((m) -> {
            addGroupToProfile(m, groupObject);
        });

        if (ChatPlusPlus.debug) System.out.println("[DEBUG] New Group added to java objects");

        groupObject.sendCreationMessage(maker);
    }

    private static void addGroupToProfile(String profileName, ChatGroup group) {
        getProfile(profileName).getGroups().add(group);
        if (ChatPlusPlus.debug) System.out.println("group added to " + profileName);
    }

    public static PlayerProfile getProfile(String name) {
        if (!profiles.containsKey(name)) profiles.put(name, new PlayerProfile(name));
        return profiles.get(name);
    }

    public static boolean groupExists(String groupName) {
        return groups.containsKey(groupName);
    }

//    public static void getOrMakeGroup(String maker, String name, List<String> members) {
//        if() {
//
//        } else {
//
//        }
//    }
}
