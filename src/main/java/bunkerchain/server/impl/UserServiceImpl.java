package bunkerchain.server.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bunkerchain.entity.User;
import bunkerchain.repository.UserRepository;
import bunkerchain.server.UserService;

@Service("UserServiceImpl")
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;
	
	
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

	@Override
	public Optional<User> findById(Long id) {
		// TODO Auto-generated method stub
		return userRepository.findById(id);
	}


	@Override
	public void deleteById(long id) {
		// TODO Auto-generated method stub
		userRepository.deleteById(id);
	}

	@Override
	public User addUser(User user) {
		// TODO Auto-generated method stub
		String userName = user.getUserName();
		String pwdString = user.getPassWord();
		
		SimpleHash hashPwdString = new SimpleHash("MD5", pwdString, userName, 3);
		String hexpwdString = hashPwdString.toBase64();
		user.setPassWord(hexpwdString);
		return userRepository.save(user);
	}

	@Override
	public User updateUser(User user) {
		// TODO Auto-generated method stub
		return userRepository.save(user);
	}

	@Override
	public List<User> findAll() {
		// TODO Auto-generated method stub
		return userRepository.findAll();
	}

	@Override
	public User getUserById(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<User> findByUserNameAndPassWord(String userName, String passWord) {
		// TODO Auto-generated method stub
		return userRepository.findByUserNameAndPassWord(userName,passWord);
	}

	@Override
	public Optional<User> findByUserName(String userName) {
		// TODO Auto-generated method stub
		return userRepository.findByUserName(userName);
	}

}
