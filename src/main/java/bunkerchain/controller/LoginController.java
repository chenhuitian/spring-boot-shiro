package bunkerchain.controller;

import java.util.Map;
import java.util.Optional;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bunkerchain.entity.User;
import bunkerchain.server.UserService;
import bunkerchain.util.JwtUtil;

@RestController
@RequestMapping("/ac")
public class LoginController {

	@Autowired
//	@Qualifier("UserServiceImpl")
	UserService userService;

	/**
	 * login
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	@RequestMapping("/login/{username}/{rememberMe}/{password}")
	public ResponseEntity<String> login(@PathVariable String username, @PathVariable Boolean rememberMe,
			@PathVariable String password, @RequestHeader(value = "logintype", required = false) String logintype) {
		String result = "";

		// get curent user
		Subject currentUser = SecurityUtils.getSubject();

		// if authenticated then pass, else login
		if (!currentUser.isAuthenticated()) {
			// package username and password
			if (logintype != null && logintype != "" && logintype.equals("jwt")) {
				Optional<User> userOptional = userService.findByUserNameAndPassWord(username, password);
				
				if (!userOptional.isPresent()) {
					result = "username isn't exist, or password is wrong!";
					return new ResponseEntity<String>(result, HttpStatus.UNAUTHORIZED);
				}else {
					// 获取当前最新时间戳
					String currentTimeMillis = String.valueOf(System.currentTimeMillis());
					String compareTokenString = JwtUtil.sign(username, currentTimeMillis);
					result = "login sucess";
					HttpHeaders responseHeaders = new HttpHeaders();
					responseHeaders.set("token", compareTokenString);
					return new ResponseEntity<String>(result, responseHeaders, HttpStatus.OK);
				}
				
			} else {
				UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, password);
				usernamePasswordToken.setRememberMe(rememberMe);
				try {
					// login
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
			}
		} else {

			result = "you have logined in";
		}
		return new ResponseEntity<String>(result, HttpStatus.OK);
	}
	
	@RequestMapping("/logout")
	public String getMessageGuest() {
		Subject subject = SecurityUtils.getSubject();
		// 登出
		subject.logout();
		return "logout success";
	}
}
