package zxc.rich.client.ui.altmanager;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.*;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.util.text.TextFormatting;
import org.apache.commons.lang3.RandomStringUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import zxc.rich.api.utils.render.ColorUtils;
import zxc.rich.api.utils.render.RenderUtils;
import zxc.rich.client.ui.altmanager.alt.Alt;
import zxc.rich.client.ui.altmanager.alt.AltLoginThread;
import zxc.rich.client.ui.altmanager.alt.AltManager;
import zxc.rich.client.ui.altmanager.api.AltService;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GuiAltManager extends GuiScreen {
    public static final AltService altService = new AltService();
    public Alt selectedAlt = null;
    public String status;
    private GuiAltButton login;
    private GuiAltButton remove;
    private GuiAltButton rename;
    private AltLoginThread loginThread;
    private float offset;
    private GuiTextField searchField;

    private ResourceLocation resourceLocation;

    public GuiAltManager() {
        this.status = TextFormatting.DARK_GRAY + "(" + TextFormatting.GRAY + AltManager.registry.size() + TextFormatting.DARK_GRAY + ")";
    }

    private void getDownloadImageSkin(ResourceLocation resourceLocationIn, String username) {
        TextureManager textureManager = mc.getTextureManager();
        textureManager.getTexture(resourceLocationIn);
        ThreadDownloadImageData textureObject = new ThreadDownloadImageData(null, String.format("https://minotar.net/avatar/%s/64.png", StringUtils.stripControlCodes(username)), DefaultPlayerSkin.getDefaultSkin(AbstractClientPlayer.getOfflineUUID(username)), new ImageBufferDownload());
        textureManager.loadTexture(resourceLocationIn, textureObject);
    }

    public void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 0:
                break;
            case 1:
                (this.loginThread = new AltLoginThread(this.selectedAlt)).start();
                break;
            case 2:
                if (this.loginThread != null) {
                    this.loginThread = null;
                }

                AltManager.registry.remove(this.selectedAlt);
                this.status = TextFormatting.GREEN + "Removed.";

                this.selectedAlt = null;
                break;
            case 3:
                this.mc.displayGuiScreen(new GuiAddAlt(this));
                break;
            case 4:
                this.mc.displayGuiScreen(new GuiAltLogin(this));
                break;
            case 5:
                String randomName = RandomStringUtils.randomAlphabetic(5).toLowerCase() + RandomStringUtils.randomNumeric(2);
                (this.loginThread = new AltLoginThread(new Alt(randomName, ""))).start();
                AltManager.registry.add(new Alt(randomName, ""));
                break;
            case 6:
                this.mc.displayGuiScreen(new GuiRenameAlt(this));
                break;
            case 7:
                this.mc.displayGuiScreen(new GuiMainMenu());
                break;
            case 8:
                /*try {
                    AltManager.registry.clear();
                    Main.instance.fileManager.getFile(Alts.class).loadFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }*/

                this.status = TextFormatting.RED + "Refreshed!";
                break;
            case 8931:
                this.mc.displayGuiScreen(new GuiMultiplayer(this));
                break;
            case 4545:
                this.mc.displayGuiScreen(new GuiConnecting(this, this.mc, new ServerData(I18n.format("selectServer.defaultName"), "play.hypixel.net", false)));
                break;
        }
    }

    public void drawScreen(int par1, int par2, float par3) {
        ScaledResolution sr = new ScaledResolution(mc);
        RenderUtils.drawBorderedRect(-5, 0, sr.getScaledWidth() - -6, sr.getScaledHeight(), 0.5F, (new Color(44, 44, 44, 255)).getRGB(), (new Color(33, 33, 33, 255)).getRGB(), true);
        RenderUtils.drawBorderedRect(1F, 1.4F, sr.getScaledWidth() - 1, sr.getScaledHeight() - 1.7F, 0.5F, (new Color(17, 17, 17, 255)).getRGB(), (new Color(33, 33, 33, 255)).getRGB(), true);

        if (Mouse.hasWheel()) {
            int wheel = Mouse.getDWheel();
            if (wheel < 0) {
                this.offset += 26;
                if (this.offset < 0) {
                    this.offset = 0;
                }
            } else if (wheel > 0) {
                this.offset -= 26;
                if (this.offset < 0) {
                    this.offset = 0;
                }
            }
        }


        String altName = "Name: " + (this.mc.session.getUsername());
        mc.neverlose500_18.drawStringWithShadow(altName, 11, 10, 14540253);


        RenderUtils.drawRect(mc.neverlose500_18.getStringWidth(altName) + 14, mc.neverlose500_18.getStringHeight(altName), 9, mc.neverlose500_18.getStringHeight(altName) + 12, ColorUtils.getColor(255, 30));

        GlStateManager.pushMatrix();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();

        mc.neverlose500_18.drawCenteredString("Account Manager", width / 2F, 10, -1);
        mc.neverlose500_18.drawCenteredString(this.loginThread == null ? this.status : this.loginThread.getStatus(), width / 2F, 21, -1);
        GlStateManager.pushMatrix();
        RenderUtils.scissorRect(0.0F, 33.0F, (float) width, (float) (height - 50));
        GL11.glEnable(3089);
        int y = 38;
        int number = 0;
        Iterator<Alt> e = this.getAlts().iterator();

        while (true) {
            Alt alt;
            do {
                if (!e.hasNext()) {
                    GL11.glDisable(3089);
                    GL11.glPopMatrix();
                    super.drawScreen(par1, par2, par3);
                    if (this.selectedAlt == null) {
                        this.login.enabled = false;
                        this.remove.enabled = false;
                        this.rename.enabled = false;
                    } else {
                        this.login.enabled = true;
                        this.remove.enabled = true;
                        this.rename.enabled = true;
                    }

                    if (Keyboard.isKeyDown(200)) {
                        this.offset -= 26;
                    } else if (Keyboard.isKeyDown(208)) {
                        this.offset += 26;
                    }

                    if (this.offset < 0) {
                        this.offset = 0;
                    }

                    this.searchField.drawTextBox();
                    if (this.searchField.getText().isEmpty() && !this.searchField.isFocused()) {
                        mc.neverlose500_18.drawStringWithShadow("Search Alt", width / 2 + 125, height - 18, ColorUtils.getColor(180));
                    }
                    return;
                }

                alt = e.next();
            } while (!this.isAltInArea(y));

            ++number;
            String name;
            if (alt.getMask().equals("")) {
                name = alt.getUsername();
            } else {
                name = alt.getMask();
            }


            String pass;
            if (alt.getPassword().equals("")) {
                pass = "Not License";
            } else {
                pass = alt.getPassword().replaceAll(".", "*");
            }

            if (alt != this.selectedAlt) {
                if (this.isMouseOverAlt(par1, par2, y) && Mouse.isButtonDown(0)) {
                    RenderUtils.drawBorderedRect(width / 2F - 125, (y - this.offset - 4.0F), (width / 1.5F), (y - this.offset + 30), 1.0F, -ColorUtils.getColor(255, 50), ColorUtils.getColor(40, 50), false);
                } else if (this.isMouseOverAlt(par1, par2, y - this.offset)) {
                    RenderUtils.drawBorderedRect(width / 2F - 125, (y - this.offset - 4), (width / 1.5F), (y - this.offset + 30), 1.0F, ColorUtils.getColor(255, 50), ColorUtils.getColor(40, 50), false);
                }
            } else {
                if (this.isMouseOverAlt(par1, par2, y) && Mouse.isButtonDown(0)) {
                    RenderUtils.drawBorderedRect(width / 2F - 125, (y - this.offset - 4), (width / 1.5F), (y - this.offset + 30), 1.0F, ColorUtils.getColor(255, 50), ColorUtils.getColor(40, 50), false);
                } else if (this.isMouseOverAlt(par1, par2, y - this.offset)) {
                    RenderUtils.drawBorderedRect(width / 2F - 125, (y - this.offset - 4), (width / 1.5F), (y - this.offset + 30), 1.0F, ColorUtils.getColor(255, 50), ColorUtils.getColor(40, 50), false);
                } else {
                    RenderUtils.drawBorderedRect(width / 2F - 125, (y - this.offset - 4), (width / 1.5F), (y - this.offset + 30), 1.0F, ColorUtils.getColor(255, 50), ColorUtils.getColor(40, 50), false);
                }

            }

            String numberP = TextFormatting.GRAY + "" + number + ". " + TextFormatting.RESET;
            GlStateManager.pushMatrix();
            GlStateManager.color(1, 1, 1, 1);
            if (this.resourceLocation == null) {
                this.resourceLocation = AbstractClientPlayer.getLocationSkin(name);
                this.getDownloadImageSkin(this.resourceLocation, name);
            } else {
                mc.getTextureManager().bindTexture(this.resourceLocation);
                GlStateManager.enableTexture2D();
                Gui.drawScaledCustomSizeModalRect(width / 2F - 161, y - this.offset - 4F, 8.0f, 8.0f, 8, 8, 33, 33, 64.0f, 64.0f);
            }
            GlStateManager.popMatrix();
            mc.sfui18.drawCenteredString(numberP + (name), width / 2F, y - this.offset + 5, -1);
            mc.sfui18.drawCenteredString( pass, width / 2F, y - this.offset + 17, ColorUtils.getColor(110));
            y += 40;
        }
    }

    public void initGui() {
        this.searchField = new GuiTextField(this.eventButton, this.mc.fontRendererObj, width / 2 + 116, height - 22, 72, 16);
        this.buttonList.add(this.login = new GuiAltButton(1, width / 2 - 122, height - 48, 100, 20, "Login"));
        this.buttonList.add(this.remove = new GuiAltButton(2, width / 2 - 40, height - 24, 70, 20, "Remove"));
        this.buttonList.add(new GuiAltButton(3, width / 2 + 4 + 86, height - 48, 100, 20, "Add"));
        this.buttonList.add(new GuiAltButton(4, width / 2 - 16, height - 48, 100, 20, "Direct Login"));
        this.buttonList.add(new GuiAltButton(5, width / 2 - 122, height - 24, 78, 20, "Random"));
        this.buttonList.add(this.rename = new GuiAltButton(6, width / 2 + 38, height - 24, 70, 20, "Edit"));
        this.buttonList.add(new GuiAltButton(7, width / 2 - 190, height - 24, 60, 20, "Back"));
        this.login.enabled = false;
        this.remove.enabled = false;
        this.rename.enabled = false;
    }

    protected void keyTyped(char par1, int par2) {
        this.searchField.textboxKeyTyped(par1, par2);
        if ((par1 == '\t' || par1 == '\r') && this.searchField.isFocused()) {
            this.searchField.setFocused(!this.searchField.isFocused());
        }

        try {
            super.keyTyped(par1, par2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isAltInArea(int y) {
        return y - this.offset <= height - 50;
    }

    private boolean isMouseOverAlt(double x, double y, double y1) {
        return x >= width / 2F - 125 && y >= y1 - 4 && x <= width / 1.5 && y <= y1 + 20 && x >= 0 && y >= 33 && x <= width && y <= height - 50;
    }

    protected void mouseClicked(int par1, int par2, int par3) {
        this.searchField.mouseClicked(par1, par2, par3);
        if (this.offset < 0) {
            this.offset = 0;
        }

        double y = 38 - this.offset;

        for (Iterator<Alt> e = this.getAlts().iterator(); e.hasNext(); y += 40) {
            Alt alt = e.next();
            if (this.isMouseOverAlt(par1, par2, y)) {
                if (alt == this.selectedAlt) {
                    this.actionPerformed(login);
                    return;
                }
                this.selectedAlt = alt;
            }
        }

        try {
            super.mouseClicked(par1, par2, par3);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Alt> getAlts() {
        List<Alt> altList = new ArrayList<>();
        Iterator iterator = AltManager.registry.iterator();

        while (true) {
            Alt alt;
            do {
                if (!iterator.hasNext()) {
                    return altList;
                }

                alt = (Alt) iterator.next();
            }
            while (!this.searchField.getText().isEmpty() && !alt.getMask().toLowerCase().contains(this.searchField.getText().toLowerCase()) && !alt.getUsername().toLowerCase().contains(this.searchField.getText().toLowerCase()));

            altList.add(alt);
        }
    }
}
