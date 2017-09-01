package fr.toenga.common.tech.sql.methods;

import fr.toenga.common.tech.sql.SQLService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public abstract class SQLMethod 
{
	
	private SQLService sqlService;
	
	public abstract void run(SQLService sqlService);
	
}
