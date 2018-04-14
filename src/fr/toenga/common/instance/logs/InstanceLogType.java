package fr.toenga.common.instance.logs;

@SuppressWarnings("unused")
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
