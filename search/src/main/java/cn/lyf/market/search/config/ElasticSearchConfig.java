package cn.lyf.market.search.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Configuration;

/**
 * https://www.elastic.co/guide/en/elasticsearch/client/java-rest/7.4/java-rest-high-getting-started-initialization.html
 */
@Configuration
public class ElasticSearchConfig {

    public RestHighLevelClient esRestClient(){
        RestClientBuilder builder = RestClient.builder(
                new HttpHost("localhost", 9200, "http")
                // , new HttpHost("localhost", 9201, "http")
        );
        return new RestHighLevelClient(builder);
    }

}
