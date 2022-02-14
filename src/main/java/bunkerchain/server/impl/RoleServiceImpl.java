package bunkerchain.server.impl;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bunkerchain.entity.Role;
import bunkerchain.entity.User;
import bunkerchain.repository.RoleRepository;
import bunkerchain.server.RoleService;

@Service
public class RoleServiceImpl implements RoleService{

	@Autowired
	RoleRepository roleRepository;

	@Override
	public List<Role> findAll() {
		// TODO Auto-generated method stub
		return roleRepository.findAll();
	}

	@Override
	public Optional<Role> findRoleById(long id) {
		// TODO Auto-generated method stub
		return roleRepository.findById(id);
	}

	@Override
	public Role addRole(Role role) {
		// TODO Auto-generated method stub
		Role newRole = roleRepository.save(role);
		return newRole;
	}

	@Override
	public void deleteRole(Long id) {
		// TODO Auto-generated method stub
		roleRepository.deleteById(id);
	}

	@Override
	public Role updateRole(Role role) {
		// TODO Auto-generated method stub
		return roleRepository.save(role);
	}
	
	

}
