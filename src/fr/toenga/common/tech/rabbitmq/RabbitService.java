package fr.toenga.common.tech.rabbitmq;

import java.util.ArrayList;
import java.util.List;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import fr.toenga.common.tech.AutoReconnector;
import fr.toenga.common.tech.rabbitmq.listener.RabbitListener;
import fr.toenga.common.tech.rabbitmq.packet.RabbitPacket;
import fr.toenga.common.tech.rabbitmq.packet.RabbitPacketManager;
import fr.toenga.common.tech.rabbitmq.setting.RabbitSettings;
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

	private List<RabbitListener>	listeners = new ArrayList<>();

	public RabbitService(String name, RabbitSettings settings)
	{
		setName(name);
		setSettings(settings);
		setConnectionFactory(settings.toFactory());
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
		getPacketManager().sendPacket(rabbitPacket);
	}

	public void remove()
	{
		if (isDead())
		{
			Log.log(LogType.ERROR, "[RabbitConnector] The service is already dead.");
			return;
		}
		long time = System.currentTimeMillis();
		setDead(true); // Set dead
		cancel(); // Cancel AutoReconnector task
		// Close channel
		try {
			getChannel().close();
		} catch (Exception error) {
			Log.log(LogType.ERROR, "[RabbitConnector] Something gone wrong while trying to close RabbitMQ channel.");
			Log.log(LogType.ERROR, "[RabbitConnector] Otherwhise, we are trying to close connection..");
			error.printStackTrace();
		}
		// Close connection
		try {
			getConnection().close();
		} catch (Exception error) {
			Log.log(LogType.ERROR, "[RabbitConnector] Something gone wrong while trying to close RabbitMQ connection.");
			error.printStackTrace();
			return;
		}
		RabbitConnector.getInstance().getServices().remove(this.getName());
		Log.log(LogType.SUCCESS, "[RabbitConnector] RabbitMQ service disconnected (" + (System.currentTimeMillis() - time) + " ms).");
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
		if (isDead())
		{
			return;
		}
		if (isConnected()) 
		{
			return;
		}
		try 
		{
			long time = System.currentTimeMillis();
			// Create connection
			if (getConnection() == null || !getConnection().isOpen())
				setConnection(getConnectionFactory().newConnection());
			// Create channel
			if (getChannel() == null || !getChannel().isOpen())
				setChannel(getConnection().createChannel());
			// Reload listeners
			listeners.stream().forEach(listener -> listener.load());
			Log.log(LogType.SUCCESS, "[RabbitConnector] Successfully reconnected to RabbitMQ service (" + (System.currentTimeMillis() - time) + " ms).");
		}
		catch(Exception error) 
		{
			error.printStackTrace();
			setConnectionFactory(settings.toFactory());
			Log.log(LogType.ERROR, "[RabbitConnector] Unable to connect to RabbitMQ service (" + error.getMessage() + ").");
		}
	}

	public RabbitPacketManager getPacketManager()
	{
		return RabbitPacketManager.getInstance(this);
	}

}
