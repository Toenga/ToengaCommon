package fr.toenga.common.utils.permissions;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.reflect.TypeToken;

import i18n.I18n;
import lombok.Getter;
import lombok.Setter;

public class PermissionManager
{

	private static final Type collectionType = new TypeToken<List<PermissionGroup>>() {}.getType();
	@Getter@Setter
	public static Map<String, List<PermissionGroup>> groups = new HashMap<>(); 
	
	public static void load(String name, String json)
	{
		List<PermissionGroup> permissionGroup = I18n.getGson().fromJson(json, collectionType);
		groups.put(name, permissionGroup);
		System.out.println("Loaded permissions : " + name);
	}
	
}
