package cn.lyf.market.ware.exception;

import cn.lyf.common.exception.BaseCodeEnum;
import cn.lyf.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Slf4j
@RestControllerAdvice(basePackages = "cn.lyf.market.ware.controller")
public class ExceptionAdvice {

    @ExceptionHandler(value = PurchaseMergeException.class)
    public R PurchaseMergeHandleException(PurchaseMergeException e) {
        log.error("采购单合并异常【{}】:{}", e.getClass(), e.getMessage());
        BaseCodeEnum bce = BaseCodeEnum.OP_EXCEPTION;
        return R.error(bce.code, bce.msg).put("data", e.getMessage());
    }


    @ExceptionHandler(value = PurchaseException.class)
    public R PurchaseHandleException(PurchaseMergeException e) {
        log.error("采购单异常【{}】:{}", e.getClass(), e.getMessage());
        BaseCodeEnum bce = BaseCodeEnum.OP_EXCEPTION;
        return R.error(bce.code, bce.msg).put("data", e.getMessage());
    }
}
