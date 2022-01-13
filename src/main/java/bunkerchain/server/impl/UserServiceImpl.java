package bunkerchain.server.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.stereotype.Service;

import bunkerchain.server.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Override
	public Map<String, Object> getUserInfo(String username) {
		Map<String, Object> userInfo = null;
        if ("admin".equals(username)) {
            userInfo = new HashMap<>();
            userInfo.put("username", "admin");

            //加密算法，原密码，盐值，加密次数
            userInfo.put("password", new SimpleHash("MD5", "1", username, 3));
        }
        
        if ("guest".equals(username)) {
            userInfo = new HashMap<>();
            userInfo.put("username", "guest");

            //加密算法，原密码，盐值，加密次数
            userInfo.put("password", new SimpleHash("MD5", "1", username, 3));
        }
        
        return userInfo;
	}

	@Override
	public Set<String> getRoles(String username) {
		// TODO Auto-generated method stub
		Set<String> roles = new HashSet<>();
		if ("admin".equals(username)) {
			roles.add("user");
	        roles.add("admin");
			
        } else if("guest".equals(username)) {
        	roles.add("user");
        }
		
        
        return roles;
	}

	@Override
	public Set<String> getPrivileges(String username) {
		// TODO Auto-generated method stub
		Set<String> privileges = new HashSet<>();
		
		if ("admin".equals(username)) {
			privileges.add("user:list");
			privileges.add("user:create");
			
        } else if("guest".equals(username)) {
        	privileges.add("user:list");
        }
		
        return privileges;
	}

}
