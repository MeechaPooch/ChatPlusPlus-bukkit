package io.github.meechapooch.chatplusplus;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class UUIDManager {
    public static int NO_ERR = 0;
    public static int ERR_PLAYER_NOT_KNOWN = 1;
    public static int ERR_ALREADY_RESTORED = 2;
    public static int ERR_CANT_RENAME = 3;
    public static HashMap<String, String> players;
    static JavaPlugin plugin;
    private static File file;

    public static void main(String... args) {
        File one = new File("test1" + File.separator + "test.dat_old");
        File two = new File("test1" + File.separator + "to.dat");
        one.getParentFile().mkdirs();

        try {
            one.createNewFile();
            two.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            System.out.println(one.renameTo(two));
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            two.delete();
            System.out.println(one.renameTo(two));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void playerJoined(String playerName, String uuid) {
        playerName = playerName.toLowerCase();

        if (!players.containsKey(playerName)) {
            players.put(playerName, uuid);
            writeToDatabase();
        }
    }

    public static List<String> getPlayerNames() {
        LinkedList<String> ret = new LinkedList<>(players.keySet());
        return ret;
    }

    public static int quickRestore(String playerName) {
        if (!players.containsKey(playerName.toLowerCase())) return ERR_PLAYER_NOT_KNOWN;

        Player player = plugin.getServer().getPlayer(playerName);

        if (player != null) player.kickPlayer("Your data is being quickrestored!");

//        System.out.println(players.get(playerName));
//        System.out.println(plugin.getDataFolder().getAbsoluteFile().toString());
//        System.out.println(plugin.getDataFolder().toString());
//        System.out.println(plugin.getDataFolder().getParentFile().toString());
//        System.out.println(plugin.getDataFolder().getParentFile().getParentFile().toString());
//        System.out.println(plugin.getDataFolder().getParentFile().getParentFile().getAbsolutePath());

        File oldDat = new File(plugin.getDataFolder().getAbsoluteFile().getParentFile().getParentFile().getAbsolutePath() + File.separator + "world" + File.separator + "playerdata" + File.separator + players.get(playerName.toLowerCase()) + ".dat_old");
        File badDat = new File(plugin.getDataFolder().getAbsoluteFile().getParentFile().getParentFile().getAbsolutePath() + File.separator + "world" + File.separator + "playerdata" + File.separator + players.get(playerName.toLowerCase()) + ".dat");
//        File badDat = new File(plugin.getDataFolder().getAbsolutePath() + "\\..\\..\\world\\playerdata\\" + players.get(playerName.toLowerCase()) + ".dat");

        System.out.println("check 1");
        System.out.println(oldDat.toString());
        System.out.println(badDat.toString());

        if (!oldDat.exists()) return ERR_ALREADY_RESTORED;
//        if(!badDat.exists()) return -1;
        System.out.println("check 2");

        try {
            badDat.delete();
            System.out.println("renaming return: " + oldDat.renameTo(badDat));
            System.out.println("ACTUAL RENAME CODE RUN");
        } catch (Exception e) {
            e.printStackTrace();
            return ERR_CANT_RENAME;
        }

        return NO_ERR;
    }

    private static void writeToDatabase() {
        try {
            FileWriter writer = new FileWriter(file);
            players.forEach((player, uuid) -> {
                try {
                    writer.append(player.toLowerCase()).append("\n").append(uuid).append("\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void readToMap() {
        try {
            file = new File(plugin.getDataFolder().getAbsolutePath() + File.separator + "uuids.txt");

            players = new HashMap<>();

            Scanner scanner = new Scanner(file);
            String playerName = "";
            String uuid = "";

            boolean onName = false;

            while (scanner.hasNext()) {
                onName = !onName;
                if (onName) {
                    playerName = scanner.nextLine();
                } else {
                    uuid = scanner.nextLine();
                    players.put(playerName, uuid);
                }
            }

            players.forEach((key, value) -> System.out.println(key + "," + value));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void initDatabase() {
        plugin.getDataFolder().mkdirs();
        file = new File(plugin.getDataFolder().getAbsolutePath() + File.separator + "uuids.txt");

        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void init() {
        plugin = ChatPlusPlus.INSTANCE;
        initDatabase();
        readToMap();
    }

    public static class Line {
        int line;
        String cont;

        public Line(int line, String cont) {
            this.line = line;
            this.cont = cont;
        }
    }
}
