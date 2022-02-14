package bunkerchain.server;

import java.util.List;
import java.util.Optional;

import bunkerchain.entity.Role;
import bunkerchain.entity.User;

public interface RoleService {
	 List<Role> findAll();
	 Optional<Role> findRoleById(long id);
	 Role addRole(Role role);
	 void deleteRole(Long id);
	 Role updateRole(Role role);
}
