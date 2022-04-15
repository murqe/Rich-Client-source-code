package zxc.rich.client.features.visuals;

import net.minecraft.tileentity.*;
import net.minecraft.util.math.BlockPos;
import zxc.rich.api.event.EventTarget;
import zxc.rich.api.event.events.impl.Event3D;
import zxc.rich.api.utils.render.ClientHelper;
import zxc.rich.api.utils.render.RenderUtils;
import zxc.rich.client.features.Feature;
import zxc.rich.client.features.FeatureCategory;
import zxc.rich.client.ui.settings.impl.BooleanSetting;
import zxc.rich.client.ui.settings.impl.ColorSetting;

import java.awt.*;

public class BlockESP extends Feature {

    private final BooleanSetting enderChest;
    private final BooleanSetting chest;
    private final BooleanSetting clientColor;
    private final ColorSetting spawnerColor;
    private final BooleanSetting espOutline;
    private final ColorSetting chestColor;
    private final ColorSetting enderChestColor;
    private final ColorSetting shulkerColor;
    private final ColorSetting bedColor;
    private final BooleanSetting bed;
    private final BooleanSetting shulker;
    private final BooleanSetting spawner;

    public BlockESP() {
        super("BlockESP", "Подсвечивает опредленные блоки", FeatureCategory.VISUALS);
        chest = new BooleanSetting("Chest", true, () -> true);
        enderChest = new BooleanSetting("Ender Chest", false, () -> true);
        spawner = new BooleanSetting("Spawner", false, () -> true);
        shulker = new BooleanSetting("Shulker", false, () -> true);
        bed = new BooleanSetting("Bed", false, () -> true);
        chestColor = new ColorSetting("Chest Color", new Color(0xEE2CFF).getRGB(), chest::getBoolValue);
        enderChestColor = new ColorSetting("EnderChest Color", new Color(0xEE2CFF).getRGB(), enderChest::getBoolValue);
        shulkerColor = new ColorSetting("Shulker Color", new Color(0xEE2CFF).getRGB(), shulker::getBoolValue);
        spawnerColor = new ColorSetting("Spawner Color", new Color(0xEE2CFF).getRGB(), spawner::getBoolValue);
        bedColor = new ColorSetting("Bed Color", new Color(0xEE2CFF).getRGB(), bed::getBoolValue);
        clientColor = new BooleanSetting("Client Colors", false, () -> true);
        espOutline = new BooleanSetting("ESP Outline", false, () -> true);
        addSettings(espOutline, chest, enderChest, spawner, shulker, bed, chestColor, enderChestColor, spawnerColor, shulkerColor, bedColor, clientColor);
    }

    @EventTarget
    public void onRender3D(Event3D event) {
        Color colorChest = clientColor.getBoolValue() ? ClientHelper.getClientColor() : new Color(chestColor.getColorValue());
        Color enderColorChest = clientColor.getBoolValue() ? ClientHelper.getClientColor() : new Color(enderChestColor.getColorValue());
        Color shulkColor = clientColor.getBoolValue() ? ClientHelper.getClientColor() : new Color(shulkerColor.getColorValue());
        Color bedColoR = clientColor.getBoolValue() ? ClientHelper.getClientColor() : new Color(bedColor.getColorValue());
        Color spawnerColoR = clientColor.getBoolValue() ? ClientHelper.getClientColor() : new Color(spawnerColor.getColorValue());
        if (mc.player != null || mc.world != null) {
            for (TileEntity entity : mc.world.loadedTileEntityList) {
                BlockPos pos = entity.getPos();
                if (entity instanceof TileEntityChest && chest.getBoolValue()) {
                    RenderUtils.blockEsp(pos, new Color(colorChest.getRGB()), espOutline.getBoolValue());
                } else if (entity instanceof TileEntityEnderChest && enderChest.getBoolValue()) {
                    RenderUtils.blockEsp(pos, new Color(enderColorChest.getRGB()), espOutline.getBoolValue());
                } else if (entity instanceof TileEntityBed && bed.getBoolValue()) {
                    RenderUtils.blockEsp(pos, new Color(bedColoR.getRGB()), espOutline.getBoolValue());
                } else if (entity instanceof TileEntityShulkerBox && shulker.getBoolValue()) {
                    RenderUtils.blockEsp(pos, new Color(shulkColor.getRGB()), espOutline.getBoolValue());
                } else if (entity instanceof TileEntityMobSpawner && spawner.getBoolValue()) {
                    RenderUtils.blockEsp(pos, new Color(spawnerColoR.getRGB()), espOutline.getBoolValue());
                }
            }
        }
    }
}
