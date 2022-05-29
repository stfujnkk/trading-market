package cn.lyf.market.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.lyf.common.utils.PageUtils;
import cn.lyf.market.product.entity.CategoryEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品三级分类
 *
 * @author lyf
 * @email 3365310020@qq.com
 * @date 2022-05-28 12:18:19
 */
public interface CategoryService extends IService<CategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<CategoryEntity> listWithTree();
}

