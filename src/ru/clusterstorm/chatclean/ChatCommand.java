package ru.clusterstorm.chatclean;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ChatCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!sender.hasPermission("chatcleaner.admin")) return true;
		if (args.length < 2) return true;

		if (args[0].equalsIgnoreCase("delete")) {
			ChatCleaner.getInstance().deleteMessage(Integer.parseInt(args[1]));
			return true;
		}

		if (args[0].equalsIgnoreCase("undo")) {
			ChatCleaner.getInstance().restoreMessage(Integer.parseInt(args[1]));
			return true;
		}
		return true;
	}

}
