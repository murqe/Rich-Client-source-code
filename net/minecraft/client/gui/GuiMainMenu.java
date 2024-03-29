package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.Runnables;


import java.awt.Color;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Random;


import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.realms.RealmsBridge;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.WorldServerDemo;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.WorldInfo;
import optifine.CustomPanorama;
import optifine.CustomPanoramaProperties;
import optifine.Reflector;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.glu.Project;
import prot.rich.UserData;
import zxc.rich.api.utils.render.animbackground;
import zxc.rich.client.ui.altmanager.GuiAltManager;


public class GuiMainMenu extends GuiScreen {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final Random RANDOM = new Random();
    private final float updateCounter;
    private String splashText;
    private GuiButton buttonResetDemo;
    private float panoramaTimer;
    private DynamicTexture viewportTexture;
    private final Object threadLock = new Object();
    public static final String MORE_INFO_TEXT = "Please click " + TextFormatting.UNDERLINE + "here"
            + TextFormatting.RESET + " for more information.";
    private int openGLWarning2Width;

    private int openGLWarning1Width;

    /**
     * Left x coordinate of the OpenGL warning
     */
    private int openGLWarningX1;

    /**
     * Top y coordinate of the OpenGL warning
     */
    private int openGLWarningY1;

    /**
     * Right x coordinate of the OpenGL warning
     */
    private int openGLWarningX2;

    /**
     * Bottom y coordinate of the OpenGL warning
     */
    private int openGLWarningY2;

    /**
     * OpenGL graphics card warning.
     */
    private String openGLWarning1;

    /**
     * OpenGL graphics card warning.
     */
    private String openGLWarning2;

    /**
     * Link to the Mojang Support about minimum requirements
     */
    private String openGLWarningLink;
    private static final ResourceLocation SPLASH_TEXTS = new ResourceLocation("texts/splashes.txt");
    private static final ResourceLocation MINECRAFT_TITLE_TEXTURES = new ResourceLocation(
            "textures/gui/title/minecraft.png");
    private static final ResourceLocation field_194400_H = new ResourceLocation("textures/gui/title/edition.png");

    private static final ResourceLocation[] TITLE_PANORAMA_PATHS = new ResourceLocation[]{
            new ResourceLocation("textures/gui/title/background/panorama_0.png"),
            new ResourceLocation("textures/gui/title/background/panorama_1.png"),
            new ResourceLocation("textures/gui/title/background/panorama_2.png"),
            new ResourceLocation("textures/gui/title/background/panorama_3.png"),
            new ResourceLocation("textures/gui/title/background/panorama_4.png"),
            new ResourceLocation("textures/gui/title/background/panorama_5.png")};
    private ResourceLocation backgroundTexture;

    private GuiButton realmsButton;

    private boolean hasCheckedForRealmsNotification;

    private GuiScreen realmsNotification;
    private int field_193978_M;
    private int field_193979_N;
    private GuiButton modButton;
    private GuiScreen modUpdateNotification;
    private final long initTime = System.currentTimeMillis();
    private final animbackground backgroundShader;

    public GuiMainMenu() {
        this.openGLWarning2 = MORE_INFO_TEXT;
        this.splashText = "missingno";
        IResource iresource = null;

        try {
            this.backgroundShader = new animbackground("/noise.fsh");
        } catch (IOException var2) {
            throw new IllegalStateException("Failed to load backgound shader", var2);
        }

        try {
            List<String> list = Lists.<String>newArrayList();
            iresource = Minecraft.getMinecraft().getResourceManager().getResource(SPLASH_TEXTS);
            BufferedReader bufferedreader = new BufferedReader(
                    new InputStreamReader(iresource.getInputStream(), StandardCharsets.UTF_8));
            String s;

            while ((s = bufferedreader.readLine()) != null) {
                s = s.trim();

                if (!s.isEmpty()) {
                    list.add(s);
                }
            }

            if (!list.isEmpty()) {
                while (true) {
                    this.splashText = list.get(RANDOM.nextInt(list.size()));

                    if (this.splashText.hashCode() != 125780783) {
                        break;
                    }
                }
            }
        } catch (IOException var8) {
            ;
        } finally {
            IOUtils.closeQuietly((Closeable) iresource);
        }

        this.updateCounter = RANDOM.nextFloat();
        this.openGLWarning1 = "";

    }

