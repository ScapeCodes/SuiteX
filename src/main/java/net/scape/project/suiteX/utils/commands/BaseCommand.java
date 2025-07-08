package net.scape.project.suiteX.utils.commands;

import net.scape.project.suiteX.SuiteX;

public abstract class BaseCommand {

    public BaseCommand() {
        SuiteX.getInstance().getCommandFramework().registerCommands(this, null);
    }

    public abstract void executeAs(CommandArguments command);
}
