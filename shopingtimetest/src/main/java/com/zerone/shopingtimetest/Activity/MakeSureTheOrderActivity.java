package com.zerone.shopingtimetest.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.zerone.shopingtimetest.Adapter.cart_list.MakeOrderDetialsListItemAdapter;
import com.zerone.shopingtimetest.Base64AndMD5.CreateToken;
import com.zerone.shopingtimetest.BaseActivity.BaseAppActivity;
import com.zerone.shopingtimetest.Bean.UserInfo;
import com.zerone.shopingtimetest.Bean.order.SubmitDataBean;
import com.zerone.shopingtimetest.Bean.order.SubmitShopBean;
import com.zerone.shopingtimetest.Bean.refresh.RefreshBean;
import com.zerone.shopingtimetest.Bean.shoplistbean.ShopMessageBean;
import com.zerone.shopingtimetest.Contants.ContantData;
import com.zerone.shopingtimetest.Contants.IpConfig;
import com.zerone.shopingtimetest.DB.impl.UserInfoImpl;
import com.zerone.shopingtimetest.R;
import com.zerone.shopingtimetest.Utils.AppSharePreferenceMgr;
import com.zerone.shopingtimetest.Utils.DoubleUtils;
import com.zerone.shopingtimetest.Utils.LoadingUtils;
import com.zerone.shopingtimetest.Utils.NetUtils;
import com.zyao89.view.zloading.ZLoadingDialog;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by on 2018/3/30 0030 11 36.
 * Author  LiuXingWen
 * 这个是确认订单页面
 * 选完商品后的下单前确认订单
 */

public class MakeSureTheOrderActivity extends BaseAppActivity implements NumberPicker.OnValueChangeListener, NumberPicker.Formatter, NumberPicker.OnScrollListener {

    //=========================定时器========================

