package cn.lyf.market.ware.service.impl;

import cn.lyf.common.constant.WareConstant;
import cn.lyf.market.ware.entity.PurchaseDetailEntity;
import cn.lyf.market.ware.exception.PurchaseException;
import cn.lyf.market.ware.exception.PurchaseMergeException;
import cn.lyf.market.ware.service.PurchaseDetailService;
import cn.lyf.market.ware.service.WareSkuService;
import cn.lyf.market.ware.vo.MergeVo;
import cn.lyf.market.ware.vo.PurchaseDoneVo;
import cn.lyf.market.ware.vo.PurchaseItemVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.lyf.common.utils.PageUtils;
import cn.lyf.common.utils.Query;

import cn.lyf.market.ware.dao.PurchaseDao;
import cn.lyf.market.ware.entity.PurchaseEntity;
import cn.lyf.market.ware.service.PurchaseService;
import org.springframework.transaction.annotation.Transactional;


@Service("purchaseService")
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {

    @Autowired
    PurchaseDetailService detailService;

    @Autowired
    WareSkuService wareSkuService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageUnreceivePurchase(Map<String, Object> params) {
        QueryWrapper<PurchaseEntity> queryWrapper = new QueryWrapper<>();
        // 刚新建或刚分配给某人
        queryWrapper.eq("status", 0).or().eq("status", 1);
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                queryWrapper
        );
        return new PageUtils(page);
    }

    @Override
    @Transactional
    public void mergePurchase(MergeVo mergeVo) {
        Long purchaseId = mergeVo.getPurchaseId();
        PurchaseEntity purchaseEntity = new PurchaseEntity();
        if (purchaseId == null) {
            // 新建采购单
            purchaseEntity.setStatus(WareConstant.PurchaseStatusEnum.CREATED.getCode());
            purchaseEntity.setCreateTime(new Date());
            purchaseEntity.setUpdateTime(new Date());
            this.save(purchaseEntity);
            purchaseId = purchaseEntity.getId();
        } else {
            purchaseEntity = this.getById(purchaseId);
            int status = purchaseEntity.getStatus();
            // 被合并的采购单状态只能是新建或已分配
            if (status != WareConstant.PurchaseStatusEnum.CREATED.getCode() && status != WareConstant.PurchaseStatusEnum.ASSIGNED.getCode()) {
                throw new PurchaseMergeException("被合并的采购单状态只能是新建或已分配");
            }
            purchaseEntity.setUpdateTime(new Date());
            this.updateById(purchaseEntity);
        }
        List<Long> items = mergeVo.getItems();
        final Long finalPurchaseId = purchaseId;
        List<PurchaseDetailEntity> collect = items.stream().map(i -> {
            PurchaseDetailEntity detailEntity = new PurchaseDetailEntity();
            detailEntity.setId(i);
            detailEntity.setPurchaseId(finalPurchaseId);
            // 采购需求已经分配给了采购单
            detailEntity.setStatus(WareConstant.PurchaseDetailStatusEnum.ASSIGNED.getCode());
            return detailEntity;
        }).collect(Collectors.toList());
        detailService.updateBatchById(collect);
    }

    @Override
    public void received(List<Long> ids) {
        //1) 确认采购单是已分配或新建状态
        List<PurchaseEntity> collect = ids.stream().map(id -> {
            PurchaseEntity entity = this.getById(id);
            if (entity == null) {
                throw new PurchaseException("采购单不存在");
            }
            return entity;
        }).filter(item -> item.getStatus() == WareConstant.PurchaseStatusEnum.CREATED.getCode() ||
                item.getStatus() == WareConstant.PurchaseStatusEnum.ASSIGNED.getCode()
        ).map(item -> {
            item.setStatus(WareConstant.PurchaseStatusEnum.RECEIVE.getCode());
            item.setUpdateTime(new Date());
            return item;
        }).collect(Collectors.toList());
        //2) 改变采购单状态
        this.updateBatchById(collect);
        //3) 改变采购项（采购需求）状态
        collect.forEach(item -> {
            List<PurchaseDetailEntity> entities = detailService.listDetailByPurchaseId(item.getId());
            List<PurchaseDetailEntity> detailEntities = entities.stream().map(entity -> {
                PurchaseDetailEntity entity1 = new PurchaseDetailEntity();
                entity1.setId(entity.getId());
                entity1.setStatus(WareConstant.PurchaseDetailStatusEnum.BUYING.getCode());
                return entity1;
            }).collect(Collectors.toList());
            detailService.updateBatchById(detailEntities);
        });
    }

    @Override
    @Transactional
    public void done(PurchaseDoneVo doneVo) {
        boolean flag = true;
        Long id = doneVo.getId();
        //1、改变采购项状态
        // TODO 采购项部分完成,扩展表应采购和实际采购和原因
        List<PurchaseDetailEntity> updates = new ArrayList<>();
        for (PurchaseItemVo item : doneVo.getItems()) {
            PurchaseDetailEntity detailEntity = new PurchaseDetailEntity();
            if (item.getStatus() == WareConstant.PurchaseDetailStatusEnum.FINISH.getCode()) {
                // 采购成功
                detailEntity.setStatus(item.getStatus());
                //3、将成功采购的入库
                PurchaseDetailEntity entity = detailService.getById(item.getItemId());
                wareSkuService.addStock(entity.getSkuId(), entity.getWareId(), entity.getSkuNum());
            } else {
                flag = false;
                detailEntity.setStatus(WareConstant.PurchaseDetailStatusEnum.HASERROR.getCode());
            }
            detailEntity.setId(item.getItemId());
            updates.add(detailEntity);
        }
        detailService.updateBatchById(updates);
        //2、改变采购单状态
        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setId(id);
        purchaseEntity.setStatus(flag ? WareConstant.PurchaseStatusEnum.FINISH.getCode() : WareConstant.PurchaseStatusEnum.HASERROR.getCode());
        purchaseEntity.setUpdateTime(new Date());
        this.updateById(purchaseEntity);
    }

}
