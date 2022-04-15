package zxc.rich;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.opengl.Display;
import prot.rich.Client;
import zxc.rich.api.command.CommandManager;
import zxc.rich.api.command.macro.MacroManager;
import zxc.rich.api.event.EventManager;
import zxc.rich.api.event.EventTarget;
import zxc.rich.api.event.events.impl.EventKey;
import zxc.rich.api.event.events.impl.EventShutdownClient;
import zxc.rich.api.utils.render.ShaderShell;
import zxc.rich.client.features.Feature;
import zxc.rich.client.features.FeatureDirector;
import zxc.rich.client.friend.FriendManager;
import zxc.rich.client.ui.clickgui.ClickGuiScreen;
import zxc.rich.client.ui.config.ConfigManager;
import zxc.rich.client.ui.draggable.DraggableManager;
import zxc.rich.client.ui.settings.FileManager;
import zxc.rich.client.ui.settings.impls.HudConfig;
import zxc.rich.client.ui.settings.impls.MacroConfig;

import java.io.IOException;

public class Main {

    public static Main instance = new Main();

    public String name = "Rich Premium", version = "beta 0.2.1";
    public FeatureDirector featureDirector;
    public ConfigManager configManager;
    public FriendManager friendManager;
    public CommandManager commandManager;
    public MacroManager macroManager;
    public FileManager fileManager;
    public DraggableManager draggableManager;
    public Client client;
    public ClickGuiScreen clickGui;
    public static long playTimeStart = 0;

    public void load() throws IOException {

        Display.setTitle(name + " " + version);

        ShaderShell.init();

        (fileManager = new FileManager()).loadFiles();
        featureDirector = new FeatureDirector();
        client = new Client();
        commandManager = new CommandManager();
        configManager = new ConfigManager();
        macroManager = new MacroManager();
        draggableManager = new DraggableManager();
        clickGui = new ClickGuiScreen();
        friendManager = new FriendManager();

        try {
            fileManager.getFile(HudConfig.class).loadFile();
        } catch (Exception ignored) {
        }
        try {
            fileManager.getFile(MacroConfig.class).loadFile();
        } catch (Exception ignored) {
        }

        EventManager.register(this);
    }

    @EventTarget
    public void shutDown(EventShutdownClient event) {
        EventManager.unregister(this);
        (fileManager = new FileManager()).saveFiles();

    }

    public static void msg(String s, boolean prefix) {
        s = (prefix ? TextFormatting.GRAY + "[" + TextFormatting.RED + "Rich Premium" + TextFormatting.GRAY + "]" + ": " : "") + s;
        Minecraft.getMinecraft().player.addChatMessage(new TextComponentString(s.replace("&", "??")));
    }

    @EventTarget
    public void onKey(EventKey event) {
        featureDirector.getAllFeatures().stream().filter(module -> module.getKey() == event.getKey()).forEach(Feature::toggle);
        macroManager.getMacros().stream().filter(macros -> macros.getKey() == event.getKey()).forEach(macros -> Minecraft.getMinecraft().player.sendChatMessage(macros.getValue()));
    }
}