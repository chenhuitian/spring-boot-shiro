package bunkerchain.shiro;

import java.util.Map;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import bunkerchain.server.UserService;

public class GmailRealm extends AuthorizingRealm {

	@Autowired
	@Qualifier("GmailServiceImpl")
	private UserService userService;
	
	 @Override
	    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
	        Object username = principalCollection.getPrimaryPrincipal();
	        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
	        simpleAuthorizationInfo.setRoles(userService.getRoles(username.toString()));
	        simpleAuthorizationInfo.setStringPermissions(userService.getPrivileges(username.toString()));
	        return simpleAuthorizationInfo;
	    }

	 @Override
	    public boolean supports(AuthenticationToken authenticationToken) {
	        return authenticationToken instanceof UsernamePasswordToken;
	    }
	    /**
	     * 认证
	     *
	     * @param authenticationToken
	     * @return
	     * @throws AuthenticationException
	     */
	    @Override
	    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
	    	UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;

	        String username = token.getUsername();
	        Map<String, Object> userInfo = userService.getUserInfo(username);
	        if (userInfo == null) {
	            throw new UnknownAccountException();
	        }

	        //盐值，此处使用用户名作为盐
	        ByteSource salt = ByteSource.Util.bytes(username);

	        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(username, userInfo.get("password"), salt, getName());
	        return authenticationInfo;
	    }

}
