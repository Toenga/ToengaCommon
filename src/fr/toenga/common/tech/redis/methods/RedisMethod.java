package fr.toenga.common.tech.redis.methods;

import fr.toenga.common.tech.redis.RedisService;
import redis.clients.jedis.Jedis;

public abstract class RedisMethod
{

	public void work(RedisService redisService) throws Exception
	{
		work(redisService.getJedis());
	}
	
	public abstract void work(Jedis jedis) throws Exception;
	
}
