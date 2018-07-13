package com.zerone_catering.avtivity.openorderpage;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
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
import com.bumptech.glide.Glide;
import com.zerone_catering.Base64AndMD5.CreateToken;
import com.zerone_catering.Contants.ContantData;
import com.zerone_catering.Contants.IpConfig;
import com.zerone_catering.R;
import com.zerone_catering.adapter.cart_list.MakeOrderDetialsListItemAdapter;
import com.zerone_catering.avtivity.BaseSet.BaseActvity;
import com.zerone_catering.avtivity.details.Order_Details_Print_Activity;
import com.zerone_catering.domain.UserInfo;
import com.zerone_catering.domain.colse.CloseActivity;
import com.zerone_catering.domain.order.SubmitShopBean;
import com.zerone_catering.domain.refresh.RefreshBean;
import com.zerone_catering.domain.shoplistbean.ShopMessageBean;
import com.zerone_catering.utils.AppSharePreferenceMgr;
import com.zerone_catering.utils.DoubleUtils;
import com.zerone_catering.utils.GetUserInfo;
import com.zerone_catering.utils.LoadingUtils;
import com.zerone_catering.utils.NetUtils;
import com.zerone_catering.utils.OutSignCustomer;
import com.zerone_catering.utils.view.ListViewSetHightUtils;
import com.zerone_catering.view.MyNumberPicker;
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

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by on 2018/3/30 0030 11 36.
 * Author  LiuXingWen
 * 这个是确认订单页面
 * 选完商品后的下单前确认订单
 */

public class MakeSureTheOrderActivity extends BaseActvity implements NumberPicker.OnValueChangeListener, NumberPicker.Formatter, NumberPicker.OnScrollListener {

    //=========================定时器========================

    private Intent intent;
    private ArrayList<ShopMessageBean> listObj;
    private RelativeLayout submitbtn;
    private UserInfo userInfo;
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
    private TextView remark;
    private RelativeLayout relative_back;
    private TextView zhekou;
    private TextView zhekouValue;
    private MyNumberPicker numberPicker1;
    private String zkou = "";
    private Button btn_signin;
    private Dialog dialog;
    private ScrollView scrollView;
    private Timer timer;
    private TimerTask task;
    private Context mContext;
    private String terminalId;
    private String merchantId;
    private int signInt = 1;
    //签退页面
    private Dialog out_dialog;
    private Dialog sure_submit_order_dialog;
    private ZLoadingDialog confirm_loading;
    private ZLoadingDialog get_qrcode_loading;

