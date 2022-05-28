package cn.lyf.market.coupon;

import cn.lyf.market.coupon.service.SkuLadderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CouponApplicationTests {
    @Autowired
    SkuLadderService sku;

    @Test
    void contextLoads() {
        System.out.println(sku);
    }

}
