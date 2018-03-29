package fr.toenga.common.utils.permissions;

import java.util.Map;

import com.google.gson.JsonObject;

import fr.toenga.common.utils.general.GsonUtils;
import lombok.Getter;

public class PermissionsManager
{
	@Getter
	private static PermissionsManager manager;
	
	public static void createPermissionManager(Map<String, Permissible> groups, String place)
	{
		if(manager != null)
		{
			new IllegalStateException("Permission manager already created!");
		}
		
		manager = new PermissionsManager(groups, place);
	}
	
	@Getter
	private String currentPlace;
	private Map<String, Permissible> groups;
	
	public PermissionsManager(Map<String, Permissible> groups, String place)
	{
		this.currentPlace = place;
		reloadGroups(groups);
	}
	
	public void reloadGroups(Map<String, Permissible> groups)
	{
		this.groups = groups;
	}
	
	public Permissible getGroup(String name)
	{
		return groups.get(name);
	}
	
	public Permissible loadPermissible(JsonObject value)
	{
		return GsonUtils.getGson().fromJson(value, Permissible.class);
	}
	
}
