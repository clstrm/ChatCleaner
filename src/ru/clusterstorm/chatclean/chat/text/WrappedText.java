package ru.clusterstorm.chatclean.chat.text;

import org.bukkit.entity.Player;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;

public class WrappedText implements IText {

	private WrappedChatComponent component;

	public WrappedText(WrappedChatComponent component) {
		this.component = component;
	}
	
	@Override
	public void send(Player player) {
		try {
			ProtocolManager m = ProtocolLibrary.getProtocolManager();
			PacketContainer p = m.createPacket(PacketType.Play.Server.CHAT);
			p.getChatComponents().write(0, component);
			m.sendServerPacket(player, p);
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

}
