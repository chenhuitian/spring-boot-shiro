package bunkerchain.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

import bunkerchain.entity.Privilege;
import bunkerchain.server.PrivilegeService;

@RestController
@RequestMapping("/privileges")
public class PrivilegeController {
	@Autowired
	PrivilegeService privilegeService;
	
	@RequestMapping("/{id}")
	public ResponseEntity<Optional<Privilege> > findById(@PathVariable Long id){
		return new ResponseEntity<Optional<Privilege>>(privilegeService.findById(id),HttpStatus.OK);
	}
		
	@RequestMapping
	public ResponseEntity<List<Privilege>> findAll(){
		return new ResponseEntity<List<Privilege>>(privilegeService.findAll(),HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<Privilege> addPrivilege(@RequestBody Privilege privilege){
		return new ResponseEntity<Privilege>(privilegeService.addPrivilege(privilege),HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deletePrivilege(@PathVariable Long id){
		privilegeService.deletePrivilege(id);
		return new ResponseEntity<String>("delete success",HttpStatus.OK);
	} 
	@PutMapping("/{id}")
	public ResponseEntity<Privilege> updatePrivilege(@PathVariable Long id,@RequestBody Privilege privilege) throws Exception{
		Optional<Privilege> oldPrivilegeOptional = privilegeService.findById(id);
		if(!oldPrivilegeOptional.isPresent()) {
			throw new Exception("no data");			
		}
		Privilege oldPrivilege = oldPrivilegeOptional.get();
		oldPrivilege.setOperation(privilege.getOperation());
		return new ResponseEntity<Privilege>(privilegeService.updatePrivilege(oldPrivilege),HttpStatus.OK);
	}
}
