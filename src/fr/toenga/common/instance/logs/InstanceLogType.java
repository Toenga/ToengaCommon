package fr.toenga.common.instance.logs;

public class InstanceLogType {
	private String 		logTypeId;
	private ValueType 	valueType;
	
	// reports ?
	
	public enum ValueType
	{
		String,
		Integer,
		Double;
	}
}
