package cn.lyf.market.product.vo;

import cn.lyf.market.product.entity.AttrEntity;
import lombok.Data;

import java.util.List;

/*
[{
        "group":{
        "attrGroupId":1,
        "attrGroupName":"njn",
        "attrs":[{
        "attrId":0,
        "attrName":"bbb",
        "valueSelect":"1;2",
        "showDesc":"xxxx"
        }]
        }
}]
* */
@Data
public class GroupWithAttrVo {
    /**
     * 分组id
     */
    private Long attrGroupId;
    /**
     * 组名
     */
    private String attrGroupName;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 描述
     */
    private String descript;
    /**
     * 组图标
     */
    private String icon;
    /**
     * 所属分类id
     */
    private Long catelogId;


    private List<AttrEntity> attrs;
}
