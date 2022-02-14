package bunkerchain.entity;

import lombok.Data;

@Data
public class RedisEntity {
	private String key;
	private String value;
	private int expireTime;
}
