package bunkerchain.shiro;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import bunkerchain.server.UserService;

public class MyRealm extends AuthorizingRealm {
    /**
     * 授权
     *
     * @param principalCollection
     * @return
     */
	
	@Autowired
	UserService userService;
	
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        Object username = principalCollection.getPrimaryPrincipal();
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.setRoles(userService.getRoles(username.toString()));
        simpleAuthorizationInfo.setStringPermissions(userService.getPrivileges(username.toString()));
        return simpleAuthorizationInfo;
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

    /**
     * 模拟数据库查询，通过用户名获取用户信息
     *
     * @param username
     * @return
     */
    private Map<String, Object> getUserInfo(String username) {
        Map<String, Object> userInfo = null;
        if ("zhangsan".equals(username)) {
            userInfo = new HashMap<>();
            userInfo.put("username", "zhangsan");

            //加密算法，原密码，盐值，加密次数
            userInfo.put("password", new SimpleHash("MD5", "123456", username, 3));
        }
        return userInfo;
    }

    /**
     * 模拟查询数据库，获取用户角色列表
     *
     * @param username
     * @return
     */
    private Set<String> getRoles(String username) {
        Set<String> roles = new HashSet<>();
        roles.add("user");
        roles.add("admin");
        return roles;
    }
}
