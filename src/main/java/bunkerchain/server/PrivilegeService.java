package bunkerchain.server;

import java.util.List;
import java.util.Optional;

import bunkerchain.entity.Privilege;

public interface PrivilegeService {
	List<Privilege> findAll();
	Optional<Privilege> findById(long Id);	
	Privilege addPrivilege(Privilege privilege);
	Privilege updatePrivilege(Privilege privilege);
	void deletePrivilege(Long privilegeId);
	
}
