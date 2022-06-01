package cn.lyf.market.product.entity;

import cn.lyf.common.valid.EnumIntValid;
import cn.lyf.common.valid.group.AddGroup;
import cn.lyf.common.valid.group.UpdateGroup;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;
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
    @NotNull(groups = {UpdateGroup.class})
    @Null(groups = {AddGroup.class})
    private Long brandId;
    /**
     * 品牌名
     */
    @NotNull(groups = {AddGroup.class})
    @Pattern(regexp = "^[\u4e00-\u9fa5a-zA-Z0-9.-]+$", groups = {AddGroup.class, UpdateGroup.class})
    private String name;
    /**
     * 品牌logo地址
     */
    @URL(message = "非法URL", groups = {AddGroup.class, UpdateGroup.class})
    @NotNull(groups = {AddGroup.class})
    private String logo;
    /**
     * 介绍
     */
    private String descript;
    /**
     * 显示状态[0-不显示；1-显示]
     */
    @EnumIntValid(value = {0, 1}, groups = {AddGroup.class, UpdateGroup.class})
    @NotNull(groups = {AddGroup.class})
    private Integer showStatus;
    /**
     * 检索首字母
     */
    @Pattern(regexp = "^[a-zA-Z]$", message = "检索字母必须为一个英文字母", groups = {AddGroup.class, UpdateGroup.class})
    private String firstLetter;
    /**
     * 排序
     */
    @Min(value = 0, message = "不是正整数", groups = {AddGroup.class, UpdateGroup.class})
    private Integer sort;

}
