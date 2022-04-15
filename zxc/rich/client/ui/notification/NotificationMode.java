package zxc.rich.client.ui.notification;

public enum  NotificationMode {
    SUCCESS("Success", "a"),
    WARNING("WARNING", "b"),
    INFO("Information", "c");

    private final String iconString;
    private final String titleString;

    NotificationMode(String titleString, String iconString) {
        this.titleString = titleString;
        this.iconString = iconString;
    }

    public final String getIconString() {
        return iconString;
    }

    public final String getTitleString() {
        return titleString;
    }
}
