package zxc.rich.client.ui;

import zxc.rich.api.utils.Helper;

public class MouseHelper implements Helper {

    public static boolean isHovered(double x, double y, double mouseX, double mouseY, int width, int height) {
        return width > x && height > y && width < mouseX && height < mouseY;
    }
}