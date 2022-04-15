package zxc.rich.client.ui.clickgui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;

import zxc.rich.Main;
import zxc.rich.api.utils.render.BlurUtil;
import zxc.rich.api.utils.render.ColorUtils;
import zxc.rich.api.utils.render.RenderUtils;
import zxc.rich.client.features.Feature;
import zxc.rich.client.features.FeatureCategory;
import zxc.rich.client.features.FeatureDirector;
import zxc.rich.client.features.hud.ClickGUI;
import zxc.rich.client.ui.clickgui.component.AnimationState;
import zxc.rich.client.ui.clickgui.component.Component;
import zxc.rich.client.ui.clickgui.component.DraggablePanel;
import zxc.rich.client.ui.clickgui.component.ExpandableComponent;
import zxc.rich.client.ui.clickgui.component.impl.ModuleComponent;

import java.awt.*;
import java.util.List;

public final class Panel extends DraggablePanel {
    Minecraft mc = Minecraft.getMinecraft();
    public static final int HEADER_WIDTH = 100;
    public static final int X_ITEM_OFFSET = 1;
    public static final int ITEM_HEIGHT = 15;
    public static final int HEADER_HEIGHT = 17;
    public List<Feature> features;
    public FeatureCategory type;
    public AnimationState state;
    private int prevX;
    private int prevY;
    private boolean dragging;

    public Panel(FeatureCategory category, int x, int y) {
        super(null, category.name(), x, y, HEADER_WIDTH, HEADER_HEIGHT);
        int moduleY = HEADER_HEIGHT;
        this.state = AnimationState.STATIC;
        this.features = Main.instance.featureDirector.getFeaturesForCategory(category);
        for (Feature feature : features) {
            this.components.add(new ModuleComponent(this, feature, X_ITEM_OFFSET, moduleY, HEADER_WIDTH - (X_ITEM_OFFSET * 2), ITEM_HEIGHT));
            moduleY += ITEM_HEIGHT;
        }
        this.type = category;
    }

    @Override
    public void drawComponent(ScaledResolution scaledResolution, int mouseX, int mouseY) {
        if (dragging) {
            setX(mouseX - prevX);
            setY(mouseY - prevY);
        }
        int x = getX();
        int y = getY();
        int width = getWidth();
        int height = getHeight();
        int headerHeight;
        int heightWithExpand = getHeightWithExpand();
        headerHeight = (isExpanded() ? heightWithExpand : height);

        Color color = new Color(ClickGUI.color.getColorValue());
        Color onecolor = new Color(ClickGUI.color.getColorValue());
        switch (ClickGUI.clickGuiColor.currentMode) {
            case "Astolfo":
                color = ColorUtils.astolfo(true, (int) y);
                break;
            case "Static":
                color = onecolor;
                break;
            case "Rainbow":
                color = ColorUtils.rainbow(300, 1, 1);
                break;
        }
        float extendedHeight = 2;
        RenderUtils.drawGradientRect(x - 2, y, x + width + 2, y + height - 2.5f, new Color(20, 20, 20, 215).getRGB(), new Color(35, 35, 35, 215).getRGB());

        RenderUtils.drawRect(x, y + 14.5f, x + width, y + headerHeight - extendedHeight, new Color(20, 20, 20, 215).getRGB());

        RenderUtils.drawRect(x, y + headerHeight - extendedHeight, x + width, y + headerHeight - extendedHeight + 1.3f, color.getRGB());
        RenderUtils.drawGlowRoundedRect(x - 3, y + headerHeight - extendedHeight - 3, x + width + 2.8f, y + headerHeight - extendedHeight + 4, new Color(color.getRed(), color.getGreen(), color.getBlue(), 140).getRGB(), 10, 10);

        RenderUtils.drawRect(x, y + headerHeight - extendedHeight, x + width, y + headerHeight - extendedHeight + 1.3f, color.getRGB());

        mc.neverlose500_20.drawCenteredString(getName(), x + 50, y + HEADER_HEIGHT / 2F - 4, Color.LIGHT_GRAY.getRGB());

        super.drawComponent(scaledResolution, mouseX, mouseY);

        if (isExpanded()) {
            for (Component component : components) {
                component.setY(height);
                component.drawComponent(scaledResolution, mouseX, mouseY);
                int cHeight = component.getHeight();
                if (component instanceof ExpandableComponent) {
                    ExpandableComponent expandableComponent = (ExpandableComponent) component;
                    if (expandableComponent.isExpanded()) {
                        cHeight = expandableComponent.getHeightWithExpand() + 5;
                    }
                }
                height += cHeight;
            }
        }
    }

    @Override
    public void onPress(int mouseX, int mouseY, int button) {
        if (button == 0 && !this.dragging) {
            dragging = true;
            prevX = mouseX - getX();
            prevY = mouseY - getY();
        }
    }


    @Override
    public void onMouseRelease(int button) {
        super.onMouseRelease(button);
        dragging = false;
    }

    @Override
    public boolean canExpand() {
        return !features.isEmpty();
    }

    @Override
    public int getHeightWithExpand() {
        int height = getHeight();
        if (isExpanded()) {
            for (Component component : components) {
                int cHeight = component.getHeight();
                if (component instanceof ExpandableComponent) {
                    ExpandableComponent expandableComponent = (ExpandableComponent) component;
                    if (expandableComponent.isExpanded())
                        cHeight = expandableComponent.getHeightWithExpand() + 5;
                }
                height += cHeight;
            }
        }
        return height;
    }
}
