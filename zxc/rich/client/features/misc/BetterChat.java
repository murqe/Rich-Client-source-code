package zxc.rich.client.features.misc;

import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.network.play.server.SPacketCloseWindow;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import zxc.rich.api.event.EventTarget;
import zxc.rich.api.event.events.impl.EventReceivePacket;
import zxc.rich.client.features.Feature;
import zxc.rich.client.features.FeatureCategory;

public class BetterChat extends Feature {

    private String lastMessage = "";
    private int amount;
    private int line;

    public BetterChat() {
        super("BetterChat", "Убирает спам", FeatureCategory.MISC);
    }

    @EventTarget
    public void onReceivePacket(EventReceivePacket event) {
        SPacketChat sPacketChat;
        Packet<?> packet = event.getPacket();
        if (packet instanceof SPacketCloseWindow) {
            if (mc.currentScreen instanceof GuiChat) {
                event.setCancelled(true);
            }
        } else if (packet instanceof SPacketChat && (sPacketChat = (SPacketChat) packet).getChatType() == ChatType.CHAT) {
            ITextComponent message = sPacketChat.getChatComponent();
            String rawMessage = message.getFormattedText();
            GuiNewChat chatGui = mc.ingameGUI.getChatGUI();
            if (lastMessage.equals(rawMessage)) {
                chatGui.deleteChatLine(line);
                ++amount;
                sPacketChat.getChatComponent().appendText(TextFormatting.GRAY + " [x" + this.amount + "]");
            } else {
                amount = 1;
            }
            ++line;
            lastMessage = rawMessage;
            chatGui.printChatMessageWithOptionalDeletion(message, line);
            if (line > 256) {
                line = 0;
            }
            event.setCancelled(true);
        }
    }
}
