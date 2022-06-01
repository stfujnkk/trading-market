package cn.lyf.market.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * 品牌
 *
 * @author lyf
 * @email 3365310020@qq.com
 * @date 2022-05-28 12:18:19
 */
// JSR303
@Data
@TableName("pms_brand")
public class BrandEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 品牌id
     */
    @TableId
    private Long brandId;
    /**
     * 品牌名
     */
    @NotBlank(message = "品牌名不能为空")
    private String name;
    /**
     * 品牌logo地址
     */
    @URL(message = "非法URL")
    @NotNull
    private String logo;
    /**
     * 介绍
     */
    private String descript;
    /**
     * 显示状态[0-不显示；1-显示]
     */
    private Integer showStatus;
    /**
     * 检索首字母
     */
    @Pattern(regexp = "^[a-zA-Z]$", message = "检索字母必须为一个英文字母")
    private String firstLetter;
    /**
     * 排序
     */
    @Min(value = 0,message = "不是正整数")
    private Integer sort;

}
