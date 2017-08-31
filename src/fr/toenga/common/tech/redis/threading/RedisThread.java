package fr.toenga.common.tech.redis.threading;

import fr.toenga.common.tech.TechThread;
import fr.toenga.common.tech.redis.RedisService;
import fr.toenga.common.tech.redis.methods.RedisMethod;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class RedisThread extends TechThread<RedisMethod> 
{

	private RedisService redisService;

	public RedisThread(RedisService redisService, int id)
	{
		super("RedisThread", redisService.getQueue(), id);
		setRedisService(redisService);
		this.start();
	}

	@Override
	public void work(RedisMethod redisMethod) throws Exception {
		redisMethod.work(getRedisService());
	}

	@Override
	public String getErrorMessage()
	{
		return "[RedisConnector] An error occurred while trying to send packet.";
	}

	@Override
	public boolean isServiceAlive()
	{
		return getRedisService().isAlive();
	}

}
