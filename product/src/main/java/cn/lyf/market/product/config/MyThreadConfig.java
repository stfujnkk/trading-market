package cn.lyf.market.product.config;

//import org.springframework.boot.context.properties.EnableConfigurationProperties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
//@EnableConfigurationProperties(ThreadPoolConfigProperties.class)
public class MyThreadConfig {

	@Bean
	public ThreadPoolExecutor threadPoolExecutor(ThreadPoolConfigProperties config) {
		return new ThreadPoolExecutor(config.getCoreSize(), config.getMaxSize(), config.getKeepAliveTime(),
				TimeUnit.SECONDS, new LinkedBlockingDeque<>(1024 * 128),
				Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy()
		);
	}
}
