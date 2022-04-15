package zxc.rich.client.features.player;

import org.lwjgl.input.Keyboard;
import zxc.rich.api.event.EventTarget;
import zxc.rich.api.event.events.impl.EventUpdate;
import zxc.rich.client.features.Feature;
import zxc.rich.client.features.FeatureCategory;

public class GuiWalk extends Feature {
    public GuiWalk() {
        super("GuiWalk", "ѕозвол€ет ходить в открытом контейнере", FeatureCategory.PLAYER);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (!(mc.currentScreen instanceof net.minecraft.client.gui.GuiChat)) {
            mc.gameSettings.keyBindJump.pressed = Keyboard.isKeyDown(mc.gameSettings.keyBindJump.getKeyCode());
            mc.gameSettings.keyBindForward.pressed = Keyboard.isKeyDown(mc.gameSettings.keyBindForward.getKeyCode());
            mc.gameSettings.keyBindBack.pressed = Keyboard.isKeyDown(mc.gameSettings.keyBindBack.getKeyCode());
            mc.gameSettings.keyBindLeft.pressed = Keyboard.isKeyDown(mc.gameSettings.keyBindLeft.getKeyCode());
            mc.gameSettings.keyBindRight.pressed = Keyboard.isKeyDown(mc.gameSettings.keyBindRight.getKeyCode());
            mc.gameSettings.keyBindSprint.pressed = Keyboard.isKeyDown(mc.gameSettings.keyBindSprint.getKeyCode());
        }
    }

    public void onDisable() {
        mc.gameSettings.keyBindJump.pressed = false;
        mc.gameSettings.keyBindForward.pressed = false;
        mc.gameSettings.keyBindBack.pressed = false;
        mc.gameSettings.keyBindLeft.pressed = false;
        mc.gameSettings.keyBindRight.pressed = false;
        mc.gameSettings.keyBindSprint.pressed = false;
        super.onDisable();
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }
}