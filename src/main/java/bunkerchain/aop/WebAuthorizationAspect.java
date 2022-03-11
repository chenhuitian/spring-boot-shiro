package bunkerchain.aop;

import java.util.Arrays;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class WebAuthorizationAspect {

//    @Before("@target(org.springframework.stereotype.Controller) && @annotation(RequiresPermissions)")
//    public void assertAuthorized(JoinPoint jp, RequiresPermissions requiresPermissions) {
//        SecurityUtils.getSubject().checkRoles(Arrays.asList(requiresPermissions.value()));
//    }
}