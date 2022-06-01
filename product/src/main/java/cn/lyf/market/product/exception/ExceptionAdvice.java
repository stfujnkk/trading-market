package cn.lyf.market.product.exception;

import cn.lyf.common.exception.BaseCodeEnum;
import cn.lyf.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice(basePackages = "cn.lyf.market.product.controller")
public class ExceptionAdvice {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R handleValidException(MethodArgumentNotValidException e) {
        log.error("数据校验【{}】:{}", e.getClass(), e.getMessage());
        BindingResult bindingResult = e.getBindingResult();
        Map<String, String> errorMap = new HashMap<>();
        bindingResult.getFieldErrors().forEach(x -> errorMap.put(x.getField(), x.getDefaultMessage()));
        BaseCodeEnum bce = BaseCodeEnum.VALID_EXCEPTION;
        return R.error(bce.code, bce.msg).put("data", errorMap);
    }

    @ExceptionHandler(value = Throwable.class)
    public R handleException(Throwable e) {
        log.error("controller异常【{}】:{}", e.getClass(), e.getMessage());
        BaseCodeEnum bce = BaseCodeEnum.UNKNOWN_EXCEPTION;
        return R.error(bce.code, bce.msg).put("data", e.getMessage());
    }
}
