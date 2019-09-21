package com.inspur.bjzx.networkdailyserve.util;

import java.util.Iterator;

/**
 * Created by Administrator on 2016/5/18.
 */
public class Reduce {
    private Reduce() {

    }

    public static <F,T> T reduce(final Iterable<F> iterable, final Func<F, T> func, T origin) {

        for (Iterator iterator = iterable.iterator(); iterator.hasNext(); ) {
            origin = func.apply((F)(iterator.next()), origin);
        }

        return origin;
    }
}