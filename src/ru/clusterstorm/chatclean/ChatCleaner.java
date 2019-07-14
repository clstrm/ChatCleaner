package ru.clusterstorm.chatclean;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import ru.clusterstorm.chatclean.chat.ChatMessage;
import ru.clusterstorm.chatclean.chat.MessageQueue;
import ru.clusterstorm.chatclean.protocol.ChatPacketHandler;

public class ChatCleaner extends JavaPlugin {

	public static final String CHAT_FORMAT = "[\"\",{\"text\":\"\u267B\",\"color\":\"red\",\"clickEvent\":"
			+ "{\"action\":\"run_command\",\"value\":\"/chat delete {id}\"},"
			+ "\"hoverEvent\":{\"action\":\"show_text\",\"value\":"
			+ "{\"text\":\"\",\"extra\":[{\"text\":\"Delete message\",\"color\":\"gray\"}]}}},"
			+ "{\"text\":\" {sender}: {message}\",\"color\":\"white\"}]";
	
	public static final String CHAT_FORMAT_DELETED = "[\"\",{\"text\":\"\u2713\",\"color\":\"green\",\"bold\":\"true\",\"clickEvent\":"
			+ "{\"action\":\"run_command\",\"value\":\"/chat undo {id}\"},"
			+ "\"hoverEvent\":{\"action\":\"show_text\",\"value\":"
			+ "{\"text\":\"\",\"extra\":[{\"text\":\"Undo\",\"color\":\"gray\"}]}}},"
			+ "{\"text\":\" {sender}: §7§o<message deleted>\",\"color\":\"white\"}]";

	private static ChatCleaner instance;
	private MessageQueue queue;
	private boolean sending;

	@Override
	public void onEnable() {
		instance = this;
		Bukkit.getPluginManager().registerEvents(new ChatListener(), this);
		getCommand("chat").setExecutor(new ChatCommand());
		
		queue = new MessageQueue();
		
		ChatPacketHandler packet = new ChatPacketHandler(this, ListenerPriority.NORMAL, PacketType.Play.Server.CHAT);
		ProtocolLibrary.getProtocolManager().addPacketListener(packet);
	}

	public void deleteMessage(int id) {
		if(queue.deleteMessage(id)) queue.resend();
	}

	public void restoreMessage(int id) {
		if(queue.restoreMessage(id)) queue.resend();
	}

	public void sendMessage(String sender, String message) {
		ChatMessage msg = new ChatMessage(sender, message);
		queue.putChatMessage(msg);
		sendChatMessage(msg);
	}
	
	private void sendChatMessage(ChatMessage msg) {
		sending = true;
		
		try {
			for (Player p : Bukkit.getOnlinePlayers()) {
				queue.getPlayer(p).putMessage(msg);
				sendChatFormat(msg, p);
			}
		} catch(Exception e) {
			sending = false;
			throw e;
		}
		
		sending = false;
	}

	public static ChatCleaner getInstance() {
		return instance;
	}
	
	public MessageQueue getQueue() {
		return queue;
	}
	
	public boolean isSending() {
		return sending;
	}

	public static void sendChatFormat(ChatMessage msg, Player p) {
		p.spigot().sendMessage(ChatMessageType.CHAT, 
				new TextComponent(ComponentSerializer.parse(
						msg.replace(msg.isDeleted() ? CHAT_FORMAT_DELETED : CHAT_FORMAT))));
	}
}
