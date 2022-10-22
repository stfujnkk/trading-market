package cn.lyf.market.product.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Catalog2Vo {
    // 一级分类id
    private String catalog1Id;
    private List<Catalog3Vo> catalog3List;
    private  String id;
    private String name;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Catalog3Vo{
        // 二级分类id
        private String catelog2Id;
        private  String id;
        private String name;
    }
}
