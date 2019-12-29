package com.zkys.pad.launcher.Rxbus;

import android.util.Log;

import io.reactivex.functions.Consumer;

/**
 * Created by anyrsan on 2018/1/19.
 */

public abstract class RxConsumer<T> implements Consumer<T> {
    @Override
    public void accept(T o) throws Exception {
        try {
            handler(o);
        } catch (Exception ex) {
//            ex.printStackTrace();
            Log.e("msg", ex.getLocalizedMessage());
        }
    }

    public abstract void handler(T o);

}
