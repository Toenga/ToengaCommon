package fr.toenga.common.utils.data;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import com.mongodb.DBObject;

import fr.toenga.common.utils.general.GsonUtils;

public class ExplicitObject
{

	public ExplicitObject(DBObject object)
	{
		if (object == null)
		{
			return;
		}
		for (Field field : this.getClass().getFields())
		{
			field.setAccessible(true);
			
			if (!field.isAccessible())
			{
				continue;
			}
			
			if (Modifier.isTransient(field.getModifiers()))
			{
				continue;
			}
			
			String fieldName = field.getName();
			Object obj = object.get(fieldName);
			try
			{
				field.set(this, obj);
			}
			catch (Exception exception)
			{
				exception.printStackTrace();
			}
		}
	}
	
	@Override
	public String toString()
	{
		return GsonUtils.getGson().toJson(this);
	}
	
}
