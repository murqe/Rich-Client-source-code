package zxc.rich.client.ui.draggable;

import zxc.rich.client.ui.draggable.impl.*;

import java.util.ArrayList;

public class DraggableManager {

    public ArrayList<DraggableModule> mods = new ArrayList<>();

    public DraggableManager() {
        mods.add(new InfoComponent());
        mods.add(new PotionComponent());
        mods.add(new TargetHUDComponent());
        mods.add(new SessionInfoComponent());
    }

    public ArrayList<DraggableModule> getMods() {
        return mods;
    }

    public void setMods(ArrayList<DraggableModule> mods) {
        this.mods = mods;
    }
}