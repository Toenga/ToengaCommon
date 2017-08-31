package fr.toenga.common.tech.mongodb.setting;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * A MongoCredentials object
 * @author xMalware
 *
 */
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public class MongoSettings
{

	private String[]				hostnames;
	private	int						port;
	private String					username;
	private	String					password;
	private String					database;
	private int						workerThreads;

}
