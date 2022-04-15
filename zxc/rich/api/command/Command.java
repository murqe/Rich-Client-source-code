package zxc.rich.api.command;

@FunctionalInterface
public interface Command {
    void execute(String... strings);
}