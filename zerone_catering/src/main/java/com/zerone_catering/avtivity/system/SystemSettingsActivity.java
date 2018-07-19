package com.zerone_catering.avtivity.system;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.githang.statusbar.StatusBarCompat;
import com.zerone_catering.DB.impl.UserInfoImpl;
import com.zerone_catering.R;
import com.zerone_catering.avtivity.BaseSet.BaseActvity;
import com.zerone_catering.domain.SignCompleteStatus;
import com.zerone_catering.domain.UserInfo;
import com.zerone_catering.utils.AppSharePreferenceMgr;
import com.zerone_catering.utils.IsIntentExite;
import com.zerone_catering.utils.UtilsTime;
import com.zyao89.view.zloading.ZLoadingDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by on 2018/3/30 0030 13 54.
 * Author  LiuXingWen
 * 设置页面
 */

public class SystemSettingsActivity extends BaseActvity {

    private CheckBox system_login_rember_account;
    private CheckBox system_kaidan;
    private CheckBox system_fkjkc;
    private CheckBox system_xdjkc;
    private ZLoadingDialog loading_dailog;
    private SystemSettingsActivity mContext;
    private UserInfo userInfo;
    private ImageView system_back;
    private Button systemout;
    private boolean remberChecked;
    private LinearLayout layout_back;
    private TextView storename;
    private TextView posid;
    private LinearLayout setPrintData;
    private CheckBox first;
    private CheckBox second;
    private CheckBox triple;
    private CheckBox fourth;
    private EditText otherGroup;
    private TextView group;
    private LinearLayout sign;
    private long firsttime;
    private TextView signTime;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    String kaidanJson = (String) msg.obj;
                    Log.i("URL", "kaidanJson=" + kaidanJson);
                    loading_dailog.dismiss();
                    try {
                        JSONObject jfk = new JSONObject(kaidanJson);
                        int status = jfk.getInt("status");
                        if (status == 1) {
                            JSONObject data = jfk.getJSONObject("data");
                            if ("1".equals(data.getString("vfg_value"))) {
                                system_kaidan.setChecked(true);
                            } else if ("2".equals(data.getString("vfg_value"))) {
                                system_kaidan.setChecked(false);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;

                case 2:
                    String fkJson = (String) msg.obj;
                    Log.i("URL", "" + fkJson);
                    loading_dailog.dismiss();
                    try {
                        JSONObject jfk = new JSONObject(fkJson);
                        int status = jfk.getInt("status");
                        if (status == 1) {
                            JSONObject data = jfk.getJSONObject("data");
                            if ("1".equals(data.getString("vfg_value"))) {
                                system_fkjkc.setChecked(true);
                                system_xdjkc.setChecked(false);
                            } else if ("2".equals(data.getString("vfg_value"))) {
                                system_fkjkc.setChecked(false);
                                system_xdjkc.setChecked(true);
                            }
                        } else {
                            system_fkjkc.setChecked(system_fkjkc.isChecked());
                            Toast.makeText(SystemSettingsActivity.this, jfk.getString("msg"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    String xdJson = (String) msg.obj;
                    loading_dailog.dismiss();
                    try {
                        JSONObject jfk = new JSONObject(xdJson);
                        int status = jfk.getInt("status");
                        if (status == 1) {
                            JSONObject data = jfk.getJSONObject("data");
                            if ("1".equals(data.getString("vfg_value"))) {
                                system_fkjkc.setChecked(true);
                                system_xdjkc.setChecked(false);
                            } else if ("2".equals(data.getString("vfg_value"))) {
                                system_fkjkc.setChecked(false);
                                system_xdjkc.setChecked(true);
                            }
                        } else {
                            system_xdjkc.setChecked(system_xdjkc.isChecked());
                            Toast.makeText(SystemSettingsActivity.this, jfk.getString("msg"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;
                case 4:
                    String dJson = (String) msg.obj;
                    loading_dailog.dismiss();
                    try {
                        JSONObject jsonObject = new JSONObject(dJson);
                        Log.i("URL", "" + jsonObject);
                        int status = jsonObject.getInt("status");
                        if (status == 0) {
                            Toast.makeText(mContext, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        } else if (status == 1) {
                            JSONObject data1 = jsonObject.getJSONObject("data");
                            JSONArray data = data1.getJSONArray("cfglist");
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject jbe = data.getJSONObject(i);
                                if ("allow_zero_stock".equals(jbe.getString("cfg_name"))) {
                                    //开启/关闭零库存开单接口
                                    String cfg_value = jbe.getString("cfg_value");
                                    if ("1".equals(cfg_value)) {
                                        //开启0库存开单   值为  1
                                        system_kaidan.setChecked(true);
                                    } else if ("2".equals(cfg_value)) {
                                        //关闭0库存开单  值为  2
                                        system_kaidan.setChecked(false);
                                    }
                                } else if ("change_stock_role".equals(jbe.getString("cfg_name"))) {
                                    //下单减库存
                                    String cfg_value = jbe.getString("cfg_value");
                                    if ("1".equals(cfg_value)) {
                                        //   //付款后减库存   值为  1
                                        system_fkjkc.setChecked(true);
                                        system_xdjkc.setChecked(false);
                                    } else if ("2".equals(cfg_value)) {
                                        //下单后减库存  值为  2
                                        system_xdjkc.setChecked(true);
                                        system_fkjkc.setChecked(false);
                                    }
                                }
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 10:
                    String gnum = (String) msg.obj;
                    group.setText(gnum + "联");
                    break;
                case 11:
                    SignCompleteStatus signCompleteStatus = (SignCompleteStatus) msg.obj;
//                    String datePoor = UtilsTime.getDatePoor(firsttime, secondtime);
//                    Toast.makeText(SystemSettingsActivity.this,datePoor,Toast.LENGTH_SHORT).show();
                    try {
                        int resultCode = signCompleteStatus.getResultCode();
//                        if (resultCode == 0) {
//                            //签到成功
//                            Log.i("URL", "resultCode=" + resultCode);
//                        } else if (resultCode == 1) {
//                            //签到失败  Log.i("URL","resultCode="+resultCode);
//                        }
                        setMsgDialog(signCompleteStatus);
//                        setPrintDataDialog();
                        if (firsttime == 0l) {
                            signTime.setText("您还没有签到");
                        } else {
                            signTime.setText("上次签到时间为：" + UtilsTime.setTime(firsttime));
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 511:
                    Toast.makeText(SystemSettingsActivity.this, "网络超时，请重试", Toast.LENGTH_SHORT).show();
                    loading_dailog.dismiss();
                    break;
            }
        }
    };
    private long secondtime;
    private CheckBox system_speak;
    private CheckBox prch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_settings);
        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#ffffff"));
        //注册广播接受器
        EventBus.getDefault().register(this);
        mContext = SystemSettingsActivity.this;
        remberChecked = (boolean) AppSharePreferenceMgr.get(mContext, "remberChecked", false);
        initGetUserInfo();
        initView();
        aciton();
//        initCheckBoxStates();
//        checkBoxAction();
    }

    private void aciton() {
        layout_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SystemSettingsActivity.this.finish();
            }
        });
        systemout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog();
            }
        });

        /**
         * 启动打印机设置页面
         */
        setPrintData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPrintDataDialog();
            }
        });
        //签到
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initPosInfo();
            }
        });
    }



    /**
     * 获取用户信息
     */
    private void initGetUserInfo() {
        UserInfoImpl userInfoImpl = new UserInfoImpl(mContext);
        try {
            userInfo = userInfoImpl.getUserInfo("10");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * view的初始
     */
    private void initView() {
        String nmber = (String) AppSharePreferenceMgr.get(SystemSettingsActivity.this, "numberGroup", "1");
        String terminalId = (String) AppSharePreferenceMgr.get(mContext, "terminalId", "");
        group = (TextView) findViewById(R.id.group);
        group.setText(nmber + "联");
        layout_back = (LinearLayout) findViewById(R.id.layout_back);
        storename = (TextView) findViewById(R.id.storename);
        if (userInfo != null) {
            storename.setText(userInfo.getOrganization_name());
        }
        posid = (TextView) findViewById(R.id.posid);
        posid.setText(terminalId);
        system_login_rember_account = (CheckBox) findViewById(R.id.system_login_rember_account);
        system_login_rember_account.setChecked(remberChecked);
        system_kaidan = (CheckBox) findViewById(R.id.system_kaidan);
        system_fkjkc = (CheckBox) findViewById(R.id.system_fkjkc);
        system_xdjkc = (CheckBox) findViewById(R.id.system_xdjkc);
        system_back = (ImageView) findViewById(R.id.system_back);
        systemout = (Button) findViewById(R.id.systemout);
        setPrintData = (LinearLayout) findViewById(R.id.set_print_number);
        sign = (LinearLayout) findViewById(R.id.sign);
        signTime = (TextView) findViewById(R.id.signTime);
        Long firsttime = (long) AppSharePreferenceMgr.get(SystemSettingsActivity.this, "firsttime", 0l);
        if (firsttime == 0l) {
            signTime.setText("您还没有签到");
        } else {
            signTime.setText("上次签到时间为：" + UtilsTime.setTime(firsttime));
        }
        system_speak = (CheckBox) findViewById(R.id.system_speak);
        Boolean speak = (Boolean) AppSharePreferenceMgr.get(SystemSettingsActivity.this, "speak", true);
        if (speak != null) {
            system_speak.setChecked(speak);
        }
        system_speak.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.i("URL", isChecked + "");
                system_speak.setChecked(isChecked);
                AppSharePreferenceMgr.put(SystemSettingsActivity.this, "speak", isChecked);
            }
        });

        Boolean print = (Boolean) AppSharePreferenceMgr.get(SystemSettingsActivity.this, "print", false);

        prch = (CheckBox) findViewById(R.id.system_print);
        prch.setChecked(print);
        prch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                prch.setChecked(isChecked);
                AppSharePreferenceMgr.put(SystemSettingsActivity.this, "print", isChecked);
            }
        });

    }

    /**
     * 点击checkbox的提交信息到服务器
     */
    private void checkBoxAction() {

        system_login_rember_account.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                AppSharePreferenceMgr.put(mContext, "remberChecked", isChecked);
            }
        });

    }

    /**
     * 自定义对话框
     */
    private void customDialog() {
        //终端号
        final String terminalId = (String) AppSharePreferenceMgr.get(SystemSettingsActivity.this, "terminalId", "");
        final Dialog dialog = new Dialog(this, R.style.NormalDialogStyle);
        View view = View.inflate(this, R.layout.activity_dialog_out_view, null);
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
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SystemSettingsActivity.this.removeALLActivity();
                System.exit(0);
            }
        });
        dialog.show();
    }

    /**
     * 打印机联数设置
     */
    private void setPrintDataDialog() {
        //终端号
        final Dialog dialog = new Dialog(this, R.style.NormalDialogStyle);
        View view = View.inflate(this, R.layout.activity_system_set_print_data, null);
        TextView cancel = view.findViewById(R.id.dbtn);
        TextView confirm = view.findViewById(R.id.sbtn);
        first = view.findViewById(R.id.the_first_group);
        second = view.findViewById(R.id.the_second_group);
        triple = view.findViewById(R.id.the_first_triple);
        fourth = view.findViewById(R.id.the_fourth_group);
        otherGroup = view.findViewById(R.id.other_group);
        setCheckBoxSelected();
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
                Integer integer;
                integer = findNumbers();
                Message message = new Message();
                if (integer == null) {
                    String numberGroup = otherGroup.getText().toString().trim();
                    if (numberGroup.length() > 0) {
                        AppSharePreferenceMgr.put(SystemSettingsActivity.this, "numberGroup", numberGroup);
                        message.obj = numberGroup + "";
                    } else {
                        Toast.makeText(SystemSettingsActivity.this, "联数不能为空", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    AppSharePreferenceMgr.put(SystemSettingsActivity.this, "numberGroup", integer + "");
                    message.obj = integer + "";
                }
                message.what = 10;
                handler.sendMessage(message);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    /**
     * 提示消息
     */
    private void setMsgDialog(SignCompleteStatus signCompleteStatus) {
        //终端号
        final Dialog dialog = new Dialog(this, R.style.NormalDialogStyle);
        View view = View.inflate(this, R.layout.activity_dialog_msg_view, null);
        TextView cancel = view.findViewById(R.id.ordersure_cancel);
        TextView confirm = view.findViewById(R.id.ordersure_confirm);
        TextView msg = view.findViewById(R.id.msg);
        msg.setText(signCompleteStatus.getMsg());
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
                dialog.dismiss();
            }
        });
        if (firsttime == 0l) {
            signTime.setText("您还没有签到");
        } else {
            signTime.setText("上次签到时间为：" + UtilsTime.setTime(firsttime));
        }
        dialog.show();
    }

    /**
     * 看看那个是被选中的
     *
     * @return
     */
    private Integer findNumbers() {
        if (first.isChecked()) {
            return 1;
        }
        if (second.isChecked()) {
            return 2;
        }
        if (triple.isChecked()) {
            return 3;
        }
        if (fourth.isChecked()) {
            return 4;
        }
        return null;
    }

    /**
     * 设置设置checkbox的选择
     */
    private void setCheckBoxSelected() {
        first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                first.setChecked(true);
                second.setChecked(false);
                triple.setChecked(false);
                fourth.setChecked(false);
            }
        });
        second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                first.setChecked(false);
                second.setChecked(true);
                triple.setChecked(false);
                fourth.setChecked(false);
            }
        });
        triple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                first.setChecked(false);
                second.setChecked(false);
                triple.setChecked(true);
                fourth.setChecked(false);
            }
        });
        fourth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                first.setChecked(false);
                second.setChecked(false);
                triple.setChecked(false);
                fourth.setChecked(true);
            }
        });
        otherGroup.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (start == 0) {
                    changeCheckBox();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }


    private void changeCheckBox() {
        first.setChecked(false);
        second.setChecked(false);
        triple.setChecked(false);
        fourth.setChecked(false);
    }

    /**
     * 签到
     */
    private void initPosInfo() {
        Intent intent = new Intent("sunmi.payment.L3");
        String transId = System.currentTimeMillis() + "";
        intent.putExtra("transId", transId);
        intent.putExtra("transType", 8);
        intent.putExtra("appId", getPackageName());
        //判断intent是否存在
        if (IsIntentExite.isIntentExisting(intent, SystemSettingsActivity.this)) {
            startActivity(intent);
            AppSharePreferenceMgr.put(SystemSettingsActivity.this, "transType", "8");
            int signtime = (int) AppSharePreferenceMgr.get(SystemSettingsActivity.this, "signtime", 1);
            if (signtime == 1) {
                firsttime = System.currentTimeMillis();
                AppSharePreferenceMgr.put(SystemSettingsActivity.this, "firsttime", firsttime);
                AppSharePreferenceMgr.put(SystemSettingsActivity.this, "signtime", 2);
            } else if (signtime == 2) {
                firsttime = (long) AppSharePreferenceMgr.get(SystemSettingsActivity.this, "firsttime", 0l);
                secondtime = System.currentTimeMillis();
                AppSharePreferenceMgr.put(SystemSettingsActivity.this, "firsttime", secondtime);
                AppSharePreferenceMgr.put(SystemSettingsActivity.this, "signtime", 2);
            }
        } else {
            Toast.makeText(this, "此机器上没有安装L3应用", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 接收到签到返回来的信息
     *
     * @param scs
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void signCompleteStatus(SignCompleteStatus scs) {
        //接收到清空购车的信息了
        Message message = new Message();
        message.what = 11;
        message.obj = scs;
        handler.sendMessage(message);
    }
}
