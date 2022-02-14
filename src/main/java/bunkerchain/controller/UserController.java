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

import bunkerchain.entity.User;
import bunkerchain.server.UserService;

@RestController
@RequestMapping("/users")
public class UserController {
	@Autowired
	@Qualifier("UserServiceImpl")
	UserService userService;
	
	
	@RequestMapping
	public ResponseEntity<List<User>>  findAll() {
		return new ResponseEntity<List<User>>(userService.findAll(), HttpStatus.OK);
	}
	
	@RequestMapping("/{id}")
	public ResponseEntity<Optional<User>>  findById( @PathVariable Long id) {
		return new ResponseEntity<Optional<User>>(userService.findById(id), HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<User> addRole(@RequestBody User user){		
		return new ResponseEntity<User>(userService.addUser(user),HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}") 
	public ResponseEntity<String> deleteById(@PathVariable Long id){
		userService.deleteById(id);
		return new ResponseEntity<String>("delete success",HttpStatus.OK);
	} 
	
	@PutMapping("/{id}")
	public ResponseEntity<User> updateUser(@PathVariable Long id,@RequestBody User user) throws Exception{	
		
		return null;
	}
	
}
