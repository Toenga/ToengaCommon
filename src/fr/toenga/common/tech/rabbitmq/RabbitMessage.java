package fr.toenga.common.tech.rabbitmq;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter public class RabbitMessage {

	private long   expire = -1L;
	private String message;

	public RabbitMessage(long ttl, String message) {
		if (ttl > 0) setExpire(System.currentTimeMillis() + ttl);
		setMessage(message);
	}
	
	public RabbitMessage(String message) {
		setMessage(message);
	}
	
	public boolean isAlive() {
		return !isExpired();
	}
	
	public boolean isExpired() {
		return getExpire() != -1L && getExpire() < System.currentTimeMillis();
	}
	
	public String toJson() {
		return RabbitConnector.getInstance().getGson().toJson(this);
	}
	
	public static RabbitMessage fromJson(String message) {
		return RabbitConnector.getInstance().getGson().fromJson(message, RabbitMessage.class);
	}
	
}
