package bunkerchain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import bunkerchain.entity.User;
public interface UserRepository extends JpaRepository<User, Long> {

}
