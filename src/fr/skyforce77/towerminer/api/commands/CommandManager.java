package fr.skyforce77.towerminer.api.commands;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.api.PluginManager;
import fr.skyforce77.towerminer.api.Utils;
import fr.skyforce77.towerminer.api.events.chat.CommandTypedEvent;
import fr.skyforce77.towerminer.protocol.chat.ChatMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Set;

public class CommandManager {

    private static HashMap<String, Command> commands = new HashMap<String, Command>();
    private static HashMap<String, String> alias = new HashMap<String, String>();

    public static void createCommands() {
        register("help", new CommandHelp());
        register("version", new CommandVersion());
    }

    public static void register(String label, Command command) {
        commands.put(label, command);
        command.label = label;
        command.onInitialized(label);

        for (String s : command.getAlias()) {
            registerAlias(label, s);
        }
    }

    public static void registerAlias(String label, String alia) {
        alias.put(alia, label);
    }

    public static boolean isCommand(String label) {
        if (commands.containsKey(label)) {
            return true;
        }
        if (alias.containsKey(label)) {
            return true;
        }
        return false;
    }

    public static Command getCommand(String label) {
        if (isCommand(label)) {
            if (commands.containsKey(label)) {
                return commands.get(label);
            }
            if (alias.containsKey(label)) {
                return commands.get(alias.get(label));
            }
        } else {
            return null;
        }
        return null;
    }

    public static Set<String> getCommands() {
        return commands.keySet();
    }

    public static void onCommandTyped(CommandSender sender, String label, String[] args) {
        CommandTypedEvent cte = new CommandTypedEvent(label, args);
        PluginManager.callSyncEvent(cte);
        if (!cte.isCancelled()) {
            String s = cte.getLabel();
            for (String a : cte.getArguments()) {
                s = s + " " + a;
            }
            if (isCommand(cte.getLabel())) {
                if (getCommand(cte.getLabel()).isCorrect(cte.getArguments())) {
                    try {
                        getCommand(cte.getLabel()).onTyped(sender, cte.getArguments());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    if (getCommand(cte.getLabel()).getUse() != null) {
                        ChatMessage message = new ChatMessage("Incorrect arguments. Usage: /");
                        message.add(getCommand(cte.getLabel()).getUse());
                        Utils.write(message);
                    } else {
                        Utils.write("Incorrect arguments. Usage: /" + cte.getLabel());
                    }
                }
            } else {
                Utils.write("Unknown command, type /help for help");
            }
        }
    }

    public static void createTerminal() {
        new Thread("TerminalHandler") {
            @Override
            public void run() {
                while (TowerMiner.game != null) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

                    String line = null;

                    try {
                        line = br.readLine();
                    } catch (IOException ioe) {
                        TowerMiner.printError("IO error trying to read command!");
                    }

                    String label;
                    if (line.contains(" ")) {
                        label = line.split(" ")[0];
                    } else {
                        label = line;
                    }

                    String[] args;
                    if (line.contains(" ")) {
                        args = line.replace(label, "").split(" ");
                    } else {
                        args = new String[]{""};
                    }

                    onCommandTyped(new CommandSender(SenderType.CONSOLE), label, args);
                }
            }
        }.start();
    }

}
