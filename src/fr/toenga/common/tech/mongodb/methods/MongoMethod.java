package fr.toenga.common.tech.mongodb.methods;

import fr.toenga.common.tech.mongodb.MongoService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public abstract class MongoMethod 
{
	
	private MongoService mongoService;
	
	public abstract void run(MongoService mongoService2);
	
}
