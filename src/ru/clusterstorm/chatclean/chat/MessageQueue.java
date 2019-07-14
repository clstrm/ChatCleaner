package ru.clusterstorm.chatclean.chat;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

public class MessageQueue {

	private Map<String, PlayerQueue> messages = new HashMap<String, PlayerQueue>();
	
	private ChatMessage[] array;
	private int pointer;
	
	public MessageQueue() {
		array = new ChatMessage[100];
	}
	
	public synchronized void addPlayer(Player p) {
		messages.put(p.getName(), new PlayerQueue(p));
	}
	
	public synchronized void removePlayer(Player p) {
		messages.remove(p.getName());
	}
	
	public synchronized void putChatMessage(ChatMessage msg) {
		msg.setId(pointer);
		array[pointer] = msg;
		pointer++;
		if(pointer >= array.length) pointer = 0;
	}
	
	public synchronized boolean deleteMessage(int id) {
		if(id < array.length && array[id] != null) {
			array[id].setDeleted(true);
			return true;
		}
		return false;
	}
	
	public synchronized boolean restoreMessage(int id) {
		if(id < array.length && array[id] != null) {
			array[id].setDeleted(false);
			return true;
		}
		return false;
	}
	
	public synchronized void resend() {
		for(PlayerQueue queue : messages.values()) {
			queue.resend();
		}
	}
	
	public synchronized PlayerQueue getPlayer(Player p) {
		PlayerQueue q = messages.get(p.getName());
		if(q == null) {
			q = new PlayerQueue(p);
			messages.put(p.getName(), q);
		}
		return q;
	}
	
	
	
	
	
	
	
	
	
	
}
