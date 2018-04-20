package com.zerone.store.shopingtimetest.JavaClass;

import com.zerone.store.shopingtimetest.Interface.ViewCallBack;

public abstract class BasePresenter {

    protected ViewCallBack mViewCallBack;

    public void add(ViewCallBack viewCallBack) {
        this.mViewCallBack = viewCallBack;
    }

    public void remove() {
        this.mViewCallBack = null;
    }

    protected abstract void getData();
}
