package com.zerone.store.shopingtimetest.Activity.makesureorder;

import android.app.Dialog;
import android.content.Context;
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
import com.bumptech.glide.Glide;
import com.zerone.store.shopingtimetest.Activity.details.OrderDetailsDFKActivity;
import com.zerone.store.shopingtimetest.Adapter.cart_list.MakeOrderDetialsListItemAdapter;
import com.zerone.store.shopingtimetest.Base64AndMD5.CreateToken;
import com.zerone.store.shopingtimetest.BaseActivity.BaseAppActivity;
import com.zerone.store.shopingtimetest.Bean.UserInfo;
import com.zerone.store.shopingtimetest.Bean.UserInfoVip;
import com.zerone.store.shopingtimetest.Bean.order.SubmitDataBean;
import com.zerone.store.shopingtimetest.Bean.order.SubmitShopBean;
import com.zerone.store.shopingtimetest.Bean.refresh.RefreshBean;
import com.zerone.store.shopingtimetest.Bean.shoplistbean.ShopMessageBean;
import com.zerone.store.shopingtimetest.Contants.ContantData;
import com.zerone.store.shopingtimetest.Contants.IpConfig;
import com.zerone.store.shopingtimetest.R;
import com.zerone.store.shopingtimetest.Utils.AppSharePreferenceMgr;
import com.zerone.store.shopingtimetest.Utils.DoubleUtils;
import com.zerone.store.shopingtimetest.Utils.GetUserInfo;
import com.zerone.store.shopingtimetest.Utils.LoadingUtils;
import com.zerone.store.shopingtimetest.Utils.NetUtils;
import com.zerone.store.shopingtimetest.Utils.OutSignCustomer;
import com.zerone.store.shopingtimetest.view.MyNumberPicker;
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

