package zxc.rich.client.ui.settings;



import zxc.rich.Main;
import zxc.rich.client.ui.settings.impls.AltConfig;
import zxc.rich.client.ui.settings.impls.HudConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

public class FileManager {

    public static File directory = new File("C:\\RichClient\\game\\RichConfig");
    public static ArrayList<CustomFile> files = new ArrayList<>();

    public FileManager() {
        files.add(new HudConfig("HudConfig", true));
        files.add(new AltConfig("AltConfig", true));
    }


    public void loadFiles() {
        for (CustomFile file : files) {
            try {
                if (file.loadOnStart()) {
                    file.loadFile();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void saveFiles() {
        for (CustomFile f : files) {
            try {
                f.saveFile();
            } catch (Exception e) {

            }
        }
    }

    public CustomFile getFile(Class<?> clazz) {
        Iterator<CustomFile> customFileIterator = files.iterator();

        CustomFile file;
        do {
            if (!customFileIterator.hasNext()) {
                return null;
            }

            file = customFileIterator.next();
        } while (file.getClass() != clazz);

        return file;
    }

    public abstract static class CustomFile {

        private final File file;
        private final String name;
        private final boolean load;

        public CustomFile(String name, boolean loadOnStart) {
            this.name = name;
            this.load = loadOnStart;
            this.file = new File(FileManager.directory, name + ".json");
            if (!this.file.exists()) {
                try {
                    this.saveFile();
                } catch (Exception e) {

                }
            }
        }

        public final File getFile() {
            return this.file;
        }

        private boolean loadOnStart() {
            return this.load;
        }

        public final String getName() {
            return this.name;
        }

        public abstract void loadFile() throws Exception;

        public abstract void saveFile() throws Exception;
    }
}
