package com.store.zerone.zeronestore;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.githang.statusbar.StatusBarCompat;
import com.store.zerone.zeronestore.contanst.ContantData;
import com.store.zerone.zeronestore.demo1.TestGoods;
import com.store.zerone.zeronestore.event.ChangeSelectedTab;
import com.store.zerone.zeronestore.event.MessageEvent;
import com.store.zerone.zeronestore.fragment.OrderControlFrgment;
import com.store.zerone.zeronestore.fragment.ShopingFragment;
import com.store.zerone.zeronestore.utils.AnimationUtil;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    /**
     * 双击退出函数
     */
    private static Boolean isExit = false;
    private LinearLayout llIndex, lltwo, llthree;
    private ImageView ivIndex, ivtwo, ivthree, ivCurrent;
    private TextView tvIndex, tvtwo, tvhree, tvCurrent;
    private android.app.FragmentManager fragmentManager;
    private android.app.FragmentTransaction beginTransaction;
    private TextView goTOCheckOut;
    private ImageView m_waiter_btn;
    private MessageEvent messageEvent;
    private RelativeLayout shopCartMain;
    private TextView shopCartNum;
    private TextView totalPrice;
    private TextView noShop;
    private TextView title;
    private ImageView scanqr;
    private LayoutInflater layoutInflater;
    private ViewGroup anim_mask_layout;//动画层
    private List<TestGoods> list;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    shopCartNum.setVisibility(View.GONE);
                    totalPrice.setVisibility(View.GONE);
                    noShop.setVisibility(View.VISIBLE);
                    totalPrice.setText("");
                    shopCartNum.setText("");
                    break;
                //扫码后的结果
                case 2:
                    String code = (String) msg.obj;
                    for (int i = 0; i < list.size(); i++) {
                        if (code.equals(list.get(i).getGoodsBarCode())) {
                            Toast.makeText(MainActivity.this, "商品信息" + list.get(i).toString(), Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }
                    break;
                case 10:
                    title.setText("收银");
                    break;
                case 11:
                    title.setText("订单");
                    break;
            }
        }
    };
    private View scanqr_pop_view;
    private PopupWindow scanqr_popwindow;
    private LinearLayout parentview;
    private RelativeLayout scan_btn;
    private TextView scan_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_main);
        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#ffffff"));
//        initData();
        initView();
        dolistenner();

    }

    private void initData() {
        list = new ArrayList<TestGoods>();
        //商品的实力化
        TestGoods tgoods = new TestGoods();
        tgoods.setGoodsBarCode("6941143500177");
        tgoods.setGoodsMoney("318");
        tgoods.setGoodsName("英皇保罗");
        tgoods.setGoodsNumber("001");
        list.add(tgoods);
        TestGoods tgoods2 = new TestGoods();
        tgoods2.setGoodsBarCode("6930373500316");
        tgoods2.setGoodsMoney("12.5");
        tgoods2.setGoodsName("笔记本");
        tgoods2.setGoodsNumber("002");
        list.add(tgoods2);
        TestGoods tgoods3 = new TestGoods();
        tgoods3.setGoodsBarCode("6946891702422");
        tgoods3.setGoodsMoney("120.88");
        tgoods3.setGoodsName("鲜花");
        tgoods3.setGoodsNumber("003");
        list.add(tgoods3);

    }

    private void initView() {
        parentview = (LinearLayout) findViewById(R.id.activity_main);
        //---------------scanqr----------
        if (scanqr_pop_view == null) {
            scanqr_pop_view = LayoutInflater.from(this).inflate(R.layout.activity_scanqr_pop, null);
        }

        scan_btn = (RelativeLayout) scanqr_pop_view.findViewById(R.id.scan_btn);

        scan_cancel = (TextView) scanqr_pop_view.findViewById(R.id.scan_cancel);

        //---------------scanqr------------
        scanqr = (ImageView) findViewById(R.id.scanqr);
        title = (TextView) findViewById(R.id.title);
        llIndex = (LinearLayout) findViewById(R.id.llIndex);
        lltwo = (LinearLayout) findViewById(R.id.lltwo);
        llIndex.setOnClickListener(this);
        lltwo.setOnClickListener(this);
        ivIndex = (ImageView) findViewById(R.id.ivIndex);
        ivtwo = (ImageView) findViewById(R.id.ivtwo);
//        ivthree = (ImageView) findViewById(R.id.ivthree);
        tvIndex = (TextView) findViewById(R.id.tvIndex);
        tvtwo = (TextView) findViewById(R.id.tvtwo);
        ivIndex.setSelected(true);
        tvIndex.setSelected(true);
        ivCurrent = ivIndex;
        tvCurrent = tvIndex;
        fragmentManager = getFragmentManager();
        beginTransaction = fragmentManager.beginTransaction();
        beginTransaction.replace(R.id.ll_main, new ShopingFragment());
        beginTransaction.commit();
        goTOCheckOut = (TextView) findViewById(R.id.goTOCheckOut);
        shopCartMain = (RelativeLayout) findViewById(R.id.shopCartMain);
        shopCartNum = (TextView) findViewById(R.id.shopCartNum);
        totalPrice = (TextView) findViewById(R.id.totalPrice);
        noShop = (TextView) findViewById(R.id.noShop);
    }

    /**
     * 点击事件
     */
    private void dolistenner() {
        goTOCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(NewMainActivity.this, ListGoodsDetailsActivity.class);
//                intent.putExtra("goods", messageEvent);
//                TableDBean.DataBean      tableBean = (TableDBean.DataBean) Utils.getACache(NewMainActivity.this).getAsObject("tableBean");
//
//                if (tableBean==null){
//                    Toast.makeText(NewMainActivity.this, "请选择餐桌", Toast.LENGTH_SHORT).show();
//                    return;
//                }
////                startActivity(intent);
//                startActivityForResult(intent, ContantData.MAINMAKEORDERREQUESE);
            }
        });

