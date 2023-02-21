package cn.lyf.market.product.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "jmall.thread")
public class ThreadPoolConfigProperties {
	private Integer coreSize;
	private Integer maxSize;
	private Integer keepAliveTime;
}
