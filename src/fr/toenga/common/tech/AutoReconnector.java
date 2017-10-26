package fr.toenga.common.tech;

import java.util.TimerTask;

import fr.toenga.common.utils.threading.TimerUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public abstract class AutoReconnector extends Service
{
	
	private TimerTask task;

	// 	public RedisService(String name, RedisSettings settings) 
	public AutoReconnector(String name, Settings settings) 
	{
		super(name, settings);
		task = run();
		TimerUtils.getTimer().schedule(task, 1000, 1000);
	}

	public abstract boolean	isConnected();

	public abstract void	reconnect();

	public TimerTask run() 
	{
		return new TimerTask()
		{
			@Override
			public void run()
			{
				reconnect();
			}
		};
	}

}
