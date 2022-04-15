package zxc.rich.client.ui.components;

import zxc.rich.client.ui.clickgui.component.Component;
import zxc.rich.client.ui.clickgui.component.impl.ModuleComponent;

import java.util.Comparator;

public class SorterHelper implements Comparator<Component> {

    @Override
    public int compare(Component component, Component component2) {
        if (component instanceof ModuleComponent && component2 instanceof ModuleComponent) {
            return component.getName().compareTo(component2.getName());
        }
        return 0;
    }
}