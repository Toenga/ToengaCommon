package fr.toenga.common.tech.rabbitmq;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class RabbitThread extends Thread 
{

	private RabbitPacketManager packetManager;
	
	public RabbitThread(RabbitPacketManager packetManager, int id)
	{
		super("ToengaCommon/RabbitThread/" + id);
		this.start();
	}
	
	@Override
	public void run() 
	{
		while (getPacketManager().isAlive()) {
			// TODO
		}
	}

	public boolean canHandlePacket()
	{
		return isAlive() && getState().equals(State.WAITING);
	}

	public void stirHimself() {
		synchronized (this) {
			this.notify();
		}
	}
	
}
