package zxc.rich.client.ui.settings.impls;

import zxc.rich.Main;
import zxc.rich.client.ui.draggable.DraggableModule;
import zxc.rich.client.ui.settings.FileManager;

import java.io.*;

public class HudConfig extends FileManager.CustomFile {

    public HudConfig(String name, boolean loadOnStart) {
        super(name, loadOnStart);
    }

    public void loadFile() {
        try {
            FileInputStream fileInputStream = new FileInputStream(this.getFile().getAbsolutePath());
            DataInputStream in = new DataInputStream(fileInputStream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = br.readLine()) != null) {
                String curLine = line.trim();
                String x = curLine.split(":")[1];
                String y = curLine.split(":")[2];
                for (DraggableModule hudModule : Main.instance.draggableManager.getMods()) {
                    if (hudModule.getName().equals(curLine.split(":")[0])) {
                        hudModule.drag.setXPosition(Integer.parseInt(x));
                        hudModule.drag.setYPosition(Integer.parseInt(y));
                    }
                }
            }
            br.close();
        } catch (Exception e) {

        }
    }

    public void saveFile() {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(this.getFile()));
            for (DraggableModule draggableModule : Main.instance.draggableManager.getMods()) {
                out.write(draggableModule.getName() + ":" + draggableModule.drag.getXPosition() + ":" + draggableModule.drag.getYPosition());
                out.write("\r\n");
            }
            out.close();
        } catch (Exception ignored) {

        }
    }
}
