package ru.clusterstorm.chatclean.chat;

public class ChatMessage {

	private String player;
	private String message;
	private int id;
	private boolean deleted;
	
	public ChatMessage(String player, String message) {
		this.player = player;
		this.message = message;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public boolean isDeleted() {
		return deleted;
	}
	
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	
	public String replace(String c) {
		return c.replace("{id}", String.valueOf(id)).replace("{sender}", player).replace("{message}", message);
	}
}
