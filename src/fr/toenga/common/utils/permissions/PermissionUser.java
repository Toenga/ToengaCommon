package fr.toenga.common.utils.permissions;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.reflect.TypeToken;
import com.google.gson.JsonObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

import fr.toenga.common.utils.general.GsonUtils;
import fr.toenga.common.utils.permissions.Permission.PermissionResult;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class PermissionUser
{

	@SuppressWarnings("serial")
	private static transient Type groupType = new TypeToken<Map<String, Map<String, Long>>>() {}.getType();
	@SuppressWarnings("serial")
	private static transient Type permissionType = new TypeToken<List<Permission>>() {}.getType();

	private Map<String, Map<String, Long>>	groups;
	private List<Permission>				permissions;

	public PermissionUser(JsonObject jsonObject)
	{
		System.out.println(GsonUtils.getPrettyGson().toJson(jsonObject));
		groups = GsonUtils.getPrettyGson().fromJson(jsonObject.get("groups"), groupType);
		if (jsonObject.get("permissions").isJsonNull())
		{
			permissions = new ArrayList<>();
		}
		else
		{
			System.out.println(GsonUtils.getPrettyGson().toJson(jsonObject.get("permissions")));
			//permissions = GsonUtils.getPrettyGson().fromJson(JSON.serialize(), permissionType);
		}
		if (groups == null)
		{
			groups = new HashMap<>();
		}
		if (permissions == null)
		{
			permissions = new ArrayList<>();
		}
	}

	public boolean hasPermission(String place, String permission)
	{
		if (groups == null)
		{
			return false;
		}
		if (!groups.containsKey(place))
		{
			HashMap<String, Long> map = new HashMap<>();
			map.put("default", -1L);
			groups.put(place, map);
			return false;
		}
		Map<String, Long> g = groups.get(place);
		if (g == null)
		{
			return false;
		}
		long time = System.currentTimeMillis();
		PermissionResult permissionResult = null;
		for (Entry<String, Long> entry : g.entrySet())
		{
			if (entry.getValue() <= time)
			{
				continue;
			}
			Permissible permissible = PermissionsManager.getManager().getGroup(entry.getKey());
			if (permissible == null)
			{
				continue;
			}

			permissionResult = permissible.testPermission(new Permission(permission));
			if (permissionResult.equals(PermissionResult.YES))
			{
				return true;
			}
		}
		return false;
	}

	public DBObject getDBObject()
	{
		BasicDBObject dbObject = new BasicDBObject();
		dbObject.put("groups", groups);
		dbObject.put("permissions", groups);
		return dbObject;
	}

}
