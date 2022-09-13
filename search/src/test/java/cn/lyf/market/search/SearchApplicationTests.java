package cn.lyf.market.search;

import cn.lyf.market.search.config.ElasticSearchConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.ToString;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.AvgAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Auto-generated: 2022-09-13 9:41:0
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
@Data
@ToString
class Account {
    private int account_number;
    private int balance;
    private String firstname;
    private String lastname;
    private int age;
    private String gender;
    private String address;
    private String employer;
    private String email;
    private String city;
    private String state;
}

@RunWith(SpringRunner.class)
@SpringBootTest
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
class SearchApplicationTests {

    @Autowired
    ObjectMapper jsonMapper;

    @Autowired
    private RestHighLevelClient client;

    public String toBeautifyJson(String jsonString) {
        try {
            Object obj = jsonMapper.readValue(jsonString, Object.class);
            return jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    public String beanToJsonString(Object jsonObj) {
        try {
            return jsonMapper.writeValueAsString(jsonObj);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    public List<Object> JsonStringToList(String jsonArr) {
        try {
            return jsonMapper.readValue(jsonArr, List.class);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    public Map<String, Object> JsonStringToMap(String jsonObj) {
        try {
            return jsonMapper.readValue(jsonObj, new TypeReference<Map<String, Object>>() {
            });
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    public void printJson(String jsonString) {
        System.out.println(toBeautifyJson(jsonString));
    }

    public <T> T JsonStringToBean(String jsonObj, Class<T> tClass) {
        try {
            return jsonMapper.readValue(jsonObj, tClass);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    @Data
    class User {
        private String userName;
        private String gender;
        private Integer age;
    }

    @Test
    void contextLoads() {
        // @SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
        System.out.println(client);
    }

    /**
     * 测试数据存到es
     * https://www.elastic.co/guide/en/elasticsearch/client/java-rest/7.4/java-rest-high-document-index.html
     */
    @Test
    void indexData() throws Exception {
        // json string
        User user = new User();
        user.setAge(22);
        user.setUserName("Liyifeng");
        user.setGender("M");
        String jsonString = beanToJsonString(user);
        new IndexRequest("user").id("1").source(jsonString, XContentType.JSON);
        // Map
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("user", "kimchy");
        jsonMap.put("postDate", new Date());
        jsonMap.put("message", "trying out Elasticsearch");
        new IndexRequest("posts").id("1").source(jsonMap);
        // builder
        XContentBuilder builder = XContentFactory.jsonBuilder();
        builder.startObject();
        {
            builder.field("user", "kimchy");
            builder.timeField("postDate", new Date());
            builder.field("message", "trying out Elasticsearch");
        }
        builder.endObject();
        new IndexRequest("posts").id("1").source(builder);
        // K V
        IndexRequest request = new IndexRequest("posts").id("1")
                .source(
                        "user", "kimchy",
                        "postDate", new Date(),
                        "message", "trying out Elasticsearch"
                );
        // 保存更新
        // Synchronous execution
        IndexResponse indexResponse = client.index(request, RequestOptions.DEFAULT);
        System.out.println(indexResponse);
    }

    /**
     * 测试数据存到es
     * https://www.elastic.co/guide/en/elasticsearch/client/java-rest/7.4/java-rest-high-search.html
     */
    @Test
    void searchData() throws Exception {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("bank");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchSourceBuilder.query(
                QueryBuilders.matchQuery("address", "mill")
        );

        TermsAggregationBuilder ageAgg = AggregationBuilders.terms("ageAgg").field("age").size(10);
        AvgAggregationBuilder balanceAvg = AggregationBuilders.avg("balanceAvg").field("balance");
        searchSourceBuilder.aggregation(ageAgg);
        // 嵌套使用 subAggregation()
        searchSourceBuilder.aggregation(balanceAvg);

        System.out.println("检索条件");
        printJson(searchSourceBuilder.toString());

        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest, ElasticSearchConfig.COMMON_OPTIONS);

        System.out.println("查询结果");
        printJson(searchResponse.toString());

        System.out.println("遍历查询结果");
        for (SearchHit hit : searchResponse.getHits().getHits()) {
            Account account = JsonStringToBean(hit.getSourceAsString(), Account.class);
            System.out.println(account);
        }
        System.out.println("遍历聚合结果");
        Aggregations aggregations = searchResponse.getAggregations();
        for (Aggregation aggregation : aggregations) {
            System.out.println("当前聚合" + aggregation.getName());
        }
        Terms ageAgg1 = aggregations.get("ageAgg");
        for (Terms.Bucket bucket : ageAgg1.getBuckets()) {
            String keyAsString = bucket.getKeyAsString();
            System.out.println("年龄：" + keyAsString + "==>" + bucket.getDocCount());
        }
    }
}
