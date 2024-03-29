package bunkerchain.shiro;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.naming.AuthenticationException;

import org.apache.shiro.SecurityUtils;
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
import org.springframework.context.annotation.Lazy;

import bunkerchain.entity.Privilege;
import bunkerchain.entity.Role;
import bunkerchain.entity.User;
import bunkerchain.server.UserService;

public class MyShiroRealm extends AuthorizingRealm {
    @Autowired
    @Lazy
    UserService userService;

    /**
     * 权限设置
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        System.out.println("进入自定义权限设置方法！");
        String username = (String) principals.getPrimaryPrincipal();
        // 从数据库或换村中获取用户角色信息
        Optional<User> optionaluser = userService.findByUserName(username);
        
        if(!optionaluser.isPresent()) {
        	return null;
        }
        User user = optionaluser.get();
        // 获取用户角色
        List<Role> roles = user.getRoles();
        Set<String> roleList = new HashSet<>();
        Set<String> privilegeList = new HashSet<>();
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        for (Role role : roles) {
        	roleList.add(role.getName());	
        	for(Privilege privilege : role.getPrivileges()) {
        		privilegeList.add(privilege.getOperation());
        	}
		}
        simpleAuthorizationInfo.setRoles(roleList);  
        simpleAuthorizationInfo.setStringPermissions(privilegeList);

        return simpleAuthorizationInfo;
    }

    /**
     * 身份验证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) {
        System.out.println("进入自定义登录验证方法！");
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
        String username = usernamePasswordToken.getUsername();// 用户输入用户名
        Optional<User> optionaluser = userService.findByUserName(username);// 根据用户输入用户名查询该用户
        if (!optionaluser.isPresent()) {
            throw new UnknownAccountException();// 用户不存在
        }
        
        User user = optionaluser.get();
        String password = user.getPassWord();
        
        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(username, password, getName());
        // 加盐，对比的时候会使用该参数对用户输入的密码按照密码比较器指定规则加盐，加密，再去对比数据库密文
        simpleAuthenticationInfo.setCredentialsSalt(ByteSource.Util.bytes(username));
        return simpleAuthenticationInfo;
    }

    
    /**
     * 重写方法,清除当前用户的的 授权缓存
     * @param principals
     */
    public void clearCachedAuthorizationInfo() {
        super.clearCachedAuthorizationInfo(SecurityUtils.getSubject().getPrincipals());
    }

    /**
     * 重写方法，清除当前用户的 认证缓存
     * @param principals
     */
    public void clearCachedAuthenticationInfo() {
        super.clearCachedAuthenticationInfo(SecurityUtils.getSubject().getPrincipals());
    }
    /**
     * 清除某个用户认证和授权缓存
     */
    @Override
    public void clearCache(PrincipalCollection principals) {
        super.clearCache(principals);
    }

    /**
     * 自定义方法：清除所有 授权缓存
     */
    public void clearAllCachedAuthorizationInfo() {
        getAuthorizationCache().clear();
    }

    /**
     * 自定义方法：清除所有 认证缓存
     */
    public void clearAllCachedAuthenticationInfo() {
        getAuthenticationCache().clear();
    }

    /**
     * 自定义方法：清除所有的  认证缓存  和 授权缓存
     */
    public void clearAllCache() {
        clearAllCachedAuthenticationInfo();
        clearAllCachedAuthorizationInfo();
    }
    
    

}