//        m_waiter_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Intent intn  = new Intent(NewMainActivity.this, CheckWaiterActivity.class);
////                startActivity(intn);
////                removeALLActivity();
//            }
//        });


        scanqr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //打开扫码的popwindow
                setPopScanQrWindow();
//                Intent intent = new Intent(MainActivity.this, ScanQrCodeGetGoods.class);
//                startActivityForResult(intent, ContantData.SCANQRCODE);
            }
        });

        /**
         * 启动扫码功能
         *
         */
        scan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
                startActivityForResult(intent, 520);
            }
        });

        /**
         * 管闭扫码功能
         */
        scan_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanqr_popwindow.dismiss();
            }
        });
    }

    @Override
    public void onClick(View v) {
        ivCurrent.setSelected(false);
        tvCurrent.setSelected(false);
        android.app.FragmentManager fragmentManager = getFragmentManager();
        android.app.FragmentTransaction beginTransaction = fragmentManager
                .beginTransaction();
        switch (v.getId()) {
            case R.id.llIndex:
                beginTransaction.replace(R.id.ll_main, new ShopingFragment());
                shopCartMain.setVisibility(View.VISIBLE);
                shopCartMain.startAnimation(
                        AnimationUtil.createInAnimation(MainActivity.this, shopCartMain.getMeasuredHeight()));
            case 0:
                ivIndex.setSelected(true);
                ivCurrent = ivIndex;
                tvIndex.setSelected(true);
                tvCurrent = tvIndex;
                Message message = new Message();
                message.what = 10;
                handler.sendMessage(message);
                scanqr.setVisibility(View.VISIBLE);
                break;
            case R.id.lltwo:
                beginTransaction.replace(R.id.ll_main, new OrderControlFrgment());
                if (shopCartMain.isShown()) {
                    shopCartMain.startAnimation(
                            AnimationUtil.createOutAnimation(MainActivity.this, shopCartMain.getMeasuredHeight()));
                    shopCartMain.setVisibility(View.GONE);
                }
            case 1:
                ivtwo.setSelected(true);
                ivCurrent = ivtwo;
                tvtwo.setSelected(true);
                tvCurrent = tvtwo;
                Message message01 = new Message();
                message01.what = 11;
                handler.sendMessage(message01);
                scanqr.setVisibility(View.GONE);
                break;
//            case R.id.llthree:
//                beginTransaction.replace(R.id.ll_main, new SelectedTableFrgment());
//                if (shopCartMain.isShown()){
//                    shopCartMain.startAnimation(
//                            AnimationUtil.createOutAnimation(NewMainActivity.this, shopCartMain.getMeasuredHeight()));
//                    shopCartMain.setVisibility(View.GONE);
//                }
//            case 2:
//                ivthree.setSelected(true);
//                ivCurrent = ivthree;
//                tvhree.setSelected(true);
//                tvCurrent = tvhree;
//                Message message02  = new Message();
//                message02.what=12;
//                handler.sendMessage(message02);
//                break;
            default:
                break;
        }
        beginTransaction.commit();
    }

    /**
     * 添加 或者  删除  商品发送的消息处理
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (event != null) {
            messageEvent = event;
            Log.i("URLF", event.list.toString());
            if (event.num > 0) {
                shopCartNum.setText(String.valueOf(event.num));
                shopCartNum.setVisibility(View.VISIBLE);
                totalPrice.setVisibility(View.VISIBLE);
                noShop.setVisibility(View.GONE);
            } else {
                shopCartNum.setVisibility(View.GONE);
                totalPrice.setVisibility(View.GONE);
                noShop.setVisibility(View.VISIBLE);
            }
            totalPrice.setText("¥" + String.valueOf(event.price));
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                exitBy2Click();
                break;
        }
        return true;
    }

    public void exitBy2Click() {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true; // 准备退出
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 1000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务
        } else {
            finish();
        }
    }

    /**
     * 设置动画（点击添加商品）
     *
     * @param v
     * @param startLocation
     */
    public void setAnim(final View v, int[] startLocation) {
        anim_mask_layout = null;
        anim_mask_layout = createAnimLayout();
        anim_mask_layout.addView(v);//把动画小球添加到动画层
        final View view = addViewToAnimLayout(anim_mask_layout, v, startLocation);
        int[] endLocation = new int[2];// 存储动画结束位置的X、Y坐标
        shopCartNum.getLocationInWindow(endLocation);
        // 计算位移
        int endX = 0 - startLocation[0] + 40;// 动画位移的X坐标
        int endY = endLocation[1] - startLocation[1];// 动画位移的y坐标

        TranslateAnimation translateAnimationX = new TranslateAnimation(0, endX, 0, 0);
        translateAnimationX.setInterpolator(new LinearInterpolator());
        translateAnimationX.setRepeatCount(0);// 动画重复执行的次数
        translateAnimationX.setFillAfter(true);

        TranslateAnimation translateAnimationY = new TranslateAnimation(0, 0, 0, endY);
        translateAnimationY.setInterpolator(new AccelerateInterpolator());
        translateAnimationY.setRepeatCount(0);// 动画重复执行的次数
        translateAnimationY.setFillAfter(true);

        AnimationSet set = new AnimationSet(false);
        set.setFillAfter(false);
        set.addAnimation(translateAnimationY);
        set.addAnimation(translateAnimationX);
        set.setDuration(400);// 动画的执行时间
        view.startAnimation(set);
        // 动画监听事件
        set.setAnimationListener(new Animation.AnimationListener() {
            // 动画的开始
            @Override
            public void onAnimationStart(Animation animation) {
                v.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            // 动画的结束
            @Override
            public void onAnimationEnd(Animation animation) {
                v.setVisibility(View.GONE);
            }
        });

    }

    /**
     * 初始化动画图层
     *
     * @return
     */
    private ViewGroup createAnimLayout() {
        ViewGroup rootView = (ViewGroup) this.getWindow().getDecorView();
        LinearLayout animLayout = new LinearLayout(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        animLayout.setLayoutParams(lp);
        animLayout.setId(Integer.MAX_VALUE - 1);
        animLayout.setBackgroundResource(android.R.color.transparent);
        rootView.addView(animLayout);
        return animLayout;
    }

    /**
     * 将View添加到动画图层
     *
     * @param parent
     * @param view
     * @param location
     * @return
     */
    private View addViewToAnimLayout(final ViewGroup parent, final View view,
                                     int[] location) {
        int x = location[0];
        int y = location[1];
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = x;
        lp.topMargin = y;
        view.setLayoutParams(lp);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ContantData.MAINMAKEORDERREQUESE && resultCode == RESULT_OK) {
            //清空购物车和总价
            Message message = new Message();
            message.what = 1;
            handler.sendMessage(message);
        } else if (requestCode == ContantData.SCANQRCODE && resultCode == RESULT_OK) {
            String goodscode = data.getStringExtra("result");
            Log.i("URL", goodscode + "888888888888888888888");
            //清空购物车和总价
            Message message = new Message();
            message.what = 2;
            message.obj = goodscode;
            handler.sendMessage(message);
        } else if (requestCode == 520) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    Toast.makeText(this, "解析结果:" + result, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent();
                    intent.putExtra("result", result);
                    setResult(RESULT_OK, intent);
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(MainActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    /**
     * 选完桌子后显示点餐页面
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onChangeEvent(ChangeSelectedTab event) {
        if (event == null) {
            return;
        }
        android.app.FragmentManager fragmentManager = getFragmentManager();
        android.app.FragmentTransaction beginTransaction = fragmentManager
                .beginTransaction();
        ivIndex.setSelected(true);
        tvIndex.setSelected(true);
        ivCurrent = ivIndex;
        tvCurrent = tvIndex;
        beginTransaction.replace(R.id.ll_main, new ShopingFragment());
        shopCartMain.setVisibility(View.VISIBLE);
        shopCartMain.startAnimation(AnimationUtil.createInAnimation(MainActivity.this, shopCartMain.getMeasuredHeight()));
        beginTransaction.commit();
        Message message = new Message();
        message.what = 10;
        handler.sendMessage(message);
    }


    //
    public void setPopScanQrWindow() {
        scanqr_popwindow = new PopupWindow(scanqr_pop_view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
        scanqr_popwindow.setTouchable(true);
        scanqr_popwindow.setOutsideTouchable(false);
        scanqr_popwindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        scanqr_popwindow.showAtLocation(parentview, Gravity.CENTER, 0, 0);
    }


}
