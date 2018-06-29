package com.zerone_catering.avtivity.cashierpage;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
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
import com.zerone_catering.adapter.cart_list.MakeOrderDetialsCashierListItemAdapter;
import com.zerone_catering.adapter.expandlistview.MyExpandableListViewAdapter;
import com.zerone_catering.avtivity.BaseSet.BaseActvity;
import com.zerone_catering.avtivity.openorderpage.MakeSureMethod;
import com.zerone_catering.avtivity.openorderpage.OrderListActvity;
import com.zerone_catering.domain.UserInfo;
import com.zerone_catering.domain.UserInfoVip;
import com.zerone_catering.domain.order.SubmitDataBean;
import com.zerone_catering.domain.order.SubmitShopBean;
import com.zerone_catering.domain.payorderlistbean.OrderCashierListBean;
import com.zerone_catering.domain.refresh.RefreshBean;
import com.zerone_catering.domain.tablefinal.cashiertable.TableListInfoCashierFinal;
import com.zerone_catering.domain.twolist.CashierDetailsListBean;
import com.zerone_catering.utils.AppSharePreferenceMgr;
import com.zerone_catering.utils.GetUserInfo;
import com.zerone_catering.utils.LoadingUtils;
import com.zerone_catering.utils.NetUtils;
import com.zerone_catering.utils.NetworkUtil;
import com.zerone_catering.utils.OutSignCustomer;
import com.zerone_catering.view.CustomExpandableListView;
import com.zerone_catering.view.MyNumberPicker;
import com.zyao89.view.zloading.ZLoadingDialog;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
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
 * 这个是确认收银订单页面
 */
public class MakeSure_Cashier_Order_Activity extends BaseActvity implements NumberPicker.OnValueChangeListener, NumberPicker.Formatter, NumberPicker.OnScrollListener {

    //=========================定时器========================

