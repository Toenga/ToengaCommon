package fr.toenga.common.tech.rabbitmq.listener;

import java.io.IOException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import fr.toenga.common.tech.rabbitmq.packet.RabbitPacketMessage;
import fr.toenga.common.utils.logs.Log;
import fr.toenga.common.utils.logs.LogType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode(callSuper = false)
@Getter
@Setter
public class RabbitListenerConsumer extends DefaultConsumer
{

	private RabbitListener	rabbitListener;

	public RabbitListenerConsumer(Channel channel, RabbitListener rabbitListener)
	{
		super(channel);
		this.setRabbitListener(rabbitListener);
	}

	@Override
	public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException
	{
		String message = new String(body, "UTF-8");
		if (getRabbitListener().getRabbitService().isDead()) 
		{
			return;
		}
		try
		{
			RabbitPacketMessage rabbitMessage = RabbitPacketMessage.fromJson(message);
			if (rabbitMessage.isAlive()) 
			{
				if (getRabbitListener().isDebug()) 
				{
					Log.log(LogType.DEBUG, "[RabbitConnector] Received packet from " + getRabbitListener().getName() + ": " + rabbitMessage.getMessage());
				}
				getRabbitListener().onPacketReceiving(rabbitMessage.getMessage());
			}
			else if (getRabbitListener().isDebug())
			{
				Log.log(LogType.ERROR, "[RabbitConnector] Error during a received packet from " + getRabbitListener().getName() + ": EXPIRED!");
				Log.log(LogType.ERROR, "[RabbitConnector] " + rabbitMessage.getMessage());
			}
		}
		catch(Exception error)
		{
			Log.log(LogType.ERROR, "[RabbitConnector] Error during the handle delivery.");
			error.printStackTrace();
		}
	}

}
