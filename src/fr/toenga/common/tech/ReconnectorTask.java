package fr.toenga.common.tech;

import java.util.TimerTask;

import fr.toenga.common.utils.threading.TimerUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class ReconnectorTask<T extends Settings> extends TimerTask
{
	
	private AutoReconnectedService<T>	autoReconnectedService;

	protected ReconnectorTask(AutoReconnectedService<T> autoReconnectedService)
	{
		setAutoReconnectedService(autoReconnectedService);
		TimerUtils.getTimer().schedule(this, 0, getReconnectorRepeater());
	}

	@Override
	public void run() {
		getAutoReconnectedService().reconnect();
	}
	
	private long getReconnectorRepeater()
	{
		// TODO
		return 1000;
	}
	
}
