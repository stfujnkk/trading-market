package cn.lyf.market.product.controller;

import cn.lyf.common.utils.PageUtils;
import cn.lyf.common.utils.R;
import cn.lyf.common.valid.group.AddGroup;
import cn.lyf.common.valid.group.UpdateGroup;
import cn.lyf.market.product.entity.BrandEntity;
import cn.lyf.market.product.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * 品牌
 *
 * @author lyf
 * @email 3365310020@qq.com
 * @date 2022-05-28 13:53:41
 */
@RestController
@RequestMapping("product/brand")
public class BrandController {
	@Autowired
	private BrandService brandService;

	/**
	 * 列表
	 */
	@RequestMapping("/list")
	//@RequiresPermissions("product:brand:list")
	public R list(@RequestParam Map<String, Object> params) {
		PageUtils page = brandService.queryPage(params);

		return R.ok().put("page", page);
	}


	/**
	 * 信息
	 */
	@RequestMapping("/info/{brandId}")
	//@RequiresPermissions("product:brand:info")
	public R info(@PathVariable("brandId") Long brandId) {
		BrandEntity brand = brandService.getById(brandId);

		return R.ok().put("brand", brand);
	}

	/**
	 * 保存
	 */
	@RequestMapping("/save")
	//@RequiresPermissions("product:brand:save")
	public R save(@Validated({AddGroup.class}) @RequestBody BrandEntity brand/*, BindingResult bindingResult*/) {
		brandService.save(brand);
		return R.ok();
	}

	/**
	 * 修改
	 */
	@RequestMapping("/update")
	//@RequiresPermissions("product:brand:update")
	public R update(@Validated({UpdateGroup.class}) @RequestBody BrandEntity brand) {
		brandService.updateDetail(brand);
		return R.ok();
	}

	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	//@RequiresPermissions("product:brand:delete")
	public R delete(@RequestBody Long[] brandIds) {
		brandService.removeByIds(Arrays.asList(brandIds));

		return R.ok();
	}

	@GetMapping("/infos")
	public R info(@RequestParam("brandIds") List<Long> brandIds) {
		List<BrandEntity> brandEntities = brandService.listByIds(brandIds);
		return R.ok().put("brand", brandEntities);
	}

}
