package com.zerone_catering.avtivity.details;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zerone_catering.Base64AndMD5.CreateToken;
import com.zerone_catering.Contants.IpConfig;
import com.zerone_catering.R;
import com.zerone_catering.adapter.Print_Choose_Item_Adapter;
import com.zerone_catering.adapter.details.Cancel_Single_Order_ListItemAdapter;
import com.zerone_catering.avtivity.BaseSet.BaseActvity;
import com.zerone_catering.avtivity.manageorderpage.printlist.PrintResponseActivity;
import com.zerone_catering.domain.PrinterMachine;
import com.zerone_catering.domain.UserInfo;
import com.zerone_catering.domain.colse.CloseActivity;
import com.zerone_catering.domain.order.Order_Cashier_Details_Bean;
import com.zerone_catering.domain.refresh.RefreshBean;
import com.zerone_catering.print.PrintBean;
import com.zerone_catering.print.PrintItem;
import com.zerone_catering.utils.AppSharePreferenceMgr;
import com.zerone_catering.utils.DoubleUtils;
import com.zerone_catering.utils.GetUserInfo;
import com.zerone_catering.utils.JavaUtilsNormal;
import com.zerone_catering.utils.LoadingUtils;
import com.zerone_catering.utils.NetUtils;
import com.zerone_catering.utils.NetworkUtil;
import com.zerone_catering.utils.UtilsTime;
import com.zerone_catering.utils.printutils.PrintUtils;
import com.zerone_catering.utils.view.ListViewSetHightUtils;
import com.zyao89.view.zloading.ZLoadingDialog;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by on 2018/4/2 0002 19 32.
 * Author  LiuXingWen
 * 这个是打印机的详情
 */
