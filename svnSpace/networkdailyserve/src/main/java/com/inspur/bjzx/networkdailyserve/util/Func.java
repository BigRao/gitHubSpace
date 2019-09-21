package com.inspur.bjzx.networkdailyserve.util;

/**
 * Created by Administrator on 2016/5/18.
 */
public interface Func<F,T> {
    T apply(F currentElement, T origin);
}
