package com.duongdev.effortlessbuilding.commands;

import com.duongdev.effortlessbuilding.EffortlessBuilding;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EBCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;
        EffortlessBuilding.PlayerBuildingState state = EffortlessBuilding.getInstance().getPlayerState(player.getUniqueId());

        if (args.length == 0) {
            player.sendMessage(ChatColor.GOLD + "--- Effortless Building ---");
            player.sendMessage(ChatColor.YELLOW + "/eb mirror <x|y|z|off> - Toggle mirror mode");
            player.sendMessage(ChatColor.YELLOW + "/eb array <count> <up|down|north|south|east|west|off> - Toggle array mode");
            player.sendMessage(ChatColor.YELLOW + "/eb clones <count|off> - Toggle radial clones");
            player.sendMessage(ChatColor.YELLOW + "/eb grid <x_count> <z_count>|off - Toggle grid clones");
            player.sendMessage(ChatColor.YELLOW + "/eb undo - Undo last building action");
            return true;
        }

        if (args[0].equalsIgnoreCase("grid")) {
            if (args.length < 2 || args[1].equalsIgnoreCase("off")) {
                state.setGridX(0);
                state.setGridZ(0);
                player.sendMessage(ChatColor.GREEN + "Grid mode disabled.");
            } else if (args.length >= 3) {
                try {
                    int x = Integer.parseInt(args[1]);
                    int z = Integer.parseInt(args[2]);
                    state.setGridX(x);
                    state.setGridZ(z);
                    player.sendMessage(ChatColor.GREEN + "Grid clones enabled: " + x + "x" + z);
                } catch (NumberFormatException e) {
                    player.sendMessage(ChatColor.RED + "Invalid counts.");
                }
            } else {
                player.sendMessage(ChatColor.RED + "Usage: /eb grid <x> <z>");
            }
            return true;
        }

        if (args[0].equalsIgnoreCase("clones")) {
            if (args.length < 2 || args[1].equalsIgnoreCase("off")) {
                state.setRadialCount(0);
                player.sendMessage(ChatColor.GREEN + "Clones (radial) mode disabled.");
            } else {
                try {
                    int count = Integer.parseInt(args[1]);
                    if (count < 2) {
                        player.sendMessage(ChatColor.RED + "Count must be at least 2.");
                        return true;
                    }
                    state.setRadialCount(count);
                    state.setRadialCenter(player.getLocation());
                    player.sendMessage(ChatColor.GREEN + "Radial clones enabled: " + count + " copies around your current location.");
                } catch (NumberFormatException e) {
                    player.sendMessage(ChatColor.RED + "Invalid count.");
                }
            }
            return true;
        }

        if (args[0].equalsIgnoreCase("undo")) {
            if (state.getUndoHistory().isEmpty()) {
                player.sendMessage(ChatColor.RED + "Nothing to undo.");
            } else {
                java.util.List<org.bukkit.block.BlockState> lastChanges = state.getUndoHistory().pop();
                for (org.bukkit.block.BlockState oldState : lastChanges) {
                    oldState.update(true);
                }
                player.sendMessage(ChatColor.GREEN + "Undid last action (" + lastChanges.size() + " blocks).");
            }
            return true;
        }

        if (args[0].equalsIgnoreCase("mirror")) {
            if (args.length < 2 || args[1].equalsIgnoreCase("off")) {
                state.setMirrorEnabled(false);
                player.sendMessage(ChatColor.GREEN + "Mirror mode disabled.");
            } else {
                String axis = args[1].toLowerCase();
                if (axis.equals("x") || axis.equals("y") || axis.equals("z")) {
                    state.setMirrorEnabled(true);
                    state.setMirrorAxis(axis);
                    state.setMirrorCenter(player.getLocation());
                    player.sendMessage(ChatColor.GREEN + "Mirror mode enabled on axis: " + axis + " at your current location.");
                } else {
                    player.sendMessage(ChatColor.RED + "Invalid axis. Use x, y, or z.");
                }
            }
            return true;
        }

        if (args[0].equalsIgnoreCase("array")) {
            if (args.length < 2 || args[1].equalsIgnoreCase("off")) {
                state.setArrayCount(0);
                player.sendMessage(ChatColor.GREEN + "Array mode disabled.");
            } else if (args.length >= 3) {
                try {
                    int count = Integer.parseInt(args[1]);
                    String dir = args[2].toLowerCase();
                    state.setArrayCount(count);
                    state.setArrayDirection(dir);
                    player.sendMessage(ChatColor.GREEN + "Array mode enabled: " + count + " blocks " + dir);
                } catch (NumberFormatException e) {
                    player.sendMessage(ChatColor.RED + "Invalid count.");
                }
            } else {
                player.sendMessage(ChatColor.RED + "Usage: /eb array <count> <direction>");
            }
            return true;
        }

        return false;
    }
}
