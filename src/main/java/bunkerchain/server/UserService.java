package bunkerchain.server;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import bunkerchain.entity.User;

public interface UserService {	
	Optional<User> findById(Long id);
	Optional<User> findByUserNameAndPassWord(String userName, String passWord);
	Optional<User> findByUserName(String userName);
	
	List<User> findAll();
	void deleteById(long id);
	User addUser(User user);
	User updateUser(User user);
	User getUserById(long id);
	Map<String, Object> getUserInfo(String username);
	Set<String> getRoles(String username);
	Set<String> getPrivileges(String username);
}