    private Intent intent;
    private ArrayList<OrderCashierListBean> listObj;
    private RelativeLayout submitbtn;
    private List<SubmitShopBean> subbeanlist;
    private ZLoadingDialog loading_dailog;
    private ListView goodslist;
    private MakeOrderDetialsCashierListItemAdapter mAdapter;
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
    private UserInfo userinfo;
    private CustomExpandableListView expandableListView;
    private List<CashierDetailsListBean> mGroupList = null;
    private MyExpandableListViewAdapter myAdapter;
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
                            OutSignCustomer.signOut(userinfo, MakeSure_Cashier_Order_Activity.this, terminalId, handler, 6);
                            Intent intent = new Intent(MakeSure_Cashier_Order_Activity.this, OrderListActvity.class);
                            intent.putExtra("orderid", orderid);
                            startActivity(intent);
                            AppSharePreferenceMgr.put(MakeSure_Cashier_Order_Activity.this, "orderid", orderid);
                            MakeSure_Cashier_Order_Activity.this.finish();
                            EventBus.getDefault().post(new RefreshBean("清空购物车的类", ContantData.REFRESH_ONE));
                            if (sure_submit_order_dialog != null) {
                                sure_submit_order_dialog.dismiss();
                            }
                            MakeSure_Cashier_Order_Activity.this.removeALLActivity();
                        } else if (status == 0) {
                            //订单提交失败  提示用户失败的原因
                            Toast.makeText(MakeSure_Cashier_Order_Activity.this, "错误返回：" + jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(MakeSure_Cashier_Order_Activity.this, "获取图片二维码失败，请重新获取", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(MakeSure_Cashier_Order_Activity.this, qrcodeJson.toString(), Toast.LENGTH_SHORT).show();
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
                                Glide.with(MakeSure_Cashier_Order_Activity.this).load(userinfovip.getHead_imgurl()).into(userHeadImg);
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
                            Toast.makeText(MakeSure_Cashier_Order_Activity.this, "签退失败,请重试", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(MakeSure_Cashier_Order_Activity.this, "签退成功", Toast.LENGTH_SHORT).show();
                            userHeadImg.setVisibility(View.GONE);
                            btn_signin.setText("客户签入");
                            btn_signin.setBackgroundColor(Color.parseColor("#fe4543"));
                            vip_.setText("");
                        } else if (return_code == 0) {
                            Toast.makeText(MakeSure_Cashier_Order_Activity.this, "签退失败,您需要手动试试", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 50:
                    String json = (String) msg.obj;
                    try {
                        if (!MakeSure_Cashier_Order_Activity.this.isFinishing()) {
                            loading_dailog.show();
                        }
                        JSONObject jsonObject = new JSONObject(json);
                        int status = jsonObject.getInt("status");
                        if (status == 1) {
                            JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("goodsdata");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                CashierDetailsListBean cdlb = new CashierDetailsListBean();
                                cdlb.setOrdersn(jsonArray.getJSONObject(i).getString("ordersn"));
                                JSONArray goodsdata = jsonArray.getJSONObject(i).getJSONArray("goodsdata");
                                List<CashierDetailsListBean.GoodsdataBean> zlist = new ArrayList<>();
                                for (int j = 0; j < goodsdata.length(); j++) {
                                    CashierDetailsListBean.GoodsdataBean gb = new CashierDetailsListBean.GoodsdataBean();
                                    gb.setGoods_id(goodsdata.getJSONObject(j).getInt("goods_id"));
                                    gb.setId(goodsdata.getJSONObject(j).getInt("id"));
                                    gb.setPrice(goodsdata.getJSONObject(j).getString("price"));
                                    gb.setThumb(goodsdata.getJSONObject(j).getString("thumb"));
                                    gb.setTitle(goodsdata.getJSONObject(j).getString("title"));
                                    gb.setTotal(goodsdata.getJSONObject(j).getInt("total"));
                                    zlist.add(gb);
                                }
                                cdlb.setGoodsdata(zlist);
                                mGroupList.add(cdlb);
                                sureOrderMoney.setText("￥" + jsonObject.getJSONObject("data").getString("order_price"));
                                subMoney.setText("￥" + jsonObject.getJSONObject("data").getString("order_price"));
                            }
                            setExpandListView();
                        } else if (status == 0) {

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {

                    }
                    break;
            }
        }
    };
    private TableListInfoCashierFinal tabinfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_makesurecashierorder);
        intent = getIntent();
        mContext = MakeSure_Cashier_Order_Activity.this;
        userinfo = GetUserInfo.initGetUserInfo(this);
        listObj = (ArrayList<OrderCashierListBean>) getIntent().getSerializableExtra("list");
        tabinfo = (TableListInfoCashierFinal) intent.getSerializableExtra("tableinfo");
        //终端号
        terminalId = (String) AppSharePreferenceMgr.get(mContext, "terminalId", "");
        //pos商户号
        merchantId = (String) AppSharePreferenceMgr.get(mContext, "merchantId", "");
        mGroupList = new ArrayList<>();
        initView();
        initData();
        initViewBtn();
        initAction();
    }

    /**
     * 获取选中的订单的订单列表
     */
    private void initData() {
        String order_array = getOrder_Array();
        if (userinfo == null) {
            return;
        }
        String timestamp = System.currentTimeMillis() + "";
        String token = CreateToken.createToken(userinfo.getUuid(), timestamp, userinfo.getAccount());
        Map<String, String> tMap = new HashMap<String, String>();
        tMap.put("account_id", userinfo.getAccount_id());
        tMap.put("timestamp", timestamp);
        tMap.put("organization_id", userinfo.getOrganization_id());
        tMap.put("token", token);
        tMap.put("table_id", tabinfo.getId() + "");
        tMap.put("order_array", order_array);
        if (!NetworkUtil.isNetworkAvailable(mContext)) {
            Toast.makeText(mContext, "网络不可用，请检查", Toast.LENGTH_SHORT).show();
            return;
        }
        loading_dailog = LoadingUtils.getDailog(mContext, Color.RED, "获取订单中...");
        if (!MakeSure_Cashier_Order_Activity.this.isFinishing()) {
            loading_dailog.show();
        }
        NetUtils.netWorkByMethodPost(mContext, tMap, IpConfig.URL_ORDER_GOODS_LIST, handler, 50);
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
            dSOMoney += Double.parseDouble(listObj.get(i).getOrder_price());
        }
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        zhekouValue = (TextView) findViewById(R.id.zhekouValue);
        goodslist = (ListView) findViewById(R.id.goodslist);
        subMoney = (TextView) findViewById(R.id.subMoney);
        sureOrderMoney = (TextView) findViewById(R.id.sureOrderMoney);
        submitbtn = (RelativeLayout) findViewById(R.id.submitbtn);
        relative_back = (RelativeLayout) findViewById(R.id.relative_back);
        remark = (TextView) findViewById(R.id.remark);
        actionremark = (LinearLayout) findViewById(R.id.actionremark);
        writeoff = (LinearLayout) findViewById(R.id.writeoff);
        jiedaiyuan = (TextView) findViewById(R.id.jiedaiyuan);
        if (userinfo != null) {
            jiedaiyuan.setText(userinfo.getRealName());
        }
        zhekou = (TextView) findViewById(R.id.zhekou);
        userHeadImg = (CircleImageView) findViewById(R.id.userHeandImg);
        vip_ = (TextView) findViewById(R.id.vip_);
    }

    public void setExpandListView() {
        //===========================================
        expandableListView = (CustomExpandableListView) findViewById(R.id.expandableListView);
        expandableListView.setGroupIndicator(null);

        // 为ExpandableListView设置Adapter
        myAdapter = new MyExpandableListViewAdapter(this, mGroupList);
        expandableListView.setAdapter(myAdapter);
        setListViewHeight(expandableListView);
        int groupCount = expandableListView.getCount();
        for (int i = 0; i < groupCount; i++) {
            expandableListView.expandGroup(i);
        }

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
                MakeSure_Cashier_Order_Activity.this.finish();
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
                MakeSure_Cashier_Order_Activity.this.finish();
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
        if (userinfo == null) {
            return;
        }
        Log.i("URL", "我没有被调用吗？？？？");
        String tableid = intent.getStringExtra("tableid");
        subbeanlist = new ArrayList<>();
        String timestamp = System.currentTimeMillis() + "";
        String token = CreateToken.createToken(userinfo.getUuid(), timestamp, userinfo.getAccount());
        Map<String, String> subMap = new HashMap<String, String>();
        if (userinfo.getOrganization_id() != null) {
            subMap.put("organization_id", userinfo.getOrganization_id());
        }
        if (userinfo.getAccount_id() != null) {
            subMap.put("account_id", userinfo.getAccount_id());
        }
        if (token != null) {
            subMap.put("token", token);
        }

        if (timestamp != null) {
            subMap.put("timestamp", timestamp);
        }
        String remarks = remark.getText().toString();
        if (!TextUtils.isEmpty(remarks)) {
            subMap.put("remarks", remarks);
        }
        for (int i = 0; i < listObj.size(); i++) {
            //提交订单时的需要处理
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
            subMap.put("goods_list", goodsdata);
        }
        if (tableid != null) {
            subMap.put("table_id", tableid);
        }

//        if (zkou != null && !"0.0".equals(zkou)) {
//            subMap.put("discount", zkou);
//        }
//        confirm_loading = LoadingUtils.getDailog(mContext, Color.RED, "提交订单中。。。。");
//        confirm_loading.show();
//        NetUtils.netWorkByMethodPost(mContext, subMap, IpConfig.URL_SUBMIT, handler, 0);
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
        MakeSureMethod.setNumberPickerDividerColor(numberPicker1, MakeSure_Cashier_Order_Activity.this);
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
        Glide.with(MakeSure_Cashier_Order_Activity.this).load(url).into(signin_img);
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
            Glide.with(MakeSure_Cashier_Order_Activity.this).load(userinfovip.getHead_imgurl()).into(signvipimg);
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
                OutSignCustomer.signOut(userinfo, MakeSure_Cashier_Order_Activity.this, terminalId, handler, 5);
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
//                gotoSubmit();
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
//        //有问题
//        try {
//            String timestamp = System.currentTimeMillis() + "";
//            String token = CreateToken.createToken(userInfo.getUuid(), timestamp, userInfo.getAccount());
//            Map<String, String> codeMap = new HashMap<>();
//            codeMap.put("organization_id", userInfo.getFansnamage_id());
//            Log.i("URL", "organization_id=" + userInfo.getFansnamage_id());
//            codeMap.put("store_id", userInfo.getOrganization_id());
//            codeMap.put("device_num", terminalId);
//            get_qrcode_loading = LoadingUtils.getDailog(MakeSureTheOrderActivity.this, Color.RED, "获取签入二维中...");
//            get_qrcode_loading.show();
//            NetUtils.netWorkByMethodPost(MakeSureTheOrderActivity.this, codeMap, IpConfig.URL_GETCODE, handler, 3);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }

    /**
     * 获取会员信息
     *
     * @return
     */
    public void getUserInfo() {
//        String timestamp = System.currentTimeMillis() + "";
//        String token = CreateToken.createToken(userInfo.getUuid(), timestamp, userInfo.getAccount());
//        Map<String, String> codeMap = new HashMap<>();
//        codeMap.put("device_num", terminalId);
//        NetUtils.netWorkByMethodPost(MakeSureTheOrderActivity.this, codeMap, IpConfig.URL_GETUSERINFO, handler, 4);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OutSignCustomer.signOut(userinfo, MakeSure_Cashier_Order_Activity.this, terminalId, handler, 580);
    }

    public String getOrder_Array() {
        String array = "";
        for (int i = 0; i < listObj.size(); i++) {
            if (i == 0) {
                array += listObj.get(i).getId() + "";
            } else {
                array += "," + listObj.get(i).getId();
            }
        }
        return array;
    }


    private void setListViewHeight(ExpandableListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        int totalHeight = 0;
        int count = listAdapter.getCount();
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}
