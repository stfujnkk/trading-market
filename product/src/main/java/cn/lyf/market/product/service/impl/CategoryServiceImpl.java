package cn.lyf.market.product.service.impl;

import cn.lyf.common.utils.PageUtils;
import cn.lyf.common.utils.Query;
import cn.lyf.market.product.dao.CategoryDao;
import cn.lyf.market.product.entity.CategoryEntity;
import cn.lyf.market.product.service.CategoryBrandRelationService;
import cn.lyf.market.product.service.CategoryService;
import cn.lyf.market.product.vo.Catalog2Vo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {
	@Autowired
	CategoryBrandRelationService categoryBrandRelationService;

	@Autowired
	StringRedisTemplate stringRedisTemplate;

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		IPage<CategoryEntity> page = this.page(
				new Query<CategoryEntity>().getPage(params),
				new QueryWrapper<CategoryEntity>()
		);

		return new PageUtils(page);
	}

	@Override
	public List<CategoryEntity> listWithTree() {
		//TMP baseMapper.selectList(null);
		List<CategoryEntity> categoryList = baseMapper.selectList(null);
		Map<Long, List<CategoryEntity>> menus =
				categoryList.stream().collect(Collectors.groupingBy(CategoryEntity::getParentCid));
		Comparator<CategoryEntity> cmp = (a, b) -> {
			Integer sa = a.getSort(), sb = b.getSort();
			return (sa == null ? 0 : sa) - (sb == null ? 0 : sb);
		};
		categoryList.stream().forEach(x -> {
			Long id = x.getCatId();
			List<CategoryEntity> children = menus.get(id);
			if (children == null) return;
			children.sort(cmp);
			x.setChildren(children);
		});
		return categoryList.stream().filter(x -> x.getParentCid() == 0).sorted(cmp).collect(Collectors.toList());
	}

	@Override
	public void removeMenuByIds(List<Long> ids) {
		// TODO 检查菜单是否被引用
		baseMapper.deleteBatchIds(ids);
	}

	@Override
	public List<Long> findCatelogPath(Long catelogId) {
		List<Long> ans = new ArrayList<>();
		findParentCatelogId(ans, catelogId);
		return ans;
	}

	@Override
	//级联更新关系表
	public void updateCascade(CategoryEntity category) {
		baseMapper.updateById(category);
		categoryBrandRelationService.updateCategory(category.getCatId(), category.getName());
	}

	@Override
	public List<CategoryEntity> getLevel1Categorys() {
		List<CategoryEntity> categoryEntities = baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", 0));
		return categoryEntities;
	}

	@Override
	public Map<String, List<Catalog2Vo>> getCatalogJson() {
		// Netty有堆外内存泄露问题
		final ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
		String catalogCache = ops.get("CatalogJson");
		ObjectMapper mapper = new ObjectMapper();
		try {
			if (StringUtils.isEmpty(catalogCache)) {
				Map<String, List<Catalog2Vo>> catalogJson = getCatalogJsonFromDB();
				ops.set("CatalogJson", mapper.writeValueAsString(catalogJson));
				return catalogJson;
			}
			return mapper.readValue(catalogCache, Map.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}

	Map<String, List<Catalog2Vo>> getCatalogJsonFromDB() {
		// TODO 减少查询数据库次数
		List<CategoryEntity> level1Categorys = getLevel1Categorys();
		return level1Categorys.stream().collect(Collectors.toMap(
				item -> item.getCatId().toString(),
				catalog1 -> {
					List<CategoryEntity> catalog2List = baseMapper.selectList((new QueryWrapper<CategoryEntity>().eq("parent_cid", catalog1.getCatId())));
					List<Catalog2Vo> catalog2VoList = null;
					if (catalog2List != null) {
						// 有二级分类
						catalog2VoList = catalog2List.stream().map(catalog2 -> {
							Catalog2Vo catalog2Vo = new Catalog2Vo(catalog1.getCatId().toString(), null, catalog2.getCatId().toString(), catalog2.getName());
							List<CategoryEntity> catalog3List = baseMapper.selectList((new QueryWrapper<CategoryEntity>().eq("parent_cid", catalog2.getCatId())));
							if (catalog3List != null) {
								// 有三级分类
								List<Catalog2Vo.Catalog3Vo> catalog3VoList = catalog3List.stream().map(catalog3 -> {
									Catalog2Vo.Catalog3Vo catalog3Vo = new Catalog2Vo.Catalog3Vo(
											catalog2.getCatId().toString(),
											catalog3.getCatId().toString(),
											catalog3.getName()
									);
									return catalog3Vo;
								}).collect(Collectors.toList());
								catalog2Vo.setCatalog3List(catalog3VoList);
							}
							return catalog2Vo;
						}).collect(Collectors.toList());
					}
					return catalog2VoList;
				}));
	}

	private void findParentCatelogId(List<Long> ans, Long catelogId) {
		if (catelogId == null || catelogId == 0) return;
		CategoryEntity now = this.getById(catelogId);
		findParentCatelogId(ans, now.getParentCid());
		ans.add(catelogId);
	}

}
