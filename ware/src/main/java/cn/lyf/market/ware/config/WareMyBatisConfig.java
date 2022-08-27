package cn.lyf.market.ware.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@MapperScan("cn.lyf.market.ware.dao")
@Configuration
public class WareMyBatisConfig {
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        PaginationInnerInterceptor pag = new PaginationInnerInterceptor(DbType.MYSQL);
        // 请求大于最大页时回到首页
        pag.setOverflow(true);
        // 请求最大数据条数,-1不受限制
        pag.setMaxLimit(1000L);
        interceptor.addInnerInterceptor(pag);
        return interceptor;
    }
}
