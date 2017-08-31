package fr.toenga.common.tech.rabbitmq;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode(callSuper = false)
@Data
public class RabbitPacketManager 
{

	@Getter private static RabbitPacketManager	instance	= new RabbitPacketManager(32);
	
	private List<RabbitThread>					threads		= new ArrayList<>();
	private	Queue<RabbitPacket>					queue		= new ConcurrentLinkedDeque<>();
	private boolean								dead		= false;
	
	RabbitPacketManager(int threads)
	{
		for(int i=0;i<threads;i++)
		{
			getThreads().add(new RabbitThread(this, i));
		}
	}
	
	public void sendPacket(RabbitPacket rabbitPacket)
	{
		getQueue().add(rabbitPacket);
		dislogeQueue();
	}
	
	private void dislogeQueue()
	{
		Optional<RabbitThread> availableThread = getAvailableThread();
		if (isUnreachable(availableThread)) return;
		RabbitThread thread = availableThread.get();
		thread.stirHimself();
	}
	
	private boolean isUnreachable(Optional<?> optional)
	{
		return optional == null || !optional.isPresent();
	}
	
	private Optional<RabbitThread> getAvailableThread()
	{
		return threads.stream().filter(thread -> thread.canHandlePacket()).findAny();
	}

	public boolean isAlive() {
		return !isDead();
	}
	
}