public class Order_Details_Print_Activity extends BaseActvity {
    List<Order_Cashier_Details_Bean> list;
    private ListView listView;
    private Order_Details_Print_Activity mContext;
    private Intent intent;
    private TextView ordermoney;
    private RelativeLayout subSurePay;
    private String money;
    private String discount_mone;
    private TextView listOrderMoney;
    //下单时间
    private TextView ordertime;
    //消费者
    private TextView customer;
    //订单编号
    private TextView ordersn;
    //接待员
    private TextView receptionist;
    //备注
    private TextView remarks;
    private Dialog dialog;
    private RelativeLayout relative_back;
    private LinearLayout refresh_data;
    private Dialog dpay;
    private List<PrinterMachine> printList;
    private Cancel_Single_Order_ListItemAdapter codlia;
    private ZLoadingDialog loading_dailog;
    private UserInfo userInfo;
    private TextView discount;
    private Dialog corder_dialog;
    private EditText goodsnumber;
    private Print_Choose_Item_Adapter pchooseAdapter;
    private int printerCode;
    private TextView printName;
    private String order_id;
    private PrintBean printBean;
    private List<PrintItem> printItemList;
    private TextView confirm;
    private TextView tableandroom;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    try {
                        printBean = new PrintBean();
                        printItemList = new ArrayList<>();
                        String detailsJson = (String) msg.obj;
                        Log.i("URL", "detailsJson=" + detailsJson);
                        JSONObject jsonObject = new JSONObject(detailsJson);
                        int status = jsonObject.getInt("status");
                        if (status == 1) {
                            list.clear();
                            //封装打印类 的数据
                            long aLong = jsonObject.getJSONObject("data").getJSONObject("orderdata").getLong("created_at") * 1000;
                            //下单时间
                            Date d = new Date(aLong);
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            printBean.setCreateTime(sdf.format(d));
                            printBean.setOrdersn(jsonObject.getJSONObject("data").getJSONObject("orderdata").getString("ordersn"));
                            int payStatus = jsonObject.getJSONObject("data").getJSONObject("orderdata").getInt("status");
                            if (payStatus == -1) {
                                printBean.setOrderTuype("取消状态");
                            } else if (payStatus == 0) {
                                printBean.setOrderTuype("待付款");
                            } else if (payStatus == 1) {
                                printBean.setOrderTuype("已付款");
                            }

                            printBean.setPmoney(jsonObject.getJSONObject("data").getJSONObject("orderdata").getString("order_price"));
                            String table_name = jsonObject.getJSONObject("data").getJSONObject("orderdata").getString("table_name");
                            String room_name = jsonObject.getJSONObject("data").getJSONObject("orderdata").getString("room_name");
                            printBean.setRoomAndTable(room_name + "：" + table_name);
                            tableandroom.setText(room_name + "：" + table_name);
                            JSONArray jsonArra = jsonObject.getJSONObject("data").getJSONArray("ordergoods");
                            for (int i = 0; i < jsonArra.length(); i++) {
                                JSONObject Item = jsonArra.getJSONObject(i);
                                PrintItem printItem = new PrintItem();
                                printItem.setGcount(Item.getString("total"));
                                printItem.setGoodsname(Item.getString("title"));
                                printItem.setGprice(Item.getString("price"));
                                printItemList.add(printItem);
                            }
                            printBean.setPayment_price("");
                            printBean.setDiscount("");
                            printBean.setList(printItemList);
                            JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("ordergoods");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                Order_Cashier_Details_Bean ocdb = new Order_Cashier_Details_Bean();
                                ocdb.setGoods_id(object.getInt("goods_id"));
                                ocdb.setPrice(object.getString("price"));
                                ocdb.setThumb(object.getString("thumb"));
                                ocdb.setTitle(object.getString("title"));
                                ocdb.setTotal(object.getInt("total"));
                                ocdb.setGoods_ckeck(false);
                                list.add(ocdb);
                            }
                            ordertime.setText(UtilsTime.getTime(Long.parseLong(jsonObject.getJSONObject("data").getJSONObject("orderdata").getString("created_at"))));
                            ordersn.setText(jsonObject.getJSONObject("data").getJSONObject("orderdata").getString("ordersn"));
                            customer.setText(jsonObject.getJSONObject("data").getJSONObject("orderdata").getString("nickname"));
                            receptionist.setText(jsonObject.getJSONObject("data").getJSONObject("orderdata").getString("realname"));
                            String remark = jsonObject.getJSONObject("data").getJSONObject("orderdata").getString("remarks");
                            if ("null".equals(remark) && remark.length() > 0) {
                                remarks.setText("没有备注");
                                printBean.setRemark("");
                            } else {
                                remarks.setText(remark);
                                printBean.setRemark(remark);
                            }
                            String nmoney = jsonObject.getJSONObject("data").getJSONObject("orderdata").getString("order_price");
                            Log.i("URL", "nomney=" + nmoney);
                            listOrderMoney.setText("￥" + DoubleUtils.subMoney(nmoney));
                            codlia.notifyDataSetChanged();
                            printerCode = jsonObject.getJSONObject("data").getJSONObject("orderdata").getInt("re_printer");
                            if (printerCode > 0) {
                                //这个是已打印
                                printName.setText("催单");
                            } else if (printerCode == 0) {
                                //这个是未打印
                                printName.setText("打印订单");
                            }
                            ListViewSetHightUtils.setListViewHeightBasedOnChildren(listView);
                            EventBus.getDefault().post(new RefreshBean("刷新打印订单列表", 100));
                        } else if (status == 0) {
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (!Order_Details_Print_Activity.this.isFinishing()) {
                            loading_dailog.dismiss();
                        }
                    }
                    break;
                case 1:
                    int index = (int) msg.obj;
                    AppSharePreferenceMgr.put(Order_Details_Print_Activity.this, "position", index);
                    Order_Cashier_Details_Bean ocdb = list.get(index);
                    cancel_Order_Dialog(ocdb);
                    break;
                case 2:
                    String ycJson = (String) msg.obj;
                    int position = (int) AppSharePreferenceMgr.get(Order_Details_Print_Activity.this, "position", 0);
                    try {
                        JSONObject jsonObject = new JSONObject(ycJson);
                        int status = jsonObject.getInt("status");
                        if (status == 1) {
                            String trim = goodsnumber.getText().toString().trim();
                            String price = listOrderMoney.getText().toString().trim().substring(1);
                            double nprice = Double.parseDouble(price);

                            if ("".equals(trim)) {
                                nprice -= (Double.parseDouble(list.get(position).getPrice()) * list.get(position).getTotal());
                                list.remove(position);
                                printBean.getList().remove(position);
                                printBean.setPmoney(nprice + "");
                            } else {
                                int gNum = Integer.parseInt(trim);
                                int newNum = list.get(position).getTotal() - gNum;
                                nprice -= (Double.parseDouble(list.get(position).getPrice()) * gNum);
                                if (newNum == 0) {
                                    list.remove(position);
                                    printBean.getList().remove(position);
                                } else {
                                    list.get(position).setTotal(newNum);
                                    printBean.getList().get(position).setGcount(newNum + "");
                                }
                                printBean.setPmoney(nprice + "");
                            }
                            EventBus.getDefault().post(new RefreshBean("100", 100));
                            listOrderMoney.setText("￥" + DoubleUtils.subMoney(nprice + ""));
                            codlia.notifyDataSetChanged();
                            ListViewSetHightUtils.setListViewHeightBasedOnChildren(listView);
                        } else if (status == 0) {
                            Toast.makeText(Order_Details_Print_Activity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        AppSharePreferenceMgr.remove(Order_Details_Print_Activity.this, "position");
                        if (!Order_Details_Print_Activity.this.isFinishing()) {
                            loading_dailog.dismiss();
                            corder_dialog.dismiss();
                        }
                    }
                    break;
                case 3:
                    try {
                        printList.clear();
                        String printJson = (String) msg.obj;
                        JSONObject jsonObject = new JSONObject(printJson);
                        int status = jsonObject.getInt("status");
                        if (status == 1) {
                            JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("printer_list");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                PrinterMachine pm = new PrinterMachine();
                                String type = jsonArray.getJSONObject(i).getString("type");
                                if (printerCode > 0) {
                                    pm.setChblen(false);
                                } else if (printerCode == 0) {
                                    if ("1".equals(type)) {
                                        pm.setChblen(true);
                                    } else {
                                        pm.setChblen(false);
                                    }
                                }
                                pm.setType(type);
                                pm.setMaId(jsonArray.getJSONObject(i).getString("id"));
                                pm.setMaName(jsonArray.getJSONObject(i).getString("machine_name"));
                                //默认打印已联
                                pm.setPrintNum(1);
                                printList.add(pm);
                            }
                            checkMachine();
                        } else if (status == 0) {
                            Toast.makeText(Order_Details_Print_Activity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        if (loading_dailog != null) {
                            loading_dailog.dismiss();
                        }
                        subSurePay.setEnabled(true);
                    }
                    break;
                case 4:
                    String nmber = (String) AppSharePreferenceMgr.get(Order_Details_Print_Activity.this, "numberGroup", "1");
                    Boolean plean = (Boolean) AppSharePreferenceMgr.get(Order_Details_Print_Activity.this, "print", false);
                    String pjson = (String) msg.obj;
                    try {
                        JSONObject pjsonObject = new JSONObject(pjson);
                        int status = pjsonObject.getInt("status");
                        if (status == 1) {
                            Intent intent = new Intent(Order_Details_Print_Activity.this, PrintResponseActivity.class);
                            startActivity(intent);
                            EventBus.getDefault().post(new CloseActivity("print", 1000));
                            if (plean) {
                                if (printerCode == 0) {
                                    for (int i = 0; i < Integer.parseInt(nmber); i++) {
                                        PrintUtils.print(userInfo.getOrganization_name(), printBean, i);
                                    }
                                }
                            }
                            Order_Details_Print_Activity.this.finish();
                        } else if (status == 0) {
                            JSONArray data = pjsonObject.getJSONArray("data");
                            StringBuffer sb = new StringBuffer();
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject jsonObject = data.getJSONObject(i);
                                sb.append(jsonObject.getString("error_description") + "\r\n");
                            }
                            Toast.makeText(Order_Details_Print_Activity.this, sb.toString(), Toast.LENGTH_SHORT).show();
                        } else if (status == 2) {
                            Toast.makeText(Order_Details_Print_Activity.this, pjsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        if (!Order_Details_Print_Activity.this.isFinishing()) {
                            loading_dailog.dismiss();
                        }
                        if (dpay != null) {
                            dpay.dismiss();
                        }
                        confirm.setEnabled(true);
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderdetails_print_dfk);
        mContext = Order_Details_Print_Activity.this;
        list = new ArrayList<Order_Cashier_Details_Bean>();
        intent = getIntent();
        order_id = intent.getStringExtra("order_id");
        userInfo = GetUserInfo.initGetUserInfo(this);
        initView();
        initDataSet();
        intiAction();
    }

    /**
     * 测试数据
     */
    private void initDataSet() {

        getOrderDetailsInfo();
    }

    private void intiAction() {

        subSurePay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //这个地方要获取打印机列表数据 成功后在打开对话框选择
                gotoGetPrintList();
                subSurePay.setEnabled(false);
            }
        });
        relative_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Order_Details_Print_Activity.this.finish();
            }
        });

        refresh_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOrderDetailsInfo();
            }
        });


    }

    /**
     * 获取打印机列表
     */
    private void gotoGetPrintList() {
        if (userInfo == null) {
            return;
        }
        String timestamp = System.currentTimeMillis() + "";
        String token = CreateToken.createToken(userInfo.getUuid(), timestamp, userInfo.getAccount());
        Map<String, String> tMap = new HashMap<String, String>();
        tMap.put("account_id", userInfo.getAccount_id());
        tMap.put("timestamp", timestamp);
        tMap.put("organization_id", userInfo.getOrganization_id());
        tMap.put("token", token);
        if (!NetworkUtil.isNetworkAvailable(mContext)) {
            Toast.makeText(mContext, "网络不可用，请检查", Toast.LENGTH_SHORT).show();
            return;
        }
        loading_dailog = LoadingUtils.getDailog(mContext, Color.RED, "获取打印机...");
        if (!Order_Details_Print_Activity.this.isFinishing()) {
            loading_dailog.show();
        }
        NetUtils.netWorkByMethodPost(mContext, tMap, IpConfig.URL_PRINTER_LIST, handler, 3);
    }
    /**
     * 初始化view
     */
    private void initView() {
        printList = new ArrayList<PrinterMachine>();
        tableandroom = (TextView) findViewById(R.id.tableandroom);
        printName = (TextView) findViewById(R.id.printName);
        listOrderMoney = (TextView) findViewById(R.id.listOrderMoney);
        //确认订单按钮
        subSurePay = (RelativeLayout) findViewById(R.id.subSurePay);
        //确认订单的按钮 显示的价格
        ordermoney = (TextView) findViewById(R.id.ordermoney);
        //======================
        ordertime = (TextView) findViewById(R.id.ordertime);
        customer = (TextView) findViewById(R.id.xiaofeizhe);
        ordersn = (TextView) findViewById(R.id.ordersn);
        receptionist = (TextView) findViewById(R.id.jiedaiyuan);
        remarks = (TextView) findViewById(R.id.beizhu);
        relative_back = (RelativeLayout) findViewById(R.id.relative_back);
        refresh_data = (LinearLayout) findViewById(R.id.refresh_data);
        discount = (TextView) findViewById(R.id.discount);
        listView = (ListView) findViewById(R.id.goodslist);
        codlia = new Cancel_Single_Order_ListItemAdapter(this, list, handler);
        listView.setAdapter(codlia);
        ListViewSetHightUtils.setListViewHeightBasedOnChildren(listView);
    }
    /**
     * 选择需要打印的打印机
     */
    private void checkMachine() {
        dpay = new Dialog(this, R.style.NormalDialogStyle);
        View view = View.inflate(this, R.layout.activity_dialog_check_p_view, null);
        RecyclerView TheprinterList = view.findViewById(R.id.TheprinterList);
        pchooseAdapter = new Print_Choose_Item_Adapter(printList, Order_Details_Print_Activity.this);
        TheprinterList.setLayoutManager(new LinearLayoutManager(Order_Details_Print_Activity.this));
        TheprinterList.setAdapter(pchooseAdapter);
        pchooseAdapter.setOnItemClickListener(new Print_Choose_Item_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String type = printList.get(position).getType();
                if ("1".equals(type)) {
                    if (printerCode > 0) {
                        printList.get(position).setChblen(!printList.get(position).isChblen());
                        pchooseAdapter.notifyDataSetChanged();
                    } else if (printerCode == 0) {
                    }
                } else {
                    printList.get(position).setChblen(!printList.get(position).isChblen());
                    pchooseAdapter.notifyDataSetChanged();
                }
            }
        });
        TextView cancel = view.findViewById(R.id.cancel);
        confirm = view.findViewById(R.id.confirm);
        dpay.setContentView(view);
        //使得点击对话框外部不消失对话框
        dpay.setCanceledOnTouchOutside(false);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dpay.dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //连接打印机服务器连接
                confirm.setEnabled(false);
                doPrint();
            }
        });
        dpay.show();
    }

    private void doPrint() {
        if (userInfo == null) {
            confirm.setEnabled(true);
            return;
        }
        String json = getPrintJson();
        Log.i("URL", "mpinfo=" + json);
        String timestamp = System.currentTimeMillis() + "";
        String token = CreateToken.createToken(userInfo.getUuid(), timestamp, userInfo.getAccount());
        Map<String, String> tMap = new HashMap<String, String>();
        tMap.put("account_id", userInfo.getAccount_id());
        tMap.put("timestamp", timestamp);
        tMap.put("organization_id", userInfo.getOrganization_id());
        tMap.put("token", token);
        tMap.put("order_id", order_id);
        Log.i("URL", "json=" + json);
        if ("{}".equals(json)) {
            Toast.makeText(Order_Details_Print_Activity.this, "请选择打印机", Toast.LENGTH_SHORT).show();
            confirm.setEnabled(true);
            return;
        }
        tMap.put("printer_array", json);
        if (!NetworkUtil.isNetworkAvailable(mContext)) {
            Toast.makeText(mContext, "网络不可用，请检查", Toast.LENGTH_SHORT).show();
            confirm.setEnabled(true);
            return;
        }
        loading_dailog = LoadingUtils.getDailog(mContext, Color.RED, "打印中请稍后...");
        if (!Order_Details_Print_Activity.this.isFinishing()) {
            loading_dailog.show();
        }
        if (printerCode > 0) {
            NetUtils.netWorkByMethodPost(mContext, tMap, IpConfig.URL_ORDER_PRINTER_AGAIN, handler, 4);
        } else {
            NetUtils.netWorkByMethodPost(mContext, tMap, IpConfig.URL_ORDER_PRINTER, handler, 4);
        }
    }
    /**
     * 自定义对话框
     */
    private void outDialog() {
        dialog = new Dialog(this, R.style.NormalDialogStyle);
        View view = View.inflate(this, R.layout.activity_dialog_qx_order_view, null);
        TextView cancel = view.findViewById(R.id.cancel);
        TextView confirm = view.findViewById(R.id.confirm);
        dialog.setContentView(view);
        //使得点击对话框外部不消失对话框
        dialog.setCanceledOnTouchOutside(false);
        //设置对话框的大小
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                subSurePay.setEnabled(true);
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Order_Details_Print_Activity.this.finish();
            }
        });
        dialog.show();
    }

    /**
     * 获取订单详情
     */
    public void getOrderDetailsInfo() {
        if (userInfo == null) {
            return;
        }
        String timestamp = System.currentTimeMillis() + "";
        String token = CreateToken.createToken(userInfo.getUuid(), timestamp, userInfo.getAccount());
        Map<String, String> tMap = new HashMap<String, String>();
        tMap.put("account_id", userInfo.getAccount_id());
        tMap.put("timestamp", timestamp);
        tMap.put("organization_id", userInfo.getOrganization_id());
        tMap.put("token", token);
        tMap.put("order_id", order_id + "");
        if (!NetworkUtil.isNetworkAvailable(mContext)) {
            Toast.makeText(mContext, "网络不可用，请检查", Toast.LENGTH_SHORT).show();
            return;
        }
        loading_dailog = LoadingUtils.getDailog(mContext, Color.RED, "获取详情中...");
        if (!Order_Details_Print_Activity.this.isFinishing()) {
            loading_dailog.show();
        }
        NetUtils.netWorkByMethodPost(mContext, tMap, IpConfig.URL_ORDER_DETAILS, handler, 0);
    }

    /**
     * 移除所选中的商品
     */
    public void cancelGoods() {
        Iterator<Order_Cashier_Details_Bean> it = list.iterator();
        while (it.hasNext()) {
            Order_Cashier_Details_Bean cc = it.next();
            if (cc.getGoods_ckeck()) {
                it.remove();
            }
        }
        codlia.notifyDataSetChanged();
        ListViewSetHightUtils.setListViewHeightBasedOnChildren(listView);
    }

    /**
     * 移除订单
     */
    private void cancel_Order_Dialog(final Order_Cashier_Details_Bean ocdb) {
        corder_dialog = new Dialog(this, R.style.NormalDialogStyle);
        View view = View.inflate(this, R.layout.activity_dialog_delete_order_view, null);
        TextView title = view.findViewById(R.id.title);
        TextView cancel = view.findViewById(R.id.cancel);
        TextView confirm = view.findViewById(R.id.confirm);
        goodsnumber = view.findViewById(R.id.goodsnumber);
        corder_dialog.setContentView(view);
        //使得点击对话框外部不消失对话框
        corder_dialog.setCanceledOnTouchOutside(false);
        //设置对话框的大小
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                corder_dialog.dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //移除接口
                deleteIntoService(ocdb);
            }
        });
        corder_dialog.show();
    }

    public void deleteIntoService(Order_Cashier_Details_Bean ocdb) {
        if (userInfo == null) {
            return;
        }
        String number = goodsnumber.getText().toString().trim();
        String timestamp = System.currentTimeMillis() + "";
        String token = CreateToken.createToken(userInfo.getUuid(), timestamp, userInfo.getAccount());
        Map<String, String> tMap = new HashMap<String, String>();
        tMap.put("account_id", userInfo.getAccount_id());
        tMap.put("timestamp", timestamp);
        tMap.put("organization_id", userInfo.getOrganization_id());
        tMap.put("token", token);
        tMap.put("order_id", order_id + "");
        tMap.put("goods_id", ocdb.getGoods_id() + "");
        Log.i("URL", "number=" + number);
        if (JavaUtilsNormal.isInteger(number) && number.length() > 0) {
            tMap.put("goods_num", number);
        }
        if (!NetworkUtil.isNetworkAvailable(mContext)) {
            Toast.makeText(mContext, "网络不可用，请检查", Toast.LENGTH_SHORT).show();
            return;
        }
        loading_dailog = LoadingUtils.getDailog(mContext, Color.RED, "删除中...");
        if (!Order_Details_Print_Activity.this.isFinishing()) {
            loading_dailog.show();
        }
        NetUtils.netWorkByMethodPost(mContext, tMap, IpConfig.URL_CANCEL_GOODS, handler, 2);
    }


    /**
     * @return
     */
    public String getPrintJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            for (int i = 0; i < printList.size(); i++) {
                if (printList.get(i).isChblen()) {
                    jsonObject.put(printList.get(i).getMaId() + "", printList.get(i).getPrintNum());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public String getPrintM() {
        JSONObject jsonObject = new JSONObject();
        String pminfo = "";
        for (int i = 0; i < printList.size(); i++) {
            if (printList.get(i).isChblen()) {
                if (i == 0) {
                    pminfo += printList.get(i).getMaId();
                } else if ("".equals(pminfo)) {
                    pminfo += printList.get(i).getMaId();
                } else {
                    pminfo += "," + printList.get(i).getMaId();
                }
            }
        }
        return pminfo;
    }
}
