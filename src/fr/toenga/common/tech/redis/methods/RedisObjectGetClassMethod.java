package fr.toenga.common.tech.redis.methods;

import fr.toenga.common.tech.redis.RedisService;
import fr.toenga.common.utils.data.Callback;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import redis.clients.jedis.Jedis;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public class RedisObjectGetClassMethod<T> extends RedisMethod
{

	private RedisService		redisService;
	private String				key;
	private Class<T>			clazz;
	private boolean				indented;
	private Callback<T>			callback;
	
	@Override
	public void work(Jedis jedis) {
		if (getKey() == null)
		{
			return;
		}
		if (getCallback() == null)
		{
			return;
		}
		getCallback().done(getRedisService().getGson(isIndented()).fromJson(jedis.get(getKey()), getClazz()), null);
	}

}
