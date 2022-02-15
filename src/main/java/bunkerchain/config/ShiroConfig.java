package bunkerchain.config;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;

import org.apache.shiro.authc.Authenticator;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import bunkerchain.shiro.GmailRealm;
import bunkerchain.shiro.JwtFilter;
import bunkerchain.shiro.MyModularRealmAuthenticator;
import bunkerchain.shiro.MyRealm;
import bunkerchain.shiro.TokenRealm;

@Configuration
public class ShiroConfig {
    /**
     * logger
     */
    private static final Logger logger = LoggerFactory.getLogger(ShiroConfig.class);

//	@Bean
//	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
//		AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
//		authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
//		return authorizationAttributeSourceAdvisor;
//	}
	
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
    	
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
//        Map<String, Filter> filters = new HashMap<String, Filter>();
////        filters.put("jwt",new JwtFilter());
//        shiroFilterFactoryBean.setFilters(filters);
        //设置安全管理器
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        //设置未认证(登录)时，访问需要认证的资源时跳转的页面
        shiroFilterFactoryBean.setLoginUrl("/login.html");

        //设置访问无权限的资源时跳转的页面
        shiroFilterFactoryBean.setUnauthorizedUrl("/unauthorizedPage");
        
        
        //指定路径和过滤器的对应关系
        Map<String, String> filterMap = new HashMap<>();
        //设置/user/login不需要登录就能访问
        filterMap.put("/user/login", "anon");
        filterMap.put("/employees/**", "authc");
        filterMap.put("/**", "anon");
        
//        //设置/user/list需要登录用户拥有角色user时才能访问
//        filterMap.put("/user/list", "roles[user]");
//        //其他路径则需要登录才能访问
//      filterMap.put("/employees/**", "authc");
//      filterMap.put("/privileges/**", "authc");
//      filterMap.put("/users/**", "jwt");
//      filterMap.put("/**", "anon");
      shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);
      return shiroFilterFactoryBean;
    }

    @Bean
    public DefaultWebSecurityManager securityManager() {
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();

        defaultWebSecurityManager.setRealms(Arrays.asList(myRealm(), tokenRealm()));
        
//        defaultWebSecurityManager.setRealms(Arrays.asList(myRealm(), gmailRealm(), tokenRealm()));
//        defaultWebSecurityManager.setRealms(Arrays.asList(myRealm()));
//        defaultWebSecurityManager.setRealms(Arrays.asList(gmailRealm()));
        return defaultWebSecurityManager;
    }
    
    @Bean
    public Realm myRealm() {
        MyRealm realm = new MyRealm();
//        realm.setPermissionResolver(null);
//        //使用HashedCredentialsMatcher带加密的匹配器来替换原先明文密码匹配器
//        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
//        //指定加密算法
//        hashedCredentialsMatcher.setHashAlgorithmName("MD5");
//        //指定加密次数
//        hashedCredentialsMatcher.setHashIterations(3);
//        realm.setCredentialsMatcher(hashedCredentialsMatcher);
        return realm;
    }
    

    @Bean
    public Realm tokenRealm() {
    	TokenRealm realm = new TokenRealm();
        return realm;
    }
    
//    @Bean
//    public Realm gmailRealm() {
//    	GmailRealm realm = new GmailRealm();
//        //使用HashedCredentialsMatcher带加密的匹配器来替换原先明文密码匹配器
//        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
//        //指定加密算法
//        hashedCredentialsMatcher.setHashAlgorithmName("MD5");
//        //指定加密次数
//        hashedCredentialsMatcher.setHashIterations(3);
//        realm.setCredentialsMatcher(hashedCredentialsMatcher);
//        return realm;
//    }
       
    
    @Bean
    public Authenticator authenticator() {
        //Extend the original method of the parent class to catch the original exception
        ModularRealmAuthenticator authenticator = new MyModularRealmAuthenticator();
        //Two realms are set, one for user login authentication and access permission acquisition, and the other for authentication of jwt token
//        authenticator.setRealms(Arrays.asList(myRealm(), gmailRealm()));
//        authenticator.setRealms(Arrays.asList(myRealm()));
//        authenticator.setRealms(Arrays.asList(gmailRealm()));
        /**
         FirstSuccessfulStrategy: As long as one of the realms is verified successfully, only the authentication information of the first one is returned, and others are ignored;
         AtLeastOneSuccessfulStrategy: As long as one of the realms is verified successfully, unlike firstsuccessfullstrategy, all the authentication information of the successful Realm authentication is returned; (default)
         AllSuccessfulStrategy: All Realm authentication succeeds, and all authentication information of the successful Realm authentication is returned. If one fails, it fails.
         */
        //Set multiple realm authentication policies. If one succeeds, other authentication policies will be skipped
        authenticator.setAuthenticationStrategy(new AtLeastOneSuccessfulStrategy());
        return authenticator;
    }
    
}
