package ru.clusterstorm.chatclean.chat.text;

import org.bukkit.entity.Player;

import ru.clusterstorm.chatclean.ChatCleaner;
import ru.clusterstorm.chatclean.chat.ChatMessage;

public class ChatText implements IText {

	private ChatMessage msg;

	public ChatText(ChatMessage msg) {
		this.msg = msg;
	}
	
	@Override
	public void send(Player p) {
		ChatCleaner.sendChatFormat(msg, p);
	}

}
