package prot.rich;

import java.net.Socket;

import net.minecraft.client.gui.GuiScreen;

public class Client {

    private Socket socket;

    public static int flex = -1324157812;

    public void connect(String ip, int port) {
        try {
            this.socket = new Socket(ip, port);
        }catch(Exception ex) {
            System.exit(-1);
        }
    }

    public GuiScreen getScreen(String s) {
        try {
            return (GuiScreen) Class.forName(s).newInstance();
        }catch(Exception ex) {
        	return new GuiCrack();
        }
    }

    public Socket getSocket() {
        return this.socket;
    }

}