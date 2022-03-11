package bunkerchain.config;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import bunkerchain.shiro.MyCredentialsMatcher;
import bunkerchain.shiro.MyShiroRealm;


@Configuration
public class ShiroConfig {
	 private static final String ENCRYPTION_KEY = "3AvVhmFLUs0KTA3Kprsdag==";

	    @Bean
	    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {
	        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
	        shiroFilterFactoryBean.setSecurityManager(securityManager);
	        // 拦截器
	        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
	        filterChainDefinitionMap.put("/**", "anon");
	        // 未授权的界面
	        shiroFilterFactoryBean.setUnauthorizedUrl("/unauthorized");
	        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

	        return shiroFilterFactoryBean;
	    }

	    /**
	     * 自定义身份认证 realm;
	     * <p>
	     * 必须写这个类，并加上 @Bean 注解，目的是注入 MyShiroRealm， 否则会影响 MyShiroRealm类 中其他类的依赖注入
	     */
	    @Bean
	    public MyShiroRealm myShiroRealm() {
	        MyShiroRealm myShiroRealm = new MyShiroRealm();
	        // 设置密码比较器
	        myShiroRealm.setCredentialsMatcher(CredentialsMatcher());
	        // 启用身份验证缓存，即缓存AuthenticationInfo信息，默认false
	        myShiroRealm.setAuthenticationCachingEnabled(true);
	        // 启用授权缓存，即缓存AuthorizationInfo信息，默认false,一旦配置了缓存管理器，授权缓存默认开启
	        myShiroRealm.setAuthorizationCachingEnabled(true);
	        
	        return myShiroRealm;
	    }

	    @Bean
	    public SimpleCredentialsMatcher CredentialsMatcher() {
	        MyCredentialsMatcher hct = new MyCredentialsMatcher();//自定义凭证比较器
	        // 加密算法的名称
	        hct.setHashAlgorithmName("MD5");
	        // 配置加密的次数
	        hct.setHashIterations(1024);
	        // 是否存储为16进制
	        hct.setStoredCredentialsHexEncoded(true);
	        return hct;
	    }

	    /**
	     * 注入 securityManager
	     */
	    @Bean
	    public SecurityManager securityManager() {
	        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
	        securityManager.setRealm(myShiroRealm());//配置自定义权限认证器
	        securityManager.setRememberMeManager(rememberMeManager());//配置记住我管理器
	        securityManager.setCacheManager(cacheManager());//配置缓存管理器
	        return securityManager;
	    }
	    
	    /**
	     * 缓存管理器
	     * @return
	     */
	    @Bean
	    public CacheManager cacheManager() {
	        MemoryConstrainedCacheManager mccm = new MemoryConstrainedCacheManager();
	        return mccm;
	    }
	    
	    /**
	     * Cookie 对象 用户免登陆操作，但是需要配置filter /** 权限为user生效
	     * 
	     * @return
	     */
	    public SimpleCookie rememMeCookie() {
	        // 初始化设置cookie的名称
	        SimpleCookie simpleCookie = new SimpleCookie("shiro-remember");
	        simpleCookie.setMaxAge(2592000);// 设置cookie的生效时间
	        simpleCookie.setHttpOnly(true);
	        return simpleCookie;
	    }

	    /**
	     * cookie 管理对象，记住我功能
	     * 
	     * @return
	     */
	    public CookieRememberMeManager rememberMeManager() {
	        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
	        cookieRememberMeManager.setCookie(rememMeCookie());
	        // remeberMe cookie 加密的密钥 各个项目不一样 默认AES算法 密钥长度（128 256 512）
	        cookieRememberMeManager.setCipherKey(Base64.decode(ENCRYPTION_KEY));
	        return cookieRememberMeManager;
	    }


	    /**
	     * 开启Shiro的注解(如@RequiresRoles,@RequiresPermissions),需借助SpringAOP扫描使用Shiro注解的类,并在必要时进行安全逻辑验证
	     * 配置以下两个bean(DefaultAdvisorAutoProxyCreator和AuthorizationAttributeSourceAdvisor)即可实现此功能
	     * 
	     * @return
	     */
	    @Bean
	    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
	        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
	        advisorAutoProxyCreator.setProxyTargetClass(true);
	        return advisorAutoProxyCreator;
	    }

	    /**
	     * 开启aop注解支持
	     * 
	     * @param securityManager
	     * @return
	     */
	    @Bean
	    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
	        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
	        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
	        return authorizationAttributeSourceAdvisor;
	    }
	    
