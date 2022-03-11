package bunkerchain.server.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.stereotype.Service;

import bunkerchain.entity.User;
import bunkerchain.server.UserService;

//@Service("TokenUserServieImpl")
public class TokenUserServieImpl implements UserService {

	@Override
	public Map<String, Object> getUserInfo(String username) {
		Map<String, Object> userInfo = null;
        if ("admin".equals(username)) {
            userInfo = new HashMap<>();
            userInfo.put("username", "admin");

            userInfo.put("password", "3");
            //加密算法，原密码，盐值，加密次数
//            userInfo.put("password", new SimpleHash("MD5", "2", username, 3));
        }
        
        if ("guest".equals(username)) {
            userInfo = new HashMap<>();
            userInfo.put("username", "guest");
            userInfo.put("password", "3");
            //加密算法，原密码，盐值，加密次数
//            userInfo.put("password", new SimpleHash("MD5", "2", username, 3));
        }
        
        return userInfo;
	}

	@Override
	public Set<String> getRoles(String username) {
		
		return null;
		
//		Set<String> roles = new HashSet<>();
//		if ("admin".equals(username)) {
//			roles.add("user");
//	        roles.add("admin");
//			
//        } else if("guest".equals(username)) {
//        	roles.add("user");
//        }
//		
//        
//        return roles;
	}

	@Override
	public Set<String> getPrivileges(String username) {
		return null;
//		// TODO Auto-generated method stub
//		Set<String> privileges = new HashSet<>();
//		
//		if ("admin".equals(username)) {
//			privileges.add("user:list");
//			privileges.add("user:create");
//			privileges.add("employee:list");
//			privileges.add("employee:create");
//			
//        } else if("guest".equals(username)) {
//        	privileges.add("user:list");
//        }
//		
//        return privileges;
	}


	@Override
	public List<User> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteById(long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public User addUser(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User updateUser(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<User> findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User getUserById(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<User> findByUserNameAndPassWord(String userName, String passWord) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<User> findByUserName(String userName) {
		// TODO Auto-generated method stub
		return null;
	}

}
