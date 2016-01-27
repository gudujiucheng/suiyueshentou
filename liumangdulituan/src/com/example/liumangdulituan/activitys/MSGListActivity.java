package com.example.liumangdulituan.activitys;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.liumangdulituan.R;
import com.example.liumangdulituan.R.color;
import com.example.liumangdulituan.R.id;
import com.example.liumangdulituan.R.layout;
import com.example.liumangdulituan.bean.XGNotification;
import com.example.liumangdulituan.common.NotificationService;

/**
 * 
 * @Description 消息列表
 * @author zhangcan
 * @date 2016-1-19 上午11:53:48
 */
public class MSGListActivity extends Activity implements OnItemClickListener, OnScrollListener {
    private ListView pushListV;

    private LinearLayout bloadLayout;

    private TextView bloadInfo;

    private LinearLayout tloadLayout;

    private TextView tloadInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg);
        initUI();
        updateNotifications(id);

    }

    private void initUI() {
        notificationService = NotificationService.getInstance(this);
        pushListV = (ListView) this.findViewById(R.id.push_list);
        // 点击事件
        pushListV.setOnItemClickListener(this);
        // 滑动事件
        pushListV.setOnScrollListener(this);

        // 3. 创建一个角标线性布局来显示正在加载
        bloadLayout = new LinearLayout(this);
        bloadLayout.setMinimumHeight(100);
        bloadLayout.setGravity(Gravity.CENTER);

        // 定义一个文本显示"正在加载文本"
        bloadInfo = new TextView(this);
        bloadInfo.setTextSize(16);
        bloadInfo.setTextColor(Color.parseColor("#858585"));
        bloadInfo.setText("加载更多...");
        bloadInfo.setGravity(Gravity.CENTER);

        // 綁定組件
        bloadLayout.addView(bloadInfo, new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        bloadLayout.getBottom();

        // 绑定提示到列表底部
        pushListV.addFooterView(bloadLayout);

        // 4. 创建一个角标线性布局来显示正在加载
        tloadLayout = new LinearLayout(this);
        tloadLayout.setGravity(Gravity.CENTER);
        tloadLayout.setBackgroundResource(R.color.fuxk_base_color_white);
        // 定义一个文本显示"正在加载文本"
        tloadInfo = new TextView(this);
        tloadInfo.setTextSize(14);
        tloadInfo.setTextColor(Color.parseColor("#858585"));
        // tloadInfo.setBackgroundResource(R.color.gray);
        tloadInfo.setText("更多新消息...");
        tloadInfo.setGravity(Gravity.CENTER);
        tloadInfo.setHeight(0);

        // 綁定組件
        tloadLayout.addView(tloadInfo, new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        // 绑定提示到列表底部
        pushListV.addHeaderView(tloadLayout);
        tloadLayout.setVisibility(View.GONE);
    }

    private boolean isLast = false;// 是否最后一条

    private int firstItem;// 第一条显示出来的数据的游标

    private int lastItem;// 最后显示出来数据的游标

    private int pageSize = 1;// 默认共1页

    private int currentPage = 1;// 默认第一页

    private boolean isUpdate = false;

    private String id = "";// 查询条件

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // 是否到最底部并且数据还没读完
        if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {// 停下不动了
            if (isLast && currentPage < pageSize) {
                currentPage++;
                // 设置显示位置
                pushListV.setSelection(lastItem);
                // 增加数据
                appendNotifications(id);
            } else if (firstItem == 0) {
                if (isUpdate && tloadInfo.getHeight() >= 50) {
                    isUpdate = false;
                    updateNotifications(id);
                    TranslateAnimation alp = new TranslateAnimation(0, 0, 80, 0);
                    alp.setDuration(1000);
                    alp.setRepeatCount(1);
                    tloadLayout.setAnimation(alp);
                    alp.setAnimationListener(new AnimationListener() {

                        @Override
                        public void onAnimationStart(Animation animation) {
                            tloadInfo.setText("正在更新...");
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            tloadInfo.setText("更多新消息...");
                            tloadLayout.setVisibility(View.GONE);
                            tloadInfo.setHeight(0);
                            tloadLayout.setMinimumHeight(0);
                        }
                    });
                }
            }
        } else if (scrollState == OnScrollListener.SCROLL_STATE_TOUCH_SCROLL && firstItem == 0) {
            if (tloadInfo.getHeight() < 50) {
                isUpdate = true;
                tloadInfo.setHeight(50);
                tloadLayout.setMinimumHeight(100);
                tloadLayout.setVisibility(View.VISIBLE);
            }
        }
    }

    private NotificationService notificationService;// 获取通知数据服务

    private int allRecorders = 0;// 全部记录数

    private static final int lineSize = 10;// 每次显示数

    private pushAdapter adapter;// 列表适配器

    private void appendNotifications(String id) {
        // 计算总数据条数
        allRecorders = notificationService.getCount();
        // 计算总页数
        pageSize = (allRecorders + lineSize - 1) / lineSize;
        int oldsize = adapter.getData().size();
        // 更新适配器
        adapter.getData().addAll(NotificationService.getInstance(this).getScrollData(currentPage, lineSize, id));
        // 如果到了最末尾则去掉"正在加载"
        if (allRecorders == adapter.getCount()) {
            bloadInfo.setHeight(0);
            bloadLayout.setMinimumHeight(0);
            bloadLayout.setVisibility(View.GONE);
        } else {
            bloadInfo.setHeight(50);
            bloadLayout.setMinimumHeight(100);
            bloadLayout.setVisibility(View.VISIBLE);
        }
        Toast.makeText(this, "共" + allRecorders + "条信息,加载了" + (adapter.getData().size() - oldsize) + "条信息",
                Toast.LENGTH_SHORT).show();
        // 通知改变
        adapter.notifyDataSetChanged();
    }

    private void updateNotifications(String id) {
        // 计算总数据条数
        int oldAllRecorders = allRecorders;
        allRecorders = notificationService.getCount();
        getNotificationswithouthint(id);
        Toast.makeText(this, "共" + allRecorders + "条信息,更新了" + (allRecorders - oldAllRecorders) + "条新信息",
                Toast.LENGTH_SHORT).show();
    }

    private void getNotificationswithouthint(String id) {
        if (allRecorders != 0) {
            this.findViewById(R.id.nodata).setVisibility(View.GONE);
        }

        // 计算总页数
        pageSize = (allRecorders + lineSize - 1) / lineSize;

        // 创建适配器
        adapter = new pushAdapter(this);
        adapter.setData(NotificationService.getInstance(this).getScrollData(currentPage = 1, lineSize, id));
        if (allRecorders <= lineSize) {
            bloadLayout.setVisibility(View.GONE);
            bloadInfo.setHeight(0);
            bloadLayout.setMinimumHeight(0);
        } else {
            if (pushListV.getFooterViewsCount() < 1) {
                bloadLayout.setVisibility(View.VISIBLE);
                bloadInfo.setHeight(50);
                bloadLayout.setMinimumHeight(100);
            }
        }
        pushListV.setAdapter(adapter);
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        firstItem = firstVisibleItem;
        lastItem = totalItemCount - 1;
        if (firstVisibleItem + visibleItemCount == totalItemCount) {
            isLast = true;
        } else {
            isLast = false;
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int index, long id) {
        Intent ait = new Intent(this, MsgInfoActivity.class);
        if (index > 0 && index <= lastItem) {
            XGNotification xgnotification = adapter.getData().get(index - 1);
            ait.putExtra("msg_id", xgnotification.getMsg_id());
            ait.putExtra("title", xgnotification.getTitle());
            ait.putExtra("content", xgnotification.getContent());
            ait.putExtra("activity", xgnotification.getActivity());
            ait.putExtra("notificationActionType", xgnotification.getNotificationActionType());
            ait.putExtra("update_time", xgnotification.getUpdate_time());
            this.startActivity(ait);
        }

    }

    private class pushAdapter extends BaseAdapter {

        private Activity mActivity;

        private LayoutInflater mInflater;

        List<XGNotification> adapterData;

        public pushAdapter(Activity aActivity) {
            mActivity = aActivity;
            mInflater = LayoutInflater.from(mActivity);
        }

        public List<XGNotification> getData() {
            return adapterData;
        }

        public void setData(List<XGNotification> pushInfoList) {
            adapterData = pushInfoList;
        }

        @Override
        public int getCount() {
            return (null == adapterData ? 0 : adapterData.size());
        }

        @Override
        public Object getItem(int position) {
            return (null == adapterData ? null : adapterData.get(position));
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @SuppressLint("SimpleDateFormat")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            pushViewHolder aholder = null;
            XGNotification item = adapterData.get(position);
            if (convertView == null) {
                aholder = new pushViewHolder();
                convertView = mInflater.inflate(R.layout.item_push, null);
                aholder.msg_idv = (TextView) convertView.findViewById(R.id.push_msg_id);
                aholder.contentv = (TextView) convertView.findViewById(R.id.push_content);
                aholder.timev = (TextView) convertView.findViewById(R.id.push_time);
                aholder.titlev = (TextView) convertView.findViewById(R.id.push_title);
                convertView.setTag(aholder);
            } else {
                aholder = (pushViewHolder) convertView.getTag();
            }

            aholder.msg_idv.setText("ID:" + item.getMsg_id());
            aholder.titlev.setText(item.getTitle());
            aholder.contentv.setText(item.getContent());
            if (item.getUpdate_time() != null && item.getUpdate_time().length() > 18) {
                String notificationdate = item.getUpdate_time().substring(0, 10);
                String notificationtime = item.getUpdate_time().substring(11);
                if (new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime())
                        .equals(notificationdate)) {
                    aholder.timev.setText(notificationtime);
                } else {
                    aholder.timev.setText(notificationdate);
                }
            } else {
                aholder.timev.setText("未知");
            }
            return convertView;
        }
    };

    private class pushViewHolder {
        TextView msg_idv;

        TextView titlev;

        TextView timev;

        TextView contentv;
    }
/**
 * 
 * @Description  清空消息记录
 * @author zhangcan
 * @param v
 */
    public void clearMSG(View v) {
        NotificationService.getInstance(this).deleteAll();
        adapter.setData(null);
        adapter.notifyDataSetChanged();
        findViewById(R.id.nodata).setVisibility(View.VISIBLE);
        Toast.makeText(this, "清空成功", Toast.LENGTH_SHORT).show();

    }

}
