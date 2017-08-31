package fr.toenga.common.tech.redis.setting;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * A RedisCredentials object
 * @author xMalware
 */
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public class RedisSettings 
{

	private	String[]		hostnames;
	private	int				port;
	private	String			password;
	private int				database;

}
