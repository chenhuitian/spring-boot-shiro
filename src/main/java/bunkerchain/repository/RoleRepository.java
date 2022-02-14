package bunkerchain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import bunkerchain.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
	@Modifying(clearAutomatically = true)
	Role save(Role role);
}
