package com.suiyueshentou.base;

/**
 * 基类需要实现的方法
 * Created by zc on 2016/7/26.
 */
public interface BaseImpMethod {
    /**
     * 初始化view
     */
    void initView();

    /**
     * 初始化数据
     */
    void initData();

    /**
     * 显示加载窗
     */
    void showLoadingDialog();

    /**
     * 关闭加载窗
     */
    void dismissLoadingDialog();

    /**
     * 设置页面标题
     *
     * @param resId 布局id
     * @param title 标题名
     */
    void setHeadView(int resId, String title);

    /**
     * 设置back键
     *
     * @param backResId 布局id
     */
    void setBack(int backResId);
}