//    /**
//     * logger
//     */
////    private static final Logger logger = LoggerFactory.getLogger(ShiroConfig.class);
//
////	@Bean
////	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
////		AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
////		authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
////		return authorizationAttributeSourceAdvisor;
////	}
//	
////    @Bean("shiroFilter")
////    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
////        
////        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
//////        Map<String, Filter> filters = new HashMap<String, Filter>();
////////        filters.put("jwt",new JwtFilter());	
//////        shiroFilterFactoryBean.setFilters(filters);
////        //设置安全管理器
////        shiroFilterFactoryBean.setSecurityManager(securityManager);
////
////        //设置未认证(登录)时，访问需要认证的资源时跳转的页面
////        shiroFilterFactoryBean.setLoginUrl("/login.html");
////
////        //设置访问无权限的资源时跳转的页面
////        shiroFilterFactoryBean.setUnauthorizedUrl("/unauthorizedPage");
////        
////        
////        //指定路径和过滤器的对应关系
////        Map<String, String> filterMap = new HashMap<>();
////        //设置/user/login不需要登录就能访问
////        filterMap.put("/user/login", "anon");
////        filterMap.put("/employees/**", "authc");
////        filterMap.put("/**", "anon");
////        
//////        //设置/user/list需要登录用户拥有角色user时才能访问
//////        filterMap.put("/user/list", "roles[user]");
//////        //其他路径则需要登录才能访问
//////      filterMap.put("/employees/**", "authc");
//////      filterMap.put("/privileges/**", "authc");
//////      filterMap.put("/users/**", "jwt");
//////      filterMap.put("/**", "anon");
////      shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);
////      return shiroFilterFactoryBean;
////      
////      
////    }
//
//    @Bean("shiroFilter")
//    public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager securityManager) {
//    	
//        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
//        factoryBean.setSecurityManager(securityManager);
//        // 自定义url规则使用LinkedHashMap有序Map
//        LinkedHashMap<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>(16);
//      
//        // 登录接口放开
//        filterChainDefinitionMap.put("/user/login", "anon");
//        factoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
//        return factoryBean;
//    }
//    
//    @Bean
//    public DefaultWebSecurityManager securityManager() {
//    	 DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
//         // 使用自定义Realm
//         defaultWebSecurityManager.setRealm(myRealm());
//         // 关闭Shiro自带的session
////         DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
////         DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
////         defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
////         subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
////         defaultWebSecurityManager.setSubjectDAO(subjectDAO);
//         // 设置自定义Cache缓存
//         defaultWebSecurityManager.setCacheManager(new CustomCacheManager());
//         return defaultWebSecurityManager;
//        
//       
////        defaultWebSecurityManager.setRealms(Arrays.asList(myRealm()));
//        
////        defaultWebSecurityManager.setRealms(Arrays.asList(myRealm(), gmailRealm(), tokenRealm()));
////        defaultWebSecurityManager.setRealms(Arrays.asList(myRealm()));
////        defaultWebSecurityManager.setRealms(Arrays.asList(gmailRealm()));
////        return defaultWebSecurityManager;
//    }
//    
//    @Bean
//    public Realm myRealm() {
//        MyRealm realm = new MyRealm();
////        realm.setPermissionResolver(null);
////        //使用HashedCredentialsMatcher带加密的匹配器来替换原先明文密码匹配器
////        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
////        //指定加密算法
////        hashedCredentialsMatcher.setHashAlgorithmName("MD5");
////        //指定加密次数
////        hashedCredentialsMatcher.setHashIterations(3);
////        realm.setCredentialsMatcher(hashedCredentialsMatcher);
//        return realm;
//    }
//    
//
////    @Bean
////    public Realm tokenRealm() {
////    	TokenRealm realm = new TokenRealm();
////        return realm;
////    }
//    
////    @Bean
////    public Realm gmailRealm() {
////    	GmailRealm realm = new GmailRealm();
////        //使用HashedCredentialsMatcher带加密的匹配器来替换原先明文密码匹配器
////        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
////        //指定加密算法
////        hashedCredentialsMatcher.setHashAlgorithmName("MD5");
////        //指定加密次数
////        hashedCredentialsMatcher.setHashIterations(3);
////        realm.setCredentialsMatcher(hashedCredentialsMatcher);
////        return realm;
////    }
//       
//    
////    @Bean
////    public Authenticator authenticator() {
////        //Extend the original method of the parent class to catch the original exception
////        ModularRealmAuthenticator authenticator = new MyModularRealmAuthenticator();
////        //Two realms are set, one for user login authentication and access permission acquisition, and the other for authentication of jwt token
//////        authenticator.setRealms(Arrays.asList(myRealm(), gmailRealm()));
//////        authenticator.setRealms(Arrays.asList(myRealm()));
//////        authenticator.setRealms(Arrays.asList(gmailRealm()));
////        /**
////         FirstSuccessfulStrategy: As long as one of the realms is verified successfully, only the authentication information of the first one is returned, and others are ignored;
////         AtLeastOneSuccessfulStrategy: As long as one of the realms is verified successfully, unlike firstsuccessfullstrategy, all the authentication information of the successful Realm authentication is returned; (default)
////         AllSuccessfulStrategy: All Realm authentication succeeds, and all authentication information of the successful Realm authentication is returned. If one fails, it fails.
////         */
////        //Set multiple realm authentication policies. If one succeeds, other authentication policies will be skipped
////        authenticator.setAuthenticationStrategy(new AtLeastOneSuccessfulStrategy());
////        return authenticator;
//    }
   
}
