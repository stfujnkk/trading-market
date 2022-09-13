/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package cn.lyf.common.utils;

import cn.lyf.common.exception.BaseCodeEnum;
import org.apache.http.HttpStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 返回数据
 *
 * @author Mark sunlightcs@gmail.com
 */
public class R<T> extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;

    public static final String DATA_KEY = "data";
    public static final String PAGE_KEY = "page";

    public void setData(T t) {
        super.put(DATA_KEY, t);
    }

    public T getData() {
        return (T) super.get(DATA_KEY);
    }

    public void setPage(List<T> ts) {
        super.put(PAGE_KEY, ts);
    }

    public List<T> getPage() {
        return (List<T>) super.get(PAGE_KEY);
    }

    @Override
    public Object get(Object key) {
        String k = (String) key;
        switch (k) {
            case PAGE_KEY:
                return getPage();
            case DATA_KEY:
                return getData();
            default:
                return super.get(key);
        }
    }

    public R() {
        put("code", 0);
        put("msg", "success");
    }

    public static R error() {
        return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, "未知异常，请联系管理员");
    }

    public static R error(String msg) {
        return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, msg);
    }

    public static R error(BaseCodeEnum baseCodeEnum) {
        return R.error(baseCodeEnum.code, baseCodeEnum.msg);
    }

    public static R error(int code, String msg) {
        R r = new R();
        r.put("code", code);
        r.put("msg", msg);
        return r;
    }

    public static R ok(String msg) {
        R r = new R();
        r.put("msg", msg);
        return r;
    }

    public static R ok(Map<String, Object> map) {
        R r = new R();
        r.putAll(map);
        return r;
    }

    public static R ok() {
        return new R();
    }

    public R put(String key, Object value) {
        switch (key) {
            case PAGE_KEY:
                setPage((List<T>) value);
            case DATA_KEY:
                setData((T) value);
            default:
                super.put(key, value);
        }
        return this;
    }

    public Integer getCode() {
        return (Integer) get("code");
    }

    public String getMsg() {
        return (String) get("msg");
    }
}
