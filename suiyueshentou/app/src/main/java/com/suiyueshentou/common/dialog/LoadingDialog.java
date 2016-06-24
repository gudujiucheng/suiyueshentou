package com.suiyueshentou.common.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.suiyueshentou.R;

/**
 * Created by xz on 2016/3/21.
 */
public class LoadingDialog extends Dialog {

    private Context mContext;
    private ImageView mImageView;
//    private TextView mTextView;
//    private String mText = null; //加载文字
    private int mResId = -1; //加载图片资源

    /**
     * LoadingDialog
     * @param context 上下文
     */
    public LoadingDialog(Context context) {
        super(context, R.style.loading_dialog);
        mContext = context;
        initView();
    }


    /**
     * 初始化视图
     */
    private void initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_loading, null);
        RelativeLayout layout = (RelativeLayout) view.findViewById(R.id.layout_dialog);
        mImageView = (ImageView) view.findViewById(R.id.iv_progress);
        if (mResId != -1) mImageView.setImageResource(mResId);
        initImageAnim(mImageView);
        this.setCanceledOnTouchOutside(false);
        this.setContentView(layout);
    }

    /**
     * 设置加载旋转图片
     * @param resId
     */
    public void setImageView(int resId) {
        mImageView.setImageResource(resId);
    }

    /**
     * 旋转动画
     * @param iv
     */
    private void initImageAnim(ImageView iv) {
        AnimationSet animationSet = new AnimationSet(true);
        RotateAnimation rotate = new RotateAnimation(0, 359, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setRepeatCount(Animation.INFINITE);
        rotate.setRepeatMode(Animation.RESTART);
        animationSet.addAnimation(rotate);
        animationSet.setDuration(2000);
        LinearInterpolator lir = new LinearInterpolator();
        animationSet.setInterpolator(lir);
        iv.startAnimation(animationSet);
    }

    @Override
    public void show() {
        initImageAnim(mImageView);
        super.show();
    }
}

