package fr.toenga.common.tech.rabbitmq.threading;

import com.rabbitmq.client.Channel;

import fr.toenga.common.tech.TechThread;
import fr.toenga.common.tech.rabbitmq.RabbitService;
import fr.toenga.common.tech.rabbitmq.packet.RabbitPacket;
import fr.toenga.common.tech.rabbitmq.packet.RabbitPacketManager;
import fr.toenga.common.utils.logs.Log;
import fr.toenga.common.utils.logs.LogType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class RabbitThread extends TechThread<RabbitPacket> 
{

	private RabbitPacketManager packetManager;

	public RabbitThread(RabbitPacketManager packetManager, int id)
	{
		super("RabbitThread", packetManager.getQueue(), id);
		setPacketManager(packetManager);
		this.start();
	}

	@Override
	public void work(RabbitPacket rabbitPacket) throws Exception
	{
		RabbitService rabbitService = getPacketManager().getRabbitService();
		Channel channel = rabbitService.getChannel();
		if (rabbitPacket.getRabbitMessage() == null)
		{
			return;
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

	@Override
	public String getErrorMessage()
	{
		return "[RabbitConnector] An error occurred while trying to send packet.";
	}

	@Override
	public boolean isServiceAlive()
	{
		return getPacketManager().isAlive();
	}

	private void debugPacket(RabbitPacket rabbitPacket)
	{
		if (!rabbitPacket.isDebug())
		{
			return;	
		}
		Log.log(LogType.DEBUG, "[RabbitConnector] Packet sended to '" + rabbitPacket.getQueue() + "' : " + rabbitPacket.getRabbitMessage().getMessage());
	}

}
