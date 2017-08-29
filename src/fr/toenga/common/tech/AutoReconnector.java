package fr.toenga.common.tech;

import java.util.TimerTask;

import fr.toenga.common.utils.logs.TimerUtils;

public abstract class AutoReconnector extends TimerTask 
{

	public AutoReconnector() 
	{
		TimerUtils.getTimer().schedule(this, 1000, 1000);
	}

	public abstract boolean	isConnected();

	public abstract void	reconnect();

	@Override
	public void run() 
	{
		reconnect();
	}

}
