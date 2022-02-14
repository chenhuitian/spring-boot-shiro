package bunkerchain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import bunkerchain.entity.Privilege;

public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {
	
}