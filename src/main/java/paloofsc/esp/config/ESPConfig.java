package paloofsc.esp.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ESPConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path FILE = FabricLoader.getInstance().getConfigDir().resolve("paloofsc_esp.json");

    public boolean espEnabled = true;
    public boolean tracerESP = true;
    public boolean boxESP = true;
    public boolean skeletonESP = false;
    public boolean healthESP = true;
    public boolean distanceESP = true;
    public boolean showSneak = true;
    public boolean showInvis = true;
    public int tracerAlpha = 80;
    public int boxWidth = 2;
    public int espDistance = 256;
    public String colorEnemy = "#FF0000";
    public String colorFriend = "#00FF00";
    public String colorSneak = "#FFAA00";
    public String colorInvis = "#FFFF00";

    public static ESPConfig load() {
        if (Files.exists(FILE)) {
            try { return GSON.fromJson(Files.readString(FILE), ESPConfig.class); }
            catch (IOException e) { e.printStackTrace(); }
        }
        ESPConfig cfg = new ESPConfig();
        cfg.save();
        return cfg;
    }

    public void save() {
        try { Files.writeString(FILE, GSON.toJson(this)); }
        catch (IOException e) { e.printStackTrace(); }
    }
}
