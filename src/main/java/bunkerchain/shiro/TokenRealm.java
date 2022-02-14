package bunkerchain.shiro;

import java.util.Map;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import bunkerchain.common.Constant;
import bunkerchain.server.UserService;
import bunkerchain.util.JwtUtil;

public class TokenRealm extends AuthorizingRealm{
    private static final Logger logger = LoggerFactory.getLogger(TokenRealm.class);

	@Autowired
	@Qualifier("TokenUserServieImpl")
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
	        return authenticationToken instanceof JwtToken;
	    }
	    
	    
	    
	    @Override
	    public void setCredentialsMatcher( CredentialsMatcher credentialsMatcher ) {
	    	  if ( !( credentialsMatcher instanceof TokenCredentialsMatcher ) ) {
	    	    if (logger.isDebugEnabled()) {
	    	      logger.debug("Replacing {} with AllowAllCredentialsMatcher", credentialsMatcher);
	    	    }
	    	    credentialsMatcher = new TokenCredentialsMatcher();
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
//	    @Override
//	    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
//	    	UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
//
//	        String username = token.getUsername();
//	        Map<String, Object> userInfo = userService.getUserInfo(username);
//	        if (userInfo == null) {
//	            throw new UnknownAccountException();
//	        }
//
//	        //盐值，此处使用用户名作为盐
//	        ByteSource salt = ByteSource.Util.bytes(username);
//
//	        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(username, userInfo.get("password"), salt, getName());
//	        return authenticationInfo;
//	    }

	    @Override
	    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
	    	
	    	JwtToken token = (JwtToken) auth;

	        String username = JwtUtil.getUsername(token.getToken());
	        
	        Map<String, Object> userInfo = userService.getUserInfo(username);
	        if (userInfo == null) {
	            throw new UnknownAccountException();
	        }
	        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(username,token ,"TokenRealm");
	        return authenticationInfo;
	    }
}