public class MakeSureTheOrderActivity extends BaseAppActivity implements NumberPicker.OnValueChangeListener, NumberPicker.Formatter, NumberPicker.OnScrollListener {

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
    private UserInfoVip userinfovip;
    private CircleImageView userHeadImg;
    private TextView vip_;
    private Dialog sure_submit_order_dialog;
    private ZLoadingDialog confirm_loading;
    private ZLoadingDialog get_qrcode_loading;

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
                            Intent intent = new Intent(MakeSureTheOrderActivity.this, OrderDetailsDFKActivity.class);
                            intent.putExtra("orderid", orderid);
                            startActivity(intent);
                            AppSharePreferenceMgr.put(MakeSureTheOrderActivity.this, "orderid", orderid);
                            MakeSureTheOrderActivity.this.finish();
                            EventBus.getDefault().post(new RefreshBean("清空购物车的类", ContantData.REFRESH_ONE));
                            if (sure_submit_order_dialog != null) {
                                sure_submit_order_dialog.dismiss();
                            }
                        } else if (status == 0) {
                            //订单提交失败  提示用户失败的原因
                            Toast.makeText(MakeSureTheOrderActivity.this, "错误返回：" + jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
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
                case 3:
                    String qrcodeJson = (String) msg.obj;
                    try {
                        JSONObject jsonObject = new JSONObject(qrcodeJson);
                        int return_code = jsonObject.getInt("return_code");
                        if (return_code == 1) {
                            String qrUrl = jsonObject.getJSONObject("data").getString("url");
                            setSignInCustomer(qrUrl);
                        } else if (return_code == 0) {
                            Toast.makeText(MakeSureTheOrderActivity.this, "获取图片二维码失败，请重新获取", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(MakeSureTheOrderActivity.this, qrcodeJson.toString(), Toast.LENGTH_SHORT).show();
                    } finally {
                        if (get_qrcode_loading != null) {
                            get_qrcode_loading.dismiss();
                        }
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
                case 1100:
                    getUserInfo();
                    break;
                case 4:
                    String userinfoJson = (String) msg.obj;
                    try {
                        JSONObject userInfoJson = new JSONObject(userinfoJson);
                        int return_code = userInfoJson.getInt("return_code");
                        if (return_code == 1) {
                            String id = userInfoJson.getJSONObject("data").getString("fansmanage_user_id");
                            if ("false".equals(id)) {
                                Log.i("URL", "用户没有注册，正在注册中请稍后！！！！！！！！！！！！！！！");
                            } else {
                                userinfovip.setDevice_num(userInfoJson.getJSONObject("data").getString("device_num"));
                                userinfovip.setHead_imgurl(userInfoJson.getJSONObject("data").getString("head_imgurl"));
                                userinfovip.setNickname(userInfoJson.getJSONObject("data").getString("nickname"));
                                userinfovip.setUser_id(userInfoJson.getJSONObject("data").getString("user_id"));
                                userinfovip.setFansmanage_user_id(userInfoJson.getJSONObject("data").getString("fansmanage_user_id"));
                                userHeadImg.setVisibility(View.VISIBLE);
                                Glide.with(MakeSureTheOrderActivity.this).load(userinfovip.getHead_imgurl()).into(userHeadImg);
                                vip_.setText(userinfovip.getNickname());
                                btn_signin.setText("客户签退");
                                btn_signin.setBackgroundColor(Color.parseColor("#aafe4543"));
                                signInt = 2;
                                MakeSureMethod.stopTimer(timer, task, dialog);
                            }
                        } else {
                            Log.i("URL", "查询数据失败，正在努力查询中。。。");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 5:
                    String outSignJson = (String) msg.obj;
                    try {
                        JSONObject outJson = new JSONObject(outSignJson);
                        int return_code = outJson.getInt("return_code");
                        if (return_code == 1) {
                            userHeadImg.setVisibility(View.GONE);
                            vip_.setText("");
                            btn_signin.setText("客户签入");
                            btn_signin.setBackgroundColor(Color.parseColor("#fe4543"));
                            if (out_dialog != null) {
                                out_dialog.dismiss();
                            }
                        } else if (return_code == 0) {
                            Toast.makeText(MakeSureTheOrderActivity.this, "签退失败,请重试", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 6:
                    String outSignJso = (String) msg.obj;
                    try {
                        JSONObject outJso = new JSONObject(outSignJso);
                        int return_code = outJso.getInt("return_code");
                        if (return_code == 1) {
                            Toast.makeText(MakeSureTheOrderActivity.this, "签退成功", Toast.LENGTH_SHORT).show();
                            userHeadImg.setVisibility(View.GONE);
                            btn_signin.setText("客户签入");
                            btn_signin.setBackgroundColor(Color.parseColor("#fe4543"));
                            vip_.setText("");
                        } else if (return_code == 0) {
                            Toast.makeText(MakeSureTheOrderActivity.this, "签退失败,您需要手动试试", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
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
        mContext = MakeSureTheOrderActivity.this;
        userInfo = GetUserInfo.initGetUserInfo(this);
        listObj = (ArrayList<ShopMessageBean>) getIntent().getSerializableExtra("listobj");
        //终端号
        terminalId = (String) AppSharePreferenceMgr.get(mContext, "terminalId", "");
        //pos商户号
        merchantId = (String) AppSharePreferenceMgr.get(mContext, "merchantId", "");
        userinfovip = new UserInfoVip();
        initView();
        initViewBtn();
        initAction();
    }

    private void initViewBtn() {
        btn_signin = (Button) findViewById(R.id.btn_Signin);
        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //1、这个地方需要一个开关按钮，用来签入活者是签退。
                //2、我们用signInt 来做区分 1是没有会员直接开启签到  2是有会员直接启用签退
                //3、signInt 这个默认值是1 默认开启的是签到
                if (signInt == 1) {
                    //终端号
                    if (terminalId != null && terminalId.length() > 0) {
                        //1、调用签入的接口 获取二维码
                        getqrcode();
                        //2、二维码获取成功打开签入的二维码
                        //3、如果没有获取成功就提示重新获取
                    }
                } else if (signInt == 2) {
                    setSignOutCustomer();
                }
            }
        });
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
        remark = (TextView) findViewById(R.id.remark);
        actionremark = (LinearLayout) findViewById(R.id.actionremark);
        writeoff = (LinearLayout) findViewById(R.id.writeoff);
        jiedaiyuan = (TextView) findViewById(R.id.jiedaiyuan);
        if (userInfo != null) {
            jiedaiyuan.setText(userInfo.getRealName());
        }

        zhekou = (TextView) findViewById(R.id.zhekou);
        userHeadImg = (CircleImageView) findViewById(R.id.userHeandImg);
        vip_ = (TextView) findViewById(R.id.vip_);
    }

    /**
     * 控件的点击事件
     */
    private void initAction() {
        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sure_submit_order();
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
        String user_id = userinfovip.getUser_id();
        if (user_id != null && user_id.length() > 0) {
            subMap.put("user_id", user_id);
        } else {
            //0为散客
            subMap.put("user_id", "0");
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
            submitShopBean.setId(listObj.get(i).getSp_id());
            submitShopBean.setNum(listObj.get(i).getSp_count());
            submitShopBean.setPrice(listObj.get(i).getSp_price());
            //商品规格
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

        if (zkou != null && !"0.0".equals(zkou)) {
            subMap.put("discount", zkou);
        }
        confirm_loading = LoadingUtils.getDailog(MakeSureTheOrderActivity.this, Color.RED, "提交订单中。。。。");
        confirm_loading.show();
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
        if (userinfovip != null) {
            Glide.with(MakeSureTheOrderActivity.this).load(userinfovip.getHead_imgurl()).into(signvipimg);
            signvipname.setText(userinfovip.getNickname());
        }

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
        TextView cancel = view.findViewById(R.id.ordersure_cancel);
        TextView confirm = view.findViewById(R.id.ordersure_confirm);
        sure_submit_order_dialog.setContentView(view);
        //使得点击对话框外部不消失对话框
        sure_submit_order_dialog.setCanceledOnTouchOutside(false);
        //设置对话框的大小
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sure_submit_order_dialog.dismiss();
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


    /**
     * 获取二维码
     *
     * @return
     */
    public void getqrcode() {
        //有问题
        try {
            String timestamp = System.currentTimeMillis() + "";
            String token = CreateToken.createToken(userInfo.getUuid(), timestamp, userInfo.getAccount());
            Map<String, String> codeMap = new HashMap<>();
            codeMap.put("organization_id", userInfo.getFansnamage_id());
            Log.i("URL", "organization_id=" + userInfo.getFansnamage_id());
            codeMap.put("store_id", userInfo.getOrganization_id());
            codeMap.put("device_num", terminalId);
            get_qrcode_loading = LoadingUtils.getDailog(MakeSureTheOrderActivity.this, Color.RED, "获取签入二维中...");
            get_qrcode_loading.show();
            NetUtils.netWorkByMethodPost(MakeSureTheOrderActivity.this, codeMap, IpConfig.URL_GETCODE, handler, 3);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 获取会员信息
     *
     * @return
     */
    public void getUserInfo() {
        String timestamp = System.currentTimeMillis() + "";
        String token = CreateToken.createToken(userInfo.getUuid(), timestamp, userInfo.getAccount());
        Map<String, String> codeMap = new HashMap<>();
        codeMap.put("device_num", terminalId);
        NetUtils.netWorkByMethodPost(MakeSureTheOrderActivity.this, codeMap, IpConfig.URL_GETUSERINFO, handler, 4);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OutSignCustomer.signOut(userInfo, MakeSureTheOrderActivity.this, terminalId, handler, 580);
    }
}
