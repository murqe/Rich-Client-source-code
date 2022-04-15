package zxc.rich.client.features;


import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.text.TextFormatting;
import zxc.rich.Main;
import zxc.rich.api.event.EventManager;
import zxc.rich.api.utils.sound.SoundHelper;
import zxc.rich.api.utils.world.TimerHelper;
import zxc.rich.client.features.hud.Notifications;
import zxc.rich.client.features.misc.ModuleSoundAlert;
import zxc.rich.client.ui.clickgui.ScreenHelper;
import zxc.rich.client.ui.notification.NotificationMode;
import zxc.rich.client.ui.notification.NotificationRenderer;
import zxc.rich.client.ui.settings.Configurable;
import zxc.rich.client.ui.settings.Setting;
import zxc.rich.client.ui.settings.impl.BooleanSetting;
import zxc.rich.client.ui.settings.impl.ColorSetting;
import zxc.rich.client.ui.settings.impl.ListSetting;
import zxc.rich.client.ui.settings.impl.NumberSetting;

public class Feature extends Configurable {

    protected static Minecraft mc = Minecraft.getMinecraft();
    protected TimerHelper timerHelper = new TimerHelper();
    public ScreenHelper screenHelper = new ScreenHelper(0, 0);
    private String label, displayName;
    private int key;
    private FeatureCategory category;
    private boolean enabled;
    private String desc;
    private String suffix;
    public double slidex = 0;
    public double slidey = 0;

    public Feature(String name, String desc, FeatureCategory category) {
        this.label = name;
        this.desc = desc;
        this.category = category;
        this.key = 0;
        enabled = false;
    }
    public Feature(String name,  FeatureCategory category) {
        this.label = name;
        this.category = category;
        this.key = 0;
        enabled = false;
    }
    public JsonObject save() {
        JsonObject object = new JsonObject();
        object.addProperty("state", isEnabled());
        object.addProperty("keyIndex", getKey());
        JsonObject propertiesObject = new JsonObject();
        for (Setting set : this.getSettings()) {
            if (this.getSettings() != null) {
                if (set instanceof BooleanSetting) {
                    propertiesObject.addProperty(set.getName(), ((BooleanSetting) set).getBoolValue());
                } else if (set instanceof ListSetting) {
                    propertiesObject.addProperty(set.getName(), ((ListSetting) set).getCurrentMode());
                } else if (set instanceof NumberSetting) {
                    propertiesObject.addProperty(set.getName(), ((NumberSetting) set).getNumberValue());
                } else if (set instanceof ColorSetting) {
                    propertiesObject.addProperty(set.getName(), ((ColorSetting) set).getColorValue());
                }
            }
            object.add("Settings", propertiesObject);
        }
        return object;
    }

    public void load(JsonObject object) {
        if (object != null) {
            if (object.has("state")) {
                this.setEnabled(object.get("state").getAsBoolean());
            }
            if (object.has("keyIndex")) {
                this.setKey(object.get("keyIndex").getAsInt());
            }
            for (Setting set : getSettings()) {
                JsonObject propertiesObject = object.getAsJsonObject("Settings");
                if (set == null)
                    continue;
                if (propertiesObject == null)
                    continue;
                if (!propertiesObject.has(set.getName()))
                    continue;
                if (set instanceof BooleanSetting) {
                    ((BooleanSetting) set).setBoolValue(propertiesObject.get(set.getName()).getAsBoolean());
                } else if (set instanceof ListSetting) {
                    ((ListSetting) set).setListMode(propertiesObject.get(set.getName()).getAsString());
                } else if (set instanceof NumberSetting) {
                    ((NumberSetting) set).setValueNumber(propertiesObject.get(set.getName()).getAsFloat());
                } else if (set instanceof ColorSetting) {
                    ((ColorSetting) set).setColorValue(propertiesObject.get(set.getName()).getAsInt());
                }
            }
        }
    }

    public ScreenHelper getScreenHelper() {
        return this.screenHelper;
    }

    public void onEnable() {
        if (Main.instance.featureDirector.getFeature(ModuleSoundAlert.class).isEnabled()) {
            mc.player.playSound(SoundEvents.BLOCK_WOOD_BUTTON_CLICK_ON, ModuleSoundAlert.volume.getNumberValue(), ModuleSoundAlert.pitch.getNumberValue());
        }
        EventManager.register(this);
        if (!(getLabel().contains("ClickGui") || getLabel().contains("Client Font") || getLabel().contains("Notifications")) && Notifications.notifMode.currentMode.equalsIgnoreCase("Rect") && Notifications.state.getBoolValue()) {
            NotificationRenderer.queue(getLabel(), TextFormatting.GREEN + "was enabled!", 1, NotificationMode.INFO);
        } else if (!(getLabel().contains("ClickGui") || getLabel().contains("Client Font") || getLabel().contains("Notifications")) && Notifications.notifMode.currentMode.equalsIgnoreCase("Chat") && Notifications.state.getBoolValue()) {
            Main.msg(TextFormatting.GRAY + "[Notifications] " + TextFormatting.WHITE + getLabel() + " was" + TextFormatting.GREEN + " enabled!", false);
        }
    }

    public static double deltaTime() {
        return Minecraft.getDebugFPS() > 0 ? (1.0000 / Minecraft.getDebugFPS()) : 1;
    }

    public void onDisable() {
        if (Main.instance.featureDirector.getFeature(ModuleSoundAlert.class).isEnabled()) {
            mc.player.playSound(SoundEvents.BLOCK_WOOD_BUTTON_CLICK_ON, ModuleSoundAlert.volume.getNumberValue(), ModuleSoundAlert.pitch.getNumberValue());

        }
        EventManager.unregister(this);
        if (!(getLabel().contains("ClickGui") || getLabel().contains("Client Font") || getLabel().contains("Notifications")) && Notifications.notifMode.currentMode.equalsIgnoreCase("Rect") && Notifications.state.getBoolValue()) {
            NotificationRenderer.queue(getLabel(), TextFormatting.RED +  "was disabled!", 1, NotificationMode.INFO);
        } else if (!(getLabel().contains("ClickGui") || getLabel().contains("Client Font") || getLabel().contains("Notifications")) && Notifications.notifMode.currentMode.equalsIgnoreCase("Chat") && Notifications.state.getBoolValue()) {
            Main.msg(TextFormatting.GRAY + "[Notifications] " + TextFormatting.WHITE + getLabel() + " was" + TextFormatting.RED + " disabled!", false);
        }

    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void onToggle() {
    }

    public void setEnabled(boolean enabled) {
        if (enabled) {
            EventManager.register(this);
        } else {
            EventManager.unregister(this);
        }
        this.enabled = enabled;
    }

    public void toggle() {
        enabled = !enabled;
        onToggle();
        if (enabled)
            onEnable();
        else
            onDisable();
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String name) {
        this.label = name;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public FeatureCategory getCategory() {
        return category;
    }

    public void setCategory(FeatureCategory category) {
        this.category = category;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getSuffix() {
        return suffix == null ? label : suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
        this.suffix = getLabel() + " " + suffix;
    }


    public void setup() {
    }
}
