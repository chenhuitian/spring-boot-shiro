package bunkerchain.entity;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "role")
public class Role {
	@Id
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String name;
	
//	@ManyToMany(fetch = FetchType.LAZY)
	@ManyToMany
	@JoinTable(name = "user_role",joinColumns = @JoinColumn(name = "role_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
	@JsonIgnoreProperties(value={ "roles" })
	private List<User> users;
	
	public void addUser(User user) {
		users.add(user);
	}
	public void deleteUser(User user) {
		users.remove(user);
	}
//	@ManyToMany(fetch = FetchType.LAZY)
	@ManyToMany
	@JoinTable(name = "role_privilege",joinColumns = @JoinColumn(name = "role_id"), inverseJoinColumns = @JoinColumn(name = "privilege_id"))
	@JsonIgnoreProperties(value={ "roles"})
	private List<Privilege> privileges;
	
	public void addPrivilege(Privilege privilege) {
		privileges.add(privilege);
	}
	public void deletePrivilege(Privilege privilege) {
		privileges.remove(privilege);
	}
}
