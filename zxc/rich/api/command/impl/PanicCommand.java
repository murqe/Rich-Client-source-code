package zxc.rich.api.command.impl;

import zxc.rich.Main;
import zxc.rich.api.command.CommandAbstract;
import zxc.rich.client.features.Feature;

public class PanicCommand extends CommandAbstract {

    public PanicCommand() {
        super("panic", "Disabled all modules", ".panic", "panic");
    }

    @Override
    public void execute(String... args) {
        if (args[0].equalsIgnoreCase("panic")) {
            for (Feature feature : Main.instance.featureDirector.getAllFeatures()) {
                if (feature.isEnabled()) {
                    feature.toggle();
                }
            }
        }
    }
}
