package fr.toenga.common.tech.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;

import fr.toenga.common.utils.logs.Log;
import fr.toenga.common.utils.logs.LogType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
public abstract class RabbitListener
{

	private RabbitService		rabbitService;
	private String				name;
	private RabbitListenerType	type;
	private boolean				debug;
	@Setter private Consumer	consumer;

	public void load()
	{
		try {
			while (getRabbitService().isAlive()) {
				if (getConsumer() == null) {
					Channel channel = getRabbitService().getChannel();
					switch (getType()) {
					case MESSAGE_BROKER:
						channel.queueDeclare(getName(), false, false, false, null);
						break;
					case SUBSCRIBER:
						channel.exchangeDeclare(getName(), "fanout");
						String tempQueueName = channel.queueDeclare().getQueue();
						channel.queueBind(tempQueueName, getName(), "");
						break;
					default:
						Log.log(LogType.ERROR, "Unknown subscriber.");
					}
					setConsumer(new RabbitConsumer(channel, this));
					channel.basicConsume(getName(), true, getConsumer());
					System.out.println("[RabbitConnector] Loaded listener from " + getName() + " (" + getClass().getSimpleName() + ").");
				}
			}
		}catch(Exception error) {
			System.out.println("[RabbitConnector] Error during a listener bind.");
			error.printStackTrace();
		}
	}

	public abstract void onPacketReceiving(String body);

}
