package bunkerchain.shiro;

import java.util.Collection;
import java.util.Iterator;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LogoutAware;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.pam.AuthenticationStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.CollectionUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyModularRealmAuthenticator extends ModularRealmAuthenticator  {
	
//	protected AuthenticationInfo doMultiRealmAuthentication(Collection<Realm> realms, UsernamePasswordToken token) {
//        AuthenticationStrategy strategy = this.getAuthenticationStrategy();
//        AuthenticationInfo aggregate = strategy.beforeAllAttempts(realms, token);
//        if (log.isTraceEnabled()) {
//            log.trace("Iterating through {} realms for PAM authentication", realms.size());
//        }
//        Iterator var5 = realms.iterator();
//        AuthenticationException authenticationException = null;
//        while(var5.hasNext()) {
//            Realm realm = (Realm)var5.next();
//            aggregate = strategy.beforeAttempt(realm, token, aggregate);
//            if (realm.supports(token)) {
//                log.trace("Attempting to authenticate token [{}] using realm [{}]", token, realm);
//                AuthenticationInfo info = null;
//                Throwable t = null;
//                try {
//                    info = realm.getAuthenticationInfo(token);
//                	
//                } catch (Throwable var11) {
//                    t = var11;
//                    authenticationException = (AuthenticationException)var11;
//                    if (log.isDebugEnabled()) {
//                        String msg = "Realm [" + realm + "] threw an exception during a multi-realm authentication attempt:";
//                        log.debug(msg, var11);
//                    }
//                }
//                aggregate = strategy.afterAttempt(realm, token, info, aggregate, t);
//                //Add this logic. Only if the authenticationException is not null, it means that there is an exception detected by the Realm. Then immediately interrupt the subsequent Realm validation and throw it out directly
//                if (authenticationException != null){
//                    throw authenticationException;
//                }
//            } else {
//                log.debug("Realm [{}] does not support token {}.  Skipping realm.", realm, token);
//            }
//        }
//
//        aggregate = strategy.afterAllAttempts(token, aggregate);
//        return aggregate;
//    }
	
	
	protected AuthenticationInfo doAuthenticate(AuthenticationToken authenticationToken) throws AuthenticationException {
	    assertRealmsConfigured();
	    Collection<Realm> realms = getRealms();
	    if (realms.size() == 1) {
	        return doSingleRealmAuthentication(realms.iterator().next(), authenticationToken);
	    } else {
	        return doMultiRealmAuthentication(realms, authenticationToken);
	    }
	}
	
//	@Override
//    protected AuthenticationInfo doAuthenticate(AuthenticationToken authenticationToken) throws AuthenticationException {
//        // Determine whether the Realm is empty
//        assertRealmsConfigured();
//        Collection<Realm> realms = getRealms();
//        UsernamePasswordToken mytoken = (UsernamePasswordToken)authenticationToken;
//        doMultiRealmAuthentication(realms,mytoken);
//      for (Realm realm : realms) {
//      if (mytoken.getTokenType()) {
//          return doMultiRealmAuthentication(realm, authenticationToken);
//      }
  
        
//        UsernamePasswordToken jwtToken = (UsernamePasswordToken) authenticationToken;
//        String loginType = jwtToken.getLoginType().name();
//        for (Realm realm : realms) {
//            if (realm.getName().equals(loginType)) {
//                return doSingleRealmAuthentication(realm, authenticationToken);
//            }
//        }
//        return null;
//    }
	
	
	public void onLogout(PrincipalCollection principals) {
        super.onLogout(principals);
        Collection<Realm> realms = getRealms();
        if (!CollectionUtils.isEmpty(realms)) {
            for (Realm realm : realms) {
                if (realm instanceof LogoutAware) {
                    ((LogoutAware) realm).onLogout(principals);
                }
            }
        }
    }
}