    /**
     * Is there currently a realms notification screen, and are realms notifications
     * enabled?
     */
    private boolean areRealmsNotificationsEnabled() {
        return Minecraft.getMinecraft().gameSettings.getOptionOrdinalValue(GameSettings.Options.REALMS_NOTIFICATIONS)
                && this.realmsNotification != null;
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen() {
        if (this.areRealmsNotificationsEnabled()) {
            this.realmsNotification.updateScreen();
        }
    }

    /**
     * Returns true if this GUI should pause the game when it is displayed in
     * single-player
     */
    public boolean doesGuiPauseGame() {
        return false;
    }

    /**
     * Fired when a key is typed (except F11 which toggles full screen). This is the
     * equivalent of KeyListener.keyTyped(KeyEvent e). Args : character (character
     * on the key), keyCode (lwjgl Keyboard key code)
     */
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when
     * the GUI is displayed and when the window resizes, the buttonList is cleared
     * beforehand.
     */
    public void initGui() {
        this.viewportTexture = new DynamicTexture(256, 256);
        this.backgroundTexture = this.mc.getTextureManager().getDynamicTextureLocation("1",
                this.viewportTexture);
        this.field_193978_M = this.fontRendererObj.getStringWidth("1");
        this.field_193979_N = this.width - this.field_193978_M - 49;

        int i = 24;
        int j = this.height / 4 + 48;

        if (this.mc.isDemo()) {
            this.addDemoButtons(j, 24);
        } else {
            this.addSingleplayerMultiplayerButtons(j, 24);
        }

        this.buttonList.add(new GuiButton(0, this.width / 2 - 45, j + 73, 100, 17, I18n.format("Options")));
        this.buttonList.add(new GuiButton(4, this.width / 2 - 45, j + 91, 100, 17, "Quit"));

        synchronized (this.threadLock) {
            this.openGLWarning1Width = this.fontRendererObj.getStringWidth(this.openGLWarning1);
            this.openGLWarning2Width = this.fontRendererObj.getStringWidth(this.openGLWarning2);
            int k = Math.max(this.openGLWarning1Width, this.openGLWarning2Width);
            this.openGLWarningX1 = (this.width - k) / 2;
            this.openGLWarningY1 = (this.buttonList.get(0)).yPosition - 24;
            this.openGLWarningX2 = this.openGLWarningX1 + k;
            this.openGLWarningY2 = this.openGLWarningY1 + 24;
        }

        this.mc.setConnectedToRealms(false);

        if (Minecraft.getMinecraft().gameSettings.getOptionOrdinalValue(GameSettings.Options.REALMS_NOTIFICATIONS)
                && !this.hasCheckedForRealmsNotification) {
            RealmsBridge realmsbridge = new RealmsBridge();
            this.realmsNotification = realmsbridge.getNotificationScreen(this);
            this.hasCheckedForRealmsNotification = true;
        }

        if (this.areRealmsNotificationsEnabled()) {
            this.realmsNotification.setGuiSize(this.width, this.height);
            this.realmsNotification.initGui();
        }

        if (Reflector.NotificationModUpdateScreen_init.exists()) {
            this.modUpdateNotification = (GuiScreen) Reflector.call(Reflector.NotificationModUpdateScreen_init, this,
                    this.modButton);
        }

    }

    /**
     * Adds Singleplayer and Multiplayer buttons on Main Menu for players who have
     * bought the game.
     */
    private void addSingleplayerMultiplayerButtons(int p_73969_1_, int p_73969_2_) {
        this.buttonList.add(new GuiButton(1, this.width / 2 - 45, p_73969_1_ + 19, 100, 17, I18n.format("Singleplayer")));
        this.buttonList.add(new GuiButton(2, this.width / 2 - 45, p_73969_1_ + p_73969_2_ * 1 + 13, 100, 17, I18n.format("Multiplayer")));

        if (Reflector.GuiModList_Constructor.exists()) {
            this.realmsButton = this.addButton(new GuiButton(14, this.width / 2, p_73969_1_ + p_73969_2_ * 2, 98,
                    20, I18n.format("menu.online").replace("Minecraft", "").trim()));
            this.buttonList.add(this.modButton = new GuiButton(6, this.width / 2 - 100, p_73969_1_ + p_73969_2_ * 2, 98,
                    20, I18n.format("fml.menu.mods")));
        } else {
            this.realmsButton = this.addButton(
                    new GuiButton(14, this.width / 2 - 45, p_73969_1_ + p_73969_2_ * 2 + 7, 100, 17, I18n.format("Alt Manager")));
        }
    }

    /**
     * Adds Demo buttons on Main Menu for players who are playing Demo.
     */
    private void addDemoButtons(int p_73972_1_, int p_73972_2_) {
        this.buttonList.add(new GuiButton(11, this.width / 2 - 100, p_73972_1_, I18n.format("menu.playdemo")));
        this.buttonResetDemo = this.addButton(
                new GuiButton(12, this.width / 2 - 100, p_73972_1_ + p_73972_2_ * 1, I18n.format("menu.resetdemo")));
        ISaveFormat isaveformat = this.mc.getSaveLoader();
        WorldInfo worldinfo = isaveformat.getWorldInfo("Demo_World");

        if (worldinfo == null) {
            this.buttonResetDemo.enabled = false;
        }
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for
     * buttons)
     */
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 0) {
            this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
        }

