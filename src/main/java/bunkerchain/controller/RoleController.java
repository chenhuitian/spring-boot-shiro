package bunkerchain.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bunkerchain.entity.Privilege;
import bunkerchain.entity.Role;
import bunkerchain.entity.User;
import bunkerchain.server.PrivilegeService;
import bunkerchain.server.RoleService;
import bunkerchain.server.UserService;

@RestController
@RequestMapping("/roles")
public class RoleController {

	@Autowired
	RoleService roleService;
	
	@Autowired
	@Qualifier("UserServiceImpl")
	UserService userService;
	
	@Autowired
	PrivilegeService privilegeService;
	
	@RequestMapping("/{id}")
	public ResponseEntity<Optional<Role>> findByRoleId(@PathVariable Long id){
		return new ResponseEntity<Optional<Role>>(roleService.findRoleById(id),HttpStatus.OK);
	}
	
	@RequestMapping()
	public ResponseEntity<List<Role>> findAll(){
		return new ResponseEntity<List<Role>>(roleService.findAll(),HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<Role> addRole(@RequestBody Role role){		
		return new ResponseEntity<Role>(roleService.addRole(role),HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteRole(@PathVariable Long id){
		roleService.deleteRole(id);
		return new ResponseEntity<String>("delete success",HttpStatus.OK);
	} 
	
	@PutMapping("/{id}")
	public ResponseEntity<Role> updateRole(@PathVariable Long id,@RequestBody Role role) throws Exception{		
		
		Optional<Role> optionalRole = roleService.findRoleById(id);
		
		if(optionalRole.isEmpty()) {
			throw new Exception("role does not exsit");
		};
		
		Role oldrole = optionalRole.get();
		
		oldrole.setName(role.getName());
		
		
		if(role.getUsers()!=null) {
			oldrole.getUsers().clear();
			
			for (User user : role.getUsers()) {
				Optional<User> userOptional = userService.findById(user.getId());
				if(userOptional.isPresent()) {
					User oldUser = userOptional.get();
					oldrole.addUser(oldUser);
				}
			}
		}
		
		if(role.getPrivileges()!=null) {
			oldrole.getPrivileges().clear();
			
			for (Privilege privilege : role.getPrivileges()) {
				Optional<Privilege> privilegeOptional = privilegeService.findById(privilege.getId());
				if(privilegeOptional.isPresent()) {
					Privilege oldPrivilege = privilegeOptional.get();
					oldrole.addPrivilege(oldPrivilege);
				}
			}
		}				
		return new ResponseEntity<Role>(roleService.updateRole(oldrole),HttpStatus.OK);
	}
	
	//add role user
	@PostMapping("/{roleId}/users/{userids}")
	public ResponseEntity<Role> addRoleUsers(@PathVariable("roleId") Long id,@PathVariable("userids") List<Long> userids) throws Exception{	
		Optional<Role> optionalRole = roleService.findRoleById(id);
		
		if(optionalRole.isEmpty()) {
			throw new Exception("role does not exsit");
		};
		
		Role oldrole = optionalRole.get();
		
		for(Long userid : userids){
			Optional<User> userOptional = userService.findById(userid);
			if(userOptional.isPresent()) {
				if(!oldrole.getUsers().contains(userOptional.get()))
				oldrole.addUser(userOptional.get());
			};
		}
		
		return new ResponseEntity<Role>(roleService.updateRole(oldrole),HttpStatus.OK);
	}
	
	//add role privileg
	@PostMapping("/{roleId}/users/{privilegeids}")
	public ResponseEntity<Role> addRolePrivileges(@PathVariable("roleId") Long id,@PathVariable("privilegeids") List<Long> privilegeids) throws Exception{	
		Optional<Role> optionalRole = roleService.findRoleById(id);
		
		if(optionalRole.isEmpty()) {
			throw new Exception("role does not exsit");
		};
		
		Role oldrole = optionalRole.get();
		
		for(Long privilegeid : privilegeids){
			Optional<Privilege> privilegeOptional = privilegeService.findById(privilegeid);
			if(privilegeOptional.isPresent()) {
				if(!oldrole.getPrivileges().contains(privilegeOptional.get()))
				oldrole.addPrivilege(privilegeOptional.get());
			};
		}
		
		return new ResponseEntity<Role>(roleService.updateRole(oldrole),HttpStatus.OK);
	}
	
}
