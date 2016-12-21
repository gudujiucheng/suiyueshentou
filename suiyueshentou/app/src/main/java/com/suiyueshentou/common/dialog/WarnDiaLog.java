package com.suiyueshentou.common.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.suiyueshentou.R;


/**
 * 删除弹窗
 */
public class WarnDiaLog extends Dialog {

    private Context context;
    private String title;
    private String buttonLeftText;
    private String buttonRightText;
    private ClickListenerInterface clickListenerInterface;

    /**
     * @param context
     * @param title                  弹窗标题
     * @param buttonLeftText         左边按键文字
     * @param buttonRightText        右边按键提示
     * @param clickListenerInterface 按键回调
     */
    public WarnDiaLog(Context context, String title,
                      String buttonLeftText, String buttonRightText, ClickListenerInterface clickListenerInterface) {
        super(context, R.style.warn_dialog_style);
        this.clickListenerInterface = clickListenerInterface;
        this.context = context;
        this.title = title;
        this.buttonLeftText = buttonLeftText;
        this.buttonRightText = buttonRightText;
    }

    public interface ClickListenerInterface {
        public void doLeft();

        public void doRight();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        init();
    }

    public void init() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_warn, null);
        setContentView(view);

        TextView tvLeft = (TextView) view.findViewById(R.id.tv_warn_dialog_left);
        TextView tvRight = (TextView) view.findViewById(R.id.tv_warn_dialog_right);
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_warn_dialog_title);

        if ("".equals(title)) {
            tvTitle.setVisibility(View.GONE);
        } else {
            tvTitle.setText(title);
        }
        tvLeft.setText(buttonLeftText);
        tvRight.setText(buttonRightText);

        tvLeft.setOnClickListener(new clickListener());
        tvRight.setOnClickListener(new clickListener());

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics();

        lp.width = (int) (d.widthPixels * 0.8);
        dialogWindow.setAttributes(lp);
    }


    private class clickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            int id = v.getId();
            switch (id) {
                case R.id.tv_warn_dialog_left:
                    clickListenerInterface.doLeft();
                    break;
                case R.id.tv_warn_dialog_right:
                    clickListenerInterface.doRight();
                    break;

                default:
                    break;
            }
        }
    }

    ;
}
