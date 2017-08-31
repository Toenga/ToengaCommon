package fr.toenga.common.tech.rabbitmq.threading;

import java.util.Queue;

import com.rabbitmq.client.Channel;

import fr.toenga.common.tech.rabbitmq.RabbitService;
import fr.toenga.common.tech.rabbitmq.packet.RabbitPacket;
import fr.toenga.common.tech.rabbitmq.packet.RabbitPacketManager;
import fr.toenga.common.utils.logs.Log;
import fr.toenga.common.utils.logs.LogType;
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
		setPacketManager(packetManager);
		this.start();
	}

	@Override
	public void run() 
	{
		synchronized (this) 
		{
			RabbitService rabbitService = getPacketManager().getRabbitService();
			while (getPacketManager().isAlive()) 
			{
				// TODO
				Queue<RabbitPacket> queue = getPacketManager().getQueue();
				while (!queue.isEmpty()) 
				{
					try
					{
						Channel channel = rabbitService.getChannel();
						RabbitPacket rabbitPacket = queue.poll();
						if (rabbitPacket == null) 
						{
							continue;
						}
						if (rabbitPacket.getRabbitMessage() == null)
						{
							continue;
						}
						String message = rabbitPacket.getRabbitMessage().toJson();
						switch (rabbitPacket.getType())
						{
						case MESSAGE_BROKER:
							channel.queueDeclare(rabbitPacket.getQueue(), false, false, false, null);
							channel.basicPublish("", rabbitPacket.getQueue(), null, message.getBytes(rabbitPacket.getEncoder().getName()));
							debugPacket(rabbitPacket);
							break;
						case PUBLISHER:
							channel.exchangeDeclare(rabbitPacket.getQueue(), "fanout");
							channel.basicPublish(rabbitPacket.getQueue(), "", null, message.getBytes(rabbitPacket.getEncoder().getName()));
							debugPacket(rabbitPacket);
							break;
						}
					}
					catch (Exception error)
					{
						Log.log(LogType.ERROR, "[RabbitConnector] An error occurred while trying to send packet.");
						error.printStackTrace();
					}
				}
				try
				{
					this.wait();
				}
				catch (InterruptedException e) 
				{
					Log.log(LogType.ERROR, "[RabbitConnector] An error occurred while trying to handle a thread.");
					e.printStackTrace();
				}
			}
		}
	}

	private void debugPacket(RabbitPacket rabbitPacket)
	{
		if (!rabbitPacket.isDebug())
		{
			return;	
		}
		Log.log(LogType.DEBUG, "[RabbitConnector] Packet sended to '" + rabbitPacket.getQueue() + "' : " + rabbitPacket.getRabbitMessage().getMessage());
	}

	public boolean canHandlePacket()
	{
		return isAlive() && getState().equals(State.WAITING);
	}

	public void stirHimself()
	{
		synchronized (this) 
		{
			this.notify();
		}
	}

}
