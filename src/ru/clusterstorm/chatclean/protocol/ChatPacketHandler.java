package ru.clusterstorm.chatclean.protocol;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.EnumWrappers.ChatType;

import ru.clusterstorm.chatclean.ChatCleaner;
import ru.clusterstorm.chatclean.chat.PlayerQueue;

public class ChatPacketHandler extends PacketAdapter {

	public ChatPacketHandler(Plugin plugin, ListenerPriority priority, PacketType type) {
		super(plugin, priority, type);
	}
	
	@Override
	public void onPacketSending(PacketEvent event) {
		if(event.getPacketType() != PacketType.Play.Server.CHAT) return;
		
		if(ChatCleaner.getInstance().isSending()) return;
		
		PacketContainer packet = event.getPacket();
		Player p = event.getPlayer();
		
		try {
			if(packet.getChatTypes().getValues().get(0) == ChatType.GAME_INFO) return;
		} catch(Exception e) {
			return;
		}
		
		PlayerQueue queue = ChatCleaner.getInstance().getQueue().getPlayer(p);
		if(queue.isLocked()) return;
		
		WrappedChatComponent c = packet.getChatComponents().getValues().get(0);
		queue.putMessage(c);
	}
}
