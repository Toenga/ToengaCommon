package fr.toenga.common.utils.permissions;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class Permission
{
	private PermissionResult result;
	private String permission;
	private boolean isAll = false;

	public Permission(String permission)
	{
		result = PermissionResult.YES;

		if(permission == null)
			permission = "";

		char first = permission.charAt( 0 );
		char last  = permission.charAt( permission.length() - 1 );

		if(first == '-')
		{
			result = PermissionResult.NO;
			permission = permission.substring(1);
		}

		if(last == '*')
		{
			isAll = true;
			permission = permission.substring(0, permission.length() - ( permission.length() == 1 ? 1 : 2 ));
		}

		this.permission = permission.toLowerCase();
	}

	public PermissionResult compare(Permission permission)
	{
		String perm1 = getPermission(), perm2 = permission.getPermission();

		if(perm1.equals(perm2) || ( isAll && perm2.startsWith(perm1) ))
		{
			return getResult();
		}

		return PermissionResult.UNKNOW;
	}

	@Override
	public String toString()
	{
		return (result == PermissionResult.NO ? "-" : "")					// -
				+ permission												// permissioon
				+ (isAll ? (permission.isEmpty() ? "*" : ".*") : "");		// *
	}

	public enum PermissionResult
	{
		YES,
		NO,
		UNKNOW;
	}
}
