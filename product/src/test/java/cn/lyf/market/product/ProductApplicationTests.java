package cn.lyf.market.product;

import cn.lyf.market.product.entity.SkuInfoEntity;
import cn.lyf.market.product.service.SkuInfoService;
import cn.lyf.market.product.service.SkuSaleAttrValueService;
import cn.lyf.market.product.vo.ItemSaleAttrVo;
import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.List;

@SpringBootTest
class ProductApplicationTests {
	@Autowired
	StringRedisTemplate stringRedisTemplate;

	@Autowired
	SkuSaleAttrValueService skuSaleAttrValueService;

	@Autowired
	RedissonClient redisson;

	@Test
	void contextLoads() {
		ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
		ops.set("Hello", "Redis!");
		System.out.println(ops.get("Hello"));
		System.out.println(redisson);
	}

	@Test
	public void test() {
		Long spuId = 13L;
		List<ItemSaleAttrVo> saleAttrsBySpuId = skuSaleAttrValueService.getSaleAttrsBySpuId(spuId);
		System.out.println(saleAttrsBySpuId);
	}

}
