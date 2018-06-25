package com.zerone.store.shopingtimetest.Activity.login;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.zerone.store.shopingtimetest.Activity.productlsit.OrderListActvity;
import com.zerone.store.shopingtimetest.BaseActivity.BaseAppActivity;
import com.zerone.store.shopingtimetest.Bean.UserInfo;
import com.zerone.store.shopingtimetest.Bean.login.Account;
import com.zerone.store.shopingtimetest.Contants.IpConfig;
import com.zerone.store.shopingtimetest.DB.impl.AccountInfoDao;
import com.zerone.store.shopingtimetest.DB.impl.UserInfoImpl;
import com.zerone.store.shopingtimetest.R;
import com.zerone.store.shopingtimetest.Utils.AppSharePreferenceMgr;
import com.zerone.store.shopingtimetest.Utils.IsIntentExite;
import com.zerone.store.shopingtimetest.Utils.LoadingUtils;
import com.zerone.store.shopingtimetest.Utils.NetUtils;
import com.zerone.store.shopingtimetest.Utils.NetworkUtil;
import com.zyao89.view.zloading.ZLoadingDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by on 2018/3/29 0029 10 28.
 * Author  LiuXingWen
 */

public class LoginActivity_ extends BaseAppActivity {
    private ZLoadingDialog loading_dailog;
    private EditText username;
    private EditText password;
    private LoginActivity_ mContext;
    private AccountInfoDao accountInfoDao;
    private CheckBox login_rember_account;
    private boolean checkboolen = false;
    private ImageView showpassword;
    private boolean showBoolean = false;
    private TextView agreement;
    private ImageView closeactivity;
    private boolean remberChecked = true;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    String loginJson = (String) msg.obj;
                    loading_dailog.dismiss();
                    try {
                        JSONObject jsonObject = new JSONObject(loginJson);
                        Log.i("URL", "jsonObject=" + jsonObject);
                        int status = jsonObject.getInt("status");
                        if (status == 0) {
                            //失败
                            String errormsg = jsonObject.getString("msg");
                            customDialog(errormsg);
                        } else if (status == 1) {
                            JSONObject data = jsonObject.getJSONObject("data");
                            String uuid = data.getString("uuid");
                            UserInfo userInfo = new UserInfo();
                            userInfo.setUuid(data.getString("uuid"));
                            userInfo.setOrganization_id(data.getString("organization_id"));
                            userInfo.setAccount_id(data.getString("account_id"));
                            userInfo.setAccount(data.getString("account"));
                            userInfo.setRealName(data.getString("realname"));
                            userInfo.setOrganization_name(data.getString("organization_name"));
                            userInfo.setFansnamage_id(data.getString("fansnamage_id"));
                            saveUserInfo(userInfo);
                            //记住用账号
                            Account account = new Account();
                            account.setAccount_name(username.getText().toString());
                            account.setAccount_pwd(password.getText().toString());
                            if (remberChecked) {
                                saveAccountIntoTable(account);
                            } else {
                                clearAccount();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    if (showBoolean) {
                        password.setInputType(InputType.TYPE_CLASS_TEXT);
                    } else {
                        password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                    }
                    break;
                case 511:
                    VolleyError error = (VolleyError) msg.obj;
                    if (error != null) {
                        if (error instanceof TimeoutError) {
                            Toast.makeText(LoginActivity_.this, "网络请求超时，请重试！", Toast.LENGTH_SHORT).show();
                            loading_dailog.dismiss();
                            return;
                        }
                        if (error instanceof ServerError) {
                            Toast.makeText(LoginActivity_.this, "服务器异常", Toast.LENGTH_SHORT).show();
                            loading_dailog.dismiss();
                            return;
                        }
                        if (error instanceof NetworkError) {
                            Toast.makeText(LoginActivity_.this, "请检查网络", Toast.LENGTH_SHORT).show();
                            loading_dailog.dismiss();
                            return;
                        }
                        if (error instanceof ParseError) {
                            Toast.makeText(LoginActivity_.this, "数据格式错误", Toast.LENGTH_SHORT).show();
                            loading_dailog.dismiss();
                            return;
                        }
                        Toast.makeText(LoginActivity_.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        loading_dailog.dismiss();
                    }
                    break;
            }
        }
    };
    private LinearLayout btn_layout_rember;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);
        mContext = LoginActivity_.this;
        remberChecked = (boolean) AppSharePreferenceMgr.get(mContext, "remberChecked", false);
        //页面全屏显示
        initPosInfo();
        initview();
        action();
    }

    private void initPosInfo() {
        Intent intent = new Intent("sunmi.payment.L3");
        String transId = System.currentTimeMillis() + "";
        intent.putExtra("transId", transId);
        intent.putExtra("transType", 13);
        intent.putExtra("appId", getPackageName());
        //判断intent是否存在
        if (IsIntentExite.isIntentExisting(intent, LoginActivity_.this)) {
            startActivity(intent);
            AppSharePreferenceMgr.put(LoginActivity_.this, "transType", "13");
        } else {
            Toast.makeText(this, "此机器上没有安装L3应用", Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * 控件的点击事件处理
     */
    private void action() {
        showpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBoolean = !showBoolean;
                Message message = new Message();
                message.what = 2;
                handler.sendMessage(message);
            }
        });
        closeactivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.finish();
            }
        });
        login_rember_account.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                AppSharePreferenceMgr.put(mContext, "remberChecked", isChecked);
            }
        });
    }
    private void initview() {
        closeactivity = (ImageView) findViewById(R.id.closeactivity);
        btn_layout_rember = (LinearLayout) findViewById(R.id.btn_layout_rember);
        agreement = (TextView) findViewById(R.id.agreement);
        accountInfoDao = new AccountInfoDao(mContext);
        showpassword = (ImageView) findViewById(R.id.showpassword);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        login_rember_account = (CheckBox) findViewById(R.id.login_rember_account);
        login_rember_account.setChecked(remberChecked);
        try {
            if (accountInfoDao != null) {
                Account account = accountInfoDao.getAccount("10");
                username.setText(account.getAccount_name());
                password.setText(account.getAccount_pwd());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        findViewById(R.id.loginbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intoLoginAction();
            }
        });

        btn_layout_rember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login_rember_account.setChecked(!login_rember_account.isChecked());
            }
        });
    }
    private void intoLoginAction() {
        String user = username.getText().toString();
        String pwd = password.getText().toString();
        Map<String, String> loginMap = new HashMap<String, String>();
        loginMap.put("account", user);
        loginMap.put("password", pwd);
        //终端号
        String terminalId = (String) AppSharePreferenceMgr.get(LoginActivity_.this, "terminalId", "");
        //pos商户号
        String merchantId = (String) AppSharePreferenceMgr.get(LoginActivity_.this, "merchantId", "");
        //POS机终端号
        if (terminalId == null && merchantId == null) {
            Toast.makeText(LoginActivity_.this, "该机器没有绑定这个账户，请联系客服。", Toast.LENGTH_SHORT).show();
            return;
        }
        loginMap.put("terminal_num", terminalId);
        //POS商户号
        loginMap.put("sft_pos_num", merchantId);
        if (!NetworkUtil.isNetworkAvailable(mContext)) {
            Toast.makeText(mContext, "网络不可用，请检查", Toast.LENGTH_SHORT).show();
            return;
        }
        loading_dailog = LoadingUtils.getDailog(mContext, Color.RED, "登录中。。。。");
        loading_dailog.show();
        NetUtils.netWorkByMethodPost(mContext, loginMap, IpConfig.URL_LOGIN, handler, 0);
    }
    /**
     * 自定义对话框
     */
    private void customDialog(String msg) {
        final Dialog dialog = new Dialog(this, R.style.NormalDialogStyle);
        View view = View.inflate(this, R.layout.activity_dialog_view, null);
        TextView cancel = view.findViewById(R.id.cancel);
        TextView confirm = view.findViewById(R.id.confirm);
        TextView errormsg = view.findViewById(R.id.errormsg);
        errormsg.setText(msg);
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
        dialog.show();
    }
    /**
     * 保存账号信息到数据库 用来做返显
     *
     * @param account
     */
    private void saveAccountIntoTable(Account account) {
        try {
            Account ac = accountInfoDao.getAccount("10");
            if (ac == null) {
                accountInfoDao.saveAccount(account);
            } else {
                accountInfoDao.deltable();
                accountInfoDao.saveAccount(account);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 清楚数据库中的表
     */
    private void clearAccount() {
        try {
            accountInfoDao.deltable();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 保存用户信息
     */
    private void saveUserInfo(UserInfo userInfo) {
        UserInfoImpl userimpl = new UserInfoImpl(mContext);
        try {
            UserInfo user = userimpl.getUserInfo("10");
            if (user == null) {
                Log.i("URL", "第一次向数据库里填写东西");
                userimpl.saveUserInfo(userInfo);
            } else {
                userimpl.deltable();
                userimpl.saveUserInfo(userInfo);
            }
            Intent intent = new Intent(mContext, OrderListActvity.class);
            startActivity(intent);
            LoginActivity_.this.finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
