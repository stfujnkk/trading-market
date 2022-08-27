package cn.lyf.market.ware.exception;

/**
 * 合并采购单错误
 */
public class PurchaseMergeException extends RuntimeException {
    public PurchaseMergeException(String message){
        super(message);
    }
}
