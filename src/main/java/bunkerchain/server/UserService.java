package bunkerchain.server;

import java.util.Map;
import java.util.Set;

public interface UserService {
	 Map<String, Object> getUserInfo(String username);
	 Set<String> getRoles(String username);
	 Set<String> getPrivileges(String username);
}
