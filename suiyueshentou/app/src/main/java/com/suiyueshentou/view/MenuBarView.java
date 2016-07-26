package com.suiyueshentou.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.suiyueshentou.R;


/**
 * 主界面底部按钮
 * Created by xz on 2015/10/28.
 */
public class MenuBarView extends FrameLayout implements View.OnClickListener {

    private ImageView[] imageViews;
    private int[] normalIds = {R.mipmap.ic_main_home_normal, R.mipmap.ic_main_store_normal, R.mipmap.ic_main_cart_normal, R.mipmap.ic_main_mine_normal};
    private int[] pressIds = {R.mipmap.ic_main_home_press, R.mipmap.ic_main_store_press, R.mipmap.ic_main_cart_press, R.mipmap.ic_main_mine_press};
    private OnMenuItemClickListener mClickListener;
    private Context mContext;

    /**
     * @param context
     */
    public MenuBarView(Context context) {
        super(context);
        initView(context);
    }

    /**
     * @param context
     * @param attributeSet
     */
    public MenuBarView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView(context);
    }

    /**
     * 初始化
     *
     * @param context
     */
    private void initView(Context context) {
        mContext = context;
        int size = normalIds.length;
        imageViews = new ImageView[size];

        View view = LayoutInflater.from(context).inflate(R.layout.menu_bar_view, this, true);

        imageViews[0] = (ImageView) view.findViewById(R.id.home_menu_iv);
        imageViews[1] = (ImageView) view.findViewById(R.id.store_menu_iv);
        imageViews[2] = (ImageView) view.findViewById(R.id.cart_menu_iv);
        imageViews[3] = (ImageView) view.findViewById(R.id.mine_menu_iv);

        for (int i = 0; i < imageViews.length; i++) {
            imageViews[i].setOnClickListener(this);
        }

    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
//        if(viewId == R.id.cart_menu_layout || viewId == R.id.store_menu_layout){//购物车、开店
//            if (MyLoginUtils.getInstance().isNeedLogin(mContext,v)) {
//                return;
//            }
//        }
        setMenuBackgroud(viewId);
        if(mClickListener!=null){
            mClickListener.onMenuItemClick(viewId);
        }
       

    }

    /**
     * 改变menu背景
     *
     * @param viewId
     */
    private void setMenuBackgroud(int viewId) {
        for (int i = 0; i < imageViews.length; i++) {
            Drawable drawableTop;
            if (imageViews[i].getId() == viewId) {
                drawableTop = getResources().getDrawable(pressIds[i]);
            } else {
                drawableTop = getResources().getDrawable(normalIds[i]);
            }
            imageViews[i].setImageDrawable(drawableTop);
        }
    }

    public interface OnMenuItemClickListener {
        void onMenuItemClick(int viewId);
    }

    public void setOnMenuItemClickListener(OnMenuItemClickListener onMenuItemClickListener) {
        mClickListener = onMenuItemClickListener;
    }

    /**
     * 设置当前item
     *
     * @param position 0~4
     */
    public void setCurrentMenuItem(int position) {
        if (position < 0 || position > normalIds.length) {
            position = 0;
        }
        imageViews[position].performClick();
    }

}
