package bunkerchain.controller;

import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.objenesis.instantiator.basic.NewInstanceInstantiator;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bunkerchain.entity.RedisEntity;
import bunkerchain.util.JedisUtil;

@RestController
@RequestMapping("/redis")
public class RedisController {
	@RequestMapping("/keys")
	public ResponseEntity<String> getAllKeys() {
		StringBuilder sBuffer = new StringBuilder();
		 Set<byte[]> keySet =  JedisUtil.keysB("test*");
		 for (byte[] bs : keySet) {
			 sBuffer.append(new String(bs)+"\n");
		}
		 return new ResponseEntity<String>(sBuffer.toString(),HttpStatus.OK);
	}
	
	@PostMapping()
	public ResponseEntity<String> setKey(@RequestBody RedisEntity redistEntity){
		
		if(redistEntity.getExpireTime() > 0) {
			JedisUtil.setObject(redistEntity.getKey(), redistEntity.getValue(),redistEntity.getExpireTime());			
		} else {
			JedisUtil.setObject(redistEntity.getKey(), redistEntity.getValue());			
		}
		return new ResponseEntity<String>(redistEntity.getKey(),HttpStatus.OK);
	}
	
	@RequestMapping("/keys/{key}")
	public ResponseEntity<String> getKey(@PathVariable("key") String key) {
		
		String valueString =  (String)JedisUtil.getObject(key);
		
		 return new ResponseEntity<String>(valueString,HttpStatus.OK);
	}
	
	@RequestMapping("/keys/{key}/ttl")
	public ResponseEntity<String> getKeyTtl(@PathVariable("key") String key) {
		
		Long ttlLong =  JedisUtil.ttl(key);
		
		 return new ResponseEntity<String>(ttlLong.toString(),HttpStatus.OK);
	}
	
}
