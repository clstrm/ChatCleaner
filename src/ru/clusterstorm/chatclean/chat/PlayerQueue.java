package ru.clusterstorm.chatclean.chat;

import org.bukkit.entity.Player;

import com.comphenix.protocol.wrappers.WrappedChatComponent;

import ru.clusterstorm.chatclean.chat.text.ChatText;
import ru.clusterstorm.chatclean.chat.text.IText;
import ru.clusterstorm.chatclean.chat.text.WrappedText;

public class PlayerQueue {

	private Player player;
	private boolean lock;
	
	private IText[] array;
	private int pointer;
	
	public PlayerQueue(Player player) {
		this.player = player;
		array = new IText[100];
	}
	
	public synchronized void putMessage(WrappedChatComponent component) {
		array[pointer] = new WrappedText(component);
		pointer++;
		if(pointer >= array.length) pointer = 0;
	}
	
	public synchronized void putMessage(ChatMessage message) {
		array[pointer] = new ChatText(message);
		pointer++;
		if(pointer >= array.length) pointer = 0;
	}
	
	
	public synchronized void resend() {
		lock = true;
		try {
			resend0();
		} catch(Throwable e) {
			lock = false;
			throw e;
		}
		lock = false;
	}
	
	private void resend0() {
		for(int i = 0; i < 50; i++) player.sendMessage("\n");
		
		for (int i = pointer; i < array.length; i++) {
			if(array[i] != null) array[i].send(player);
		}
		
		for (int i = 0; i < pointer; i++) {
			if(array[i] != null) array[i].send(player);
		}
	}
	
	public boolean isLocked() {
		return lock;
	}
	
}
