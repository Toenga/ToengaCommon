package fr.toenga.common.utils.permissions;

import java.lang.reflect.Type;
import java.util.Map;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import fr.toenga.common.utils.i18n.I18n;
import lombok.Getter;

public class PermissionsManager
{
	private static final Type collectionType = new TypeToken<Map<String, Permissible>>(){}.getType();

	@Getter
	private static PermissionsManager manager;
	
	public static void createPermissionManager(JsonObject groups, String place)
	{
		if(manager != null)
			new IllegalStateException("Permission manager already created!");
		
		manager = new PermissionsManager(groups, place);
	}
	
	@Getter
	private String currentPlace;
	private Map<String, Permissible> groups;
	
	private PermissionsManager(JsonObject groups, String place)
	{
		this.currentPlace = place;
		reloadGroups(groups);
	}
	
	public void reloadGroups(JsonObject groups)
	{
		this.groups = I18n.getGson().fromJson(groups, collectionType);
	}
	
	public Permissible getGroup(String name)
	{
		return groups.get(name);
	}
	
	public Permissible loadPermissible(JsonObject value)
	{
		return I18n.getGson().fromJson(value, Permissible.class);
	}
}