    int i = 0;
    private Intent intent;
    private ArrayList<ShopMessageBean> listObj;
    private RelativeLayout submitbtn;
    private UserInfo userInfo;
    private ZLoadingDialog loading;
    private List<SubmitShopBean> subbeanlist;
    private ZLoadingDialog loading_dailog;
    private ListView goodslist;
    private MakeOrderDetialsListItemAdapter mAdapter;
    private TextView sureOrderMoney;
    private LinearLayout actionremark;
    private LinearLayout writeoff;
    private TextView jiedaiyuan;
    private double dSOMoney = 0.00;
    private TextView subMoney;
    private ImageView back;
    private TextView remark;
    private RelativeLayout relative_back;
    private TextView zhekou;
    private NumberPicker numberPicker;
    private EditText discountValue;
    private TextView zhekouValue;
    private NumberPicker numberPicker1;
    private String zkou = "";
    private Button btn_signin;
    private Dialog dialog;
    private ScrollView scrollView;
    private Timer timer;
    private TimerTask task;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    loading.dismiss();
                    String subJSon = (String) msg.obj;
                    Log.i("URL", "" + subJSon);
                    try {
                        JSONObject jsonObject = new JSONObject(subJSon);
                        int status = jsonObject.getInt("status");
                        if (status == 1) {
                            //订单提交成功  获取商品数据 打印小票，吊起支付。
                            String orderid = jsonObject.getJSONObject("data").getString("order_id");
                            //订单提交成功 跳转到 订单详情
                            Intent intent = new Intent(MakeSureTheOrderActivity.this, OrderDetailsActivity.class);
                            intent.putExtra("orderid", orderid);
                            startActivity(intent);
                            AppSharePreferenceMgr.put(MakeSureTheOrderActivity.this, "orderid", orderid);
                            MakeSureTheOrderActivity.this.finish();
                            EventBus.getDefault().post(new RefreshBean("清空购物车的类", ContantData.REFRESH_ONE));
                        } else if (status == 0) {
                            //订单提交失败  提示用户失败的原因
                            Toast.makeText(MakeSureTheOrderActivity.this, "错误返回：" + jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        submitbtn.setEnabled(true);
                    }
                    break;

                case 2:
                    String reinfo = (String) msg.obj;
                    if (reinfo != null) {
                        remark.setText(reinfo);
                    }
                    break;

                case 10:
                    String rein = (String) msg.obj;
                    if (rein != null) {
                        zhekouValue.setText(rein);
                    }
                    break;
                case 511:
                    Toast.makeText(MakeSureTheOrderActivity.this, "网络超时，请重试", Toast.LENGTH_SHORT).show();
                    if (loading_dailog != null) {
                        loading_dailog.dismiss();
                    }
                    break;
                case 1100:
                    int i = (int) msg.obj;

                    if (i == 10) {
                        stopTimer();

                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_makesuretheorder);
        intent = getIntent();
        initGetUserInfo();
        listObj = (ArrayList<ShopMessageBean>) getIntent().getSerializableExtra("listobj");
        initView();
        initViewBtn();
        initAction();
    }

    private void initViewBtn() {
        btn_signin = (Button) findViewById(R.id.btn_Signin);
        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSignInCustomer();
            }
        });
    }

    /**
     * 获取用户信息
     */
    private void initGetUserInfo() {

        UserInfoImpl userInfoImpl = new UserInfoImpl(MakeSureTheOrderActivity.this);
        try {
            userInfo = userInfoImpl.getUserInfo("10");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * view的初始化
     */
    private void initView() {
        if (listObj == null) {
            return;
        }
        for (int i = 0; i < listObj.size(); i++) {
            listObj.get(i).getSp_price();
            dSOMoney += Double.parseDouble(listObj.get(i).getSp_price()) * Integer.parseInt(listObj.get(i).getSp_count());
        }
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        zhekouValue = (TextView) findViewById(R.id.zhekouValue);
        goodslist = (ListView) findViewById(R.id.goodslist);
        goodslist.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //事件处理
                scrollView.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        mAdapter = new MakeOrderDetialsListItemAdapter(MakeSureTheOrderActivity.this, listObj);
        goodslist.setAdapter(mAdapter);
        subMoney = (TextView) findViewById(R.id.subMoney);
        sureOrderMoney = (TextView) findViewById(R.id.sureOrderMoney);
        sureOrderMoney.setText("￥" + DoubleUtils.setSSWRDouble(dSOMoney));
        subMoney.setText("￥" + DoubleUtils.setSSWRDouble(dSOMoney));
        submitbtn = (RelativeLayout) findViewById(R.id.submitbtn);
        relative_back = (RelativeLayout) findViewById(R.id.relative_back);
        back = (ImageView) findViewById(R.id.back);
        remark = (TextView) findViewById(R.id.remark);
        actionremark = (LinearLayout) findViewById(R.id.actionremark);
        writeoff = (LinearLayout) findViewById(R.id.writeoff);
        jiedaiyuan = (TextView) findViewById(R.id.jiedaiyuan);
        if (userInfo != null) {
            jiedaiyuan.setText(userInfo.getRealName());
        }

        zhekou = (TextView) findViewById(R.id.zhekou);
    }

    /**
     * 控件的点击事件
     */
    private void initAction() {
        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitbtn.setEnabled(false);
                gotoSubmit();
            }
        });
        relative_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MakeSureTheOrderActivity.this.finish();
            }
        });

        actionremark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog();
            }
        });

        //取消订单
        writeoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MakeSureTheOrderActivity.this.finish();
            }
        });

        zhekou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDiscount();
            }
        });
    }

    /**
     * 设置提交订单时的数据整理
     */
    private void gotoSubmit() {
        if (userInfo == null) {
            return;
        }
        subbeanlist = new ArrayList<>();
        String timestamp = System.currentTimeMillis() + "";
        String token = CreateToken.createToken(userInfo.getUuid(), timestamp, userInfo.getAccount());
        Map<String, String> subMap = new HashMap<String, String>();
        if (userInfo.getOrganization_id() != null) {
            subMap.put("organization_id", userInfo.getOrganization_id());
        }

        if (userInfo.getAccount_id() != null) {
            subMap.put("account_id", userInfo.getAccount_id());
        }
        if (token != null) {
            subMap.put("token", token);
        }

        //0为散客
        subMap.put("user_id", "0");
        if (timestamp != null) {
            subMap.put("timestamp", timestamp);
        }
        String remarks = remark.getText().toString();
        if (!TextUtils.isEmpty(remarks)) {
            subMap.put("remarks", remarks);
        }
        for (int i = 0; i < listObj.size(); i++) {
            SubmitShopBean submitShopBean = new SubmitShopBean();
            submitShopBean.setId(listObj.get(i).getSp_id());
            submitShopBean.setNum(listObj.get(i).getSp_count());
            submitShopBean.setPrice(listObj.get(i).getSp_price());
            //商品规格
            //submitShopBean.setSpec("0");
            subbeanlist.add(submitShopBean);
        }
        SubmitDataBean sdb = new SubmitDataBean(subbeanlist);
        String listJSOn = JSON.toJSONString(sdb);
        String goodsdata = null;
        try {
            goodsdata = new String(listJSOn.getBytes(), "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (goodsdata != null) {
            subMap.put("goodsdata", goodsdata);
        }
        if (zkou != null) {
            subMap.put("discount", zkou);
        }
        loading = LoadingUtils.getDailog(MakeSureTheOrderActivity.this, Color.RED, "提交订单中。。。。");
        loading.show();
        NetUtils.netWorkByMethodPost(MakeSureTheOrderActivity.this, subMap, IpConfig.URL_SUBMITORDER, handler, 0);
    }

    /**
     * 自定义对话框
     */
    private void customDialog() {
        final Dialog dialog = new Dialog(this, R.style.NormalDialogStyle);
        View view = View.inflate(this, R.layout.activity_dialog_edit_view, null);
        TextView cancel = view.findViewById(R.id.cancel);
        TextView confirm = view.findViewById(R.id.confirm);
        final EditText writeremake = view.findViewById(R.id.writeremake);
        dialog.setContentView(view);
        //使得点击对话框外部不消失对话框
        dialog.setCanceledOnTouchOutside(true);
        //设置对话框的大小
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String reinfo = writeremake.getText().toString();
                Message message = new Message();
                message.what = 2;
                message.obj = reinfo;
                handler.sendMessage(message);
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    //================================客户签入=============================

    /**
     * 自定义对话框
     */
    private void setDiscount() {
        final Dialog dialog = new Dialog(this, R.style.NormalDialogStyle);
        View view = View.inflate(this, R.layout.activity_dialog_edit_discount_view, null);
//        discountValue = (EditText) view.findViewById(R.id.discount);
        TextView cancel = view.findViewById(R.id.cancel);
        TextView confirm = view.findViewById(R.id.confirm);
        numberPicker1 = view.findViewById(R.id.numberPicker);
        numberPicker1.setFormatter(this);
        numberPicker1.setOnValueChangedListener(this);
        numberPicker1.setOnScrollListener(this);
        final String[] array = new String[100];
        int i = 0;

        for (double d = 9.9; d > 0; d -= 0.1) {
            array[i] = new DecimalFormat("0.0").format(d);
            i++;
        }
        numberPicker1.setDisplayedValues(array);
        //设置最大最小值
        numberPicker1.setMinValue(1);
        numberPicker1.setMaxValue(100);
        //设置默认的位置
        numberPicker1.setValue(1);
        dialog.setContentView(view);
        //使得点击对话框外部不消失对话框
        dialog.setCanceledOnTouchOutside(true);
        //设置对话框的大小
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = new Message();
                message.what = 10;
                zkou = array[numberPicker1.getValue() - 1];
                message.obj = array[numberPicker1.getValue() - 1];
                handler.sendMessage(message);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public String format(int value) {

        return null;
    }

    @Override
    public void onScrollStateChange(NumberPicker view, int scrollState) {
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

    }

    /**
     * 自定义用户签入的view
     */
    private void setSignInCustomer() {
        dialog = new Dialog(this, R.style.NormalDialogStyle);
        View view = View.inflate(this, R.layout.activity_dialog_signin_view, null);
        TextView cancel = view.findViewById(R.id.cancel);
        TextView confirm = view.findViewById(R.id.confirm);
        dialog.setContentView(view);
        //使得点击对话框外部不消失对话框
        dialog.setCanceledOnTouchOutside(true);
        //设置对话框的大小
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                stopTimer();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                stopTimer();
            }
        });
        dialog.show();
        createTask();
    }

    public void createTask() {
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                Log.i("URL", ++i + "");
                Message message = new Message();
                message.what = 1100;
                message.obj = i;
                handler.sendMessage(message);
            }
        };
        timer.schedule(task, 0, 1000);
    }

    public void stopTimer() {
        i = 0;
        timer.cancel();
        timer = null;
        task.cancel();
        task = null;
        Log.i("URL", "停止了吗");
        dialog.dismiss();
    }
}
