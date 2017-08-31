package fr.toenga.common.tech.rabbitmq;

import java.util.ArrayList;
import java.util.List;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import fr.toenga.common.tech.AutoReconnector;
import fr.toenga.common.utils.logs.Log;
import fr.toenga.common.utils.logs.LogType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class RabbitService extends AutoReconnector
{

	private String					name;
	private RabbitSettings			settings;
	private ConnectionFactory		connectionFactory;
	private Connection				connection;
	private	Channel					channel;
	private boolean					dead;
	private RabbitPacketManager		packetManager;

	private List<RabbitListener>	listeners = new ArrayList<>();

	public RabbitService(String name, RabbitSettings settings)
	{
		setName(name);
		setSettings(settings);
		setConnectionFactory(settings.getFactory());
		setPacketManager(getPacketManager(this));
		reconnect();
	}

	public RabbitService addListener(RabbitListener listener)
	{
		listener.load();
		listeners.add(listener);
		return this;
	}
	
	public void sendPacket(RabbitPacket rabbitPacket)
	{
		RabbitPacketManager.getInstance(this).sendPacket(rabbitPacket);
	}
	
	public void remove()
	{
		setDead(true);
	}
	
	public boolean isAlive()
	{
		return !isDead();
	}

	@Override
	public boolean isConnected() 
	{
		return getConnection() != null && getConnection().isOpen() &&
				getChannel() != null && getChannel().isOpen();
	}

	@Override
	public void reconnect() 
	{
		if (isConnected()) return;
		try 
		{
			long time = System.currentTimeMillis();
			// Create connection
			if (getConnection() == null || !getConnection().isOpen())
				setConnection(getConnectionFactory().newConnection());
			// Create channel
			if (getChannel() == null || !getChannel().isOpen())
				setChannel(getConnection().createChannel());
			Log.log(LogType.ERROR, "Successfully reconnected to RabbitMQ service. (" + (System.currentTimeMillis() - time) + " ms).");
		}
		catch(Exception error) 
		{
			error.printStackTrace();
			setConnectionFactory(settings.getFactory());
			Log.log(LogType.ERROR, "Unable to connect to RabbitMQ service. (" + error.getMessage() + ").");
		}
	}
	
	public static RabbitPacketManager getPacketManager(RabbitService rabbitService)
	{
		return RabbitPacketManager.getInstance(rabbitService);
	}

}
