package cn.lyf.market.product;

import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@SpringBootTest
class ProductApplicationTests {
	@Autowired
	StringRedisTemplate stringRedisTemplate;

	@Autowired
	RedissonClient redisson;

	@Test
	void contextLoads() {
		ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
		ops.set("Hello","Redis!");
		System.out.println(ops.get("Hello"));
		System.out.println(redisson);
	}

}
