package bunkerchain.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    /**
     * login
     * @param username
     * @param password
     * @return
     */
	 @RequestMapping("/login/{username}/{rememberMe}/{password}")
	    public String login(@PathVariable String username,
	    		@PathVariable Boolean rememberMe,
	    		@PathVariable String password) {
        String result;

        //get curent user
        Subject currentUser = SecurityUtils.getSubject();

        //if authenticated then pass, else login
        if (!currentUser.isAuthenticated()) {
            //package username and password
            UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, password);

            try {
                //login 
                currentUser.login(usernamePasswordToken);
                result = "login sucess";
            } catch (UnknownAccountException uae) {
                result = "no such user";
            } catch (IncorrectCredentialsException ice) {
                result = "password is wrong";
            } catch (LockedAccountException lae) {
                result = "the user is locked";
            } catch (AuthenticationException ae) {
                result = "authenticat failed";
            }
        } else {
            result = "you have logined in";
        }

        return result;
    }

    @RequestMapping("/list")
    @RequiresPermissions(logical = Logical.AND, value = {"user:list"})
    public String userList() {
        return "list user！";
    }
    
    @RequestMapping("/create")
    @RequiresPermissions(logical = Logical.AND, value = {"user:create"})
    public String createUser() {
        return "create user！";
    }
    
    @RequestMapping("/logout")
    public String getMessageGuest() {
        Subject subject = SecurityUtils.getSubject();
        // 登出
        subject.logout();
        return "logout success";
    }
}