        if (button.id == 1) {
            this.mc.displayGuiScreen(new GuiWorldSelection(this));
        }

        if (button.id == 2) {
            this.mc.displayGuiScreen(new GuiMultiplayer(this));
        }

        if (button.id == 14) {
            this.mc.displayGuiScreen(new GuiAltManager());
        }

        if (button.id == 4) {
            this.mc.shutdown();
        }

        if (button.id == 6 && Reflector.GuiModList_Constructor.exists()) {
            this.mc.displayGuiScreen((GuiScreen) Reflector.newInstance(Reflector.GuiModList_Constructor, this));
        }

        if (button.id == 11) {
            this.mc.launchIntegratedServer("Demo_World", "Demo_World", WorldServerDemo.DEMO_WORLD_SETTINGS);
        }

        if (button.id == 12) {
            ISaveFormat isaveformat = this.mc.getSaveLoader();
            WorldInfo worldinfo = isaveformat.getWorldInfo("Demo_World");

            if (worldinfo != null) {
                this.mc.displayGuiScreen(new GuiYesNo(this, I18n.format("selectWorld.deleteQuestion"),
                        "'" + worldinfo.getWorldName() + "' " + I18n.format("selectWorld.deleteWarning"),
                        I18n.format("selectWorld.deleteButton"), I18n.format("gui.cancel"), 12));
            }
        }
    }

    private void switchToRealms() {
        RealmsBridge realmsbridge = new RealmsBridge();
        realmsbridge.switchToRealms(this);
    }

    public void confirmClicked(boolean result, int id) {
        if (result && id == 12) {
            ISaveFormat isaveformat = this.mc.getSaveLoader();
            isaveformat.flushCache();
            isaveformat.deleteWorldDirectory("Demo_World");
            this.mc.displayGuiScreen(this);
        } else if (id == 12) {
            this.mc.displayGuiScreen(this);
        } else if (id == 13) {
            if (result) {
                try {
                    Class<?> oclass = Class.forName("java.awt.Desktop");
                    Object object = oclass.getMethod("getDesktop").invoke((Object) null);
                    oclass.getMethod("browse", URI.class).invoke(object, new URI(this.openGLWarningLink));
                } catch (Throwable throwable1) {
                    LOGGER.error("Couldn't open link", throwable1);
                }
            }

            this.mc.displayGuiScreen(this);
        }
    }

    /**
     * Draws the main menu panorama
     */
    private void drawPanorama(int mouseX, int mouseY, float partialTicks) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.matrixMode(5889);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        Project.gluPerspective(120.0F, 1.0F, 0.05F, 10.0F);
        GlStateManager.matrixMode(5888);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.disableCull();
        GlStateManager.depthMask(false);
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
                GlStateManager.DestFactor.ZERO);
        int i = 8;
        int j = 64;
        CustomPanoramaProperties custompanoramaproperties = CustomPanorama.getCustomPanoramaProperties();

        if (custompanoramaproperties != null) {
            j = custompanoramaproperties.getBlur1();
        }

        for (int k = 0; k < j; ++k) {
            GlStateManager.pushMatrix();
            float f = ((float) (k % 8) / 8.0F - 0.5F) / 64.0F;
            float f1 = ((float) (k / 8) / 8.0F - 0.5F) / 64.0F;
            float f2 = 0.0F;
            GlStateManager.translate(f, f1, 0.0F);
            GlStateManager.rotate(MathHelper.sin(this.panoramaTimer / 400.0F) * 25.0F + 20.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(-this.panoramaTimer * 0.1F, 0.0F, 1.0F, 0.0F);

            for (int l = 0; l < 6; ++l) {
                GlStateManager.pushMatrix();

                if (l == 1) {
                    GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
                }

                if (l == 2) {
                    GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
                }

                if (l == 3) {
                    GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
                }

                if (l == 4) {
                    GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
                }

                if (l == 5) {
                    GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
                }

                ResourceLocation[] aresourcelocation = TITLE_PANORAMA_PATHS;

                if (custompanoramaproperties != null) {
                    aresourcelocation = custompanoramaproperties.getPanoramaLocations();
                }

                this.mc.getTextureManager().bindTexture(aresourcelocation[l]);
                bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                int i1 = 255 / (k + 1);
                float f3 = 0.0F;
                bufferbuilder.pos(-1.0D, -1.0D, 1.0D).tex(0.0D, 0.0D).color(255, 255, 255, i1).endVertex();
                bufferbuilder.pos(1.0D, -1.0D, 1.0D).tex(1.0D, 0.0D).color(255, 255, 255, i1).endVertex();
                bufferbuilder.pos(1.0D, 1.0D, 1.0D).tex(1.0D, 1.0D).color(255, 255, 255, i1).endVertex();
                bufferbuilder.pos(-1.0D, 1.0D, 1.0D).tex(0.0D, 1.0D).color(255, 255, 255, i1).endVertex();
                tessellator.draw();
                GlStateManager.popMatrix();
            }

            GlStateManager.popMatrix();
            GlStateManager.colorMask(true, true, true, false);
        }

        bufferbuilder.setTranslation(0.0D, 0.0D, 0.0D);
        GlStateManager.colorMask(true, true, true, true);
        GlStateManager.matrixMode(5889);
        GlStateManager.popMatrix();
        GlStateManager.matrixMode(5888);
        GlStateManager.popMatrix();
        GlStateManager.depthMask(true);
        GlStateManager.enableCull();
        GlStateManager.enableDepth();
    }

    /**
     * Rotate and blurs the skybox view in the main menu
     */
    private void rotateAndBlurSkybox() {
        this.mc.getTextureManager().bindTexture(this.backgroundTexture);
        GlStateManager.glTexParameteri(3553, 10241, 9729);
        GlStateManager.glTexParameteri(3553, 10240, 9729);
        GlStateManager.glCopyTexSubImage2D(3553, 0, 0, 0, 0, 0, 256, 256);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
                GlStateManager.DestFactor.ZERO);
        GlStateManager.colorMask(true, true, true, false);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        GlStateManager.disableAlpha();
        int i = 3;
        int j = 3;
        CustomPanoramaProperties custompanoramaproperties = CustomPanorama.getCustomPanoramaProperties();

        if (custompanoramaproperties != null) {
            j = custompanoramaproperties.getBlur2();
        }

        for (int k = 0; k < j; ++k) {
            float f = 1.0F / (float) (k + 1);
            int l = this.width;
            int i1 = this.height;
            float f1 = (float) (k - 1) / 256.0F;
            bufferbuilder.pos((double) l, (double) i1, (double) this.zLevel).tex((double) (0.0F + f1), 1.0D)
                    .color(1.0F, 1.0F, 1.0F, f).endVertex();
            bufferbuilder.pos((double) l, 0.0D, (double) this.zLevel).tex((double) (1.0F + f1), 1.0D)
                    .color(1.0F, 1.0F, 1.0F, f).endVertex();
            bufferbuilder.pos(0.0D, 0.0D, (double) this.zLevel).tex((double) (1.0F + f1), 0.0D)
                    .color(1.0F, 1.0F, 1.0F, f).endVertex();
            bufferbuilder.pos(0.0D, (double) i1, (double) this.zLevel).tex((double) (0.0F + f1), 0.0D)
                    .color(1.0F, 1.0F, 1.0F, f).endVertex();
        }

        tessellator.draw();
        GlStateManager.enableAlpha();
        GlStateManager.colorMask(true, true, true, true);
    }

    /**
     * Renders the skybox in the main menu
     */
    private void renderSkybox(int mouseX, int mouseY, float partialTicks) {
        this.mc.getFramebuffer().unbindFramebuffer();
        GlStateManager.viewport(0, 0, 256, 256);
        this.drawPanorama(mouseX, mouseY, partialTicks);
        this.rotateAndBlurSkybox();
        int i = 3;
        CustomPanoramaProperties custompanoramaproperties = CustomPanorama.getCustomPanoramaProperties();

        if (custompanoramaproperties != null) {
            i = custompanoramaproperties.getBlur3();
        }

        for (int j = 0; j < i; ++j) {
            this.rotateAndBlurSkybox();
            this.rotateAndBlurSkybox();
        }

        this.mc.getFramebuffer().bindFramebuffer(true);
        GlStateManager.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
        float f2 = 120.0F / (float) (this.width > this.height ? this.width : this.height);
        float f = (float) this.height * f2 / 256.0F;
        float f1 = (float) this.width * f2 / 256.0F;
        int k = this.width;
        int l = this.height;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        bufferbuilder.pos(0.0D, (double) l, (double) this.zLevel).tex((double) (0.5F - f), (double) (0.5F + f1))
                .color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        bufferbuilder.pos((double) k, (double) l, (double) this.zLevel).tex((double) (0.5F - f), (double) (0.5F - f1))
                .color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        bufferbuilder.pos((double) k, 0.0D, (double) this.zLevel).tex((double) (0.5F + f), (double) (0.5F - f1))
                .color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        bufferbuilder.pos(0.0D, 0.0D, (double) this.zLevel).tex((double) (0.5F + f), (double) (0.5F + f1))
                .color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        tessellator.draw();
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution sr = new ScaledResolution(mc);
        GlStateManager.disableCull();
        this.backgroundShader.useShader(sr.getScaledWidth() + 800, sr.getScaledHeight(), mouseX, mouseY, (float) (System.currentTimeMillis() - this.initTime) / 1000.0f);
        GL11.glBegin(7);
        GL11.glVertex2f(-1.0f, -1.0f);
        GL11.glVertex2f(-1.0f, 1.0f);
        GL11.glVertex2f(1.0f, 1.0f);
        GL11.glVertex2f(1.0f, -1.0f);
        GL11.glEnd();
        GL20.glUseProgram(0);
        GlStateManager.disableCull();

        mc.neverlose900_30.drawCenteredStringWithOutline("RICHCLIENT", sr.getScaledWidth() / 2 + 5, sr.getScaledHeight() / 2.9f, -1);
        mc.neverlose900_13.drawCenteredStringWithOutline("������� � ������� Jefferson � !dedinside", 78, sr.getScaledHeight() - 9, -1);
        mc.neverlose900_18.drawStringWithOutline("UID " + UserData.instance().getUID(), sr.getScaledWidth() - mc.neverlose900_18.getStringWidth("UID " + UserData.instance().getUID()) - 5, sr.getScaledHeight() - 10 , new Color(255, 255, 255).getRGB());
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        synchronized (this.threadLock) {
            if (!this.openGLWarning1.isEmpty() && !StringUtils.isNullOrEmpty(this.openGLWarningLink)
                    && mouseX >= this.openGLWarningX1 && mouseX <= this.openGLWarningX2
                    && mouseY >= this.openGLWarningY1 && mouseY <= this.openGLWarningY2) {
                GuiConfirmOpenLink guiconfirmopenlink = new GuiConfirmOpenLink(this, this.openGLWarningLink, 13, true);
                guiconfirmopenlink.disableSecurityWarning();
                this.mc.displayGuiScreen(guiconfirmopenlink);
            }
        }

        if (this.areRealmsNotificationsEnabled()) {
            this.realmsNotification.mouseClicked(mouseX, mouseY, mouseButton);
        }

        if (mouseX > this.field_193979_N && mouseX < this.field_193979_N + this.field_193978_M
                && mouseY > this.height - 10 && mouseY < this.height) {
            this.mc.displayGuiScreen(new GuiWinGame(false, Runnables.doNothing()));
        }
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    public void onGuiClosed() {
        if (this.realmsNotification != null) {
            this.realmsNotification.onGuiClosed();
        }
    }
}
