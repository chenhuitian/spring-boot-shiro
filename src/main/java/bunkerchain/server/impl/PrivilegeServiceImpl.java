package bunkerchain.server.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bunkerchain.entity.Privilege;
import bunkerchain.repository.PrivilegeRepository;
import bunkerchain.server.PrivilegeService;

@Service
public class PrivilegeServiceImpl implements PrivilegeService {

	@Autowired
	PrivilegeRepository privilegeRepository;
	
	
	@Override
	public Optional<Privilege> findById(long roleId) {
		// TODO Auto-generated method stub
		return  privilegeRepository.findById(roleId);
	}

	@Override
	public Privilege addPrivilege(Privilege privilege) {
		// TODO Auto-generated method stub
		return privilegeRepository.save(privilege);
	}

	@Override
	public void deletePrivilege(Long privilegeId) {
		// TODO Auto-generated method stub
		privilegeRepository.deleteById(privilegeId);
	}

	@Override
	public Privilege updatePrivilege(Privilege privilege) {
		// TODO Auto-generated method stub
		return privilegeRepository.save(privilege);
	}

	
	@Override
	public List<Privilege> findAll() {
		// TODO Auto-generated method stub
		return privilegeRepository.findAll();
	}



}
