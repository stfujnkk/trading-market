package cn.lyf.common.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

public class JsonUtils {

    public static ObjectMapper mapper = new ObjectMapper();

    /**
     * 格式化（美化）json字符串
     * @param jsonString json字符串
     */
    public static String toBeautifyJson(String jsonString) {
        try {
            Object obj = mapper.readValue(jsonString, Object.class);
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    /**
     * java对象转json字符串
     */
    public static String beanToJsonString(Object jsonObj) {
        try {
            return mapper.writeValueAsString(jsonObj);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    /**
     * json字符串转对象
     */
    public static <T> T JsonStringToBean(String jsonObj, Class<T> tClass) {
        try {
            return mapper.readValue(jsonObj, tClass);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    /**
     * json字符串 转 List
     */
    public static List<Object> JsonStringToList(String jsonArr) {
        try {
            return mapper.readValue(jsonArr, List.class);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    /**
     * json字符串 转 Map
     */
    public static Map<String, Object> JsonStringToMap(String jsonObj) {
        try {
            return mapper.readValue(jsonObj, new TypeReference<Map<String, Object>>() {
            });
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    /**
     * 打印json字符串
     */
    public static void printJson(String jsonString) {
        System.out.println(toBeautifyJson(jsonString));
    }

}
