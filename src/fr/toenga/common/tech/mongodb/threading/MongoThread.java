package fr.toenga.common.tech.mongodb.threading;

import fr.toenga.common.tech.TechThread;
import fr.toenga.common.tech.mongodb.MongoService;
import fr.toenga.common.tech.mongodb.methods.MongoMethod;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class MongoThread extends TechThread<MongoMethod> 
{

	private MongoService mongoService;

	public MongoThread(MongoService mongoService, int id)
	{
		super("MongoThread", mongoService.getQueue(), id);
		setMongoService(mongoService);
		this.start();
	}

	@Override
	public void work(MongoMethod mongoMethod) throws Exception {
		mongoMethod.run(getMongoService());
	}

	@Override
	public String getErrorMessage()
	{
		return "[MongoConnector] An error occurred while trying to send packet.";
	}

	@Override
	public boolean isServiceAlive()
	{
		return getMongoService().isAlive();
	}

}