    //0为支付不成功可以点击 1为该订单已提交
    private int payPostion = 0;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    String subJSon = (String) msg.obj;
                    try {
                        JSONObject jsonObject = new JSONObject(subJSon);
                        int status = jsonObject.getInt("status");
                        if (status == 1) {
                            //订单提交成功  获取商品数据 打印小票，吊起支付。
                            String orderid = jsonObject.getJSONObject("data").getString("order_id");
                            //订单提交成功 跳转到 订单详情
                            OutSignCustomer.signOut(userInfo, MakeSureTheOrderActivity.this, terminalId, handler, 6);
                            AppSharePreferenceMgr.put(MakeSureTheOrderActivity.this, "orderid", orderid);
                            EventBus.getDefault().post(new RefreshBean("清空购物车的类", ContantData.REFRESH_ONE));
                            //发送通知关闭activity页面
                            EventBus.getDefault().post(new CloseActivity("open", 1000));
                            if (sure_submit_order_dialog != null) {
                                sure_submit_order_dialog.dismiss();
                            }
                            customDialog("下单成功，去打印吧。", orderid);
                            payPostion = 1;
                        } else if (status == 0) {
                            //订单提交失败  提示用户失败的原因
                            Toast.makeText(MakeSureTheOrderActivity.this, "错误返回：" + jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                            payPostion = 0;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        confirm_loading.dismiss();
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
                    if (loading_dailog != null) {
                        loading_dailog.dismiss();
                    }
                    break;
            }


        }
    };
    private String tableid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_makesuretheorder);
        intent = getIntent();
        tableid = intent.getStringExtra("tableid");
        mContext = MakeSureTheOrderActivity.this;
        userInfo = GetUserInfo.initGetUserInfo(this);
        listObj = (ArrayList<ShopMessageBean>) getIntent().getSerializableExtra("listobj");
        //终端号
        terminalId = (String) AppSharePreferenceMgr.get(mContext, "terminalId", "");
        //pos商户号
        merchantId = (String) AppSharePreferenceMgr.get(mContext, "merchantId", "");
        initView();
        initAction();
    }


    /**
     * view的初始化
     */
    private void initView() {
        if (listObj != null) {
            for (int i = 0; i < listObj.size(); i++) {
                listObj.get(i).getSp_price();
                dSOMoney += Double.parseDouble(listObj.get(i).getSp_price()) * Integer.parseInt(listObj.get(i).getSp_count());
            }
        }

        scrollView = (ScrollView) findViewById(R.id.scrollView);
        zhekouValue = (TextView) findViewById(R.id.zhekouValue);
        goodslist = (ListView) findViewById(R.id.goodslist);
        if (listObj != null) {
            mAdapter = new MakeOrderDetialsListItemAdapter(MakeSureTheOrderActivity.this, listObj);
            goodslist.setAdapter(mAdapter);
        }

        ListViewSetHightUtils.setListViewHeightBasedOnChildren(goodslist);
        subMoney = (TextView) findViewById(R.id.subMoney);
        sureOrderMoney = (TextView) findViewById(R.id.sureOrderMoney);
        sureOrderMoney.setText("￥" + DoubleUtils.setSSWRDouble(dSOMoney));
        subMoney.setText("￥" + DoubleUtils.setSSWRDouble(dSOMoney));
        submitbtn = (RelativeLayout) findViewById(R.id.submitbtn);
        relative_back = (RelativeLayout) findViewById(R.id.relative_back);
        remark = (TextView) findViewById(R.id.remark);
        actionremark = (LinearLayout) findViewById(R.id.actionremark);
        writeoff = (LinearLayout) findViewById(R.id.writeoff);
        jiedaiyuan = (TextView) findViewById(R.id.jiedaiyuan);
        if (userInfo != null) {
            jiedaiyuan.setText(userInfo.getRealName());
        }
        zhekou = (TextView) findViewById(R.id.zhekou);

        if (listObj == null) {
            return;
        }

    }

    /**
     * 控件的点击事件
     */
    private void initAction() {
        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (payPostion == 0) {
                    sure_submit_order();
                } else if (payPostion == 1) {
                    Toast.makeText(MakeSureTheOrderActivity.this, "该单已提交，请不要重复提交", Toast.LENGTH_SHORT).show();
                }
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
        try {
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
            if (userInfo.getFansmanage_id() != null) {
                subMap.put("fansmanage_id", userInfo.getFansmanage_id());
            }
            if (timestamp != null) {
                subMap.put("timestamp", timestamp);
            }
            String remarks = remark.getText().toString();
            if (!TextUtils.isEmpty(remarks)) {
                subMap.put("remarks", remarks);
            }
            for (int i = 0; i < listObj.size(); i++) {
                SubmitShopBean submitShopBean = new SubmitShopBean();
                submitShopBean.setGoods_thumb(listObj.get(i).getSp_picture_url());
                submitShopBean.setNum(Integer.parseInt(listObj.get(i).getSp_count()));
                submitShopBean.setGoods_price(listObj.get(i).getSp_price());
                submitShopBean.setGoods_name(listObj.get(i).getSp_name());
                submitShopBean.setGoods_id(listObj.get(i).getSp_id());
                //商品规格
                subbeanlist.add(submitShopBean);
            }
            String string = JSON.toJSONString(subbeanlist);
            String goods = new String(string.getBytes(), "utf-8");
            if (goods != null) {
                subMap.put("goods_list", goods);
            }
            if (tableid != null) {
                subMap.put("table_id", tableid);
            }
            confirm_loading = LoadingUtils.getDailog(MakeSureTheOrderActivity.this, Color.RED, "提交订单中。。。。");
            confirm_loading.show();
            NetUtils.netWorkByMethodPost(MakeSureTheOrderActivity.this, subMap, IpConfig.URL_SUBMIT, handler, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }


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
        //禁止输入
        numberPicker1.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        MakeSureMethod.setNumberPickerDividerColor(numberPicker1, MakeSureTheOrderActivity.this);
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
    private void setSignInCustomer(String url) {
        dialog = new Dialog(this, R.style.NormalDialogStyle);
        createTask();
        View view = View.inflate(this, R.layout.activity_dialog_signin_view, null);
        ImageView signin_img = view.findViewById(R.id.signin_img);
        TextView cancel = view.findViewById(R.id.cancel);
        TextView confirm = view.findViewById(R.id.confirm);
        Glide.with(MakeSureTheOrderActivity.this).load(url).into(signin_img);
        dialog.setContentView(view);
        //使得点击对话框外部不消失对话框
        dialog.setCanceledOnTouchOutside(false);
        //设置对话框的大小
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                MakeSureMethod.stopTimer(timer, task, dialog);
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                MakeSureMethod.stopTimer(timer, task, dialog);
            }
        });
        dialog.show();
    }

    /**
     * 自定义用户签退的view
     */
    private void setSignOutCustomer() {
        out_dialog = new Dialog(this, R.style.NormalDialogStyle);
        View view = View.inflate(this, R.layout.activity_dialog_signout_view, null);
        TextView cancel = view.findViewById(R.id.signinout_cancel);
        TextView confirm = view.findViewById(R.id.signinout_confirm);
        CircleImageView signvipimg = view.findViewById(R.id.signvipimg);
        TextView signvipname = view.findViewById(R.id.signvipname);

        out_dialog.setContentView(view);
        //使得点击对话框外部不消失对话框
        out_dialog.setCanceledOnTouchOutside(false);
        //设置对话框的大小
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                out_dialog.dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OutSignCustomer.signOut(userInfo, MakeSureTheOrderActivity.this, terminalId, handler, 5);
                signInt = 1;
            }
        });
        out_dialog.show();
    }

    /**
     * 定义确认提交订单的弹框选择
     */
    private void sure_submit_order() {
        sure_submit_order_dialog = new Dialog(this, R.style.NormalDialogStyle);
        View view = View.inflate(this, R.layout.activity_dialog_makesureorder_view, null);
        TextView sure = view.findViewById(R.id.sure);
        TextView msg = view.findViewById(R.id.msg);
        TextView cancel = view.findViewById(R.id.ordersure_cancel);
        TextView confirm = view.findViewById(R.id.ordersure_confirm);
        sure_submit_order_dialog.setContentView(view);
        //使得点击对话框外部不消失对话框
        sure_submit_order_dialog.setCanceledOnTouchOutside(false);
        //设置对话框的大小
        msg.setText("确认提交点餐订单？");
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sure_submit_order_dialog.dismiss();
                EventBus.getDefault().post(new RefreshBean("清空购物车的类", ContantData.REFRESH_ONE));
                //发送通知关闭activity页面
                EventBus.getDefault().post(new CloseActivity("open", 1000));
                MakeSureTheOrderActivity.this.finish();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //提交订单
                gotoSubmit();
            }
        });
        sure_submit_order_dialog.show();
    }

    public void createTask() {
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 1100;
                handler.sendMessage(message);
            }
        };
        timer.schedule(task, 0, 2000);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 弹框提示
     *
     * @param msg 提示消息
     */
    private void customDialog(String msg, final String orderid) {
        dialog = new Dialog(this, R.style.NormalDialogStyle);
        View view = View.inflate(this, R.layout.activity_dialog_view, null);
        TextView cancel = view.findViewById(R.id.cancel);
        TextView confirm = view.findViewById(R.id.confirm);
        TextView errormsg = view.findViewById(R.id.errormsg);
        errormsg.setText(msg);
        dialog.setContentView(view);
        //使得点击对话框外部不消失对话框
        dialog.setCanceledOnTouchOutside(false);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MakeSureTheOrderActivity.this, Order_Details_Print_Activity.class);
                intent.putExtra("order_id", orderid);
                startActivity(intent);
                MakeSureTheOrderActivity.this.finish();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

}
