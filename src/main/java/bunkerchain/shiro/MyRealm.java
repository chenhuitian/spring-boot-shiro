package bunkerchain.shiro;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import bunkerchain.entity.Privilege;
import bunkerchain.entity.Role;
import bunkerchain.entity.User;
import bunkerchain.server.UserService;

public class MyRealm extends AuthorizingRealm {
	private static final Logger logger = LoggerFactory.getLogger(MyRealm.class);
	/**
	 * 授权
	 *
	 * @param principalCollection
	 * @return
	 */

	@Autowired
	@Qualifier("UserServiceImpl")
	UserService userService;

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

//    	 SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
//         HttpServletRequest request = (HttpServletRequest) ((WebSubject) SecurityUtils
//                 .getSubject()).getServletRequest();//This can be used to obtain other additional parameter information submitted during login
//         String username = (String) principals.getPrimaryPrincipal();//This is the demo written here. Later, in the actual project, traditional Chinese medicine obtains the user's role and authority through the login account. This is written dead
//         //Acceptance authority
//         //role
//         Set<String> roles = new HashSet<String>();
//         roles.add("role1");
//         authorizationInfo.setRoles(roles);
//         //Jurisdiction
//         Set<String> permissions = new HashSet<String>();
//         permissions.add("user:list");
//         //permissions.add("user:add");
//         authorizationInfo.setStringPermissions(permissions);
//         return authorizationInfo;

		Object userName = principalCollection.getPrimaryPrincipal();
		SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();

		Optional<User> userOptional = userService.findByUserName(userName.toString());

		if (!userOptional.isPresent()) {
			throw new UnknownAccountException("no person");
		}

		User user = userOptional.get();
		Set<String> roles = new HashSet<String>();
		Set<String> privileges = new HashSet<String>();
		for (Role role : user.getRoles()) {
			roles.add(role.getName());
			for(Privilege privilege : role.getPrivileges()) {
				privileges.add(privilege.getOperation());
			}
		}
		simpleAuthorizationInfo.setRoles(roles);
		simpleAuthorizationInfo.setStringPermissions(privileges);
		return simpleAuthorizationInfo;

//		if (user.getRoles() != null) {
//			
//			for (Role role : user.getRoles()) {
//
//				Set<String> roles = new HashSet<String>();
//				if (simpleAuthorizationInfo.getRoles() == null) {
//					roles = new HashSet<String>();
//				} else {
//					roles = simpleAuthorizationInfo.getRoles();
//				}
//
//				if (!roles.contains(role.getName())) {
//					roles.add(role.getName());
//				}
//
//				if (role.getPrivileges() != null) {
//					for (Privilege privilege : role.getPrivileges()) {
//
//						Set<String> privileges;
//						if (simpleAuthorizationInfo.getStringPermissions() == null) {
//							privileges = new HashSet<String>();
//						} else {
//							privileges = simpleAuthorizationInfo.getStringPermissions();
//						}
//						
//						if (!privileges.contains(privilege.getOperation())) {
//							privileges.add(privilege.getOperation());
//						}
//					}
//				}
//
//			}
//		}
//        Object username = principalCollection.getPrimaryPrincipal();
//        
//        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
//        
//        simpleAuthorizationInfo.setRoles(userService.getRoles(username.toString()));
//        simpleAuthorizationInfo.setStringPermissions(userService.getPrivileges(username.toString()));
//        return simpleAuthorizationInfo;
	}

	@Override
	public boolean supports(AuthenticationToken authenticationToken) {
		return authenticationToken instanceof UsernamePasswordToken;
	}

	@Override
	public void setCredentialsMatcher(CredentialsMatcher credentialsMatcher) {
		if (!(credentialsMatcher instanceof MyRealmCredentialsMatcher)) {
			if (logger.isDebugEnabled()) {
				logger.debug("Replacing {} with AllowAllCredentialsMatcher", credentialsMatcher);
			}
			credentialsMatcher = new MyRealmCredentialsMatcher();
		}
		super.setCredentialsMatcher(credentialsMatcher);
	}

	/**
	 * 认证
	 *
	 * @param authenticationToken
	 * @return
	 * @throws AuthenticationException
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
			throws AuthenticationException {
		UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;

		String userName = token.getUsername();

		Optional<User> userOptional = userService.findByUserName(userName);

		if (!userOptional.isPresent()) {
			throw new UnknownAccountException("no person");
		}

		ByteSource salt = ByteSource.Util.bytes(userName);
		SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(userName,
				userOptional.get().getPassWord(), salt, getName());
//    	SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(userName, userOptional.get().getPassWord(),null,null);
		return authenticationInfo;

//    	UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
//
//        String userName = token.getUsername();
//        Map<String, Object> userInfo = userService.getUserInfo(userName);
//        if (userInfo == null) {
//            throw new UnknownAccountException();
//        }
//
//        //盐值，此处使用用户名作为盐
//        ByteSource salt = ByteSource.Util.bytes(userName);
//
//        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(userName, userInfo.get("password"), salt, getName());
//        return authenticationInfo;
	}
}
